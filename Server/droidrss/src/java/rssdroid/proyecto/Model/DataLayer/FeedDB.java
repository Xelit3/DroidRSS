/*
 * Class to define de database access layer to connect with mysql and insert,
 * update, delete and get data from the feeds rss
 */
package rssdroid.proyecto.Model.DataLayer;

import rssdroid.proyecto.Model.DataLayer.BDConnect;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.Date;
import rssdroid.proyecto.Model.FeedChannel;
import rssdroid.proyecto.Model.ParseChannel;
import rssdroid.proyecto.Model.User;
import rssdroid.proyecto.Model.FeedItem;


public class FeedDB {
    
    private Connection conn;
    private static Logger logger;
    private FileHandler handler;
    private static String fileName="/var/log/feeddb/feeddb.log";
	
    public FeedDB(){
        conn=null;
        try{
            Class.forName(BDConnect.DRIVER);
            conn=DriverManager.getConnection(BDConnect.BD_URL,BDConnect.USER,BDConnect.PASSWORD);
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        //setupLogger();
    }
    
    //return a list of channels to subscribe based on the
    // channels subscribed by the user followings
    public List<FeedChannel> getSubscriptions(User user){
        String subsurl="select * from rss_subscription where id_user = ?";
        String channels;
        String title;
        String url;
        String description;
        List<FeedChannel> subscriptions=new ArrayList();
        String channelUrl;
        try{
            PreparedStatement pst = conn.prepareStatement(subsurl);
            pst.setString(1,user.getUsername());
            ResultSet res = pst.executeQuery();
            while(res.next()){
                channelUrl=res.getString("id_feed_channel");
                channels="select title,description from feed_channel where url = ?";
                PreparedStatement pstChannel = conn.prepareStatement(channels);
                pstChannel.setString(1, channelUrl);
                ResultSet channelRes = pstChannel.executeQuery();
                if(channelRes.next()){
                    title=channelRes.getString("title");
                    description=channelRes.getString("description");
                    subscriptions.add(new FeedChannel(title,description,channelUrl));
                }
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return subscriptions;
        }
    }
    
    //Return a list of the newer feed items from the user subscription
    public List<FeedItem> getLastFeedItems(User user,Date date){
        List<FeedChannel> channels = user.getSubscriptions();
        List<FeedItem> items = new ArrayList<FeedItem>();
        FeedChannel temp;
        String url="";
        String title="";
        String pubDate="";
        String content="";
        String author="";
        int itemid;
        String dateUpdate=String.format("%tFT%<tT", date.getTime());
        String itemsSQL="select * from feed_item where channel_url = ? and pub_date >= ? order by pub_date desc";
        if(channels.size()>0){
            Iterator it = channels.iterator();
            try {
                PreparedStatement pst = conn.prepareStatement(itemsSQL);
                while(it.hasNext()){
                    temp=(FeedChannel) it.next();
                    pst.setString(1,temp.getUrl());
                    pst.setString(2,dateUpdate);
                    ResultSet feedItemRes = pst.executeQuery();
                    while(feedItemRes.next()){
                        url=feedItemRes.getString("url");
                        title=feedItemRes.getString("title");
                        pubDate=feedItemRes.getString("pub_date");
                        content=feedItemRes.getString("content");
                        author=feedItemRes.getString("author");
                        itemid=feedItemRes.getInt("id");
                        items.add(new FeedItem(url,title,pubDate,temp,content,itemid));
                    }
                }
            } catch (SQLException e) {
                //logger.log(Level.INFO,e.getMessage());
                e.printStackTrace();
            }
        }
        Collections.sort(items, new Comparator<FeedItem>() {
            public int compare(FeedItem o1, FeedItem o2) {
                return o2.getPubDateObject().compareTo(o1.getPubDateObject());
            }
        });
        return items;
    }
    
    
    //Return a list of the newer feed items from the user subscription
    public List<FeedItem> getLastFeedItems(User user){
        List<FeedChannel> channels = user.getSubscriptions();
        List<FeedItem> items = new ArrayList<FeedItem>();
        FeedChannel temp;
        String url="";
        String title="";
        String pubDate="";
        String content="";
        String author="";
        int itemid;
        String itemsSQL="select * from feed_item where channel_url = ? order by pub_date desc limit 4";
        if(channels.size()>0){
            Iterator it = channels.iterator();
            try {
                PreparedStatement pst = conn.prepareStatement(itemsSQL);
                while(it.hasNext()){
                    temp=(FeedChannel) it.next();
                    pst.setString(1,temp.getUrl());
                    ResultSet feedItemRes = pst.executeQuery();
                    while(feedItemRes.next()){
                        url=feedItemRes.getString("url");
                        title=feedItemRes.getString("title");
                        pubDate=feedItemRes.getString("pub_date");
                        content=feedItemRes.getString("content");
                        author=feedItemRes.getString("author");
                        itemid=feedItemRes.getInt("id");
                        items.add(new FeedItem(url,title,pubDate,temp,content,itemid));
                    }
                }
            } catch (SQLException e) {
                //logger.log(Level.INFO,e.getMessage());
                e.printStackTrace();
            }
        }
        Collections.sort(items, new Comparator<FeedItem>() {
            public int compare(FeedItem o1, FeedItem o2) {
                return o2.getPubDateObject().compareTo(o1.getPubDateObject());
            }
        });
        return items;
    }
    
    //Get all the items favorited by the user
    public List<FeedItem> getFavFeedItems(User user){
        List<FeedItem> items = new ArrayList<FeedItem>();
        String url="";
        String title="";
        String pubDate="";
        String content="";
        String author="";
        String channelUrl;
        String channelTitle;
        String channelDesc;
        int itemid;
        String favSQL="select id_feed from user_feed_item_fav where id_user = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(favSQL);
            pst.setString(1,user.getUsername());
            ResultSet favItemsRes = pst.executeQuery();
            while(favItemsRes.next()){
                String itemSQL="select item.id,item.channel_url,item.url,item.title,item.pub_date,item.content,item.author,"
                        + "channel.title channel_title,channel.description channel_desc from feed_item item,feed_channel channel where "
                        + "item.id = ? and item.channel_url=channel.url;";
                PreparedStatement itempst = conn.prepareStatement(itemSQL);
                itempst.setInt(1,Integer.parseInt(favItemsRes.getString("id_feed")));
                ResultSet itemsRes = itempst.executeQuery();
                if(itemsRes.next()){
                    url=itemsRes.getString("url");
                    title=itemsRes.getString("title");
                    pubDate=itemsRes.getString("pub_date");
                    content=itemsRes.getString("content");
                    author=itemsRes.getString("author");
                    itemid=itemsRes.getInt("id");
                    channelUrl=itemsRes.getString("channel_url");
                    channelTitle=itemsRes.getString("channel_title");
                    channelDesc=itemsRes.getString("channel_desc");
                    items.add(new FeedItem(url,title,pubDate,new FeedChannel(channelUrl,channelTitle,channelDesc),content,itemid));
                }
            }
        } 
        catch (SQLException e) {
                //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
            }
        Collections.sort(items, new Comparator<FeedItem>() {
            public int compare(FeedItem o1, FeedItem o2) {
                return o2.getPubDateObject().compareTo(o1.getPubDateObject());
            }
        });
        
        return items;
    }
    
    //Get the last feed items favorited by his followings
    public List<FeedItem> getFollowFeedItems(User user){
        List<FeedItem> items = new ArrayList<FeedItem>();
        List<User> users = user.getFollowings();
        User temp;
        Iterator it = users.listIterator();
        while(it.hasNext()){
            temp= (User) it.next();
            items.addAll(getFavFeedItems(temp));
        }
        
        return items;
    }
    
    //Add new channel to the database
    public int addChannel(FeedChannel channel){
        ParseChannel parser = new ParseChannel(channel);
        boolean valid=parser.parseHeaders();
        int insert=0;
        if(valid){
            String channelSQL ="insert into feed_channel values(?,?,LEFT(?,100))";
            try{
                PreparedStatement pst = conn.prepareStatement(channelSQL);
                pst.setString(1,channel.getUrl());
                pst.setString(2,channel.getTitle());
                pst.setString(3,channel.getDescription());
                insert=pst.executeUpdate();
            }
            catch(SQLIntegrityConstraintViolationException e){
                return insert;
            }
            catch(SQLException e){
                //logger.log(Level.INFO,e.getMessage());
                e.printStackTrace();
            }
        }
        return insert;
    }
    
    //Add new channel subscription to a user
    public int addSubscription(User user,FeedChannel channel){
        int insert=0;
        String subscriptionSQL ="insert into rss_subscription values(?,?)";
        try{
            PreparedStatement pst = conn.prepareStatement(subscriptionSQL);
            pst.setString(1,user.getUsername());
            pst.setString(2,channel.getUrl());
            insert=pst.executeUpdate();
        }
        catch(SQLIntegrityConstraintViolationException e){
            return insert;
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        return insert;
    }
    
    //Delete channel subscription from the user
    public int deleteSubscription(User user,FeedChannel channel){
        int delete=0;
        String subscriptionSQL ="delete from rss_subscription where id_user = ? and id_feed_channel = ?";
        try{
            PreparedStatement pst = conn.prepareStatement(subscriptionSQL);
            pst.setString(1,user.getUsername());
            pst.setString(2,channel.getUrl());
            delete=pst.executeUpdate();
        }
        catch(SQLIntegrityConstraintViolationException e){
            return delete;
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        return delete;
    }
    
    //Return channel object searched by his url
    public FeedChannel searchChannelByUrl(String url){
        String userSQL="select * from feed_channel where url = ?";
        String title,description;
        FeedChannel channel=null;
        try{
            PreparedStatement pst = conn.prepareStatement(userSQL);  
            pst.setString(1, url);
            ResultSet res = pst.executeQuery();
            if(res.next()){
                title=res.getString("title");
                description=res.getString("description");
                channel = new FeedChannel(title,description,url);
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return channel;
        }
    }
    
    //Return a list of channels searched
    public List<FeedChannel> searchChannel(String content){
        String userSQL="select * from feed_channel where title like ? or description like ?";
        String title,description,url;
        List<FeedChannel> channels = new ArrayList<FeedChannel>();
        try{
            PreparedStatement pst = conn.prepareStatement(userSQL);  
            pst.setString(1, "%"+content+"%");
            pst.setString(2, "%"+content+"%");
            ResultSet res = pst.executeQuery();
            while(res.next()){
                title=res.getString("title");
                description=res.getString("description");
                url=res.getString("url");
                FeedChannel channel = new FeedChannel(title,description,url);
                channels.add(channel);
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return channels;
        }
    }
    
     private void setupLogger(){
        try{
            logger = Logger.getLogger(rssdroid.proyecto.Model.DataLayer.FeedDB.class.getCanonicalName());
            logger.setLevel(Level.INFO);
            handler = new FileHandler(fileName,true);
            handler.setFormatter(new XMLFormatter());
            logger.addHandler(handler);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }  
}
