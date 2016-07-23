package com.xtsoft.workflow.activiti.listener.reimburse;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

import com.xtsoft.Constants;
import com.xtsoft.reimburse.ReimburseServiceUtil;
import com.xtsoft.reimburse.model.Reimburse;

public class ReimburseEndListener implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		String busKey = execution.getProcessBusinessKey();
		if(busKey!= null && !busKey.equals("")){
			Reimburse reimburse=ReimburseServiceUtil.getService().getReimburseByPrimaryKey(Long.parseLong(busKey));
			reimburse.setStatus(Constants.REIMBURSE_STATUS_5);
			ReimburseServiceUtil.getService().update(reimburse,null,  false);
		}
	}

}
