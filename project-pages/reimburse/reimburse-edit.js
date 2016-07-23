var ds;
var grid;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var taskId = getQueryString("TASKID");
var processId = getQueryString("PROCESSINSTANCEID");
var buesnessKey = getQueryString("BUSINESSKEY");
var taskIdDefin = getQueryString("TASKDEFINITIONKEY");
var finish = getQueryString("FINISH");
var deployWin;
var dealWin;
var complete = 0;
var showUsers = 0;
var deployWinDetail;
var nextUsers = "";
var dataType = "";
var grid;
function onMessage(){
	document.getElementById('fraView2').src = "";
	dealWin.hide();
	top.closeTab();
}
function init() {
	try {
		top.PageBus.subscribe('com.runtime.task', null, onMessage, null);
	} catch (ex) {

	}
	document.getElementById("ID").value = buesnessKey;
	Ext.Ajax.request({
		url : 'getOrganizationList.do',
		method : 'post',
		params : {
			PARENID : '0'
		},
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for (var i = 0; i < response.length; i++) {
					var dd = response[i];
					// document.getElementById("dealDept_SEARCH").options.add(new
					// Option(dd.text, dd.organizationId));
					document.getElementById("deptLeadUserId").options.add(new Option(dd.text, dd.organizationId));
				}
			}
		}
	});

	tb = new Ext.Toolbar({
		items : [ {
			xtype : "cirbutton",
			id : 'confirm',
			iconCls : "icon-utils-s-confirm",
			text : "提交",
			disabled : true,
			handler : function() {
				onDealWin()
			}
		}, '-', {
			xtype : "cirbutton",
			iconCls : "icon-utils-s-go",
			text : "流程监控",
			handler : function() {
				onItemPocess()
			}
		} ]
	});
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '报销人',
		dataIndex : 'USERNAME',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '所属合伙人',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '单据张数',
		dataIndex : 'BILLNUMBER',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '金额',
		dataIndex : 'AMOUNT',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '备注',
		dataIndex : 'DESCRIPTION',
		sortable : false,
		width : 300,
		align : 'center'
	} ]); //
	ds = new Ext.data.JsonStore({
		url : 'reimburse!getReimburseDetailList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "DETAILID",
			type : "string"
		}, {
			name : "BILLNUMBER",
			type : "string"
		}, {
			name : "AMOUNT",
			type : "string"
		}, {
			name : "REIMBURSEID",
			type : "string"
		}, {
			name : "ORGANIZATIONID",
			type : "string"
		}, {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "INVOICENO",
			type : "string"
		}, {
			name : "ACTIVE",
			type : "string"
		}, {
			name : "USERID",
			type : "string"
		}, {
			name : "USERNAME",
			type : "string"
		}, {
			name : "ORGNAME_SHOWVALUE",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["REIMBURSEID"] = buesnessKey;
	});
	detailtb = new Ext.Toolbar({
		items : [ {
			xtype : "cirbutton",
			id : 'adddetaibar',
			iconCls : "icon-utils-s-add",
			text : "增加",
			disabled : true,
			handler : function() {
				onItemAddCheckDetail()
			}
		}, '-', {
			xtype : "cirbutton",
			iconCls : "icon-utils-s-delete",
			id : 'deletedetaibar',
			disabled : true,
			text : "删除",
			handler : function() {
				onItemDeleteDetailCheck()
			}
		} ]
	});

	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		border : false,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		tbar : detailtb,
		stripeRows : true,
		title : '报销明细',
		collapsible : false,
		region : 'center'

	});
	
	//
	var cmWorkFlow = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '节点名称',
		width : 100,
		sortable : true,
		dataIndex : 'ACTIVITINAME'
	}, {
		header : '操作人',
		width : 60,
		sortable : true,
		dataIndex : 'USERNAME'
	}, {
		header : '操作时间',
		width : 100,
		sortable : true,
		dataIndex : 'COMPLETETIME'
	}, {
		header : '结果',
		width : 60,
		sortable : true,
		dataIndex : 'AUDITRESULT'
	}, {
		header : '意见',
		width : 200,
		sortable : true,
		dataIndex : 'COMMENT'
	} ]);
	dsWorkFlow = new Ext.data.JsonStore({
		url : 'workflow!getProcessComment.do',
		totalProperty : "total",
		root : 'rows',
		searchTime : 'searchTime',
		fields : [ {
			name : "TASKID",
			type : "string"
		}, {
			name : "ACTIVITINAME",
			type : "string"
		}, {
			name : "PROCESSDEFINITIONID",
			type : "string"
		}, {
			name : "DURATIONINMILLIS",
			type : "string"
		}, {
			name : "PROCESSINSTANCEID",
			type : "string"
		}, {
			name : "COMPLETETIME",
			type : "string"
		}, {
			name : "USERNAME",
			type : "string"
		}, {
			name : "COMMENT",
			type : "string"
		}, {
			name : "AUDITRESULT",
			type : "string"
		} ]
	});
	//alert(processId)
	dsWorkFlow.on("beforeload", function(thiz, options) {
		thiz.baseParams["processInstanceId"] = processId;
	});
	dsWorkFlow.load();
	gridworkflow = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : dsWorkFlow,
		cm : cmWorkFlow,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		
		height:180,
		title:'审批意见',
		//height : 200,
		//title : '',
		collapsible : false,
		el : "workflowmygrid"
		//region : "center"

	});
	gridworkflow.render();
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		//layout:'fit',
		items : [ {
			region : "north",
			height : 270,
			border : false,
			contentEl : "searchdiv"
		}, grid]
	});
	tb.render('toolbar');
	// doSearch();
	// alert(buesnessKey);
	Ext.Ajax.request({
		url : 'reimburse!loadReimburse.do',
		method : 'post',
		params : {
			'REIMBURSEID' : buesnessKey
		},
		success : function(response, options) {
			var data = Ext.util.JSON.decode(response.responseText);
			document.getElementById("OPERATION_HTML").innerHTML = data.createorName;
			document.getElementById("CREATE_HTML").innerHTML = data.createDateStr;
			document.getElementById("CODE_HTML").innerHTML = data.seqCode;
			document.getElementById("CATEGORY_HTML").innerHTML = data.typeName;
			nextUsers = data.nextUser
			dataType = data.type;
			document.getElementById("AMOUNT_HTML").innerHTML = data.amount;
			Ext.Ajax.request({
				url : 'companyAction!getCompanyList.do',
				method : 'post',
				success : function(data2, options) {
					var response = Ext.util.JSON.decode(data2.responseText);
					if (response != null) {
						for (var i = 0; i < response.rows.length; i++) {
							var dd = response.rows[i];
							document.getElementById("company_text").options.add(new Option(dd.NAME, dd.COMPANYID));
						}

					}
					// alert(data.companyId)
					document.getElementById("company_text").value = data.companyId;
				}
			});
		}
	});

	Ext.Ajax.request({
		url : 'workflow!showDisplayByUserId.do',
		method : 'post',
		params : {
			'TASKID' : taskId
		},
		success : function(response, options) {
			var data = Ext.util.JSON.decode(response.responseText);
			if (data.CANEXE == '1') {
				Ext.getCmp("confirm").enable();
				Ext.getCmp("deletedetaibar").enable();
				Ext.getCmp("adddetaibar").enable();
			} else {

			}

		}
	});
	ds.load();
	var field = new Ext.form.DropUserGirdField({
		selectOnFocus : true,
		allowBlank : false,
		id : 'manning_userId_menu',
		el : 'USERNAME'
	});
	field.render();
	field.on('rowclick', function(grid, record) {
		if (record != null) {
			document.getElementById("userId").value = record.data.USERID + "";
			document.getElementById("USERNAME").value = record.data.USERNAME + "";
			document.getElementById("deptLeadUserId").value = record.data.ORGANIZATIONID + "";
		}

	});
	ds.addListener("add", function(store, record, op) {
		var totle = 0;
		grid.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("AMOUNT_HTML").innerHTML = totle + "";
	});
	ds.addListener("remove", function(store, record, op) {
		var totle = 0;
		grid.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("AMOUNT_HTML").innerHTML = totle + ""
	});
}
function onItemAddCheckDetail() {
	// document.getElementById("ID").value = '';
	// document.getElementById("name").value = '';
	// document.getElementById("description").value = '';
	document.getElementById("amount").value = '';
	document.getElementById("billnumber").value = '';
	Ext.getCmp("manning_userId_menu").setValue('')
	document.getElementById("userId").value = '';
	document.getElementById("USERNAME").value = '';
	document.getElementById("description").value = '';
	if (deployWinDetail == null) {
		deployWinDetail = _window('window-win-detail');
		deployWinDetail.height = 160;
		deployWinDetail.addButton('确定', doSaveItemDetail);
		deployWinDetail.addButton('关闭', docloseWinDetail);
	}
	deployWinDetail.show();
}
function onItemDeleteDetailCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		_confirm('删除后不可恢复，您确认要删除吗？', deleteDetailConFirm);
	} else {
		_alert("请选择要删除的记录");
	}
}
function deleteDetailConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var record = grid.getSelectionModel().getSelected();
		grid.getStore().remove(record);
		grid.getView().refresh();// 刷新表格
	}
}

