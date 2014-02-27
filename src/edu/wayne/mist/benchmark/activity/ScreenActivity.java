package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenActivity extends Activity {
	private TextView symbol;
	private Button cal;
	private EditText input;
	public View rootView;
	
	private String[] colorlist;
	private String[] colorcodes;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
	    
	    symbol = (TextView)findViewById(R.id.brightnessTextView);
        cal = (Button)findViewById(R.id.brightnessButton);
        input = (EditText)findViewById(R.id.brightnessEditText);
        
        
        cal.setOnClickListener(clickListener);
        
        int original = getScreenBrightness();
        symbol.setText("The brightness is " + original);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        rootView = symbol.getRootView();
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( 
        		this, R.array.colorlist, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        spinner.setAdapter(adapter);
        
        spinner.setOnItemSelectedListener(selectListenner);

        
        		
        		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.screen, menu);
		return true;
	}
	
	private OnItemSelectedListener selectListenner = new OnItemSelectedListener(){
		 public void onItemSelected(AdapterView<?> arg0, View arg1, 
			        int arg2, long arg3)

			        {

			        int index = arg0.getSelectedItemPosition();

			        // storing string resources into Array 
			        colorlist = getResources().getStringArray(R.array.colorlist);
			        colorcodes = getResources().getStringArray(R.array.colorcode);
			        
			       // Color.parseColor(colorcodes[index]);

			        rootView.setBackgroundColor(Color.parseColor(colorcodes[index]));
			       
			        }

			        public void onNothingSelected(AdapterView<?> arg0) { 
			        // do nothing

			        }

			        
	};
	
	
	private OnClickListener clickListener = new OnClickListener()
    {
		 @Override
		public void onClick(View v) {
			 
			 int bright = 0;
			 try{
				 bright = Integer.parseInt(input.getText().toString());
			 }catch(NumberFormatException ex){
			 }
			 
			 if(bright < 0) bright = 0;
			 if(bright > 255) bright = 255;
			 
			 if(isAutoBrightness())
				 stopAutoBrightness();
			
			 int old = getScreenBrightness();			 
			 setBrightness(bright);
			 saveBrightness(bright);

		    symbol.setText("Brightness old: " + old + " new:" +bright);
		}
     
    };
	
	private boolean isAutoBrightness() {
	    boolean automicBrightness = false;
	    try {
	        automicBrightness = Settings.System.getInt(getContentResolver(),
	                Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
	    } catch (SettingNotFoundException e) {
	        e.printStackTrace();
	    }
	    return automicBrightness;
	}
	
	private  int getScreenBrightness() {
	    int nowBrightnessValue = 0;
	    ContentResolver resolver = getContentResolver();
	    try {
	        nowBrightnessValue = android.provider.Settings.System.getInt(
	                resolver, Settings.System.SCREEN_BRIGHTNESS);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return nowBrightnessValue;
	}
	
	private void setBrightness( int brightness) {
	    WindowManager.LayoutParams lp = getWindow().getAttributes();
	    lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
	    getWindow().setAttributes(lp);
	 
	}
	
	private void stopAutoBrightness() {
	    Settings.System.putInt(getContentResolver(),
	            Settings.System.SCREEN_BRIGHTNESS_MODE,
	            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	
	private void saveBrightness(int brightness) {
	    Uri uri = android.provider.Settings.System
	            .getUriFor("screen_brightness");
	    android.provider.Settings.System.putInt(getContentResolver(), "screen_brightness",
	            brightness);
	    getContentResolver().notifyChange(uri, null);
	}
	
}
