package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MathActivity extends Activity {
    private Button add, min,mul,div;
    private EditText iterationTimesEditText;
    private int iterationTimes;
    private TextView status;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_math);
		
        add = (Button)findViewById(R.id.addButton);
        min = (Button)findViewById(R.id.minusButton);
        mul = (Button)findViewById(R.id.multiplyButton);
        div = (Button)findViewById(R.id.divideButton);
        
        add.setOnClickListener(new MyOnClickListener());
        min.setOnClickListener(new MyOnClickListener());
        mul.setOnClickListener(new MyOnClickListener());
        div.setOnClickListener(new MyOnClickListener());
        
        iterationTimesEditText =(EditText)this.findViewById(R.id.maxIterationEditText);
        status = (TextView)this.findViewById(R.id.maxStatusTextView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.math, menu);
		return true;
	}

    private void runCalculateAdd(){
		status.setText("Calculating...");
        int a , b;       
        
    	for(int k = 0; k < iterationTimes; k++){
            a = Integer.MIN_VALUE + 1; b = 2;    		
            while(a != Integer.MAX_VALUE){
                a = a + b;      
             } 
    	}
    	
		status.setText("Done...");
    }
    
    private void runCalculateMin(){
		status.setText("Calculating...");
        int a , b;        
    	for(int k = 0; k < iterationTimes; k++){
            a = Integer.MAX_VALUE - 1; b = 2;    		
            while(a != Integer.MIN_VALUE){
                a = a - b;      
             } 
    	}
    	
		status.setText("Done...");
    }
    
    private void runCalculateMul(){
		status.setText("Calculating...");
        double a , b, end; 
        end = Double.MAX_VALUE - 2e16;

    	for(int k = 0; k < iterationTimes; k++){
            a = 10e-300; b = 1.000001;    		
            while(a < end){
                a = a * b;      
             } 
    	}
    	
		status.setText("Done...");
    }
    
    private void runCalculateDiv(){
		status.setText("Calculating...");
        double a , b, end; 
        end = 10e-300;
        
    	for(int k = 0; k < iterationTimes; k++){
            a = Double.MAX_VALUE; b = 1.000003;    		
            while(a > end){
                a = a / b;      
             } 
    	}
    	
		status.setText("Done...");
    }
    
    class MyOnClickListener implements  OnClickListener
    {
		 @Override
		public void onClick(View v) {
			iterationTimes = Integer.valueOf(iterationTimesEditText.getText().toString());
			
			if(v==add){
			    runCalculateAdd();
			}else if(v==min){
				runCalculateMin();
			}else if(v==mul){
				runCalculateMul();
			}else if(v==div){
				runCalculateDiv();
			}
		}		
     
    }
}
