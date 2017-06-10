/* ProfileFragment.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidrss.controller.Controller;
import com.droidrss.model.FeedChannel;
import com.droidrss.model.User;

// TODO: Auto-generated Javadoc
/**
 * The Class ProfileFragment.
 */
public class ProfileFragment extends Fragment implements ExpandableListView.OnChildClickListener{
	
	/** The controller. */
	private Controller controller;
	
	/** The tv user. */
	private TextView tvUser;
	
	/** EditText for username, password and mail */
	private EditText etUsername, etPassword, etMail;
	
	/** The elv follows. */
	private ExpandableListView elvFollows;
	
	/** The elv adapter. */
	private ExpListAdapter elvAdapter;
	
	/** The usr following. */
	private User user, usrFollowing;
	
	/** The following list. */
	private List<User> followersList = new ArrayList<User>(), followingList = new ArrayList<User>();
	
	/** The suggestions list. */
	private List<FeedChannel> suggestionsList;
	
	/** The channel suggestion. */
	private FeedChannel channelSuggestion;
	
	/**
	 * Instantiates a new profile fragment.
	 */
	public ProfileFragment(){}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
	        return null;
	    }
		controller = new Controller(getActivity().getApplicationContext());
		
	    LinearLayout viewLayout = (LinearLayout)inflater.inflate(R.layout.fragment_profile, container, false);
	
	    user = controller.getUserFromWS();
	    
	    tvUser = (TextView) viewLayout.findViewById(R.id.fp_tv_user);
	    etUsername = (EditText) viewLayout.findViewById(R.id.fp_et_username);
	    etPassword = (EditText) viewLayout.findViewById(R.id.fp_et_password);
	    etMail = (EditText) viewLayout.findViewById(R.id.fp_et_mail);
	    	    
	    etPassword.setVisibility(View.INVISIBLE);
	    
	    elvFollows = (ExpandableListView) viewLayout.findViewById(R.id.fp_expList_follows);
	    
        ArrayList<ExpListParent> arrayParent = new ArrayList<ExpListParent>();
        
        ArrayList<Object> lista = new ArrayList<Object>();
        
        if(user!=null){
        	followersList = user.getFollowers();
	        followingList = user.getFollowings();
	        suggestionsList = controller.getSuggestions(user);
        	
        	arrayParent.add(new ExpListParent(getString(R.string.fp_capSubscriptions), suggestionsList.size()));
            arrayParent.add(new ExpListParent(getString(R.string.fp_capFollowers), followersList.size()));
            arrayParent.add(new ExpListParent(getString(R.string.fp_capFollowings), followingList.size()));
            
		    tvUser.setText(user.getUsername());
		    etUsername.setText(user.getSurname());
		    etMail.setText(user.getMail());
		    
		    for(FeedChannel c : suggestionsList){
	        	lista.add(c);
	        }
	        arrayParent.get(0).setArrayChild(lista);
	        
	        lista = new ArrayList<Object>();
	        for(User u : followersList){
	        	lista.add(u);
	        }
	        arrayParent.get(1).setArrayChild(lista);
	        
	        lista = new ArrayList<Object>();
	        for(User u : followingList){
	        	lista.add(u);
	        }
	        arrayParent.get(2).setArrayChild(lista);
	    }
        
        //sets the adapter that provides data to the list.
        elvAdapter = new ExpListAdapter(this.getActivity().getApplicationContext(), arrayParent);
        elvFollows.setAdapter(elvAdapter);
        elvFollows.setOnChildClickListener(this);
	    
	    return viewLayout;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListView.OnChildClickListener#onChildClick(android.widget.ExpandableListView, android.view.View, int, int, long)
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Object obj = elvAdapter.getChild(groupPosition, childPosition);
				
		if(obj instanceof User && groupPosition == 2){
			usrFollowing = (User) obj;
			
			ConfirmDialog dialogFragment = ConfirmDialog
					.newInstance(getString(R.string.title_alert_dialog), usrFollowing);
			
			dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
		
		}
		else if(obj instanceof FeedChannel && groupPosition == 0){
			channelSuggestion = (FeedChannel) obj;
			
			ConfirmDialog dialogFragment = ConfirmDialog
					.newInstance(getString(R.string.title_alert_dialog_sub), channelSuggestion);
			
			dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
		}
				
		return true;
	}
	
}
