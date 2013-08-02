package org.fw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.fw.event.MoveMouseListener;
import org.fw.qq.QQTalkFrame;
import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class FramePanel extends JPanel {

	private static final long serialVersionUID = 9159162439583812390L;

	private static Logger log = Logger.getLogger(FramePanel.class);

	protected String title;// 标题文字

	protected Image bgImage;// 背景图片

	protected JLabel titleLabel;// 标题标签

	protected MoveMouseListener mml;// 鼠标事件
	
	protected JPanel headPanel;//头部面板
	protected ImageIcon headIcon;//头部图片

	protected OpaqueButton minButton;// 最小化按钮

	protected OpaqueButton maxButton;// 最大化按钮

	protected OpaqueButton closeButton;// 关闭按钮

	protected Icon minIcon;// 最小化按钮图标

	protected Icon maxIcon;// 最大化按钮图标

	protected Icon closeIcon;// 关闭按钮图标

	protected Icon normalIcon;// 普通按钮图标

	protected JFrame parent;// 父窗口

	protected JComponent contain;// 窗体的主要内容

	protected JComponent bottom;// 窗体的底部

	protected Boolean canMove;// 是否可以移动窗口

	protected Boolean canResize;// 是否可以改变窗口大小

	protected Boolean ennableMaxButton;// 是否启用最大化窗口

	protected int hgap= 5;// BorderLayout水平间距

	protected int vgap=5;// BorderLayout垂直间距

	private int iwidth;// 背景图片宽度

	private int iheight;// 背景图片高度
	
	private Boolean isMain=false;//是否是主窗体

	Config basicCFG;// 全局配置
	
	ImageHelper imageHelper;
	MouseListener minMaxListener;

	public FramePanel() {
		init();
	}
	
	public FramePanel(Boolean isMain){
		this.isMain = isMain;
		init();
	}

	/**
	 * 指定文件来创建Panel
	 * 
	 * @param file
	 *            图片文件
	 */
	public FramePanel(File file) {
		try {
			bgImage = ImageIO.read(file);
		} catch (IOException e) {
			log.info("读取图片文件失败!");
			bgImage = null;
			e.printStackTrace();
		}
		init();
	}

	/**
	 * 指定图片路径来创建Panel
	 * 
	 * @param imagePath
	 *            图片路径
	 */
	public FramePanel(String imagePath) {
		try {
			bgImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			log.info("读取图片文件失败!");
			bgImage = null;
			e.printStackTrace();
		}
		init();
	}

	/**
	 * 指定图片来创建Panel
	 * 
	 * @param image
	 *            图片
	 */
	public FramePanel(Image image) {
		this.bgImage = image;
		init();
	}
	/**
	 * 
	 * @param headImage 头部图片 
	 */
	public FramePanel(ImageIcon headImage){
		this.headIcon = headImage;
		imageHelper = new ImageHelper();
		initParameters();
		initComponents(headIcon);
		initListeners();
	}

	private void init() {
		imageHelper = new ImageHelper();
		initParameters();
		initComponents(headIcon);
		initListeners();
	}

	private void initParameters() {
	
		basicCFG = new Config(ProjectPath.getProjectPath()
				+ "cfg/basic.properties");
		
		if (canMove == null) {
			canMove = true;
		}
		if (canResize == null) {
			canResize = true;
		}
		title = "新窗口";
		ennableMaxButton = false;
		hgap = 5;
		vgap = 5;
		iwidth = 0;
		iheight = 0;
		minIcon = imageHelper.getFWImageIcon(basicCFG.getProperty("MinIcon",
				"skin/default/min.png"));
		maxIcon = imageHelper.getFWImageIcon(basicCFG.getProperty("MaxIcon",
				"skin/default/max.png"));
		normalIcon = imageHelper.getFWImageIcon(basicCFG.getProperty("NormalIcon",
				"skin/default/normal.png"));
		closeIcon = imageHelper.getFWImageIcon(basicCFG.getProperty("CloseIcon",
				"skin/default/close.png"));
		
	}

	private void initComponents(ImageIcon headImage) {
		
		this.setLayout(new BorderLayout(5,5));
		
		// 北部
		headPanel = new JPanel(new BorderLayout(5, 5));
		headPanel.setOpaque(false);
		
		if(headIcon != null){
			titleLabel = new JLabel(title,new ImageIcon("skin/default/head/head_1.png"),SwingConstants.CENTER);
		}else{
			titleLabel = new JLabel(title);
		}
		headPanel.add(titleLabel, BorderLayout.WEST);

		if (parent != null) {
			JPanel headButtonPanel = new JPanel();
			headButtonPanel.setLayout(new FlowLayout());
			headButtonPanel.setOpaque(false);
			if (minButton == null) {
				minButton = new OpaqueButton(minIcon);
				minButton.setToolTipText(basicCFG.getProperty("MinIconTip",
				"最小化"));
				minButton.addMouseListener(new MouseListener() {

					public void mouseClicked(MouseEvent mouseevent) {
						parent.setExtendedState(JFrame.ICONIFIED);// 最小化
					}

					public void mouseEntered(MouseEvent mouseevent) {
						minButton.setMouseOver(true);
					}

					public void mouseExited(MouseEvent mouseevent) {
						minButton.setMouseOver(false);
					}

					public void mousePressed(MouseEvent mouseevent) {
					}

					public void mouseReleased(MouseEvent mouseevent) {
					}
				});
			}
			if (ennableMaxButton) {
				if (maxButton == null) {
					maxButton = new OpaqueButton(maxIcon);
					maxButton.setToolTipText(basicCFG.getProperty("MaxIconTip",
					"最大化"));
					maxButton.addMouseListener(new MouseListener() {

						public void mouseClicked(MouseEvent mouseevent) {

							if (parent.getExtendedState() == JFrame.NORMAL) {
								parent.setExtendedState(JFrame.MAXIMIZED_BOTH);// 最大化
								maxButton.setIcon(normalIcon);
								maxButton.setToolTipText(basicCFG.getProperty("NormalIconTip",
								"还原"));
								return;
							} else if (parent.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
								parent.setExtendedState(JFrame.NORMAL);
								maxButton.setIcon(maxIcon);
								maxButton.setToolTipText(basicCFG.getProperty("MaxIconTip",
								"最大化"));
								return;
							}
						}

						public void mouseEntered(MouseEvent mouseevent) {
							maxButton.setMouseOver(true);
						}

						public void mouseExited(MouseEvent mouseevent) {
							maxButton.setMouseOver(false);
						}

						public void mousePressed(MouseEvent mouseevent) {
						}

						public void mouseReleased(MouseEvent mouseevent) {
						}
					});
				}
			}
			if (closeButton == null) {
				closeButton = new OpaqueButton(closeIcon);
				closeButton.setToolTipText(basicCFG.getProperty("CloseIconTip",
				"关闭"));
				closeButton.addMouseListener(new MouseListener() {

					public void mouseClicked(MouseEvent mouseevent) {
						if(isMain){
							parent.setVisible(false);
							System.exit(0);
						}else{
							parent.setVisible(false);
						}
						
						if(parent instanceof QQTalkFrame){
							
						}
					}

					public void mouseEntered(MouseEvent mouseevent) {
						closeButton.setMouseOver(true);
						parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}

					public void mouseExited(MouseEvent mouseevent) {
						closeButton.setMouseOver(false);
					}

					public void mousePressed(MouseEvent mouseevent) {
					}

					public void mouseReleased(MouseEvent mouseevent) {
					}
				});
			}
			headButtonPanel.add(minButton);
			if (ennableMaxButton) {
				headButtonPanel.add(maxButton);
			}
			headButtonPanel.add(closeButton);

			headPanel.add(headButtonPanel, BorderLayout.EAST);
		}
		this.add(headPanel, BorderLayout.NORTH);
		
		minMaxListener = new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					if (parent.getExtendedState() == JFrame.NORMAL) {
						parent.setExtendedState(JFrame.MAXIMIZED_BOTH);// 最大化
						maxButton.setIcon(normalIcon);
						maxButton.setToolTipText(basicCFG.getProperty("NormalIconTip",
						"还原"));
						return;
					} else if (parent.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
						parent.setExtendedState(JFrame.NORMAL);
						maxButton.setIcon(maxIcon);
						maxButton.setToolTipText(basicCFG.getProperty("MaxIconTip",
						"最大化"));
						return;
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				
			}

			public void mouseExited(MouseEvent e) {
				
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {
				
			}
			
		};
		
		headPanel.addMouseListener(minMaxListener);
		// 中间
		contain = new JPanel();
		contain.setOpaque(false);
		contain.setLayout(new GridBagLayout());
		if (contain != null) {
			this.add(contain, BorderLayout.CENTER);
		}
		// 底部
		bottom = new JPanel();
		bottom.setOpaque(false);
		bottom.setLayout(new GridBagLayout());
		if (bottom != null) {
			this.add(bottom, BorderLayout.SOUTH);
		}
	}

	private void initListeners() {
		if (canResize || canMove) {
			mml = new MoveMouseListener(this);
			mml.setCanMove(canMove);
			mml.setCanResize(canResize);
			this.addMouseListener(mml);
			this.addMouseMotionListener(mml);
		}
	}

	protected void paintComponent(Graphics g) {
		if (bgImage != null) {
			processBackground(g);
		}
	}

	/**
	 * 设置是否可以拖动
	 * 
	 * @param canDrag
	 *            是否可以拖动
	 */
	public void setDraggable(Boolean canDrag) {
		this.canMove = canDrag;
		if (!canDrag) {
			this.removeMouseListener(mml);
			this.removeMouseMotionListener(mml);
		} else {
			this.addMouseListener(mml);
			this.addMouseMotionListener(mml);
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		this.title = title;
		titleLabel.setText(title);
	}

	public void setTitle(String title,ImageIcon icon){
		this.title = title;
		titleLabel.setIcon(icon);
		titleLabel.setText(title);
		headPanel.repaint();
	}
	
	/**
	 * 设置背景图片
	 * 
	 * @param image
	 *            要用的背景图片
	 */
	public void setBackground(Image image) {
		this.bgImage = image;
		iwidth = bgImage.getHeight(this);
		iheight = bgImage.getHeight(this);
		this.repaint();
	}
	/**
	 * 循环画背景图片
	 * 
	 * @param g
	 */
	private void processBackground(Graphics g) {
		if (bgImage == null) {
			g.setColor(Color.blue);
			g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 5, 5);
		} else {
			int width = getWidth();
			int height = getHeight();
			iwidth = bgImage.getHeight(this);
			iheight = bgImage.getHeight(this);
			int x = 0;
			int y = 0;
			g.drawImage(bgImage, x, y, null);
		}
	}

	public JFrame getParent() {
		return parent;
	}

	/**
	 * 设置包含该面板的窗口,面板内的最小化等按钮来控制该窗口的最小化等
	 * 
	 * @param parent
	 *            窗口
	 */
	public void setParent(JFrame parent) {
		this.parent = parent;
		initComponents(headIcon);
	}

	/**
	 * 是否显示最大化按钮
	 * 
	 * @param ennableMaxButton
	 */
	public void setEnnableMaxButton(Boolean ennableMaxButton) {
		this.ennableMaxButton = ennableMaxButton;
		if (this.getParent() != null) {
			initComponents(headIcon);
		}
	}

	/**
	 * 设置布局的水平方向的间距
	 * 
	 * @param hgap
	 *            间距值
	 */
	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	/**
	 * 设置布局的垂直方向的间距
	 * 
	 * @param vgap
	 *            间距值
	 */
	public void setVgap(int vgap) {
		this.vgap = vgap;
	}


//	/**
//	 * 设置面板的底部内容
//	 * 
//	 * @param bottom
//	 *            底部内容,基本上也是panel
//	 */
//	public void setBottom(JComponent bottom) {
//		this.bottom = bottom;
//		if (bottom instanceof JPanel) {
//			bottom.setOpaque(false);
//		}
//		initComponents();
//	}

	public Boolean getCanResize() {
		return canResize;
	}

	public void setCanResize(Boolean canResize) {
		this.canResize = canResize;
	}

	public Boolean getCanMove() {
		return canMove;
	}

	public void setCanMove(Boolean canMove) {
		this.canMove = canMove;
	}
	
	/**
	 * 添加组件到面板中间部分
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param anchor
	 * @param fill
	 * @param weightx
	 * @param weighty
	 */
	public void addContainWithGridBag(Component comp,  int x, int y,
			int width, int height, int anchor, int fill, double weightx,
			double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		contain.add(comp, gbc);
	}
	
	public void addContainWithGridBag(Component comp,  int x, int y,
			int width, int height, int anchor, int fill, double weightx,
			double weighty,Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		contain.add(comp, gbc);
	}
	/**
	 * 添加组件到面板底部部分
	 * @param comp
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param anchor
	 * @param fill
	 * @param weightx
	 * @param weighty
	 */
	public void addBottomWithGridBag(Component comp,  int x, int y,
			int width, int height, int anchor, int fill, double weightx,
			double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		bottom.add(comp, gbc);
	}
	public void addBottomWithGridBag(Component comp,  int x, int y,
			int width, int height, int anchor, int fill, double weightx,
			double weighty,Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		bottom.add(comp, gbc);
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
}
