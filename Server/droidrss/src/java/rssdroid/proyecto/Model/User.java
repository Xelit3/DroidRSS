/*
 * This class define the different attributes and methods for a user
 */
package rssdroid.proyecto.Model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


@XmlRootElement
public class User{
    
    private String username;
    private String surname;
    private String password;
    private String token;
    private String mail;
    private List<FeedChannel> subscriptions;
    private List<User> followers;
    private List<User> followings;
    
    public User(String username,String surname,String mail,String password){
        this.username=username;
        this.surname=surname;
        this.mail=mail;
        this.password=password;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getToken(){
        return this.token;
    }
    
    public void setToken(String token){
        this.token=token;
    }
    
    public String getMail(){
        return this.mail;
    }
    
    public String getSurname(){
        return this.surname;
    }
    
    public void setMail(String mail){
        this.mail=mail;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setPassword(String password){
        this.password=password;
    }
    
    public void setSurname(String surname){
        this.surname=surname;
    }
    
    public void setUsername(String username){
        this.username=username;
    }
    
    @XmlElementWrapper(name = "followers")
    @XmlElement(name = "follow")
    public List<User> getFollowers(){
        return this.followers;
    }
    
    public void setFollowers(List<User> followers){
        this.followers=followers;
    }
    
    @XmlElementWrapper(name = "followings")
    @XmlElement(name = "follow")
    public List<User> getFollowings(){
        return this.followings;
    }
    
    public void setFollowings(List<User> followings){
        this.followings=followings;
    }
    
    public User(String username,String token){
        this.username=username;
        this.token=token;
    }
    
    public User(){
        
    }

    @XmlElementWrapper(name = "subscriptions")
    @XmlElement(name = "channel")
    public List<FeedChannel> getSubscriptions(){
        return this.subscriptions;
    }
    
    public void setSubscriptions(List<FeedChannel> subscriptions){
        this.subscriptions=subscriptions;
    }
}
