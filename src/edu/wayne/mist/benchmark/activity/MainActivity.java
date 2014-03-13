package edu.wayne.mist.benchmark.activity;

import java.util.LinkedHashMap;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity{

	private ListView list = null;
	@SuppressWarnings("rawtypes")
	private static LinkedHashMap<String, Class> cases = new LinkedHashMap<String, Class>();
	static{
		cases.put("Cache", CacheActivity.class);		
		cases.put("Math", MathActivity.class);		
		cases.put("PI Calculator", PICalculatorActivity.class);					
		cases.put("Prime", PrimeActivity.class);							
		cases.put("Location Service", LocationActivity.class);
		cases.put("SensorHub Test", SensorActivity.class);		
		cases.put("Wifi Info", WifiInfoActivity.class);	
		cases.put("Net Info", NetActivity.class);			
		cases.put("Sqlite", SqliteActivity.class);
		cases.put("Screen", ScreenActivity.class);	
		cases.put("WakeLock", WakeLockActivity.class);	
		cases.put("Socket", SocketClient.class);			
	}
	
	@SuppressWarnings("rawtypes")
	private ArrayAdapter adapter;
	private Object[] items;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		items = cases.keySet().toArray();
		list = getListView();
		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
		list.setAdapter(adapter);
	}

    @Override 
    public void onListItemClick(ListView l, View v, int position, long id) {
        @SuppressWarnings("rawtypes")
		Class cla = cases.get(items[position]);
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }
}
