package com.xtsoft.kernel.counter.model;

/****
 * 
 * @author x61
 * 
 */
public class CounterRegister {

	public CounterRegister(String name, long rangeMin, long rangeMax,
			int rangeSize) {

		_name = name;
		_currentValue = rangeMin;
		_rangeMax = rangeMax;
		_rangeSize = rangeSize;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public long getRangeMax() {
		return _rangeMax;
	}

	public void setRangeMax(long rangeMax) {
		_rangeMax = rangeMax;
	}

	public int getRangeSize() {
		return _rangeSize;
	}

	public void setRangeSize(int rangeSize) {
		_rangeSize = rangeSize;
	}

	public long getCurrentValue() {
		return _currentValue;
	}

	public void setCurrentValue(long currentValue) {
		_currentValue = currentValue;
	}

	private String _name;
	private long _currentValue;
	private long _rangeMax;
	private int _rangeSize;

}