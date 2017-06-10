/*
 * This class is used to define the different methods to update and get data
 * from the feeds subscriptions by the users
 */
package rssdroid.proyecto.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import rssdroid.proyecto.Controller.FeedManager;
import rssdroid.proyecto.Controller.UserManager;
import rssdroid.proyecto.Model.FeedChannel;
import rssdroid.proyecto.Model.FeedItem;
import rssdroid.proyecto.Model.FeedItems;
import rssdroid.proyecto.Model.User;

//Main url access
@Path("feed")
public class FeedRest {

    private UserManager usermanager;
    private FeedManager feedmanager;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FeedRest
     */
    public FeedRest() {
        usermanager=new UserManager();
        feedmanager = new FeedManager();
    }

    /**
     * Return a list of feeditem newer than the date passed by parameter
     * @return an instance of FeedItems
     */
    /*@GET
    @Produces("application/xml")
    @Path("{username}")
    public FeedItems getFeedItems(@PathParam("username") String username,@QueryParam("token") String token,@QueryParam("date") String date) throws ParseException {
        String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
        Date myDate;
        FeedItems feeds = new FeedItems();
        List<FeedItem> feedItems;
        User user;
        if(date==null){
            date=String.format("%tFT%<tT", new Date());
        }
        try {
            //ISO FORMAT 2012-11-07T11:52:06
            myDate=sdf.parse(date);
        } 
        catch (ParseException e) {
           String current = String.format("%tFT%<tT", new Date());
           myDate=sdf.parse(current);
        }
        user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            feedItems=feedmanager.getLastFeedItems(user, myDate);
            feeds.setItems(feedItems);
        }
        return feeds;
    }*/
    
    /**
     * Return the last feed items for the user. It will return the last 4 items
     * for every channel subscribed
     * @return an instance of FeedItems
     */
    @GET
    @Produces("application/xml")
    @Path("{username}")
    public FeedItems getFeedItems(@PathParam("username") String username,@QueryParam("token") String token){
        FeedItems feeds = new FeedItems();
        List<FeedItem> feedItems;
        User user;

        user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            feedItems=feedmanager.getLastFeedItems(user);
            feeds.setItems(feedItems);
        }
        return feeds;
    }
    
    /**
     * Return all the feed items favorited by the user
     * @return an instance of FeedItems
     */
    @GET
    @Produces("application/xml")
    @Path("{username}/favorites")
    public FeedItems getFavFeedItems(@PathParam("username") String username,@QueryParam("token") String token) {
        FeedItems feeds = new FeedItems();
        List<FeedItem> feedItems;
        User user;
        user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            feedItems=feedmanager.getFavFeedItems(user);
            feeds.setItems(feedItems);
        }
        return feeds;
    }
    
    /**
     * Return all the feed items favorited by the followings of a user
     * @return an instance of FeedItems
     */
    @GET
    @Produces("application/xml")
    @Path("{username}/follow")
    public FeedItems getFollowFeedItems(@PathParam("username") String username,@QueryParam("token") String token) {
        FeedItems feeds = new FeedItems();
        List<FeedItem> feedItems;
        User user;
        user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            feedItems=feedmanager.getFollowFeedItems(user);
            feeds.setItems(feedItems);
        }
        return feeds;
    }
    
    /**
     * Returns a list of channel subscriptions based in the channels subscribed 
     * by his followings and not subscribed yet by the user.
     * @return an instance of FeedItems
     */
    @GET
    @Produces("application/xml")
    @Path("{username}/suggestions")
    public List<FeedChannel> getSuggestions(@PathParam("username") String username,@QueryParam("token") String token) {
        User user;
        List<FeedChannel> channels=new ArrayList<FeedChannel>();
        user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            channels=usermanager.getSuggestions(user);
        }
        return channels;
    }
    
    /**
     * Returns all the channel subscriptions by the user
     * @return an instance of List<FeedChannel>
     */
    @GET
    @Produces("application/xml")
    @Path("{username}/channel")
    public List<FeedChannel> getFeedChannels(@PathParam("username") String username,@QueryParam("token") String token) {
        List<FeedChannel> channels = new ArrayList<FeedChannel>();
        User user= new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            channels=user.getSubscriptions();
        }
        return channels;
    }
    
    /**
     * Add a new channel subscription for the user
     * return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Produces("application/xml")
    @Path("{username}/channel")
    public Response addSubscription(@QueryParam("token") String token,FeedChannel channel) {
        User user= usermanager.searchUserByToken(token);
        if(user!=null){
            if(feedmanager.addSubscription(user,channel)==1){
                return Response.status(201).entity("Subscription added").build();
            }
            else{
                return Response.status(409).entity("Subscription not added").build();
            }
        }
        else{
            return Response.status(402).entity("Authentication error").build();
        }
    }
    
    /**
     * delete a channel subscription from the user
     * return an HTTP response with content of the updated or created resource.
     */
    @DELETE
    @Produces("application/xml")
    @Path("{username}/channel")
    public Response deleteSubscription(@QueryParam("token") String token,@QueryParam("url") String url) {
        User user= usermanager.searchUserByToken(token);
        if(user!=null){
            FeedChannel channel = feedmanager.searchChannelByUrl(url);
            if(channel!=null){
                if(feedmanager.deleteSubscription(user,channel)==1){
                    return Response.status(201).entity("Subscription deleted").build();
                }
                else{
                    return Response.status(409).entity("Subscription not deleted").build();
                }
            }
            else{
                return Response.status(409).entity("Channel not found!").build();
            }
        }
        else{
            return Response.status(402).entity("Authentication error").build();
        }
    }
    
    /**
     * Method to search a channel based in the content and return a list of channels
     * matching with the content
     * @return a List of channels
     */
    @GET
    @Consumes("application/xml")
    @Path("channel")
    public List<FeedChannel> searchChannel(@QueryParam("token") String token,@QueryParam("search") String search) {
        User user = usermanager.searchUserByToken(token);
        List<FeedChannel> channels;
        if(user!=null){
            channels=feedmanager.searchChannel(search);
            return channels;
        }
        else{
            return new ArrayList<FeedChannel>();
        }  
    }
    

    /**
     * PUT method to add new channel to the database
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    @Path("channel")
    public Response addChannel(@QueryParam("token") String token,FeedChannel channel) {
        User user = usermanager.searchUserByToken(token);
        if(user!=null){
            if(feedmanager.addChannel(channel)==1){
                return Response.status(201).entity("Channel added").build();
            }
            else{
                return Response.status(409).entity("Channel not added").build();
            }
        }
        else{
            return Response.status(402).entity("Authentication error").build();
        }
    }
}