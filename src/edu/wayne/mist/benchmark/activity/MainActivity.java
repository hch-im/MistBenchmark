package edu.wayne.mist.benchmark.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity{

	private ListView list = null;
	private static HashMap<String, Class> cases = new HashMap<String, Class>();
	static{
		cases.put("Location Service", LocationActivity.class);
		cases.put("SensorHub Test", SensorActivity.class);		
		cases.put("Wifi Info", WifiInfoActivity.class);	
		cases.put("Sqlite", SqliteActivity.class);
		cases.put("Cache", CacheActivity.class);		
	}
	
	private ArrayAdapter adapter;
	private Object[] items;
	
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
        Class cla = cases.get(items[position]);
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }
}
