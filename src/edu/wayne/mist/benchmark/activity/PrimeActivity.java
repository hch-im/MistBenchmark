package edu.wayne.mist.benchmark.activity;

import java.util.ArrayList;

import edu.wayne.mist.benchmark.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class PrimeActivity extends Activity {
    private Button st;
    private EditText threadNumEditText, primeNumEditText;
    private RadioButton partialButon, fullButon;
    private int primeNum = 0;
    private int threadNum = 0;
    private int finishedThreadNum = 0;
    private boolean onGoing = false;
    private ArrayList<Thread> threads = new ArrayList<Thread>();
    private WakeLock wl;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prime);
		st = (Button)this.findViewById(R.id.primeButton);
		threadNumEditText = (EditText)this.findViewById(R.id.primeThreadNumEditText);
		primeNumEditText= (EditText)this.findViewById(R.id.primeNumEditText);
		partialButon = (RadioButton)this.findViewById(R.id.primePartialWakeLock);
		fullButon = (RadioButton)this.findViewById(R.id.primeFullWakelock);
		
		st.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(onGoing) return;
				onGoing = true;
				PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);		
				if(partialButon.isChecked())
					wl = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyPartialWakeLock");
				else if(fullButon.isChecked())
					wl = mgr.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyPartialWakeLock");
				
            	wl.acquire();
            	
				primeNum = Integer.valueOf(primeNumEditText.getText().toString());
				threadNum = Integer.valueOf(threadNumEditText.getText().toString());
				finishedThreadNum = 0;
				st.setText("Finding...");	
				st.setEnabled(false);
				
				for(int i = 0; i < threadNum ; i++){
					Thread t = new Thread(primeThread);
					threads.add(t);
					t.start();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prime, menu);
		return true;
	}

		
    @Override
	protected void onDestroy() {
    	for(Thread t:threads){
    		t.interrupt();
    	}
		
		super.onDestroy();
	}



	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	finishedThreadNum++;
            	if(finishedThreadNum == threadNum){
            		st.setText("Done!");
            		onGoing = false;
    				st.setEnabled(true);
    				threads.clear();
    				if(wl != null && wl.isHeld())
    					wl.release();
            	}
            }
    };    
    
	private Runnable primeThread = new Runnable(){
		 public void run() {
			 int num = 0;
			 int lastNum = 2;
			 
			 while(num < primeNum){
				 lastNum = nextPrime(lastNum);
				 num++;
			 }
		     handler.sendEmptyMessage(0);	            
	    }
		 
		 private int nextPrime(int current){
			 boolean found = false;
			 while(!found){
				 current++;
				 int i;
				 for(i = 2; i < current; i++){
					 if(current % i == 0) {//not prime
						 break;
					 }
				 }
				 
				 if(i == current)
					 found = true;
			 }
			 return current;
		 }
	};	
}
