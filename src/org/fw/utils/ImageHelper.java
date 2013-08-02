package org.fw.utils;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageHelper {

	/**
	 * 获取图片
	 * @param path 图片路径
	 * @return
	 */
	public Image getFWImage(String path){
		ImageIcon image = new ImageIcon(path);
		return image.getImage();
	}
	/**
	 * 获取图片icon
	 * @param path 图片路径
	 * @return
	 */
	public ImageIcon getFWImageIcon(String path){
		ImageIcon image = new ImageIcon(path);
		return image;
	}
}
