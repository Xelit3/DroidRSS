/* FeedChannel.java
* @author: Xavi Rueda, Iván Mora
* @date: 5-10-2013
* @description: FeedChannel class
*/
package com.droidrss.model;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedChannel.
 */
public class FeedChannel implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7289864568310506644L;
	
	/** The channel title. */
	private String channelTitle;
    
    /** The channel desc. */
    private String channelDesc;
    
    /** The url. */
    private String url;
    
    /**
     * Instantiates a new feed channel.
     *
     * @param title the title
     * @param description the description
     * @param url the url
     */
    public FeedChannel(String title,String description,String url){
        this.channelTitle=title;
        this.channelDesc=description;
        this.url=url;
    }
    
    /**
     * Instantiates a new feed channel.
     */
    public FeedChannel() {}
    
    /**
     * Instantiates a new feed channel.
     *
     * @param url the url
     */
    public FeedChannel(String url){
        this.url=url;
    }
    
    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle(){
        return this.channelTitle;
    }
    
    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl(){
        return this.url;
    }
    
    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(String url){
        this.url=url;
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription(){
        return this.channelDesc;
    }
    
    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description){
        this.channelDesc=description;
    }
    
    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title){
        this.channelTitle=title;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return channelTitle;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedChannel other = (FeedChannel) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
    
}
