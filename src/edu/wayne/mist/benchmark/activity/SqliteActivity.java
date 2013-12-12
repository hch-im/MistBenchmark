package edu.wayne.mist.benchmark.activity;

import java.util.ArrayList;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.db.Location;
import edu.wayne.mist.benchmark.db.LocationDAO;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SqliteActivity extends Activity {
	private ArrayList<Location> locs;
	private LocationDAO dao;
	private TextView status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sqlite);
		dao = new LocationDAO(this.getBaseContext());
		 
		Button store = (Button)this.findViewById(R.id.storeButton);
		Button retrieve = (Button)this.findViewById(R.id.retrieveButton);
		Button clear = (Button)this.findViewById(R.id.clearButton);
		status = (TextView)this.findViewById(R.id.statusTextView);
		
		store.setOnClickListener(storeListener);
		retrieve.setOnClickListener(retListener);
		clear.setOnClickListener(clearListener);
		generateData();
		status.setText("Status: " + "generated " + locs.size() + " data records.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sqlite, menu);
		return true;
	}

	private void generateData(){
		locs = new ArrayList<Location>();
		Location loc;
		for(int i = 0; i < 1000 ; i++){
			loc = new Location();
			loc.setBssid("12:34:56:78:90:"+i);
			loc.setSsid("AP " + i);
			loc.setNetid(i);
			loc.setLatitude(i + 181.08933f);
			loc.setLongitude(i + 54.34566f);
			loc.setAccuracy(20 + i);
			locs.add(loc);
		}
		
		dao.remove(null, null);
	}
	
	private View.OnClickListener storeListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			long time = System.currentTimeMillis();
			for(Location loc: locs){
				dao.insert(loc);
			}
			status.setText("Status: store finished." + (System.currentTimeMillis() - time));
		}
	};
	
	private View.OnClickListener retListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Location n;
			long time = System.currentTimeMillis();
			for(Location loc: locs){
				n = dao.findByBssid(loc.getBssid());
			}			
			status.setText("Status: retrieve finished." + (System.currentTimeMillis() - time));
		}
	};	
	
	private View.OnClickListener clearListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dao.remove(null, null);
			status.setText("Status:  clear finished.");
		}
	
	};	
}
