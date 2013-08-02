package org.fw;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class NormalBorder extends AbstractBorder {

	private static final long serialVersionUID = -1701846634912442516L;

	int xoff, yoff;
	Color oldColor , newColor;
	Insets insets;
	
	public NormalBorder(int x, int y) {
		this.xoff = x;
		this.yoff = y;
		insets = new Insets(0, 0, xoff, yoff);
		oldColor = new Color(139,149,140);
		newColor = new Color(193,225,246);
	}

	public NormalBorder(int x,int y,Color newColor){
		this.xoff = x;
		this.yoff = y;
		this.newColor = newColor;
		insets = new Insets(0, 0, xoff, yoff);
	}
	
	public Insets getBorderInsets(Component c) {
		return insets;
	}

	public void paintBorder(Component comp, Graphics g, int x, int y,
			int width, int height) {
		
		g.setColor(newColor);
		//左边
		g.fillRect(0, 0, xoff, height - yoff);
		//上边
		g.fillRect(0, 0, width - xoff, yoff);
		
		g.translate(x, y);
		// 右边的线
		g.fillRect(width - xoff, yoff, xoff, height - yoff);
		// 底部的线
		g.fillRect(xoff, height - yoff, width - xoff, yoff);
		g.translate(-x, -y);

	}
}
