Ext.BLANK_IMAGE_URL = '../ext/images/default/s.gif';
var mp;
var accordion;
var maxHeight = window.screen.availHeight;
var maxWidth = window.screen.availWidth;
var winpwd;
function init() {
	Ext.Ajax.request({
		url : 'userAction!getCurrentUser.do',
		success : function(response, options) {
			var response = Ext.util.JSON.decode(response.responseText);
			document.getElementById("userNameLogo").innerHTML = response.userName;

		}
	});
	mp = new MainPanel();
	var accordion = new Ext.Panel({
		region : 'west',
		id : 'west-panel', // see Ext.getCmp() below
		title : '系统导航',
		split : true,
		width : 180,
		minSize : 165,
		maxSize : 250,
		border : true,
		collapsible : false,
		margins : '0 0 0 0',
		layout : {
			type : 'accordion',
			animate : true
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		split : true,
		layout : "border",
		items : [ {
			border : false,
			region : 'north',
			contentEl : 'north-div',
			autoHeight : true
		}, accordion, mp ]
	});
	// addTomainTab("系统首页", "http//www.baidu.com", false);
	Ext.Ajax.request({
		url : 'menu!getMenuByUserIdAndParent.do',
		success : function(response, options) {
			var result = eval(response.responseText);
			for (var i = 0; i < result.length; i++) {
				var menu = result[i];
				var html = menu.description;
				var className = menu.icon
				var firstMenuPanel = new Ext.Panel({
					title : menu.text,
					iconCls : className,
					html : '<ul >' + html + '</ul>'
				});
				accordion.add(firstMenuPanel);
				accordion.doLayout();
			}
		}
	});
}

function addTomainTab(text, target, colseinfo) {
	if (target == '')
		return;
	var panid = genID(target);
	var isExists = false;
	if (panid.indexOf('login.jsp') != -1) {
		top.location = target;
		return;
	}
	if (!isExists) {
		mp.items.each(function(item) {
			if (item.id == panid) {
				mp.setActiveTab(panid);
				item.setSrc(target);
				isExists = true;
			}
		});
		if (!isExists) {
			mp.add({
				id : panid,
				xtype : "iframepanel",
				defaultSrc : target,
				title : text,
				closable : colseinfo,
				cls : "x-panel-body",
				style : {
					overflow : "auto"
				},
				frameBorder : 0,
				bodyBorder : false,
				border : false,
				tabTip : text

			});
			totalwin = document.documentElement.offsetHeight - 1;
			topwin = Ext.get('north-div').getHeight();
			mp.setHeight(totalwin - topwin - 1);
			mp.setActiveTab(mp.items.length == 0 ? 0 : mp.items.length - 1);
			mp.doLayout();
		}

	}

}
function genID(url) {
	var tabsrc = url;
	return tabsrc;
}

function logout() {
	_confirm("确定要退出系统吗？", function(msg) {
		if (msg == "yes") {
			Ext.Ajax.request({
				url : 'loginAction!logOut.do',
				params : {},
				success : function(response, options) {
					window.location.href = "../login/index.html";
				}
			});
		}
	});
}

function modefiyuserpwd() {
	clearForm("pwdeditform");
	if (winpwd == null) {
		winpwd = _window('window-win-pwd');
		winpwd.height = 160;
		winpwd.addButton('保 存', dosavepwd);
		winpwd.addButton('关 闭', docanclepwd);
	}
	winpwd.title = "修改密码";
	winpwd.show();
}

function dosavepwd() {
	var rules = new Array();
	rules[0] = 'oldpwd|required|旧密码为必填项，不能为空';
	rules[1] = 'newpwd|required|新密码为必填项，不能为空';
	rules[2] = 'againpwd|required|确认密码：为必填项，不能为空';
	rules[3] = 'againpwd|equal|$newpwd|新密码与确认密码不一致';
	if (performCheck("pwdeditform", rules, "classic")) {
		Ext.Ajax.request({
			url : 'loginAction!updateUserPassword.do',
			params : {
				oldPassword : document.getElementById("oldpwd").value,
				userPW : document.getElementById("newpwd").value
			},
			success : function(response, options) {
				var response = Ext.util.JSON.decode(response.responseText);
				if (response == '1') {
					_alert("修改密码成功！");
					winpwd.hide();
				} else if (response == '0') {
					_alert("修改密码失败，原密码不正确！");
				} else {
					_alert("修改密码失败！");

				}
			}
		});

	}
}

function docanclepwd() {

	winpwd.hide();

}
function closeTab() {
	if (mp.getActiveTab() != null) {
		mp.remove(mp.getActiveTab());
	}
}

MainPanel = function() {
	MainPanel.superclass.constructor.call(this, {
		contentEL : 'centerFrame',
		region : 'center',
		margins : '4 1 1 0',
		resizeTabs : true,
		minTabWidth : 90,
		tabWidth : 110,
		plugins : new Ext.ux.TabCloseMenu(),
		enableTabScroll : true,
		listeners : {
			tabchange : function(tp, p) {
				try {
					top.PageBus.publish("com.runtime.tabchange", null);
				} catch (ex) {

				}
			}

		},
		activeTab : 0
	});
};

Ext.extend(MainPanel, Ext.TabPanel, {
	initEvents : function() {
		MainPanel.superclass.initEvents.call(this);
	}
});
