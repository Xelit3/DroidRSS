/* User.java
* @author: Xavi Rueda, Iván Mora
* @date: 5-10-2013
* @description: User class
*/
package com.droidrss.model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 */
public class User implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1390294410558879083L;
	
	/** The username. */
	private String username;
    
    /** The surname. */
    private String surname;
    
    /** The password. */
    private String password;
    
    /** The token. */
    private String token;
    
    /** The mail. */
    private String mail;
    
    /** The subscriptions. */
    private List<FeedChannel> subscriptions;
    
    /** The followers. */
    private List<User> followers;
    
    /** The followings. */
    private List<User> followings;
    
    /**
     * Instantiates a new user.
     */
    public User(){
    	initLists();
    }
    
    /**
     * Instantiates a new user.
     *
     * @param username the username
     * @param token the token
     */
    public User(String username, String token){
        this.username=username;
        this.token=token;
        
        initLists();
    }
    
    /**
     * Instantiates a new user.
     *
     * @param username the username
     * @param surname the surname
     * @param password the password
     * @param mail the mail
     */
    public User(String username, String surname, String password, String mail) {
		this.username = username;
		this.surname = surname;
		this.password = password;
		
		try {
			this.token = Encrypter.getHash(username + Encrypter.getHash(password));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		this.mail = mail;
		
		initLists();
	}
    
    /**
     * Instantiates a new user.
     *
     * @param username the username
     * @param surname the surname
     * @param password the password
     * @param token the token
     * @param mail the mail
     */
    public User(String username, String surname, String password, String token, String mail) {
		this.username = username;
		this.surname = surname;
		this.password = password;
		this.token = token;
		this.mail = mail;
		
		initLists();
	}
    
    /**
     * Inits the lists.
     */
    public void initLists(){
    	this.followers = new ArrayList<User>();
		this.followings = new ArrayList<User>();
		this.subscriptions = new ArrayList<FeedChannel>();
    }

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername(){
        return this.username;
    }
    
    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken(){
        return this.token;
    }
    
    /**
     * Sets the token.
     *
     * @param token the new token
     */
    public void setToken(String token){
        this.token=token;
    }
    
    /**
     * Gets the mail.
     *
     * @return the mail
     */
    public String getMail(){
        return this.mail;
    }
    
    /**
     * Gets the surname.
     *
     * @return the surname
     */
    public String getSurname(){
        return this.surname;
    }
    
    /**
     * Sets the mail.
     *
     * @param mail the new mail
     */
    public void setMail(String mail){
        this.mail=mail;
    }
    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword(){
        return this.password;
    }
    
    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password){
        this.password=password;
    }
    
    /**
     * Sets the surname.
     *
     * @param surname the new surname
     */
    public void setSurname(String surname){
        this.surname=surname;
    }
    
    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username){
        this.username=username;
    }
        
    /**
     * Gets the followers.
     *
     * @return the followers
     */
    public List<User> getFollowers(){
        return this.followers;
    }
    
    /**
     * Sets the followers.
     *
     * @param followers the new followers
     */
    public void setFollowers(List<User> followers){
        this.followers=followers;
    }
    
    /**
     * Gets the followings.
     *
     * @return the followings
     */
    public List<User> getFollowings(){
        return this.followings;
    }
    
    /**
     * Sets the followings.
     *
     * @param followings the new followings
     */
    public void setFollowings(List<User> followings){
        this.followings=followings;
    }
    
    /**
     * Gets the subscriptions.
     *
     * @return the subscriptions
     */
    public List<FeedChannel> getSubscriptions(){
        return this.subscriptions;
    }
    
    /**
     * Sets the subscriptions.
     *
     * @param subscriptions the new subscriptions
     */
    public void setSubscriptions(List<FeedChannel> subscriptions){
        this.subscriptions=subscriptions;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return username + " [" + mail + "]";
	}
}
