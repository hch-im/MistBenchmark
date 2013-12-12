package edu.wayne.mist.benchmark.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocationDAO extends BaseDAO {
    private String tableName = "locations";
    private String[] columns = {"id", "bssid", "ssid", "netid", "latitude", "longitude", "accuracy"};
    
    public LocationDAO(Context context) {
    	super(context);
    }

//    public ArrayList<Location> getAvailableRecords()
//    {
//        ArrayList<Location> vos = new ArrayList<Location>();
//        SQLiteDatabase db = this.openConnection();
//        if(db == null) return null;
//        String args[] = {"0"};
//        
//        Cursor cursor = db.query(getTableName(),
//                getColums(), " state > ? ", args, null, null, "");
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            Location vo = (Location)readObject(cursor);
//            vos.add(vo);
//            cursor.moveToNext();
//        }
//        // Make sure to close the cursor
//        cursor.close();
//        return vos;     
//    }

    public Location findByBssid(String bssid)
    {
        SQLiteDatabase db = this.openConnection();
        if(db == null) return null;
        String[] args = {bssid};
        ArrayList<ValueObject> vos = select(getColums(), "bssid = ?", args, null, null, null);
        if(vos.size() == 0) return null;
        else
            return (Location)vos.get(0);
    }
    
    @Override
    public ValueObject readObject(Cursor cur) {
        Location r = new Location();
        int i = 0;
        r.setId(cur.getLong(i++));
        r.setBssid(cur.getString(i++));
        r.setSsid(cur.getString(i++));
        r.setNetid(cur.getInt(i++));
        r.setLatitude(cur.getFloat(i++));
        r.setLongitude(cur.getFloat(i++));
        r.setAccuracy(cur.getFloat(i++));

        return r;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String[] getColums() {
        return columns;
    }

    @Override
    public ContentValues wrapValues(ValueObject vo) {
        Location co = (Location)vo;
        ContentValues cv = new ContentValues();
        cv.put(columns[0], co.getId());
        cv.put(columns[1], co.getBssid());
        cv.put(columns[2], co.getSsid());
        cv.put(columns[3], co.getNetid());
        cv.put(columns[4], co.getLatitude());
        cv.put(columns[5], co.getLongitude());
        cv.put(columns[6], co.getAccuracy());
        
        return cv;
    }

}
