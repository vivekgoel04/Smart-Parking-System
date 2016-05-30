// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.example.vivekgoel.mapdemo.singletonclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DatabaseSingleton {
	//Variables for Edit
	boolean edit = false;
	long editRowId = 0;
	
	private static DatabaseSingleton myDb = new DatabaseSingleton();
	private DatabaseSingleton(){}
	
	public static DatabaseSingleton getInstance() {
		return myDb;
	}
	
	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBHelper";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	/*
	 * CHANGE 1:
	 */
	// TODO: Setup your fields here:
	public static final String KEY_FACE="face";
	public static final String KEY_LATTITUDE = "position";
	public static final String KEY_LONGITUDE = "datetime";
	public static final String KEY_SLOTNO = "message";
	
	// TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
	public final int COL_FACE = 1;
	public final int COL_LATTITUDE = 2;
	public final int COL_LONGITUDE = 3;
	public final int COL_SLOTNO = 4;
	

	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID,KEY_FACE, KEY_LATTITUDE, KEY_LONGITUDE,KEY_SLOTNO};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "mainTable";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION =1;
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			
			/*
			 * CHANGE 2:
			 */
			// TODO: Place your fields here!
			// + KEY_{...} + " {type} not null"
			//	- Key is the column name you created above.
			//	- {type} is one of: text, integer, real, blob
			//		(http://www.sqlite.org/datatype3.html)
			//  - "not null" means it is a required field (must be given a value).
			// NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
			+ KEY_FACE + ","
			+ KEY_LATTITUDE + " string not null, "
			+ KEY_LONGITUDE + " string not null,"
			+ KEY_SLOTNO
			
			// Rest  of creation:
			+ ");";
	
	// Context of application who uses us.
	private Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	
	
	public void Helper(Context ctx) {
		context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	
	
	// Open the database connection.
	public DatabaseSingleton open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String face, String lattitude, String longitude, String slotno) {
		/*
		 * CHANGE 3:
		 */		
		// TODO: Update data in the row with new fields.
		// TODO: Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FACE, face);
		initialValues.put(KEY_LATTITUDE, lattitude);
		initialValues.put(KEY_LONGITUDE, longitude);
		initialValues.put(KEY_SLOTNO, slotno);
		
		
		
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	//Get position from list view and return corresponding row id.
	public long getRowId(int position) {
		Cursor c = getAllRows();
		int i=0;
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			while (i!=position) {
				c.moveToNext();
				i++;
			}
		}
		long Id = c.getLong((int) rowId);
		c.close();
		return(Id);
	}
	public String getFace(long rowId) {
		Cursor c = getRow(rowId);
		return(c.getString(c.getColumnIndex(KEY_FACE)));
	}
	public String getLattitude(long rowId) {
		Cursor c = getRow(rowId);
		return(c.getString(c.getColumnIndex(KEY_LATTITUDE)));
	}
	public String getLongitude(long rowId) {
		Cursor c = getRow(rowId);
		return(c.getString(c.getColumnIndex(KEY_LONGITUDE)));
	}
	public String getSlotno(long rowId) {
		Cursor c = getRow(rowId);
		return(c.getString(c.getColumnIndex(KEY_SLOTNO)));
	}
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String face, String lattitude, String longitude, String slotno) {
		String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
		// TODO: Update data in the row with new fields.
		// TODO: Also change the function's arguments to be what you need!
		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_FACE, face);
		newValues.put(KEY_LATTITUDE, lattitude);
		newValues.put(KEY_LONGITUDE, longitude);
		newValues.put(KEY_SLOTNO, slotno);
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}