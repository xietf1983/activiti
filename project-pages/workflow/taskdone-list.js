var ds;
var grid;
var deployWin;
function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.task', null, onMessage, null);
	} catch (ex) {

	}
	Ext.get("beginTime0").dom.value = new Date(Date.parse(new Date().toString()) - 3600000 * 24 * 7).format('Y-m-d')

   Ext.get("endTime0").dom.value = new Date().format('Y-m-d')
	Ext.Ajax.request({
		url : 'workflow!findDefKeyList.do',
		method : 'post',
		params : {},
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for ( var i = 0; i < response.length; i++) {
					var dd = response[i];
					document.getElementById("defkey").options.add(new Option(dd.NAME, dd.KEYVALUE));
				}
			}
		}
	})
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '任务名称',
		width : 300,
		sortable : true,
		dataIndex : 'DESCRIPTION',
		renderer : rendertaskId
	}, {
		header : '完成时间',
		width : 110,
		sortable : true,
		dataIndex : 'ENDTIME'
	}, {
		header : '创建时间',
		width : 110,
		sortable : true,
		dataIndex : 'CREATETIME'
	}]);
	ds = new Ext.data.JsonStore({
		url : 'workflow!getAssignedTasks.do',
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
		}, {
			name : "ENDTIME",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["STARTTIME"] = document.getElementById("beginTime0").value;
		thiz.baseParams["ENDTIME"] = document.getElementById("endTime0").value;
		thiz.baseParams["DEFKEY"]=document.getElementById("defkey").value;
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
		items : [  {
			region : "north",
			height : 30,
			border : false,
			contentEl : "searchdiv"
		},grid ]
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
	top.addTomainTab('任务查看', ".." + formKey + "?TASKID=" + taskId + "&BUSINESSKEY=" + businessKey + "&PROCESSINSTANCEID=" + processInstanceId + "&TASKDEFINITIONKEY=" + taskIdDefin+"&FINISH=1", true);

}