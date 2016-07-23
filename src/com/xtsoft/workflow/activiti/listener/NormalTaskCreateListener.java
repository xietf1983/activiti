package com.xtsoft.workflow.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class NormalTaskCreateListener implements TaskListener {

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

	}

}
