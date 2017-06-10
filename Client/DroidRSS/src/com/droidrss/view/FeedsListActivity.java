package com.droidrss.view;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.droidrss.controller.Controller;
import com.droidrss.model.FeedItem;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedsListActivity.
 */
public class FeedsListActivity extends ListActivity {

	/** The controller. */
	private Controller controller;
	
	/** The feeds. */
	private List<FeedItem> feeds;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feed_list_activity);
		
		controller = new Controller(getApplicationContext());
		
		Bundle datosIn = this.getIntent().getExtras();
        
		//Values for the ListView called: 1: User feeds | 2: User favs | e: Followings favs | by default: 1
        int type = datosIn.getInt("type", 1); 
        
        feeds = controller.getFeeds(type);
        
        String[] s_feeds;
		
		if(feeds!=null){
			s_feeds = new String[feeds.size()];
			
			for(int i=0; i<feeds.size(); i++)
				s_feeds[i] = Html.fromHtml(feeds.get(i).getTitle()).toString();
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, s_feeds);
			
			setListAdapter(adapter);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		FeedItem feed = feeds.get(position);
		
		Intent intent = new Intent(this, FeedItemShowActivity.class);
	    intent.putExtra("feed", feed);
	    
	    startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateView(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet)
	 */
	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		return super.onCreateView(parent, name, context, attrs);
	}

}
