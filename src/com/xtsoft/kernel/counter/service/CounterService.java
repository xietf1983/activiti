package com.xtsoft.kernel.counter.service;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.counter.persistence.CounterPersistence;

public class CounterService {
	private CounterPersistence _persistence;

	public CounterPersistence getPersistent() {
		if (_persistence == null) {
			throw new RuntimeException("CounterPersistence is not set");
		}

		return _persistence;
	}

	public void setPersistence(CounterPersistence persistence) {
		_persistence = persistence;
	}

	public long increment() throws SystemException {
		return getPersistent().increment();
	}

	public long increment(String name) throws SystemException {
		return getPersistent().increment(name);
	}

	public long increment(String name, int size) throws SystemException {
		return getPersistent().increment(name, size);
	}

}
