package edu.wayne.mist.benchmark;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PrimeActivity extends Activity {
    private Button st;
    private EditText threadNumEditText, sleepTimeEditText, primeNumEditText;
    private int primeNum = 0;
    private int threadNum = 0;
    private int sleepTime = 0;
    private int finishedThreadNum = 0;
    private boolean onGoing = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prime);
		st = (Button)this.findViewById(R.id.primeButton);
		threadNumEditText = (EditText)this.findViewById(R.id.primeThreadNumEditText);
		sleepTimeEditText= (EditText)this.findViewById(R.id.primeSleepTimeEditText);
		primeNumEditText= (EditText)this.findViewById(R.id.primeNumEditText);
		
		st.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(onGoing) return;
				onGoing = true;
				
				primeNum = Integer.valueOf(primeNumEditText.getText().toString());
				threadNum = Integer.valueOf(threadNumEditText.getText().toString());
				sleepTime = Integer.valueOf(sleepTimeEditText.getText().toString());
				finishedThreadNum = 0;
				st.setText("Finding...");	
				for(int i = 0; i < threadNum ; i++){
					Thread t = new Thread(primeThread);
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

    @SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	finishedThreadNum++;
            	if(finishedThreadNum == threadNum)
            		st.setText("Done!");
            }
    };    
    
	private Runnable primeThread = new Runnable(){
		 public void run() {
			 int num = 0;
			 int prime = 2;
			 
			 while(num < primeNum){
				 prime = nextPrime(prime);
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
					 try {
						Thread.sleep(sleepTime);
					 } catch (InterruptedException e) {
						e.printStackTrace();
					 }
					 if(current % i == 0) {//not prime
						 break;
					 }
				 }
				 
				 if(i == current)
					 found = true;
			 }
			 return 0;
		 }
	};	
}
