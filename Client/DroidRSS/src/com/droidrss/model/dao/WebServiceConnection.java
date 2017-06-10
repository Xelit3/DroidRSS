/* WebServiceConnection.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: This class provides methods to connect the application to the REST WebService
*/

package com.droidrss.model.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.droidrss.model.FeedChannel;
import com.droidrss.model.FeedItem;
import com.droidrss.model.FeedItemParser;
import com.droidrss.model.FeedChannelParser;
import com.droidrss.model.User;
import com.droidrss.model.UserParser;

/**
 * The Class WebServiceConnection.
 */
public class WebServiceConnection extends Observable{
	
	/** The thread aux. */
	private Thread threadAux;
    
    /** The observers. */
    private List<Observer> observers = new LinkedList<Observer>();
    
    /** A parser for User XML. */
    private UserParser userParser;
    
    /** URLs to connect. */
    private static String PATH_IP = "http://192.168.21.130:8084";
    
	private static String PATH_TO_LOGIN = PATH_IP + "/droidrss/user/%s?token=%s";
	private static String PATH_TO_ADD = PATH_IP + "/droidrss/user/%s";
	private static String PATH_GET_FEEDS = PATH_IP + "/droidrss/feed/%s?token=%s";
	private static String PATH_GET_SUGGESTIONS = PATH_IP + "/droidrss/feed/%s/suggestions?token=%s";
	private static String PATH_GET_FAV_FEEDS = PATH_IP + "/droidrss/feed/%s/favorites?token=%s";
	private static String PATH_GET_FO_FAV_FEEDS = PATH_IP + "/droidrss/feed/%s/follow?token=%s";
	private static String PATH_REMOVE_FOLLOW = PATH_IP + "/droidrss/user/%s/follow?token=%s";
	private static String PATH_SEARCH_CHANNEL = PATH_IP + "/droidrss/feed/channel?search=%s&token=%s";
	private static String PATH_ADD_SUBSCRIPTION = PATH_IP + "/droidrss/feed/%s/channel?token=%s";
	private static String PATH_ADD_CHANNEL = PATH_IP + "/droidrss/feed/channel?token=%s";
	private static String PATH_DEL_SUBSCRIPTION = PATH_IP + "/droidrss/feed/%s/channel?token=%s&url=%s";
	private static String PATH_ADD_FOLLOW = PATH_IP + "/droidrss/user/%s/follow?token=%s";
	private static String PATH_SEARCH_USER = PATH_IP + "/droidrss/user/%s/search?token=%s";
	
	/** The path to add. */
	List<User> users = null;
	List<FeedItem> feedItems = null;
	List<FeedChannel> channels= null;
	
	/** The user */
	private User obtainedUser = null;
	
	private int code = 0;
	
	/**
	 * Instantiates a new web service connection.
	 */
	public WebServiceConnection() {	
		userParser = new UserParser();		
	}
	
	
	/**
	 * Connect user.
	 *
	 * @param user the user
	 * @param token the user encrypted token
	 * @return the user
	 */
	public User connectUser(final String user, final String token){
		threadAux = new Thread (new Runnable(){
			@Override
			public void run() {
				URL url;
								
				try {
					url = new URL(String.format(PATH_TO_LOGIN, user, token));
					
					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("GET");

					int code = httpCon.getResponseCode();

					StringBuilder xmlData = new StringBuilder();
					
					if(code==200){
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null) {
							xmlData.append(line);
						}

						obtainedUser = userParser.getUserFromXML(xmlData.toString());

					}
										
					httpCon.disconnect();
					
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
					obtainedUser = null;
				} 
				catch (IOException e) {
					e.printStackTrace();
					obtainedUser = null;
				}
				
				if(!observers.isEmpty())
					notifyObservers(obtainedUser);
				
			}
		});
		try {
			threadAux.start();
			threadAux.join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return obtainedUser;
	}
	
