/*
 * This class define the different methods to the database access for 
 * get, update and delete user data
 */
package rssdroid.proyecto.Model.DataLayer;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;
import rssdroid.proyecto.Model.Encrypter;
import rssdroid.proyecto.Model.FeedChannel;
import rssdroid.proyecto.Model.User;


public class UserDB {
    private Connection conn;
    private FeedDB feeddb;
    private static Logger logger;
    private FileHandler handler;
    private static String fileName="/var/log/droid_rss/userdb.log";
    private Encrypter encrypter;
	
    public UserDB(){
        conn=null;
        //setupLogger();
        try{
            Class.forName(BDConnect.DRIVER);
            conn=DriverManager.getConnection(BDConnect.BD_URL,BDConnect.USER,BDConnect.PASSWORD);
        }
        catch(ClassNotFoundException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        feeddb=new FeedDB();
        encrypter=new Encrypter();
    }
        
    //Return a boolean for the token and the username matches in the database
    public boolean checkToken(User user){
        String tokenSQL="select token from rss_token where username = ?";
        boolean valid=false;
        String token;
        try{
            PreparedStatement pst = conn.prepareStatement(tokenSQL);
            pst.setString(1, user.getUsername());
            ResultSet res = pst.executeQuery();
            if(res.next()){
                token=res.getString("token");
                if(token.equals(user.getToken())){
                    valid=true;
                }
                else{
                    valid=false;
                }
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return valid;
        }
    }
    
    //Return an user object with all the data related by the user
    public User getUserData(User user){
        String userSQL="select surname,mail from rss_user where username = ?";
        String surname,mail;
        try{
            PreparedStatement pst = conn.prepareStatement(userSQL);  
            pst.setString(1, user.getUsername());
            ResultSet res = pst.executeQuery();
            if(res.next()){
                surname=res.getString("surname");
                mail=res.getString("mail");
                user.setMail(mail);
                user.setSurname(surname);
                user.setSubscriptions(feeddb.getSubscriptions(user));
                user.setFollowers(getFollowers(user));
                user.setFollowings(getFollowings(user));
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return user;
        }
    }
    
    //Add new user to the database
    public int addUser(User user){
        int insert=0;
        String userSQL ="insert into rss_user values(?,?,?,?)";
        try{
            PreparedStatement pst = conn.prepareStatement(userSQL);
            pst.setString(1,user.getUsername());
            pst.setString(2,user.getSurname());
            pst.setString(3,user.getPassword());
            pst.setString(4,user.getMail());
            insert=pst.executeUpdate();
            if(insert==1){
                String token=encrypter.getHash(user.getUsername()+user.getPassword());
                String tokenSQL="insert into rss_token values(?,?)";
                PreparedStatement tokenpst = conn.prepareStatement(tokenSQL);
                tokenpst.setString(1,user.getUsername());
                tokenpst.setString(2,token);
                tokenpst.executeUpdate();
            }
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
    
    //Add new follower to the user
    public int addFollow(User user,String username){
        int insert=0;
        String followSQL ="insert into user_follow values(?,?)";
        try{
            PreparedStatement pst = conn.prepareStatement(followSQL);
            pst.setString(1,user.getUsername());
            pst.setString(2,username);
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
    
    //Remove follower to the user
    public int removeFollow(User user,String username){
        int delete=0;
        String followSQL ="delete from user_follow where id_rss_user = ? and id_rss_followed = ?";
        try{
            PreparedStatement pst = conn.prepareStatement(followSQL);
            pst.setString(1,user.getUsername());
            pst.setString(2,username);
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
    
    //Update the user data
    public int updateUser(String username,User user){
        int insert=0;
        try{
            PreparedStatement pst = getUpdateSQL(username,user);
            if(pst!=null){
                insert=pst.executeUpdate();
            }
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
    
    //Search a user by his token
    public User searchUserByToken(String token){
        String tokenSQL="select username from rss_token where token = ?";
        String username;
        User user=null;
        try{
            PreparedStatement pst = conn.prepareStatement(tokenSQL);  
            pst.setString(1, token);
            ResultSet tokenRes = pst.executeQuery();
            if(tokenRes.next()){
                username=tokenRes.getString("username");
                user=getUserData(new User(username,token));
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return user;
        }
    }
    
    //Search a user by his username
    public User searchUserByUsername(String username){
        String tokenSQL="select surname,mail from rss_user where username = ?";
        User user=null;
        String surname,mail;
        try{
            PreparedStatement pst = conn.prepareStatement(tokenSQL);  
            pst.setString(1, username);
            ResultSet userRes = pst.executeQuery();
            if(userRes.next()){
                surname=userRes.getString("surname");
                mail=userRes.getString("mail");
                user=new User();
                user.setMail(mail);
                user.setUsername(username);
                user.setSurname(surname);
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return user;
        }
    }
    
    //Search and return a list of users matching the search
    public List<User> searchUsers(User userSearch,String username){
        String userSQL="select username,surname,mail from rss_user where username like ? and not username = ?";
        User user;
        String surname,mail,userid;
        List<User> users = new ArrayList<User>();
        try{
            PreparedStatement pst = conn.prepareStatement(userSQL);  
            pst.setString(1,"%"+username+"%");
            pst.setString(2,userSearch.getUsername());
            ResultSet userRes = pst.executeQuery();
            while(userRes.next()){
                surname=userRes.getString("surname");
                mail=userRes.getString("mail");
                userid=userRes.getString("username");
                user=new User();
                user.setMail(mail);
                user.setUsername(userid);
                user.setSurname(surname);
                users.add(user);
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return users;
        }
    }
    
    //return a preparedtstatent to execute an update depending on the user data to update
    private PreparedStatement getUpdateSQL(String username,User user) throws SQLException{
        StringBuilder updateSQL = new StringBuilder();
        String sql="";
        PreparedStatement pst;
        updateSQL.append("update rss_user set ");
        if(!user.getMail().equals("")){
            updateSQL.append("mail=");
            updateSQL.append("'");
            updateSQL.append(user.getMail());
            updateSQL.append("'");
            updateSQL.append(",");
        }
        if(!user.getSurname().equals("")){
            updateSQL.append("surname=");
            updateSQL.append("'");
            updateSQL.append(user.getSurname());
            updateSQL.append("'");
            updateSQL.append(",");
        }
        if(user.getPassword()!=null){
            updateSQL.append("password=");
            updateSQL.append("'");
            updateSQL.append(user.getPassword());
            updateSQL.append("'");
            updateSQL.append(",");
            String tokenSQL="update rss_token set token = ? where username = ?";
            PreparedStatement tokenPST= conn.prepareStatement(tokenSQL);
            String token=encrypter.getHash(username+user.getPassword());
            tokenPST.setString(1,token);
            tokenPST.setString(2,username);
            tokenPST.executeUpdate();
        }
        
        if(!updateSQL.toString().equals("")){
            sql=updateSQL.toString().substring(0, updateSQL.length()-1);
            sql=sql+" where username ='"+username+"'";
            pst= conn.prepareStatement(sql);
        }
        else{
            pst=null;
        }
        
        return pst;
    }
    
    //delete a user from database
    public int deleteUser(User user){
        int delete=0;
        String deleteUserSQL="delete from rss_user where username= ?";
        try{
            PreparedStatement userPST = conn.prepareStatement(deleteUserSQL);
            userPST.setString(1, user.getUsername());
            delete=userPST.executeUpdate();
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
    
    //Return a the list of followers of a user
    public List<User> getFollowers(User user){
        String followersSQL="select id_rss_user from user_follow where id_rss_followed =  ?";
        String userSQL="select username,surname,mail from rss_user where username =  ?";
        List<User> users = new ArrayList();
        String username;
        User temp;
        try{
            PreparedStatement pst = conn.prepareStatement(followersSQL);  
            pst.setString(1, user.getUsername());
            ResultSet res = pst.executeQuery();
            while(res.next()){
                username=res.getString("id_rss_user");
                PreparedStatement pstUser = conn.prepareStatement(userSQL); 
                pstUser.setString(1, username);
                ResultSet userRes = pstUser.executeQuery();
                while(userRes.next()){
                    temp= new User();
                    temp.setUsername(userRes.getString("username"));
                    temp.setMail(userRes.getString("mail"));
                    temp.setSurname(userRes.getString("surname"));
                    users.add(temp);
                }
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return users;
        }
    }
    
    //Return the list of followings for the user
    public List<User> getFollowings(User user){
        String followersSQL="select id_rss_followed  from user_follow where id_rss_user  =  ?";
        String userSQL="select username,surname,mail from rss_user where username =  ?";
        List<User> users = new ArrayList();
        String username;
        User temp;
        try{
            PreparedStatement pst = conn.prepareStatement(followersSQL);  
            pst.setString(1, user.getUsername());
            ResultSet res = pst.executeQuery();
            while(res.next()){
                username=res.getString("id_rss_followed");
                PreparedStatement pstUser = conn.prepareStatement(userSQL); 
                pstUser.setString(1, username);
                ResultSet userRes = pstUser.executeQuery();
                while(userRes.next()){
                    temp= new User();
                    temp.setUsername(userRes.getString("username"));
                    temp.setMail(userRes.getString("mail"));
                    temp.setSurname(userRes.getString("surname"));
                    users.add(temp);
                }
            }
        }
        catch(SQLException e){
            //logger.log(Level.INFO,e.getMessage());
            e.printStackTrace();
        }
        finally{
            return users;
        }
    }
    
     //return a list of channels to subscribe based on the
    // channels subscribed by the user followings
    public List<FeedChannel> getSuggestions(User user){
        List<User> followings = user.getFollowings();
        List<FeedChannel> userSubscriptions = user.getSubscriptions();
        Iterator it = followings.listIterator();
        List<FeedChannel> suggestions = new ArrayList<FeedChannel>();
        User temp;
        while(it.hasNext()){
            temp=(User) it.next();
            temp=getUserData(temp);
            List<FeedChannel> channels=temp.getSubscriptions();
            Iterator channelIt =channels.listIterator();
            FeedChannel channTmp;
            while(channelIt.hasNext()){
                channTmp=(FeedChannel)channelIt.next();
                if(!userSubscriptions.contains(channTmp)){
                    suggestions.add(channTmp);
                }
            }
        }
        return suggestions;
    }
    
    private void setupLogger(){
        try{
            logger = Logger.getLogger(rssdroid.proyecto.Model.DataLayer.UserDB.class.getCanonicalName());
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
