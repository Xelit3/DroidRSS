/*
 * This method is used to add a new channel based on his url and fecth the title and
 * the description to store in database for the information of the channel
 */
package rssdroid.proyecto.Model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;


public class ParseChannel {
    private FeedChannel channel;
    private URL url;
    
    public ParseChannel(FeedChannel channel){
        this.channel=channel;
        try {
            this.url = new URL(channel.getUrl());
        } 
        catch (MalformedURLException e) {
            this.url=null;
        }
    }
    
    //Check the http headers and connectivity with a url to check
    //if a valid rss application
    private boolean checkValidContent(){
        boolean valid=false;
        try {
            if(url!=null){
                URLConnection connection = url.openConnection();
                Map responseMap = connection.getHeaderFields();
                for (Iterator iterator = responseMap.keySet().iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    if(key!=null){
                        if(key.equals("Content-Type")){
                            List values = (List) responseMap.get(key);
                            for (int i = 0; i < values.size(); i++) {
                                String value = (String)values.get(i);
                                String[] header = value.split(";");
                                String contentType=header[0];
                                if(contentType.equals("text/xml") || contentType.equals("application/atom+xml")
                                        || contentType.equals("application/rdf+xml") 
                                        || contentType.equals("application/rss+xml")|| contentType.equals("application/xml")){
                                    valid=true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ParseChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valid;
    }

    //parse the xml headers of the rss to get the title and description of the channel
    public boolean parseHeaders(){
        String title="";
        String description ="";
        boolean isHeader=true;
        boolean valid = checkValidContent();
        if(valid){
            try {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = read();
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
                while (eventReader.hasNext() && isHeader) {
                    XMLEvent event = eventReader.nextEvent();
                    if (event.isStartElement()) {
                        String localPart = event.asStartElement().getName().getLocalPart();
                        if(localPart.equals("title")){
                            if(title.equals("")){
                                title = getCharacterData(event, eventReader);
                            }
                        }
                        else if(localPart.equals("description") || localPart.equals("id")){
                            description=getCharacterData(event, eventReader);
                        }
                        else if (localPart.equals("item") || localPart.equals("items")|| localPart.equals("entry") || localPart.equals("content")){
                            isHeader=false;
                        }
                    }
                }
                channel.setDescription(description);
                channel.setTitle(title);
            } 
            catch (XMLStreamException ex) {
                Logger.getLogger(ParseChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return valid;
    }
    
    //get String data from the xml
    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
    throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
    
    //return the url connection
    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException ex) {
            Logger.getLogger(ParseChannel.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
