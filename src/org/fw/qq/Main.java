package org.fw.qq;

import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {

	public static void main(String args[]){
		//Windows风格
		//String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		//Windows Classic风格
		//String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
		//系统当前风格
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	

		QQLoginFrame qq = QQLoginFrame.getInstance();
		qq.setVisible(true);
		qq.setSize(339,225);
		qq.setDefaultCloseOperation(3);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() - qq.getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() - qq.getHeight();
		qq.setLocation((int)width/2,(int)height/2);
	}
}
