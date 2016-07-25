Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var tb;
var deployWin;
var tree;
var rootnode;
var typeKey = "1";
var deployWinView
function init() {
	Ext.QuickTips.init();
	var treeloader = new Ext.tree.TreeLoader({
		dataUrl : 'category!getCategoryList.do?parentId=' + '0' + "&TYPEKEY=" + typeKey
	});
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >����</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-edit l-btn-icon-left" >�޸�</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemDeleteCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >ɾ��</span> </span></a>');
	tree = new Ext.ux.tree.TreeGrid({
		//title : '���',
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
			header : '�������',
			dataIndex : 'name',
			sortable : false,
			width : 300
		}, {
			header : 'ʵ�ʲ���',
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
			header : '��Ӧ����',
			width : 300,
			// hidden : true,
			sortable : false,
			dataIndex : 'defkeyName',
			tpl : new Ext.XTemplate('{defkeyName:this.formatdefkey}', {
				formatdefkey : function(v) {
					if (v == null || v == '') {
						return "";
					} else {
						return v;
					}
				}
			})
		}, {
			header : '���̶���鿴',
			width : 150,
			// hidden : true,
			sortable : false,
			dataIndex : 'processDefinitionId',
			tpl : new Ext.XTemplate('{processDefinitionId:this.formatdefkey}', {
				formatdefkey : function(v) {
					if (v == null || v == '') {
						return "";
					} else {
						var buffer = "<span onclick='javascript:goprocessDefinition(\"" + v + "\")'>�鿴</span>";
						return buffer;
					}
				}
			})
		}, {
			header : '˵��',
			width : 100,
			dataIndex : 'description',
			sortable : false

		} ],

		loader : treeloader
	});
	rootnode = new Ext.tree.AsyncTreeNode({
		id : '0',
		text : '���ڵ�',
		draggable : false,// ���ڵ㲻�����϶�
		expanded : true
	});
	tree.setRootNode(rootnode);

	tree.on('beforeload', function(node) {
		if (node == rootnode) {
			tree.loader.dataUrl = 'category!getCategoryList.do?parentId=' + '0' + "&TYPEKEY=" + typeKey;
		} else {
			tree.loader.dataUrl = 'category!getCategoryList.do?parentId=' + node.attributes.categoryId + "&TYPEKEY=" + typeKey;
		}
	});
	var droptree = new Ext.form.DropTree({
		id : "taskTree",
		renderTo : "dealDept",
		xtype : "droptree",
		width : 420,
		triggerAction : "all",
		// selModel:"click"
		expandAll : true,
		rootVisible : true,
		selModel : "none"
	})

	droptree.loadLiveTree("category!getCategoryTree.do?&TYPEKEY=" + typeKey);
	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ tree ]
	});
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
}
function goprocessDefinition(v){
	if (deployWinView == null) {
		deployWinView = _window('window-win-dept');
		deployWinView.height = 550;
		deployWinView.addButton('�ر�', docloseDefinition);
	}
	document.getElementById('processDefinitionIdpng').src = 'loadByDeployment.do?processDefinitionId='+v;
	deployWinView.show();
}
function docloseDefinition(){
	deployWinView.hide();
}
function onItemAddCheck() {
	// document.getElementById("ID").value = '';
	// document.getElementById("name").value = '';
	// document.getElementById("description").value = '';
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 210;
		deployWin.addButton('ȷ��', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	Ext.getCmp("taskTree").setDisabled(false);
	var node = new Object();
	node.id = "0";
	node.text = "---��ѡ��---"
	Ext.getCmp("taskTree").setValue(node);
	document.getElementById("ID").value = '';
	document.getElementById("name").value = '';
	document.getElementById("description").value = '';
	document.getElementById("defkey").value = '';
	deployWin.show();

}
function onItemEditCheck() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node != null) {
		var id = node.attributes.categoryId;
		document.getElementById("ID").value = id;
		document.getElementById("name").value = node.attributes.name;
		if(node.attributes.description!= null){
			document.getElementById("description").value = node.attributes.description;	
		}else{
			document.getElementById("description").value = "";
		}
		if(node.attributes.defkey!=null){
			document.getElementById("defkey").value = node.attributes.defkey;	
		}else{
			document.getElementById("defkey").value ='';
		}
		//document.getElementById("defkey").value = node.attributes.defkey;
		// document.getElementById("typeKey").value = node.attributes.typeKey;
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
		_alert("��ѡ��Ҫ�޸ĵļ�¼");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 210;
		deployWin.addButton('ȷ��', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	deployWin.show();
}

function doSaveItem() {
	var rules = new Array();
	rules[0] = 'name|required|�������Ϊ���������Ϊ��';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'category!editCategory.do',
			method : 'post',
			params : {
				CATEGORYID : document.getElementById("ID").value,
				NAME : document.getElementById("name").value,
				DESCRIPTION : document.getElementById("description").value,
				// ISLEAF : document.getElementById("isleaf").value,
				DEFKEY : document.getElementById("defkey").value,
				TYPEKEY : typeKey,
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
	Ext.getCmp("taskTree").dropLayer.getRootNode().reload();
	//window.location.reload()
}

function docloseWin() {
	deployWin.hide();
}

function onItemDeleteCheck() {
	var node = tree.getSelectionModel().getSelectedNode();
	if (node == null) {
		_alert("��ѡ����Ҫɾ���ļ�¼");
		return;
	}
	_confirm('ɾ���󽫲��ɻָ�����ȷ��Ҫɾ����', deleteConFirm);
}

function deleteConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var node = tree.getSelectionModel().getSelectedNode();
		Ext.Ajax.request({
			url : 'category!removeCategory.do',
			method : 'post',
			params : {
				CATEGORYID : node.attributes.categoryId
			},
			success : function(data, options) {
				doSearch();
				_alert("�h���ɹ�");
				//doSearch();
			}
		});

	}
}
