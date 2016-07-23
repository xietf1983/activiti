var ds;
var grid;
var deployWin;
function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.task', null, onMessage, null);
	} catch (ex) {

	}
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '任务名称',
		width : 300,
		sortable : true,
		dataIndex : 'DESCRIPTION',
		renderer : rendertaskId
	}, {
		header : '创建时间',
		width : 110,
		sortable : true,
		dataIndex : 'CREATETIME'
	} ]);
	ds = new Ext.data.JsonStore({
		url : 'workflow!getRunTimeTask.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "TASKID",
			type : "string"
		}, {
			name : "NAME",
			type : "string"
		}, {
			name : "FORMKEY",
			type : "string"
		}, {
			name : "OWNER",
			type : "string"
		}, {
			name : "PROCESSDEFINITIONID",
			type : "string"
		}, {
			name : "CREATETIME",
			type : "string"
		}, {
			name : "PROCESSINSTANCEID",
			type : "string"
		}, {
			name : "EXECUTIONID",
			type : "string"
		}, {
			name : "TASKDEFINITIONKEY",
			type : "string"
		}, {
			name : "BUSINESSKEY",
			type : "string"
		} ]
	});
	grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		enableColumnMove : false,
		enableHdMenu : false,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		collapsible : false,
		el : "mygrid",
		region : "center",
		bbar : new Ext.PagingToolbar({
			id : 'pagingBar',
			beforePageText : "第",
			afterPageText : "页/共{0}页",
			pageSize : 20,
			store : ds,
			displayInfo : true,
			displayMsg : '显示从第{0}条 - 第{1}条的记录 记录总数：{2}',
			emptyMsg : "没有相应记录！"
		})

	});

	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ grid ]
	});
	doSearch();
}
function onMessage(){
	doSearch();
}
function doSearch() {
	ds.load({
		params : {
			start : 0,
			limit : 20
		}
	});
}

function rendertaskId(value, p, record, rowIndex) {
	var taskId = record.data.TASKID;
	var formKey = record.data.FORMKEY;
	var businessKey = record.data.BUSINESSKEY;
	var processInstanceId = record.data.PROCESSINSTANCEID
	var taskIdDefin = record.data.TASKDEFINITIONKEY;
	var finish = "0";
	return "<a  href=javascript:goDetailPage('" + taskId + "','" + formKey + "','" + businessKey + "','" + processInstanceId + "','" + taskIdDefin + "')>" + value + "</a>";
}
function goDetailPage(taskId, formKey, businessKey, processInstanceId, taskIdDefin) {
	top.addTomainTab('任务处理', ".." + formKey + "?TASKID=" + taskId + "&BUSINESSKEY=" + businessKey + "&PROCESSINSTANCEID=" + processInstanceId + "&TASKDEFINITIONKEY=" + taskIdDefin+"&FINISH=0", true);

}