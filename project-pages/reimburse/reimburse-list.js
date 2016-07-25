var ds;
var grid;
var deployWin;
var deployWinDetail;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var detailgird;
var detailds;
var type = "";
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
					// document.getElementById("dealDept_SEARCH").options.add(new
					// Option(dd.text, dd.organizationId));
					document.getElementById("deptLeadUserId").options.add(new Option(dd.text, dd.organizationId));
				}
			}
		}
	});
	Ext.Ajax.request({
		url : 'companyAction!getCompanyList.do',
		method : 'post',
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for (var i = 0; i < response.rows.length; i++) {
					var dd = response.rows[i];
					document.getElementById("company_text").options.add(new Option(dd.NAME, dd.COMPANYID));
				}
			}
		}
	});

	Ext.get("beginTime0").dom.value = new Date(Date.parse(new Date().toString()) - 3600000 * 24 * (1)).format('Y-m-d');
	Ext.get("endTime0").dom.value = new Date().format('Y-m-d');
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '����',
		dataIndex : 'STATUS',
		sortable : false,
		width : 60,
		align : 'center',
		renderer : renderKeyDetail
	}, {
		header : '������',
		dataIndex : 'CREATEOR_SHOWVALUE',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '�������',
		dataIndex : 'CATEGORY_SHOWVALUE',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '���',
		dataIndex : 'SEQCODE',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'COMPANY_SHOWVALUE',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '�������',
		dataIndex : 'AMOUNT',
		sortable : false,
		xtype : "numbercolumn",
		format : "0,000.00",
		width : 120,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'CREATEDATE_SHOWVALUE',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '����״̬',
		dataIndex : 'STATUS',
		sortable : false,
		width : 200,
		align : 'center',
		renderer : renderKeyStatus
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >�½�</span> </span></a>');
	tb.add('-');
	// tb.add('<a class="l-btn" href="javascript:onItemEditCheck()" ><span
	// class="l-btn-left"><span class="l-btn-text icon-utils-s-confirm
	// l-btn-icon-left" >�ύ</span> </span></a>');
	// tb.add('-');
	// tb.add('<a class="l-btn" href="javascript:onItemEditCheck()" ><span
	// class="l-btn-left"><span class="l-btn-text icon-utils-s-edit
	// l-btn-icon-left" >�޸�</span> </span></a>');
	// tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemDeleteCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >ɾ��</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'reimburse!getReimburseList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "COMPANYID",
			type : "string"
		}, {
			name : "CREATEDATE_SHOWVALUE",
			type : "string"
		}, {
			name : "INDEX",
			type : "string"
		}, {
			name : "ID",
			type : "string"
		}, {
			name : "SEQCODE",
			type : "string"
		}, {
			name : "AMOUNT",
			type : "string"
		}, {
			name : "CREATEOR_SHOWVALUE",
			type : "string"
		}, {
			name : "PROJECTCODE",
			type : "string"
		}, {
			name : "PROJECTNAME",
			type : "string"
		}, {
			name : "TYPE",
			type : "string"
		}, {
			name : "PROCESSINSTANCEID",
			type : "string"
		}, {
			name : "CATEGORY_SHOWVALUE",
			type : "string"
		}, {
			name : "COMPANY_SHOWVALUE",
			type : "string"
		}, {
			name : "NEXTUSER",
			type : "string"
		}, {
			name : "STATUS",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["PROJECTCODE"] = document.getElementById("CODE_SEARCH").value;
		thiz.baseParams["STATUS"] = document.getElementById("STATUS_SEARCH").value;
		thiz.baseParams["STARTTIME"] = document.getElementById("beginTime0").value;
		thiz.baseParams["ENDTIME"] = document.getElementById("endTime0").value;
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		tbar : tb,
		bbar : new Ext.PagingToolbar({
			id : 'pagingBar',
			beforePageText : "��",
			afterPageText : "ҳ/��{0}ҳ",
			plugins : new Ext.ux.Andrie.pPageSize(),
			pageSize : 20,
			store : ds,
			displayInfo : true,
			displayMsg : '��ʾ�ӵ�{0}�� - ��{1}���ļ�¼ ��¼������{2}',
			emptyMsg : "û����Ӧ��¼��"
		}),
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	new Ext.Viewport({ // ע��������Viewport��NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 60,
			border : false,
			contentEl : "searchdiv"
		}, grid ]
	});
	// doSearch();
	var cmdetail = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '������',
		dataIndex : 'USERNAME',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '�����ϻ���',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 200,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'BILLNUMBER',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '���',
		dataIndex : 'AMOUNT',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '��ע',
		dataIndex : 'DESCRIPTION',
		sortable : false,
		width : 300,
		align : 'center'
	} ]);
	detailtb = new Ext.Toolbar("");
	detailtb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	detailtb.add('<a class="l-btn"  href="javascript:onItemAddCheckDetail()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-add l-btn-icon-left" >����</span> </span></a>');
	detailtb.add('-');
	detailtb.add('<a class="l-btn"  href="javascript:onItemDeleteDetailCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-delete l-btn-icon-left" >ɾ��</span> </span></a>');
	detailtb.add('-');
	detailtb.add('<span id="hejishuhe" style="font-size:12;color:#2F0000��wdith:150px;">�ϼ�:0</span>');
	//
	detailtb.add('-');
	detailds = new Ext.data.JsonStore({
		url : 'reimburse!getReimburseDetailList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "DETAILID",
			type : "string"
		}, {
			name : "BILLNUMBER",
			type : "string"
		}, {
			name : "AMOUNT",
			type : "string"
		}, {
			name : "REIMBURSEID",
			type : "string"
		}, {
			name : "ORGANIZATIONID",
			type : "string"
		}, {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "INVOICENO",
			type : "string"
		}, {
			name : "ACTIVE",
			type : "string"
		}, {
			name : "USERID",
			type : "string"
		}, {
			name : "USERNAME",
			type : "string"
		}, {
			name : "ORGNAME_SHOWVALUE",
			type : "string"
		} ]
	});
	detailds.on("beforeload", function(thiz, options) {
		thiz.baseParams["REIMBURSEID"] = document.getElementById("ID").value;
	});
	detailds.addListener("add", function(store, record, op) {
		var totle = 0;
		detailgird.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("hejishuhe").innerHTML = "�ϼ� :" + totle + "";
	});
	detailds.addListener("remove", function(store, record, op) {
		var totle = 0;
		detailgird.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("hejishuhe").innerHTML = "�ϼ� :" + totle + ""
	});
	detailgird = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : detailds,
		cm : cmdetail,
		border : false,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		stripeRows : true,
		title : '������ϸ',
		height : 330,
		tbar : detailtb,
		collapsible : false,
		el : "detailgird"

	});
	detailgird.render();
	var droptree = new Ext.form.DropTree({
		id : "taskTree",
		renderTo : "dealDept",
		xtype : "droptree",
		width : 260,
		triggerAction : "all",
		// selModel:"click"
		expandAll : false,
		rootVisible : true,
		selModel : "none"
	});
	droptree.loadLiveTree("category!getCategoryTree.do?&TYPEKEY=" + 1);
	droptree.on('select', function(node) {
		if (!Ext.getCmp("taskTree").getNode().isLeaf()) {
			_alert("��ѡ���ӽڵ�");
		}
		document.all.nextuser.options.length = 0;
		Ext.Ajax.request({
			url : 'workflow!findNextUserIdByProcessKey.do',
			method : 'post',
			params : {
				PROCKEY : Ext.getCmp("taskTree").getValue()
			},
			success : function(data, options) {
				var response = Ext.util.JSON.decode(data.responseText);
				if (response != null) {
					for (var i = 0; i < response.length; i++) {
						var dd = response[i];
						// document.getElementById("dealDept_SEARCH").options.add(new
						// Option(dd.text, dd.organizationId));
						document.getElementById("nextuser").options.add(new Option(dd.USERNAME, dd.USERID));
					}
				}
			}
		});

	});
	// doSearch();

	var field = new Ext.form.DropUserGirdField({
		selectOnFocus : true,
		allowBlank : false,
		id : 'manning_userId_menu',
		el : 'USERNAME'
	});
	field.render();
	field.on('rowclick', function(grid, record) {
		if (record != null) {
			document.getElementById("userId").value = record.data.USERID + "";
			document.getElementById("USERNAME").value = record.data.USERNAME + "";
			document.getElementById("deptLeadUserId").value = record.data.ORGANIZATIONID + "";
		}

	});
	doSearch();
}
function renderKeyDetail(value, p, record, rowIndex) {
	//alert(id)
	if (record.data.ID != null && record.data.ID != '') {
		var processIntance = record.data.PROCESSINSTANCEID;
		var id = record.data.ID;
		return "<a  href=javascript:goDetailPage('" + processIntance + "','" + id + "')>�鿴</a>";
	}else{
		return "";
	}
}
function goDetailPage(processIntance, id) {
	top.addTomainTab('�Ǽ���ϸ', "../reimburse/reimburse-detail.html?" + "BUSINESSKEY=" + id + "&PROCESSINSTANCEID=" + processIntance, true);

}
function renderKeyStatus(value, p, record, rowIndex) {
	if (value == '5') {
		return "�Ѹ���"
	} else if (value == '4') {
		return "������"
	} else if (value == '3') {
		return "�˻�"
	} else if (value == '2') {
		return "�����"
	} else if (value == '1') {
		return "�ѵǼ�"
	} else if (value == '') {
		return ""
	} else {
		return "���ύ";
	}
}

