/* DummyTabContent.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DummyTabContent.
 */
public class DummyTabContent implements TabContentFactory{
	
	/** The m context. */
	private Context mContext;
	
	/**
	 * Instantiates a new dummy tab content.
	 *
	 * @param context the context
	 */
	public DummyTabContent(Context context){
		mContext = context;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	 */
	@Override
	public View createTabContent(String tag) {
		View v = new View(mContext);
		return v;
	}
	

}