function doSearch() {
	ds.load();
}
function doSaveItemDetail() {
	var rules = new Array();
	rules[0] = 'userId|required|报销人为必填项，不能为空';
	rules[1] = 'deptLeadUserId|required|下所属合伙人为必填项，不能为空';
	rules[2] = 'billnumber|required|票据张数为必填项，不能为空';
	rules[3] = 'amount|required|金额为必填项，不能为空';
	rules[4] = 'amount|numeric|金额为数字';
	// rules[4] = 'billnumber|numeric|票据张数为数字';
	// rules[4] = 'myCustomFunction()|custom';
	if (performCheck("userEditDetail", rules, "classic")) {
		var DETAILID = "";
		var AMOUNT = document.getElementById("amount").value;
		var BILLNUMBER = document.getElementById("billnumber").value;
		var REIMBURSEID = document.getElementById("ID").value;
		var USERNAME = Ext.getCmp("manning_userId_menu").getValue().USERNAME;
		var USERID = document.getElementById("userId").value;
		var item = document.getElementById("deptLeadUserId");
		var text = item.options[item.selectedIndex].text;
		var ORGNAME_SHOWVALUE = text;
		var ORGANIZATIONID = document.getElementById("deptLeadUserId").value;
		var DESCRIPTION = document.getElementById("description").value;
		var INVOICENO = ''// document.getElementById("ID").value;
		var record = new Ext.data.Record({
			DETAILID : DETAILID,
			AMOUNT : AMOUNT,
			BILLNUMBER : BILLNUMBER,
			REIMBURSEID : REIMBURSEID,
			USERID : Ext.getCmp("manning_userId_menu").getValue().USERID,
			USERNAME : USERNAME,
			ORGNAME_SHOWVALUE : ORGNAME_SHOWVALUE,
			ORGANIZATIONID : ORGANIZATIONID,
			DESCRIPTION : DESCRIPTION,
			INVOICENO : INVOICENO,
			ACTIVE : '1'

		});
		grid.getStore().add(record);
		deployWinDetail.hide();
	}

}
function docloseWinDetail() {
	deployWinDetail.hide();
}

