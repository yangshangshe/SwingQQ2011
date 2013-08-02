package org.fw.qq.plugins.screencut;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.fw.qq.QQTalkFrame;
import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class CutScreen extends JFrame implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 7763802628947303464L;
	// 截图的状态常量
	private final int INITIALIZE = 0;// 初始化
	private final int CUTTING = 1;// 截图中
	private final int GETRANGE = 2;// 已获得一个区域大小
	private final int SETRANGE = 3;// 改变区域大小
	private final int COMPLETE = 4;// 截图完成
	private final int HOTWIDTH = 5;// 鼠标热点宽度
	private final int HOTHEIGHT = 5;// 鼠标热点高度
	
	private ToolBar toolBar;// 工具条
	private JPopupMenu toolMenu;// 弹出菜单
	private JMenuItem completeItem, saveItem, showItem, exitItem;// 菜单项
	private int startX = 0, startY = 0, endX = 0, endY = 0;// 截图的起始坐标
	private int x, y;// 鼠标当前的x,y坐标
	private int offsetX, offsetY;// 鼠标当前位置与区域起点的偏移量
	private int state = INITIALIZE;
	private int width = 0;// 截图区域的宽度
	private int height = 0;// 截图区域的高度

	private BufferedImage bufImg;// 用于存放全屏的图片
	private BufferedImage targetImg;// 用于存放截取的图片
	
	Config cfg;
	CutScreen cutScreen;
	Robot robot;
	Toolkit toolkit = Toolkit.getDefaultToolkit();

	public JPopupMenu getToolMenu() {
		return toolMenu;
	}

	public BufferedImage getTargetImg() {
		return targetImg;
	}

	@SuppressWarnings("deprecation")
	public CutScreen() {
		ImageHelper imageHelper = new ImageHelper();
		cfg = new Config(ProjectPath.getProjectPath()
				+ "cfg/pluginsScreenCut.properties");
		cutScreen = this;
		init();

		toolMenu = new JPopupMenu();
		// 加载图片资源
		Image completeImage = imageHelper.getFWImage(
				cfg.getProperty("completeImage", "skin/myplugin/screencut/right.png"));
		Image saveImage = imageHelper.getFWImage(
				cfg.getProperty("saveImage", "skin/myplugin/screencut/save.png"));
		Image cancelImage = imageHelper.getFWImage(
				cfg.getProperty("cancelImage", "skin/myplugin/screencut/wrong.png"));

		// 定义菜单项
		completeItem = new JMenuItem("完成截图", new ImageIcon(completeImage));
		saveItem = new JMenuItem("保存截图", new ImageIcon(saveImage));
		showItem = new JMenuItem("隐藏工具栏");
		exitItem = new JMenuItem("退出截图", new ImageIcon(cancelImage));

		// 添加菜单
		toolMenu.add(completeItem);
		toolMenu.add(saveItem);
		toolMenu.addSeparator();
		toolMenu.add(showItem);
		toolMenu.add(exitItem);

		// 设置菜单鼠标经过的样式
		setPopuMenuItem(completeItem);
		setPopuMenuItem(saveItem);
		setPopuMenuItem(showItem);
		setPopuMenuItem(exitItem);

		// 添加焦点监听器
		toolMenu.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {

			}

			@Override
			public void focusLost(FocusEvent arg0) {
				toolMenu.setVisible(false);
			}

		});

		completeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO 完成截图事件
				state = COMPLETE;
				// 保存截图到剪贴板
				MyClipboard.setClipboardImage(targetImg);
				toolMenu.setVisible(false);
				toolBar.setVisible(false);
				QQTalkFrame.getInstance().getCutScreen().setVisible(false);
				QQTalkFrame.getInstance().setVisible(true);
			}
		});

		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolMenu.setVisible(false);
				toolBar.savePic(toolBar,cutScreen);

			}
		});

		showItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (toolBar.isVisible()) {
					toolBar.setVisible(false);
					showItem.setText("显示工具栏");
				} else {
					toolBar.setVisible(true);
					showItem.setText("隐藏工具栏");
				}
				toolMenu.setVisible(false);
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toolBar.setVisible(false);
				QQTalkFrame.getInstance().getCutScreen().setVisible(false);
				QQTalkFrame.getInstance().setVisible(true);
				toolMenu.setVisible(false);
			}
		});

		this.setSize(toolkit.getScreenSize());
		this.setCursor(Cursor.CROSSHAIR_CURSOR);
		this.setResizable(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setUndecorated(true);
	}

	protected void init() {
		try {
			robot = new Robot();
			bufImg = robot.createScreenCapture(new Rectangle(toolkit
					.getScreenSize()));
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		if (arg0.getButton() == 3) {// 单击鼠标右键
			if (state == INITIALIZE) {
				this.setVisible(false);
				toolBar.setVisible(false);
			}
			// 在截图区域内
			if (arg0.getX() > startX && arg0.getX() < startX + width
					&& arg0.getY() > startY && arg0.getY() < startY + height) {
				if (state == GETRANGE) {
					toolMenu.setLocation(arg0.getX(), arg0.getY());
					toolMenu.setVisible(true);
				}
			}
		}
		if (arg0.getButton() == 1) {// 单击左键
			toolMenu.setVisible(false);
		}
		if (arg0.getClickCount() == 2 && arg0.getButton() == 1) {
			if (endX - startX > 0 && endY - startY > 0) {
				success();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mousePressed(MouseEvent arg0) {
		// 初始化状态按下
		if (state == INITIALIZE) {
			state = CUTTING;
			startX = arg0.getX();
			startY = arg0.getY();
		}

		if (state == GETRANGE) {
			x = arg0.getX();
			y = arg0.getY();
			offsetX = x - startX;
			offsetY = y - startY;
			// 鼠标在热点按下
			if (x > startX && x < startX + HOTWIDTH && y > startY
					&& y < startY + HOTWIDTH) {// 左上角
				this.setCursor(Cursor.NW_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX + width / 2
					&& x < startX + width / 2 + HOTWIDTH && y > startY
					&& y < startY + HOTWIDTH) {// 上边界
				this.setCursor(Cursor.N_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY && y < startY + HOTWIDTH) {// 右上角
				this.setCursor(Cursor.NE_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX && x < startX + HOTWIDTH
					&& y > startY + height / 2
					&& y < startY + height / 2 + HOTHEIGHT)// 左边界
			{
				this.setCursor(Cursor.W_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY + height / 2
					&& y < startY + height / 2 + HOTHEIGHT)// 右边界
			{
				this.setCursor(Cursor.E_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX && x < startX + HOTWIDTH
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 左下角
			{
				this.setCursor(Cursor.SW_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX + width / 2
					&& x < startX + width / 2 + HOTWIDTH
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 下边界
			{
				this.setCursor(Cursor.S_RESIZE_CURSOR);
				state = SETRANGE;
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 右下角
			{
				this.setCursor(Cursor.SE_RESIZE_CURSOR);
				state = SETRANGE;
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (state == CUTTING) {
			if (Math.abs(endX - startX) > 0 && Math.abs(endY - startY) > 0) {
				width = Math.abs(endX - startX);
				height = Math.abs(endY - startY);
				if (endX - startX < 0) {// 从下往上截图
					startX = startX - width;
					startY = startY - height;
				}
				// 获取截图
				targetImg = bufImg.getSubimage(startX + 1, startY + 1,width - 1, height - 1);

				this.setCursor(Cursor.HAND_CURSOR);
				state = GETRANGE;
				repaint();
				toolBar = new ToolBar(cutScreen);
				toolBar.setLocation(x - 120, y);
				toolBar.setVisible(true);
			}
		}
		// 获取要截图的区域图片
		if (state == GETRANGE) {

			if (width > Toolkit.getDefaultToolkit().getScreenSize().width)
				width = Toolkit.getDefaultToolkit().getScreenSize().width;
			if (height > Toolkit.getDefaultToolkit().getScreenSize().height)
				height = Toolkit.getDefaultToolkit().getScreenSize().height;

			// 获取截图
			targetImg = bufImg.getSubimage(startX + 1, startY + 1, width - 1,
					height - 1);
		}

		if (state == SETRANGE) {
			state = GETRANGE;
			this.setCursor(Cursor.MOVE_CURSOR);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// 选择截图区域
		if (state == CUTTING) {
			endX = arg0.getX();
			endY = arg0.getY();
			this.x = arg0.getX();
			this.y = arg0.getY();
			repaint();
		}
		// 鼠标在截图区域内
		if (x > startX && x < startX + width && y > startY
				&& y < startY + height) {
			// 在已获取截图状态按下，改变截图区域
			if (state == GETRANGE) {
				this.x = arg0.getX();
				this.y = arg0.getY();
				startX = x - offsetX;
				startY = y - offsetY;
				repaint();
			}
		}

		if (state == SETRANGE) {

			if (this.getCursor().getType() == Cursor.NW_RESIZE_CURSOR) {// 如果向左上角拖动
				offsetX = arg0.getX() - startX;
				offsetY = arg0.getY() - startY;
				startX = arg0.getX();
				startY = arg0.getY();
				width -= offsetX;
				height -= offsetY;
			} else if (this.getCursor().getType() == Cursor.N_RESIZE_CURSOR) {// 上
				offsetY = arg0.getY() - startY;
				startY = arg0.getY();
				height -= offsetY;
			} else if (this.getCursor().getType() == Cursor.NE_RESIZE_CURSOR)// 右上角
			{
				offsetX = arg0.getX() - (startX + width);
				offsetY = arg0.getY() - startY;
				startY = arg0.getY();
				width += offsetX;
				height -= offsetY;

			} else if (this.getCursor().getType() == Cursor.W_RESIZE_CURSOR)// 左边
			{
				// 往左边拖时，startX==argo.getX();所以计算偏移量应该是本次坐标减去上次坐标
				offsetX = startX;// 上次坐标的值
				startX = arg0.getX();// 本次坐标的值
				offsetX = startX - offsetX;
				width = width - offsetX;
			} else if (this.getCursor().getType() == Cursor.E_RESIZE_CURSOR)// 右边
			{
				offsetX = arg0.getX() - (startX + width);
				width += offsetX;
			} else if (this.getCursor().getType() == Cursor.SW_RESIZE_CURSOR)// 左下角
			{
				offsetX = arg0.getX() - startX;
				offsetY = arg0.getY() - (startY + height);
				startX = arg0.getX();
				width -= offsetX;
				height += offsetY;
			} else if (this.getCursor().getType() == Cursor.S_RESIZE_CURSOR)// 下
			{
				offsetY = arg0.getY() - (startY + height);
				height += offsetY;
			} else if (this.getCursor().getType() == Cursor.SE_RESIZE_CURSOR)// 右下角
			{
				offsetX = arg0.getX() - (startX + width);
				offsetY = arg0.getY() - (startY + height);
				width += offsetX;
				height += offsetY;
			}
			repaint();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// 改变当前x,y坐标
		if (state == CUTTING) {
			this.x = arg0.getX();
			this.y = arg0.getY();
			repaint();
		}
		// 改变鼠标样式
		if (state == GETRANGE) {
			this.setCursor(Cursor.MOVE_CURSOR);
			// 如果鼠标经过热点，改变鼠标样式
			x = arg0.getX();
			y = arg0.getY();

			if (x > startX && x < startX + HOTWIDTH && y > startY
					&& y < startY + HOTWIDTH) {// 左上角
				this.setCursor(Cursor.NW_RESIZE_CURSOR);
			} else if (x > startX + width / 2
					&& x < startX + width / 2 + HOTWIDTH && y > startY
					&& y < startY + HOTWIDTH) {// 上边界
				this.setCursor(Cursor.N_RESIZE_CURSOR);
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY && y < startY + HOTWIDTH) {// 右上角
				this.setCursor(Cursor.NE_RESIZE_CURSOR);
			} else if (x > startX && x < startX + HOTWIDTH
					&& y > startY + height / 2
					&& y < startY + height / 2 + HOTHEIGHT)// 左边界
			{
				this.setCursor(Cursor.W_RESIZE_CURSOR);
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY + height / 2
					&& y < startY + height / 2 + HOTHEIGHT)// 右边界
			{
				this.setCursor(Cursor.E_RESIZE_CURSOR);
			} else if (x > startX && x < startX + HOTWIDTH
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 左下角
			{
				this.setCursor(Cursor.SW_RESIZE_CURSOR);
			} else if (x > startX + width / 2
					&& x < startX + width / 2 + HOTWIDTH
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 下边界
			{
				this.setCursor(Cursor.S_RESIZE_CURSOR);
			} else if (x > startX + width - HOTWIDTH && x < startX + width
					&& y > startY + height - HOTHEIGHT && y < startY + height)// 右下角
			{
				this.setCursor(Cursor.SE_RESIZE_CURSOR);
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bufImg, 0, 0, this);

		g2d.setBackground(Color.darkGray);
		g2d.fillRect(startX, startY - 35, 160, 35);
		g2d.setColor(Color.white);

		// 绘制左上角的提示信息
		if (state == CUTTING) {
			g2d.drawString(
					"当前大小:" + Math.abs(x - startX) + "*" + Math.abs(y - startY),
					startX + 5, startY - 20);
		} else if (state == GETRANGE || state == SETRANGE) {
			g2d.drawString("当前大小:" + width + "*" + height, startX + 5,
					startY - 20);
		}
		g2d.drawString("双击鼠标快速完成截图。", startX + 5, startY - 5);

		// 绘制区域边框
		if (state == CUTTING) {
			g2d.setColor(Color.red);
			// 绘制区域边框
			g2d.drawRect(startX, startY, x - startX, y - startY);
			// 绘制鼠标热点
			g2d.setColor(Color.black);
			g2d.fillRect(startX, startY, HOTWIDTH, HOTHEIGHT);// 左上角热点
			g2d.fillRect(startX + (x - startX) / 2, startY, HOTWIDTH, HOTHEIGHT);// 上边界热点
			g2d.fillRect(x - HOTWIDTH, startY, HOTWIDTH, HOTHEIGHT);// 右上角热点
			g2d.fillRect(startX, startY + (y - startY) / 2, HOTWIDTH, HOTHEIGHT);// 左边界热点
			g2d.fillRect(x - HOTWIDTH, startY + (y - startY) / 2, HOTWIDTH,
					HOTHEIGHT);// 右边界热点
			g2d.fillRect(startX, y - HOTHEIGHT, HOTWIDTH, HOTHEIGHT);// 右下角热点
			g2d.fillRect(startX + (x - startX) / 2, y - HOTHEIGHT, HOTWIDTH,
					HOTHEIGHT);// 下边界热点
			g2d.fillRect(x - HOTWIDTH, y - HOTHEIGHT, HOTWIDTH, HOTHEIGHT);// 右下角热点
		} else if (state == GETRANGE || state == SETRANGE) {
			g2d.setColor(Color.red);
			// 绘制区域边框
			g2d.drawRect(startX, startY, width, height);
			// 绘制鼠标热点
			g2d.setColor(Color.black);
			g2d.fillRect(startX, startY, HOTWIDTH, HOTHEIGHT);// 左上角热点
			g2d.fillRect(startX + width / 2, startY, HOTWIDTH, HOTHEIGHT);// 上边界热点
			g2d.fillRect(startX + width - HOTWIDTH, startY, HOTWIDTH, HOTHEIGHT);// 右上角热点
			g2d.fillRect(startX, startY + height / 2, HOTWIDTH, HOTHEIGHT);// 左边界热点
			g2d.fillRect(startX + width - HOTWIDTH, startY + height / 2,
					HOTWIDTH, HOTHEIGHT);// 右边界热点
			g2d.fillRect(startX, startY + height - HOTHEIGHT, HOTWIDTH,
					HOTHEIGHT);// 左下角热点
			g2d.fillRect(startX + width / 2, startY + height - HOTHEIGHT,
					HOTWIDTH, HOTHEIGHT);// 下边界热点
			g2d.fillRect(startX + width - HOTWIDTH,
					startY + height - HOTHEIGHT, HOTWIDTH, HOTHEIGHT);// 右下角热点
			// 设置工具栏位置
			if (showItem.getText().equals("隐藏工具栏")) {
				toolBar.setVisible(true);
			}
			toolBar.setLocation(startX + width - toolBar.getWidth(), startY
					+ height);
		}

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * 截图成功
	 */
	public void success() {
		state = COMPLETE;
		this.setVisible(false);
		toolBar.setVisible(false);
		MyClipboard.setClipboardImage(targetImg);
	}

	/**
	 * 设置菜单项样式
	 * 
	 * @param item
	 *            菜单项
	 */
	public void setPopuMenuItem(final JMenuItem item) {
		item.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				item.setArmed(true);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				item.setArmed(false);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});
	}
}
