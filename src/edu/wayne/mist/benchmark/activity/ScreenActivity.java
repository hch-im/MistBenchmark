package edu.wayne.mist.benchmark.activity;

import edu.wayne.mist.benchmark.R;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.app.Activity;
import android.content.ContentResolver;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScreenActivity extends Activity {
	private TextView symbol;
	private Button cal;
	private EditText input;
	 
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.screen, menu);
		return true;
	}
	
	
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
