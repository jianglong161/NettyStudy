package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 同步阻塞IO的TimeClient
 * @author Still2Almost
 *
 */
public class TimeClient {
	public static void main(String[] args) {
		int port = 8080;
		if(args != null && args.length > 0){
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				//采用默认处理
			}
		}
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try{
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			Scanner scanner = new Scanner(System.in);
			String name = scanner.nextLine();
			out.println(name);
			//out.println("QUERY TIME ORDER");
			System.out.println("Send order 2 server succedd");
			String resp = in.readLine();
			System.out.println("Now is" + resp);
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(out != null){
				out.close();
				out = null;
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
