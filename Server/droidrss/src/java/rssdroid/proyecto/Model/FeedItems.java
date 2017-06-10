/*
 * This class is used for jaxb to define the xml hierarchy for a list of FeedChannel
 */
package rssdroid.proyecto.Model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="Feeds")
@XmlAccessorType(XmlAccessType.FIELD)
public class FeedItems {
    @XmlElement(name = "FeedItem", type = FeedItem.class)
    public List<FeedItem> items;
    
    public FeedItems(){
        items=new ArrayList<FeedItem>();
    }
    public List<FeedItem> getItems(){
        return this.items;
    }
    
    public void setItems(List<FeedItem> items){
        this.items=items;
    }
}
