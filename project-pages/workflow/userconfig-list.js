var ds;
var grid;
var deployWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var dsUser;
var userGrid;
var procId = getQueryString("PROCID");
function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.userconfig', null, onsearch, null);
	} catch (ex) {

	}
	var cm = new Ext.grid.ColumnModel([ {
		header : '�ڵ�����',
		dataIndex : 'ACTIVITINAME',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '��Ա����',
		dataIndex : 'USERS',
		sortable : false,
		width : 300,
		align : 'center'
	}, {
		header : '����',
		dataIndex : 'ID',
		sortable : false,
		width : 80,
		align : 'center',
		renderer : renderKeyBox
	} ]);
	ds = new Ext.data.JsonStore({
		url : 'workflow!findActivitiConfigList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "ID_",
			type : "string"
		}, {
			name : "NAME_",
			type : "string"
		}, {
			name : "ID",
			type : "string"
		}, {
			name : "USERS",
			type : "string"
		}, {
			name : "PROCKEY",
			type : "string"
		}, {
			name : "ACTIVITIID",
			type : "string"
		}, {
			name : "ACTIVITINAME",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["ID"] = procId;
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		viewConfig : {
			forceFit : true
		},
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ grid ]
	});
	ds.load();

}
function onsearch(){
	//alert(333333)
}
function search() {
	alert(3333)
	ds.load();
}

function renderKeyBox(value, p, record, rowIndex) {
	var name = record.data.ACTIVITINAME
	return "<a  title='�ڵ���Ա����' href=javascript:showUsersWin('" + value + "','" + name + "')>��Ա����</a>";
	// alert()
}

function showUsersWin(id, name) {
	document.getElementById("ID").value = id;
	if (deployWin == null) {
		deployWin = _window('window-win-users');
		deployWin.height = 480;
		deployWin.addButton('�ر�', docloseWin);
	}
	document.getElementById('fraView').src = "userconfigmap.html?GROUPID=" + id + "&GROUPNAME=" + encodeURI(name)
	deployWin.show();

}

function docloseWin() {
	search();
	deployWin.hide();
}
