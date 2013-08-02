package org.fw.cellrender;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;

import org.fw.DragAndDropJTree;
import org.fw.OpaqueButton;
import org.fw.OpaquePanel;
import org.fw.data.FriendJTreeItem;
import org.fw.image.ReSizeImageIcon;
import org.fw.utils.Config;
import org.fw.utils.ImageHelper;
import org.fw.utils.ProjectPath;

public class FriendJTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 8962591165243824730L;

	ImageHelper imageHelper;

	protected boolean isTargetNode;
	protected boolean isTargetNodeLeaf;
	protected boolean isLastItem;
	protected DragAndDropJTree jtree;
	protected int BOTTOM_PAD = 30;
	protected OpaquePanel contentPanel;// 容器
	protected JLabel iconLabel;// 头像
	protected JLabel nickNameLabel;// 昵称
	protected OpaqueButton currentAppBtn;// 当前应用
	protected JLabel signatureLabel;// 个性签名
	protected List<OpaqueButton> applyList;// 应用列表
	protected boolean isSel;// 是否选选中

	protected Config cfg;
	protected Config qqMainFrameUICFG;

	public FriendJTreeCellRenderer(OpaquePanel contentPanel, JLabel iconLabel, JLabel nickNameLabel, OpaqueButton currentAppBtn, JLabel signatureLabel,
			List<OpaqueButton> applyList, DragAndDropJTree jtree) {
		super();
		isSel = false;
		cfg = new Config(ProjectPath.getProjectPath() + "cfg/basic.properties");
		qqMainFrameUICFG = new Config(ProjectPath.getProjectPath() + "cfg/QQMainFrameUI.properties");
		imageHelper = new ImageHelper();


		this.contentPanel = contentPanel;
		contentPanel.setLayout(new GridBagLayout());
		this.iconLabel = iconLabel;
		this.nickNameLabel = nickNameLabel;
		this.currentAppBtn = currentAppBtn;
		this.signatureLabel = signatureLabel;
		this.applyList = applyList;
		this.jtree = jtree;
		addWithGridBag(iconLabel, contentPanel, 0, 0, 1, 3, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0);
		addWithGridBag(nickNameLabel, contentPanel, 1, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0);
		addWithGridBag(currentAppBtn, contentPanel, 2, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0);
		addWithGridBag(signatureLabel, contentPanel, 1, 1, GridBagConstraints.REMAINDER, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

		isTargetNode = (value) == jtree.dropTargetNode;
		isTargetNodeLeaf = (isTargetNode && ((TreeNode) value).isLeaf());

		// boolean showSelected = sel & (jtree.dropTargetNode == null);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		FriendJTreeItem item = (FriendJTreeItem) node.getUserObject();
		if (node.isRoot() && node.getLevel() == 0) {// 根节点隐藏
			iconLabel.setText("");
			iconLabel.setIcon(null);
			nickNameLabel.setText("");
			currentAppBtn.setText("");
			currentAppBtn.setIcon(null);
			currentAppBtn.setVisible(false);
			signatureLabel.setText("");
			signatureLabel.setIcon(null);
		} else {
			if (node.getLevel() == 1) {// 一级用于显示我的好友等分组信息
				iconLabel.setText(item.getHeadInfo());
				iconLabel.setIcon(null);
				nickNameLabel.setText("");
				currentAppBtn.setText("");
				currentAppBtn.setIcon(null);
				currentAppBtn.setVisible(false);
				signatureLabel.setText("");
				signatureLabel.setIcon(null);

			} else {
				
				leaf = true;
				
				ImageIcon icon;
				if (hasFocus) {
					icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(40f);
				} else if (item.getDisplayStyle() == FriendJTreeItem.SMALL_ICON) {
					icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(20f);
					System.out.println(icon);
				} else if (item.getDisplayStyle() == FriendJTreeItem.NORMAL_ICON) {
					icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(30f);
				} else {
					icon = new ReSizeImageIcon(item.getIcon()).getReSizeImageIcon(40f);
				}

				iconLabel.setIcon(icon);

				iconLabel.setText("");
				StringBuilder nick = new StringBuilder();
				if (!item.getRemarkName().equals("")) {
					nick.append(item.getRemarkName());
					if (!item.getNickName().equals("")) {
						nick.append("(" + item.getNickName() + ")");
					}
				} else {
					if (!item.getNickName().equals("")) {
						nick.append(item.getNickName());
					} else {
						nick.append(item.getQqNumber());
					}
				}

				nickNameLabel.setText(nick.toString());

				if (item.getCurrentApp() != null && !item.getCurrentApp().equals("")) {
					currentAppBtn.setIcon(new ImageIcon(item.getCurrentApp()));
				} else {
					currentAppBtn.setIcon(null);
				}

				if (!item.getSignature().equals("")) {
					signatureLabel.setText(item.getSignature());
				} else {
					signatureLabel.setText("");
				}

			}
		}
		if (hasFocus) {
			contentPanel.setMouseOver(true);

		} else {
			contentPanel.setMouseOver(false);
		}
		return contentPanel;
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("isTargetNode" + isTargetNode);
		if (isTargetNode) {
			g.setColor(Color.black);
			if (isTargetNodeLeaf) {
				g.drawLine(0, 0, getSize().width, 0);
			} else {
				g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
			}
		}
		if (isSel) {
			System.out.println("isSel" + isSel);
			Graphics2D g2 = (Graphics2D) g;
			// 创建高亮
			GradientPaint highlight = new GradientPaint(new Point2D.Float(0, 0), new Color(255, 255, 240, 240), new Point2D.Float(this.getWidth() - 1, this.getHeight() - 1),
					new Color(255, 255, 240, 240));
			g2.setPaint(highlight);
			g2.fillRoundRect(0, 1, this.getWidth() - 2, this.getHeight() - 2, 5, 5);
			g2.drawRoundRect(0, 1, this.getWidth() - 2, this.getHeight() - 2, 5, 5);
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
	private void addWithGridBag(Component comp, Container cont, int x, int y, int width, int height, int anchor, int fill, int weightx, int weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		cont.add(comp, gbc);
	}

}
