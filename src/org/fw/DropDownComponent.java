package org.fw;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;


public class DropDownComponent extends JComponent implements AncestorListener{

	private static final long serialVersionUID = 3991938330108392016L;

	protected OpaqueButton arrow;//状态按钮
	protected JWindow popup;//弹出面板
	protected StatusListJList drop_down_comp;//弹出面板中的内容
	protected int statusCode;//状态码
	protected String status;//状态文字
	protected Config basicCFG;// 全局配置
	protected ImageHelper imageHelper;
	public DropDownComponent(StatusListJList drop_down_comp){
		this.drop_down_comp = drop_down_comp;
		basicCFG = new Config(ProjectPath.getProjectPath()
				+ "cfg/basic.properties");
		 imageHelper = new ImageHelper();
		initComponent();		
	}
	
	protected void initComponent(){
		arrow = new OpaqueButton("▼",imageHelper.getFWImageIcon(basicCFG.getProperty("StatusList.onlineImg","skin/default/online.png")));
		arrow.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
				
				//弹出窗口
				popup = new JWindow(getJFrame(null));
				popup.getContentPane().add(drop_down_comp);
				popup.addWindowFocusListener(new WindowAdapter() {
					public void windowLostFocus(WindowEvent evt) { 
						popup.setVisible(false);
					}
				});
				popup.pack();
				
				//显示弹出窗口
				Point pt = arrow.getLocationOnScreen();
				pt.translate(0,arrow.getHeight());
				popup.setLocation(pt);
				popup.toFront();
				popup.setVisible(true);
				popup.requestFocusInWindow();

			}
			
			public void mouseEntered(MouseEvent e) {
				arrow.setMouseOver(true);
			}

			public void mouseExited(MouseEvent e) {
				arrow.setMouseOver(false);
			}

			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}
			
		});
		setLayout(new FlowLayout());
		add(arrow);
		addAncestorListener(this);
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

	public void ancestorAdded(AncestorEvent event) {
		hidePopup();
	}

	public void ancestorMoved(AncestorEvent event) {
		if (event.getSource() != popup) {
			hidePopup();
		}
	}

	public void ancestorRemoved(AncestorEvent event) {
		hidePopup();
	}

	public void hidePopup() {
		if(popup != null && popup.isVisible()) {
			popup.setVisible(false);
		}
	}
	/**
	 * 设置状态
	 * @param value
	 */
	public void setArrowValue(int value,ImageIcon icon,String status){
		this.statusCode = value;
		arrow.setIcon(icon);
		this.status = status;
	}
	/**
	 * 获取状态值
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 获取状态码
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	
}
