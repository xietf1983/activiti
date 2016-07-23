var ds;
var grid;
var deployWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
Ext.onReady(function() {
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
					//document.getElementById("company_search").options.add(new Option(dd.NAME, dd.COMPANYID));
				}
			}
		}
	});
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '操作人',
		dataIndex : 'CREATEOR_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '划拨金额',
		dataIndex : 'AMOUNT',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '划拨日期',
		dataIndex : 'APPROPRIATEDATE_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '所属合伙人',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '所属主体',
		dataIndex : 'COMPANY_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '备注',
		dataIndex : 'DESCRIPTION',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '校验结果',
		dataIndex : 'SUCESS',
		sortable : false,
		width : 80,
		align : 'center',
		renderer : renderKeySucess
	}, {
		header : '失败描述',
		dataIndex : 'MSG',
		sortable : false,
		width : 80,
		align : 'center',
		renderer : function(value, metaData, record) {
			metaData.attr = 'ext:qtip="' + value + '"';
			return value;
		}
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('<a class="l-btn"  href="javascript:onItemAddCheck()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-edit l-btn-icon-left" >修改</span> </span></a>');
	tb.add('-');
	tb.add('<span id="hejishuhe" style="font-size:12;color:#2F0000；wdith:150px;">合计:0</span>');
	tb.add('-');
	ds = new Ext.data.JsonStore({
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
			name : "SUCESS",
			type : "string"
		}, {
			name : "MSG",
			type : "string"
		}, {
			name : "ORGNAME_SHOWVALUE",
			type : "string"
		}, {
			name : "ORGANIZATIONID",
			type : "string"
		} ]
	});
	ds.addListener("load", function(store, record, op) {
		var totle = 0;
		grid.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '' && rec.data.SUCESS == '1') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("hejishuhe").innerHTML = "合计 :" + totle + "";
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
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		tbar : tb,
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 30,
			border : false,
			contentEl : "north-div"
		}, grid ]
	});

});

function renderKeySucess(value, p, record, rowIndex) {
	if (value == '1') {
		return "<img src ='../images/icons/dialog-accept-2.png'>"
	} else if (value == '0') {
		return "<img src ='../images/icons/deletebukong.gif'>"
	}
}
function doLoadItem() {
	Ext.Ajax.request({
		url : '../uploadFile?uploadType=uploadAppropriate',
		isUpload : true,
		form : "upForm",
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			ds.removeAll();
			ds.loadData(eval(response));

		}

	});
}

function doSaveItem() {
	var ids = '';
	for (var i = 0; i < ds.getCount(); i++) {
		var data = ds.getAt(i);
		if (data.data['SUCESS'] == '1') {
			if (ids == '') {
				ids = ids + '{"COMPANYID":"' + data.data['COMPANYID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","APPROPRIATEDATE":"' + data.data['APPROPRIATEDATE_SHOWVALUE'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
			} else {
				ids = ids + ',{"COMPANYID":"' + data.data['COMPANYID'] + '","ORGANIZATIONID":"' + data.data['ORGANIZATIONID'] + '","AMOUNT":"' + data.data['AMOUNT'] + '","APPROPRIATEDATE":"' + data.data['APPROPRIATEDATE_SHOWVALUE'] + '","DESCRIPTION":"' + data.data['DESCRIPTION'] + '"}';
			}
		}
	}
	if (ids != '') {
		ids = "[" + ids + "]"
	}
	Ext.Ajax.request({
		url : 'appropriate!addAppropriateFromJsonList.do',
		method : 'post',
		params : {
			JSONLIST : ids
		},
		success : function(data, options) {
			parent.docloseWinExcell();
			parent.doSearch();
		}
	});
}
function docloseWinDetail(){
	deployWin.hide();
}
function doEditItemDetail(){
	var rules = new Array();
	rules[0] = 'appropriateDateStr|required|划拨日期为必填项，不能为空';
	rules[1] = 'deptLeadUserId|required|所属合伙人为必填项，不能为空';
	rules[2] = 'company_text|required|所属主体为必填项，不能为空';
	rules[3] = 'amount|required|金额为必填项，不能为空';
	rules[4] = 'amount|double|金额为数字';

	// rules[4] = 'myCustomFunction()|custom';
	if (performCheck("userEdit", rules, "classic")) {
		var AMOUNT = document.getElementById("amount").value;
		var APPROPRIATEDATE = document.getElementById("appropriateDateStr").value;
		var ORGANIZATIONID = document.getElementById("deptLeadUserId").value;
		var DESCRIPTION = document.getElementById("description").value;
		var COMPANYID = document.getElementById("company_text").value;
		var record = grid.getSelectionModel().getSelected();
		record.data['AMOUNT']=AMOUNT;
		record.data['APPROPRIATEDATE_SHOWVALUE']=APPROPRIATEDATE;
		record.data['COMPANYID']=COMPANYID;
		record.data['ORGANIZATIONID']=ORGANIZATIONID;
		record.data['DESCRIPTION']=DESCRIPTION;
		var item = document.getElementById("deptLeadUserId");
		var text = item.options[item.selectedIndex].text;
		record.data['ORGNAME_SHOWVALUE']=text;
		record.data['MSG']='';
		record.data['SUCESS']='1';
		var item2 = document.getElementById("company_text");
		var text2 = item2.options[item2.selectedIndex].text;
		record.data['COMPANY_SHOWVALUE']=text2;
		grid.getView().refresh();// 刷新表格
		var totle = 0;
		grid.getStore().each(function(rec) {
			if (rec.data.AMOUNT != null && rec.data.AMOUNT != '' && rec.data.SUCESS == '1') {
				totle = totle + parseFloat(rec.data.AMOUNT);
			}
		});
		totle = Math.floor(totle * 100) / 100
		document.getElementById("hejishuhe").innerHTML = "合计 :" + totle + "";
		deployWin.hide();
	}
	
}
function onItemAddCheck() {
	var list = grid.getSelectionModel().getSelections();
	if (list.length > 0) {
		var amount = list[0].data['AMOUNT'];
		var appropriateDateStr =list[0].data['APPROPRIATEDATE_SHOWVALUE'];
		var company_text = list[0].data['COMPANYID'];
		var deptLeadUserId = list[0].data['ORGANIZATIONID'];
		var description = list[0].data['DESCRIPTION'];
		//document.getElementById("ID").value = '';
		document.getElementById("amount").value = amount;
		document.getElementById("appropriateDateStr").value = appropriateDateStr;
		document.getElementById("deptLeadUserId").value = deptLeadUserId;
		document.getElementById("company_text").value =company_text;
		document.getElementById("description").value = description;

	} else {
		_alert("请选择要修改的记录");
		return;
	}
	if (deployWin == null) {
		deployWin = _window('window-win-deploy');
		deployWin.height = 160;
		deployWin.addButton('确定', doEditItemDetail);
		deployWin.addButton('关闭', docloseWinDetail);
	}
	deployWin.show();
}
