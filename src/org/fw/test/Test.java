package org.fw.test;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.fw.OpaquePanel;

public class Test extends JFrame{
	private static final long serialVersionUID = 1085903870845943792L;

	public Test(){
		JLabel a = new JLabel("1111");
		a.setIcon(new ImageIcon("skin/default/head/head_1.png"));
		this.getContentPane().add(a);
		this.setVisible(true);
	}
	
	public static void main(String[] args){
//		Test t = new Test();
		
		System.out.println( 1 << 4);
	}
}
