/*
 * Define the different methods and attributes for an item rss
 */
package rssdroid.proyecto.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlRootElement(name = "FeedItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedItem {
    
    private String url;
    private FeedChannel channel;
    private String title;
    private String pubDate;
    private String content;
    private String author;
    private int itemid;
    
    public FeedItem(String url,String title,String pubDate,FeedChannel channel){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
    }
    
    public FeedItem(String url,String title,String pubDate,FeedChannel channel,String content){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
        this.content=content;
    }
    
    public FeedItem(String url,String title,String pubDate,FeedChannel channel,String content,int itemid){
        this.url=url;
        this.title=title;
        this.pubDate=pubDate;
        this.channel=channel;
        this.content=content;
        this.itemid=itemid;
    }
    
    public void setItemid(int id){
        this.itemid=id;
    }
    
    public int getItemId(){
        return this.itemid;
    }
    
    public FeedItem(){}
    
    public String getUrl(){
        return this.url;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate=pubDate;
    }
    
    public FeedChannel getChannelUrl(){
        return this.channel;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setChannel(FeedChannel channel) {
        this.channel=channel;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPubDate(){
        return this.pubDate;
    }
    
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

    public String getTitle() {
        return title;
    }
    
    public void setUrl(String url){
        this.url=url;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
