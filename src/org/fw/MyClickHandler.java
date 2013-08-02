package org.fw;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

public class MyClickHandler implements MouseListener,MouseMotionListener {

	protected JList list;
	protected int index;//当前鼠标所在JList中的项的次序，JList被选中的单元的次序
	
	static Color listForeground, listBackground, listSelectionForeground,
	listSelectionBackground;

	static {
		UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
		listForeground = uid.getColor("List.foreground");
		listBackground = uid.getColor("List.background");
		listSelectionForeground = uid.getColor("List.selectionForeground");
		listSelectionBackground = uid.getColor("List.selectionBackground");
	}
	
	public MyClickHandler(JList list){
		this.list = list;
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		list.setForeground(listSelectionForeground);
		list.setBackground(listSelectionBackground);
	}

	public void mouseExited(MouseEvent e) {
		list.setForeground(listForeground);
		list.setBackground(listBackground);
	}

	public void mousePressed(MouseEvent e) {
		Point location = e.getPoint();
		System.out.println("press");
		if(e.getSource() instanceof JList){
			index = list.locationToIndex(location);
			list.setSelectedIndex(index);
		}else if(e.getSource() instanceof JButton){

		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		Point location = e.getPoint();
		System.out.println("move");
		if(e.getSource() instanceof JList){
			index = list.locationToIndex(location);
			list.getComponent(index).setForeground(listSelectionForeground);
			list.getComponent(index).setBackground(listSelectionBackground);
		}
	}

}
