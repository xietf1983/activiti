package com.xtsoft.kernel.group.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.kernel.group.persistence.GroupPersistence;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class GroupService {
	private GroupPersistence persistence;

	public GroupPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(GroupPersistence persistence) {
		this.persistence = persistence;
	}

	private static Log _log = LogFactory.getLog(GroupService.class);

}
