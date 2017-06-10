/*
 * This class is a controller to interact with the database code layer related with
 * the feeds rss
 */
package rssdroid.proyecto.Controller;

import java.util.Date;
import java.util.List;
import rssdroid.proyecto.Model.DataLayer.FeedDB;
import rssdroid.proyecto.Model.FeedChannel;
import rssdroid.proyecto.Model.FeedItem;
import rssdroid.proyecto.Model.User;

public class FeedManager {
    
    private FeedDB feeddb;
    
    public FeedManager(){
        feeddb= new FeedDB();
    }
    
    //Return a list of the last feed items newer than the date passed
    public List<FeedItem> getLastFeedItems(User user,Date date){
        return feeddb.getLastFeedItems(user, date);
    }
    
    //Return a list of the newer feed items from the user subscription
    public List<FeedItem> getLastFeedItems(User user){
        return feeddb.getLastFeedItems(user);
    }
    
    //Add new channel to the database
    public int addChannel(FeedChannel channel){
        return feeddb.addChannel(channel);
    }
    
    //Add new channel subscription to a user
    public int addSubscription(User user, FeedChannel channel){
        return feeddb.addSubscription(user, channel);
    }
    
    //Delete channel subscription from the user
    public int deleteSubscription(User user, FeedChannel channel){
        return feeddb.deleteSubscription(user, channel);
    }
    
    //Return channel object searched by his url
    public FeedChannel searchChannelByUrl(String url){
        return feeddb.searchChannelByUrl(url);
    }
    
    //Return a list of channels searched
    public List<FeedChannel> searchChannel(String content){
        return feeddb.searchChannel(content);
    }
    
    //Get all the items favorited by the user
    public List<FeedItem> getFavFeedItems(User user){
        return feeddb.getFavFeedItems(user);
    }
    
    //Get the last feed items favorited by his followings
    public List<FeedItem> getFollowFeedItems(User user){
         return feeddb.getFollowFeedItems(user);
     }
}