function onItemAddCheckDetail() {
	// document.getElementById("ID").value = '';
	// document.getElementById("name").value = '';
	// document.getElementById("description").value = '';
	document.getElementById("amount").value = '';
	document.getElementById("billnumber").value = '';
	Ext.getCmp("manning_userId_menu").setValue('')
	document.getElementById("userId").value = '';
	document.getElementById("USERNAME").value = '';
	document.getElementById("description").value = '';
	if (deployWinDetail == null) {
		deployWinDetail = _window('window-win-detail');
		deployWinDetail.height = 160;
		deployWinDetail.addButton('ȷ��', doSaveItemDetail);
		deployWinDetail.addButton('�ر�', docloseWinDetail);
	}
	deployWinDetail.show();
}
function onItemDeleteDetailCheck() {
	var list = detailgird.getSelectionModel().getSelections();
	if (list.length > 0) {
		_confirm('ɾ���󲻿ɻָ�����ȷ��Ҫɾ����', deleteDetailConFirm);
	} else {
		_alert("��ѡ��Ҫɾ���ļ�¼");
	}
}
function deleteDetailConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var record = detailgird.getSelectionModel().getSelected();
		detailgird.getStore().remove(record);
		detailgird.getView().refresh();// ˢ�±��
	}
}
function doSaveItemDetail() {
	var rules = new Array();
	rules[0] = 'userId|required|������Ϊ���������Ϊ��';
	rules[1] = 'deptLeadUserId|required|�������ϻ���Ϊ���������Ϊ��';
	// rules[2] = 'billnumber|required|Ʊ������Ϊ���������Ϊ��';
	rules[2] = 'amount|required|���Ϊ���������Ϊ��';
	rules[3] = 'amount|double|���Ϊ����';
	rules[4] = 'billnumber|numeric|Ʊ������Ϊ����'
	if (document.getElementById("billnumber").value == '') {
		document.getElementById("billnumber").value = 1;
	}
	// rules[4] = 'myCustomFunction()|custom';
	if (performCheck("userEditDetail", rules, "classic")) {
		var DETAILID = "";
		var AMOUNT = document.getElementById("amount").value;
		var BILLNUMBER = document.getElementById("billnumber").value;
		var REIMBURSEID = document.getElementById("ID").value;
		var USERNAME = Ext.getCmp("manning_userId_menu").getValue().USERNAME;
		var USERID = document.getElementById("userId").value;
		var item = document.getElementById("deptLeadUserId");
		var text = item.options[item.selectedIndex].text;
		var ORGNAME_SHOWVALUE = text;
		var ORGANIZATIONID = document.getElementById("deptLeadUserId").value;
		var DESCRIPTION = document.getElementById("description").value;
		var INVOICENO = ''// document.getElementById("ID").value;
		var record = new Ext.data.Record({
			DETAILID : DETAILID,
			AMOUNT : AMOUNT,
			BILLNUMBER : BILLNUMBER,
			REIMBURSEID : REIMBURSEID,
			USERID : document.getElementById("userId").value,
			USERNAME : USERNAME,
			ORGNAME_SHOWVALUE : ORGNAME_SHOWVALUE,
			ORGANIZATIONID : ORGANIZATIONID,
			DESCRIPTION : DESCRIPTION,
			INVOICENO : INVOICENO,
			ACTIVE : '1'

		});
		detailgird.getStore().add(record);
		deployWinDetail.hide();
	}

}

