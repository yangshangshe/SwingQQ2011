package org.fw.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.fw.CanDeleteJComboBox;
import org.fw.DropDownComponent;
import org.fw.FramePanel;
import org.fw.OpaqueTextField;
import org.fw.StatusListJList;
import org.fw.manager.FullRepaintManager;

public class TestFramePanel {
	static Color listForeground, listBackground, listSelectionForeground,
	listSelectionBackground;
	static {
		UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
		listForeground = uid.getColor("List.foreground");
		listBackground = uid.getColor("List.background");
		listSelectionForeground = uid.getColor("List.selectionForeground");
		listSelectionBackground = uid.getColor("List.selectionBackground");
	}
	
	public static void main(String[] args){
		RepaintManager.setCurrentManager(new FullRepaintManager());
		JFrame frame = new JFrame("测试FPanel");
		//填充至整个窗口
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		FramePanel panel = new FramePanel();
		panel.setParent(frame);
		panel.setCanMove(false);
		panel.setTitle("QQ2011");
		panel.setEnnableMaxButton(true);
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new FlowLayout());
		jPanel.setOpaque(false);
		jPanel.add(new JButton("按钮"));
//		System.out.println(1);
		
		//状态下拉框测试
		final StatusListJList statusList = new StatusListJList();
		MouseListener mouseListener = new MouseAdapter() {

		};
		statusList.addMouseListener(mouseListener);
		statusList.addStatusItem("QQ","skin/default/qq.png");
		statusList.addStatusItem("QQ2","skin/default/qq_2.png");
		statusList.addStatusItem("Cl","skin/default/qq.png");
		final DropDownComponent d = new DropDownComponent(statusList);
		statusList.addPropertyChangeListener("selectedStatus",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                d.hidePopup();
                String selectedStatus = (String)evt.getNewValue();
                int value = Integer.parseInt(selectedStatus.split("☆")[0]);
                ImageIcon icon = new ImageIcon(selectedStatus.split("☆")[1]);
                String status = selectedStatus.split("☆")[2];
                
                d.setArrowValue(value, icon, status);
            }
        });
		jPanel.add(d);
		panel.addContainWithGridBag(jPanel, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0);
		final OpaqueTextField textfield = new OpaqueTextField("编辑个性签名");
		textfield.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				textfield.setMouseOver(true);
			}

			public void mouseExited(MouseEvent e) {
				textfield.setMouseOver(false);
			}

			public void mousePressed(MouseEvent e) {
				
			}

			public void mouseReleased(MouseEvent e) {
				
			}
			
		});
		jPanel.add(textfield);
//		panel.setContain(jPanel);
		JPanel jPanel2 = new JPanel();
		CanDeleteJComboBox list = new CanDeleteJComboBox();
		list.setPreferredSize(new Dimension(180,25));
		
		list.addCanDeleteItem("skin/default/bg.jpg","逝水","786074249");
		list.addCanDeleteItem("skin/default/bg.jpg","逝水","786074249");
		list.addCanDeleteItem("skin/default/bg.jpg","逝水","786074249");
		list.addCanDeleteItem("skin/default/bg.jpg","逝水","786074249");
		jPanel2.add(list);
//		panel.setBottom(jPanel2);
		panel.addBottomWithGridBag(jPanel2, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0);
		Image bgImage =  Toolkit.getDefaultToolkit().getImage("skin/default/bg.jpg");
		System.out.println(bgImage);
		panel.setBackground(bgImage);
		frame.add(panel);
		frame.setLocation(200,200);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(3);
		frame.setSize(260,625);
		frame.setVisible(true);
	}
}
