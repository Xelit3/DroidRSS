/* FeedsFragment.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedsFragment.
 */
public class FeedsFragment extends Fragment implements OnClickListener{

	/** Buttons of feeds fragment. */
	private Button bt_userFeeds, bt_uFavFeeds, fFavFeeds;
	
	/**
	 * Instantiates a new feeds fragment.
	 */
	public FeedsFragment(){	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
	        return null;
	    }

	    LinearLayout viewLayout = (LinearLayout)inflater.inflate(R.layout.fragment_feeds, container, false);
	    
	    bt_userFeeds = (Button) viewLayout.findViewById(R.id.ff_bt_userFeeds);
		bt_uFavFeeds = (Button) viewLayout.findViewById(R.id.ff_bt_uFavFeeds);
		fFavFeeds = (Button) viewLayout.findViewById(R.id.ff_bt_fFavFeeds);

		bt_userFeeds.setOnClickListener(this);
		bt_uFavFeeds.setOnClickListener(this);
		fFavFeeds.setOnClickListener(this);
		
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
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity().getApplicationContext(), FeedsListActivity.class);
		//Values for the ListView called: 1: User feeds | 2: User favs | e: Followings favs
		
		switch(v.getId()){
		
			case R.id.ff_bt_userFeeds:
				intent.putExtra("type", 1);
				break;
			
			case R.id.ff_bt_uFavFeeds:
				intent.putExtra("type", 2);
				break;
				
			case R.id.ff_bt_fFavFeeds:
				intent.putExtra("type", 3);
				break;
		}
		
		startActivity(intent);
	}

}
