var ds;
var grid;
var deployWin;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var field;
function init() {
	Ext.QuickTips.init();
	Ext.Ajax.request({
		url : 'getOrganizationList.do',
		method : 'post',
		params : {
			PARENID : '0'
		},
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for (var i = 0; i < response.length; i++) {
					var dd = response[i];
					document.getElementById("dealDept_SEARCH").options.add(new Option(dd.text, dd.organizationId));
					document.getElementById("dealDept").options.add(new Option(dd.text, dd.organizationId));
				}
			}
		}
	})
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '�û���',
		dataIndex : 'USERNAME',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '�˺�',
		dataIndex : 'EMAILADDRESS',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'ORGANIZATIONIDNAME',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '��λ',
		dataIndex : 'USERTYPE',
		sortable : false,
		width : 200,
		align : 'center',
		renderer : renderKeyJob
	}, {
		header : '״̬',
		dataIndex : 'STATUS',
		sortable : false,
		width : 200,
		align : 'center',
		renderer : renderKeyStatus
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:doSearch()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >��ѯ</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >����</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-edit l-btn-icon-left" >�޸�</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemActive()" ><span class="l-btn-left"><span class="l-btn-text icon-circle-start l-btn-icon-left" >����</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemStop()" ><span class="l-btn-left"><span class="l-btn-text icon-circle-stop l-btn-icon-left" >ͣ��</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'userAction!getUserList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "USERID",
			type : "string"
		}, {
			name : "STATUS",
			type : "string"
		}, {
			name : "ORGANIZATIONIDNAME",
			type : "string"
		}, {
			name : "EMAILADDRESS",
			type : "string"
		}, {
			name : "USERNAME",
			type : "string"
		}, {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "ORGANIZATIONID",
			type : "string"
		}, {
			name : "PASSWORD",
			type : "string"
		}, {
			name : "TEL",
			type : "string"
		}, {
			name : "USERTYPE",
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
		tbar : tb,
		collapsible : false,
		el : "mygrid",
		region : "center",
		stripeRows : true,
		bbar : new Ext.PagingToolbar({
			id : 'pagingBar',
			beforePageText : "��",
			afterPageText : "ҳ/��{0}ҳ",
			pageSize : 20,
			store : ds,
			displayInfo : true,
			displayMsg : '��ʾ�ӵ�{0}�� - ��{1}���ļ�¼ ��¼������{2}',
			emptyMsg : "û����Ӧ��¼��"
		})

	});
	field = new Ext.form.DropOrgCheckALLField({
		selectOnFocus : true,
		allowBlank : true,
		width : 200,
		hiddenValue : '',
		id : 'manning_userId_menu',
		el : 'orgselect'
	});
	field.render();
	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 30,
			border : false,
			contentEl : "searchdiv"
		}, grid ]
	});

	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["ORGANIZATIONID"] = document.getElementById("dealDept_SEARCH").value;
		thiz.baseParams["KEYNAME"] = document.getElementById("NAMME_SEARCH").value;
		thiz.baseParams["STATUS"] = document.getElementById("STATUS_SEARCH").value;
	});
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

function renderKeyStatus(value, p, record, rowIndex) {
	if (value == '1') {
		return "����"
	} else {
		return "ͣ��"
	}
}

function renderKeyJob(value, p, record, rowIndex) {
	if (value == '1') {
		return "���Ÿ�����"
	} else {
		return "��ͨԱ��"
	}
}

