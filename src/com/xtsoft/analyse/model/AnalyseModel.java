package com.xtsoft.analyse.model;

import java.util.Comparator;
import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class AnalyseModel extends BaseModel<AnalyseModel> implements Comparator {
	private long companyId;
	private Long companySortId;
	private String companyName;
	private float reimburseamount;
	private float appropriateamount;
	private long organizationId;
	private Long organizationSortId;

	public Long getCompanySortId() {
		return companySortId;
	}

	public void setCompanySortId(long companySortId) {
		this.companySortId = companySortId;
	}

	public Long getOrganizationSortId() {
		return organizationSortId;
	}

	public void setOrganizationSortId(long organizationSortId) {
		this.organizationSortId = organizationSortId;
	}

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

	public int CompareTo(AnalyseModel other) {
		int retvalue = 1;
		if (other == null) {
			return 1;
		}
		long value = this.companySortId - other.companySortId;
		if (value == 0) {
			value = this.getOrganizationSortId() - other.getOrganizationSortId();
		}
		if (value == 0) {
			return 0;
		} else if (value > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		AnalyseModel user0 = (AnalyseModel) arg0;
		AnalyseModel user1 = (AnalyseModel) arg1;

		int flag = user0.getCompanySortId().compareTo(user1.getCompanySortId());
		if (flag == 0) {
			flag = user0.getOrganizationSortId().compareTo(user1.getOrganizationSortId());
		} else {
		}
		return flag;
	}

}
