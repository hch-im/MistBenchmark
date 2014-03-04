package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WakeLockActivity extends Activity {
	private WakeLock wakeLock;
	private Button st;
	private boolean wakelockAcquired = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wakelock);
        st = (Button) this.findViewById(R.id.startPartial);
        
        st.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Wakelock")
			public void onClick(View v) {
				PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
				if(wakelockAcquired == false){
	            	wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyPartialWakeLock");
	            	wakeLock.acquire();
	            	Log.i("Mist", "partialwakelock start");
	            	wakelockAcquired = true;
	            	st.setText("Release Partial Wakelock");
				}else{
					if(wakeLock != null  && wakeLock.isHeld()){ 
						wakeLock.release();
						wakelockAcquired = false;
						st.setText("Acquire Partial Wakelock");
						Log.i("Mist", "partialwakelock release");
					}
				}
            }
        }); 
	}

	@SuppressLint("Wakelock")
	@Override
	protected void onDestroy() {
		if(wakeLock != null && wakeLock.isHeld()){
			wakeLock.release();
			Log.i("wakelock", "partialwakelock release");
		}
		super.onDestroy();
	}
	
	
}
