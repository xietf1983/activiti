var ds;
var grid;
var deployWin;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
function init() {
	Ext.QuickTips.init();
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '主体名称',
		dataIndex : 'NAME',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '备注',
		dataIndex : 'DESCRIPTION',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '创建时间',
		dataIndex : 'CREATEDATE_SHOWVALUE',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '操作',
		dataIndex : 'COMPANYID',
		sortable : false,
		width : 200,
		align : 'center',
		renderer : renderKeyBox
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >增加</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-edit l-btn-icon-left" >修改</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemDeleteCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >删除</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'companyAction!getCompanyList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "COMPANYID",
			type : "string"
		}, {
			name : "CREATEDATE_SHOWVALUE",
			type : "string"
		}, {
			name : "NAME",
			type : "string"
		}, {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "SORTID",
			type : "string"
		} ]
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		stripeRows : true,
		viewConfig : {
		// forceFit : true
		},
		border:false,
		tbar : tb,
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	ds.load();
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ grid ]
	});
}
function renderKeyBox(value, p, record, rowIndex) {
	return "<a  title='向上' href=javascript:goarrowup('" + value + "','" + rowIndex + "')><img src ='../images/icons/arrow-up-4.png'></a><a  title='向下' href=javascript:goarrowdown('" + value + "','" + rowIndex + "')><img src ='../images/icons/arrow-down-4.png'></a>";
}

function goarrowup(id, index) {
	if (index != 0) {
		Ext.Ajax.request({
			url : 'companyAction!updateSortCompany.do',
			method : 'post',
			params : {
				COMPANYID : id,
				COMPANYID2 : ds.getAt(index - 1).data.COMPANYID
			},
			success : function(data, options) {
				ds.load();
			}
		});
	}
}

function goarrowdown(id, index) {
	if (index !=(ds.getCount()-1)) {
		Ext.Ajax.request({
			url : 'companyAction!updateSortCompany.do',
			method : 'post',
			params : {
				COMPANYID : id,
				COMPANYID2 : ds.getAt(parseInt(index) + 1).data.COMPANYID
			},
			success : function(data, options) {
				ds.load();
			}
		});
	}

}

function onItemAddCheck() {
	document.getElementById("ID").value = '';
	document.getElementById("name").value = '';
	document.getElementById("description").value = '';
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 140;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	deployWin.show();

}
function onItemEditCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['COMPANYID'];
		var name = list[0].data['NAME'];
		var description = list[0].data['DESCRIPTION'];
		document.getElementById("ID").value = id;
		document.getElementById("name").value = name;
		document.getElementById("description").value = description;
	} else {
		_alert("请选择要修改的记录");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 140;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	deployWin.show();
}

function doSaveItem() {
	var rules = new Array();
	rules[0] = 'name|required|主体名称为必填项，不能为空';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'companyAction!editCompany.do',
			method : 'post',
			params : {
				ID : document.getElementById("ID").value,
				NAME : document.getElementById("name").value,
				DESCRIPTION : document.getElementById("description").value
			},
			success : function(data, options) {
				ds.load();
				docloseWin();
			}
		});
	}
}
function doSearch() {
	ds.load();
}

function onItemDeleteCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		_confirm('删除后不可恢复，您确认要删除吗？', deleteUserConFirm);
	} else {
		_alert("请选择要删除的记录");
	}

}

function deleteUserConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var list = grid.getSelectionModel().getSelections();
		Ext.Ajax.request({
			url : 'companyAction!deleteCompany.do',
			method : 'post',
			params : {
				ID : list[0].data['COMPANYID']
			},
			success : function(data, options) {
				doSearch();
			}
		});
	}
}

function docloseWin() {
	deployWin.hide();
}
