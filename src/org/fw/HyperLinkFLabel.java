package org.fw;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

/**
 *  超链接标签
 * @author Administrator
 *
 */
public class HyperLinkFLabel extends JLabel implements MouseListener {

	private static final long serialVersionUID = -2654237969531133307L;

	private static Logger log = Logger.getLogger(HyperLinkFLabel.class);

	private String url;// 超链接地址
	
	private boolean isMouseIn;// 鼠标是否进入标签
	
	public HyperLinkFLabel() {
		super();
		initParameters();
	}
	
	/**
	 * 通过显示文本,超链接地址来构建Label
	 * 
	 * @param text
	 *            显示文本
	 * @param url
	 *            超链接地址
	 */
	public HyperLinkFLabel(String text, String url) {
		super(text);
		this.url = url;
		isMouseIn = false;
	}
	
	/**
	 * 参数初始化
	 */
	private void initParameters(){
		url = "";
		isMouseIn = false;
	}
	
	public void paint(Graphics g) {
		if(isMouseIn){
			this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		}else{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		super.paint(g);
	}

	public void mouseClicked(MouseEvent e) {
		try {
			if(url.trim().length()==0){
				return;
			}
			Runtime.getRuntime().exec("cmd.exe /c start " + url);
		} catch (IOException ioe) {
			log.info("打开浏览器出错!");
			ioe.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
		isMouseIn = true;
		this.repaint();
	}

	public void mouseExited(MouseEvent e) {
		isMouseIn = false;
		this.repaint();
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

}
