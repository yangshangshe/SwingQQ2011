package org.fw.data;

/**
 * ÄÚÈÝÏî
 * 
 * @author Administrator
 * 
 */
public class CanDeleteItem {
	String icon;

	String name;

	String number;

	public CanDeleteItem(String icon, String name, String number) {
		this.icon = icon;
		this.name = name;
		this.number = number;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String toString(){
		return this.number;
	}
}