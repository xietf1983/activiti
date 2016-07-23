package com.xtsoft.kernel.counter.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.counter.model.Counter;
import com.xtsoft.kernel.counter.model.CounterRegister;



/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class CounterPersistence extends BasePersistence<Counter> {
	public long increment() throws SystemException {
		return increment(_NAME);
	}

	public long increment(String name) throws SystemException {
		return increment(name, _MINIMUM_INCREMENT_SIZE);
	}

	public long increment(String name, int size) throws SystemException {
		if (size < _MINIMUM_INCREMENT_SIZE) {
			size = _MINIMUM_INCREMENT_SIZE;
		}
		CounterRegister register = getCounterRegister(name);
		synchronized (register) {
			long newValue = register.getCurrentValue() + size;
			if (newValue > register.getRangeMax()) {

				Counter model = new Counter();
				model.setName(name);
				model.setCurrentId(newValue + register.getRangeSize());
				update(SqlMapUtil.COUNTER_UPDATE, model);
				long rangeMax = newValue + register.getRangeSize();
				register.setRangeMax(rangeMax);
				register.setCurrentValue(newValue);

			} else {
				register.setCurrentValue(newValue);
			}
			return newValue;
		}
	}

	protected CounterRegister getCounterRegister(String name) throws SystemException {
		CounterRegister register = _registerLookup.get(name);
		if (register == null) {
			register = createCounterRegister(name);
			_registerLookup.put(name, register);
		}
		return register;
	}

	protected CounterRegister createCounterRegister(String name) throws SystemException {

		return createCounterRegister(name, _COUNTER_INCREMENT);
	}

	protected CounterRegister createCounterRegister(String name, long size) throws SystemException {
		long rangeMin = 0;
		long rangeMax = 0;
		Counter model = (Counter) selectOne(SqlMapUtil.COUNTER_FETCHBYPRIMARYKEY, name);
		if (model == null) {
			rangeMin = _DEFAULT_CURRENT_ID;
			model = new Counter();
			model.setNew(true);
			model.setName(name);
			rangeMax = rangeMin + size;
			model.setCurrentId(rangeMax);
			insert(SqlMapUtil.COUNTER_INSERT, model);
		} else {
			model.setName(name);
			rangeMin = model.getCurrentId();
			rangeMax = rangeMin + size;
			model.setCurrentId(rangeMax);
			update(SqlMapUtil.COUNTER_UPDATE, model);
		}

		CounterRegister register = new CounterRegister(name, rangeMin, rangeMax, _COUNTER_INCREMENT);
		return register;
	}

	private static final int _DEFAULT_CURRENT_ID = 10000;
	private static final int _MINIMUM_INCREMENT_SIZE = 1;
	private static final int _COUNTER_INCREMENT = 500;
	private Map<String, CounterRegister> _registerLookup = new ConcurrentHashMap<String, CounterRegister>();
	private static final String _NAME = Counter.class.getName();
	private static Log _log = LogFactory.getLog(CounterPersistence.class);

}
