var ds;
var grid;
var deployWin;
var deployWinDetail;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var detailgird;
var detailds;
var type = "";
var excelWin;
function onMessage(){
	doSearch();
}
function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.tabchange', null, onMessage, null);
	} catch (ex) {

	}
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
					document.getElementById("deptLeadUserId").options.add(new Option(dd.text, dd.organizationId));
				}
			}
		}
	});
	Ext.get("beginTime0").dom.value = new Date(Date.parse(new Date().toString()) - 3600000 * 24 * (1)).format('Y-m-d');
	Ext.get("endTime0").dom.value = new Date().format('Y-m-d');
	Ext.Ajax.request({
		url : 'companyAction!getCompanyList.do',
		method : 'post',
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for (var i = 0; i < response.rows.length; i++) {
					var dd = response.rows[i];
					document.getElementById("company_text").options.add(new Option(dd.NAME, dd.COMPANYID));
					document.getElementById("company_search").options.add(new Option(dd.NAME, dd.COMPANYID));
				}
			}
		}
	});
	var field = new Ext.form.DropOrgCheckField({
		selectOnFocus : true,
		allowBlank : false,
		width : 220,
		id : 'manning_userId_menu',
		el : 'orgselect'
	});
	field.render();
	Ext.get("beginTime0").dom.value = new Date(Date.parse(new Date().toString()) - 3600000 * 24 * (1)).format('Y-m-d');
	Ext.get("endTime0").dom.value = new Date().format('Y-m-d');
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '�����ϻ���',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 150,
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'COMPANY_SHOWVALUE',
		sortable : false,
		width : 150,
		align : 'center'
	}, {
		header : '�������',
		dataIndex : 'AMOUNT',
		sortable : false,
		width : 120,
		xtype: "numbercolumn",
		format: "0,000.00",
		align : 'center'
	}, {
		header : '��������',
		dataIndex : 'APPROPRIATEDATE_SHOWVALUE',
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
	tb = new Ext.Toolbar("");
	tb.add('<a class="l-btn"  href="javascript:doSearch()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >��ѯ</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-excel l-btn-icon-left" >����</span> </span></a>');
	
	//
	tb.add('-');
	ds = new Ext.data.JsonStore({
		url : 'appropriate!getAppropriateList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "COMPANYID",
			type : "string"
		}, {
			name : "CREATEDATE_SHOWVALUE",
			type : "string"
		}, {
			name : "APPROPRIATEDATE_SHOWVALUE",
			type : "string"
		}, {
			name : "APPROPRIATEID",
			type : "string"
		}, {
			name : "AMOUNT",
			type : "string"
		}, {
			name : "CREATEOR_SHOWVALUE",
			type : "string"
		}, {
			name : "TYPE",
			type : "string"
		}, {
			name : "COMPANY_SHOWVALUE",
			type : "string"
		}, {
			name : "DESCRIPTION",
			type : "string"
		}, {
			name : "ORGNAME_SHOWVALUE",
			type : "string"
		}, {
			name : "ORGANIZATIONID",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["STARTTIME"] = document.getElementById("beginTime0").value;
		thiz.baseParams["COMPANYID"] = document.getElementById("company_search").value;
		thiz.baseParams["ENDTIME"] = document.getElementById("endTime0").value;
		thiz.baseParams["ORGANIZATIONID"] = Ext.getCmp("manning_userId_menu").getValue().IDS;
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
			pageSize : 100,
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
	doSearch();

}
function doSearch() {
	ds.load({
		params : {
			start : 0,
			limit:Ext.getCmp("pagingBar").pageSize
		},
		callback : function(records, options, success) {
			var totle = 0;

			ds.each(function(rec) {
				if (rec.data.AMOUNT != null && rec.data.AMOUNT != '') {
					totle = totle + parseFloat(rec.data.AMOUNT);
				}
			});
			totle = Math.floor(totle * 100) / 100
			//document.getElementById("hejishuhe").innerHTML = "�ϼ� :" + totle + ""
			var record = new Ext.data.Record({
				COMPANYID :  '',
				AMOUNT : totle,
				CREATEDATE_SHOWVALUE : '',
				INDEX : '',
				ID : '',
				SEQCODE : '',
				CREATEOR_SHOWVALUE : '',
				PROJECTCODE :  '',
				PROJECTNAME : '',
				TYPE :  '',
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

function onItemExcellImport() {
	if (excelWin == null) {
		excelWin = _window('window-win-excelWin', true);
		// excelWin.height = 160;
		excelWin.addButton('ȷ��', doSaveExcell);
		excelWin.addButton('�ر�', docloseWinExcell);
	}
	document.getElementById('fraView2').src = "appropriate-vadidation.html";
	excelWin.show();
}

function doSaveExcell() {
	fraView2.window.doSaveItem();
	// excelWin.hide();
}

function docloseWinExcell() {
	excelWin.hide();
}
function onItemAddCheck() {
	document.getElementById("ID").value = '';
	document.getElementById("amount").value = '';
	document.getElementById("appropriateDateStr").value = '';
	document.getElementById("appropriateDateStr").value = new Date().format('Y-m-d');
	// document.getElementById("deptLeadUserId").value = '';
	document.getElementById("amount").value = '';
	document.getElementById("description").value = '';
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 160;
		deployWin.addButton('ȷ��', doSaveItemDetail);
		deployWin.addButton('�ر�', docloseWinDetail);
	}
	deployWin.show();
}
function doSaveItemDetail() {
	var rules = new Array();
	rules[0] = 'appropriateDateStr|required|��������Ϊ���������Ϊ��';
	rules[1] = 'deptLeadUserId|required|�����ϻ���Ϊ���������Ϊ��';
	rules[2] = 'company_text|required|��������Ϊ���������Ϊ��';
	rules[3] = 'amount|required|���Ϊ���������Ϊ��';
	rules[4] = 'amount|double|���Ϊ����';

	// rules[4] = 'myCustomFunction()|custom';
	if (performCheck("userEdit", rules, "classic")) {
		var AMOUNT = document.getElementById("amount").value;
		var APPROPRIATEDATE = document.getElementById("appropriateDateStr").value;
		var APPROPRIATEID = document.getElementById("ID").value;
		var ORGANIZATIONID = document.getElementById("deptLeadUserId").value;
		var DESCRIPTION = document.getElementById("description").value;
		var COMPANYID = document.getElementById("company_text").value;
		Ext.Ajax.request({
			url : 'appropriate!editAppropriate.do',
			method : 'post',
			params : {
				AMOUNT : AMOUNT,
				APPROPRIATEDATE : APPROPRIATEDATE,
				APPROPRIATEID : APPROPRIATEID,
				ORGANIZATIONID : ORGANIZATIONID,
				DESCRIPTION : DESCRIPTION,
				COMPANYID : COMPANYID

			},
			success : function(data, options) {
				docloseWinDetail();
				doSearch();
			}
		});
	}
}
function onItemEditCheck() {
	var starttime = document.getElementById("beginTime0").value;
	var companyId = document.getElementById("company_search").value;
	var endTime = document.getElementById("endTime0").value;
	var orgIds = '';

	if (Ext.getCmp("manning_userId_menu").getValue() != null && Ext.getCmp("manning_userId_menu").getValue().IDS != null) {
		orgIds = Ext.getCmp("manning_userId_menu").getValue().IDS;
	}

	var downLoadExcelPar = "startTime=" + starttime + "&endTime=" + endTime + "&companyId=" + companyId  + "&orgIds=" + orgIds ;
	Ext.get('ExcelDownIFrame').dom.src = "appropriatedownload.jsp" + "?" + downLoadExcelPar;
}
function docloseWinDetail() {
	deployWin.hide();
}

function onItemDeleteCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		_confirm('ɾ���󲻿ɻָ�����ȷ��Ҫɾ����', deleteUserConFirm);
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
			url : 'appropriate!removeAppropriate.do',
			method : 'post',
			params : {
				APPROPRIATEID : list[0].data['APPROPRIATEID']
			},
			success : function(data, options) {
				doSearch();
			}
		});
	}
}
