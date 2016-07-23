package com.xtsoft.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import com.xtsoft.util.DateUtil;
import com.xtsoft.workflow.activiti.ActivitiWorkflowEngine;

public class WorkflowEngineUtil {
	private static ActivitiWorkflowEngine activitiWorkflowEngine;

	public static ActivitiWorkflowEngine getActivitiWorkflowEngine() {
		return activitiWorkflowEngine;
	}

	public void setActivitiWorkflowEngine(ActivitiWorkflowEngine activitiWorkflowEngine) {
		this.activitiWorkflowEngine = activitiWorkflowEngine;
	}
	public static ProcessDefinition getProcessDefinitionSingleResultBykey(String processDefinitionKey) {
		return getActivitiWorkflowEngine().getProcessDefinitionSingleResultBykey(processDefinitionKey);
	}
	public static List<Map> getProcessDefinition(String processDefinitionId, String workflowName) {
		List<Map> ret = new ArrayList();
		List<ProcessDefinition> list = getActivitiWorkflowEngine().getProcessDefinition(processDefinitionId, workflowName);
		if (list != null) {
			for (ProcessDefinition p : list) {
				Map hp = new HashMap();
				hp.put("NAME", p.getName());
				hp.put("ID", p.getId());
				hp.put("KEY", p.getKey());
				hp.put("RESOURCENAME", p.getResourceName());
				hp.put("DEPLOYMENTID", p.getDeploymentId());
				hp.put("VERSION", p.getVersion());
				hp.put("DESCRIPTION", p.getDescription());
				hp.put("TENANTID", p.getTenantId());
				hp.put("DGRMRESOURCENAME", p.getDiagramResourceName());
				Deployment d = getActivitiWorkflowEngine().getDeploymentById(p.getDeploymentId());
				if (d != null) {
					hp.put("DEPLYMENTTIME", DateUtil.toString(d.getDeploymentTime()));
				}
				ret.add(hp);

			}
		}
		return ret;
	}

	public static byte[] loadByDeployment(String processDefinitionId) throws Exception {
		return getActivitiWorkflowEngine().getDefinitionImage(processDefinitionId);
	}

	public static byte[] getWorkflowImage(String processInstanceId) throws Exception {
		return getActivitiWorkflowEngine().getWorkflowImage(processInstanceId);
	}

	public static ProcessInstance startWorkflow(String processDefinitionKey, String businessKey, Map<String, Object> variables, String creator, String taskName) {
		return getActivitiWorkflowEngine().startWorkflow(processDefinitionKey, businessKey, variables, creator, taskName);
	}

	
}
