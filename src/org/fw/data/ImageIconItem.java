package org.fw.data;

/**
 * ×´Ì¬Ïî
 * 
 * @author Administrator
 * 
 */
public class ImageIconItem {
	private String icon;

	private String status;
	

	public ImageIconItem(String status, String icon) {
		this.icon = icon;
		this.status = status;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}