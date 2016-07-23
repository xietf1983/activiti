package com.xtsoft.workflow.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.workflow.model.ActivitiConfig;
import com.xtsoft.workflow.model.Workflow;

public class WorkflowPersistence extends BasePersistence<Workflow> {

	public List<Map> findDefKeyList(Map para) {
		return selectList(SqlMapUtil.WORKFLOW_DEF_KEYLIST, para);

	}

	/**
	 * 判断是否有此角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	public boolean containsUserActiviti(long userId, long configId) throws SystemException {
		HashMap para = new HashMap();
		para.put("USERID", userId);
		para.put("CONFIGID", configId);
		Long count = (Long) selectOne(SqlMapUtil.USERS_ACTIVITI_FETCHBYPRIMARYKEY, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public void addUserActiviti(long userId, long configId) throws SystemException {
		if (!containsUserActiviti(userId, configId)) {
			HashMap para = new HashMap();
			para.put("USERID", userId);
			para.put("CONFIGID", configId);
			getSqlSession().insert(SqlMapUtil.USERS_ACTIVITI_INSERT, para);
		}
	}

	public void removeConfigUserList(long userId, long configId) throws SystemException {
		HashMap para = new HashMap();
		if (userId > 0) {
			para.put("USERID", userId);
		}
		if (configId > 0) {
			para.put("CONFIGID", configId);
		}
		getSqlSession().delete(SqlMapUtil.USERS_ACTIVITI_DELETE, para);
	}

	public ActivitiConfig createActivitiConfig(long id) {
		ActivitiConfig config = new ActivitiConfig();
		config.setId(id);
		return config;
	}

	public ActivitiConfig addActivitiConfig(ActivitiConfig config) throws SystemException {
		if (!containsrActivitiConfig(config.getProckey(), config.getActivitiId())) {
			getSqlSession().insert(SqlMapUtil.ACTIVITICONFIG_INSERT, config);
		}
		return config;
	}

	public boolean containsrActivitiConfig(String prockey, String activitiId) throws SystemException {
		HashMap para = new HashMap();
		para.put("ACTIVITIID", activitiId);
		para.put("PROCKEY", prockey);
		Long count = (Long) selectOne(SqlMapUtil.ACTIVITICONFIG_COUNT_FETCHBYMAP, para);
		if (count > 0) {
			return true;
		}
		return false;
	}

	public List<Map> findActivitiList(Map para) throws SystemException {
		return selectList(SqlMapUtil.WORKFLOW_DEF_KEYLIST, para);
	}

	/**
	 * 节点名称
	 * 
	 * @param para
	 * @return
	 */
	public List<Map> findActivitiConfigList(Map para) throws SystemException {
		try {
			return selectList(SqlMapUtil.ACTIVITICONFIG_LIST_FETCHBYMAP, para);
		} catch (Exception ex) {
			int a=0;
		}
		return null;

	}

	public List<Map> findActivitiConfigUsersList(HashMap para, int start, int limit) throws SystemException {
		if (start == 0 && limit == 0) {
			return selectList(SqlMapUtil.USER_LIST_FETCHBYCONFIGID, para);
		} else {
			return selectPageList(SqlMapUtil.USER_LIST_FETCHBYCONFIGID, para, start, limit);
		}
	}

	public long findActivitiConfigUsersCount(HashMap para) throws SystemException {
		return (Long) selectOne(SqlMapUtil.USER_COUNT_FETCHBYCONFIGID, para);
	}

	/**
	 * 获取下个节点的审批人
	 * 
	 * @param taskId
	 * @return
	 */
	public List<Map> findNextConfigUsers(HashMap para) {
		return selectList(SqlMapUtil.USER_LIST_NEXTUSERBYTASKID, para);
	}

}
