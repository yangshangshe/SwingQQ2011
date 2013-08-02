package org.fw;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class OpaqueTextField extends JTextField{

	private static final long serialVersionUID = 9085120605331284763L;

	private float alphaper;// 亮点透明度

	private int arcWidth;// 圆角长度

	private int arcHeight;// 圆角宽度
	
	private boolean isMouseOver;// 鼠标进过
	
	Paint oldPiant;
	
	public OpaqueTextField(){
		init();
	}
	
	public OpaqueTextField(String text){
		super(text);
		init();
	}
	
	private void init() {
		alphaper = 0.3f;
		arcWidth = 5;
		arcHeight = 5;
		Border border = BorderFactory.createEmptyBorder(0, 1, 0, 1);
		this.setBorder(border);
		this.setOpaque(false);
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if(oldPiant == null){
			oldPiant = g2.getPaint();
		}
		
		if (isMouseOver) {
			// 创建高亮
			GradientPaint highlight = new GradientPaint(new Point2D.Float(
					0, 0), new Color(255, 255, 240, (int) (255 * this
					.getAlphaper())), new Point2D.Float(
					this.getWidth() - 1, this.getHeight() - 1), new Color(
							255, 255, 240, (int) (255 * this.getAlphaper())));
			g2.setPaint(highlight);
			g2.fillRoundRect(0, 1, this.getWidth() - 2,
					this.getHeight() - 2, arcWidth, arcHeight);
			g2.drawRoundRect(0, 1, this.getWidth() - 2,
					this.getHeight() - 2, arcWidth, arcHeight);
		}
		
		super.paintComponent(g);
	}
	
	public float getAlphaper() {
		return alphaper;
	}

	public void setAlphaper(float alphaper) {
		this.alphaper = alphaper;
		this.repaint();
	}

	public int getArcHeight() {
		return arcHeight;
	}

	public void setArcHeight(int arcHeight) {
		this.arcHeight = arcHeight;
		this.repaint();
	}

	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
		this.repaint();
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}

	public void setMouseOver(boolean isMouseOver) {
		this.isMouseOver = isMouseOver;
		this.repaint();
	}
}
