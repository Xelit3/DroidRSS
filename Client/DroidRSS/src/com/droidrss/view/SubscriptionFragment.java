/* SubscriptionFragment.java
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

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class SubscriptionFragment.
 */
public class SubscriptionFragment extends ListFragment implements OnItemClickListener{
	
	/** The controller. */
	Controller controller;
	
	/** The channelsist. */
	List<FeedChannel> channelsist = new ArrayList<FeedChannel>();
	
	/** The channel selected. */
	public static List<FeedChannel> channelSelected = new ArrayList<FeedChannel>();

	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
	        return null;
	    }
		controller = new Controller(getActivity().getApplicationContext());
		
		User user = controller.getUserFromWS();
		if(user != null){
			channelsist = user.getSubscriptions();
		}
						
		// An array of items to display in ArrayList
		String[] channels = new String[channelsist.size()];
			
		for(int i=0; i<channelsist.size(); i++)
			channels[i] = channelsist.get(i).toString();
							
		// Creating array adapter to set data in listview */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_multiple_choice, channels);

		// Setting the array adapter to the listview */
		setListAdapter(adapter);
		

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();

		// Setting the multiselect choice mode for the listview
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		getListView().setOnItemClickListener(this);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		if(channelSelected.contains(channelsist.get(position))){
			getListView().setItemChecked(position, false);
			channelSelected.remove(channelsist.get(position));
		}
		else{
			getListView().setItemChecked(position, true);
			channelSelected.add(channelsist.get(position));
		}
	}
}
