package org.fw;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JWindow;

import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class ChangeBackgroundPanel extends JComponent {

	private static final long serialVersionUID = 6324512763235022885L;

	protected JWindow popup;//弹出面板
	
	protected JTabbedPane mainTabbedPane;// tab

	protected ColorSelectionPanel colorPane;// 颜色

	protected Config qqMainFrameUICFG;// 界面配置

	protected ImageSelectionPanel imagePane;// 图片
	
	protected ImageHelper imageHelper;

	public ChangeBackgroundPanel(Point location,int height,FramePanel panel) {
		imageHelper = new ImageHelper();
		this.setOpaque(false);
		this.setLayout(new FlowLayout());
		qqMainFrameUICFG = new Config(ProjectPath.getProjectPath()
				+ "cfg/QQMainFrameUI.properties");
		ColorSelectionPanel colorPane = new ColorSelectionPanel();
		ImageSelectionPanel imagePane = new ImageSelectionPanel(panel);
		mainTabbedPane = new JTabbedPane();
		
		mainTabbedPane.addTab("", imageHelper.getFWImageIcon(qqMainFrameUICFG.getProperty(
				"QQMainFrame.ColorSelectionPanel.ImagePanelImg",
				"skin/default/imagepanel.png")), imagePane, qqMainFrameUICFG
				.getProperty("QQMainFrame.ColorSelectionPanel.ImagePanelTip",
						"皮肤"));
		mainTabbedPane.addTab("", imageHelper.getFWImageIcon(qqMainFrameUICFG.getProperty(
				"QQMainFrame.ColorSelectionPanel.ColorPanelImg",
				"skin/default/colorpanel.png")), colorPane, qqMainFrameUICFG
				.getProperty("QQMainFrame.ColorSelectionPanel.ColorPanelTip",
						"默认颜色"));
		this.add(mainTabbedPane);
		//弹出窗口
		popup = new JWindow(getJFrame(panel));
		
		popup.getContentPane().add(this);
		popup.addWindowFocusListener(new WindowAdapter() {
			public void windowLostFocus(WindowEvent evt) { 
				popup.setVisible(false);
			}
		});
		popup.pack();
		
		//显示弹出窗口
		Point pt = location;
		pt.translate(0,height);
		popup.setLocation(pt);
		popup.toFront();
		popup.setVisible(true);
		popup.requestFocusInWindow();
		
		
	}
	
	public void setNewLocation(Point p){
		popup.setVisible(true);
		popup.setLocation(p);
	}

	protected JFrame getJFrame(JComponent comp) {
		if(comp == null) {
			comp = this;
		}
		
		if(comp.getParent() instanceof JFrame) {
			return (JFrame)comp.getParent();
		}
		return getJFrame((JComponent)comp.getParent());
	}
	

	

	public void hidePopup() {
		if(popup != null && popup.isVisible()) {
			popup.setVisible(false);
		}
	}
}
