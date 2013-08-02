package org.fw.qq.plugins.screencut;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class MyClipboard {
	/**
	 * 将图片保存到剪贴板中
	 * 
	 * @param image
	 *            要保存的图片
	 */
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			// 返回 DataFlavor 对象的数组，指示可用于提供数据的 flavor
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			// 返回此对象是否支持指定的数据 flavor
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			// 返回一个对象，该对象表示将要被传输的数据
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(trans, null);
	}

	/**
	 * 将图片保存到剪贴板中
	 * 
	 * @param image
	 *            要保存的图片
	 */
	public static void setClipboardString(final String str) {
		Transferable trans = new Transferable() {
			// 返回 DataFlavor 对象的数组，指示可用于提供数据的 flavor
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.stringFlavor };
			}

			// 返回此对象是否支持指定的数据 flavor
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.stringFlavor.equals(flavor);
			}

			// 返回一个对象，该对象表示将要被传输的数据
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor))
					return str;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(trans, null);
	}
	
	/**
	 * 从剪贴板获取图片
	 * 
	 * @return 剪贴板中的图片
	 * @throws Exception
	 */
	public static Image getImageFromClipboard() throws Exception {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable pic = clipboard.getContents(null);
		if (pic != null) {
			// 检查内容是否是图片类型
			if (pic.isDataFlavorSupported(DataFlavor.imageFlavor))
				return (Image) pic.getTransferData(DataFlavor.imageFlavor);
		}
		return null;
	}
	
	/**
	 * 从剪贴板获取字符串
	 * @return 剪贴板中的字符串
	 * @throws Exception
	 */
	public static String getTestFromClipboard() throws Exception {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable str = clipboard.getContents(null);
		if (str != null) {
			// 检查内容是否是文本类型
			if (str.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String) str.getTransferData(DataFlavor.stringFlavor);
		}
		return null;
	}

}
