package org.fw.qq;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fw.FramePanel;
import org.fw.OpaqueButton;
import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;
import org.fw.utils.SocketUtil;

public class QQLoginingFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = -4368207493691846040L;

	FramePanel panel;// 窗体面板
	ImageHelper imageHelper;
	Config basicCFG;// 全局配置
	JPanel center;// 内容面板
	JLabel loginImgLabel;// 登陆图片
	JLabel numberLabel;// QQ号码
	JLabel infoLabel;// 信息
	OpaqueButton cancelButton;// 取消按钮
	String qqnumber;// qq号码
	String password;// 密码
	QQLoginFrame loginFrame;
	Image bgImage;

	DataInputStream din;
	DataOutputStream dout;
	Socket socket;
	int listenerPort = 6000;

	static QQLoginingFrame instance;

	public static QQLoginingFrame getInstance(String qqnumber, String password, QQLoginFrame loginFrame) {
		if (instance == null) {
			return new QQLoginingFrame(qqnumber, password, loginFrame);
		} else {
			return instance;
		}
	}

	private QQLoginingFrame(String qqnumber, String password, QQLoginFrame loginFrame) {
		instance = this;
		this.qqnumber = qqnumber;
		this.password = password;
		this.loginFrame = loginFrame;
		Thread t = new Thread(this);
		t.start();
		initParamter();

	}

	private void initParamter() {
		basicCFG = new Config(ProjectPath.getProjectPath() + "cfg/basic.properties");
		imageHelper = new ImageHelper();
		this.setUndecorated(true);
		panel = new FramePanel();
		bgImage = imageHelper.getFWImage(basicCFG.getProperty("LoginingFrameBackgroundImage", "skin/default/background/bg.jpg"));
		panel.setBackground(bgImage);
		panel.setParent(this);
		panel.setTitle("  " + basicCFG.getProperty("AppTitle", "QQ2011"));
		center = new JPanel();
		center.setOpaque(false);
		center.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		loginImgLabel = new JLabel(imageHelper.getFWImageIcon(basicCFG.getProperty("LoginingImage", "skin/default/loging.png")));
		center.add(loginImgLabel, gbc);
		numberLabel = new JLabel();
		numberLabel.setOpaque(false);
		numberLabel.setText(qqnumber);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		center.add(numberLabel, gbc);
		infoLabel = new JLabel();
		infoLabel.setOpaque(false);
		infoLabel.setText("正在登陆");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		center.add(infoLabel, gbc);
		cancelButton = new OpaqueButton("取消");
		cancelButton.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				instance.dispose();
				loginFrame.setVisible(true);
			}

			public void mouseEntered(MouseEvent e) {
				cancelButton.setOpaque(true);
			}

			public void mouseExited(MouseEvent e) {
				cancelButton.setOpaque(false);
			}

		});
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		center.add(cancelButton, gbc);
		panel.add(center);
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		this.setSize(260, 650);
		this.setVisible(true);
		this.setTitle("正在登录");
	}

	private void initListenerPort() {
		listenerPort = SocketUtil.getAvailablePort();
	}

	public void run() {
		socket = new Socket();
		try {

			initListenerPort();

			socket.connect(new InetSocketAddress("127.0.0.1", 11111));
			dout = new DataOutputStream(socket.getOutputStream());
			dout.writeUTF("[login]#login#" + qqnumber + "#" + password + "#" + getIp() + "#" + listenerPort);
			dout.flush();

			din = new DataInputStream(socket.getInputStream());
			boolean flag = true;
			while (flag) {
				String msg = din.readUTF();

				if (msg.equals("bye")) {
					flag = false;
				}
				if (msg.length() > 0) {
					if (msg.startsWith("[login]#legal#")) {

						msg = msg.substring(14, msg.length());

						System.out.println("登录成功，服务器返回信息:" + msg);

						QQMainFrame qq = new QQMainFrame(msg);
						qq.setVisible(true);

						double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() - qq.getWidth();
						double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - qq.getHeight();
						qq.setLocation((int) width / 2, (int) height / 2);
						// 断开连接
						dout.writeUTF("bye");
						dout.flush();

						loginFrame.dispose();
						instance.dispose();

					} else if (msg.startsWith("[login]#fail#")) {
						JOptionPane.showMessageDialog(null, "用户名或密码错误", "登录失败", 1);
					}
				}
			}
			din.close();
			dout.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private String getIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "127.0.0.1";
	}
}
