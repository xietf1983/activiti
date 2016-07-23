package com.xtsoft.reimburse.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class Reimburse extends BaseModel<Reimburse> {
    private long id;
	private String seqCode;
	private long  seqindex;
	public long getSeqindex() {
		return seqindex;
	}

	public void setSeqindex(long seqindex) {
		this.seqindex = seqindex;
	}

	private Date createDate;
	private String  createDateStr;
	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	private long companyId;
	private String  companyName;
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	private String projectCode;
	private String projectName;
	private String typeName;
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	private long type;
	private long userId;
	private long createor;
	private String createorName;
	public String getCreateorName() {
		return createorName;
	}

	public void setCreateorName(String createorName) {
		this.createorName = createorName;
	}

	private int status;
	private float amount;// ×Ü½ð¶î
	private String processInstanceId;//
	private int active;
	private long nextUser;
	public long getNextUser() {
		return nextUser;
	}

	public void setNextUser(long nextUser) {
		this.nextUser = nextUser;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSeqCode() {
		return seqCode;
	}

	public void setSeqCode(String seqCode) {
		this.seqCode = seqCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCreateor() {
		return createor;
	}

	public void setCreateor(long createor) {
		this.createor = createor;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

}
