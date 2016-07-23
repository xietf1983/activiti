package com.xtsoft.workflow.activiti;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.xtsoft.Constants;
import com.xtsoft.kernel.user.UserServiceUtil;
import com.xtsoft.kernel.util.StreamUtil;
import com.xtsoft.util.DateUtil;

public class ActivitiWorkflowEngine {
	private static Logger iLog = Logger.getLogger(ActivitiWorkflowEngine.class);
	private static final String ERR_DEPLOY_WORKFLOW = "activiti.engine.deploy.workflow.error";
	private static final String ERR_IS_WORKFLOW_DEPLOYED = "activiti.engine.is.workflow.deployed.error";
	private static final String ERR_UNDEPLOY_WORKFLOW = "activiti.engine.undeploy.workflow.error";
	private static final String ERR_UNDEPLOY_WORKFLOW_UNEXISTING = "activiti.engine.undeploy.workflow.unexisting.error";
	private static final String ERR_GET_WORKFLOW_DEF = "activiti.engine.get.workflow.definition.error";
	private static final String ERR_GET_WORKFLOW_DEF_BY_ID = "activiti.engine.get.workflow.definition.by.id.error";
	private static final String ERR_GET_WORKFLOW_DEF_BY_NAME = "activiti.engine.get.workflow.definition.by.name.error";
	private static final String ERR_GET_ALL_DEFS_BY_NAME = "activiti.engine.get.all.workflow.definitions.by.name.error";
	private static final String ERR_GET_DEF_IMAGE = "activiti.engine.get.workflow.definition.image.error";
	private static final String ERR_GET_DEF_UNEXISTING_IMAGE = "activiti.engine.get.workflow.definition.unexisting.image.error";
	private static final String ERR_START_WORKFLOW = "activiti.engine.start.workflow.error";
	private static final String ERR_GET_WORKFLOW_INSTS = "activiti.engine.get.workflows.error";
	private static final String ERR_GET_ACTIVE_WORKFLOW_INSTS = "activiti.engine.get.active.workflows.error";
	private static final String ERR_GET_COMPLETED_WORKFLOW_INSTS = "activiti.engine.get.completed.workflows.error";
	private static final String ERR_GET_WORKFLOW_PATHS = "activiti.engine.get.workflow.paths.error";
	private static final String ERR_CANCEL_WORKFLOW = "activiti.engine.cancel.workflow.error";
	private static final String ERR_CANCEL_UNEXISTING_WORKFLOW = "activiti.engine.cancel.unexisting.workflow.error";
	private static final String ERR_DELETE_WORKFLOW = "activiti.engine.delete.workflow.error";
	private static final String ERR_DELETE_UNEXISTING_WORKFLOW = "activiti.engine.delete.unexisting.workflow.error";
	protected static final String ERR_FIRE_EVENT_NOT_SUPPORTED = "activiti.engine.event.unsupported";
	private static final String ERR_GET_TASKS_FOR_PATH = "activiti.engine.get.tasks.for.path.error";
	private static final String ERR_GET_TIMERS = "activiti.engine.get.timers.error";
	protected static final String ERR_FIND_COMPLETED_TASK_INSTS = "activiti.engine.find.completed.task.instances.error";
	private static final String ERR_GET_WORKFLOW_TOKEN_INVALID = "activiti.engine.get.workflow.token.invalid";
	private static final String ERR_GET_WORKFLOW_TOKEN_NULL = "activiti.engine.get.workflow.token.is.null";
	private static final String ERR_GET_ASSIGNED_TASKS = "activiti.engine.get.assigned.tasks.error";
	private static final String ERR_GET_POOLED_TASKS = "activiti.engine.get.pooled.tasks.error";
	private static final String ERR_UPDATE_TASK = "activiti.engine.update.task.error";
	private static final String ERR_UPDATE_TASK_UNEXISTING = "activiti.engine.update.task.unexisting.error";
	private static final String ERR_UPDATE_START_TASK = "activiti.engine.update.starttask.illegal.error";
	private static final String ERR_END_UNEXISTING_TASK = "activiti.engine.end.task.unexisting.error";
	private static final String ERR_GET_TASK_BY_ID = "activiti.engine.get.task.by.id.error";
	private static final String ERR_END_TASK_INVALID_TRANSITION = "activiti.engine.end.task.invalid.transition";
	private RepositoryService repoService;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private HistoryService historyService;
	private ManagementService managementService;
	private FormService formService;
	private IdentityService identityService;

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public RepositoryService getRepoService() {
		return repoService;
	}

