package org.fw.qq.plugins.sound;

import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Frame {
	TextField tf = new TextField(20);
	TextArea ta = new TextArea();
	Button send = new Button("send");
	Button voiceChat = new Button("voiceChat");
	ServerSocket serSocket;
	Socket client;
	InputStream in;
	BufferedReader br;
	OutputStream out;
	BufferedWriter bw;

	public Server() {
		super("Server");
		add("North", tf);
		add("Center", ta);
		add("South", send);
		add("East", voiceChat);
		setBackground(Color.yellow);
		setSize(250, 250);
		show();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		try {
			ServerSocket serSocket = new ServerSocket(5000);
			client = serSocket.accept();
			ta.append("client host" + client.getInetAddress().getHostName() + "\n\n");
			in = client.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			out = client.getOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(out));
		} catch (Exception e) {
		}
		while (true) {
			try {
				byte[] buf = new byte[200];
				in.read(buf);
				String str = new String(buf);
				ta.append("Client say:" + str);
				ta.append("\n");
			} catch (IOException e) {
			}
		}
	}
	public boolean action(Event evt, Object arg) {
		if (evt.target.equals(send)) {
			try {
				String str = tf.getText();
				byte[] buf = str.getBytes();
				tf.setText(null);
				out.write(buf);
				ta.append("I say:" + str);
				ta.append("\n");
			} catch (IOException ioe) {
			}
		} else if (evt.target.equals(voiceChat)) {
			try {
				ServerSocket serSock = new ServerSocket(20013);
				Socket cli = serSock.accept();
				Playback player = new Playback(cli);
				player.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;

	}
	public static void main(String[] args) {
		Server server = new Server();
	}

}
