package com.xtsoft.dao.base.model;

import java.io.Serializable;

public abstract class BaseModel<T> implements Cloneable, Serializable {

	public boolean isNew() {
		return _new;
	}

	public void setNew(boolean n) {
		_new = n;
	}

	public boolean isCachedModel() {
		return _cachedModel;
	}

	public void setCachedModel(boolean cachedModel) {
		_cachedModel = cachedModel;
	}

	public abstract Object clone();

	private boolean _new;
	private boolean _cachedModel;

}