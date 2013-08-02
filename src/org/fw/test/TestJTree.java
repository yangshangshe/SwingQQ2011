package org.fw.test;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.fw.OpaqueButton;
import org.fw.OpaquePanel;
import org.fw.cellrender.FriendJTreeCellRenderer;
import org.fw.data.FriendJTreeItem;
import org.fw.utils.Config;
import org.fw.utils.ProjectPath;

public class TestJTree extends JFrame{

	private static final long serialVersionUID = -6786533183043245749L;
	protected OpaquePanel contentPanel;//容器
	protected JLabel iconLabel;//头像
	protected JLabel nickNameLabel;//昵称
	protected OpaqueButton currentAppBtn;//当前应用
	protected JLabel signatureLabel;//个性签名
	protected List<OpaqueButton> applyList;//应用列表
	
	public TestJTree(){
//		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
//
//		try {
//			UIManager.setLookAndFeel(lookAndFeel);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
//		UIManager.getDefaults().put("Tree.lineTypeDashed", Boolean.TRUE); 
		Config cfg = new Config(ProjectPath.getProjectPath()
				+ "cfg/basic.properties");
		UIManager.getDefaults().put("Tree.leafIcon", null);
		UIManager.getDefaults().put("Tree.openIcon", new ImageIcon(cfg.getProperty("JTree.openIcon", "skin/default/openIcon.png")));
	    UIManager.getDefaults().put("Tree.closedIcon", new ImageIcon(cfg.getProperty("JTree.closeIcon", "skin/default/closeIcon.png")));
	    
		contentPanel = new OpaquePanel();
		contentPanel.setOpaque(false);
		iconLabel = new JLabel();
		nickNameLabel = new JLabel();
		currentAppBtn = new OpaqueButton();
		signatureLabel = new JLabel();
		applyList = null;
		contentPanel.add(iconLabel);
		contentPanel.add(nickNameLabel);
		contentPanel.add(currentAppBtn);
		contentPanel.add(signatureLabel);
		
		FriendJTreeItem i1 = new FriendJTreeItem("我的好友[0/2]",2,"skin/default/head.png",
				"逝水1","杨尚设","786074249","skin/default/music.png","什么状况？",null,"",1,"0.0.0.0",8080);
		FriendJTreeItem i2 = new FriendJTreeItem("我的好友[0/2]",3,"skin/default/head.png",
				"逝水2","杨尚设","786074249","skin/default/music.png","什么状况？",null,"",1,"0.0.0.0",8080);
		FriendJTreeItem i3 = new FriendJTreeItem("我的好友[0/2]",3,"skin/default/head.png",
				"逝水3","杨尚设","786074249","skin/default/music.png","什么状况？",null,"",1,"0.0.0.0",8080);
		FriendJTreeItem i4 = new FriendJTreeItem("我的好友[0/2]",3,"skin/default/head.png",
				"逝水4","杨尚设","786074249","skin/default/music.png","什么状况？",null,"",1,"0.0.0.0",8080);
		FriendJTreeItem i5 = new FriendJTreeItem("我的好友[0/2]",3,"skin/default/head.png",
				"逝水5","杨尚设","786074249","skin/default/music.png","什么状况？",null,"",1,"0.0.0.0",8080);
		DefaultMutableTreeNode  root = new DefaultMutableTreeNode(i1);
		DefaultMutableTreeNode in2 = new DefaultMutableTreeNode(i2);
		DefaultMutableTreeNode in3 = new DefaultMutableTreeNode(i3);
		DefaultMutableTreeNode in4 = new DefaultMutableTreeNode(i4);
		DefaultMutableTreeNode in5 = new DefaultMutableTreeNode(i5);
		root.isRoot();
		root.add(in2);
		in2.add(in4);
		in2.add(in3);
		root.add(in5);
		JTree jtree  = new JTree(); 
		DefaultTreeModel model = new DefaultTreeModel(root,true);
		jtree.setModel(model);
		
//		UIManager.getDefaults().put("Tree.lineTypeDashed", Boolean.TRUE); 
//		Config cfg = new Config(ProjectPath.getProjectPath()
//				+ "cfg/basic.properties");
		FriendJTreeCellRenderer jcell = new FriendJTreeCellRenderer(contentPanel,iconLabel,nickNameLabel,currentAppBtn,signatureLabel,applyList,null);
		jcell.setLeafIcon(null);
		jcell.setClosedIcon(new ImageIcon(cfg.getProperty("JTree.closeIcon", "skin/default/closeIcon.png")));
		jcell.setOpenIcon(new ImageIcon(cfg.getProperty("JTree.openIcon", "skin/default/openIcon.png")));
		jtree.setCellRenderer(jcell);
		   
//		  jScrollPane1.getViewport().add(jTree1,   null);//注意：******
//		jScrollPane1.getViewport().removeAll(); 
		//去掉线
		jtree.putClientProperty("JTree.lineStyle",   "None");
		
		//根不显示
//		jtree.setRootVisible(false);
		JScrollPane j = new JScrollPane();
		j.getViewport().add(jtree,   null);
		
		this.getContentPane().add(j);
		this.setSize(400,200);
		this.setVisible(true);
	}
	
	public static void main(String args[]){
		TestJTree t = new TestJTree();
		t.setDefaultCloseOperation(3);
		
	}
}
