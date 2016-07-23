package com.xtsoft.appropriate.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class Appropriate extends BaseModel<Appropriate> {
	private long appropriateId;
	private long createor;
	private String createorName;
	private int status;
	private float amount;// ×Ü½ð¶î
	private int active;
	private long organizationId;
	private long companyId;
	private String  companyName;
	private Date createDate;
	private String  createDateStr;
	private Date appropriateDate;
	private String  appropriateDateStr;
	private String description;
	private long  type;
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getAppropriateId() {
		return appropriateId;
	}
	public void setAppropriateId(long appropriateId) {
		this.appropriateId = appropriateId;
	}
	public long getCreateor() {
		return createor;
	}
	public void setCreateor(long createor) {
		this.createor = createor;
	}
	public String getCreateorName() {
		return createorName;
	}
	public void setCreateorName(String createorName) {
		this.createorName = createorName;
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
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public Date getAppropriateDate() {
		return appropriateDate;
	}
	public void setAppropriateDate(Date appropriateDate) {
		this.appropriateDate = appropriateDate;
	}
	public String getAppropriateDateStr() {
		return appropriateDateStr;
	}
	public void setAppropriateDateStr(String appropriateDateStr) {
		this.appropriateDateStr = appropriateDateStr;
	}
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
