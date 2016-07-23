var ds;
var grid;
var deployWin;
var taskId = getQueryString("TASKID");
var processInstanceId = getQueryString("PROCESSINSTANCEID");
var showNext = getQueryString("showuser");
function init() {
	Ext.QuickTips.init();
	if (showNext != null && showNext == '1') {
		document.getElementById("nextuserdiv").style.display = "inline-block";
	} else {
		document.getElementById("nextuserdiv").style.display = "none";
	}
	new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
		enableTabScroll : true,
		layout : "border",
		items : [ {
			border : false,
			region : 'center',
			contentEl : 'north-div',
			height : 90
		} ]
	});
	Ext.Ajax.request({
		url : 'workflow!findNextConfigUsers.do',
		method : 'post',
		params : {
			'TASKID' : taskId
		},
		success : function(response, options) {
			var response = Ext.util.JSON.decode(response.responseText);
			if (response != null) {
				for (var i = 0; i < response.length; i++) {
					var dd = response[i];
					document.getElementById("nextuser").options.add(new Option(dd.USERNAME, dd.USERID));// 使用程序
				}
			}
		}
	});

}
function changeappresult() {
	if (document.getElementById("appresult").checked) {
		if (showNext != null && showNext == '1') {
			document.getElementById("nextuserdiv").style.display = "inline";
		}
	} else {
		document.getElementById("nextuserdiv").style.display = "none";
	}
}
function endTask() {
	var result = 0;
	if (document.getElementById("appresult").checked) {
		result = 1;
	}
	Ext.Ajax.request({
		url : 'workflow!endTask.do',
		method : 'post',
		params : {
			'TASKID' : taskId,
			'PROCESSINSTANCEID' : processInstanceId,
			'ASSIGNEE' : document.getElementById("nextuser").value,
			'RESULT' : result,
			'MESSAGE' : document.getElementById("message").value

		},
		success : function(response, options) {
			//alert(2)
			top.PageBus.publish("com.runtime.task", null);
			//alert(222)
		}
	});
}
