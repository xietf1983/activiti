var pagemask;
var grid;
var ds;
var dsalluser;
var tb;
var tball;
var screenHight;
var availHeight;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
function init() {
	screenHight = "";
	availHeight = document.documentElement.offsetHeight - 108;
	screenHight = "width:99.8%;height:" + availHeight + "px;";
	document.getElementById("GROUPNAME").value = decodeURI(getQueryString("GROUPNAME"));
	document.getElementById("NAME").innerHTML = document.getElementById("GROUPNAME").value;
	document.getElementById("GROUPID").value = getQueryString("GROUPID");
	tb = new Ext.Toolbar([ '<a id="deleteRole" class="l-btn"  href=javascript:onButtonClick() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-delete l-btn-icon-left" >删除</span> </span></a>' ]);
	// tb.render("toolbar");
	var sm = new Ext.grid.CheckboxSelectionModel();
	var sm2 = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([ sm, {
		header : "账号",
		sortable : false,
		dataIndex : "EMAILADDRESS",
		width : 200
	}, {
		header : "用户名",
		sortable : false,
		dataIndex : "USERNAME",
		width : 200
	} ]);

	var cm2 = new Ext.grid.ColumnModel([ sm2, {
		header : "帐号",
		sortable : false,
		dataIndex : "EMAILADDRESS",
		width : 200
	}, {
		header : "用户名",
		sortable : false,
		dataIndex : "USERNAME",
		width : 200
	} ]);

	ds = new Ext.data.JsonStore({
		url : 'findUserListByRoleId.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : 'USERID'
		}, {
			name : 'EMAILADDRESS'
		}, {
			name : 'USERNAME'
		} ]
	});

	var pagebar = new Ext.PagingToolbar({
		id : 'pagingBar',
		beforePageText : "第",
		afterPageText : "页/共{0}页",
		beforePageSetText : "每页",
		afterPageSetText : "行",
		pageSize : 20,
		store : ds,
		displayInfo : true,
		displayMsg : '显示从第{0}条 - 第{1}条的记录 记录总数：{2}',
		emptyMsg : "没有相应记录！"
	}),

	grid = new Ext.grid.GridPanel({
		id : "gdProjects",
		border : false,
		//el : "roleusergrid",
		tbar : tb,
		//bodyStyle : screenHight,
		ds : ds,
		cm : cm,
		sm : sm,
		title:'当前',
		loadMask : {
			msg : "加载中,请稍候"
		},
		bbar : pagebar
	 //renderTo : document.getElementById("roleusergrid")
	});
	//grid.render();
	ds.baseParams = {
		start : 0,
		limit : 20,
		ROLEID : document.getElementById("GROUPID").value,
		ROLEIDIN : 'ROLEIN'
	};
	ds.load();
	tball = new Ext.Toolbar([

	"-", "用户名:  ", new Ext.form.TextField({
		name : "keyword",
		id : "keyword",
		emptyText : "请输入用户名查询"
	}), "-", '<a id="search" class="l-btn"  href=javascript:onSearchClick() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >查询</span> </span></a>', "-", '<a id="addusertorole" class="l-btn"  href=javascript:onSaveClick() ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-add  l-btn-icon-left" >新增</span> </span></a>' ]);

	//tball.render("toolbar2");

	dsalluser = new Ext.data.JsonStore({
		url : 'findUserListByRoleId.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : 'USERID'
		}, {
			name : 'EMAILADDRESS'
		}, {
			name : 'USERNAME'
		} ]
	});
	gdallusers = new Ext.grid.GridPanel({
		id : "gdallusers",
		//width : document.body.scrollWidth,
		border : false,
		ds : dsalluser,
		cm : cm2,
		tbar:tball,
		title:'可用',
		//renderTo:'allusergrid',
		bodyStyle : screenHight,
		sm : sm2,
		loadMask : {
			msg : "加载中"
		},
		viewConfig : {
			forceFit : true
		},
		bbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : dsalluser,
			displayInfo : true
		})
	});
	//gdallusers.render();
	dsalluser.baseParams = {
		start : 0,
		limit : 20,
		ROLEID : document.getElementById("GROUPID").value,
		ROLENOTIN : 'ROLENOTIN'
	}
	dsalluser.load();
	var tabpanel = new Ext.TabPanel({
		activeTab : 0,
		region : "center",
		border : false,
		items : [grid, gdallusers]
	});
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height :25,
			border : false,
			contentEl : "searchdiv"
		}, tabpanel ]
	});
	tabpanel.items.first().on("beforeshow", refreshFirstTab);
}

function refreshFirstTab() {
	ds.reload({
		params : {
			start : 0,
			limit : 20,
			ROLEID : document.getElementById("GROUPID").value,
			ROLEIDIN : 'ROLEIN'
		}
	});
}
function onButtonClick() {
	var userids = "";
	var records = Ext.getCmp('gdProjects').getSelectionModel().getSelections();
	for (var i = 0; i < records.length; i++) {
		if (userids == '') {
			userids = records[i].data.USERID;
		} else {
			userids = userids + "," + records[i].data.USERID;
		}
	}
	if (records != null && records.length > 0) {
		if (userids == "") {
			_alert("请选择用户");
			return;
		}
		Ext.Ajax.request({
			url : 'removeRoleUserList.do',
			method : 'post',
			params : {
				ROLEID : document.getElementById("GROUPID").value,
				USERIDS : userids
			},
			success : function(data, options) {
				onSearchClick();
				refreshFirstTab();
			}
		});
	}

}

function onSaveClick() {
	var userids = "";
	var records = gdallusers.getSelectionModel().getSelections();
	for (var i = 0; i < records.length; i++) {
		if (userids == '') {
			userids = records[i].data.USERID;
		} else {
			userids = userids + "," + records[i].data.USERID;
		}
	}
	// alert(111)
	if (records != null && records.length > 0) {
		if (userids == "") {
			_alert("请选择用户");
			return;
		}
		Ext.Ajax.request({
			url : 'editRoleUserList.do',
			method : 'post',
			params : {
				ROLEID : document.getElementById("GROUPID").value,
				USERIDS : userids
			},
			success : function(data, options) {
				// alert(111)
				onSearchClick();
				refreshFirstTab();
			}
		});
	}

}
function onSearchClick() {
	dsalluser.load({
		params : {
			start : 0,
			limit : 20,
			NAME : Ext.getCmp("keyword").getValue(),
			ROLEID : document.getElementById("GROUPID").value,
			ROLENOTIN : 'ROLENOTIN'
		}
	});
}
