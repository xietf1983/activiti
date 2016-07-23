package com.xtsoft.category.model;

import com.xtsoft.dao.base.model.BaseModel;
import com.xtsoft.kernel.util.StringPool;

public class Category extends BaseModel<Category> {
	private long categoryId;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescription() {
		if(description==null){
			return StringPool.BLANK;
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefkey() {
		return defkey;
	}

	public void setDefkey(String defkey) {
		this.defkey = defkey;
	}

	public int getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}
	private String processDefinitionId;
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	private long parentId;
	private String name;
	private String remark;
	private String description;
	private String defkey;
	private int isleaf;
	private String typeKey;
	private int active;
	private long sortId;
	private String  fullPath;
	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	private String defkeyName;
	public String getDefkeyName() {
		return defkeyName;
	}

	public void setDefkeyName(String defkeyName) {
		this.defkeyName = defkeyName;
	}

	public long getSortId() {
		return sortId;
	}

	public void setSortId(long sortId) {
		this.sortId = sortId;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public Object clone() {
		Category object = new Category();
		object.setCategoryId(getCategoryId());
		object.setRemark(getRemark());
		object.setParentId(getParentId());
		object.setName(getName());
		object.setIsleaf(getIsleaf());
		object.setDefkey(getDefkey());
		object.setTypeKey(getTypeKey());
		return null;
	}

}
