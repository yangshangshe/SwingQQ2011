package org.fw.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTalkServer extends Thread{

	DatagramSocket serverSocket = null;
	DatagramPacket serverPacket;
	InetAddress remoteHost;
	int remotePort;
	String datagram,s;
	public UDPTalkServer(int port){
		try{
			serverSocket = new DatagramSocket(port);
			System.out.println("在端口"+port+"上监听");
		}catch(Exception e){
			
		}
	}
	
	public void run(){
		if(serverSocket == null){
			return;
		}
		
		while(true){
			try{
				byte[] dataBuf = new byte[512];
				
				serverPacket = new DatagramPacket(dataBuf,512);
				serverSocket.receive(serverPacket);
				remoteHost = serverPacket.getAddress();
				remotePort = serverPacket.getPort();
				datagram = new String(serverPacket.getData());
				System.out.println("收到"+remoteHost+"消息"+datagram);
				datagram = new String("返回信息");
				dataBuf = datagram.getBytes();
				serverPacket = new DatagramPacket(dataBuf,dataBuf.length,remoteHost,remotePort);
				serverSocket.send(serverPacket);
			}catch(Exception e){
				
			}
		}
	}
	
	public static void main(String[] args){
		UDPTalkServer s = new UDPTalkServer(6000);
		s.start();
	}
}