	public void setRepoService(RepositoryService repoService) {
		this.repoService = repoService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public ManagementService getManagementService() {
		return managementService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	public FormService getFormService() {
		return formService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	/**
	 * 流程定义发布
	 * 
	 * @param workflowDefinition
	 * @param mimetype
	 * @param name
	 * @return
	 */
	public Deployment deployDefinition(InputStream workflowDefinition, String mimetype, String name) {
		Deployment deployment = null;
		try {
			deployment = getRepoService().createDeployment().addZipInputStream(new ZipInputStream((workflowDefinition))).deploy();
		} catch (Exception ae) {
			int a = 0;
		}
		return deployment;
	}

	public Deployment getDeploymentById(String deploymentId) {
		try {
			return getRepoService().createDeploymentQuery().deploymentId(deploymentId).singleResult();
		} catch (Exception ae) {

		}
		return null;
	}
	
	public ProcessInstance getProcessInstance(String processInstanceId) {
		ProcessInstance processInstance = null;
		try {
			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			return processInstance;
		} catch (ActivitiException ae) {
			iLog.error(ae.toString());
		}
		return null;
	}
	
	public ProcessInstance cancelWorkflow(String processInstanceId) {
		ProcessInstance processInstance = null;
		try {
			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if (processInstance == null) {
				return null;
			}
			runtimeService.deleteProcessInstance(processInstance.getId(), ActivitiConstants.PROP_CANCELLED);
			HistoricProcessInstance deletedInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
			historyService.deleteHistoricProcessInstance(deletedInstance.getId());
			return processInstance;
		} catch (ActivitiException ae) {
			iLog.error(ae.toString());
		}
		return null;
	}


	/**
	 * 查询流程定义
	 * 
	 * @param processDefinitionId
	 * @param workflowName
	 * @return
	 */
	public List<ProcessDefinition> getProcessDefinition(String processDefinitionId, String workflowName) {
		List<ProcessDefinition> processDefinitionList = null;
		ProcessDefinitionQuery query = getRepoService().createProcessDefinitionQuery();
		if (processDefinitionId != null) {
			query.processDefinitionId(processDefinitionId);
		}
		if (workflowName != null) {
			query.processDefinitionNameLike(workflowName);
		}
		query.orderByProcessDefinitionName().desc();
		processDefinitionList = query.list();
		return processDefinitionList;
	}

	public ProcessDefinition getProcessDefinitionSingleResultBykey(String processDefinitionKey) {
		ProcessDefinitionQuery query = getRepoService().createProcessDefinitionQuery();
		query.processDefinitionKey(processDefinitionKey);
		query.orderByProcessDefinitionVersion().desc();
		List<ProcessDefinition> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public ProcessDefinition getProcessDefinitionSingleResult(String processDefinitionId) {
		ProcessDefinitionQuery query = getRepoService().createProcessDefinitionQuery();
		query.processDefinitionId(processDefinitionId);
		return query.singleResult();
	}

	public byte[] getDefinitionImage(String processDefinitionId) {
		try {
			ProcessDefinition processDefinition = getRepoService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			if (processDefinition == null) {
				return null;
			}
			String diagramResourceName = ((ReadOnlyProcessDefinition) processDefinition).getDiagramResourceName();
			if (diagramResourceName != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream resourceInputStream = getRepoService().getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
				// Write the resource to a ByteArrayOutpurStream
				IOUtils.copy(resourceInputStream, out);
				return out.toByteArray();
			}
			// No image was found for the process definition
			return null;
		} catch (IOException ioe) {
			iLog.error(ioe.toString());
			return null;
		}
	}

	/**
	 * 根据流程定义查找流程实例
	 * 
	 * @param definitionId
	 * @param startedAfter
	 * @param finishedBefore
	 * @param isActive
	 * @param maxItems
	 * @param skipCount
	 * @return
	 */
	public List<HistoricProcessInstance> getWorkflowList(String definitionId, Date startedAfter, Date finishedBefore, Boolean isActive, int maxItems, int skipCount) {
		HistoricProcessInstanceQuery query = createQuery(definitionId, startedAfter, finishedBefore, isActive);
		List<HistoricProcessInstance> completedInstances;
		if (maxItems > 0) {
			completedInstances = query.orderByProcessInstanceDuration().desc().listPage(skipCount, maxItems);
		} else {
			completedInstances = query.list();
		}
		return completedInstances;
	}

	/**
	 * 根据流程定义查找流程实例数量
	 * 
	 * @param definitionId
	 * @param startedAfter
	 * @param finishedBefore
	 * @param isActive
	 * @return
	 */
	public long getWorkflowCount(String definitionId, Date startedAfter, Date finishedBefore, Boolean isActive) {
		HistoricProcessInstanceQuery query = createQuery(definitionId, startedAfter, finishedBefore, isActive);
		return query.count();
	}

	private HistoricProcessInstanceQuery createQuery(String definitionId, Date startedAfter, Date finishedBefore, Boolean isActive) {
		HistoricProcessInstanceQuery query;
		if (isActive != null) {
			if (isActive) {
				query = getHistoryService().createHistoricProcessInstanceQuery().unfinished();
			} else {
				query = getHistoryService().createHistoricProcessInstanceQuery().finished();
			}
		} else {
			query = getHistoryService().createHistoricProcessInstanceQuery();
		}
		if (definitionId != null) {
			query = query.processDefinitionId(definitionId);
		}
		if (startedAfter != null) {
			query.startedAfter(startedAfter);
		}
		if (finishedBefore != null) {
			query.finishedBefore(finishedBefore);
		}
		return query;
	}

	public HistoricProcessInstance getProcessInstanceById(String processInstanceId) {
		HistoricProcessInstance historicInstance = null;
		try {
			historicInstance = getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		} catch (ActivitiException ae) {

		}
		return historicInstance;
	}

	public List<Task> getRunTimeTask(String authority, int maxItems, int skipCount) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		if (authority != null) {
			taskQuery.taskAssignee(authority);
		}
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(authority).orderByTaskCreateTime().desc().listPage(skipCount, maxItems);
		if (maxItems > 0) {
			return taskQuery.listPage(skipCount, maxItems);
		}
		return taskQuery.list();
	}

	public long getRunTimeTaskCount(String authority) {
		TaskQuery taskQuery = taskService.createTaskQuery();

		if (authority != null) {
			taskQuery.taskAssignee(authority);
		}

		return taskQuery.count();
	}

	/**
	 * 获取我的任务
	 * 
	 * @param authority
	 * @param finished
	 * @param maxItems
	 * @param skipCount
	 * @return
	 */
	public List<HistoricTaskInstance> getAssignedTasks(String authority, boolean finished, Date startDate, Date endDate, String processDefinitionKey, int maxItems, int skipCount) {
		List<HistoricTaskInstance> historicTasks = null;
		HistoricTaskInstanceQuery taskQuery;
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(authority).orderByTaskCreateTime().desc().listPage(skipCount, maxItems);
		taskQuery = historyService.createHistoricTaskInstanceQuery();
		if (!finished) {
			taskQuery.unfinished();

		} else {
			taskQuery.finished();
		}
		if (authority != null) {
			taskQuery.taskAssignee(authority);
		}
		if (processDefinitionKey != null && !processDefinitionKey.equals("")) {
			taskQuery.processDefinitionKey(processDefinitionKey);
		}
		if (startDate != null) {
			taskQuery.taskCompletedAfter(startDate);
		}
		if (endDate != null) {
			taskQuery.taskCompletedBefore(endDate);
		}
		taskQuery.orderByHistoricTaskInstanceEndTime().desc();
		if (maxItems > 0) {
			return taskQuery.listPage(skipCount, maxItems);
		}
		return taskQuery.list();
	}

	public long getAssignedTasksCount(String authority, boolean finished, Date startDate, Date endDate, String processDefinitionKey) {
		List<HistoricTaskInstance> historicTasks = null;
		HistoricTaskInstanceQuery taskQuery;
		taskQuery = historyService.createHistoricTaskInstanceQuery();
		if (!finished) {
			taskQuery.unfinished();

		} else {
			taskQuery.finished();
		}
		if (processDefinitionKey != null && !processDefinitionKey.equals("")) {
			taskQuery.processDefinitionKey(processDefinitionKey);
		}
		if (startDate != null) {
			taskQuery.taskCompletedAfter(startDate);
		}
		if (endDate != null) {
			taskQuery.taskCompletedBefore(endDate);
		}
		if (authority != null) {
			taskQuery.taskAssignee(authority);
		}

		return taskQuery.count();
	}

	public HistoricTaskInstance endTask(String taskId, String assignee,Map<String, Object> variables, String processInstanceId, String message, String result) {

		return endNormalTask(taskId, assignee, variables, processInstanceId, message, result);
	}

	private HistoricTaskInstance endNormalTask(String taskId, String assignee, Map<String, Object> variables, String processInstanceId, String message, String result) {
		// Retrieve task
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

		if (task == null) {
			return null;
		}
		if (message != null && !message.equals("")) {
			taskService.addComment(taskId, processInstanceId, message);
		}
		if (result != null && !result.equals("")) {
			taskService.addComment(taskId, processInstanceId, Constants.AUDITRESULT, result);
		}
		taskService.setAssignee(taskId, assignee);
		task.setAssignee(assignee);
		taskService.complete(taskId, variables);

		// The task should have a historicTaskInstance
		HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
		return historicTask;
	}

	public boolean hasWorkflowImage(String processInstanceId) {
		boolean hasImage = false;

		ExecutionEntity pi = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

		// If the process is finished, there is no diagram available
		if (pi != null) {

			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repoService).getDeployedProcessDefinition(pi.getProcessDefinitionId());

			hasImage = (processDefinition != null && processDefinition.isGraphicalNotationDefined());
		}

		return hasImage;
	}

	public byte[] getWorkflowImage(String processInstanceId) {
		InputStream inputStream = null;
		try {
			ExecutionEntity pi = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			if (pi != null) {
				// Fetch the bpmn model
				BpmnModel model = repoService.getBpmnModel(pi.getProcessDefinitionId());

				if (model != null && model.getLocationMap().size() > 0) {
					ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
					inputStream = generator.generateDiagram(model, ActivitiConstants.PROCESS_INSTANCE_IMAGE_FORMAT, runtimeService.getActiveActivityIds(processInstanceId), Collections.<String> emptyList(), "宋体", "宋体", null, 1.0);

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					IOUtils.copy(inputStream, out);
					StreamUtil.cleanUp(inputStream, out);
					return out.toByteArray();
				}
			} else {
				HistoricProcessInstance historicInstance = null;
				try {
					historicInstance = getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
					BpmnModel model = repoService.getBpmnModel(historicInstance.getProcessDefinitionId());

					if (model != null && model.getLocationMap().size() > 0) {
						ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
						inputStream = generator.generateDiagram(model, ActivitiConstants.PROCESS_INSTANCE_IMAGE_FORMAT, Collections.<String> emptyList(), Collections.<String> emptyList(), "宋体", "宋体", null, 1.0);

						ByteArrayOutputStream out = new ByteArrayOutputStream();
						IOUtils.copy(inputStream, out);
						StreamUtil.cleanUp(inputStream, out);
						return out.toByteArray();
					}
				} catch (ActivitiException ae) {

				}

			}
		} catch (Exception ae) {

		}
		return null;
	}

	public ProcessInstance startWorkflowFirstTaskEnd(String processDefinitionKey, String businessKey, Map<String, Object> variables, String creator, String taskName) {
		ProcessInstance instance = null;
		try {
			identityService.setAuthenticatedUserId(creator);
			if (variables == null) {
				variables = new HashMap();
			}
			if (creator != null) {
				variables.put("TASKOWNER", creator);
			}
			if (taskName != null) {
				variables.put("TASKNAME", taskName);
			}
			instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
			if (instance != null) {
				List<Task> taklist = getTaskService().createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
				if (taklist != null) {
					for (Task t : taklist) {
						endNormalTask(t.getId(), creator, variables, instance.getProcessInstanceId(), null, null);
					}
				}
			}
			identityService.setAuthenticatedUserId(null);
		} catch (Exception ae) {
			int a = 0;
		}
		return instance;
	}

	public ProcessInstance startWorkflow(String processDefinitionKey, String businessKey, Map<String, Object> variables, String creator, String taskName) {
		ProcessInstance instance = null;
		try {
			identityService.setAuthenticatedUserId(creator);
			if (variables == null) {
				variables = new HashMap();
			}
			if (creator != null) {
				variables.put("TASKOWNER", creator);
			}
			if (taskName != null) {
				variables.put("TASKNAME", taskName);
			}
			instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
			identityService.setAuthenticatedUserId(null);
		} catch (Exception ae) {
			int a = 0;
		}
		return instance;
	}

	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();// 执行实例
		Object property = PropertyUtils.getProperty(execution, "activityId");
		String activityId = "";
		if (property != null) {
			activityId = property.toString();
		}
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repoService).getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		List<ActivityImpl> activitiList = processDefinition.getActivities();// 获得当前任务的所有节点

		List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
		for (ActivityImpl activity : activitiList) {

			boolean currentActiviti = false;
			String id = activity.getId();

			// 当前节点
			if (id.equals(activityId)) {
				currentActiviti = true;
			}

			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

			activityInfos.add(activityImageInfo);
		}

		return activityInfos;
	}

