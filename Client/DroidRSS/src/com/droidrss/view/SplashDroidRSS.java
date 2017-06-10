package com.droidrss.view;

import com.droidrss.controller.Controller;
import com.droidrss.model.User;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashDroidRSS.
 */
public class SplashDroidRSS extends Activity {
	
	/** The init sound */
	private MediaPlayer mpSplash;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		mpSplash = MediaPlayer.create(this, R.raw.initsound);
		mpSplash.start();
				
		Thread logoTimer = new Thread(){
			public void run(){
				try{
					Controller controller = new Controller(getApplicationContext());
					int logoTimer = 0;
					
					while(logoTimer < 2000){
						sleep(100);
						logoTimer+= 100;						
					}
					
					//We will check here if there's a user saved into SQLite DB...
					User user = controller.getUser();
					
					if(user == null){
						Intent intent = new Intent(getApplicationContext(), StartActivity.class);
						startActivity(intent);
					}
					
					else
						startActivity(new Intent(getApplicationContext(), MainActivity.class));
						
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				finally{
					finish();
				}
				
			}
			
		};
		
		logoTimer.start();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mpSplash.release();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mpSplash.pause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mpSplash.start();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

}
