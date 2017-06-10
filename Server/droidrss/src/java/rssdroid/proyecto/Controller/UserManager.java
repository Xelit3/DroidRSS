/*
 * This controller class define the different methods used to connect with
 * the database access layer.
 */
package rssdroid.proyecto.Controller;

import java.util.List;
import rssdroid.proyecto.Model.User;
import rssdroid.proyecto.Model.DataLayer.UserDB;
import rssdroid.proyecto.Model.FeedChannel;


public class UserManager {
    private UserDB userdb;
    
    public UserManager(){
        userdb= new UserDB();
    }
    
    
    //Return a boolean for the token and the username matches in the database
    public boolean checkToken(User user){
        return userdb.checkToken(user);
    }
    
    //Return an user object with all the data related by the user
    public User getUserData(User user){
        return userdb.getUserData(user);
    }
    
    //Add new user to the database
    public int addUser(User user){
        return userdb.addUser(user);
    }
    
    
    //Add new follower to the user
    public int addFollow(User user,String username){
        return userdb.addFollow(user,username);
    }
    
    //Remove follower to the user
    public int removeFollow(User user,String username){
        return userdb.removeFollow(user,username);
    }
    
    //Update the user data
    public int updateUser(String username,User user){
        return userdb.updateUser(username,user);
    }
    
    //delete a user from database
    public int deleteUser(User user){
        return userdb.deleteUser(user);
    }
    
    //Search a user by his token
    public User searchUserByToken(String token){
        return userdb.searchUserByToken(token);
    }
    
    
    //Search a user by his username
    public User searchUserByUsername(String username){
        return userdb.searchUserByUsername(username);
    }
    
    //return a list of channels to subscribe based on the
    // channels subscribed by the user followings
    public List<FeedChannel> getSuggestions (User user){
        return userdb.getSuggestions(user);
    }
     
    //Search and return a list of users matching the search
     public List<User> searchUsers(User user,String username){
         return userdb.searchUsers(user,username);
     }
}
