package com.xtsoft.kernel.company.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class Company extends BaseModel<Company> {
	private String name;
	private int active;
	private long companyId;
	private String description;
	private long maxusers;
	private Date createDate;
	private long sortId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getMaxusers() {
		return maxusers;
	}

	public void setMaxusers(long maxusers) {
		this.maxusers = maxusers;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public long getSortId() {
		return sortId;
	}

	public void setSortId(long sortId) {
		this.sortId = sortId;
	}

	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
