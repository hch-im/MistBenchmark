package edu.wayne.mist.benchmark.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.NetworkInfo;
import android.util.Log;

public class WiFiStatusReceiver extends BroadcastReceiver {
	String Tag = "WiFi Status";

	@Override
	public void onReceive(Context arg0, Intent intent) {
		String action  = intent.getAction();
    if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
    {
    	Log.i(Tag, "Scan Finished, got results");
    }else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
    {
     int iTemp = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
       WifiManager.WIFI_STATE_UNKNOWN);
       checkState(iTemp);
    }else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION))
    {
    
     DetailedState state =WifiInfo.getDetailedStateOf((SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE));
    
     changeState(state);
    
    }
    else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
    {
        DetailedState state=
       ((NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
       changeState(state);
     }
   }
   private void changeState(DetailedState aState)
    {
     
   if (aState == DetailedState.SCANNING)
   {
	   Log.i(Tag, "SCANNING");
   }
   else if (aState == DetailedState.CONNECTING)
   {
	   Log.i(Tag, "CONNECTING");
   }else if(aState == DetailedState.OBTAINING_IPADDR)
   {
	   Log.i(Tag, "OBTAINING_IPADDR");
   }
   else if (aState == DetailedState.CONNECTED)
   {
	   Log.i(Tag, "CONNECTED");
   }
   else if (aState == DetailedState.DISCONNECTING)
   {
	   Log.i(Tag, "DISCONNECTING");
   }
   else if (aState == DetailedState.DISCONNECTED)
   {
	   Log.i(Tag, "DISCONNECTTED");
   }
   else if (aState == DetailedState.FAILED)
   {
	   Log.i(Tag,"FAILED");
   }
  }

  public void checkState(int aInt)
  {
   if (aInt == WifiManager.WIFI_STATE_ENABLING)
    {
    Log.i(Tag, "WIFI_STATE_ENABLING");
    }
   else if (aInt== WifiManager.WIFI_STATE_ENABLED)
   {
	   Log.i(Tag, "WIFI_STATE_ENABLED");
    }
    else if (aInt == WifiManager.WIFI_STATE_DISABLING)
    {
    	Log.i(Tag, "WIFI_STATE_DISABLING");
    }
    else if (aInt == WifiManager.WIFI_STATE_DISABLED)
    {
    	Log.i(Tag, "WIFI_STATE_DISABLED");
    }
  }

}
