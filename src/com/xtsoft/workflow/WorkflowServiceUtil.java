package com.xtsoft.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.workflow.service.WorkflowService;

public class WorkflowServiceUtil {
	private static WorkflowService _service;

	public static WorkflowService getService() {
		return _service;
	}

	public  void setService(WorkflowService service) {
		_service = service;
	}

	private static Log _log = LogFactory.getLog(WorkflowServiceUtil.class);
}
