package org.fw.manager;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

public class FullRepaintManager extends RepaintManager{

	/**
	 *  为所有组件添加脏标记，使全部组件重画
	 */
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h) 
    {
		super.addDirtyRegion(c, x, y, w, h);
		JComponent root = getRootJComponent(c);
		if(c != root){
			super.addDirtyRegion(root,0, 0, root.getWidth(),root.getHeight());
		}
    }
	
	public JComponent getRootJComponent(JComponent c) {
		Container parent = c.getParent();
		if(parent instanceof JComponent){
			return getRootJComponent((JComponent)parent);
		}
		return c;
	}
}