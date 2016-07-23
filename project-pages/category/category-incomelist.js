Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var tb;
var deployWin;
var tree;
var rootnode;
var defkey="2";
function init() {
	Ext.QuickTips.init();
	var treeloader = new Ext.tree.TreeLoader({
		dataUrl : 'category!getCategoryList.do?parentId=' + '0' + "&defkey="+defkey
	});
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >增加</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-edit l-btn-icon-left" >修改</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemDeleteCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >删除</span> </span></a>');
	tree = new Ext.ux.tree.TreeGrid({
		title : '类别',
		enableColumnMove : false,
		tbar : tb,
		enableHdMenu : false,
		el : "treegrid",
		region : "center",
		enableDD : false,
		menuDisabled : true,
		headersDisabled : true,
		rootVisible : false,
		animate : true,
		height : document.documentElement.offsetHeigh,
		autoScroll : true,
		columns : [ {
			header : '类别名称',
			dataIndex : 'name',
			sortable : false,
			width : 300
		}, {
			header : '说明',
			width : 400,
			dataIndex : 'description',
			sortable : false

		}, {
			header : '实际操作',
			width : 250,
			hidden : true,
			sortable : false,
			dataIndex : 'categoryId'
		}, {
			header : '',
			width : 250,
			hidden : true,
			sortable : false,
			dataIndex : 'parentId'
		}, {
			header : '',
			width : 250,
			hidden : true,
			sortable : false,
			dataIndex : 'isleaf'
		}, {
			header : '',
			width : 250,
			hidden : true,
			sortable : false,
			dataIndex : 'defkey'
		} ],

		loader : treeloader
	});
	rootnode = new Ext.tree.AsyncTreeNode({
		id : '0',
		text : '根节点',
		draggable : false,// 根节点不容许拖动
		expanded : true
	});
	tree.setRootNode(rootnode);

	tree.on('beforeload', function(node) {
		if (node == rootnode) {
			tree.loader.dataUrl = 'category!getCategoryList.do?parentId=' + '0' + "&defkey="+defkey;
		} else {
			tree.loader.dataUrl = 'category!getCategoryList.do?parentId=' + node.attributes.categoryId + "&defkey="+defkey;
		}
	});
	var droptree = new Ext.form.DropTree({
		id : "taskTree",
		renderTo : "dealDept",
		xtype : "droptree",
		width : 160,
		triggerAction : "all",
		// selModel:"click"
		expandAll : true,
		rootVisible : true,
		selModel : "none"
	})

	droptree.loadLiveTree("category!getCategoryTree.do?&defkey="+defkey);
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ tree ]
	});
}

function onItemAddCheck() {
	// document.getElementById("ID").value = '';
	// document.getElementById("name").value = '';
	// document.getElementById("description").value = '';
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 210;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	Ext.getCmp("taskTree").setDisabled(false);
	var node = new Object();
	node.id = "0";
	node.text = "---请选择---"
	Ext.getCmp("taskTree").setValue(node);
	document.getElementById("ID").value = '';
	document.getElementById("name").value = '';
	document.getElementById("description").value = '';
	//document.getElementById("defkey").value = '';
	deployWin.show();

}
function onItemEditCheck() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node != null) {
		var id = node.attributes.categoryId;
		document.getElementById("ID").value = id;
		document.getElementById("name").value = node.attributes.name;
		document.getElementById("description").value = node.attributes.description;
		document.getElementById("isleaf").value = node.attributes.isleaf;
		//document.getElementById("defkey").value = node.attributes.defkey;
		//document.getElementById("typeKey").value = node.attributes.typeKey;
		Ext.getCmp("taskTree").dropLayer.getRootNode().cascade(function(node2) {
			if (node.attributes.parentId == node2.id) {
				var nodeselect = new Object();
				nodeselect.id = node2.id;
				nodeselect.text = node2.text;
				Ext.getCmp("taskTree").setValue(nodeselect);
				node2.select();
				Ext.getCmp("taskTree").setDisabled(true);
			}
		});
	} else {
		_alert("请选择要修改的记录");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 210;
		deployWin.addButton('确定', doSaveItem);
		deployWin.addButton('取消', docloseWin);
	}
	deployWin.show();
}

function doSaveItem() {
	var rules = new Array();
	rules[0] = 'name|required|类别名称为必填项，不能为空';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'category!editCategory.do',
			method : 'post',
			params : {
				CATEGORYID : document.getElementById("ID").value,
				NAME : document.getElementById("name").value,
				DESCRIPTION : document.getElementById("description").value,
				//ISLEAF : document.getElementById("isleaf").value,
				//DEFKEY : document.getElementById("defkey").value,
				TYPEKEY :typeKey,
				PARENTID : Ext.getCmp("taskTree").getValue()
			},
			success : function(data, options) {
				doSearch();
				docloseWin();
			}
		});
	}
}
function doSearch() {
	rootnode.reload();
}

function docloseWin() {
	deployWin.hide();
}

function onItemDeleteCheck() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node == null) {
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
		var node = tree.getSelectionModel().getSelectedNode();
		Ext.Ajax.request({
			url : 'category!removeCategory',
			method : 'post',
			params : {
				CATEGORYID : node.attributes.categoryId
			},
			success : function(data, options) {
				_alert("刪除成功");
				doSearch();
			}
		});

	}
}
