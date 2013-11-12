package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.service.MyLocationService;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class LocationActivity extends Activity {
	private static String tag = "MistBench-Location";
	private RadioGroup provider = null;
	private RadioGroup mode = null;
	private TextView accuracyText = null;
	private TextView resultText = null;	
	private SeekBar accuBar = null;
	private Button start = null;
	private MyLocationService locationService = null;
	
	int providerMode = 0;
	int usageMode = 0;
	int accuracy = 50;
	int updateTimes = 0;
	long startTime = 0;
	long endTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		provider = (RadioGroup) findViewById(R.id.myRadioGroup);
		provider.check(R.id.radioButtonGPS);
		
		mode = (RadioGroup) findViewById(R.id.myRadioGroup2);
		mode.check(R.id.radioButtonMode1);
		
		accuracyText = (TextView)this.findViewById(R.id.accTextView);
		resultText = (TextView)this.findViewById(R.id.resultTextView);
		
		accuBar = (SeekBar)this.findViewById(R.id.accBar);
		start = (Button)this.findViewById(R.id.startButton);
		
		start.setOnClickListener(startListener);
		accuBar.setOnSeekBarChangeListener(seekListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

	private View.OnClickListener startListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = provider.getCheckedRadioButtonId();
			View radioButton = provider.findViewById(id);
			providerMode = provider.indexOfChild(radioButton);
			
			id = mode.getCheckedRadioButtonId();
			radioButton = mode.findViewById(id);
			usageMode = mode.indexOfChild(radioButton);
			
			accuracy = accuBar.getProgress() * 10;
			if(accuracy == 0) accuracy = 10;
			
			locationService = new MyLocationService(getBaseContext(), providerMode, usageMode, accuracy);		
			startTime = System.currentTimeMillis();
			endTime = 0;
			if(locationService.startGetLocation(locListener)){
				start.setText("Get Location ...");
				start.setEnabled(false);
				updateTimes = 0;
			}
		}
	};
	
	private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub		
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			accuracyText.setText("Accuracy: " + progress * 10);
		}
	};	
	
	@Override
	protected void onPause() {
		if(locationService != null){
			locationService.removeListener(locListener);
		}
		
		super.onPause();
	}

	private LocationListener locListener = new LocationListener(){
		@Override
		public void onLocationChanged(Location loc) {
	    	Log.i(tag, "Location -- lat:" + loc.getLatitude() + " lon:" + loc.getLongitude());
	    	if(usageMode == 0){
				endTime = System.currentTimeMillis();
	    		locationService.removeListener(this);	
				start.setText("Start");
				start.setEnabled(true);
				resultText.setText("Result: " + (endTime - startTime) + "ms");
	    	}else if(usageMode == 1){
	    		updateTimes++;
	    		if(loc.getAccuracy() <= accuracy || updateTimes >= 50){
					endTime = System.currentTimeMillis();
		    		locationService.removeListener(this);	
					start.setText("Start");
					start.setEnabled(true);		    		
					resultText.setText("Result: " + (endTime - startTime) + "ms ; update " + updateTimes + " times");
	    		}	    		
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
	};
}
