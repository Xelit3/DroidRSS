/* MainActivity.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;

import java.util.ArrayList;
import java.util.List;

import com.droidrss.controller.Controller;
import com.droidrss.model.FeedChannel;
import com.droidrss.model.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity  extends FragmentActivity implements OnTabChangeListener, OnItemClickListener{

	/** The TabHost will contains the tabs of our FragmentActivity. */
	private TabHost tHost;
	
	/** The controller. */
	private Controller controller;
	
	/** Dialogs for search user, channel and add it both. */
	private Dialog dialog,listDialog;
	
	/** The channels. */
	private List<FeedChannel> channels;
	
	/** The users. */
	private List<User> users;
	
	/** The search. */
	private String search="";
	
	/** The type. */
	private int code, type = -1;
		
	/**
	 * The Enum CurrentTab.
	 */
	public enum CurrentTab{
		PROFILE,  SUBSCRIPTIONS,  FEEDS
	};
	
	/** The current tab. */
	CurrentTab currentTab;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		controller = new Controller(this);
		dialog = new Dialog(this);
		listDialog = new Dialog(this);
		
		tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();
		
        tHost.setOnTabChangedListener(this);
        
        // Defining tab builder for Subscriptions tab
        TabHost.TabSpec tSpecSubscriptions = tHost.newTabSpec("Subscriptions");
        tSpecSubscriptions.setIndicator("Subscriptions");        
        tSpecSubscriptions.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecSubscriptions);
        
        // Defining tab builder for Feeds tab
        TabHost.TabSpec tSpecFeeds = tHost.newTabSpec("Feeds");
        tSpecFeeds.setIndicator("Feeds");        
        tSpecFeeds.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecFeeds);
        
        // Defining tab builder for Profile tab
        TabHost.TabSpec tSpecProfile = tHost.newTabSpec("Profile");
        tSpecProfile.setIndicator("Profile");        
        tSpecProfile.setContent(new DummyTabContent(getBaseContext()));
        tHost.addTab(tSpecProfile);
        
        /*
         If we want to put an image in the tab, we have to change the method setIndicator(string) by the other
         overload (string, resource), like this:
         tSpecProfile.setIndicator("Profile",getResources().getDrawable(R.drawable.image));
        */
        
		}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		switch(currentTab){
			case PROFILE:
				getMenuInflater().inflate(R.menu.menu_profile, menu);
	            break;
				
			case SUBSCRIPTIONS:
				getMenuInflater().inflate(R.menu.menu_subscriptions, menu);
				break;
				
			case FEEDS:
				getMenuInflater().inflate(R.menu.menu_basic, menu);
				break;
	
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String tabId) {
		android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
		
		SubscriptionFragment subscriptionFragment = (SubscriptionFragment) fm.findFragmentByTag("Subscriptions");
		FeedsFragment feedsFragment = (FeedsFragment) fm.findFragmentByTag("Feeds");
		ProfileFragment profileFragment = (ProfileFragment) fm.findFragmentByTag("Profile");
		
		android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
		
		// Detaches the subscriptionFragment if exists
		if(subscriptionFragment != null)
			ft.detach(subscriptionFragment);
		
		// Detaches the feedsFragment if exists
		if(feedsFragment != null)
			ft.detach(feedsFragment);
		
		// Detaches the feedsFragment if exists
		if(profileFragment != null)
			ft.detach(profileFragment);
		
		// If current tab is subscriptions
		if(tabId.equalsIgnoreCase("Subscriptions")){
			currentTab = CurrentTab.SUBSCRIPTIONS;
			if(subscriptionFragment==null){		
				// Create SubscriptionFragment and adding to fragmenttransaction
				ft.add(R.id.realtabcontent, new SubscriptionFragment(), "Subscriptions");						
			}
			else{
				// Bring to the front, if already exists in the fragmenttransaction
				ft.attach(subscriptionFragment);						
			}
			
		}
		else if (tabId.equalsIgnoreCase("Feeds")){	// If current tab is Feeds
			currentTab = CurrentTab.FEEDS;
			if(feedsFragment==null){
				// Create FeedsFragment and adding to fragmenttransaction
				ft.add(R.id.realtabcontent,new FeedsFragment(), "Feeds");
			}
			else{
				// Bring to the front, if already exists in the fragmenttransaction
				ft.attach(feedsFragment);						
			}
		}
		else{	// If current tab is Profile
			currentTab = CurrentTab.PROFILE;
			if(profileFragment==null){
				// Create ProfileFragment and adding to fragmenttransaction
				ft.add(R.id.realtabcontent,new ProfileFragment(), "Profile");
			}
			else{
				// Bring to the front, if already exists in the fragmenttransaction
				ft.attach(profileFragment);						
			}
		}
		
		ft.commit();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.mi_about:
				Intent intent = new Intent(this, AboutDroidRSSActivity.class);
			    startActivity(intent);
				break;
				
			case R.id.mi_addChannel:
				showAddChannelDialog();
				break;
			case R.id.mi_delChannel:
				deleteSubscription();
				break;
			
			case R.id.mi_addSubscription:
				showAddSubscriptionDialog();
				break;
				
			case R.id.mi_searchUser:
				showSearchUserDialog();
				break;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user = controller.getUser();;
		switch(type){
		
			case 0:
				FeedChannel channel = channels.get(position);
				controller.addSubscription(channel,user);
		        Toast.makeText(getApplicationContext(), (String)channel.getTitle()+ getString(R.string.maAlert_addSubscription), Toast.LENGTH_LONG).show();
		        break;
		        
			case 1:
				User toFollow = users.get(position);
				controller.addFollowing(toFollow.getUsername(), user.getToken());
				Toast.makeText(getApplicationContext(), user.getUsername() + " - " +getString(R.string.maAlert_addFolloing), Toast.LENGTH_LONG).show();
		        break;
		
		}
	}
	
	/**
	 * Delete subscription.
	 */
	public void deleteSubscription(){
		List<FeedChannel> channels = SubscriptionFragment.channelSelected;
		User user = controller.getUser();
		int code = controller.deleteSubscription(channels, user);
		SubscriptionFragment.channelSelected = new ArrayList<FeedChannel>();
		showMsg(String.valueOf(channels.size())+"Code: "+code);
	}
	
	/**
	 * Show add subscription dialog.
	 * It will show a dialog to input characters for add a subscription
	 */
	public void showAddSubscriptionDialog(){
		dialog.setContentView(R.layout.channel_dialog);
		dialog.setTitle(getString(R.string.maAlert_tv_title_channel));

		Button dialogButton = (Button) dialog.findViewById(R.id.buttonSrch);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchTxt = (EditText) dialog.findViewById(R.id.searchTxt);
				search = searchTxt.getText().toString();
				showListDialog();
			}
		});
		
		Button cancelButton = (Button) dialog.findViewById(R.id.buttonCncl);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * Show search user dialog.
	 * It will show a dialog to input characters for search a user
	 */
	public void showSearchUserDialog(){
		dialog.setContentView(R.layout.user_dialog);
		dialog.setTitle(getString(R.string.maAlert_tv_title_user));
				
		Button dialogButton = (Button) dialog.findViewById(R.id.buttonSrch);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchTxt = (EditText) dialog.findViewById(R.id.searchTxt);
				search = searchTxt.getText().toString();
				showListUserDialog();
			}
		});
		
		Button cancelButton = (Button) dialog.findViewById(R.id.buttonCncl);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
	/**
	 * Show add channel dialog.
	 * It will show a dialog to input characters for add a channel
	 */
	public void showAddChannelDialog(){
		dialog.setContentView(R.layout.channel_dialog);
		dialog.setTitle(getString(R.string.maAlert_addChannel));
		final String token = controller.getUser().getToken();
		Button dialogButton = (Button) dialog.findViewById(R.id.buttonSrch);
		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchTxt = (EditText) dialog.findViewById(R.id.searchTxt);
				search = searchTxt.getText().toString();
				code = controller.addChannel(search,token);
				dialog.dismiss();
				if(code == 201)
					showMsg(getString(R.string.maAlert_channelAdded));
			
				else
					showMsg(getString(R.string.maAlert_failChannelAdded));
			}
		});
		
		Button cancelButton = (Button) dialog.findViewById(R.id.buttonCncl);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * Show list dialog.
	 * Shows the list of channles founded
	 */
	public void showListDialog(){
		String token = controller.getUser().getToken();
		channels=controller.searchChannel(search,token);
		listDialog.setContentView(R.layout.list_channel);
		
		ListView lv = (ListView) listDialog.findViewById(R.id.listChannels);
		String[] channelsTitle = new String[channels.size()];
		for(int i=0;i<channels.size();i++){
			channelsTitle[i]=channels.get(i).getTitle();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(listDialog.getContext(), android.R.layout.simple_list_item_1, channelsTitle);
		lv.setAdapter(adapter);
		listDialog.setCancelable(true);
		listDialog.setTitle(getString(R.string.g_channel));
		dialog.dismiss();
		lv.setOnItemClickListener(this);
		type = 0;
		listDialog.show();
	}
	
	/**
	 * Show list user dialog.
	 * Shows the list of users founded
	 */
	public void showListUserDialog(){
		String token = controller.getUser().getToken();
		users = controller.getUsersBySearch(search, token);
		listDialog.setContentView(R.layout.list_channel);
		
		ListView lv = (ListView) listDialog.findViewById(R.id.listChannels);
		String[] s_users = new String[users.size()];
		
		for(int i=0; i<users.size(); i++){
			s_users[i] = users.get(i).toString();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(listDialog.getContext(), android.R.layout.simple_list_item_1, s_users);
		lv.setAdapter(adapter);
		listDialog.setCancelable(true);
		listDialog.setTitle(getString(R.string.g_users));
		dialog.dismiss();
		lv.setOnItemClickListener(this);
		type = 1;
		listDialog.show();
	}
	
	/**
	 * Delete following.
	 * This method deletes a following of the user in WS
	 *
	 * @param usr the usr
	 */
	public void deleteFollowing(User usr) {
		controller.deleteFollowing(usr.getUsername(), controller.getUser().getToken());
	}
	
	/**
	 * Accept suggestion.
	 *
	 * @param channel the channel
	 */
	public void acceptSuggestion(FeedChannel channel) {
		controller.addSubscription(channel, controller.getUser());
	}
	
	/**
	 * With this we can show a message in this activity
	 *
	 * @param msg the message to show in Toast
	 */
	public void showMsg(final String msg){
		this.runOnUiThread(new Runnable() {
			public void run() {
				if(msg!= null)
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
