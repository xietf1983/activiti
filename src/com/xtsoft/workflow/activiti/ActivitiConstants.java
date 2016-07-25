package com.xtsoft.workflow.activiti;

public interface ActivitiConstants {
	public static final String ENGINE_ID = "activiti";
	public static final String NODE_NAME = "name";
	public static final String NODE_DESCRIPTION = "documentation";
	public static final String NODE_TYPE = "type";

	public static final String PROP_START_TASK_END_DATE = "_startTaskCompleted";
	public static final String START_TASK_PREFIX = "start";

	public static final String DEFAULT_TRANSITION_NAME = "Next";
	public static final String DEFAULT_TRANSITION_DESCRIPTION = "Default Transition";

	public static final String USER_TASK_NODE_TYPE = "userTask";
	public static final String PROP_TASK_FORM_KEY = "taskFormKey";
	public static final String PROP_POOLED_ACTORS_HISTORY = "pooledActorsHistory";
	public static final String DELETE_REASON_DELETED = "deleted";
	public static final String DELETE_REASON_CANCELLED = "cancelled";

	public static final String SERVICE_REGISTRY_BEAN_KEY = "services";

	public static final String PROCESS_INSTANCE_IMAGE_FORMAT = "png";

	public static final String VAR_TENANT_DOMAIN = "_tenant_domain";

	public static final String PROP_INITIATOR = "initiator";
	public static final String PROP_INITIATOR_HOME = "initiatorhome";
	public static final String PROP_COMPANY_HOME = "companyhome";
	public static final String PROP_WORKFLOW_INSTANCE_ID = "workflowinstanceid";
	public static final String PROP_CANCELLED = "cancelled";

	public static final String TASK_STATUS_COMPLETED = "Completed";
	public static final String TASK_STATUS_IN_PROGRESS = "In Progress";

}