function docloseWinDetail() {
	deployWinDetail.hide();
}

function onItemAddCheck() {
	document.getElementById("ID").value = '';
	// document.getElementById("nextuser").value=list[0].data['NEXTUSER'];
	// document.getElementById("name").value = name;
	// document.getElementById("company_text").value = company_text;
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 440;
		// deployWin.addButton('����', doSaveItem);
		deployWin.addButton('�ύ', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	detailgird.getStore().removeAll();
	deployWin.show();

}
function onItemEditCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var id = list[0].data['ID'];
		var company_text = list[0].data['COMPANYID'];
		var description = list[0].data['DESCRIPTION'];
		var type = list[0].data['TYPE'];
		document.getElementById("ID").value = id;
		document.getElementById("nextuser").value = list[0].data['NEXTUSER'];
		// document.getElementById("name").value = name;
		document.getElementById("company_text").value = company_text;
		Ext.getCmp("taskTree").dropLayer.getRootNode().cascade(function(node2) {
			if (type == node2.id) {
				var nodeselect = new Object();
				nodeselect.id = node2.id;
				nodeselect.text = node2.text;
				Ext.getCmp("taskTree").setValue(nodeselect);
				node2.select();
				// Ext.getCmp("taskTree").setDisabled(true);
			}
		});

	} else {
		_alert("��ѡ��Ҫ�޸ĵļ�¼");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 440;
		deployWin.addButton('ȷ��', doSaveItem);
		deployWin.addButton('ȡ��', docloseWin);
	}
	detailds.load();
	deployWin.show();
}
function myCustomFunction() {
	var value = Ext.getCmp("taskTree").getValue()
	// alert(value)
	if (value == null || value == '' || value == '0') {
		return "�������,Ϊ���������Ϊ��";
	}
	if (!Ext.getCmp("taskTree").getNode().isLeaf()) {
		return "��ѡ������ı������";
	}

	return null;
}
function doSaveItem() {
	var rules = new Array();
	rules[0] = 'company_text|required|������������Ϊ���������Ϊ��';
	rules[1] = 'nextuser|required|�»���������Ϊ���������Ϊ��';
	rules[2] = 'myCustomFunction()|custom';
	if (performCheck("userEdit", rules, "classic")) {
		var ids = '';
		var ids = '[';
		for (var i = 0; i < detailgird.getStore().getCount(); i++) {
			var data = detailgird.getStore().getAt(i);
			if (i == 0) {
				ids = ids + '{"USERID":"' + data.data['USERID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","BILLNUMBER":"' + data.data['BILLNUMBER'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
			} else {
				ids = ids + ',{"USERID":"' + data.data['USERID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","BILLNUMBER":"' + data.data['BILLNUMBER'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
			}

		}
		var ids = ids + ']';
		Ext.Ajax.request({
			url : 'reimburse!startWorkFlowReimburse.do',
			method : 'post',
			params : {
				REIMBURSEID : document.getElementById("ID").value,
				COMPANYID : document.getElementById("company_text").value,
				NEXTUSER : document.getElementById("nextuser").value,
				TYPE : Ext.getCmp("taskTree").getValue(),
				JSONLIST : ids

			},
			success : function(data, options) {
				doSearch();
				try {
					top.PageBus.publish("com.runtime.task", null);
				} catch (ex) {

				}
				docloseWin();
			}
		});
	}
}
function doSearch() {
	ds.load({
		params : {
			start : 0,
			limit : Ext.getCmp("pagingBar").pageSize

		},
		callback : function(records, options, success) {
			var totle = 0;

			ds.each(function(rec) {
				if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
					totle = totle + parseFloat(rec.data.AMOUNT);
				}
			});
			totle = Math.floor(totle * 100) / 100
			var record = new Ext.data.Record({
				COMPANYID : '',
				AMOUNT : totle,
				CREATEDATE_SHOWVALUE : '',
				INDEX : '',
				ID : '',
				SEQCODE : '',
				CREATEOR_SHOWVALUE : '',
				PROJECTCODE : '',
				PROJECTNAME : '',
				TYPE : '',
				PROCESSINSTANCEID : '',
				CATEGORY_SHOWVALUE : '',
				COMPANY_SHOWVALUE : '�ϼ�',
				NEXTUSER : '',
				STATUS : ''
			});
			grid.getStore().add(record);

		}
	});
}

function onItemDeleteCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		// alert(list[0].data['ID']);
		if (list[0].data['ID'] == '') {
			return;
		}
		if (list[0].data['STATUS'] == '1' || list[0].data['STATUS'] == '0' || list[0].data['STATUS'] == '3') {
			_confirm('ɾ���󲻿ɻָ�����ȷ��Ҫɾ����', deleteUserConFirm);
		} else {
			_alert("��¼���̴�����,�˻غ����ɾ��");
		}
	} else {
		_alert("��ѡ��Ҫɾ���ļ�¼");
	}

}

function deleteUserConFirm(btn) {
	if (btn == 'no') {
		return false;
	}
	if (btn == 'yes') {
		var list = grid.getSelectionModel().getSelections();
		Ext.Ajax.request({
			url : 'reimburse!removeReimburse.do',
			method : 'post',
			params : {
				REIMBURSEID : list[0].data['ID']
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
