package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends Activity {

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	private String pi_string;
    private TextView tv;
    private ProgressDialog pd;
    
    private Button st;
    private EditText thread, digits;
    
    private Button add, min,mul,div;
	private EditText a,b;
    private TextView Re1,Re2,Re3,Re4;

    @Override
    public void onCreate(Bundle icicle) {
            super.onCreate(icicle);
            setContentView(R.layout.activity_calculator);

            st = (Button) this.findViewById(R.id.startPi);
            thread = (EditText) this.findViewById(R.id.thread);
            digits = (EditText) this.findViewById(R.id.digits);
            tv = (TextView) this.findViewById(R.id.main);
            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
            
            add = (Button)findViewById(R.id.add);
            min = (Button)findViewById(R.id.min);
            mul = (Button)findViewById(R.id.mul);
            div = (Button)findViewById(R.id.div);
            
          
            a = (EditText)findViewById(R.id.A);
            b = (EditText)findViewById(R.id.B);
            
          
          //  a.setText("A: " + A);
          //  b.setText("B: " + B);
            
            Re1 = (TextView)findViewById(R.id.Re1);
            Re2 = (TextView)findViewById(R.id.Re2);
            Re3 = (TextView)findViewById(R.id.Re3);
            Re4 = (TextView)findViewById(R.id.Re4);
            
            add.setOnClickListener(new MyOnClickListener());
            min.setOnClickListener(new MyOnClickListener());
            mul.setOnClickListener(new MyOnClickListener());
            div.setOnClickListener(new MyOnClickListener());
            
            st.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Do something in response to button click
                  	if(thread.getText().toString().equals(""))
                		thread.setText("1");
                	if(digits.getText().toString().equals(""))
                		digits.setText("1000");
                  	tv.setText("");
                  //	System.out.println(thread.getText().toString()+ ", "+ digits.getText().toString()+ ", "+ tv.getText().toString());
                	Runnable calpi = new Runnable(){
           			 public void run() {
           		            pi_string = Pi.computePi(Integer.valueOf(digits.getText().toString())).toString();
           		            handler.sendEmptyMessage(0);
           		    }
           		};
           		
           		for(int i = 0; i< Integer.valueOf(thread.getText().toString()); i++)
           		 {
           			Thread thread = new Thread(calpi);
           			thread.start();
           		 }
                }
            });
          
            
          //  tv = (TextView) this.findViewById(R.id.main);
          //  tv.setText("Press any key to start calculation");
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

    		Runnable calpi = new Runnable(){
    			 public void run() {
    		            pi_string = Pi.computePi(2000000).toString();
    		            handler.sendEmptyMessage(0);
    		    }
    		};
    	
            pd = ProgressDialog.show(this, "Working..", "Calculating Pi", true,
                            false);

            Thread thread = new Thread(calpi);
            thread.start();

            return super.onKeyDown(keyCode, event);
    }

   */

    private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                   // pd.dismiss();
                    tv.setText(pi_string);

            }
    };

    private double runCalculateAdd(){
    	double i= Double.valueOf(a.getText().toString());
    	double j = Double.valueOf(b.getText().toString());
    	int Freq = Integer.valueOf(thread.getText().toString());
    	int k = 0;
    	while(k<Freq){
    		i += j;
    		//result.setText("The result is " + i);
    		k++;
    	}
    	return i;
    }
    
    private double runCalculateMin(){
    	double i= Double.valueOf(a.getText().toString());
    	double j = Double.valueOf(b.getText().toString());
    	int Freq = Integer.valueOf(thread.getText().toString());
    	int k = 0;
    	while(k<Freq){
    		i -= j;
    		//result.setText("The result is " + i);
    		k++;
    	}
    	return i;
    }
    
    private double runCalculateMul(){
    	double i= Double.valueOf(a.getText().toString());
    	double j = Double.valueOf(b.getText().toString());
    	int Freq = Integer.valueOf(thread.getText().toString());
    	int k = 0;
    	while(k<Freq){
    		i = i*j;
    		//result.setText("The result is " + i);
    		k++;
    	}
    	return i;
    }
    
    private double runCalculateDiv(){
    	double i= Double.valueOf(a.getText().toString());
    	double j = Double.valueOf(b.getText().toString());
    	int Freq = Integer.valueOf(thread.getText().toString());
    	int k = 0;
    	while(k<Freq){
    		i = i/j;
    		//result.setText("The result is " + i);
    		k++;
    	}
    	return i;
    }
    
    class MyOnClickListener implements  OnClickListener
    {
    	double re = 0;;
		 @Override
		public void onClick(View v) {
			 
			if(v==add)
			{
			    re = runCalculateAdd();
				Re1.setText(Double.toString(re));
			}
			
			if(v==min)  //it did not work, the app not responding
			{
				re = runCalculateMin();
				Re2.setText(Double.toString(re));
			}
			
			if(v==mul)  //it did not work, the app not responding
			{
				re = runCalculateMul();
				Re3.setText(Double.toString(re));
			}
			
			if(v==div)  //it did not work, the app not responding
			{
				re = runCalculateDiv();
				Re4.setText(Double.toString(re));
			}
		}
		
     
    }

    
}