	/**
	 * createUser
	 * This method creates a Thread to connect with the WebService and create a new user
	 * @param xmlAccount the xml account
	 * @param user the user
	 */
	public void createUser(final User user){
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {
				String out;
				try {
					if(addUser(user))
						out = "DONE";
					
					else
						out = "Fail";
					
					notifyObservers(out);
				}
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		threadAux.start();
		try {
			threadAux.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * getFeedItems
	 * This method creates a Thread to connect with the WebService and gets the last feeditems
	 * @param user the user
	 * @param token the token
	 * @param date the date
	 * @return the feeds
	 */
	public List<FeedItem> getFeedItems(final String user, final String token, final int type){
		//Types: 1: User feeds | 2: Favorite user feeds | 3: Followers favorite feeds
		
		feedItems = new ArrayList<FeedItem>();
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {

				URL url = null;
				try {
					
					switch(type){
						case 1:
							url = new URL(String.format(PATH_GET_FEEDS, user, token));
							break;
							
						case 2:
							url = new URL(String.format(PATH_GET_FAV_FEEDS, user, token));
							break;
							
						case 3:
							url = new URL(String.format(PATH_GET_FO_FAV_FEEDS, user, token));
							break;
							
						default:
							url = new URL(String.format(PATH_GET_FEEDS, user, token));
							break;
					}
					
					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("GET");

					int code = httpCon.getResponseCode();

					StringBuilder sb = new StringBuilder();

					if(code==200){
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}

						FeedItemParser fiParser = new FeedItemParser(sb.toString());

						feedItems.addAll(fiParser.getFeedItems());

						httpCon.disconnect();

					}
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		try {
			threadAux.start();
			threadAux.join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}	

		return feedItems;
	}
	/**
	 * getFeedItems
	 * This method connects with the WS to delete a following
	 * @param user the user to delete
	 * @param token the token
	 * @return the feeds
	 */
	public void deleteFollowing(final String usrFollowing, final String token){
		feedItems = new ArrayList<FeedItem>();
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(String.format(PATH_REMOVE_FOLLOW, usrFollowing, token));

					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("DELETE");

					httpCon.getResponseCode();
					
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}

			}

		});

		try {
			threadAux.start();
			threadAux.join();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}	
	
	}
	
	/**
	 * Gets the search channels.
	 *
	 * @param search the search
	 * @param token the token
	 * @return the search channels
	 */
	public List<FeedChannel> getSearchChannels(final String search, final String token){
		channels = new ArrayList<FeedChannel>();
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(String.format(PATH_SEARCH_CHANNEL,search, token));

					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("GET");

					int code = httpCon.getResponseCode();

					StringBuilder sb = new StringBuilder();

					if(code==200){
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}

						FeedChannelParser chparser = new FeedChannelParser(sb.toString());

						channels.addAll(chparser.getChannels());

					}
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			threadAux.start();
			threadAux.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	

