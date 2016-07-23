package com.xtsoft.workflow.activiti.listener.reimburse;

import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;

import com.xtsoft.Constants;
import com.xtsoft.reimburse.ReimburseServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;
import com.xtsoft.reimburse.model.ReimburseDetail;
import com.xtsoft.workflow.WorkflowEngineUtil;

public class FirstTaskCreateListener implements TaskListener {

	public void notify(DelegateTask delegateTask) {
		String owner = (String) delegateTask.getVariable("TASKOWNER");
		if (owner != null) {
			delegateTask.setOwner(owner);
			delegateTask.setAssignee(owner);
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
				reimburse.setStatus(Constants.REIMBURSE_STATUS_3);
				ReimburseServiceUtil.getService().update(reimburse,null,  false);
			}
		} catch (Exception ex) {

		}
		
	}
}
