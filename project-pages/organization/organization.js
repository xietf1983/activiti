Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var tb;
var deployWin;
function init() {
	Ext.QuickTips.init();
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '部门名称',
		dataIndex : 'text',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '说明',
		dataIndex : 'comments',
		sortable : false,
		width : 200,
		align : 'center'
	} , {
		header : '操作',
		dataIndex : 'organizationId',
		sortable : false,
		width : 100,
		align : 'center',
		renderer : renderKeyBox
	}]);
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >增加</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-edit l-btn-icon-left" >修改</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemDeleteCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >删除</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'getOrganizationGridList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "organizationId",
			type : "string"
		}, {
			name : "parentOrganizationId",
			type : "string"
		}, {
			name : "comments",
			type : "string"
		}, {
			name : "text",
			type : "string"
		}]
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		stripeRows : true,
		viewConfig : {
		 //forceFit : true
		},
		tbar : tb,
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["PARENID"] = 0;
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
			url : 'updateSortOrganization.do',
			method : 'post',
			params : {
				organizationId : id,
				organizationId2 : ds.getAt(index - 1).data.organizationId
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
			url : 'updateSortOrganization.do',
			method : 'post',
			params : {
				organizationId : id,
				organizationId2 : ds.getAt(parseInt(index) + 1).data.organizationId
			},
			success : function(data, options) {
				ds.load();
			}
		});
	}

}
function onItemEditCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['organizationId'];
		var name = list[0].data['text'];
		var description = list[0].data['comments'];
		document.getElementById("ID").value = id;
		document.getElementById("name").value = name;
		document.getElementById("description").value = description;
	} else {
		_alert("请选择要修改的记录");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 150;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	deployWin.show();
}
function onItemAddCheck() {
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 150;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	document.getElementById("description").value = '';
	document.getElementById("ID").value = '';
	document.getElementById("name").value = '';
	deployWin.show();

}
function doSaveItem() {
	var rules = new Array();
	rules[0] = 'name|required|类别名称为必填项，不能为空';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'editOrganization.do',
			method : 'post',
			params : {
				PARENID : 0,
				ORGANIZATIONID : document.getElementById("ID").value,
				COMMENTS : document.getElementById("description").value,
				NAME : document.getElementById("name").value
			},
			success : function(data, options) {
				doSearch();
				docloseWin();
			}
		});
	}
}
function doSearch() {
	ds.load();
	//rootnode.reload();
}

function docloseWin() {
	deployWin.hide();
}

function onItemDeleteCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length <= 0) {
		_alert("请选择需要删除的记录");
		return;
	}
	_confirm('删除后将不可恢复，您确认要删除吗？', deleteConFirm);
}

function deleteConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var list = grid.getSelectionModel().getSelections();
		var node =list[0];
		Ext.Ajax.request({
			url : 'removeOrganization.do',
			method : 'post',
			params : {
				ORGANIZATIONID : node.data.organizationId
			},
			success : function(data, options) {
				_alert("刪除成功");
				doSearch();
			}
		});

	}
}
