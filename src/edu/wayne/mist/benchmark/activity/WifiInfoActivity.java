package edu.wayne.mist.benchmark.activity;

import java.math.BigInteger;
import java.net.InetAddress;

import edu.wayne.mist.benchmark.R;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.EditText;

public class WifiInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_info);
		EditText text = (EditText)this.findViewById(R.id.wifiText);
		WifiManager manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		if(info != null){
			StringBuffer buff = new StringBuffer();
			buff.append("Network ID: ").append(info.getNetworkId()).append("\r\n");			
			buff.append("SSID: ").append(info.getSSID()).append("\r\n");
			buff.append("BSSID: ").append(info.getBSSID()).append("\r\n");
			try{
				byte[] bytes = BigInteger.valueOf(info.getIpAddress()).toByteArray();			
				InetAddress address = InetAddress.getByAddress(bytes);
				buff.append("IP: ").append(address.getHostAddress()).append("\r\n");				
			}catch(Exception ex){}
			buff.append("Link Speed: ").append(info.getLinkSpeed()).append("Mbps\r\n");			
			buff.append("MAC: ").append(info.getMacAddress()).append("\r\n");
			buff.append("RSSI: ").append(info.getRssi()).append("\r\n");	
			text.setText(buff.toString());
		}else{
			text.setText("Cannot acquire wifi information!");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi_info, menu);
		return true;
	}

}
