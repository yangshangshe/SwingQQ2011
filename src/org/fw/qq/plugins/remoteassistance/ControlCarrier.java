package org.fw.qq.plugins.remoteassistance;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControlCarrier implements Serializable {

	private String type;
	private int mouseX = -1;
	private int mouseY = -1;
	private int wheelAmt = -1;
	private int mousePressBtn = -1;
	private int mouseReleaseBtn = -1;
	private List<Integer> keyPressCode = new ArrayList<Integer>();
	private List<Integer> keyReleaseCode = new ArrayList<Integer>();
	private byte[] desktopImg = null;

	public byte[] getDesktopImg() {
		return desktopImg;
	}
	public void setDesktopImg(byte[] desktopImg) {
		this.desktopImg = desktopImg;
	}
	public int getMousePressBtn() {
		return mousePressBtn;
	}
	public void setMousePressBtn(int mousePressBtn) {
		this.mousePressBtn = mousePressBtn;
	}
	public int getMouseReleaseBtn() {
		return mouseReleaseBtn;
	}
	public void setMouseReleaseBtn(int mouseReleaseBtn) {
		this.mouseReleaseBtn = mouseReleaseBtn;
	}
	public int getMouseX() {
		return mouseX;
	}
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}
	public int getMouseY() {
		return mouseY;
	}
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}
	public int getWheelAmt() {
		return wheelAmt;
	}
	public void setWheelAmt(int wheelAmt) {
		this.wheelAmt = wheelAmt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074156105274743383L;

	public List<Integer> getKeyPressCode() {
		return keyPressCode;
	}
	public void setKeyPressCode(List<Integer> keyPressCode) {
		this.keyPressCode = keyPressCode;
	}
	public List<Integer> getKeyReleaseCode() {
		return keyReleaseCode;
	}
	public void setKeyReleaseCode(List<Integer> keyReleaseCode) {
		this.keyReleaseCode = keyReleaseCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		PropertyDescriptor[] pd = null;
		try {
			pd = Introspector.getBeanInfo(this.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : pd) {
				Method method = propertyDescriptor.getReadMethod();
				builder.append("\n ");
				builder.append(propertyDescriptor.getName());
				builder.append(" : ");
				builder.append(method.invoke(this));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
