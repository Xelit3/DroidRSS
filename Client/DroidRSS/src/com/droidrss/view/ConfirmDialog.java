package com.droidrss.view;

import java.io.Serializable;

import com.droidrss.model.FeedChannel;
import com.droidrss.model.User;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfirmDialog.
 */
public class ConfirmDialog extends DialogFragment {
	
	/** The user. */
	private User user;
	
	/** The channel. */
	private FeedChannel channel;
	
	/** The type. */
	private int type = -1;
	
	/**
	 * New instance.
	 *
	 * @param title the title
	 * @param object the object
	 * @return the confirm dialog
	 */
	static ConfirmDialog newInstance(String title, Serializable object){
		ConfirmDialog fragment = new ConfirmDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putSerializable("object", object);
		fragment.setArguments(args);
		
		return fragment;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Types: 1 will be for User serializable, and delete following || 2 will be for FeedChannel suggestion and subscribe
		String title = getArguments().getString("title");
		
		Serializable srl = getArguments().getSerializable("object");
		
		if(srl instanceof User){
			user = (User) srl;
			type = 0;
		}
		else if (srl instanceof FeedChannel){
			channel = (FeedChannel) srl;
			type = 1;
		}
				
		return new AlertDialog.Builder(getActivity())
			.setIcon(R.drawable.ic_launcher)
			.setTitle(title)
			.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(type == 0)
						((MainActivity)getActivity()).deleteFollowing(user);
					else if(type == 1)
						((MainActivity)getActivity()).acceptSuggestion(channel);
				}
			})
			.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.create();
	}
}
