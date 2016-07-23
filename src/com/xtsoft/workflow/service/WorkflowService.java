package com.xtsoft.workflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.exception.SystemException;
import com.xtsoft.workflow.model.ActivitiConfig;
import com.xtsoft.workflow.persistence.WorkflowPersistence;

public class WorkflowService {

	private WorkflowPersistence persistence;

	public WorkflowPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(WorkflowPersistence persistence) {
		this.persistence = persistence;
	}

	public List<Map> findDefKeyList(Map para) {
		return getPersistence().findDefKeyList(para);
	}

	public ActivitiConfig createActivitiConfig(long id) {
		return getPersistence().createActivitiConfig(id);
	}

	public ActivitiConfig addActivitiConfig(ActivitiConfig config) throws SystemException {
		return getPersistence().addActivitiConfig(config);
	}

	public List<Map> findActivitiConfigList(Map para) throws SystemException {
		return getPersistence().findActivitiConfigList(para);

	}

	/*
	 * 节点对应的人员
	 */
	public List<Map> findActivitiConfigUsersList(HashMap map, int start, int limit) throws SystemException {
		return getPersistence().findActivitiConfigUsersList(map, start, limit);

	}

	public long findActivitiConfigUsersCount(HashMap map) throws SystemException {
		return getPersistence().findActivitiConfigUsersCount(map);

	}

	public void editConfigUserList(long configId, String[] userIds) throws SystemException {
		if (userIds != null) {
			for (String userId : userIds) {
				if (userId != null && !userId.equals("")) {
					getPersistence().addUserActiviti(Long.parseLong(userId), configId);
				}
			}
		}
	}

	public void removeConfigUserList(long configId, String[] userIds) throws SystemException {
		if (userIds != null) {
			for (String s : userIds) {
				if (s != null && !s.equals("")) {
					getPersistence().removeConfigUserList(Long.parseLong(s), configId);
				}
			}
		}
	}

	public List<Map> findNextConfigUsers(HashMap para) throws SystemException {
		return getPersistence().findNextConfigUsers(para);
	}

}
