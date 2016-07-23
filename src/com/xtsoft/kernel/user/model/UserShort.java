package com.xtsoft.kernel.user.model;

public class UserShort {
	private long userId;
	private String emailaddress;
	private String password;
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	private String userName;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
