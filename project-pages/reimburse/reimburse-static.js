var ds;
var grid;
var deployWin;
var deployWinDetail;
var deptWin;
Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var detailgird;
var detailds;
function onMessage(){
	doSearch();
}

function init() {
	Ext.QuickTips.init();
	try {
		top.PageBus.subscribe('com.runtime.tabchange', null, onMessage, null);
	} catch (ex) {

	}
	Ext.get("beginTime0").dom.value = new Date(Date.parse(new Date().toString()) - 3600000 * 24 * (1)).format('Y-m-d');
	Ext.get("endTime0").dom.value = new Date().format('Y-m-d');
	Ext.Ajax.request({
		url : 'companyAction!getCompanyList.do',
		method : 'post',
		success : function(data, options) {
			var response = Ext.util.JSON.decode(data.responseText);
			if (response != null) {
				for ( var i = 0; i < response.rows.length; i++) {
					var dd = response.rows[i];
					document.getElementById("company_text").options.add(new Option(dd.NAME, dd.COMPANYID));
				}
			}
		}
	});
	var cm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
		header : '报销人',
		dataIndex : 'USERNAME',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '所属合伙人',
		dataIndex : 'ORGNAME_SHOWVALUE',
		sortable : false,
		width : 140,
		align : 'center'
	}, {
		header : '报销类别',
		dataIndex : 'CATEGORY_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '报销主体',
		dataIndex : 'COMPANY_SHOWVALUE',
		sortable : false,
		width : 140,
		align : 'center'
	}, {
		header : '报销金额',
		dataIndex : 'AMOUNT',
		sortable : false,
		xtype: "numbercolumn",
		format: "0,000.00",
		//format：'0,000.00',
		width : 80,
		align : 'center'
	}, {
		header : '报销日期',
		dataIndex : 'CREATEDATE_SHOWVALUE',
		sortable : false,
		width : 100,
		align : 'center'
	}, {
		header : '报销状态',
		dataIndex : 'STATUS',
		sortable : false,
		width : 80,
		align : 'center',
		renderer : renderKeyStatus
	} ]);
	tb = new Ext.Toolbar("");
	tb.add('<a class="l-btn"  href="javascript:doSearch()" ><span class="l-btn-left"><span class="l-btn-text  icon-utils-s-search l-btn-icon-left" >查询</span> </span></a>');
	tb.add('-');
	tb.add('<a class="l-btn"  href="javascript:onItemEditCheck()" ><span class="l-btn-left"><span class="l-btn-text icon-utils-s-excel l-btn-icon-left" >导出</span> </span></a>');
	ds = new Ext.data.JsonStore({
		url : 'reimburse!getReimburseStaticList.do',
		totalProperty : "total",
		root : 'rows',
		fields : [ {
			name : "COMPANYID",
			type : "string"
		}, {
			name : "CREATEDATE_SHOWVALUE",
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
			name : "COMPANY_SHOWVALUE",
			type : "string"
		}, {
			name : "PROJECTNAME",
			type : "string"
		}, {
			name : "TYPE",
			type : "string"
		}, {
			name : "USERNAME",
			type : "string"
		}, {
			name : "ORGNAME_SHOWVALUE",
			type : "string"
		}, {
			name : "CATEGORY_SHOWVALUE",
			type : "string"
		}, {
			name : "STATUS",
			type : "string"
		} ]
	});
	ds.on("beforeload", function(thiz, options) {
		thiz.baseParams["STATUS"] = document.getElementById("STATUS_SEARCH").value;
		thiz.baseParams["STARTTIME"] = document.getElementById("beginTime0").value;
		thiz.baseParams["COMPANYID"] = document.getElementById("company_text").value;
		thiz.baseParams["ENDTIME"] = document.getElementById("endTime0").value;
		if (Ext.getCmp("manning_userId_menu").getValue() != null && Ext.getCmp("manning_userId_menu").getValue().IDS != null) {
			thiz.baseParams["ORGANIZATIONID"] = Ext.getCmp("manning_userId_menu").getValue().IDS;
		}
		thiz.baseParams["TYPE"] = Ext.getCmp("taskTree").getValue()
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
		collapsible : false,
		el : "mygrid",
		region : "center"

	});
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			region : "north",
			height : 60,
			border : false,
			contentEl : "searchdiv"
		}, grid ]
	});

	var droptree = new Ext.form.DropTree({
		id : "taskTree",
		renderTo : "dealDept",
		xtype : "droptree",
		width : 160,
		triggerAction : "all",
		// selModel:"click"
		//expandAll : true,
		rootVisible : true,
		selModel : "none"
	});
	droptree.loadLiveTree("category!getCategoryTree.do?&TYPEKEY=" + 1);

	var field = new Ext.form.DropOrgCheckField({
		selectOnFocus : true,
		allowBlank : false,
		id : 'manning_userId_menu',
		el : 'orgselect'
	});
	field.render();
	doSearch();

}

function renderKeyStatus(value, p, record, rowIndex) {
	if (value == '5') {
		return "已付款"
	} else if (value == '4') {
		return "待付款"
	} else if (value == '3') {
		return "退回"
	} else if (value == '2') {
		return "待审核"
	} else if (value == '1') {
		return "已登记"
	} else {
		return ""
	}
}

function onItemEditCheck() {
	var status = document.getElementById("STATUS_SEARCH").value;
	var starttime = document.getElementById("beginTime0").value;
	var companyId = document.getElementById("company_text").value;
	var endTime = document.getElementById("endTime0").value;
	var orgIds = '';

	if (Ext.getCmp("manning_userId_menu").getValue() != null && Ext.getCmp("manning_userId_menu").getValue().IDS != null) {
		orgIds = Ext.getCmp("manning_userId_menu").getValue().IDS;
	}
	var type = Ext.getCmp("taskTree").getValue();
	var downLoadExcelPar = "startTime=" + starttime + "&endTime=" + endTime + "&companyId=" + companyId + "&type=" + type + "&orgIds=" + orgIds + "&status=" + status;
	Ext.get('ExcelDownIFrame').dom.src = "reimbursedownload.jsp" + "?" + downLoadExcelPar;
}

function doSearch() {
	ds.load();
}

function docloseWin() {
	deployWin.hide();
}
