package edu.wayne.mist.benchmark.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class MyLocationService extends Service implements LocationListener {
	private static final String tag = "MyLocationService";
    private final Context mContext;
    private int providerMode = 0;//0:gps, 1:network, 2: both
    private int usageMode = 0;//0, first, 1: fixed accuracy
    private int accuracy = 50;
    private Location location; // location
    private static final long minDistance = 0; // 10 meters
    private static final long minTime = 0; // 1 minute
    protected LocationManager locationManager;
    private int updateTimes = 0;

    public MyLocationService(Context context) {
        this.mContext = context;
    }

    public MyLocationService(Context context, int providerMode, int usageMode, int accuracy) {
        this.mContext = context;
        this.providerMode = providerMode;
        this.usageMode = usageMode;
        this.accuracy = accuracy;
        if(accuracy == 0) accuracy = 10;
    }
    
    public boolean startGetLocation(LocationListener listener) {
    	updateTimes = 0;
        if(listener == null) listener = this;
        
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        	boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);    	
            if (!isGPSEnabled || !isNetworkEnabled) {
            	showAlert();
            	return false;
            }
            
			switch(providerMode){
			case 0:
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, listener);			
				break;
			case 1:
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener);			
				break;
			case 2:
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime, minDistance, listener);			
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, listener);							
				break;
			} 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
	@Override
	public void onLocationChanged(Location loc) {
    	this.location = loc;
    	Log.i(tag, "Location -- lat:" + location.getLatitude() + " lon:" + location.getLongitude());
    	if(usageMode == 0){
    		locationManager.removeUpdates(this);	
    	}else if(usageMode == 1){
    		if(location.getAccuracy() <= accuracy || updateTimes >= 1000){
	    		locationManager.removeUpdates(this);	    		
    		}	    		
    		updateTimes++;
    	}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Location Service");
        alertDialog.setMessage("Please enable location service.");
  
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        alertDialog.show();
    }	
	
	public void removeListener(LocationListener listener){
		if(locationManager != null)
			locationManager.removeUpdates(listener);
	}
}
