package com.xtsoft.analyse.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class AnalyseModel extends BaseModel<AnalyseModel> {
	private long companyId;
	private String companyName;
	private float reimburseamount;
	private float appropriateamount;
	private long organizationId;
	private String organizationName;
	private float balance;

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

	public float getReimburseamount() {
		return reimburseamount;
	}

	public void setReimburseamount(float reimburseamount) {
		this.reimburseamount = reimburseamount;
	}

	public float getAppropriateamount() {
		return appropriateamount;
	}

	public void setAppropriateamount(float appropriateamount) {
		this.appropriateamount = appropriateamount;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
