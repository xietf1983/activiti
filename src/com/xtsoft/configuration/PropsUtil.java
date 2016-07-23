package com.xtsoft.configuration;

import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropsUtil {

	public static boolean contains(String key) {
		return _instance._contains(key);
	}

	public static String get(String key) {
		return _instance._get(key);
	}

	public static String[] getArray(String key) {
		return _instance._getArray(key);
	}

	public static void set(String key, String value) {
		_instance._set(key, value);
	}

	private PropsUtil() {
		try {
			factory = new ConfigurationFactory();
			URL url = PropsUtil.class.getResource("/app-config.xml");
			factory.setConfigurationURL(url);
			_configuration = factory.getConfiguration();
		} catch (Exception e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to initialize PropsUtil", e);
			}
		}
	}

	private boolean _contains(String key) {
		return _getConfiguration().containsKey(key);
	}

	private String _get(String key) {
		return _getConfiguration().getString(key);
	}

	private String[] _getArray(String key) {
		return _getConfiguration().getStringArray(key);
	}

	private Configuration _getConfiguration() {
		return _configuration;
	}

	private void _set(String key, String value) {
		_getConfiguration().setProperty(key, value);
	}

	private static Log _log = LogFactory.getLog(PropsUtil.class);

	private static PropsUtil _instance = new PropsUtil();

	private ConfigurationFactory factory;
	private Configuration _configuration;

}