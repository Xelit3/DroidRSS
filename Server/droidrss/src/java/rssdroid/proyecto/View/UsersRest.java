/*
 * This class define the webservice for get and modify user data.
 */
package rssdroid.proyecto.View;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rssdroid.proyecto.Model.User;
import rssdroid.proyecto.Controller.UserManager;

//Main path to access to the resource
@Path("user/")
public class UsersRest {
    
    private UserManager usermanager;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsersRest
     */
    public UsersRest() {
        usermanager=new UserManager();
    }

    /**
     * Return xml with the complete user data like the username, mail, followings, followers,
     * surname and subscriptions
     * @return an User object
     */
    @GET
    @Produces("application/xml")
    @Path("{username}")
    public User getUserData(@PathParam("username") String username,@QueryParam("token") String token) {
        User user=new User(username,token);
        if(usermanager.checkToken(user)){
            user=usermanager.getUserData(user);
            return user;
        }
        else{
            return new User();
        }
    }
    
    /**
      * Search a user and return a list of search matches
     * @return a List of users
     */
    @GET
    @Produces("application/xml")
    @Path("{username}/search")
    public List<User> searchUser(@PathParam("username") String username,@QueryParam("token") String token) {
        User user=usermanager.searchUserByToken(token);
        List<User> users;
        if(user!=null){
            users=usermanager.searchUsers(user, username);
            return users;
        }
        else{
            return new ArrayList<User>();
        }
    }

    /**
     * PUT method for add a new user to the database
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    @Path("{username}")
    public Response addUser(@PathParam("username") String username,User user) {
        if(usermanager.addUser(user)==1){
            return Response.status(201).entity("User added").build();
        }
        else{
            return Response.status(409).entity("Problem adding user").build();
        }
    }
    
    /**
     * PUT method to add a new follower, the username PathParam is used
     * to indicate the user to follow
     * Token is used to authenticate the user
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    @Path("{username}/follow")
    public Response addFollow(@PathParam("username") String username,@QueryParam("token") String token) {
        User user = usermanager.searchUserByToken(token);
        if(user!=null){
            if(usermanager.addFollow(user,username)==1){
                return Response.status(201).entity("Follow added").build();
            }
            else{
                return Response.status(409).entity("Follow not added").build();
            }
        }
        else{
            return Response.status(403).entity("Authentication error").build();
        }
    }
    
    /**
     * DELETE method to remove a following. The username PathParam is used to
     * indicate to unfollow
     * Token is used to authenticate the user
     * @return an HTTP response with content of the updated or created resource.
     */
    @DELETE
    @Consumes("application/xml")
    @Path("{username}/follow")
    public Response removeFollow(@PathParam("username") String username,@QueryParam("token") String token) {
        User user = usermanager.searchUserByToken(token);
        if(user!=null){
            if(usermanager.removeFollow(user,username)==1){
                return Response.status(201).entity("Follow removed").build();
            }
            else{
                return Response.status(409).entity("Follow not removed").build();
            }
        }
        else{
            return Response.status(403).entity("Authentication error").build();
        }
    }
    
    /**
     * POST method to update the user data profile
     * @param User gets an xml with the user data to change
     * Token is used to authenticate the user
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/xml")
    @Path("{username}")
    public Response updateUser(@PathParam("username") String username,User newUser) {
        User user = new User(username,newUser.getToken());
        if(usermanager.checkToken(user)){
            if(usermanager.updateUser(username, newUser)==1){
                return Response.status(201).entity("User updated").build();
            }
            else{
                return Response.status(409).entity("Problem updating user").build();
            }
        }
        else{
            return Response.status(402).entity("Authentication error").build();
        }
    }
    
    /**
     * DELETE method to delete a user from database
     * @param content representation for the resource
     * Token is used to authenticate the user
     * @return an HTTP response with content of the deleted resource.
     */
    @DELETE
    @Consumes("application/xml")
    @Path("{username}")
    public Response deleteUser(@PathParam("username") String username,@QueryParam("token") String token) {
        User user = new User(username,token);
        if(usermanager.checkToken(user)){
            if(usermanager.deleteUser(user)==1){
                return Response.status(201).entity("User deleted").build();
            }
            else{
                return Response.status(409).entity("Problem deleting user").build();
            }
        }
        else{
            return Response.status(402).entity("Authentication error").build();
        }
    }
}