package com.phei.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO的时间服务器 
 * @author Still2Almost
 *
 */
public class MultiplexerTimeServer implements Runnable{
	private Selector selector;
	private ServerSocketChannel servChannel;
	private volatile boolean stop;
	/**
	 * 初始化多路复用，监听端口
	 */
	public MultiplexerTimeServer(int port) {
		try {
			selector = Selector.open();
			servChannel = ServerSocketChannel.open();
			servChannel.configureBlocking(false);
			servChannel.socket().bind(new InetSocketAddress(port),1024);
			servChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("The time server is start in port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void stop(){
		this.stop = true;
	}
	@Override
	public void run() {
		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if(key != null){
							key.cancel();
							if(key.channel() != null)
								key.channel().close();
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if(selector != null){
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			//处理接入的新消息
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					String  body = new String(bytes,"utf-8");
					System.out.println("The timeserver receiver order : " +
							body);
				 	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
				 	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? 
						sf.format(new Date()) : "BAD ORDER";
					doWrite(sc,currentTime);
				}else if(readBytes < 0){
					//对链路关闭
					key.cancel();
					sc.close();
				}else{
					;
				}
			}
		}
	}
	private void doWrite(SocketChannel sc, String response) throws IOException{
		if(response != null && response.trim().length() > 0){
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
}
