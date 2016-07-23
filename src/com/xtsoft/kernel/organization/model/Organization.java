package com.xtsoft.kernel.organization.model;

import java.util.Date;
import java.util.List;

import com.xtsoft.dao.base.model.BaseModel;

public class Organization extends BaseModel<Organization> {
	private long organizationId;
	private long parentOrganizationId;
	private String comments;
	private int active;
	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	private long userId;
	private String userName;
	private Date createDate;
	private Date modifiedDate;
	private String fullPath;
	private long sortId;
	private String text;
	private boolean leaf;
	private boolean checked;
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}

	private boolean expanded;
	private List<Organization> children;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getSortId() {
		return sortId;
	}

	public void setSortId(long sortId) {
		this.sortId = sortId;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public long getParentOrganizationId() {
		return parentOrganizationId;
	}

	public void setParentOrganizationId(long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

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

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Object clone() {
		Organization clone = new Organization();
		clone.setOrganizationId(getOrganizationId());
		clone.setParentOrganizationId(getParentOrganizationId());
		clone.setUserName(getUserName());
		clone.setSortId(getSortId());
		clone.setCreateDate(getCreateDate());
		clone.setModifiedDate(getModifiedDate());
		clone.setComments(getComments());
		clone.setFullPath(getFullPath());
		return clone;
	}
}
