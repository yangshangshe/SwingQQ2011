package org.fw.qq.plugins.sound;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Playback implements Runnable {

	final int bufSize = 16384;
	SourceDataLine line;
	Thread thread;
	Socket s;

	public Playback(Socket s) {// 构造器 取得socket以获得网络输入流
		this.s = s;
	}
	public void start() {

		thread = new Thread(this);
		thread.setName("Playback");
		thread.start();
	}

	public void stop() {
		thread = null;
	}

	public void run() {

		// AudioFormat(float sampleRate, int sampleSizeInBits, int channels,
		// boolean signed, boolean bigEndian）
		AudioFormat format = new AudioFormat(8000, 16, 2, true, true);
		BufferedInputStream playbackInputStream;

		try {
			playbackInputStream = new BufferedInputStream(new AudioInputStream(s.getInputStream(), format, 2147483647));// 封装成音频输出流，如果网络流是经过压缩的需在此加套解压流
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufSize);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}

		byte[] data = new byte[1024];// 此处数组的大小跟实时性关系不大，可根据情况进行调整
		int numBytesRead = 0;
		line.start();

		while (thread != null) {
			try {
				numBytesRead = playbackInputStream.read(data);
				line.write(data, 0, numBytesRead);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

		if (thread != null) {
			line.drain();
		}

		line.stop();
		line.close();
		line = null;

	}

}
