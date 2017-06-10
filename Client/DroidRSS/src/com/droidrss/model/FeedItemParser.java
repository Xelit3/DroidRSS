/* FeedItemParser.java
 * @author: Xavi Rueda, Iván Mora
 * @date: 5-10-2013
 * @description: FeedItemParser for FeedItems
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
 * The Class FeedItemParser.
 */
public class FeedItemParser extends DefaultHandler{

	/** The item. */
	private FeedItem item;
	
	/** The channel. */
	private FeedChannel channel;
	
	/** The items. */
	private List<FeedItem> items = new ArrayList<FeedItem>();
	
	/** The temp. */
	private String temp="";
	
	/** The bff. */
	private String bff="";
	
	/** The is channel. */
	private boolean isChannel=false;

	/**
	 * Instantiates a new feed item parser.
	 *
	 * @param xml the xml
	 */
	public FeedItemParser(String xml){
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
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) {
		bff="";
		if (qName.equalsIgnoreCase("channel")) {
			channel = new FeedChannel();
			isChannel=true;
		}
		else if (qName.equalsIgnoreCase("FeedItem")) {
			item = new FeedItem();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) {
		if (qName.equalsIgnoreCase("channel")) {
			item.setChannel(channel);
			isChannel=false;
		}
		if(isChannel){
			if (qName.equalsIgnoreCase("title")) {
				channel.setTitle(bff);
			}
			else if(qName.equalsIgnoreCase("url")){
				channel.setUrl(bff);
			}
			else if(qName.equalsIgnoreCase("description")){
				channel.setDescription(bff);
			}
		}
		else{
			if(qName.equalsIgnoreCase("url")){
				item.setUrl(bff);
			}
			else if(qName.equalsIgnoreCase("title")){
				item.setTitle(bff);
			}
			else if(qName.equalsIgnoreCase("content")){
				item.setContent(bff);
			}
			else if(qName.equalsIgnoreCase("pubDate")){
				item.setPubDate(bff);
			}
			else if(qName.equalsIgnoreCase("itemid")){
				item.setItemid(Integer.parseInt(bff));
			}
			else if (qName.equalsIgnoreCase("FeedItem")){
				items.add(item);
			}
		} 
	}  

	/**
	 * Gets the feed items.
	 *
	 * @return the feed items
	 */
	public List<FeedItem> getFeedItems(){
		return items;
	}
}
