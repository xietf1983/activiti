package com.xtsoft.workflow.activiti.listener.reimburse;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;

import com.xtsoft.Constants;
import com.xtsoft.reimburse.ReimburseServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.workflow.WorkflowEngineUtil;

public class LastTaskCreateListener implements TaskListener {

	public void notify(DelegateTask delegateTask) {
		String nextUser = (String) delegateTask.getVariable("NEXTUSER");
		if (nextUser != null) {
			delegateTask.setAssignee(nextUser);
		}
		String taskName = (String) delegateTask.getVariable("TASKNAME");
		if (taskName != null) {
			delegateTask.setDescription(taskName + "-" + delegateTask.getName());
			// delegateTask.setName(taskName+"-"+delegateTask.getName());
		} else {
			delegateTask.setDescription(delegateTask.getName());
		}
		ProcessInstance processInstance = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessInstance(delegateTask.getProcessInstanceId());
		try {
			if (processInstance != null && !processInstance.getBusinessKey().equals("")) {
				Reimburse reimburse = ReimburseServiceUtil.getService().getReimburseByPrimaryKey(Long.parseLong(processInstance.getBusinessKey()));
				reimburse.setStatus(Constants.REIMBURSE_STATUS_4);
				ReimburseServiceUtil.getService().update(reimburse,null,  false);
			}
		} catch (Exception ex) {

		}

	}
}
