package org.fw.qq.plugins.remoteassistance;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Client extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -806686211556049511L;
	private String controlIp;
	private int controlPort;
	private BufferedImage controlDesktop;

	public Client(String controlIp, int controlPort) {
		this.controlIp = controlIp;
		this.controlPort = controlPort;

		setFocusable(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);

	}

	public void start() {
		ControlCarrier command = new ControlCarrier();
		sendControlCommand(command);
	}

	public void paintComponent(Graphics g) {
		if (controlDesktop != null) {
			g.drawImage(controlDesktop, 0, 0, this);
		}
	}

	private void sendControlCommand(ControlCarrier command) {
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectInput = null;
		try {
			Socket commandSocket = new Socket(InetAddress.getByName(controlIp), controlPort);
			objectOut = new ObjectOutputStream(new BufferedOutputStream(commandSocket.getOutputStream()));
			objectOut.writeObject(command);
			objectOut.flush();

			objectInput = new ObjectInputStream(new BufferedInputStream(commandSocket.getInputStream()));
			ControlCarrier desktopState = (ControlCarrier) objectInput.readObject();
			displayDesktopState(desktopState);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objectOut != null) {
				try {
					objectOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (objectInput != null) {
				try {
					objectInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void displayDesktopState(ControlCarrier desktopState) {
		ByteArrayInputStream bin = new ByteArrayInputStream(desktopState.getDesktopImg());
		try {
			controlDesktop = ImageIO.read(bin);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "被控制端已断开连接，或网络异常!");
		}
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mouseClicked");
		if (e.getClickCount() == 1) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				command.setMousePressBtn(InputEvent.BUTTON1_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON1_MASK);
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				command.setMousePressBtn(InputEvent.BUTTON2_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON2_MASK);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				command.setMousePressBtn(InputEvent.BUTTON3_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON3_MASK);
			}
		} else if (e.getClickCount() == 2) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				command.setMousePressBtn(InputEvent.BUTTON1_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON1_MASK);
				command.setMousePressBtn(InputEvent.BUTTON1_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON1_MASK);
			} else if (e.getButton() == MouseEvent.BUTTON2) {
				command.setMousePressBtn(InputEvent.BUTTON2_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON2_MASK);
				command.setMousePressBtn(InputEvent.BUTTON2_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON2_MASK);
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				command.setMousePressBtn(InputEvent.BUTTON3_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON3_MASK);
				command.setMousePressBtn(InputEvent.BUTTON3_MASK);
				command.setMouseReleaseBtn(InputEvent.BUTTON3_MASK);
			}
		}
		sendControlCommand(command);
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mousePressed");
		if (e.getButton() == MouseEvent.BUTTON1) {
			command.setMousePressBtn(InputEvent.BUTTON1_MASK);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			command.setMousePressBtn(InputEvent.BUTTON2_MASK);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			command.setMousePressBtn(InputEvent.BUTTON3_MASK);
		}
		sendControlCommand(command);
	}

	public void mouseReleased(MouseEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mouseReleased");
		if (e.getButton() == MouseEvent.BUTTON1) {
			command.setMouseReleaseBtn(InputEvent.BUTTON1_MASK);
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			command.setMouseReleaseBtn(InputEvent.BUTTON2_MASK);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			command.setMouseReleaseBtn(InputEvent.BUTTON3_MASK);
		}
		sendControlCommand(command);
	}

	public void mouseDragged(MouseEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mouseDragged");
		command.setMousePressBtn(e.getButton());
		command.setMouseX(e.getX());
		command.setMouseY(e.getY());
		sendControlCommand(command);
	}

	public void mouseMoved(MouseEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mouseMoved");
		command.setMouseX(e.getX());
		command.setMouseY(e.getY());
		sendControlCommand(command);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("mouseWheelMoved");
		command.setWheelAmt(e.getWheelRotation());
		sendControlCommand(command);
	}

	public void keyPressed(KeyEvent e) {

		ControlCarrier command = new ControlCarrier();
		command.setType("keyPressed");
		List<Integer> keyPress = new ArrayList<Integer>();
		if (e.isControlDown()) {
			keyPress.add(KeyEvent.VK_CONTROL);
		}
		if (e.isAltDown()) {
			keyPress.add(KeyEvent.VK_ALT);
		}
		if (e.isShiftDown()) {
			keyPress.add(KeyEvent.VK_SHIFT);
		}
		keyPress.add(e.getKeyCode());
		command.setKeyPressCode(keyPress);
		sendControlCommand(command);
	}

	public void keyReleased(KeyEvent e) {
		ControlCarrier command = new ControlCarrier();
		command.setType("keyReleased");
		List<Integer> keyRelease = new ArrayList<Integer>();
		keyRelease.add(e.getKeyCode());
		command.setKeyReleaseCode(keyRelease);
		sendControlCommand(command);
	}

	public void keyTyped(KeyEvent e) {

	}

	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("远程控制");
		String ip = JOptionPane.showInputDialog("请输入远程连接IP");
		if ("null".equals(ip)) {
			System.exit(0);
		}
		while (ip.isEmpty()) {
			ip = JOptionPane.showInputDialog("请输入远程连接IP");
		}
		while (!StringUtil.isValidIP(ip)) {
			ip = JOptionPane.showInputDialog("请输入合法IP");
		}

		String port = JOptionPane.showInputDialog("输入远程连接端口");
		if ("null".equals(port)) {
			System.exit(0);
		}
		while (port.isEmpty()) {
			port = JOptionPane.showInputDialog("请输入远程连接端口");
		}
		while (!StringUtil.isValidPort(port)) {
			port = JOptionPane.showInputDialog("请输入合法端口");
		}
		JOptionPane.showMessageDialog(null, "开始远程连接!");

		Client client = new Client(ip, Integer.parseInt(port));
		JScrollPane jsp = new JScrollPane(client, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(jsp);
		frame.setDefaultCloseOperation(3);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

}
