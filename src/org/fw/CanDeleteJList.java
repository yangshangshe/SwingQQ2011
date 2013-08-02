package org.fw;

import org.fw.cellrender.CanDeleteCellRenderer;
import org.fw.data.CanDeleteItem;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class CanDeleteJList extends JList{

	private static final long serialVersionUID = -6286055495667023494L;

	private List<CanDeleteItem> itemList = new ArrayList<CanDeleteItem>();

	private JLabel iconLabel, nameLabel, numberLabel;

//	private JButton delBtn;

	private OpaquePanel contentPanel;// 状态面板

	public CanDeleteJList() {
		initComponents();
	}

	private void initComponents() {
		//设置单元格布局
		contentPanel = new OpaquePanel();
		contentPanel.setLayout(new GridBagLayout());
		iconLabel = new JLabel();
		addWithGridBag(iconLabel, contentPanel, 0, 0, 1, 2,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
		nameLabel = new JLabel();
		addWithGridBag(nameLabel, contentPanel, 1, 0, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 1, 0);
		numberLabel = new JLabel();
		addWithGridBag(numberLabel, contentPanel, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 1, 0);
//		delBtn = new JButton();
//		addWithGridBag(delBtn, contentPanel, 2, 0, 1, 2,
//				GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0);
//		opacify(contentPanel);
		
		
		//设置单元格渲染类
		setCellRenderer(new CanDeleteCellRenderer(contentPanel,iconLabel,nameLabel,numberLabel));
		
		//填充数据
		setModelData();
	}
	
	/**
	 * 添加数据内容
	 * @param item 数据内容
	 */
	public void addCanDeleteItem(String icon,String name,String number){
		itemList.add(new CanDeleteItem(icon,name,number));
		setModelData();
	}
	
	/**
	 * 设置数据模型
	 *
	 */
	private void setModelData() {
		setModel(new DefaultListModel());
		DefaultListModel mod = (DefaultListModel) getModel();
		for(CanDeleteItem item : itemList){
			mod.addElement(item);
		}
	}

	/**
	 * 布局参数设置
	 * 
	 * @param comp
	 * @param cont
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param anchor
	 * @param fill
	 * @param weightx
	 * @param weighty
	 */
	private void addWithGridBag(Component comp, Container cont, int x, int y,
			int width, int height, int anchor, int fill, int weightx,
			int weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		cont.add(comp, gbc);
	}
	
	
	public static void main(String[] args){
		CanDeleteJList list = new CanDeleteJList();
		
		list.addCanDeleteItem("image/bg.jpg","逝水","786074249");
		list.addCanDeleteItem("image/bgImage.jpg","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		list.addCanDeleteItem("image/leaf.jpg","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		list.addCanDeleteItem("image/head.png","逝水","786074249");
		JScrollPane pain = new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JFrame frame = new JFrame("StatusListJList");
		pain.setOpaque(false);
		frame.getContentPane().add(pain);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}
	
}
