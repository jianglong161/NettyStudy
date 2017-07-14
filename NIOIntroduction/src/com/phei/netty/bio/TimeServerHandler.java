package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.omg.Messaging.SyncScopeHelper;

public class TimeServerHandler implements Runnable{
	private Socket socket;
	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}
	@Override	
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream());
			String currentTime = null;
			String body = null;
			while(true){
				body = in.readLine();
				if(body == null)
					break;
				System.out.println("The time server receive order" + body);
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? 
						sf.format(new Date()) : "BAD ORDER";
						       
			}
		} catch (Exception e) {
			if(in != null){
				try {
					in.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if(out != null){
				out.close();
				out = null;
			}
			if(this.socket != null){
				try {
					this.socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				this.socket = null;
			}
		}
	}

}
