/* NewAccountActivity.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;

import java.util.Observable;
import java.util.Observer;

import com.droidrss.controller.Controller;
import com.droidrss.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

// TODO: Auto-generated Javadoc
/**
 * The Class NewAccountActivity.
 */
public class NewAccountActivity extends Activity implements OnClickListener, Observer{

	/** The controller. */
	private Controller controller;
	
	/** The bt_add new user. */
	private Button bt_addNewUser;
	
	/** The result request. */
	private String resultRequest;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_profile);
		controller = new Controller(this.getApplicationContext());
		controller.addObserver(this);
		
		bt_addNewUser = (Button) findViewById(R.id.bt_addNewUser);
		bt_addNewUser.setOnClickListener(this);
		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		
		EditText et_user, et_username, et_mail, et_password;
		String user, username, mail, password = null;
		
		et_user = (EditText) findViewById(R.id.et_user);
		et_username = (EditText) findViewById(R.id.et_username);
		et_mail = (EditText) findViewById(R.id.et_mail);
		et_password = (EditText) findViewById(R.id.et_password);
		
		user = et_user.getText().toString();
		username = et_username.getText().toString();
		mail = et_mail.getText().toString();
		password = et_password.getText().toString();
		
		if(!user.isEmpty() && !password.isEmpty()){
			
			
			User newUser = new User(username, user, password, mail);
			
			controller.createNewAccount(newUser);
			
			if(resultRequest.equals("DONE"))
				setResult(RESULT_OK, intent);
			
			else
				setResult(RESULT_CANCELED, intent);
				
		}
		
		finish();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object data) {
		resultRequest = data.toString();
	}

}
