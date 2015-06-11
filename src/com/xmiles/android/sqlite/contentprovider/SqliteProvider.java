package com.xmiles.android.sqlite.contentprovider;

import com.xmiles.android.sqlite.helper.*;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/** A custom Content Provider to do the database operations */
public class SqliteProvider extends ContentProvider{
	
	private static final String LOG = "Facebook_Places";
	//public static final String PROVIDER_NAME = "com.facetooth_light.android.provider";
	public static final String PROVIDER_NAME = "com.xmiles.android.provider";
	
	/** A uri to do operations on contacts table. A content provider is identified by its uri */
	public static final Uri CONTENT_URI_USER_PROFILE = Uri.parse("content://" + PROVIDER_NAME + "/UserProfile");
	public static final Uri CONTENT_URI_USER_PLACES = Uri.parse("content://" + PROVIDER_NAME + "/UserPlaces");
	public static final Uri CONTENT_URI_USER_FRIENDS = Uri.parse("content://" + PROVIDER_NAME + "/UserFriends");
	public static final Uri CONTENT_URI_CITY_BUSLINE = Uri.parse("content://" + PROVIDER_NAME + "/CityBusline");
	
	/** Constants to identify the requested operation */
	private static final int USER_PROFILE = 1;
	private static final int USER_PLACES = 2;
	private static final int USER_FRIENDS = 3;
	private static final int CITY_BUSLINE = 4;
	
	private static final UriMatcher uriMatcher ;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "UserProfile", USER_PROFILE);
		uriMatcher.addURI(PROVIDER_NAME, "UserPlaces", USER_PLACES);
		uriMatcher.addURI(PROVIDER_NAME, "UserFriends", USER_FRIENDS);
		uriMatcher.addURI(PROVIDER_NAME, "CityBusline", CITY_BUSLINE);
	}
	
	/** This content provider does the database operations by this object */
	DatabaseHelper mDatabaseHelper;
	
	/** A callback method which is invoked when the content provider is starting up */
	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}	

	/** A callback method which is invoked when delete operation is requested on this content provider */	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;		
		/*
		int cnt = 0;
		if(uriMatcher.match(uri)==CONTACT_ID){
			String contactID = uri.getPathSegments().get(1);
			cnt = mDatabaseHelper.del(contactID);			
		}
		return cnt;
		*/
	}
	
	@Override
	public String getType(Uri uri) {		
		return null;
	}
	
	/** A callback method which is invoked when insert operation is requested on this content provider */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri _uri=null;
		//long rowID = mDatabaseHelper.createUserProfile(values);		
	    switch (uriMatcher.match(uri)){
		    case USER_PROFILE:
		    	//------
		    	//mDatabaseHelper.resetUserProfile();
		    	//------
		    	long rowID_1 = mDatabaseHelper.createUserProfile(values);
				//Uri _uri=null;
				if(rowID_1>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_PROFILE, rowID_1);
				}else {		
					try {
						throw new SQLException("Failed to insert at TABLE_USER_PROFILE: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
		    case USER_PLACES:
		    	long rowID_2 = mDatabaseHelper.createUserPlaces(values);
				//Uri _uri=null;
				if(rowID_2>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_PLACES, rowID_2);
				}else {		
					try {
						throw new SQLException("Failed to insert at TABLE_USER_PLACES: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
		    
		    case CITY_BUSLINE:
		    	long rowID_3 = mDatabaseHelper.createCityBusline(values);
				//Uri _uri=null;
				if(rowID_3>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_CITY_BUSLINE, rowID_3);
				}else {		
					try {
						throw new SQLException("Failed to insert at TABLE_CITY_BUSLINE: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
			
			
			/*
		    case USER_FRIENDS:
		    	long rowID_3 = mDatabaseHelper.createUserFriends(values);
				//Uri _uri=null;
				if(rowID_3>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_FRIENDS, rowID_3);
				}else {		
					try {
						throw new SQLException("Failed to insert at TABLE_USER_FRIENDS: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			*/
			
		    }
		return _uri;	
	}

	/** A callback method which is invoked when insert operation is requested on this content provider */
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
	    int uriType = 0;
	    int insertCount = 0;
	    try {

	        uriType = uriMatcher.match(uri);
	        //SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();

	        switch (uriType) {
	        case USER_PLACES:
	            try {
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	/**
	                	 * Por algum motivo ainda desconhecido quando o array de 
	                	 * places é da ordem de 100-400 amostras, no metade das 
	                	 * primeiras 30 amostras, o json contém um valor = null
	                	 * apresentando o seguinte erro:
	                	 * sqlite returned: error code = 1, msg = near "null": syntax error, db=/data/data/com.facetooth_light.android/databases/b2h
	                	 * mas esse erro não chega a para o APP
	                	 */
	                	//Log.d(LOG, "values " + values.toString());
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_PLACES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;	            
	            
	        case USER_FRIENDS:
	            try {
	            	//----
	            	mDatabaseHelper.resetUserFriends();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_FRIENDS, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;
	            
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	        }
	        // getContext().getContentResolver().notifyChange(uri, null);
	    } catch (Exception e) {
	      // Your error handling
	    }

	    return insertCount;
	}	
	
	/** A callback method which is by the default content uri */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {	
		
		if(uriMatcher.match(uri)==USER_PLACES){
			//return mDatabaseHelper.getAllContacts();
			return mDatabaseHelper.get_UserPlaces();
		}else if (uriMatcher.match(uri)==USER_FRIENDS){
			return mDatabaseHelper.get_FriendList();
		
		}else if (uriMatcher.match(uri)==USER_PROFILE){
			return mDatabaseHelper.get_UserProfile();		

		}else if (uriMatcher.match(uri)==CITY_BUSLINE){
			return mDatabaseHelper.get_CityBusline();		
			
		}else{
			return null;
		}
		//return mDatabaseHelper.get_UserPlaces();
	}
	

	/** A callback method which is invoked when update operation is requested on this content provider */

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
		/*
		int cnt = 0;
		if(uriMatcher.match(uri)==CONTACT_ID){
			String contactID = uri.getPathSegments().get(1);
			cnt = mDatabaseHelper.update(contentValues, contactID);
			
		}
		return cnt;
		*/
		// TODO Auto-generated method stub
		return 0;

	}
}
