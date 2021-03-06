package com.phei.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 同步阻塞IO的TimerServer
 * @author Still2Almost
 *
 */
public class TimeServer {
	public static void main(String[] args) throws IOException {
		int port = 8080;
		if(args != null && args.length > 0){
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				//采用默认值
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port" + port);
			Socket socket = null;
			socket = server.accept();
			while(true){
				
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if(server != null){
				System.out.println("The time server is close");
				server.close();
				server =null;
			}
		}
	}
}
