var ds;
var grid;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var processInstanceId = getQueryString("processInstanceId");
function init() {
	Ext.QuickTips.init();
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
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
	ds = new Ext.data.JsonStore({
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
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["processInstanceId"] = processInstanceId;
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		height : 200,
		title : '',
		collapsible : false,
		el : "mygrid",
		region : "center"

	});

	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ grid, {
			border : false,
			region : 'south',
			contentEl : 'south-div',
			height : 220
		} ]
	});
	ds.load();
	document.getElementById("processDefinitionIdpng").src = "workflow!loadByProcessInstance.do?processInstanceId=" + processInstanceId+"&random"+Math.random();
}