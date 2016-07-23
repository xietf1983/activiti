package com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.xtsoft.kernel.counter.CounterServiceUtil;
import com.xtsoft.kernel.organization.OrganizationServiceUtil;
import com.xtsoft.kernel.organization.model.Organization;
import com.xtsoft.util.ValidateEngine;
import com.xtsoft.workflow.WorkflowEngineUtil;
import com.xtsoft.workflow.WorkflowServiceUtil;
import com.xtsoft.workflow.model.ActivitiConfig;

import flexjson.JSONSerializer;

public class UploadFile extends HttpServlet {
	/**
	 * 获取文件的路径
	 */
	public static final String deployProcess = "deployProcess";
	public static final String uploadAppropriate = "uploadAppropriate";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String uploadType = request.getParameter("uploadType");
		Map object = new HashMap();
		try {
			if (uploadType.equals(deployProcess)) {
				java.util.List<FileItem> fileItems = upload.parseRequest(request);
				for (FileItem file : fileItems) {
					if (file.isFormField()) {
					} else {
						processUploadedFile(file, uploadType);
					}

				}
				object.put("success", true);
				String jsonString = new JSONSerializer().exclude("class").deepSerialize(object);
				response.getWriter().write(jsonString);
				response.getWriter().flush();
			} else if (uploadType.equals(uploadAppropriate)) {
				java.util.List<FileItem> fileItems = upload.parseRequest(request);
				for (FileItem file : fileItems) {
					if (file.isFormField()) {
					} else {

						List<Map> retList = ValidateEngine.validateAppropriate(file.getInputStream(), true, new com.xtsoft.kernel.user.model.UserShort());
						object.put("total", retList.size());
						object.put("rows", retList);
						object.put("success", true);
						String content = new flexjson.JSONSerializer().exclude("class").deepSerialize(object);
						response.setContentType("text/html;charset=GB18030");
						response.getWriter().write(content);
						response.getWriter().flush();
					}

				}

			}

		} catch (Exception e) {
			try {

				object.put("success", false);
				String jsonString = new JSONSerializer().exclude("class").deepSerialize(object);
				response.getWriter().write(jsonString);
				response.getWriter().flush();
			} catch (Exception ex) {

			}

		}
	}

	private void processUploadedFile(FileItem file, String uploadType) throws IOException {
		if (uploadType.equals(deployProcess)) {
			Deployment d = WorkflowEngineUtil.getActivitiWorkflowEngine().deployDefinition(file.getInputStream(), "", "");
			ProcessDefinitionEntity def2 = (ProcessDefinitionEntity) WorkflowEngineUtil.getActivitiWorkflowEngine().getRepoService().createProcessDefinitionQuery().deploymentId(d.getId()).singleResult();

			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) (WorkflowEngineUtil.getActivitiWorkflowEngine().getRepoService().getProcessDefinition(def2.getId()));// WorkflowEngineUtil.getActivitiWorkflowEngine().endTask("47504",

			List<ActivityImpl> activitiList = processDefinition.getActivities();
			int rowIndex = 0;
			for (ActivityImpl activityImpl : activitiList) {
				String id = activityImpl.getId();
				ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();
				try {
					if (activityBehavior instanceof UserTaskActivityBehavior) {
						String activitiId = activityImpl.getId();
						String processDefinitionId = processDefinition.getKey();
						String name = activityImpl.getProperty("name") + "";
						ActivitiConfig c = WorkflowServiceUtil.getService().createActivitiConfig(CounterServiceUtil.increment(ActivitiConfig.class.getName()));
						c.setActivitiId(activitiId);
						c.setName(name);
						c.setProckey(processDefinitionId);
						c.setRowIndex(rowIndex);
						rowIndex = rowIndex + 1;
						WorkflowServiceUtil.getService().addActivitiConfig(c);
					}
				} catch (Exception ex) {
					System.out.print(ex);
				}
			}

		}

	}

	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doPost(arg0, arg1);
	}

}