function onItemAddCheck() {
	// alert(111111)
	document.getElementById("USERID").value = '';
	document.getElementById("description").value = '';
	document.getElementById("NAME").value = '';
	document.getElementById("EMAILADDRESS").value = '';
	document.getElementById("PASSWORD").value = '';
	document.getElementById("TEL").value = '';
	Ext.getCmp("manning_userId_menu").setValue('', '');
	// alert(22222)
	if (deployWin == null) {
		deployWin = _window('winAddInner');
		deployWin.height = 260;
		deployWin.addButton('ȷ��', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	// alert(111111)
	deployWin.show();

}
function onItemEditCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['USERID'];
		var description = list[0].data['DESCRIPTION'];
		var emailAddress = list[0].data['EMAILADDRESS'];
		var tel = list[0].data['TEL'];
		var userName = list[0].data['USERNAME'];
		var password = list[0].data['PASSWORD'];
		var organizationId = list[0].data['ORGANIZATIONID'];
		document.getElementById("USERID").value = id;
		document.getElementById("description").value = description;
		document.getElementById("NAME").value = userName;
		document.getElementById("EMAILADDRESS").value = emailAddress;
		document.getElementById("PASSWORD").value = password;
		document.getElementById("TEL").value = tel;
		document.getElementById("jobtitle").value = list[0].data['USERTYPE'];
		document.getElementById("dealDept").value = organizationId;
		Ext.Ajax.request({
			url : 'userAction!getUserSelectedOrgList.do',
			method : 'post',
			params : {
				USERID : document.getElementById("USERID").value
			},
			success : function(data, options) {
				var response = Ext.util.JSON.decode(data.responseText);
				var rows = response.rows;
				var text = "";
				var ids = "";
				for (var i = 0; i < rows.length; i++) {
					var dd = rows[i];
					if (text != '') {
						text = text + "," + dd.TEXT;
					} else {
						text = text + dd.TEXT;
					}
					if (text != '') {
						ids = ids + "," + dd.ORGANIZATIONID;
					} else {
						ids = ids + "," + dd.ORGANIZATIONID;
					}
				}
				// Ext.getCmp("manning_userId_menu").hiddenValue = new Object();
				// Ext.getCmp("manning_userId_menu").hiddenValue.IDS=ids;
				Ext.getCmp("manning_userId_menu").setValue(text, ids);
				// Ext.getCmp("manning_userId_menu").hiddenValue.IDS=ids;
			}
		});
	} else {
		_alert("��ѡ��Ҫ�޸ĵļ�¼");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('winAddInner');
		deployWin.height = 260;
		deployWin.addButton('ȷ��', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	deployWin.show();
}

function doSaveItem() {
	var rules = new Array();
	rules[0] = 'NAMME|required|�û���Ϊ���������Ϊ��';
	rules[1] = 'EMAILADDRESS|required|�˺�Ϊ���������Ϊ��';
	rules[2] = 'PASSWORD|required|����Ϊ���������Ϊ��';
	if (performCheck("userEdit", rules, "classic")) {
		Ext.Ajax.request({
			url : 'userAction!editUser.do',
			method : 'post',
			params : {
				USERID : document.getElementById("USERID").value,
				NAME : document.getElementById("NAME").value,
				DESCRIPTION : document.getElementById("description").value,
				EMAILADDRESS : document.getElementById("EMAILADDRESS").value,
				PASSWORD : document.getElementById("PASSWORD").value,
				ORGANIZATIONID : document.getElementById("dealDept").value,
				TEL : document.getElementById("TEL").value,
				USERTYPE : document.getElementById("jobtitle").value,
				ORGIDS : Ext.getCmp("manning_userId_menu").hiddenValue.IDS
			},
			success : function(data, options) {
				if (data.responseText == '2') {
					_alert(document.getElementById("EMAILADDRESS").value + "�˺��Ѵ���")
				} else if (data.responseText == '0') {
					_alert("δ֪�쳣")
				} else {
					doSearch();
					docloseWin();
				}
			}
		});
	}
}

function onItemActive() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['USERID'];
		Ext.Ajax.request({
			url : 'userAction!editUserStatus.do',
			method : 'post',
			params : {
				USERID : id,
				STATUS : 1
			},
			success : function(data, options) {
				doSearch();
				// _alert("��ѡ��Ҫ���õļ�¼");
			}
		});
	} else {
		_alert("��ѡ��Ҫ���õļ�¼");
		return;
	}

}

function onItemStop() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['USERID'];
		Ext.Ajax.request({
			url : 'userAction!editUserStatus.do',
			method : 'post',
			params : {
				USERID : id,
				STATUS : 0
			},
			success : function(data, options) {
				doSearch();
				// _alert("��ѡ��Ҫ���õļ�¼");
			}
		});
	} else {
		_alert("��ѡ��Ҫͣ�õļ�¼");
		return;
	}
}

function docloseWin() {
	deployWin.hide();
}
