package com.xtsoft.kernel.organization.model;

public class OrgRemain {
	private long organizationId;
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public float getReimburseAmounted() {
		return reimburseAmounted;
	}
	public void setReimburseAmounted(float reimburseAmounted) {
		this.reimburseAmounted = reimburseAmounted;
	}
	public float getReimburseAmounting() {
		return reimburseAmounting;
	}
	public void setReimburseAmounting(float reimburseAmounting) {
		this.reimburseAmounting = reimburseAmounting;
	}
	public float getAppropriatAmounted() {
		return appropriatAmounted;
	}
	public void setAppropriatAmounted(float appropriatAmounted) {
		this.appropriatAmounted = appropriatAmounted;
	}
	private float reimburseAmounted;
	private float reimburseAmounting;
	private float appropriatAmounted;
}
