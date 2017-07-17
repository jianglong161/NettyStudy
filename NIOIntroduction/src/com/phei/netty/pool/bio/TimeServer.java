package com.phei.netty.pool.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.phei.netty.bio.TimeServerHandler;

/**
 * α�첽io��TimerSever
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
				//����Ĭ��ֵ
			}
		}
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The timeServer is start in port :" + port);
			Socket socket = null;
			TimeServerHandelerExecutiPool pool = 
					new TimeServerHandelerExecutiPool(50, 1000);
			while(true){
				socket = server.accept();
				pool.execute(new TimeServerHandler(socket));
			}
			
		}finally {
			if(server != null){
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}  
	}
}
