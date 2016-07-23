package com.xtsoft;

/**
 * 常量
 * 
 * @author Administrator
 * 
 */
public interface SqlMapUtil {
	// ***********************主键表
	public static final String COUNTER_INSERT = "counter_insert";
	public static final String COUNTER_UPDATE = "counter_update";
	public static final String COUNTER_FETCHBYPRIMARYKEY = "counter_fetchByPrimaryKey";

	// ***********************角色名称
	public static final String ROLE_INSERT = "role_insert";
	public static final String ROLE_UPDATE = "role_update";
	public static final String ROLE_DELETE = "role_delete";
	public static final String ROLE_FETCHBYPRIMARYKEY = "role_fetchByPrimaryKey";
	public static final String ROLE_LIST_FETCHBYMAP = "role_list_fetchByMap";
	public static final String ROLE_MENU_DELETE = "role_menu_delete";
	public static final String ROLE_MENU_INSERT = "role_menu_insert";
	public static final String ROLE_MENU_CONTAINS = "role_menu_contains";

	// ***********************主题名称
	public static final String COMPANY_INSERT = "company_insert";
	public static final String COMPANY_UPDATE = "company_update";
	public static final String COMPANY_DELETE = "company_delete";
	public static final String COMPANY_FETCHBYPRIMARYKEY = "company_fetchByPrimaryKey";
	public static final String COMPANY_FETCHBYNAME = "company_fetchByName";
	public static final String COMPANY_LIST_FETCHBYMAP = "company_list_fetchByMap";

	// ***********************部门
	public static final String ORGANIZATION_INSERT = "organization_insert";
	public static final String ORGANIZATION_UPDATE = "organization_update";
	public static final String ORGANIZATION_DELETE = "organization_delete";
	public static final String ORGANIZATION_FETCHBYPRIMARYKEY = "organization_fetchByPrimaryKey";
	public static final String ORGANIZATION_FETCHBYNAME = "organization_fetchByName";
	public static final String ORGANIZATION_LIST_FETCHBYMAP = "organization_list_fetchByMap";
	public static final String ORGANIZATION_LIST_FETCHBYPARENTID = "organization_list_fetchByParentId";
	// ***********************用户主
	public static final String GROUP_INSERT = "group_insert";
	public static final String GROUP_UPDATE = "group_update";
	public static final String GROUP_DLETE = "group_delete";
	public static final String GROUP_FETCHBYPRIMARYKEY = "group_fetchByPrimaryKey";
	public static final String GROUP_LIST_FETCHBYMAP = "group_list_fetchByMap";

	// ***********************用户
	public static final String USER_INSERT = "user_insert";
	public static final String USER_UPDATE = "user_update";
	public static final String USER_FETCHBYPRIMARYKEY = "user_fetchByPrimaryKey";
	public static final String USER_LIST_FETCHBYPARAMTER = "user_list_fetchByUser";
	public static final String USER_COUNT_FETCHBYPARAMTER = "user_count_fetchByUser";

	public static final String USER_LIST_FETCHBYROLEID = "user_list_fetchByRoleId";
	public static final String USER_COUNT_FETCHBYROLEID = "user_count_fetchByRoleId";

	// ***********************用户-角色
	public static final String USERS_ROLES_FETCHBYPRIMARYKEY = "users_Roles_fetchbyPrimarykey";
	public static final String USERS_ROLES_INSERT = "users_Roles_insert";
	public static final String USERS_ROLES_DELETE = "users_Roles_delete";
	// ***********************用户-机构
	public static final String USERS_ORG_FETCHBYPRIMARYKEY = "users_Org_fetchbyPrimarykey";
	public static final String USERS_ORG_INSERT = "users_Org_insert";
	public static final String USERS_ORG_DELETE = "users_Org_delete";
	public static final String USERS_ORG_LIST = "users_Org_list";

	// ***********************用户-用户组
	public static final String USERS_GROUP_FETCHBYPRIMARYKEY = "users_Group_fetchbyPrimarykey";
	public static final String USERS_GROUP_INSERT = "users_Group_insert";
	public static final String USERS_GROUP_DELETE = "users_Group_delete";
	// ***********************菜单
	public static final String MENU_LIST_FETCHBYUSERID = "menu_List_fetchByUserid";
	public static final String MENU_LIST_FETCHBYPARENTID = "menu_List_fetchByParentId";
	public static final String MENUUD_LIST_FETCHBYROLEID = "menu_List_fetchByRoleId";
	public static final String MENU_COUNT_PROTECTEDRESOURC = "menu_Count_protectedResourc";
	public static final String MENU_COUNT_HASRIGHT = "menu_Count_hasRight";

