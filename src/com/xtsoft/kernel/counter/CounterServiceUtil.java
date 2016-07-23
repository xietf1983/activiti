package com.xtsoft.kernel.counter;

import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.counter.service.CounterService;

public class CounterServiceUtil {
	private static CounterService _service;

	public static CounterService getService() {
		if (_service == null) {
			throw new RuntimeException("_service is not set");
		}

		return _service;
	}

	public void setService(CounterService service) {
		_service = service;
	}

	public static long increment() throws SystemException {
		return getService().increment();
	}

	public static long increment(String name) throws SystemException {
		return getService().increment(name);
	}

	public static long increment(String name, int size) throws SystemException {
		return getService().increment(name, size);
	}
}
