package edu.wayne.mist.benchmark.activity;

import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.IOException;
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.io.PrintWriter;  
import java.net.InetAddress;  
import java.net.NetworkInterface;
import java.net.Socket;  
import java.net.SocketException;
import java.net.UnknownHostException;  
import java.util.Enumeration;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.util.TCPClient;
  
import android.app.Activity;  
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;  
import android.os.Handler;
import android.os.PowerManager.WakeLock;
import android.util.Log;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.TextView;  
   
public class SocketClient extends Activity {  
    private static final String TAG = "Socket_Client";  
      
    private EditText mReceiverSize = null; 
    private EditText mSendSize = null;
    private EditText mDelay = null;
    private TextView tx1 = null;  
    private Button mButton = null;  
    
    private boolean state = false;
    private Handler eventHandler = new Handler();
    private int period=1000;
    
    private String toServer = "";
    
    Socket socket;
    int len = 0;
    private TCPClient mTcpClient;
 
    
   
    private Runnable eventPeriodicTask = new Runnable() {
        public void run() {
        
            if(state){
            	
            	if (mTcpClient != null) {
                    mTcpClient.sendMessage(toServer);
                }
				
            	eventHandler.postDelayed(eventPeriodicTask, period);
            }
            
           
            	
        }
    };   
    
	@Override
	public void onDestroy() {  
		state  =  false;
	    eventHandler.removeCallbacks(eventPeriodicTask);
	    if(mTcpClient!=null)
	    	mTcpClient.stopClient();
	
	    super.onDestroy();
	}

      
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_socketclient);

		mButton = (Button) findViewById(R.id.Button);
		mReceiverSize = (EditText) findViewById(R.id.ReceiveSize);
		mSendSize = (EditText) findViewById(R.id.SendSize);
		mDelay = (EditText) findViewById(R.id.Delay);
		

		tx1 = (TextView) findViewById(R.id.tx);
		
		Enumeration<NetworkInterface> interfaces = null;
	    try {
	        //the WiFi network interface will be one of these.
	        interfaces = NetworkInterface.getNetworkInterfaces();
	       for(; interfaces.hasMoreElements();){
	    	   NetworkInterface i = interfaces.nextElement();
	        Log.d(TAG, "display name " + i.getDisplayName());
	       }
	    } catch (SocketException e) {
	        
	    }
	    
	    NetworkInterface k = getWifiNetworkInterface((WifiManager)getSystemService(Context.WIFI_SERVICE));
	    Log.d(TAG, "wifi name " + k.getDisplayName());
		
        new connectTask().execute("");
        
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
            	char[] c = new char[1024];
				for (int i = 0; i < 1024; i++)
					c[i] = 'h';

				String content = "";
				for (int i = 0; i < Integer.valueOf(mSendSize.getText()
						.toString()); i++)
					content += String.valueOf(c);

				toServer = mReceiverSize.getText().toString() + ":"
						+ content + '\r' + '\n';
       
				 if(mDelay!=null)
                 	period = Integer.valueOf(mDelay.getText().toString());
					state = true;
					
					eventHandler.postDelayed(eventPeriodicTask, period);
                
                
 
             
            }
        });
        
 
    }
	
	public static NetworkInterface getWifiNetworkInterface(WifiManager manager) {
		 
	    Enumeration<NetworkInterface> interfaces = null;
	    try {
	        //the WiFi network interface will be one of these.
	        interfaces = NetworkInterface.getNetworkInterfaces();
	    } catch (SocketException e) {
	        return null;
	    }
	     
	    //We'll use the WiFiManager's ConnectionInfo IP address and compare it with
	    //the ips of the enumerated NetworkInterfaces to find the WiFi NetworkInterface.
	 
	    //Wifi manager gets a ConnectionInfo object that has the ipAdress as an int
	    //It's endianness could be different as the one on java.net.InetAddress
	    //maybe this varies from device to device, the android API has no documentation on this method.
	    int wifiIP = manager.getConnectionInfo().getIpAddress();
	     
	    //so I keep the same IP number with the reverse endianness
	    int reverseWifiIP = Integer.reverseBytes(wifiIP);       
	 
	    while (interfaces.hasMoreElements()) {
	 
	        NetworkInterface iface = interfaces.nextElement();
	 
	        //since each interface could have many InetAddresses...
	        Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
	        while (inetAddresses.hasMoreElements()) {
	            InetAddress nextElement = inetAddresses.nextElement();
	            int byteArrayToInt = byteArrayToInt(nextElement.getAddress(),0);
	             
	            //grab that IP in byte[] form and convert it to int, then compare it
	            //to the IP given by the WifiManager's ConnectionInfo. We compare
	            //in both endianness to make sure we get it.
	            if (byteArrayToInt == wifiIP || byteArrayToInt == reverseWifiIP) {
	                return iface;
	            }
	        }
	    }
	 
	    return null;
	}
	 
	public static final int byteArrayToInt(byte[] arr, int offset) {
	    if (arr == null || arr.length - offset < 4)
	        return -1;
	 
	    int r0 = (arr[offset] & 0xFF) << 24;
	    int r1 = (arr[offset + 1] & 0xFF) << 16;
	    int r2 = (arr[offset + 2] & 0xFF) << 8;
	    int r3 = arr[offset + 3] & 0xFF;
	    return r0 + r1 + r2 + r3;
	}
 
	
    public class connectTask extends AsyncTask<String,String,TCPClient> {
 
        @Override
        protected TCPClient doInBackground(String... message) {
 
            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();
 
            return null;
        }
 
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d(TAG,"The lenth is "+ values[0].length());
        }
    }
}



