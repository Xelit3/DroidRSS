/* FeedItem.java
* @author: Xavi Rueda, Iván Mora
* @date: 5-10-2013
* @description: FeedItem class
*/
package com.droidrss.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedItem.
 */
public class FeedItem implements Parcelable{
    
    /** The url. */
    private String url;
    
    /** The channel. */
    private FeedChannel channel;
    
    /** The title. */
    private String title;
    
    /** The pub date. */
    private String pubDate;
    
    /** The content. */
    private String content;
    
    /** The author. */
    private String author;
    
    /** The itemid. */
    private int itemid;
    
    /**
     * Instantiates a new feed item.
     */
    public FeedItem(){}
    
    /**
     * Instantiates a new feed item.
     *
     * @param in the in
     */
    public FeedItem(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        
        this.title = data[0];
        this.author = data[1];
        this.pubDate = data[2];
        this.content = data[3];
        this.url = data[4];
    }
    
    /**
     * Instantiates a new feed item.
     *
     * @param url the url
     * @param title the title
     * @param pubDate the pub date
     * @param channel the channel
     */
    public FeedItem(String url,String title,String pubDate,FeedChannel channel){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
    }
    
    /**
     * Instantiates a new feed item.
     *
     * @param url the url
     * @param title the title
     * @param pubDate the pub date
     * @param channel the channel
     * @param content the content
     */
    public FeedItem(String url,String title,String pubDate,FeedChannel channel,String content){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
        this.content=content;
    }
    
    /**
     * Instantiates a new feed item.
     *
     * @param url the url
     * @param title the title
     * @param pubDate the pub date
     * @param channel the channel
     * @param content the content
     * @param itemid the itemid
     */
    public FeedItem(String url,String title,String pubDate,FeedChannel channel,String content,int itemid){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
        this.content=content;
        this.itemid=itemid;
    }
    
    /**
     * Sets the itemid.
     *
     * @param id the new itemid
     */
    public void setItemid(int id){
        this.itemid=id;
    }
    
    /**
     * Gets the item id.
     *
     * @return the item id
     */
    public int getItemId(){
        return this.itemid;
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
     * Sets the pub date.
     *
     * @param pubDate the new pub date
     */
    public void setPubDate(String pubDate) {
        this.pubDate=pubDate;
    }
    
    /**
     * Gets the channel url.
     *
     * @return the channel url
     */
    public FeedChannel getChannelUrl(){
        return this.channel;
    }

    /**
     * Gets the author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     *
     * @param author the new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the channel.
     *
     * @param channel the new channel
     */
    public void setChannel(FeedChannel channel) {
        this.channel=channel;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content the new content
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Gets the pub date.
     *
     * @return the pub date
     */
    public String getPubDate(){
        return this.pubDate;
    }
    
    /**
     * Gets the pub date object.
     *
     * @return the pub date object
     */
    @SuppressLint("SimpleDateFormat")
	public Date getPubDateObject(){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date myDate=sdf.parse(this.pubDate);
            return myDate;
        }
        catch(ParseException e){
            return new Date();
        }
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
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
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {this.title,
                							this.author,
                							this.pubDate,
                							this.content,
                							this.url});
	}
	
	/** The Constant CREATOR. */
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in); 
        }

        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };
}
