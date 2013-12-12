package edu.wayne.mist.benchmark.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper{

    public static final String TABLE_CONFIG = "locations";
    
    private static final String DATABASE_NAME = "mistbenchmark.db";
    private static final int DATABASE_VERSION = 3;
    
    // Database creation sql statement
    private static final String CONFIG_CREATE = "create table "
            + TABLE_CONFIG + "(id integer primary key autoincrement not null, bssid text not null unique, ssid text not null, netid int, latitude real, longitude real, accuracy real)";
    
    public SqliteHelper(Context context) {
    	super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" +  DATABASE_NAME, null, DATABASE_VERSION );
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONFIG_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
