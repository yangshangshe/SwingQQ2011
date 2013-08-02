package org.fw;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class ShadowBorder extends AbstractBorder {

	private static final long serialVersionUID = -8222012482406820087L;

	int xoff, yoff;

	Insets insets;

	public ShadowBorder(int x, int y) {
		this.xoff = x;
		this.yoff = y;
		insets = new Insets(0, 0, xoff, yoff);

	}

	public Insets getBorderInsets(Component c) {
		return insets;
	}

	public void paintBorder(Component comp, Graphics g, int x, int y,
			int width, int height) {
		g.setColor(Color.gray);
		g.translate(x, y);
		// 右边的线
		g.fillRect(width - xoff, yoff, xoff, height - yoff);
		// 底部的线
		g.fillRect(xoff, height - yoff, width - xoff, yoff);
		g.translate(-x, -y);

	}
}
