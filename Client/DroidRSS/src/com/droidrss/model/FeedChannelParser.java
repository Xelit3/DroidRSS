/* FeedChannelParser.java
* @author: Iván Mora
* @date: 5-24-2013
* @description: FeedChannelParser class
*/
package com.droidrss.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


// TODO: Auto-generated Javadoc
/**
 * The Class FeedChannelParser.
 */
public class FeedChannelParser extends DefaultHandler{
    
    /** The temp. */
    private String temp="";
    
    /** The bff. */
    private String bff="";
    
    /** The channel. */
    private FeedChannel channel;
    
    /** The suggestions. */
    private List<FeedChannel> suggestions= new ArrayList<FeedChannel>();
    
    /**
     * Instantiates a new feed channel parser.
     *
     * @param xml the xml
     */
    public FeedChannelParser(String xml){
        try{
            SAXParserFactory spfac = SAXParserFactory.newInstance();
            SAXParser sp = spfac.newSAXParser();
            sp.parse(new InputSource(new StringReader(xml)),this); 
            
        }
        catch(ParserConfigurationException e){
            e.printStackTrace();
        }
        catch(SAXException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char[] buffer, int start, int length) {
        temp = new String(buffer, start, length);
        boolean elementDataContainsNewLine = (temp.indexOf("\n") != -1);
        if (!elementDataContainsNewLine){
            bff=bff+temp;
        }
        else{
            bff=temp;
        }
    }
    
    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        bff="";
        if (qName.equalsIgnoreCase("channel")) {
            channel = new FeedChannel();
        }
    }
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("channel")) {
            suggestions.add(channel);
        }
        else if (qName.equalsIgnoreCase("title")) {
            channel.setTitle(bff);
        }
        else if(qName.equalsIgnoreCase("url")){
            channel.setUrl(bff);
        }
        else if(qName.equalsIgnoreCase("description")){
            channel.setDescription(bff);
        }
    }
    
    /**
     * Gets the channels.
     *
     * @return the channels
     */
    public List<FeedChannel> getChannels(){
        return this.suggestions;
    }
    
}
