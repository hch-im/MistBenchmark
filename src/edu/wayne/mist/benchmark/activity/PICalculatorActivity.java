package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.util.Pi;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PICalculatorActivity extends Activity {
    private Button st;
    private EditText thread, digits;
    private int digitsNum;
    private int threadNum;
    private int finishedNum;
    
    @SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	finishedNum++;
            	if(finishedNum == threadNum)
            		st.setText("Done!");
            }
    };    
    
	private Runnable calpi = new Runnable(){
		 public void run() {
	            Pi.computePi(digitsNum);
		        handler.sendEmptyMessage(0);	            
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picalculator);
        st = (Button) this.findViewById(R.id.calPIButton);
        thread = (EditText) this.findViewById(R.id.threadEditText);
        digits = (EditText) this.findViewById(R.id.digitsEditText);		
        
        st.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              	if(thread.getText().toString().trim().equals(""))
            		thread.setText("1");
            	if(digits.getText().toString().trim().equals(""))
            		digits.setText("1000");
            	
            	threadNum = Integer.valueOf(thread.getText().toString());
            	digitsNum = Integer.valueOf(digits.getText().toString());
            	if(digitsNum > 100000) digitsNum = 100000;
            	
	       		st.setText("Calculating...");
	       		finishedNum = 0;
	       		
	       		for(int i = 0; i< threadNum; i++)
	       		 {
	       			Thread thread = new Thread(calpi);
	       			thread.start();
	       		 }
            }
        });        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picalculator, menu);
		return true;
	}

}
