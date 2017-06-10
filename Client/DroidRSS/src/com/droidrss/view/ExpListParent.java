package com.droidrss.view;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpListParent.
 */
public class ExpListParent {
    
    /** The title. */
    private String title;
    
    /** The count. */
    private int count;
    
    /** The array child. */
    private ArrayList<Object> arrayChild;
 
    /**
     * Instantiates a new exp list parent.
     *
     * @param title the title
     * @param count the count
     */
    public ExpListParent(String title, int count){
    	this.title = title;
    	this.setCount(count);
    }
    
    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
 
    /**
     * Sets the title.
     *
     * @param mTitle the new title
     */
    public void setTitle(String mTitle) {
        this.title = mTitle;
    }
 
    /**
     * Gets the count.
     *
     * @return el count
     */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count el count a establecer
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the array child.
	 *
	 * @return the array child
	 */
	public ArrayList<Object> getArrayChild() {
        return arrayChild;
    }
 
    /**
     * Sets the array child.
     *
     * @param arrayFollows the new array child
     */
    public void setArrayChild(ArrayList<Object> arrayFollows) {
        this.arrayChild = arrayFollows;
    }
}