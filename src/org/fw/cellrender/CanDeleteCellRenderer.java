package org.fw.cellrender;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.fw.OpaquePanel;
import org.fw.data.CanDeleteItem;
import org.fw.image.ReSizeImageIcon;

public class CanDeleteCellRenderer extends JComponent implements ListCellRenderer{
	
	private static final long serialVersionUID = -3678591212706028652L;
	protected JLabel iconLabel, nameLabel, numberLabel;
	protected OpaquePanel contentPanel;// 状态面板
	protected int currentIndex;

	public CanDeleteCellRenderer(OpaquePanel contentPanel,JLabel iconLabel,JLabel nameLabel,JLabel numberLabel){
		this.contentPanel = contentPanel;
		contentPanel.setLayout(new GridBagLayout());
		this.iconLabel = iconLabel;
		this.nameLabel = nameLabel;
		this.numberLabel = numberLabel;
		addWithGridBag(iconLabel, contentPanel, 0, 0, 1, 2,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
		addWithGridBag(nameLabel, contentPanel, 1, 0, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 1, 0);
		addWithGridBag(numberLabel, contentPanel, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, 1, 0);
	}
	
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		CanDeleteItem item = (CanDeleteItem)value;

		ImageIcon icon;
		//将大图转化为32*32的大小
		if(isSelected){
			 icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(32f);
		}else{
			 icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(20f);
		}
		
		iconLabel.setIcon(icon);
		nameLabel.setText(item.getName());
		numberLabel.setText(item.getNumber());
		if(isSelected){
			contentPanel.setMouseOver(true);
		}else{
			contentPanel.setMouseOver(false);
		}
		return contentPanel;
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
}
