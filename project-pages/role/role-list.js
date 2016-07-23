var grid;
var screenHight;
var tree;
var root;
var availHeight;
var winAddInner;
var add = false;
var ds;
var myReade;
var cm;
var mask;
var datalist;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var tabs;
function getTopScreen() {
	screenHight = "";
	availHeight = document.documentElement.offsetHeight - 140;
	screenHight = "width:99.8%;height:" + availHeight + "px;";
}

function init() {
	mask = _extMask("bodybody", "正在加载");
	mask.show();
	getTopScreen();
	showlist();
}

function showlist() {
	ds = new Ext.data.JsonStore({
		url : 'getRoleList.do',
		root : 'rows',
		autoLoad : true,
		fields : [ {
			name : 'ROLEID'
		}, {
			name : 'NAME'
		} ]
	});
	cm = new Ext.grid.ColumnModel([ {
		header : "权限组名称",
		width : 260,
		sortable : false,
		hideable : false,
		dataIndex : "NAME"
	} ]);
	grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		stripeRows : true,
		viewConfig : {
			forceFit : true,
			stripeRows : true,
			scrollOffset : 0
		},
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		enableColumnMove : false,
		enableHdMenu : false,
		bodyStyle : screenHight,
		tbar : [ '<a class="l-btn"  href=javascript:showinfo() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >增加</span> </span></a>', '<a class="l-btn"  href=javascript:updategroup() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-edit  l-btn-icon-left" >修改</span> </span></a>', '<a class="l-btn"  href=javascript:deletegroup() ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-delete l-btn-icon-left" >删除</span> </span></a>' ]
	});

	var toolbar = new Ext.Toolbar({
		renderTo : "toolbar"
	});
	toolbar.add('<a class="l-btn"  href=javascript:onButtonClickSave() ><span class="l-btn-left"><span class="l-btn-text  icon-circle-save l-btn-icon-left" >保存</span> </span></a>', "-");
	toolbar.add('<a class="l-btn"  href=javascript:onButtonClickSelect() ><span class="l-btn-left"><span class="l-btn-text  icon-circle-selectall l-btn-icon-left" >全选</span> </span></a>', "-");
	toolbar.add('<a class="l-btn"  href=javascript:onButtonClickUnselect() ><span class="l-btn-left"><span class="l-btn-text  icon-circle-dsselectall l-btn-icon-left" >全不选</span> </span></a>');
	root = new Ext.tree.TreeNode({
		text : "根节点",
		draggable : false,
		expanded : true,
		leaf : false,
		id : "0"
	});
	tree = new Ext.tree.TreePanel({
		contentEL : "tree_div",
		useArrows : true,
		animate : true,
		//height : availHeight + 65,
		autoScroll : true,
		enableDD : true,
		rootVisible : true,
		expanded : true,
		border : false,
		root : root
	});
	
	grid.getSelectionModel().on("rowselect", function(sm, rowIdx, r) {
		mask.show();
		document.getElementById("GROUPID").value = r.data.ROLEID;
		document.getElementById("NAME").value = r.data.NAME;
		document.getElementById("GROUPNAME").value = r.data.NAME;
		changeall(root, false);
		Ext.Ajax.request({
			url : 'getRoleMenuList.do',
			method : 'post',
			params : {
				ID : document.getElementById("GROUPID").value
			},
			success : function(data, options) {
				var response = Ext.util.JSON.decode(data.responseText);
				setchecked(response);
			}
		});
		showiframe();

	});
	grid.store.on("load", function() {
		this.grid.getSelectionModel().selectFirstRow();
		this.grid.getView().focusRow(0);
	}, this);
	
	Ext.Ajax.request({
		url : '"getMenuByParent".do',
		method : 'post',
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			var data = eval(response);
			//alert(data)
			root.appendChild(data);
			//alert(22)
			doload();
		}
	});
	tree.on("checkchange", function(node, flag) {
		// 获取所有子节点
		node.cascade(function(node) {
			node.attributes.checked = flag;
			node.ui.checkbox.checked = flag;
			return true;
		});
		// 获取所有父节点
		var pNode = node.parentNode;
		for (; pNode.id != "0"; pNode = pNode.parentNode) {
			if (flag || tree.getChecked(node.id, node.parentNode) == "") {
				pNode.ui.checkbox.checked = flag;
				pNode.attributes.checked = flag;
			}
		}
	});
	tabs = new Ext.TabPanel({
		id : 'tab-customRole',
		renderTo : 'center-div',
		resizeTabs : true,
		height : document.documentElement.clientHeight,
		activeTab : 0,
		tabWidth : 90,
		border : false,
		items : [ {
			contentEL : "center1-div",
			title : "菜单权限",
			region : "center",
			items : [ toolbar, tree ],
			margins : "0 0 0 0"
		}, {
			id : 'customUserListTab',
			contentEl : 'center2-div',
			title : '权限组成员',
			height : availHeight + 120,
			listeners : {
				"activate" : showiframe
			},
			html : '<iframe id="roleuser-iframe"  src = "roleusermap.jsp" frameborder="0" scrolling="auto" style="border:0px none;" width="100%" height="100%"></iframe>'
		} ]
	});
	
	var viewport = new Ext.Viewport({
		layout : "border",
		items : [ {
			region : "west",
			id : "west-panel",
			contentEL : "west-div",
			layoutConfig : {
				animate : true
			},
			items : [ grid ],
			title : "权限组列表",
			split : true,
			width : 280,
			minSize : 175,
			maxSize : 400,
			collapsible : false,
			border : false,
			layout : "fit"
		}, {
			id : "center",
			contentEL : "center-div",
			title : "",
			region : "center",
			items : tabs,
			margins : "0 0 0 0",
			layoutConfig : {
				animate : true
			},
			collapsible : false
		} ]
	});
	
	// grid.render();
	mask.hide();
	
}
// 显示下属成员
function showiframe() {
	var groupId = document.getElementById("GROUPID").value;
	var groupName = document.getElementById("GROUPNAME").value;
	if (groupId == 'undefined' || groupId == '') {
		parent._alert('请先选择左边权限组!');
		return;
	}
	if (tabs.activeTab.id == 'customUserListTab') {
		if (document.getElementById('roleuser-iframe') != null) {
			document.getElementById('roleuser-iframe').src = "roleusermap.html?GROUPID=" + groupId + "&GROUPNAME=" + encodeURI(groupName)
		}
	}
}
function setchecked(listIds) {
	root.cascade(function(node) {
		for (var j = 0; j < listIds.length; j++) {
			if (node.id == listIds[j]) {
				node.attributes.checked = true;
				node.ui.checkbox.checked = true;
				break;
			}
		}
	});
	mask.hide();

}

