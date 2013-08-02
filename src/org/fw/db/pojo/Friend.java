package org.fw.db.pojo;

import java.io.Serializable;

public class Friend implements Serializable{

	private static final long serialVersionUID = 3748748603823242218L;
	
	private int id;
	private int userid;
	private int friendid;
	public int getFriendid() {
		return friendid;
	}
	public void setFriendid(int friendid) {
		this.friendid = friendid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
