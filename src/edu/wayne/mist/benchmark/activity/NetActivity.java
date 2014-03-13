package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.R.layout;
import edu.wayne.mist.benchmark.R.menu;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class NetActivity extends Activity {

	private TextView state;
	private TextView type;
	private TextView subType;

	private ConnectivityManager cm;	
	private boolean isRun = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_net);
		
		state = (TextView)this.findViewById(R.id.netStateTextView);
		type = (TextView)this.findViewById(R.id.netTypeTextView);
		subType = (TextView)this.findViewById(R.id.netSubTypeTextView);
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.net, menu);
		return true;
	}

	
	@Override
	protected void onDestroy() {
		isRun = false;
		super.onPause();
	}

	@Override
	protected void onStart() {		
		super.onResume();
		if(!isRun){
			isRun = true;
			Thread t = new Thread(mainThread);
			t.start();
		}
	}


	private Runnable mainThread = new Runnable(){
		 public void run() {
			 if(isRun == false) return;
			 
			 NetworkInfo info = cm.getActiveNetworkInfo();
			 if(info != null){
				 state.setText("Network Status: " + info.getState());
				 type.setText("Network Type: " + info.getTypeName());
				 subType.setText("Network SubType: " + info.getSubtypeName() );
			 }
			 try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	};
}
