package com.xtsoft.workflow.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

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

	}
}