function onItemPocess() {
	var processInstanceId = processId;

	if (deployWin == null) {
		deployWin = _window('window-win-users');
		deployWin.height = 520;
		deployWin.addButton('关闭', docloseWin);
	}
	document.getElementById('fraView').src = "../workflow/process-monitoring.html?processInstanceId=" + processId
	deployWin.show();

}

function docloseWin() {
	deployWin.hide();
}

function onDealWin() {
	var processInstanceId = processId;
	var ids = '';
	var ids = '[';
	for (var i = 0; i < grid.getStore().getCount(); i++) {
		var data = grid.getStore().getAt(i);
		if (i == 0) {
			ids = ids + '{"USERID":"' + data.data['USERID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","BILLNUMBER":"' + data.data['BILLNUMBER'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
		} else {
			ids = ids + ',{"USERID":"' + data.data['USERID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","BILLNUMBER":"' + data.data['BILLNUMBER'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
		}

	}
	var ids = ids + ']';
	Ext.Ajax.request({
		url : 'reimburse!editReimburse.do',
		method : 'post',
		params : {
			REIMBURSEID : document.getElementById("ID").value,
			COMPANYID : document.getElementById("company_text").value,
			NEXTUSER : nextUsers,
			TYPE : dataType,
			JSONLIST : ids
		},
		success : function(data, options) {

		}
	});
	if (dealWin == null) {
		dealWin = new Ext.Window({
			el : 'window-win-dealWin',
			closeAction : 'hide',
			title : "任务处理",
			width : 685,
			height : 100,
			buttonAlign : 'center',
			buttons : [ {
				text : "提交",
				id : 'doprocess',
				xtype : "cirbutton",
				iconCls : "icon-circle-start",
				handler : function() {
					fraView2.window.endTask();
					Ext.getCmp("confirm").disable();
					Ext.getCmp("deletedetaibar").disable();
					Ext.getCmp("adddetaibar").disable();
					//document.getElementById('fraView2').src ="";
					dealWin.hide();
				}
			}, {
				xtype : "cirbutton",
				iconCls : "icon-utils-s-close",
				text : "关闭",
				handler : function() {
					dealWin.hide();
				}
			} ]
		});
	}
	document.getElementById('fraView2').src = "../workflow/audit2.html?PROCESSINSTANCEID=" + processInstanceId + "&TASKID=" + taskId + "&showuser=" + 1;

	dealWin.show();

}
