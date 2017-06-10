/* StartActivity.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: 
*/

package com.droidrss.view;

import java.util.Observable;
import java.util.Observer;

import com.droidrss.controller.Controller;
import com.droidrss.model.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class StartActivity.
 */
public class StartActivity extends Activity implements OnClickListener, Observer{

	/** The controller. */
	private Controller controller;
	
	/** The et_password. */
	EditText et_user, et_password;
	
	/** The bt_new account. */
	Button bt_login, bt_newAccount;
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		controller = new Controller(this.getApplicationContext());
		controller.addObserver(this);
		
		et_user = (EditText) findViewById(R.id.et_login_user);
		et_password = (EditText) findViewById(R.id.et_login_password);
		bt_login = (Button) findViewById(R.id.btn_login);
		bt_newAccount = (Button) findViewById(R.id.btn_newProfile);
		
		bt_login.setOnClickListener(this);
		bt_newAccount.setOnClickListener(this);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_basic, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		
		case R.id.mi_about:
			Intent intent = new Intent(this, AboutDroidRSSActivity.class);
		    startActivity(intent);
			break;
		
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		
			case R.id.btn_login:
				controller.loginUser(et_user.getText().toString(), et_password.getText().toString());
				break;
				
			case R.id.btn_newProfile:
				Intent intent = new Intent(this, NewAccountActivity.class);
			    startActivityForResult(intent, 1234);
				break;
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==1234 && resultCode==RESULT_OK) {
            
			startMainActivity();
		}
		
		else showMsg(getString(R.string.maAlert_failCreatingAccoung));
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof String) {
			showMsg(String.valueOf(arg1));
		}
		else if (arg1 instanceof User){
			User user = (User) arg1;
			
			if(user.getUsername() != null){
				controller.setUserinDB(user);
				startMainActivity();
			}
			else
				showMsg(getString(R.string.maAlert_failLogin));
		}
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
	
	/**
	 * Start main activity.
	 */
	private void startMainActivity(){
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	}
	
}