package org.fw.test;

import javax.swing.*;

import org.fw.SnapTipTabbedPane;

public class TestJFrame {

	public static void main(String args[]){
		JFrame f = new JFrame();
		JPanel j1 = new JPanel();
		j1.add(new JButton("ER"));
		j1.add(new JButton("ER1"));
		j1.add(new JButton("ER2"));
		JPanel j2 = new JPanel();
		j2.add(new JButton("ER"));
		j2.add(new JButton("ER"));
		j2.add(new JButton("ER"));
		SnapTipTabbedPane s =new SnapTipTabbedPane();
		s.insertTab("≤‚ ‘1",new ImageIcon("skin/default/qq.png"),j1,"ss1",0);
		s.insertTab("≤‚ ‘2",new ImageIcon("skin/default/qq.png"),j2,"ss2",1);
		f.getContentPane().add(s);
		f.setDefaultCloseOperation(3);
		f.setVisible(true);
	}
}
