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
		width : 160
	}, {
		header : "用户名",
		sortable : false,
		dataIndex : "USERNAME",
		width : 160
	} ]);

	var cm2 = new Ext.grid.ColumnModel([ sm2, {
		header : "帐号",
		sortable : false,
		dataIndex : "EMAILADDRESS",
		width : 160
	}, {
		header : "用户名",
		sortable : false,
		dataIndex : "USERNAME",
		width : 160
	} ]);

	ds = new Ext.data.JsonStore({
		url : 'workflow!findActivitiConfigUsers.do',
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
		// el : "roleusergrid",
		tbar : tb,
		title:'当前',
		// bodyStyle : screenHight,
		autoScroll : true,
		height : 325,
		ds : ds,
		cm : cm,
		sm : sm,
		loadMask : {
			msg : "加载中,请稍候"
		},
		bbar : pagebar
		// renderTo : document.getElementById("roleusergrid")
	});
	// grid.render();
	ds.baseParams = {
		start : 0,
		limit : 20,
		CONFIGID : document.getElementById("GROUPID").value
	};
	ds.load();
	tball = new Ext.Toolbar([ "-", "用户名:  ", new Ext.form.TextField({
		name : "keyword",
		id : "keyword",
		emptyText : "请输入用户名查询"
	}), "-", '<a id="search" class="l-btn"  href=javascript:onSearchClick() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >查询</span> </span></a>', "-", '<a id="addusertorole" class="l-btn"  href=javascript:onSaveClick() ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-add  l-btn-icon-left" >新增</span> </span></a>' ]);

	// tball.render("toolbar2");

	dsalluser = new Ext.data.JsonStore({
		url : 'workflow!findActivitiConfigUsers.do',
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
		// width : document.body.scrollWidth,
		border : false,
		ds : dsalluser,
		autoScroll : true,
		cm : cm2,
		// height : 265,
		tbar:tball,
		sm : sm2,
		loadMask : {
			msg : "加载中"
		},
		title:'可用',
		viewConfig : {
			forceFit : true
		},
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : dsalluser,
			displayInfo : true
		})
		// renderTo : document.getElementById("allusergrid")
	});
	dsalluser.baseParams = {
		start : 0,
		limit : 10,
		CONFIGIDNOTIN : document.getElementById("GROUPID").value
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
			limit : 10,
			CONFIGID : document.getElementById("GROUPID").value
		}
	});
}
function onButtonClick() {
	var userids = "";
	var records = Ext.getCmp('gdProjects').getSelectionModel().getSelections();
	for ( var i = 0; i < records.length; i++) {
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
			url : 'workflow!removeConfigUserList.do',
			method : 'post',
			params : {
				CONFIGID : document.getElementById("GROUPID").value,
				USERIDS : userids
			},
			success : function(data, options) {
				onSearchClick();
				refreshFirstTab();
				try {
					top.PageBus.publish('com.runtime.userconfig', null);
				} catch (ex) {
                    // alert(ex)
				}
			}
		});
	}

}

function onSaveClick() {
	var userids = "";
	var records = gdallusers.getSelectionModel().getSelections();
	for ( var i = 0; i < records.length; i++) {
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
			url : 'workflow!editConfigUserList.do',
			method : 'post',
			params : {
				CONFIGID : document.getElementById("GROUPID").value,
				USERIDS : userids
			},
			success : function(data, options) {
				try {
					top.PageBus.publish("com.runtime.userconfig", null);
				} catch (ex) {

				}
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
			limit : 10,
			NAME : Ext.getCmp("keyword").getValue(),
			CONFIGIDOTIN : document.getElementById("GROUPID").value
		}
	});
}
