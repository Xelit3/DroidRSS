/* WebServiceConnection.java
* @author: Xavi Rueda
* @date: 5-14-2013
* @description: This class is a helper to create, update an get the database for a manager
*/
package com.droidrss.model.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class DBHelper.
 */
public class DBHelper extends SQLiteOpenHelper {
	
	/**
	 * Instantiates a new dB helper.
	 *
	 * @param context the context
	 * @param name the name
	 * @param factory the factory
	 * @param version the version
	 * @param errorHandler the error handler
	 */
	public DBHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}
	
	/**
	 * Instantiates a new dB helper.
	 *
	 * @param context the context
	 * @param name the name
	 * @param factory the factory
	 * @param version the version
	 */
	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBManager.CREATE_DB);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBManager.DROP_DB);
		db.execSQL(DBManager.CREATE_DB);
	}
	
}
