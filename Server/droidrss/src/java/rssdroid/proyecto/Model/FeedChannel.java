/*
 * Define the different methods and attributes to define a channel rss
 */
package rssdroid.proyecto.Model;


import javax.xml.bind.annotation.*;

@XmlRootElement (name="channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedChannel {
    @XmlElement (name = "title")
    private String channelTitle;
    @XmlElement (name = "description")
    private String channelDesc;
    @XmlElement (name = "url")
    private String url;
    
    public FeedChannel(String title,String description,String url){
        this.channelTitle=title;
        this.channelDesc=description;
        this.url=url;
    }
    
    public FeedChannel() {}
    
    public FeedChannel(String url){
        this.url=url;
    }
    
    public String getTitle(){
        return this.channelTitle;
    }
    
    public String getUrl(){
        return this.url;
    }
    
    public void setUrl(String url){
        this.url=url;
    }
    
    public String getDescription(){
        return this.channelDesc;
    }
    
    public void setDescription(String description){
        this.channelDesc=description;
    }
    
    public void setTitle(String title){
        this.channelTitle=title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeedChannel other = (FeedChannel) obj;
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }
    
    
    
}
