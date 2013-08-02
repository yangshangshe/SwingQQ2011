package org.fw.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class SocketUtil {

	static Socket socket;
	static DataInputStream din;
	static DataOutputStream dout;

	/*
	 * 检测端口是否被用
	 */
	private static boolean isPortUsed(int port) {
		try {
			DatagramSocket socket = new DatagramSocket(port);
			socket.close();
			return true;
		} catch (SocketException e) {
			return false;
		}
	}
	public static int getAvailablePort() {
		for (int i = 20001; i < 65336; i++) {
			if (isPortUsed(i)) {
				System.out.println("客户端可用端口:" + i);
				return i;
			}
		}
		return 65335;
	}

	public static String connectWithServer(String command) {
		socket = new Socket();
		try {

			socket.connect(new InetSocketAddress("127.0.0.1", 11111));
			dout = new DataOutputStream(socket.getOutputStream());
			dout.writeUTF(command);
			dout.flush();

			din = new DataInputStream(socket.getInputStream());
			boolean flag = true;
			while (flag) {
				String msg = din.readUTF();
				// 断开连接
				dout.writeUTF("bye");
				dout.flush();
				return msg;
			}
			din.close();
			dout.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return command;
	}

}
