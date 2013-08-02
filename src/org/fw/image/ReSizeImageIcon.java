package org.fw.image;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.fw.utils.ImageHelper;

public class ReSizeImageIcon extends ImageIcon{

	private static final long serialVersionUID = 3918392615485379614L;
	
	ImageIcon icon;
	String path;//图片路径
	
	public ReSizeImageIcon(String path){
		
		this.path = path;
	}
	
	/**
	 * 获取等比例缩放的图片
	 * @return
	 */
	public ImageIcon getReSizeImageIcon(float width){
		icon = new ImageHelper().getFWImageIcon(path);
	    Image img = icon.getImage();
	    
	    
	    float factor = 1.0f;
	    if (img.getWidth(null) > img.getHeight(null))
	        factor = Math.min (width / img.getWidth(null), 1.0f);
	    else
	        factor = Math.min (width / img.getHeight(null), 1.0f);
	    Image scaledImage =
	        img.getScaledInstance ((int) (img.getWidth(null) * factor),
	                               (int) (img.getHeight(null) * factor),
	                               Image.SCALE_FAST);
	    
	    icon.setImage(scaledImage);
	    return icon;
	}
	/**
	 * 获取指定宽度和高度的图片
	 * @param width
	 * @param height
	 * @return
	 */
	public ImageIcon getReSizeImageIcon(int width,int height){
		icon = new ImageHelper().getFWImageIcon(path);
	    Image img = icon.getImage();
		Image scaledImage =
	        img.getScaledInstance (width,
	        		height,Image.SCALE_FAST);
	    icon.setImage(scaledImage);
	    return icon;
	}
}
