/* UserParser.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: User parser for User class
*/
package com.droidrss.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class UserParser.
 */
public class UserParser extends DefaultHandler  {
	
	/** The user f. */
	private User user, userF;
	
	/** The subs channel. */
	private FeedChannel subsChannel;
	
	/** The is subscriptions. */
	private boolean isFollowers = false, isFollowings = false, isSubscriptions = false;
	
	/** The temp. */
	private String temp;
	
	/** The followers. */
	private List<User> followers;
	
	/** The followings. */
	private List<User> followings;
	
	/** The searched. */
	private List<User> searched = new ArrayList<User>();
	
	/** The subscriptions. */
	private List<FeedChannel> subscriptions;

	/**
	 * Creates the user xml.
	 *
	 * @param user the user
	 * @return the string
	 */
	public String createUserXML(User user) {
		String resultXML = null;
		
		try{
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element root = doc.createElement("user");
			
			Element e_surname = doc.createElement("surname");
			Text text = doc.createTextNode(user.getSurname());
			e_surname.appendChild(text);
			root.appendChild(e_surname);
				
			Element e_username = doc.createElement("username");
			text = doc.createTextNode(user.getUsername());
			e_username.appendChild(text);
			root.appendChild(e_username);
			
			Element e_password = doc.createElement("password");
			text = doc.createTextNode(user.getPassword());
			e_password.appendChild(text);
			root.appendChild(e_password);
			
			Element e_mail = doc.createElement("mail");
			text = doc.createTextNode(user.getMail());
			e_mail.appendChild(text);
			root.appendChild(e_mail);
			
			doc.appendChild(root);
			
			TransformerFactory tranFactory = TransformerFactory.newInstance(); 
			Transformer aTransformer = tranFactory.newTransformer(); 
			StringWriter buffer = new StringWriter();
			
			aTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			
			aTransformer.transform(new DOMSource(doc), new StreamResult(buffer));
			
			resultXML = buffer.toString();
			
		}
		catch(ParserConfigurationException pce){
			System.out.println(pce.toString());
		}
		
		catch(TransformerConfigurationException tce){
			System.out.println(tce.toString());
		}
		
		catch(TransformerException te){
			System.out.println(te.toString());
		}
		
		return resultXML;
	}
	
	/**
	 * Gets the user from xml.
	 *
	 * @param xmlData the xml data
	 * @return the user from xml
	 */
	public User getUserFromXML(String xmlData){
		SAXParserFactory spfac = SAXParserFactory.newInstance();

        SAXParser sp;
		try {
			sp = spfac.newSAXParser();
			
			sp.parse(new InputSource(new StringReader(xmlData)),this);
			
		} 
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
		catch (SAXException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

        return user;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		temp = "";
        if (qName.equalsIgnoreCase("user"))
        	user = new User();
        
        else if(qName.equalsIgnoreCase("followers")){
        	isFollowers = true;
        	isFollowings = false;
			isSubscriptions = false;
        	followers = new ArrayList<User>();
        }
        else if(qName.equalsIgnoreCase("followings")){
        	isFollowings = true;
        	isFollowers = false;
			isSubscriptions = false;
        	followings = new ArrayList<User>();
        }
        else if(qName.equalsIgnoreCase("subscriptions")){
        	isSubscriptions = true;
        	isFollowings = false;
			isFollowers = false;
        	subscriptions = new ArrayList<FeedChannel>();
        }
        else if(qName.equalsIgnoreCase("follow")){
        	userF = new User();
        }
        else if(qName.equalsIgnoreCase("channel")){
        	subsChannel = new FeedChannel();
        }
    }
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] buffer, int start, int length) throws SAXException {
		temp = new String(buffer, start, length);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(!isFollowers && !isFollowings && !isSubscriptions){
			if (qName.equalsIgnoreCase("username")) {
				user.setUsername(temp);
	        }
			else if (qName.equalsIgnoreCase("surname")) {
				user.setSurname(temp);
	        }
			else if (qName.equalsIgnoreCase("token")) {
				user.setToken(temp);
	        }
			else if (qName.equalsIgnoreCase("mail")) {
				user.setMail(temp);
	        }
		}
		
		else if(isFollowers){
			if (qName.equalsIgnoreCase("username")) {
				userF.setUsername(temp);
	        }
			if (qName.equalsIgnoreCase("surname")) {
				userF.setSurname(temp);
	        }
			if (qName.equalsIgnoreCase("mail")) {
				userF.setMail(temp);
	        }
			
			if(qName.equalsIgnoreCase("follow")){
				followers.add(userF);
			}
		}
			
		else if(isFollowings){
			if (qName.equalsIgnoreCase("username")) {
				userF.setUsername(temp);
	        }
			if (qName.equalsIgnoreCase("surname")) {
				userF.setSurname(temp);
	        }
			if (qName.equalsIgnoreCase("mail")) {
				userF.setMail(temp);
	        }
			
			if(qName.equalsIgnoreCase("follow")){
				followings.add(userF);
			}
		}
				
		else if(isSubscriptions){
			if (qName.equalsIgnoreCase("title")) {
				subsChannel.setTitle(temp);
	        }
			if (qName.equalsIgnoreCase("description")) {
				subsChannel.setDescription(temp);
	        }
			if (qName.equalsIgnoreCase("url")) {
				subsChannel.setUrl(temp);
	        }
			
			if(qName.equalsIgnoreCase("channel")){
				subscriptions.add(subsChannel);
			}
		}
		
		if (qName.equalsIgnoreCase("followers")) {
			user.setFollowers(followers);
			isFollowers = false;
        }
		if (qName.equalsIgnoreCase("followings")) {
			user.setFollowings(followings);
			isFollowings = false;
        }
		if (qName.equalsIgnoreCase("subscriptions")) {
			user.setSubscriptions(subscriptions);
			isSubscriptions = false;
        }
		
		if (qName.equalsIgnoreCase("user")) {
			searched.add(user);
        }
		
	}
	
	/**
	 * Gets the user list.
	 *
	 * @return the user list
	 */
	public List<User> getUserList(){
		return searched;
	}
	
}
