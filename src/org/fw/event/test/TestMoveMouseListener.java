package org.fw.event.test;

import javax.swing.JFrame;

import org.fw.FramePanel;
import org.fw.event.MoveMouseListener;

public class TestMoveMouseListener {

	public static void main(String[] args){
		JFrame frame = new JFrame();
		FramePanel panel = new FramePanel("image/bgImage.jpg");
		
		MoveMouseListener mml = new MoveMouseListener(panel);
		panel.addMouseListener(mml);
		panel.addMouseMotionListener(mml);
		frame.setUndecorated(true);
		frame.add(panel);
		frame.setVisible(true);
		frame.setSize(468,446);
	}
}
