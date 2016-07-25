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
function onMessage(){
	document.getElementById('fraView2').src = "";
	dealWin.hide();
	//alert(11111111)
	top.closeTab();
}
function init() {
	try {
		top.PageBus.subscribe('com.runtime.task', null, onMessage, null);
	} catch (ex) {

	}
	var tb = "";
	tb = new Ext.Toolbar({
		items : [ {
			xtype : "cirbutton",
			id : 'confirm22',
			iconCls : "icon-utils-s-confirm",
			text : "�ύ",
			disabled : true,
			handler : function() {
				onDealWin()
			}
		}, '-', {
			xtype : "cirbutton",
			iconCls : "icon-utils-s-go",
			text : "���̼��",
			handler : function() {
				onItemPocess()
			}
		} ]
	});
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '������',
		dataIndex : 'USERNAME',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '�����ϻ���',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'BILLNUMBER',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '���',
		dataIndex : 'AMOUNT',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '��ע',
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
		stripeRows : true,
		title : '������ϸ',
		collapsible : false,
		region : 'center'

	});
	var cmWorkFlow = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '�ڵ�����',
		width : 100,
		sortable : true,
		dataIndex : 'ACTIVITINAME'
	}, {
		header : '������',
		width : 60,
		sortable : true,
		dataIndex : 'USERNAME'
	}, {
		header : '����ʱ��',
		width : 100,
		sortable : true,
		dataIndex : 'COMPLETETIME'
	}, {
		header : '���',
		width : 60,
		sortable : true,
		dataIndex : 'AUDITRESULT'
	}, {
		header : '���',
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
		title:'�������',
		//height : 200,
		//title : '',
		collapsible : false,
		el : "workflowmygrid"
		//region : "center"

	});
	gridworkflow.render();
	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 270,
			border : false,
			contentEl : "searchdiv"
		}, grid ]
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
			document.getElementById("COMPANY_HTML").innerHTML = data.companyName;
			document.getElementById("AMOUNT_HTML").innerHTML = data.amount;

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
			showUsers = data.SHOWUSERS
			if (data.CANEXE == '1') {
				Ext.getCmp("confirm22").enable();
				// Ext.getCmp('confirm22').disable();
			} else {

			}

		}
	});
	doSearch();
}

function doSearch() {
	ds.load({
		callback : function(records, options, success) {
			var totle = 0;

			ds.each(function(rec) {
				if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
					totle = totle + parseFloat(rec.data.AMOUNT);
				}
			});
			totle = Math.floor(totle * 100) / 100

			var record = new Ext.data.Record({
				DETAILID :  '',
				AMOUNT : totle,
				BILLNUMBER : '',
				REIMBURSEID : '',
				USERID : '',
				USERNAME : '',
				ORGNAME_SHOWVALUE : '�ϼ�',
				ORGANIZATIONID :  '',
				DESCRIPTION : '',
				INVOICENO :  '',
				ACTIVE : '1'

			});
			grid.getStore().add(record);
		}
	});
}

function onItemPocess() {
	var processInstanceId = processId;

	if (deployWin == null) {
		deployWin = _window('window-win-users');
		deployWin.height = 520;
		deployWin.addButton('�ر�', docloseWin);
	}
	document.getElementById('fraView').src = "../workflow/process-monitoring.html?processInstanceId=" + processId
	// document.getElementById('fraView').reload()
	deployWin.show();

}

function docloseWin() {
	deployWin.hide();
}

function onDealWin() {
	var processInstanceId = processId;
	if (dealWin == null) {
		dealWin = new Ext.Window({
			el : 'window-win-dealWin',
			closeAction : 'hide',
			title : "������",
			width : 685,
			height : 200,
			buttonAlign : 'center',
			buttons : [ {
				text : "�ύ",
				id : 'doprocess',
				xtype : "cirbutton",
				iconCls : "icon-circle-start",
				handler : function() {
					// Ext.getCmp("confirm22").disable();
					// Ext.getCmp("confirm").disable();
					Ext.getCmp('confirm22').disable();
					fraView2.window.endTask();
					// Ext.getCmp("confirm").disable();
					//document.getElementById('fraView2').src = "";
					//dealWin.hide();

				}
			}, {
				xtype : "cirbutton",
				iconCls : "icon-utils-s-close",
				text : "�ر�",
				handler : function() {
					dealWin.hide();
				}
			} ]
		});
	}
	document.getElementById('fraView2').src = "../workflow/audit.html?PROCESSINSTANCEID=" + processInstanceId + "&TASKID=" + taskId + "&showuser=" + showUsers;
	dealWin.show();

}