	/**
	 * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
	 * 
	 * @param activity
	 * @param processInstance
	 * @param currentActiviti
	 * @return
	 */
	private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance, boolean currentActiviti) throws Exception {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		setPosition(activity, activityInfo);
		setWidthAndHeight(activity, activityInfo);
		Map<String, Object> properties = activity.getProperties();
		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		if (activityBehavior instanceof UserTaskActivityBehavior) {

			Task currentTask = null;

			/*
			 * 当前节点的task
			 */
			if (currentActiviti) {
				currentTask = getCurrentTaskInfo(processInstance);
			}

			/*
			 * 当前任务的分配角色
			 */
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if (!candidateGroupIdExpressions.isEmpty()) {

				// 任务的处理角色
				setTaskGroup(vars, candidateGroupIdExpressions);

				// 当前处理人
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}
		vars.put("节点说明", properties.get("documentation"));
		String description = activity.getProcessDefinition().getDescription();
		vars.put("描述", description);
		activityInfo.put("vars", vars);
		return activityInfo;
	}

	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = "";
		for (Expression expression : candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}

	/**
	 * 设置当前处理人信息
	 * 
	 * @param vars
	 * @param currentTask
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		String assignee = currentTask.getAssignee();
		if (assignee != null) {
			User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			vars.put("当前处理人", userInfo);
		}
	}

	/**
	 * 获取当前节点信息
	 * 
	 * @param processInstance
	 * @return
	 */
	private Task getCurrentTaskInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");

			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId).singleResult();

		} catch (Exception e) {

		}
		return currentTask;
	}

	/**
	 * 设置宽度、高度属性
	 * 
	 * @param activity
	 * @param activityInfo
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}

	/**
	 * 设置坐标位置
	 * 
	 * @param activity
	 * @param activityInfo
	 */
	private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}

	public HistoricTaskInstance getHistoricTaskInstance(String taskId) {
		return getHistoryService().createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
	}

	public List<Map> getProcessComment(String processInstanceId) {
		List<Map> ret = new ArrayList();
		List<HistoricActivityInstance> list = getHistoryService().createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).activityType("userTask").finished().orderByHistoricActivityInstanceStartTime().asc().list();
		if (list != null) {
			for (HistoricActivityInstance p : list) {
				Map hp = new HashMap();
				hp.put("ACTIVITINAME", p.getActivityName());
				hp.put("TASKID", p.getTaskId());
				hp.put("PROCESSDEFINITIONID", p.getProcessDefinitionId());
				hp.put("DURATIONINMILLIS", p.getDurationInMillis());
				hp.put("PROCESSINSTANCEID", p.getProcessInstanceId());
				hp.put("COMPLETETIME", DateUtil.dateToString(p.getEndTime()));
				try {
					hp.put("USERNAME", UserServiceUtil.getService().findByPrimaryKey(Long.parseLong(p.getAssignee())).getUserName());
				} catch (Exception e) {

				}
				String historytaskId = p.getTaskId();
				List<Comment> comments = taskService.getTaskComments(historytaskId);
				List<Comment> commentsResult = taskService.getTaskComments(historytaskId, Constants.AUDITRESULT);
				comments.addAll(commentsResult);
				for (Comment c : comments) {
					if (c.getType().equals(Constants.COMMENT)) {
						// 审批意见
						hp.put("COMMENT", c.getFullMessage());
					} else if (c.getType().equals(Constants.AUDITRESULT)) {
						// 审批结果
						if (c.getFullMessage() != null) {
							if (c.getFullMessage().equals("1")) {
								hp.put("AUDITRESULT", "同意");
							} else if (c.getFullMessage().equals("0")) {
								hp.put("AUDITRESULT", "不同意");
							}
						}
					}
				}

				ret.add(hp);

			}
		}
		return ret;
	}
}
