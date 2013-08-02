package org.fw;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DragAndDropJTree extends JTree implements DragSourceListener, DropTargetListener, DragGestureListener {

	private static final long serialVersionUID = 2484237826433700802L;

	static DataFlavor localObjectFlavor;
	static {
		try {
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	static DataFlavor[] supportedFlavors = {localObjectFlavor};
	DragSource dragSource;
	DropTarget dropTarget;
	public DefaultMutableTreeNode dropTargetNode = null;
	public DefaultMutableTreeNode draggedNode = null;

	public DragAndDropJTree() {
		dragSource = new DragSource();
		// DragGestureRecognizer dgr = dragSource
		// .createDefaultDragGestureRecognizer(this,
		// DnDConstants.ACTION_MOVE, this);
		dropTarget = new DropTarget(this, this);

	}
	
	public void expandAll() {
		expandAll(this, new TreePath(getModel().getRoot()), true);
	}

	public void closeAll() {
		expandAll(this, new TreePath(getModel().getRoot()), false);
	}
	
	private void expandAll(DragAndDropJTree tree, TreePath parent, boolean expand) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		}
		
		if (expand) {
			tree.expandPath(parent);
		} else {
			if(parent.getPathCount()>=2)
			tree.collapsePath(parent);
		}
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}

	public void dragExit(DropTargetEvent dte) {

	}

	public void dragOver(DropTargetDragEvent dtde) {
		Point dragPoint = dtde.getLocation();
		TreePath path = getPathForLocation(dragPoint.x, dragPoint.y);
		if (path == null)
			dropTargetNode = null;
		else {
			dropTargetNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		}

		repaint();

	}

	public void drop(DropTargetDropEvent dtde) {
		Point dropPoint = dtde.getLocation();
		TreePath path = getPathForLocation(dropPoint.x, dropPoint.y);
		if (path == null) {
			return;
		}
		boolean dropped = false;
		try {
			dtde.acceptDrop(DnDConstants.ACTION_MOVE);
			Object droppedObject = dtde.getTransferable().getTransferData(localObjectFlavor);
			DefaultMutableTreeNode droppedNode = null;
			DefaultMutableTreeNode dropNode = (DefaultMutableTreeNode) path.getLastPathComponent();

			if (dropNode.getLevel() == 0) {
				return;
			}

			if (droppedObject instanceof DefaultMutableTreeNode) {
				droppedNode = (DefaultMutableTreeNode) droppedObject;

				((DefaultTreeModel) getModel()).removeNodeFromParent(droppedNode);

			} else {
				droppedNode = new DefaultMutableTreeNode(droppedObject);
			}

			System.out.println("drop");

			if (dropNode.isLeaf() && dropNode.getChildCount() != 0) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dropNode.getParent();
				int index = parent.getIndex(dropNode);
				((DefaultTreeModel) getModel()).insertNodeInto(droppedNode, parent, index);
			} else {
				if (dropNode.getLevel() == 1) {// 一级节点
					((DefaultTreeModel) getModel()).insertNodeInto(droppedNode, dropNode, dropNode.getChildCount());
				} else {// 如果不是一级节点，则添加到它的父节点上(其实也就是添加到一级节点上)
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dropNode.getParent();
					int index = parent.getIndex(dropNode);
					((DefaultTreeModel) getModel()).insertNodeInto(droppedNode, parent, index);
				}

			}
			dropped = true;
		} catch (Exception e) {

			e.printStackTrace();
		}
		dtde.dropComplete(dropped);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		dropTargetNode = null;
		draggedNode = null;
		repaint();
	}

	public void dragEnter(DragSourceDragEvent dsde) {

	}

	public void dragExit(DragSourceEvent dse) {

	}

	public void dragOver(DragSourceDragEvent dsde) {

	}

	public void dropActionChanged(DragSourceDragEvent dsde) {

	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		Point clickPoint = dge.getDragOrigin();
		TreePath path = getPathForLocation(clickPoint.x, clickPoint.y);
		if (path == null) {
			System.out.println("not on a node");
			return;
		}
		draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		Transferable trans = new RJLTransferable(draggedNode);
		dragSource.startDrag(dge, Cursor.getDefaultCursor(), trans, this);
	}

	class RJLTransferable implements Transferable {
		Object object;

		public RJLTransferable(Object o) {
			object = o;
		}

		public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(df))
				return object;
			else
				throw new UnsupportedFlavorException(df);
		}

		public boolean isDataFlavorSupported(DataFlavor df) {
			return (df.equals(localObjectFlavor));
		}

		public DataFlavor[] getTransferDataFlavors() {
			return supportedFlavors;
		}
	}
}
