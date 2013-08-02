package org.fw.test;


import javax.swing.*;

import org.fw.HyperLinkFLabel;

public class TestHyperLinkFLabel {

	public static void main(String[] args){
		JFrame frame = new JFrame("≤‚ ‘HyperLinkFLabel");
		JPanel panel = new JPanel();
		HyperLinkFLabel label = new HyperLinkFLabel("∞Ÿ∂»","http://www.baidu.com");
		label.addMouseListener(label);
		panel.add(label);
		frame.add(panel);
		frame.setSize(400,200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}
}
