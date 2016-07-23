package com.xtsoft.workflow.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.Struts2Utils;
import com.xtsoft.category.CategoryServiceUtil;
import com.xtsoft.category.model.Category;
import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.role.RoleServiceUtil;
import com.xtsoft.kernel.user.model.UserShort;
import com.xtsoft.util.DateUtil;
import com.xtsoft.workflow.WorkflowEngineUtil;
import com.xtsoft.workflow.WorkflowServiceUtil;
import com.xtsoft.workflow.model.ActivitiConfig;

public class WorkflowAction extends ActionSupport {
	private static Logger _log = Logger.getLogger(WorkflowAction.class);

	public String getDeploymentDef() {
		Map map = new HashMap();
		try {
			String processDefinitionId = Struts2Utils.getParameter("processDefinitionId");
			String workflowName = Struts2Utils.getParameter("workflowName");
			List<Map> list = WorkflowEngineUtil.getProcessDefinition(processDefinitionId, workflowName);
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("WorkflowAction.getDeploymentDef" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String loadByDeployment() {
		try {
			String processDefinitionId = Struts2Utils.getParameter("processDefinitionId");
			HttpServletResponse response = Struts2Utils.getResponse();
			byte[] b = WorkflowEngineUtil.loadByDeployment(processDefinitionId);
			response.getOutputStream().write(b);

		} catch (Exception e) {

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("WorkflowAction.loadByDeployment" + sw.toString());
		}
		return null;

	}

	public String loadByProcessInstance() {
		try {
			String processInstanceId = Struts2Utils.getParameter("processInstanceId");
			HttpServletResponse response = Struts2Utils.getResponse();
			byte[] b = WorkflowEngineUtil.getWorkflowImage(processInstanceId);
			response.getOutputStream().write(b);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("WorkflowAction.loadByProcessInstance" + sw.toString());
		}
		return null;

	}

	public String findDefKeyList() {
		List<Map> list = null;
		try {
			list = WorkflowServiceUtil.getService().findDefKeyList(null);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("WorkflowAction.findDefKeyList" + sw.toString());
		}
		Struts2Utils.renderDeepJson(list);
		return null;

	}

	public String findActivitiConfigList() {
		Map map = new HashMap();
		try {
			String id = Struts2Utils.getParameter("ID");
			map.put("PROCID", id);
			List<Map> list = WorkflowServiceUtil.getService().findActivitiConfigList(map);
			if (list != null && list.size() > 0) {
				for (Map p : list) {
					p.put("CONFIGID", Long.parseLong(String.valueOf(p.get("ID"))));
					List<Map> userlist = WorkflowServiceUtil.getService().findActivitiConfigUsersList((HashMap) p, 0, 0);
					if (userlist != null && userlist.size() > 0) {
						StringBuffer buffer = new StringBuffer();
						for (Map userMap : userlist) {
							if (!buffer.toString().equals("")) {
								buffer.append(",");
							}
							buffer.append(userMap.get("USERNAME"));
						}
						p.put("USERS", buffer.toString());
					}
				}
			}
			map.put("total", list.size());
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			_log.error("WorkflowAction.findActivitiConfigList" + sw.toString());
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String findActivitiConfigUsers() {
		Map map = new HashMap();
		try {
			String name = Struts2Utils.getParameter("NAME");
			HashMap hp = new HashMap();
			if (name != null && !name.equals("")) {
				hp.put("USERNAME", "%" + name + "%");
			}
			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			String configId = Struts2Utils.getParameter("CONFIGID");
			String configIdNotIn = Struts2Utils.getParameter("CONFIGIDNOTIN");
			if (configId != null && !configId.equals("")) {
				hp.put("CONFIGID", configId);
			} else if (configIdNotIn != null && !configIdNotIn.equals("")) {
				hp.put("CONFIGIDNOTIN", configIdNotIn);
			}
			List<Map> list = WorkflowServiceUtil.getService().findActivitiConfigUsersList(hp, Integer.parseInt(start), Integer.parseInt(limit));
			map.put("total", WorkflowServiceUtil.getService().findActivitiConfigUsersCount(hp));
			map.put("rows", list);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("WorkflowAction.getRoleList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String deleteDeployment() {
		Map map = new HashMap();
		try {
			String id = Struts2Utils.getParameter("ID");
			if (id != null && !id.equals("")) {
				WorkflowEngineUtil.getActivitiWorkflowEngine().getRepoService().deleteDeployment(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("WorkflowAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String editConfigUserList() {
		Map map = new HashMap();
		try {
			String configId = Struts2Utils.getParameter("CONFIGID");
			String userIds = Struts2Utils.getParameter("USERIDS");
			if (userIds == null) {
				userIds = "";
			}
			WorkflowServiceUtil.getService().editConfigUserList(Long.parseLong(configId), userIds.split(","));
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("WorkflowAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String removeConfigUserList() {
		Map map = new HashMap();
		try {
			String configId = Struts2Utils.getParameter("CONFIGID");
			String userIds = Struts2Utils.getParameter("USERIDS");
			if (userIds == null) {
				userIds = "";
			}
			WorkflowServiceUtil.getService().removeConfigUserList(Long.parseLong(configId), userIds.split(","));
			// Struts2Utils.renderDeepJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("WorkflowAction.editRoleMenuList" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String endTask() {
		String taskId = Struts2Utils.getParameter("TASKID");
		String nextUser = Struts2Utils.getParameter("ASSIGNEE");
		String processInstanceId = Struts2Utils.getParameter("PROCESSINSTANCEID");
		String message = Struts2Utils.getParameter("MESSAGE");
		String result = Struts2Utils.getParameter("RESULT");
		Map<String, Object> variables = new HashMap();
		variables.put("NEXTUSER", nextUser);
		variables.put("PASS", result);
		UserShort usershort = Struts2Utils.getCurrentUser();
		HistoricTaskInstance his = WorkflowEngineUtil.getActivitiWorkflowEngine().endTask(taskId, usershort.getUserId() + "", variables, processInstanceId, message, result);
		Struts2Utils.renderDeepJson(new HashMap());
		return null;
	}

	public String getAssignedTasks() {
		Map map = new HashMap();
		try {

			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			String startTime = Struts2Utils.getParameter("STARTTIME");
			String endTime = Struts2Utils.getParameter("ENDTIME");
			String processDefinitionKey = Struts2Utils.getParameter("DEFKEY");
			UserShort usershort = Struts2Utils.getCurrentUser();
			Date startDate = null;
			Date endDate = null;
			if (startTime != null && !startTime.equals("")) {
				startDate = DateUtil.getDateFromString(startTime);
			}
			if (endTime != null && !endTime.equals("")) {
				endDate = DateUtil.getDateFromString(endTime);
				endDate = new Date(endDate.getTime() + DateUtil.DAY);
			}
			List<HistoricTaskInstance> list = WorkflowEngineUtil.getActivitiWorkflowEngine().getAssignedTasks(usershort.getUserId() + "", true, startDate, endDate, processDefinitionKey, Integer.parseInt(limit), Integer.parseInt(start));
			List<Map> retList = new ArrayList();
			if (list != null) {
				for (HistoricTaskInstance h : list) {
					Map p = new HashMap();
					p.put("DESCRIPTION", h.getDescription());
					p.put("TASKID", h.getId());
					p.put("NAME", h.getName());
					p.put("FORMKEY", h.getFormKey());
					p.put("OWNER", h.getOwner());
					p.put("PROCESSDEFINITIONID", h.getProcessDefinitionId());
					p.put("CREATETIME", DateUtil.dateToString(h.getCreateTime()));
					p.put("ENDTIME", DateUtil.dateToString(h.getEndTime()));
					p.put("PROCESSINSTANCEID", h.getProcessInstanceId());
					p.put("EXECUTIONID", h.getExecutionId());
					p.put("TASKDEFINITIONKEY", h.getTaskDefinitionKey());
					HistoricProcessInstance hisp = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessInstanceById(h.getProcessInstanceId());
					if (hisp != null) {
						p.put("BUSINESSKEY", hisp.getBusinessKey());
					}
					retList.add(p);
				}
			}
			map.put("total", WorkflowEngineUtil.getActivitiWorkflowEngine().getAssignedTasksCount(usershort.getUserId() + "", true, startDate, endDate, processDefinitionKey));
			map.put("rows", retList);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("WorkflowAction.getRunTimeTask" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	/**
	 * 
	 * @return
	 */
	public String getRunTimeTask() {
		Map map = new HashMap();
		try {

			String start = Struts2Utils.getParameter("start");
			String limit = Struts2Utils.getParameter("limit");
			UserShort usershort = Struts2Utils.getCurrentUser();
			List<Task> list = WorkflowEngineUtil.getActivitiWorkflowEngine().getRunTimeTask(usershort.getUserId() + "", Integer.parseInt(limit), Integer.parseInt(start));
			List<Map> retList = new ArrayList();
			if (list != null) {
				for (Task h : list) {
					Map p = new HashMap();
					p.put("DESCRIPTION", h.getDescription());
					p.put("TASKID", h.getId());
					p.put("NAME", h.getName());
					p.put("FORMKEY", h.getFormKey());
					p.put("OWNER", h.getOwner());
					p.put("PROCESSDEFINITIONID", h.getProcessDefinitionId());
					p.put("CREATETIME", DateUtil.dateToString(h.getCreateTime()));
					p.put("PROCESSINSTANCEID", h.getProcessInstanceId());
					p.put("EXECUTIONID", h.getExecutionId());
					p.put("TASKDEFINITIONKEY", h.getTaskDefinitionKey());
					HistoricProcessInstance hisp = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessInstanceById(h.getProcessInstanceId());
					if (hisp != null) {
						p.put("BUSINESSKEY", hisp.getBusinessKey());
					}
					retList.add(p);
				}
			}
			map.put("total", WorkflowEngineUtil.getActivitiWorkflowEngine().getRunTimeTaskCount(usershort.getUserId() + ""));
			map.put("rows", retList);

		} catch (Exception e) {
			map.put("total", 0);
			map.put("rows", new ArrayList());
			e.printStackTrace();
			_log.error("WorkflowAction.getRunTimeTask" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;

	}

	public String getProcessComment() {
		Map map = new HashMap();
		String processInstanceId = Struts2Utils.getParameter("processInstanceId");
		List ret = new ArrayList();
		try {
			ret = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessComment(processInstanceId);
		} catch (Exception e) {
			ret = new ArrayList();
			_log.error("WorkflowAction.getProcessComment" + "");
		}
		map.put("total", ret.size());
		map.put("rows", ret);
		Struts2Utils.renderDeepJson(map);
		return null;
	}

	public String findNextUserIdByProcessKey() {
		List ret = new ArrayList();
		try {
			HashMap map = new HashMap();
			String type = Struts2Utils.getParameter("PROCKEY");
			// map.put("PROCKEY", key);
			Category c = CategoryServiceUtil.getService().getCategoryByPrimaryKey(Long.parseLong(type));
			if (c != null && c.getDefkey() != null && !c.getDefkey().equals("")) {
				ProcessDefinition process = WorkflowEngineUtil.getProcessDefinitionSingleResultBykey(c.getDefkey());
				if (process != null) {
					map.put("PROCID", process.getId());
					// map.put("PROCKEY", c.getDefkey());
					List<Map> list = WorkflowServiceUtil.getService().findActivitiConfigList(map);
					if (list.size() > 1) {
						String taskkey = list.get(1).get("ACTIVITIID") + "";
						map.put("ACTIVITIID", taskkey);
					}
					map.put("PROCKEY", c.getDefkey());
					ret = WorkflowServiceUtil.getService().findNextConfigUsers(map);
				}
			}
		} catch (Exception e) {

		}
		Struts2Utils.renderDeepJson(ret);
		return null;
	}

	public String findNextConfigUsers() {
		HashMap map = new HashMap();
		List ret = new ArrayList();
		String taskkey = "";
		try {
			String taskId = Struts2Utils.getParameter("TASKID");
			HistoricTaskInstance his = WorkflowEngineUtil.getActivitiWorkflowEngine().getHistoricTaskInstance(taskId);
			if (his != null) {
				String processInstanceId = his.getProcessInstanceId();
				String taskDef = his.getTaskDefinitionKey();
				his.getProcessDefinitionId();
				ProcessDefinition proced = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessDefinitionSingleResult(his.getProcessDefinitionId());
				if (proced != null) {
					String pdfKey = proced.getKey();
					map.put("PROCID", proced.getId());
					map.put("PROCKEY", proced.getKey());
					List<Map> list = WorkflowServiceUtil.getService().findActivitiConfigList(map);
					if (list != null && list.size() > 0) {
						for (int j = 0; j < list.size(); j++) {
							Map p = list.get(j);
							if (p.get("ACTIVITIID").equals(taskDef)) {
								if (list.size() > j + 1) {
									map.put("PROCKEY", proced.getKey());
									taskkey = list.get(j + 1).get("ACTIVITIID") + "";
									map.put("ACTIVITIID", taskkey);
									break;
								}
							}
						}
					}

				}

			}
			if (taskkey != null && !taskkey.equals("")) {
				ret = WorkflowServiceUtil.getService().findNextConfigUsers(map);
			}

		} catch (Exception e) {
			ret = new ArrayList();
			_log.error("WorkflowAction.findNextConfigUsers" + "");
		}
		Struts2Utils.renderDeepJson(ret);
		return null;

	}

	public String showDisplayByUserId() {
		Map map = new HashMap();
		String taskId = Struts2Utils.getParameter("TASKID");
		Task task;
		HistoricTaskInstance histask;
		UserShort usershort = Struts2Utils.getCurrentUser();
		map.put("COMPLETE", "0");
		map.put("SHOWUSERS", "0");
		map.put("CANEXE", "0");
		try {
			task = WorkflowEngineUtil.getActivitiWorkflowEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
			if (usershort != null) {
				if (task != null) {
					if (task.getAssignee().endsWith(usershort.getUserId() + "")) {
						map.put("CANEXE", "1");
					}
					HistoricProcessInstance hp = WorkflowEngineUtil.getActivitiWorkflowEngine().getProcessInstanceById(task.getProcessInstanceId());
					if (hp != null) {
						ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) (WorkflowEngineUtil.getActivitiWorkflowEngine().getRepoService().getProcessDefinition(hp.getProcessDefinitionId()));
						List<ActivityImpl> activitiList = processDefinition.getActivities();
						List<String> retList = new ArrayList();
						for (ActivityImpl activityImpl : activitiList) {
							String id = activityImpl.getId();
							ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();
							try {
								if (activityBehavior instanceof UserTaskActivityBehavior) {
									String activitiId = activityImpl.getId();
									retList.add(activitiId);
								}
							} catch (Exception ex) {
								System.out.print(ex);
							}
						}
						for (int j = 0; j < retList.size(); j++) {
							if (retList.get(j).equals(task.getTaskDefinitionKey())) {
								if (retList.size() > j + 1) {
									map.put("SHOWUSERS", "1");
								} else {
									map.put("SHOWUSERS", "0");
								}
								break;
							}
						}

					}

				} else {
					map.put("COMPLETE", "1");
					map.put("SHOWUSERS", "0");
					map.put("CANEXE", "0");
				}
			}
		} catch (Exception e) {

			_log.error("WorkflowAction.showDisplayByUserId" + "");
		}
		Struts2Utils.renderDeepJson(map);
		return null;
	}
}
