package com.droidrss.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.droidrss.model.FeedItem;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedItemShowActivity.
 */
public class FeedItemShowActivity extends Activity {
	
	/** TextViews for show all data of a feed. */
	private TextView tvTitle, tvAuthor, tvDate, tvContent, tvUrl;
	
	/** A drawable to get images from the feed. */
	private Drawable d = null;
	
	/** The spanned text + images for the TextView. */
	private Spanned s;
	
	/** The feed to show. */
	private FeedItem feed;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feed_show_activity);
		
		tvTitle = (TextView) findViewById(R.id.fsi_tv_title);
		tvAuthor = (TextView) findViewById(R.id.fsi_tv_author);
		tvDate = (TextView) findViewById(R.id.fsi_tv_pubdate);
		tvContent = (TextView) findViewById(R.id.fsi_tv_content);
		tvUrl = (TextView) findViewById(R.id.fsi_tv_url);
		
		tvContent.setMovementMethod(new ScrollingMovementMethod());
		
		Bundle datosIn = this.getIntent().getExtras();
        
        if (datosIn == null)
        	return;
        
        feed = (FeedItem) datosIn.getParcelable("feed");
        
        tvTitle.setText(Html.fromHtml(feed.getTitle()));
        tvAuthor.setText(feed.getAuthor());
        tvDate.setText(feed.getPubDate());
        tvContent.setText(Html.fromHtml(feed.getContent()));
        tvUrl.setText(Html.fromHtml("<a href='" + feed.getUrl() + "'>Visitar enlace</a>"));
    }
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onPostResume()
	 */
	@Override
	protected void onPostResume() {
		super.onPostResume();
			tvContent = (TextView) findViewById(R.id.fsi_tv_content);
			tvContent.setTextIsSelectable(true);
			Thread threadAux = new Thread (new Runnable(){
	        	public void run() {
	        		s = Html.fromHtml(feed.getContent(),getImageHTML(),null);
	        	}
	        });
			threadAux.setPriority(Thread.MAX_PRIORITY);
	        threadAux.start();
	        try {
				threadAux.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        tvContent.setText(s); 
    }
	
	/**
	 * Gets the image html.
	 *
	 * @return the image html
	 */
	protected ImageGetter getImageHTML(){
		ImageGetter ig = new ImageGetter(){
			public Drawable getDrawable(final String source) {
				Thread threadAux = new Thread (new Runnable(){
					@Override
					public void run() {
						InputStream is;
						try {
							is = new URL(source).openStream();
							d = Drawable.createFromStream(is, "src name");
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				});
				threadAux.start();
				try {
					threadAux.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(d!=null){
					d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
				}
				return d;
			}
		};
		return ig;
	}
}

