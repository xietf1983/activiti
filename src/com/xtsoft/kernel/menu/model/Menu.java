package com.xtsoft.kernel.menu.model;

import java.io.Serializable;
import java.util.List;

import com.xtsoft.dao.base.model.BaseModel;
import com.xtsoft.kernel.util.StringPool;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class Menu extends BaseModel<Menu> {
	private long id;
	private long parent;
	private String iconCls;
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	private String targeturl;
	private int type;
	private int active;
	private String text;
	private int sortId;
	private boolean leaf;
	private boolean checked;
	private boolean expanded;
	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	private String description;
	private List<Menu> children;

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public Object clone() {
		Menu clone = new Menu();
		clone.setActive(getActive());
		clone.setId(getId());
		clone.setText(getText());
		clone.setParent(getParent());
		clone.setTargeturl(getTargeturl());
		clone.setType(getType());
		clone.setSortId(getSortId());
		clone.setDescription(getDescription());
		clone.setChildren(getChildren());
		return clone;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	

	public String getTargeturl() {
		if (this.targeturl == null) {
			return StringPool.BLANK;
		} else {
			return this.targeturl;
		}
	}

	public void setTargeturl(String targeturl) {
		this.targeturl = targeturl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getText() {
		if (this.text == null) {
			return StringPool.BLANK;
		} else {
			return this.text;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
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