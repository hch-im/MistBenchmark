package edu.wayne.mist.benchmark.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
	private int sendSize = 0;
	private int reqSize = 0;
	private char unit = 'B'; //B:byte, K: kb, M:mb
	
	private EditText serverText, portText, periodText, sizeText, reqSizeText, timesText;
	private Button sockButton, stopButton;
    private Handler eventHandler = new Handler();
	private RadioButton byteRadio, kbRadio, mbRadio;
	private char[] buffer = new char[4096];
	private char[] sendBuffer = new char[4096];
	
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
		kbRadio = (RadioButton)this.findViewById(R.id.kbRadio);
		mbRadio = (RadioButton)this.findViewById(R.id.mbRadio);
		sockButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				serverAddress = serverText.getEditableText().toString();
				serverPort = Integer.valueOf(portText.getEditableText().toString());
				period = Integer.valueOf(periodText.getEditableText().toString());
				times = Integer.valueOf(timesText.getEditableText().toString());
				sendSize = Integer.valueOf(sizeText.getEditableText().toString());
				reqSize = Integer.valueOf(reqSizeText.getEditableText().toString());
				
				if(kbRadio.isChecked()){
					unit = 'K';
				}else if(mbRadio.isChecked()){
					unit = 'M';
				}else{
					unit = 'B';
				}
			
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
		
		for(int i = 0; i < sendBuffer.length; i++)
			sendBuffer[i] = 'S';
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
            	
            	if(socket.isConnected()){
            		readMessage();
            	}
            	
            } catch (Exception e1) {
                e1.printStackTrace();
            }finally{
            	close();
            }
        }
        
        public void sendMessage(){        	
        	if(socket != null && !socket.isClosed()){
        		out.write(String.valueOf(unit) + reqSize + "$");
        		int i = sendSize, len;
        		while(i > 0){
        			len = i > sendBuffer.length ? sendBuffer.length:i;
        			out.write(sendBuffer, 0, len);
        			i -= len;
        		}
        		out.write("\r\n");
        		out.flush();
        		Log.i("Socket", "Sent: " + sendSize);
        	}
        }
        
        private void readMessage() throws IOException{
        	int len;
        	while((len = in.read(buffer)) > 0){
            	Log.i("Socket", "received: " + len);
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
        	if(client == null || client.connected() == false){
        		if(client != null)
        			client.close();
        		client = null;        		
        		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		client = new ClientThread();
        		client.start();

        		try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}       		
        	}        	
            
        	client.sendMessage();
            sended++;
            if(sended < times && state){
            	eventHandler.postDelayed(eventPeriodicTask, period * 1000);
            }else{
    			sockButton.setText("Done");
           	}
        }
    };
}
