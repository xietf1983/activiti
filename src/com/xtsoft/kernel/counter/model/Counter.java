package com.xtsoft.kernel.counter.model;

import java.io.Serializable;

import com.xtsoft.dao.base.model.BaseModel;


/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class Counter extends BaseModel<Counter> {
	private String name;
	private long currentId;

	public Counter() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}

	public Object clone() {
		Counter counterImpl = new Counter();

		counterImpl.setName(getName());

		counterImpl.setCurrentId(getCurrentId());

		return counterImpl;
	}

}