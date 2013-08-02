package org.fw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import org.fw.utils.ImageHelper;

public class ListTextFieldPanel extends JComponent implements AncestorListener{

	private static final long serialVersionUID = -2023982561623123050L;
	
	protected OpaqueButton arrow;//下拉按钮
	protected JTextField inputContent;//输入内容
	protected CanDeleteJList drop_down_comp;//弹出面板中的内容
	protected ImageHelper imageHelper;
	private JWindow popup;

	
	public ListTextFieldPanel(CanDeleteJList drop_down_comp){
		imageHelper= new ImageHelper();
		this.drop_down_comp = drop_down_comp;
		this.setLayout(new BorderLayout(0,0));
		arrow = new OpaqueButton(imageHelper.getFWImageIcon("image/arrow.jpg"));
		inputContent = new JTextField("");
		inputContent.setOpaque(false);
		inputContent.setPreferredSize(new Dimension(180,25));
		inputContent.setBorder(null);
		arrow.setBorder(null);
		this.add(inputContent,BorderLayout.WEST);
		this.add(arrow,BorderLayout.EAST);
		this.setOpaque(false);
		this.setBorder(new NormalBorder(1,1));
		arrow.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				popup = new JWindow(getJFrame(null));
				popup.getContentPane().add(new JLabel("123"));
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
		this.addAncestorListener(this);
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
	
	
	protected JFrame getJFrame(JComponent comp) {
		if(comp == null) {
			comp = this;
		}
		if(comp.getParent() instanceof JFrame) {
			return (JFrame)comp.getParent();
		}
		return getJFrame((JComponent)comp.getParent());
	}

	public static void main(String args[]){
		JFrame f = new JFrame();
		f.setBackground(Color.white);
		f.setSize(200,100);
		f.setVisible(true);
		f.setDefaultCloseOperation(3);
	}
}
