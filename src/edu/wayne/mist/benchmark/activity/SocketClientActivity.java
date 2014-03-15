package edu.wayne.mist.benchmark.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import edu.wayne.mist.benchmark.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class SocketClientActivity extends Activity {
	private String serverAddress = "hch.im";
	private int serverPort = 8081;
	private int period = 1000;
	private int times = 1;
	private int sended = 0;
	
	private ClientThread client = null;
	private String sendMsg = "";
	
	private EditText serverText, portText, periodText, sizeText, reqSizeText, timesText;
	private Button sockButton, stopButton;
    private Handler eventHandler = new Handler();
	private RadioButton byteRadio, kbRadio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_socket);
		serverText = (EditText)this.findViewById(R.id.serverEditText);
		portText = (EditText)this.findViewById(R.id.portEditText);
		periodText = (EditText)this.findViewById(R.id.periodEditText);
		sizeText = (EditText)this.findViewById(R.id.msgSizeEditText);
		reqSizeText = (EditText)this.findViewById(R.id.reqSizeEditText);
		timesText = (EditText)this.findViewById(R.id.timesEditText);
		sockButton = (Button)this.findViewById(R.id.SocketButton);
		stopButton = (Button)this.findViewById(R.id.stopButton);
		byteRadio = (RadioButton)this.findViewById(R.id.byteRadio);; 
		kbRadio = (RadioButton)this.findViewById(R.id.kbRadio);;
		sockButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				serverAddress = serverText.getEditableText().toString();
				serverPort = Integer.valueOf(portText.getEditableText().toString());
				period = Integer.valueOf(periodText.getEditableText().toString());
				times = Integer.valueOf(timesText.getEditableText().toString());
				int size = Integer.valueOf(sizeText.getEditableText().toString());
				int reqSize = Integer.valueOf(reqSizeText.getEditableText().toString());
				
				if(kbRadio.isChecked()){
					size *= 1024;
					reqSize *= 1024;
				}
				
				StringBuffer buf = new StringBuffer();
				buf.append(reqSize).append(':');
				for(int i = 0; i < size; i++)
					buf.append('S');
				
				sendMsg = buf.toString();
				sended = 0;
				state = true;
				sockButton.setText("Sending");
				eventHandler.postDelayed(eventPeriodicTask, period * 1000);
			}
		});
		
		stopButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				state = false;
				sockButton.setText("Done");
			}
		});
		client = new ClientThread();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_socket, menu);
		return true;
	}

    @Override
	protected void onDestroy() {
    	state = false;
    	client.close();
		super.onDestroy();
	}

	class ClientThread extends Thread {
    	private Socket socket;
    	private PrintWriter out = null;
    	private BufferedReader in = null;
    	
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverAddress);
                socket = new Socket(serverAddr, serverPort);
                
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));                
            	out = new PrintWriter(new BufferedWriter(
		                    new OutputStreamWriter(socket.getOutputStream())),
	                    true);  
            	
            	while(socket.isConnected()){
            		if(in.ready()){
            			String msg = in.readLine();
            			Log.i("Socket", "received: " + msg.length());
            		}
            		
            		Thread.sleep(100);
            	}
            	
            } catch (Exception e1) {
                e1.printStackTrace();
            }finally{
            	close();
            }
        }
        
        public void sendMessage(String msg){        	
        	if(socket != null && !socket.isClosed()){
        		out.write(msg + "\r\n");
        		out.flush();
        		Log.i("Socket", "Sent: " + msg);
        	}
        }
        
        public void close(){
        	try{
        		if(in != null)
        			in.close();
            	if(out != null)
            		out.close();
            	if(socket != null && socket.isConnected())
            		socket.close();
            	in = null;
            	out = null;
            	socket = null;
        	}catch(Exception ex){}        	
        }
        
        public boolean connected(){
        	return socket != null && socket.isConnected();
        }
    }
	
    private boolean state = false;
    private Runnable eventPeriodicTask = new Runnable() {
        public void run() {    
        	if(client.connected() == false){
        		client.start();
        		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}        	
            
        	client.sendMessage(sendMsg);
            sended++;
            if(sended < times && state){
            	eventHandler.postDelayed(eventPeriodicTask, period * 1000);
            }else{
    			sockButton.setText("Done");
           	}
        }
    };
}