// 保存
function onButtonClickSave() {
	if (document.getElementById("GROUPID").value == "") {
		parent._alert("请在左边选中权限组!");
		return;
	}
	var ids = tree.getChecked("id");
	document.getElementById("TREEIDS").value = ids;
	Ext.Ajax.request({
		url : 'editRoleMenuList.do',
		method : 'post',
		params : {
			ROLEID : document.getElementById("GROUPID").value,
			TREEIDS : document.getElementById("TREEIDS").value
		},
		success : function(data, options) {
		}
	});

}

function onButtonClickSelect() {
	changeall(root, true);
}

function onButtonClickUnselect() {
	changeall(root, false);
}

function changeall(root, checked) {
	root.expand();
	if (root.hasChildNodes()) {
		root.eachChild(function(child) {
			child.ui.toggleCheck(checked);
			child.attributes.checked = checked;
			changeall(child, checked);
		});
	}
}

// 删除树节点
function removeallchildnode(root) {
	if (root.hasChildNodes()) {
		root.eachChild(function(child) {
			child.remove();
		});
	}
}
function doload() {
	ds.load();
}
// 删除用户组
function deletegroup() {
	var rows = grid.getSelectionModel().getSelections();
	if (rows.length == 0) {
		_alert("请选择要删除的权限组!");
		return;
	}
	_confirm("删除后数据无法恢复，确认要删除吗？", function(msg) {
		if (msg == "yes") {
			// 删除操作
			mask.show();
			Ext.Ajax.request({
				url : 'deleteRole.do',
				method : 'post',
				params : {
					ID : document.getElementById("GROUPID").value
				},
				success : function(data, options) {
					ds.load();
					// closeWin();
				}
			});
			document.getElementById("GROUPID").value = "";
			document.getElementById("NAME").value = "";
			mask.hide();
			doload();
		}
	});
}

function showinfo() {
	// 如果该窗体不存在则定义一个新窗体（定义新窗体标识为相对应DIV的ID）
	if (winAddInner == null) {
		winAddInner = _window("winAddInner");
		winAddInner.addButton('确定', savegroupName);
		winAddInner.addButton('取消', closeWin);
	}
	document.getElementById("GROUPID").value = '';
	document.getElementById("NAME").value = "";
	winAddInner.show();
}

function updategroup() {
	// 如果该窗体不存在则定义一个新窗体（定义新窗体标识为相对应DIV的ID）
	if (winAddInner == null) {
		winAddInner = _window("winAddInner");
		winAddInner.addButton('确定', savegroupName);
		winAddInner.addButton('取消', closeWin);
	}
	var rows = grid.getSelectionModel().getSelections();
	if (rows.length == 0) {
		_alert("请选择要修改的权限组!");
		return;
	}
	winAddInner.show();
}

function closeWin() {
	if (winAddInner != null) {
		winAddInner.hide();
	}
}

function savegroupName() {
	var rules = new Array();
	rules[0] = 'NAME|required|号主体名称为必填项，不能为空';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'editRole.do',
			method : 'post',
			params : {
				ID : document.getElementById("GROUPID").value,
				NAME : document.getElementById("NAME").value
			},
			success : function(data, options) {
				doload();
				closeWin();
			}
		});
	}
}

function groupmap() {

}
