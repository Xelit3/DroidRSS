/* Controller.java
* @author: Xavi Rueda
* @date: 5-16-2013
* @description: Controller for the application DroidRSS
*/

package com.droidrss.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Observer;

import android.content.Context;

import com.droidrss.model.Encrypter;
import com.droidrss.model.FeedChannel;
import com.droidrss.model.FeedItem;
import com.droidrss.model.User;
import com.droidrss.model.dao.DBManager;
import com.droidrss.model.dao.WebServiceConnection;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
public class Controller {

	/** The class to manage the conexion with the WS. */
	private WebServiceConnection conexion;
	
	/** The class to manage the conexion with the internal SQLite DB. */
	private DBManager dbManager;
			
	/**
	 * Instantiates a new controller.
	 *
	 * @param context the context
	 */
	public Controller(Context context){
		this.conexion = new WebServiceConnection();
		this.dbManager = new DBManager(context);
	}
	
	/**
	 * Adds the observer.
	 *
	 * @param o the o
	 */
	public void addObserver(Observer o){
		conexion.addObserver(o);
	}
	
	/**
	 * Creates the new account.
	 *
	 * @param user the user
	 */
	public void createNewAccount(User user){
		conexion.createUser(user);
		setUserinDB(user);
	}

	/**
	 * Login user.
	 *
	 * @param user the user
	 * @param password the password
	 */
	public void loginUser(String user, String password) {
		String token = null;
		try {
			token = Encrypter.getHash(user + Encrypter.getHash(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		conexion.connectUser(user, token);
	}

	/**
	 * Gets the feeds from WS.
	 *
	 * @param type the type
	 * @return the feeds
	 */
	public List<FeedItem> getFeeds(int type){
		User current = dbManager.getUser();
		
		return conexion.getFeedItems(current.getUsername(), current.getToken(), type);
	}

	/**
	 * Sets the user into SQLite DB.
	 *
	 * @param user the new userin db
	 */
	public void setUserinDB(User user) {
		dbManager.insert(user);
	}
	
	/**
	 * Gets the user from DB.
	 *
	 * @return the user
	 */
	public User getUser() {
		return dbManager.getUser();
	}
	
	/**
	 * Gets the user from WS.
	 *
	 * @return the user from ws
	 */
	public User getUserFromWS(){
		User toGet = dbManager.getUser();
		
		return conexion.connectUser(toGet.getUsername(), toGet.getToken());
	}
	
	/**
	 * Delete following.
	 *
	 * @param usrFollowing the user following to delete
	 * @param token the token
	 */
	public void deleteFollowing(String usrFollowing, String token){
		conexion.deleteFollowing(usrFollowing, token);
	}
	
	/**
	 * Delete subscription.
	 *
	 * @param channels the channels
	 * @param user the user
	 * @return the int
	 */
	public int deleteSubscription(List<FeedChannel> channels,User user){
		return conexion.deleteSubscription(channels, user);
	}
	
	/**
	 * Adds the channel.
	 *
	 * @param url the url
	 * @param token the token
	 * @return the int
	 */
	public int addChannel(String url,String token){
		return conexion.addChannel(url,token);
	}
	
	/**
	 * Search channel.
	 *
	 * @param search the search
	 * @param token the token
	 * @return the list
	 */
	public List<FeedChannel> searchChannel(String search,String token){
		return conexion.getSearchChannels(search,token);
	}
	
	/**
	 * Adds the subscription.
	 *
	 * @param channel the channel
	 * @param user the user
	 * @return the int
	 */
	public int addSubscription(FeedChannel channel, User user){
		return conexion.addSubscription(channel,user);
	}
	
	/**
	 * Gets the suggestions.
	 *
	 * @param user the user
	 * @return the suggestions
	 */
	public List<FeedChannel> getSuggestions(User user){
		return conexion.getSuggestions(user);
	}
	
	/**
	 * Gets the users by search.
	 *
	 * @param userref the userref
	 * @param token the token
	 * @return the users by search
	 */
	public List<User> getUsersBySearch(String userref, String token){
		return conexion.getUsersBySearch(userref, token);
	}

	/**
	 * Adds the following.
	 *
	 * @param username the username
	 * @param token the token
	 */
	public void addFollowing(String username, String token) {
		conexion.addFollow(username, token);
	}
}
