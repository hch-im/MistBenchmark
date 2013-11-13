package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import edu.wayne.mist.benchmark.R.layout;
import edu.wayne.mist.benchmark.R.menu;
import java.util.Iterator;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener {
	private SensorManager sensorManager;
	private  EditText delay;
	private  List<Sensor> sensors;
	private  TextView accx;
	private  TextView accy;
	private  TextView accz;
	private  TextView gryx;
	private  TextView gryy;
	private  TextView gryz;
	private  TextView grax;
	private  TextView gray;
	private  TextView graz;
	private  TextView magx;
	private  TextView magy;
	private  TextView magz;
	private  TextView temx;
	private  TextView prex;
	private  TextView prox;
	private  TextView ligx;
	private  TextView ligy;
	private  TextView ligz;
	private  TextView humx;
	private  Button start;
	private  Button stop;
	private CheckBox localCheckAcc;
	private CheckBox localCheckGry;
	private CheckBox localCheckGra;
	private CheckBox localCheckTem;
	private CheckBox localCheckMag;
	private CheckBox localCheckPre;
	private CheckBox localCheckPro;
	private CheckBox localCheckLig;
	private CheckBox localCheckHum;
	private int delaytime = 20;
	private TextView info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	        WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_sensor);
	    
	    String sensorInfo = "";
	    
	    info = (TextView) findViewById(R.id.info);
	    start = (Button) findViewById(R.id.start);
	    stop = (Button) findViewById(R.id.stop);
	    delay = (EditText) findViewById(R.id.delayvalue);
	    		
	    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    
	     this.localCheckAcc = (CheckBox)findViewById(R.id.acc);
	     this.localCheckGry = (CheckBox)findViewById(R.id.gry);
	     this.localCheckGra = (CheckBox)findViewById(R.id.gra);
	     this.localCheckMag = (CheckBox)findViewById(R.id.mag);
	     this.localCheckTem = (CheckBox)findViewById(R.id.tem);
	     this.localCheckPre = (CheckBox)findViewById(R.id.pre);
	     this.localCheckPro = (CheckBox)findViewById(R.id.pro);
	     this.localCheckLig = (CheckBox)findViewById(R.id.lig);
	     this.localCheckHum = (CheckBox)findViewById(R.id.hum);
	    this.accx = ((TextView)findViewById(R.id.accx));
	    this.accy = ((TextView)findViewById(R.id.accy));
	    this.accz = ((TextView)findViewById(R.id.accz));
	    this.gryx = ((TextView)findViewById(R.id.gryx));
	    this.gryy = ((TextView)findViewById(R.id.gryy));
	    this.gryz = ((TextView)findViewById(R.id.gryz));
	    this.magx = ((TextView)findViewById(R.id.magx));
	    this.magy = ((TextView)findViewById(R.id.magy));
	    this.magz = ((TextView)findViewById(R.id.magz));
	    this.grax = ((TextView)findViewById(R.id.grax));
	    this.gray = ((TextView)findViewById(R.id.gray));
	    this.graz = ((TextView)findViewById(R.id.graz));
	    this.prex = ((TextView)findViewById(R.id.prex));
	    this.prox = ((TextView)findViewById(R.id.prox));
	    this.ligx = ((TextView)findViewById(R.id.ligx));
	    this.ligy = ((TextView)findViewById(R.id.ligy));
	    this.ligz = ((TextView)findViewById(R.id.ligz));
	    this.temx = ((TextView)findViewById(R.id.temx));
	    this.humx = ((TextView)findViewById(R.id.humx));
	    
	    this.sensors = this.sensorManager.getSensorList(Sensor.TYPE_ALL);
	    Iterator localIterator = this.sensors.iterator();
	    while(localIterator.hasNext()){
				Sensor sen = (Sensor) localIterator.next();
				//System.out.println(sen.getName());
				sensorInfo += sen.getName()+" "+sen.getType() + '\n';
				switch (sen.getType()) {
				case Sensor.TYPE_ACCELEROMETER: 
					localCheckAcc.setChecked(true);
					break;
				case Sensor.TYPE_GYROSCOPE:
					localCheckGry.setChecked(true);
					break;
				case Sensor.TYPE_GRAVITY:
					localCheckGra.setChecked(true);
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					localCheckMag.setChecked(true);
					break;
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					localCheckTem.setChecked(true);
					break;
				case Sensor.TYPE_LIGHT:
					localCheckLig.setChecked(true);
					break;
				case Sensor.TYPE_PRESSURE:
					localCheckPre.setChecked(true);
					break;
				case Sensor.TYPE_PROXIMITY:
					localCheckPro.setChecked(true);
					break;
				case Sensor.TYPE_RELATIVE_HUMIDITY:
					localCheckHum.setChecked(true);
					break;
				default:
					break;
				}
			
			}
	    
	    info.setText(sensorInfo);
	   start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	 SensorActivity.this.sensorManager.unregisterListener(SensorActivity.this);
            	 delaytime = Integer.valueOf(delay.getText().toString());
            	 delaytime = delaytime*1000;
 		        
            	if (SensorActivity.this.localCheckAcc.isChecked())
                     SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),delaytime);
              
                if (SensorActivity.this.localCheckGry.isChecked())         
                    SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),delaytime);
                
                if(SensorActivity.this.localCheckGra.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),delaytime); 	
                
                if(SensorActivity.this.localCheckMag.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),delaytime); 	
               
                if(SensorActivity.this.localCheckTem.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),delaytime); 	
                
                if(SensorActivity.this.localCheckLig.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),delaytime); 	
                
                if(SensorActivity.this.localCheckPro.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),delaytime); 	
                
                if(SensorActivity.this.localCheckPre.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),delaytime); 	
                
                if(SensorActivity.this.localCheckHum.isChecked())
                	SensorActivity.this.sensorManager.registerListener(SensorActivity.this, SensorActivity.this.sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),delaytime); 	
                
            }
        });
	   stop.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   SensorActivity.this.sensorManager.unregisterListener(SensorActivity.this);
           }
           });
	}
	     
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensors, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:{
			if(event.accuracy==3){
			this.accx.setText("X: "+ event.values[0]);
			this.accy.setText("Y: "+ event.values[1]);
			this.accz.setText("Z: "+ event.values[2]);
			}
			break;
		}
		case Sensor.TYPE_GYROSCOPE:{
			if(event.accuracy==3){
			this.gryx.setText("X: "+ event.values[0]);
			this.gryy.setText("Y: "+ event.values[1]);
			this.gryz.setText("Z: "+ event.values[2]);
			}
			break;
		}
		case Sensor.TYPE_MAGNETIC_FIELD:{
			if(event.accuracy>=0){
			this.magx.setText("X: "+ event.values[0]);
			this.magy.setText("Y: "+ event.values[1]);
			this.magz.setText("Z: "+ event.values[2]);
			}
			break;
		}
		case Sensor.TYPE_GRAVITY:{
			if(event.accuracy>=1){
			this.grax.setText("X: "+ event.values[0]);
			this.gray.setText("Y: "+ event.values[1]);
			this.graz.setText("Z: "+ event.values[2]);
			}
			break;
		}
		case Sensor.TYPE_AMBIENT_TEMPERATURE:{
			//if(event.accuracy>=0){
			//if(event.values!=null && event.values[0]!=0.0F)
				switch(event.values.length){
				case 5:
				case 4:
				case 3:
				//this.temz.setText("Z: "+ event.values[2]);
				case 2:
				//this.temy.setText("Y: "+ event.values[1]);
				case 1:
			    this.temx.setText("X: "+ event.values[0]);
			    default:
			    break;
				}
				
		//	}
			break;
		}
		case Sensor.TYPE_LIGHT:{
			if(event.accuracy>=0){
				switch(event.values.length){
				case 5:
				case 4:
				case 3:
				//this.ligz.setText("Z: "+ event.values[2]);
				case 2:
				this.ligy.setText("Y: "+ event.values[1]);
				case 1:
			    this.ligx.setText("X: "+ event.values[0]);
			    default:
			    break;
				}
			//this.ligx.setText("X: "+ event.values[0]);
			}
			break;
		}
		case Sensor.TYPE_PRESSURE:{
			if(event.accuracy>=0)
			this.prex.setText("X: "+ event.values[0]);
			break;
		}
		case Sensor.TYPE_PROXIMITY:{
			if(event.accuracy>=0)
			this.prox.setText("X: "+ event.values[0]);
			break;
		}
		case Sensor.TYPE_RELATIVE_HUMIDITY:{
			if(event.accuracy>=0)
			this.humx.setText("X: "+ event.values[0]);
			break;
		}
		default: break;
		}
		
	}
	
	
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    // register this class as a listener for the orientation and
	    // accelerometer sensors
	    Iterator localIterator = this.sensors.iterator();
	    while (localIterator.hasNext())
	    {
	      Sensor localSensor = (Sensor)localIterator.next();
	      this.sensorManager.registerListener(this, localSensor, delaytime);
	    }
	    
	  }

	  @Override
	  protected void onPause() {
	    // unregister listener
	    super.onPause();
	    sensorManager.unregisterListener(this);
	  }

}
