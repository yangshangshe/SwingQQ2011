package org.fw.qq.plugins.screencut;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class ToolBar extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4716824854669427393L;

	ToolBar toolBar;
	JButton saveBtn, cancelBtn, completeBtn;
	JFileChooser fileDialog;// 文件选择器
	CutScreen cutScreen;
	

	public ToolBar(CutScreen cutScreen) {
		this.cutScreen = cutScreen;
		this.toolBar = this;
		this.setSize(120, 30);
		init();
		this.setUndecorated(true);
	}

	protected void init() {
		
		ImageHelper imageHelper = new ImageHelper();
		Config cfg = new Config(ProjectPath.getProjectPath()
				+ "cfg/pluginsScreenCut.properties");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));

		JPanel panelOther = new JPanel();
		panelOther.setLayout(new GridLayout(1, 2));
		panelOther.setBorder(new EmptyBorder(0, 0, 0, 0));
		Image saveImage =imageHelper.getFWImage(
				cfg.getProperty("saveImage", "skin/myplugin/screencut/save.png"));
		saveBtn = new JButton(new ImageIcon(saveImage));
		saveBtn.setToolTipText("保存截图");
		saveBtn.setBorder(BorderFactory.createEmptyBorder());
		saveBtn.addActionListener(this);
		//设置鼠标经过时按钮样式
		setButtonStyle(saveBtn);

		Image cancelImage = imageHelper.getFWImage(
				cfg.getProperty("cancelImage", "skin/myplugin/screencut/wrong.png"));
		cancelBtn = new JButton(new ImageIcon(cancelImage));
		cancelBtn.setToolTipText("退出截图");
		cancelBtn.addActionListener(this);
		cancelBtn.setBorder(BorderFactory.createEmptyBorder());
		//设置鼠标经过时按钮样式
		setButtonStyle(cancelBtn);
		
		
		Image completeImage = imageHelper.getFWImage(
				cfg.getProperty("completeImage", "skin/myplugin/screencut/right.png"));
		completeBtn = new JButton("确定", new ImageIcon(completeImage));
		completeBtn.setToolTipText("完成截图");
		completeBtn.addActionListener(this);
		completeBtn.setBorder(BorderFactory.createEmptyBorder());
		//设置鼠标经过时按钮样式
		setButtonStyle(completeBtn);

		panelOther.add(saveBtn);
		panelOther.add(cancelBtn);

		panel.add(panelOther);
		panel.add(completeBtn);

		this.add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == completeBtn) {
			// 保存截图
			toolBar.setVisible(false);
			cutScreen.setVisible(false);
			//将截图保存在剪贴板中
			MyClipboard.setClipboardImage(cutScreen.getTargetImg());
		} else if (e.getSource() == cancelBtn) {
			//隐藏可能还显示的弹出菜单
			cutScreen.getToolMenu().setVisible(false);
			toolBar.setVisible(false);
			cutScreen.setVisible(false);
		} else if (e.getSource() == saveBtn) {
			//隐藏可能还显示的弹出菜单
			cutScreen.getToolMenu().setVisible(false);
			savePic(this,cutScreen);
			toolBar.setVisible(false);
			cutScreen.setVisible(false);
		}
	}
	//保存图片
	public void savePic(JFrame toolBar,CutScreen cutScreen) {
		// 获取所截的图片
		Image image = cutScreen.getTargetImg();
		// 从上次保存的目录开始
		fileDialog = new JFileChooser(
				);
		String[] picExt = ".jpg,.gif,.png,.bmp,.jpeg".split(",");
		// 循环向文件过滤器列表添加过滤器
		for (int i = 0; i < picExt.length; i++) {
			MyFileFilter filter = new MyFileFilter();
			filter.addFilter(picExt[i], picExt[i]);
			fileDialog.addChoosableFileFilter(filter);
		}

		fileDialog.setDialogTitle("保存图片");
		int returnVal = fileDialog.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {// 如果单击保存按钮
			try {
				// 获取当前文件的后缀
				String fileExt = fileDialog.getFileFilter().getDescription()
						.replace(".", "");
				// 获取要保存的文件路径
				String fileName = fileDialog.getSelectedFile().toString();
				// 如果没有输入文件名
				if (fileName.length() <= 0) {
					return;
				}
				boolean hasExt = false;
				// 检测用户是否输入后缀
				for (int i = 0; i < picExt.length; i++) {
					if (fileName.endsWith(picExt[i])) {
						hasExt = true;
					}
				}
				// 如果没有后缀名,程序为自动用户加上
				if (!hasExt) {
					fileName = fileName + "." + fileExt;
				}
				// 将图片数据写入文件
				ImageIO.write((RenderedImage) image, fileExt,
						new File(fileName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			toolBar.setVisible(false);
			cutScreen.setVisible(false);
		} 
	}
	
	public void setButtonStyle(final JButton btn){
		btn.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				btn.setBorder(BorderFactory.createEtchedBorder());
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				btn.setBorder(BorderFactory.createEmptyBorder());
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

