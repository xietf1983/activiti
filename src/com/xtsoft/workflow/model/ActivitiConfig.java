package com.xtsoft.workflow.model;

public class ActivitiConfig {
	private String activitiId;
	private long id;
	private String name;
	private String prockey;
	public String getProckey() {
		return prockey;
	}

	public void setProckey(String prockey) {
		this.prockey = prockey;
	}

	private long rowIndex;

	public long getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(long rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getActivitiId() {
		return activitiId;
	}

	public void setActivitiId(String activitiId) {
		this.activitiId = activitiId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
