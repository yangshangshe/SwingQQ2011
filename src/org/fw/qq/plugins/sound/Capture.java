package org.fw.qq.plugins.sound;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Capture implements Runnable {

	TargetDataLine line;
	Thread thread;
	Socket s;
	BufferedOutputStream captrueOutputStream;

	public Capture(Socket s) {// 构造器 取得socket以获得网络输出流
		this.s = s;
	}

	public void start() {
		thread = new Thread(this);
		thread.setName("Capture");
		thread.start();
	}

	public void stop() {
		thread = null;
		try {
			captrueOutputStream.close();
			line.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			captrueOutputStream = new BufferedOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// AudioFormat(float sampleRate, int sampleSizeInBits, int channels,
		// boolean signed, boolean bigEndian）
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format, line.getBufferSize());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		byte[] data = new byte[1024];
		int numBytesRead = 0;
		line.start();

		while (thread != null) {
			numBytesRead = line.read(data, 0, data.length);

			try {
				captrueOutputStream.write(data, 0, numBytesRead);// 写入网络流
			} catch (Exception ex) {
				ex.printStackTrace();
				break;
			}

			try {
				captrueOutputStream.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
