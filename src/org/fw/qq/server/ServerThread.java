package org.fw.qq.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTextArea;

import org.fw.db.pojo.Friend;
import org.fw.db.pojo.User;
import org.fw.db.pojo.UserDAO;

public class ServerThread extends Thread {

	private static Map<String, Integer> clientListPortMap = new HashMap<String, Integer>();
	private static Map<String, String> clientListIpMap = new HashMap<String, String>();

	Socket socket = null;
	int seqnum = 0;
	JTextArea infoArea;

	public ServerThread(Socket socket, int seqnum) {
		this.socket = socket;
		this.seqnum = seqnum;
		System.out.println("当前服务线程号" + seqnum);
	}

	public ServerThread(Socket socket2, int serverNum, JTextArea infoArea) {
		this.socket = socket2;
		this.seqnum = serverNum;
		System.out.println("当前服务线程号" + seqnum);
		this.infoArea = infoArea;
	}

	public void run() {
		try {
			String msg;
			boolean flag = true;
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			while (flag) {
				msg = in.readUTF();

				if (msg.equals("bye")) {
					flag = false;
					System.out.println("和服务器交互结束!");
					os.writeUTF("bye");
					os.flush();
					in.close();
					os.close();
					if (socket.isConnected()) {
						socket.close();
						socket = null;
					}
					break;
				}

				System.out.println("客户端请求信息：" + msg);

				// 登录
				if (msg.startsWith("[login]#login#")) {
					dealLogin(msg, os);
				} else if (msg.startsWith("[talk]#getip#")) {
					dealTalkUserInfo(msg, os);
				} else if (msg.startsWith("[voicetalk]#request#")) {
					sendVoiceTalkRequestToDestination(msg, os);
				} else if (msg.startsWith("[voicetalk]#requestaccpet#")) {
					responseVoiceTalkRequest(msg, os);
				} else if(msg.startsWith("[videotalk]#request#")){
					sendVideoTalkRequestToDestination(msg,os);
				}
			}

		} catch (IOException e) {
			if (socket.isConnected()) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
			e.printStackTrace();
		}
	}

	private void sendVideoTalkRequestToDestination(String msg, DataOutputStream os) {
		String talkQQNumber = msg.split("#")[2];
		String talkDestinationIp = msg.split("#")[3];
		String requestQQNumber = msg.split("#")[4];
		String requstIp = msg.split("#")[5];
		String requestPort = msg.split("#")[6];
		String talkQQInfo = msg.split("#")[7];
		int talkDestinationListenerPort = clientListPortMap.get(talkQQNumber);
		System.out.println("开始转发视频请求 : " + msg);

		try {
			DatagramSocket transmitSocket = new DatagramSocket();
			DatagramPacket transmitPacket;
			String transmitCommand = "[videotalk]#server-transmit#" + requstIp + "#" + requestPort + "#" + talkQQInfo;
			byte[] dataBuf = new byte[transmitCommand.length()];
			dataBuf = transmitCommand.getBytes();
			transmitPacket = new DatagramPacket(dataBuf, dataBuf.length, InetAddress.getByName(talkDestinationIp), talkDestinationListenerPort);
			transmitSocket.send(transmitPacket);
			transmitSocket.close();
			os.writeUTF("[videotalk]#server-transmit#accept");
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void responseVoiceTalkRequest(String msg, DataOutputStream os) {
		String talkQQNumber = msg.split("#")[4];
		String requestQQNumber = msg.split("#")[2];
		try {
			os.writeUTF("[voicetalk]#requestaccpet#confirm");
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendVoiceTalkRequestToDestination(String msg, DataOutputStream os) {
		String talkQQNumber = msg.split("#")[2];
		String talkDestinationIp = msg.split("#")[3];
		String requestQQNumber = msg.split("#")[4];
		String requstIp = msg.split("#")[5];
		String requestPort = msg.split("#")[6];
		String talkQQInfo = msg.split("#")[7];
		int talkDestinationListenerPort = clientListPortMap.get(talkQQNumber);
		System.out.println("开始转发语音请求 : " + msg);

		try {
			DatagramSocket transmitSocket = new DatagramSocket();
			DatagramPacket transmitPacket;
			String transmitCommand = "[voicetalk]#server-transmit#" + requstIp + "#" + requestPort + "#" + talkQQInfo;
			byte[] dataBuf = new byte[transmitCommand.length()];
			dataBuf = transmitCommand.getBytes();
			transmitPacket = new DatagramPacket(dataBuf, dataBuf.length, InetAddress.getByName(talkDestinationIp), talkDestinationListenerPort);
			transmitSocket.send(transmitPacket);
			transmitSocket.close();
			os.writeUTF("[voicetalk]#server-transmit#accept");
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void dealTalkUserInfo(String msg, DataOutputStream os) throws IOException {
		// 获取要聊天的对象的ip
		System.out.println("获取要聊天的对象port : " + msg);
		String qqNumber = msg.split("#")[2];
		os.writeUTF("[talk]#ipport#" + clientListIpMap.get(qqNumber) + "#" + clientListPortMap.get(qqNumber));
		os.flush();
	}

	private void dealLogin(String msg, DataOutputStream os) throws IOException {
		String number = msg.split("#")[2];
		String password = msg.split("#")[3];
		String ip = msg.split("#")[4];
		int listenerPort = Integer.parseInt(msg.split("#")[5]);

		User user = UserDAO.login(number, password);
		if (user != null) {

			System.out.println(user.getUserInfo().getNickName() + " 登录成功!");

			StringBuilder sb = new StringBuilder();
			sb.append(user.getUserInfo().getNickName());
			sb.append("," + user.getUserInfo().getRemarkName());
			sb.append("," + user.getUserInfo().getSignature());
			sb.append("," + user.getUserInfo().getStatue());
			sb.append("," + ip);
			sb.append("," + user.getUserInfo().getGroupName());
			sb.append("," + user.getUserInfo().getHeadIcon());
			sb.append("," + user.getNumber());
			sb.append("," + listenerPort);
			Set<Friend> friends = user.getFriends();
			Iterator<Friend> iterators = friends.iterator();
			String firendids = "";
			while (iterators.hasNext()) {
				Friend friend = iterators.next();
				firendids += "'" + friend.getFriendid() + "',";
			}
			firendids = firendids.substring(0, firendids.length() - 1);
			List<User> users = UserDAO.getFriends(firendids);
			for (User u : users) {
				sb.append("#" + u.getUserInfo().getNickName());
				sb.append("," + u.getUserInfo().getRemarkName());
				sb.append("," + u.getUserInfo().getSignature());
				sb.append("," + u.getUserInfo().getStatue());
				sb.append("," + u.getUserInfo().getIp());
				sb.append("," + u.getUserInfo().getGroupName());
				sb.append("," + u.getUserInfo().getHeadIcon());
				sb.append("," + u.getNumber());
				sb.append("," + "0");
			}
			os.writeUTF("[login]#legal#" + sb.toString());
			os.flush();

			clientListIpMap.put(number, ip);
			for (String key : clientListIpMap.keySet()) {
				System.out.println("当前客户端ip ： " + number + " " + clientListIpMap.get(key));
			}
			clientListPortMap.put(number, listenerPort);
			for (String key : clientListPortMap.keySet()) {
				System.out.println("当前客户端端口 : " + number + " " + clientListPortMap.get(key));
			}
		} else {
			System.out.println("登录失败");
			os.writeUTF("[login]#fail#");
			os.flush();
		}
	}
}
