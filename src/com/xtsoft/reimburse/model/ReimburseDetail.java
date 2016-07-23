package com.xtsoft.reimburse.model;

import java.util.Date;

import com.xtsoft.dao.base.model.BaseModel;

public class ReimburseDetail extends BaseModel<ReimburseDetail> {
	private long detailId;// 详细Id
	private long companyId;
	private long type;
	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	private int status;
	private Date createDate;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getDetailId() {
		return detailId;
	}

	public void setDetailId(long detailId) {
		this.detailId = detailId;
	}


	public long getAppropriateUserId() {
		return appropriateUserId;
	}

	public void setAppropriateUserId(long appropriateUserId) {
		this.appropriateUserId = appropriateUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getBillnumber() {
		return billnumber;
	}

	public void setBillnumber(int billnumber) {
		this.billnumber = billnumber;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	private long reimburseId;// 报销主体Id

	public long getReimburseId() {
		return reimburseId;
	}

	public void setReimburseId(long reimburseId) {
		this.reimburseId = reimburseId;
	}
	private long createUserId;
	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	private long organizationId;// 合伙人ID
	private long appropriateUserId;// 报销人ID
	private String description;// 描述
	private int billnumber;// 票据数量
	private float amount;// 金额
	private String invoiceNo;// 发票编号
	private int active;
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
