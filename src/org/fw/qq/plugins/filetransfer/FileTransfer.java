package org.fw.qq.plugins.filetransfer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class FileTransfer extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5946884272739604176L;
	private String receiverIp;
	private int receiverPort;

	private String transferFilePath;
	private Thread transferThread;
	private final int fps = 5;
	private int pos;

	private JLabel speedLabel;
	private JLabel fileLabel;
	private JProgressBar progress;

	public static void main(String[] args) {
		FileTransfer transfer = new FileTransfer("127.0.0.1", 60000, "F:\\game\\大富翁8.zip");
		transfer.start();
		JFrame frame = new JFrame("发送窗口");
		frame.add(transfer);
		frame.setDefaultCloseOperation(3);
		frame.setSize(540, 75);
		frame.setVisible(true);
	}

	public FileTransfer(String receiverIp, int receiverPort, String transferFilePath) {
		this.receiverIp = receiverIp;
		this.receiverPort = receiverPort;
		this.transferFilePath = transferFilePath;

		transferThread = new Thread(this);
		pos = 0;

		progress = new JProgressBar(JProgressBar.HORIZONTAL);
		progress.setMaximum(100);
		progress.setMinimum(0);
		progress.setStringPainted(true);
		speedLabel = new JLabel("速度：0KB/S");
		fileLabel = new JLabel("");
		this.add(fileLabel);
		this.add(progress);
		this.add(speedLabel);
	}

	public void start() {
		transferThread.start();
	}

	public void run() {

		File transferFile = new File(transferFilePath);
		long startTransferTime = System.currentTimeMillis();
		boolean speedStart = false;
		try {
			// 缓存大小对速度有一定的影响
			byte[] buffer = new byte[10240];
			RandomAccessFile ras = new RandomAccessFile(transferFile, "r");
			long startTime = 0;
			long startPos = 0;
			while (true) {

				if (!speedStart) {
					startTime = System.currentTimeMillis();
					startPos = pos;
					speedStart = true;
				}
				ras.seek(pos);
				ras.read(buffer);
				FileCarrier carrier = new FileCarrier();
				carrier.setFileName(transferFile.getName());
				carrier.setCurBlockLength(buffer.length);
				carrier.setPos(pos);
				carrier.setTotalLength(ras.length());
				carrier.setContent(buffer);
				pos = pos + buffer.length;
				Socket socket = new Socket(receiverIp, receiverPort);
				ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				objectOut.writeObject(carrier);
				objectOut.flush();
				objectOut.close();

				fileLabel.setText("发送文件:" + transferFile.getName());

				if (System.currentTimeMillis() - startTime >= 1E3) {
					speedStart = false;
					progress.setValue((int) ((double) (pos + buffer.length) / (ras.length()) * 100));
					speedLabel.setText("速度：" + (pos - startPos) / 1024 + "KB/S");
				}

				Thread.sleep(fps);

				if (pos >= ras.length()) {
					progress.setValue(100);
					System.out.println("发送完毕");
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis() - startTransferTime);
		System.out.println("耗时: " + c.get(Calendar.MINUTE) + "分 " + c.get(Calendar.SECOND) + "秒 " + c.get(Calendar.MILLISECOND) + " 微秒");
	}

}
