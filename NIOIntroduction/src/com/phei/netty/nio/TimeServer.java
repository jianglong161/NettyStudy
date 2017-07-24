package com.phei.netty.nio;
/**
 * NIO时间服务器TimeServer
 * @author Still2Almost
 *
 */
public class TimeServer {
	public static void main(String[] args) {
		int port = 8080;
		if(args != null && args.length > 0){
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer,"NIO-MultiplexerTimerServer-001").start();
	}
}
