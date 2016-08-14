var ds;
var grid;
var deployWin;
var deployWinDetail;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var detailgird;
var detailds;
function onMessage() {
	doSearch();
}
function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.tabchange', null, onMessage, null);
	} catch (ex) {

	}
	// Ext.get("beginTime0").dom.value = '2016-01-01';//new Date(Date.parse(new
	// Date().toString()) - 3600000 * 24 * (1)).format('Y-m-d');
	// Ext.get("endTime0").dom.value = new Date().format('Y-m-d');
	var field = new Ext.form.DropOrgCheckField({
		selectOnFocus : true,
		width : 180,
		allowBlank : false,
		id : 'manning_userId_menu',
		el : 'orgselect'
	});
	field.render();

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

	var cm = new Ext.grid.ColumnModel([ {
		header : '所属主体',
		dataIndex : 'companyName',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '所属合伙人',
		dataIndex : 'organizationName',
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '划拨金额',
		dataIndex : 'appropriateamount',
		sortable : false,
		xtype : "numbercolumn",
		format : "0,000.00",
		width : 120,
		align : 'center'
	}, {
		header : '报销金额',
		dataIndex : 'reimburseamount',
		xtype : "numbercolumn",
		format : "0,000.00",
		sortable : false,
		width : 120,
		align : 'center'
	}, {
		header : '结存金额',
		dataIndex : 'balance',
		xtype : "numbercolumn",
		format : "0,000.00",
		sortable : false,
		width : 200,
		align : 'center'
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('&nbsp;&nbsp;&nbsp;&nbsp;');
	tb.add('<a class="l-btn"  href="javascript:doSearch()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >查询</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-excel l-btn-icon-left" >导出</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'analyse!getReimburseAndAppropriateList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "balance",
			type : "string"
		}, {
			name : "reimburseamount",
			type : "string"
		}, {
			name : "appropriateamount",
			type : "string"
		}, {
			name : "companyName",
			type : "string"
		}, {
			name : "organizationName",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["STATUS"] = '5';
		// thiz.baseParams["STARTTIME"] =
		// document.getElementById("beginTime0").value;
		thiz.baseParams["COMPANYID"] = document.getElementById("company_text").value;
		// thiz.baseParams["ENDTIME"] =
		// document.getElementById("endTime0").value;
		if (Ext.getCmp("manning_userId_menu").getValue() != null && Ext.getCmp("manning_userId_menu").getValue().IDS != null) {
			thiz.baseParams["ORGANIZATIONID"] = Ext.getCmp("manning_userId_menu").getValue().IDS;
		}
		// thiz.baseParams["TYPE"] = Ext.getCmp("taskTree").getValue();
	});
	grid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableHdMenu : false,
		ds : ds,
		cm : cm,
		//stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		tbar : tb,
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	ds.load();
	grid.store.on("load", function() {
		gridSpan(grid, "row", "[companyName]", "");
	});
	//ds.load();
	
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 30,
			border : false,
			contentEl : "searchdiv"
		}, grid ]
	});

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

function onItemEditCheck() {
	var status = "5";
	var starttime = "";
	var companyId = document.getElementById("company_text").value;
	var endTime = "";
	var orgIds = '';

	if (Ext.getCmp("manning_userId_menu").getValue() != null && Ext.getCmp("manning_userId_menu").getValue().IDS != null) {
		orgIds = Ext.getCmp("manning_userId_menu").getValue().IDS;
	}
	var type = "";
	var downLoadExcelPar = "startTime=" + starttime + "&endTime=" + endTime + "&companyId=" + companyId + "&type=" + type + "&orgIds=" + orgIds + "&status=" + status;
	Ext.get('ExcelDownIFrame').dom.src = "statisticsdownload.jsp" + "?" + downLoadExcelPar;
}

function docloseWin() {
	deployWin.hide();
}
