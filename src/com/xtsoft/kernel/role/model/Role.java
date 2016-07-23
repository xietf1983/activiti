package com.xtsoft.kernel.role.model;

import com.xtsoft.dao.base.model.BaseModel;
import com.xtsoft.kernel.util.StringPool;


/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class Role extends BaseModel<Role> {
	private long roleId;
	private String name;
	private String title;
	private String description;

	public Object clone() {
		Role clone = new Role();
		clone.setDescription(getDescription());
		clone.setName(getName());
		clone.setRoleId(getRoleId());
		clone.setTitle(getTitle());
		return clone;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		if (this.name == null) {
			return StringPool.BLANK;
		} else {
			return this.name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		if (this.title == null) {
			return StringPool.BLANK;
		} else {
			return this.title;
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		if (this.description == null) {
			return StringPool.BLANK;
		} else {
			return this.description;
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

}