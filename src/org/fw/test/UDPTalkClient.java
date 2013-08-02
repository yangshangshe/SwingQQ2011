package org.fw.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPTalkClient extends Thread{

	DatagramSocket clientSocket;
	DatagramPacket clientPacket;
	InetAddress remoteHost;
	int remotePort;
	byte[] dataBuf;
	String datagram;
	int localport;
	String serverIp;
	int serverPort;
	public UDPTalkClient(int localport,String serverIp,int serverPort){
		this.localport = localport;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		try {
			clientSocket = new DatagramSocket(localport);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
				
		while(true){
			try{
				byte[] dataBuf = new byte[512];
				
				remoteHost = InetAddress.getByName(serverIp);
				remotePort = serverPort;
				datagram = new String("这是测试文字");
				dataBuf = datagram.getBytes();
				clientPacket = new DatagramPacket(dataBuf,dataBuf.length,remoteHost,remotePort);
				clientSocket.send(clientPacket);
				dataBuf = new byte[512];
				clientPacket = new DatagramPacket(dataBuf,512);
				clientSocket.receive(clientPacket);
				datagram = new String(clientPacket.getData());
				System.out.println("从服务端传来的文字"+datagram);
			}catch(Exception e){
				
			}
		}
	}
	
	public static void main(String[] args){
		UDPTalkClient t = new UDPTalkClient(5000,"127.0.0.1",6000);
		t.start();
	}
}
