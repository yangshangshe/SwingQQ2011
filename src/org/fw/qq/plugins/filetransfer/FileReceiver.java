package org.fw.qq.plugins.filetransfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class FileReceiver extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8385869427220376418L;
	private String listenerIp;
	private int listenerPort;
	private String saveFilePath;
	private ServerSocket server;
	private Thread thread;
	
	private JProgressBar progress;
	private JLabel speedLabel;
	private JLabel fileLabel;

	public static void main(String[] args) {
		FileReceiver receiver = new FileReceiver("127.0.0.1", 60000, "G:\\test.rar");
		receiver.start();
		JFrame frame = new JFrame("接收窗口");
		frame.add(receiver);
		frame.setSize(540, 75);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}

	public FileReceiver(String listenerIp, int listenerPort, String saveFilePath) {
		this.listenerIp = listenerIp;
		this.listenerPort = listenerPort;
		this.saveFilePath = saveFilePath;

		thread = new Thread(this);

		try {
			server = new ServerSocket(this.listenerPort, 1, InetAddress.getByName(this.listenerIp));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		progress = new JProgressBar(JProgressBar.HORIZONTAL);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		progress.setMinimum(0);
		speedLabel = new JLabel("速度:0KB/S");
		fileLabel = new JLabel();
		this.add(fileLabel);
		this.add(progress);
		this.add(speedLabel);
		
	}

	public void start() {
		thread.start();
	}

	public void run() {
		
		boolean speedStart =false;
		long startTime = 0;
		long startPos = 0;
		while (true) {

			try {
				
				Socket client = server.accept();
				ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
				FileCarrier carrier = (FileCarrier) objIn.readObject();
				File file = new File(saveFilePath);
				if (!file.exists())
					file.createNewFile();
				RandomAccessFile ras = new RandomAccessFile(file, "rw");
				ras.seek(carrier.getPos());
				ras.write(carrier.getContent());
				ras.close();
				objIn.close();
				
				fileLabel.setText("接收文件:"+carrier.getFileName());
				
				if(!speedStart){
					startTime = System.currentTimeMillis();
					startPos = carrier.getPos();
					speedStart = true;
				}
				
				if(System.currentTimeMillis() - startTime >= 1E3){
					speedStart = false;
					progress.setValue((int)((double)( carrier.getPos()+ carrier.getCurBlockLength())/(carrier.getTotalLength())*100));
					speedLabel.setText("速度："+(carrier.getPos() - startPos)/1024 + "KB/S");
				}
				progress.setValue((int)((double)(carrier.getPos()+carrier.getCurBlockLength())/carrier.getTotalLength()*100));

				if ((carrier.getPos() + carrier.getCurBlockLength()) >= carrier.getTotalLength()) {
					System.out.println("接收完毕");
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

}
