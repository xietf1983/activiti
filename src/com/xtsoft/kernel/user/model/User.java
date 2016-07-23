package com.xtsoft.kernel.user.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;
import com.xtsoft.kernel.util.StringPool;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class User extends BaseModel<User> {
	private long userId;
	private Date createDate;
	private Date modifiedDate;
	private int defaultUser;
	private String password;
	private Date passwordModifiedDate;
	private String reminderQueryQuestion;
	private String reminderQueryAnswer;
	private long graceLoginCount;
	private String screenName;// Í«≥∆,œµÕ≥’ ∫≈
	private String emailAddress;
	private String greeting;
	private String userName;
	private String jobTitle;
	private Date loginDate;
	private String loginIP;
	private Date lastLoginDate;
	private String lastLoginIP;
	private int status;
	private String employeeNumber;
	private int male;
	private Date birthday;
	private String tel;
	private String organizationId;
	private String description;
	private int userType;
	private String orgIds;
	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Object clone() {
		User clone = new User();
		clone.setBirthday(getBirthday());
		clone.setCreateDate(getCreateDate());
		clone.setDefaultUser(getDefaultUser());
		clone.setEmailAddress(getEmailAddress());
		clone.setEmployeeNumber(getEmployeeNumber());
		clone.setGraceLoginCount(getGraceLoginCount());
		clone.setGreeting(getGreeting());
		clone.setJobTitle(getJobTitle());
		clone.setLastLoginDate(getLastLoginDate());
		clone.setLastLoginIP(getLastLoginIP());
		clone.setLoginDate(getLoginDate());
		clone.setLoginIP(getLoginIP());
		clone.setMale(getMale());
		clone.setModifiedDate(getModifiedDate());
		clone.setPassword(getPassword());
		clone.setPasswordModifiedDate(getPasswordModifiedDate());
		clone.setReminderQueryAnswer(getReminderQueryAnswer());
		clone.setScreenName(getScreenName());
		clone.setStatus(getStatus());
		clone.setUserId(getUserId());
		clone.setTel(getTel());
		clone.setUserName(getUserName());
		clone.setOrganizationId(getOrganizationId());
		clone.setDescription(getDescription());
		return clone;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(int defaultUser) {
		this.defaultUser = defaultUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getPasswordModifiedDate() {
		return passwordModifiedDate;
	}

	public void setPasswordModifiedDate(Date passwordModifiedDate) {
		this.passwordModifiedDate = passwordModifiedDate;
	}

	public String getReminderQueryQuestion() {
		if (this.reminderQueryQuestion == null) {
			return StringPool.BLANK;
		} else {
			return this.reminderQueryQuestion;
		}
	}

	public void setReminderQueryQuestion(String reminderQueryQuestion) {
		this.reminderQueryQuestion = reminderQueryQuestion;
	}

	public String getReminderQueryAnswer() {
		if (this.reminderQueryAnswer == null) {
			return StringPool.BLANK;
		} else {
			return this.reminderQueryAnswer;
		}
	}

	public void setReminderQueryAnswer(String reminderQueryAnswer) {
		this.reminderQueryAnswer = reminderQueryAnswer;
	}

	public long getGraceLoginCount() {
		return graceLoginCount;
	}

	public void setGraceLoginCount(long graceLoginCount) {
		this.graceLoginCount = graceLoginCount;
	}

	public String getScreenName() {
		if (this.screenName == null) {
			return StringPool.BLANK;
		} else {
			return this.screenName;
		}
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getEmailAddress() {
		if (this.emailAddress == null) {
			return StringPool.BLANK;
		} else {
			return this.emailAddress;
		}
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public String getUserName() {
		if (this.userName == null) {
			return StringPool.BLANK;
		} else {
			return this.userName;
		}
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJobTitle() {
		if (this.jobTitle == null) {
			return StringPool.BLANK;
		} else {
			return this.jobTitle;
		}
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIP() {
		if (this.loginIP == null) {
			return StringPool.BLANK;
		} else {
			return this.loginIP;
		}
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIP() {
		if (this.lastLoginIP == null) {
			return StringPool.BLANK;
		} else {
			return this.lastLoginIP;
		}
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getEmployeeNumber() {
		if (this.employeeNumber == null) {
			return StringPool.BLANK;
		} else {
			return this.employeeNumber;
		}
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public int getMale() {
		return male;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

}