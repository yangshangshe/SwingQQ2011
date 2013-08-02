package org.fw.cellrender;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.fw.OpaquePanel;
import org.fw.data.ImageIconItem;

public class ImageIconCellRenderer extends JComponent implements ListCellRenderer{

	private static final long serialVersionUID = -6311168827384041226L;
	static Color listForeground, listBackground, listSelectionForeground,
	listSelectionBackground;

	static {
	UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
	listForeground = uid.getColor("List.foreground");
	listBackground = uid.getColor("List.background");
	listSelectionForeground = uid.getColor("List.selectionForeground");
	listSelectionBackground = uid.getColor("List.selectionBackground");
	}
	
	protected JLabel statusLabel,iconLabel;
	protected OpaquePanel statueComp;
	
	public ImageIconCellRenderer(JLabel statusLabel,JLabel iconLabel,OpaquePanel statueComp){
		this.statueComp = statueComp;
		statueComp.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.statusLabel = statusLabel;
		this.iconLabel = iconLabel;
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		ImageIconItem item = (ImageIconItem) value;
		statusLabel.setText(item.getStatus());
		iconLabel.setIcon(new ImageIcon(item.getIcon()));
		if (isSelected) {
			statueComp.setMouseOver(true);
		} else {
			statueComp.setMouseOver(false);
		}
		return statueComp;
	}
}
