package org.fw.qq.plugins.sound;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
public class MyRecorder extends Frame implements ActionListener {
	Label wl = new Label("", Label.CENTER);
	Button b[] = {new Button("Â¼Òô"), new Button("ÖÕÖ¹"), new Button("²¥·Å"), new Button("Í£Ö¹")};
	byte da[] = new byte[160000];
	Record r;
	Play p;
	public MyRecorder() {
		setTitle("Â¼Òô»ú");
		wl.setFont(new Font("", Font.BOLD, 80));
		add(wl, BorderLayout.NORTH);
		Panel pa = new Panel();
		for (int i = 0; i < 4; i++) {
			b[i].setFont(new Font("", Font.BOLD, 17));
			b[i].addActionListener(this);
			pa.add(b[i]);
		}
		add(pa, BorderLayout.SOUTH);
		setBounds(100, 100, 450, 300);
		setVisible(true);
		validate();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Â¼Òô") {
			r = new Record();
			r.start();
		}
		if (e.getActionCommand() == "ÖÕÖ¹") {
			r.stopThread();
			r.getdata(da);
		}
		if (e.getActionCommand() == "²¥·Å") {
			p = new Play();
			p.setData(da);
			p.start();
		}
		if (e.getActionCommand() == "Í£Ö¹") {
			p.stopThread();
		}
	}
	public static void main(String[] args) {
		new MyRecorder();
	}
}
class Record extends Thread {
	private AudioFormat audioFormat = null;
	private TargetDataLine targetLine = null;
	private DataLine.Info Line_in = null;
	byte b[] = new byte[160000];
	public Record() {
		audioFormat = new AudioFormat(8000, 16, 2, true, true);
		Line_in = new DataLine.Info(TargetDataLine.class, audioFormat);
	}
	public void run() {
	
		try {
			targetLine = (TargetDataLine) AudioSystem.getLine(Line_in);
			targetLine.open(audioFormat, targetLine.getBufferSize());
			targetLine.start();
			targetLine.read(b, 0, b.length);
			System.out.println("¿ªÊ¼Â¼Òô");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void getdata(byte a[]) {
		for (int i = 0; i < 160000; i++)
			a[i] = b[i];
	}
	void stopThread() {
		targetLine.stop();
		targetLine.close();
	}
}
class Play extends Thread {
	private AudioFormat audioFormat = null;
	private SourceDataLine sourceLine = null;
	private DataLine.Info Line_out = null;
	byte b[] = new byte[160000];
	public Play() {
		audioFormat = new AudioFormat(8000, 16, 2, true, true);
		Line_out = new DataLine.Info(SourceDataLine.class, audioFormat);
	}
	void setData(byte a[]) {
		for (int i = 0; i < 160000; i++)
			b[i] = a[i];
	}
	public void run() {
		try {
			System.out.println("¿ªÊ¼²¥·Å");
			sourceLine = (SourceDataLine) AudioSystem.getLine(Line_out);
			sourceLine.open(audioFormat, sourceLine.getBufferSize());
			sourceLine.start();
			sourceLine.write(b, 0, b.length);
		} catch (Exception e) {
		}
	}
	void stopThread() {
		sourceLine.stop();
		sourceLine.close();
	}
}