package org.fw.db.pojo;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable{

	private static final long serialVersionUID = -4215483474669860924L;
	
	private int id;
	private String number;//qq∫≈
	private String password;//√‹¬Î
	private Set<Friend> friends;
	private UserInfo userInfo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Friend> getFriends() {
		return friends;
	}
	public void setFriends(Set<Friend> friends) {
		this.friends = friends;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