		return channels;
	}
	
	public List<FeedChannel> getSuggestions(final User user){
		channels = new ArrayList<FeedChannel>();
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(String.format(PATH_GET_SUGGESTIONS,user.getUsername(), user.getToken()));

					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("GET");

					int code = httpCon.getResponseCode();

					StringBuilder sb = new StringBuilder();

					if(code==200){
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}

						FeedChannelParser chparser = new FeedChannelParser(sb.toString());

						channels.addAll(chparser.getChannels());

					}
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			threadAux.start();
			threadAux.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	

		return channels;
	}
	
	public int addSubscription(final FeedChannel channel,final User user){
		threadAux = new Thread (new Runnable(){
			
			@Override
			public void run() {
				URL url;
				try {
					url = new URL(String.format(PATH_ADD_SUBSCRIPTION,user.getUsername(), user.getToken()));
					StringBuilder sb = new StringBuilder();
					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("PUT");
					OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			        sb.append("<channel>");
			        sb.append("<url>");
			        String urlChannel = channel.getUrl();
			        sb.append(urlChannel);
			        sb.append("</url>");
			        sb.append("</channel>");
			        out.write(sb.toString());
			        out.close();
			        code = httpCon.getResponseCode();
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
			threadAux.start();
			return code;
	}
	
	public int deleteSubscription(final List<FeedChannel> channels,final User user){
		threadAux = new Thread (new Runnable(){
			@Override
			public void run() {
				URL url;
				try {
					Iterator<FeedChannel> it = channels.listIterator();
					FeedChannel channel;
					while (it.hasNext()){
						channel = (FeedChannel)it.next();
						url = new URL(String.format(PATH_DEL_SUBSCRIPTION,user.getUsername(), user.getToken(),channel.getUrl()));
						HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
						httpCon.setRequestProperty("Content-Type", "application/xml" );
						httpCon.setRequestMethod("DELETE");
				        code = httpCon.getResponseCode();
					}
				} 
				catch (MalformedURLException e) {
					e.printStackTrace();
				} 
				catch (ProtocolException e) {
					e.printStackTrace();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
			threadAux.start();
			return code;
	}
	
	public int addChannel(final String urlChannel, final String token){
		threadAux = new Thread (new Runnable(){
			@Override
			public void run() {
				try{
					StringBuilder sb =new StringBuilder();
			        URL url = new URL(String.format(PATH_ADD_CHANNEL,token));
			        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			        httpCon.setRequestProperty("Content-Type", "application/xml" );
			        httpCon.setRequestMethod("PUT");
			        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			        sb.append("<channel>");
			        sb.append("<url>");
			        sb.append(urlChannel);
			        sb.append("</url>");
			        sb.append("</channel>");
			        out.write(sb.toString());
			        out.close();
			        code = httpCon.getResponseCode();
			        
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
			threadAux.start();
			return code;
	}
	
	public List<User> getUsersBySearch(final String userref, final String token){
		threadAux = new Thread (new Runnable(){
			@Override
			public void run() {
				URL url;

				try {
					url = new URL(String.format(PATH_SEARCH_USER, userref, token));

					HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
					httpCon.setRequestProperty("Content-Type", "application/xml" );
					httpCon.setRequestMethod("GET");

					code = httpCon.getResponseCode();

					StringBuilder sb = new StringBuilder();

					if(code==200){
						BufferedReader reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}

						userParser = new UserParser();
						userParser.getUserFromXML(sb.toString());

						users = new ArrayList<User>();
						users.addAll(userParser.getUserList());
						
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		threadAux.start();
		
		try {
			threadAux.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public int addFollow(final String userFollow, final String token){
		threadAux = new Thread (new Runnable(){
			@Override
			public void run() {
				try{
					URL url = new URL(String.format(PATH_ADD_FOLLOW, userFollow, token));
			        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			        httpCon.setRequestProperty("Content-Type", "application/xml" );
			        httpCon.setRequestMethod("PUT");
			        
			        code = httpCon.getResponseCode();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		threadAux.start();
			
		return code;
	}
	
	/**
	 * Adds the user.
	 *
	 * @param XmlUserProfile the xml user profile
	 * @param user the user
	 * @return the int
	 * @throws ProtocolException the protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private boolean addUser(User user) throws ProtocolException, IOException{
		boolean done = false;

		URL url = new URL(String.format(PATH_TO_ADD, user));
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Content-Type", "application/xml" );
		httpCon.setRequestMethod("PUT");

		String xmlUserProfile = userParser.createUserXML(user);

		if(xmlUserProfile != null){
			OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			out.write(xmlUserProfile);
			out.close();
		}

		httpCon.getResponseCode();

		done = true;

		return done;
    }
	
	/* (sin Javadoc)
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	public void addObserver(Observer o) {
		observers.add(o);
	}
		
	/* (sin Javadoc)
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	public void notifyObservers(Object o) {
		for (Observer obs: observers) {
			obs.update(this, o);
		}
	}
	
}
