package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CacheActivity extends Activity implements OnClickListener{
	private EditText interText;
	private EditText l1Text;
	private EditText l2Text;
    private TextView status;
    	
    private Button L1Button;
    private Button L2Button;
    private Button memButton;
    
	private long iter;  
	public final static int CACHELINE_SIZE = 64; //byte
	private int L1CacheSize = 0; //kb
	private int L2CacheSize = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache);
		interText = (EditText)findViewById(R.id.iterEditText);
		l1Text = (EditText)findViewById(R.id.cache1EditText);
		l2Text = (EditText)findViewById(R.id.cache2EditText);
		status = (TextView)findViewById(R.id.cacheStatus);
		L1Button = (Button)findViewById(R.id.L1Button);
		L2Button = (Button)findViewById(R.id.L2Button);		
		memButton = (Button)findViewById(R.id.memButton);
		L1Button.setOnClickListener(this);
		L2Button.setOnClickListener(this);
		memButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cpu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(checkInput() == false) return;
		status.setText("Status: started.");
		if(v == L1Button){
			int arrSize = CACHELINE_SIZE/4;
			int[] aLine = new int[arrSize];
			
		    int i = 0;
		    long sysStartTime = SystemClock.elapsedRealtime();		    
		    long start = System.currentTimeMillis();
		    for(int j = 0; j < iter; j++){
			    while(i < Integer.MAX_VALUE){
			          aLine[i % arrSize] = i;          
			          i++;
			     }		
			    i = 0;
		    }
		    long end = System.currentTimeMillis();		    
		    status.setText( sysStartTime+"-" +SystemClock.elapsedRealtime() +" last: " + (end - start) + "ms.");
		}else if(v == L2Button){
			int arrSize = (L1CacheSize * 1024 / CACHELINE_SIZE * 2);      
			int[][] array = new int[arrSize][CACHELINE_SIZE/4];
			
		    int i = 0;
		    long sysStartTime = SystemClock.elapsedRealtime();		    
		    long start = System.currentTimeMillis();
		    for(int j = 0; j < iter; j++){
				while(i < Integer.MAX_VALUE){
				          array[i % arrSize][0] = i;          
				          i++;
				}			
		    }
		    long end = System.currentTimeMillis();		    
		    status.setText( sysStartTime+"-" +SystemClock.elapsedRealtime() +" last: " + (end - start) + "ms.");		    
		}else if(v == memButton){
			int arrSize = (L2CacheSize * 1024 / CACHELINE_SIZE * 2);      
			int[][] array = new int[arrSize][CACHELINE_SIZE/4];
			
		    int i = 0;
		    long sysStartTime = SystemClock.elapsedRealtime();		    
		    long start = System.currentTimeMillis();
		    for(int j = 0; j < iter; j++){
				while(i < Integer.MAX_VALUE){
				          array[i % arrSize][0] = i;          
				          i++;
				}			
		    }
		    long end = System.currentTimeMillis();		    
		    status.setText( sysStartTime+"-" +SystemClock.elapsedRealtime() +" last: " + (end - start) + "ms.");		    			
		}
	}	
	
	private boolean checkInput(){
		String str = interText.getText().toString().trim();
		if(str.equals("")){
			status.setText("Status: wrong iteration times." );
			return false;
		}
		
		iter = Integer.parseInt(str);
		
		str = l1Text.getText().toString().trim();
		if(str.equals("")){
			status.setText("Status: wrong L1 cache size." );
			return false;
		}

		L1CacheSize = Integer.parseInt(str);
		
		str = l2Text.getText().toString().trim();
		if(str.equals("")){
			status.setText("Status: wrong L2 cache size." );
			return false;
		}

		L2CacheSize = Integer.parseInt(str);
		
		return true;
	}
}
