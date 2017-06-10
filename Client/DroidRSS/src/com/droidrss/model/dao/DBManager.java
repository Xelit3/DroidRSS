/* DBManager.java
* @author: Xavi Rueda
* @date: 5-10-2013
* @description: This class provides methods to manage data of the SQLite database
*/

package com.droidrss.model.dao;

import com.droidrss.model.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

// TODO: Auto-generated Javadoc
/**
 * The Class DBManager.
 */
public class DBManager {
	
	/** The user db. */
	private SQLiteDatabase userDB;
	
	/** The helper. */
	private DBHelper helper;
	
	/** The context. */
	private Context context;
	
	/** Static strings, statments for database. */
	protected static String CREATE_DB = "CREATE TABLE users(user TEXT PRIMARY KEY, name TEXT, token TEXT, mail TEXT)";
	protected static String DROP_DB = "DROP TABLE IF EXISTS users";
	protected static String INSERT_USER = "INSERT INTO users (user, name, token, mail) VALUES ('%s', '%s', '%s', '%s')";
	protected static String GET_USER = "SELECT * FROM users";
	
	/**
	 * Instantiates a new dB manager.
	 *
	 * @param context the context
	 */
	public DBManager(Context context){
		this.context = context;
		this.helper = new DBHelper(this.context, "Users", null, 1);
	}
	
	/**
	 * Insert the user into SQLite.
	 *
	 * @param toAdd the to add
	 */
	public void insert(User toAdd)
    {
		userDB = helper.getReadableDatabase();
		
		if (userDB != null){
    		userDB.execSQL(String.format(INSERT_USER, toAdd.getUsername(), toAdd.getSurname(), toAdd.getToken(), toAdd.getMail()));
    	}
    		
		userDB.close();
    }

	/**
	 * Gets the user from SQLite.
	 *
	 * @return the user
	 */
	public User getUser() {
		Cursor cursor;
    	userDB = helper.getReadableDatabase();
    	User currentUser = new User();
    	
    	try{
	    	cursor = userDB.rawQuery(GET_USER, null);
	    	
	    	if (cursor.moveToFirst()){
	    		currentUser.setUsername(cursor.getString(0));
	    		currentUser.setSurname(cursor.getString(1));
	    		currentUser.setToken(cursor.getString(2));
	    		currentUser.setMail(cursor.getString(3));
	    	}
	    	else
	    		currentUser = null;
	    		
	    	cursor.close();
	    }
    	catch(SQLiteException sqle){
    		
    	}
    	
		userDB.close();
    	
    	return currentUser;
	}
	
}