	// ***********************报销类型

	public static final String CATEGORY_INSERT = "category_insert";
	public static final String CATEGORY_UPDATE = "category_update";
	public static final String CATEGORY_DELETE = "category_delete";
	public static final String CATEGORY_FETCHBYPRIMARYKEY = "category_fetchByPrimaryKey";
	public static final String CATEGORY_LIST_FETCHBYMAP = "category_list_fetchByMap";
	public static final String CATEGORY_LIST_FETCHBYPARENTID = "category_list_fetchByParentId";
	public static final String CATEGORY_LIST_FETCHBYFULLPATH = "category_fetchByFullPath";
	
	// ***********************工作流程
	public static final String WORKFLOW_DEF_KEYLIST = "workflow_def_keyList";
	public static final String ACTIVITICONFIG_INSERT = "activiticonfig_insert";// 节点说明
	public static final String USERS_ACTIVITI_FETCHBYPRIMARYKEY = "users_activiti_fetchbyPrimarykey";
	public static final String USERS_ACTIVITI_INSERT = "users_activiti_insert";
	public static final String USERS_ACTIVITI_DELETE = "users_activiti_delete";
	public static final String ACTIVITICONFIG_LIST_FETCHBYMAP = "activiticonfig_list_fetchByMap";// 节点说明
	public static final String ACTIVITICONFIG_FETCHBYPRIMARYKEY = "activiticonfig_fetchByPrimaryKey";
	public static final String ACTIVITICONFIG_COUNT_FETCHBYMAP = "activiticonfig_count_fetchByMap";
	public static final String USER_LIST_FETCHBYCONFIGID = "user_list_fetchByconfigId";
	public static final String USER_COUNT_FETCHBYCONFIGID = "user_count_fetchByconfigId";
	public static final String USER_LIST_NEXTUSERBYTASKID = "user_list_nextUserByTaskId";

	/************** 报销登记 **************/
	public static final String REIMBURSE_LIST_FETCHBYPARAMTER = "reimburse_list_fetchByParamter";
	public static final String REIMBURSE_COUNT_FETCHBYPARAMTER = "reimburse_count_fetchByParamter";
	public static final String REIMBURSE_SEQCODE = "reimburse_seqcode";
	public static final String REIMBURSE_FETCHBYPRIMARYKEY = "reimburse_fetchByPrimaryKey";
	public static final String REIMBURSEDETAIL_FETCHBYPRIMARYKEY= "reimbursedetail_fetchByPrimaryKey";
	public static final String REIMBURSEDETAIL_LIST_FETCHBYKEY = "reimbursedetail_list_fetchbyKey";
	public static final String REIMBURSEDETAIL_DELETE = "reimbursedetail_delete";
	public static final String REIMBURSEDETAIL_INSERT = "reimbursedetail_insert";
	public static final String REIMBURSEDETAIL_UPDATE = "reimbursedetail_update";
	public static final String REIMBURSEDETAIL_STATUS_UPDATE = "reimbursedetail_status_update";
	public static final String REIMBURSE_DELETE = "reimburse_delete";
	public static final String REIMBURSE_INSERT = "reimburse_insert";
	public static final String REIMBURSE_UPDATE = "reimburse_update";
	public static final String REIMBURSE_STATIC_FETCHBYPARAMTER = "reimburse_static_fetchByParamter";
	public static final String REIMBURSE_ANALYSE_FETCHBYPARAMTER = "reimburse_analyse_fetchByParamter";
	
	/************** 划拨登记 **************/
	public static final String APPROPRIATE_LIST_FETCHBYPARAMTER = "appropriate_list_fetchByParamter";
	public static final String APPROPRIATE_COUNT_FETCHBYPARAMTER = "appropriate_count_fetchByParamter";
	public static final String APPROPRIATE_SEQCODE = "appropriate_seqcode";
	public static final String APPROPRIATE_FETCHBYPRIMARYKEY = "appropriate_fetchByPrimaryKey";
	public static final String APPROPRIATE_DELETE = "appropriate_delete";
	public static final String APPROPRIATE_INSERT = "appropriate_insert";
	public static final String APPROPRIATE_UPDATE = "appropriate_update";
	public static final String APPROPRIATE_ANALYSE_FETCHBYPARAMTER = "appropriate_analyse_fetchByParamter";

}
