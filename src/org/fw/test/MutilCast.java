package org.fw.test;

import java.net.*;

import java.awt.event.*;

import java.awt.*;

import java.io.*;

import javax.swing.*;

public class MutilCast {

	public static void main(String[] args)

	{

		MulticastSocket s = null;

		InetAddress group = null;

		JPanel northPanel = new JPanel();

		JPanel southPanel = new JPanel();

		JPanel namePanel = new JPanel(new FlowLayout());

		JLabel sendLabel = new JLabel("发送内容：");

		JLabel nameChangeLabel = new JLabel("给自己起个名字：");

		final JTextField nameSpace = new JTextField(20);

		final JTextArea messageArea = new JTextArea("", 20, 20);

		final JTextField sendField = new JTextField(30);

		JScrollPane message = new JScrollPane(messageArea);

		JButton sendButton = new JButton("发送");

		JButton saveButton = new JButton("保存记录");

		JButton clearUpButton = new JButton("清空面板");

		Box centerBox = Box.createVerticalBox();

		namePanel.add(nameChangeLabel);

		namePanel.add(nameSpace);

		centerBox.add(namePanel);

		centerBox.add(message);

		// 实现组播数据传送的配置信息

		try {

			group = InetAddress.getByName("228.9.6.18");

		} catch (UnknownHostException e1) {

			System.out.println("组播地址绑定失败");

		}

		try {

			s = new MulticastSocket(6789);

		} catch (IOException e1) {

			System.out.println("组播地址创建失败");

		}

		try {

			s.joinGroup(group);

		} catch (IOException e1) {

			System.out.println("组播地址加入失败");

		}

		// end实现组播数据传送的配置信息

		// “发送”按钮实现信息功能的发送部分

		class SendMsg implements ActionListener

		{

			String msg = null;

			MulticastSocket s = null;

			InetAddress group = null;

			public SendMsg(MulticastSocket s, InetAddress group)

			{

				this.s = s;

				this.group = group;

			}

			public void actionPerformed(ActionEvent e)

			{

				try

				{

					// 如果名字为空 给出提示信息

					if (nameSpace.getText().isEmpty())

						new JOptionPane().showMessageDialog(null, "请一定要取别名！");

					else

					{

						// 如果名字不为空 则把名字添加到数据报头

						msg = (nameSpace.getText() + "说：" + sendField.getText());

						DatagramPacket data =

						new DatagramPacket(msg.getBytes(),
								msg.getBytes().length, group, 6789);

						s.send(data);

					}
				}

				catch (IOException e1) {

					System.out.println("发送失败");

				}

			}

		}

		// 实现数据报的接受线程

		class recevMsg extends Thread

		{

			MulticastSocket s = null;

			public recevMsg(MulticastSocket s) {

				this.s = s;

			};

			public void run()

			{

				byte[] buf = new byte[100];

				DatagramPacket recv = new DatagramPacket(buf, buf.length);

				try

				{

					while (true)

					{

						s.receive(recv);

						String str = new String(recv.getData());

						String line = System.getProperty("line.separator");

						messageArea.append(str);

						messageArea.append(line);

					}

				}

				catch (IOException e1)

				{

					System.out.println("接受失败");

				}

			}

		}

		// 聊天记录的保存 保存在当前位置下的"Note.txt"文件中

		class SaveMsg implements ActionListener

		{

			String msg = null;

			String line = System.getProperty("line.separator");

			public void actionPerformed(ActionEvent e)

			{

				try

				{

					msg = messageArea.getText();

					FileOutputStream Note = new FileOutputStream("Note.txt");

					messageArea.append("记录已经保存在Note.txt");

					Note.write(msg.getBytes());

					messageArea.append(line);

					Note.close();

				}

				catch (IOException e1) {

					System.out.println("发送失败");

				}

			}

		}

		// 清除面板上的聊天记录

		class ClearMsg implements ActionListener

		{

			public void actionPerformed(ActionEvent e)

			{

				try

				{

					messageArea.setText("");

				}

				catch (Exception e1) {

					System.out.println("清空失败");

				}

			}

		}

		JFrame mutilCastFrame = new JFrame("组播聊天工具");

		northPanel.add(sendLabel);

		northPanel.add(sendField);

		northPanel.add(sendButton);

		southPanel.add(saveButton);

		southPanel.add(clearUpButton);

		mutilCastFrame.getContentPane().add(northPanel, "North");

		mutilCastFrame.getContentPane().add(southPanel, "South");

		mutilCastFrame.getContentPane().add(centerBox, "Center");

		mutilCastFrame.setDefaultCloseOperation(mutilCastFrame.EXIT_ON_CLOSE);

		sendButton.addActionListener(new SendMsg(s, group));

		saveButton.addActionListener(new SaveMsg());

		clearUpButton.addActionListener(new ClearMsg());

		mutilCastFrame.setSize(500, 500);

		mutilCastFrame.setLocation(200, 200);
		mutilCastFrame.show();

		recevMsg r = new recevMsg(s);

		r.start();

	}

}
