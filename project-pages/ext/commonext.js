Ext.Ajax.on('requestcomplete', checkSessionStatus, this);
function checkSessionStatus(conn, response, options) {
	var json =  Ext.util.JSON.decode(response.responseText);
	if (json.sessiontimeout != null) {
		top.window.location.href = json.redirectUri;
	}

}

function _window(windowId, isClear) {
	if (Ext.getDom(windowId)) {
		extwindow = Ext.getDom(windowId);
		width = parseFloat(extwindow.getAttribute("width") || 500);
		height = parseFloat(extwindow.getAttribute("height") || 200);
		modal = extwindow.getAttribute("modal") || false;
		title = extwindow.getAttribute("title") || "";
		closeAction = extwindow.getAttribute("closeAction") || "hide";
		resizable = extwindow.getAttribute("resizable") || true;
		if (resizable != 'true') {
			resizable = false;
		} else {
			resizable = true;
		}
		buttons = extwindow.getAttribute("buttons");
		butlist = buttons || "";
		butlist = butlist == "" ? new Array() : butlist.split("|");
		butobj = new Object();
		butobjlist = new Array();
		for (var i = 0; i < butlist.length; i++) {
			butobj = new Object();
			temp = butlist[i].split(":");
			butobj.text = temp[0];
			if (temp[1]) {
				eval("butobj.handler = " + temp[1]);
			}
			butobjlist[butobjlist.length] = butobj;
		}
		if (butobjlist.length == 0) {
			windowId = new Ext.Window({
				el : windowId,
				layout : "fit",
				title : title,
				width : width,
				height : height,
				buttonAlign : 'center',
				autoScroll : true,
				closeAction : closeAction,
				resizable : resizable,
				modal : modal,
				buttonAlign : 'center',
				closable : true
			});
		} else {
			windowId = new Ext.Window({
				el : windowId,
				layout : "fit",
				title : title,
				width : width,
				height : height,
				autoScroll : true,
				closeAction : closeAction,
				resizable : resizable,
				modal : modal,
				buttons : butobjlist,
				buttonAlign : 'center',
				closable : false
			});
		}
		if (!isClear) {
			windowId.on("beforehide", function() {
				var ifrs = this.body.dom.getElementsByTagName("IFRAME");
				for (var i = 0; i < ifrs.length; i++) {
					try {
						ifrs[i].src = "about:blank";
					} catch (e) {
					}
				}
			});
		}
		windowId.on("beforeshow", function(win) {
			setWinPostion(win);
		});
		return windowId;
	}
	return null;
}

// 锟斤拷锟斤拷windw锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷位
function setWinPostion(win) {
	var list = win.getPosition();
	var toppos = window.document.body.scrollTop == 0 ? 10 : window.document.body.scrollTop + 10;
	var winwidth = win.getSize().width;
	var left = Math.floor((window.document.body.clientWidth - winwidth + 10) / 2);
	win.setPosition(left, toppos);
}

function _extMask(containerId, msgStr) {
	return new Ext.LoadMask(containerId, {
		msg : msgStr
	});
}

// 锟斤拷锟缴伙拷锟斤拷EXT锟斤拷alert
function _alert(msg, callbackfun) {
	if (Ext.Msg) {
		Ext.Msg.show({
			title : "提示",
			msg : msg + "&nbsp;&nbsp;&nbsp;&nbsp;",
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.INFO,
			fn : (callbackfun || Ext.emptyFn)
		});
	}
}

// 锟斤拷锟缴伙拷锟斤拷EXT锟斤拷warring
function _warning(msg, callbackfun) {
	if (Ext.Msg) {
		Ext.Msg.show({
			title : "警告",
			msg : msg + "&nbsp;&nbsp;&nbsp;&nbsp;",
			buttons : Ext.MessageBox.OK,
			icon : Ext.MessageBox.WARNING,
			fn : (callbackfun || Ext.emptyFn)
		});
	}
}

// 锟斤拷锟缴伙拷锟斤拷EXT锟斤拷Confirm
function _confirm(msg, fun, objId) {
	if (Ext.Msg) {
		objId = objId || "";
		var funobj = new Object();
		if (fun) {
			eval("funobj.fn = " + fun);
		}
		title = "提示";
		msg = msg + "              ";
		funobj.animEl = objId;
		Ext.Msg.confirm(title, msg, fun);
	}
}

function getString(str) {
	if (str == null || str == "null" || str == undefined) {
		return "";
	} else {
		return str;
	}

}

/*
 * 锟斤拷取锟斤拷前页锟斤拷锟斤拷指锟斤拷锟斤拷锟狡碉拷URL锟斤拷锟斤拷锟斤拷 queryStringName锟斤拷url锟斤拷锟斤拷锟斤拷锟斤拷
 */
function getQueryString(queryStringName, locationUrl) {
	var returnValue = "";
	var URLString = "";
	if (locationUrl == null || locationUrl == '') {
		URLString = new String(document.location);
	} else {
		URLString = locationUrl;
	}
	var serachLocation = -1;
	var queryStringLength = queryStringName.length;
	do {
		serachLocation = URLString.indexOf(queryStringName + "=");
		if (serachLocation != -1) {
			if ((URLString.charAt(serachLocation - 1) == "?") || (URLString.charAt(serachLocation - 1) == "&")) {
				URLString = URLString.substr(serachLocation);
				break;
			}
			URLString = URLString.substr(serachLocation + queryStringLength + 1);
		}
	} while (serachLocation != -1);
	if (serachLocation != -1) {
		var seperatorLocation = URLString.indexOf("&");
		if (seperatorLocation == -1) {
			returnValue = URLString.substr(queryStringLength + 1);
		} else {
			returnValue = URLString.substring(queryStringLength + 1, seperatorLocation);
		}
	}
	return returnValue;
}

/*
 * 锟斤拷jso锟叫碉拷值锟斤拷锟斤拷锟斤拷clsName锟斤拷span锟斤拷
 */
function fillJSOInToSpan(jso, clsName) {
	elementslist = document.getElementsByClassName(clsName);
	for (var i = 0; i < elementslist.length; i++) {
		if (jso[elementslist[i].id] != null && typeof (jso[elementslist[i].id]) != "undefined") {
			elementslist[i].innerHTML = jso[elementslist[i].id];
		}
	}
}

/*
 * 锟斤拷jso锟叫碉拷值锟斤拷锟斤拷锟斤拷clsName锟斤拷span锟斤拷
 */
function fillJSOInToSpan(jso, clsName) {
	elementslist = document.getElementsByClassName(clsName);
	for (var i = 0; i < elementslist.length; i++) {
		if (jso[elementslist[i].id] != null && typeof (jso[elementslist[i].id]) != "undefined") {
			elementslist[i].innerHTML = jso[elementslist[i].id];
		}
	}
}

/*
 * 锟斤拷锟斤拷classname锟斤拷取element list
 */
document.getElementsByClassName = function(clsName) {
	var retVal = new Array();
	var elements = document.getElementsByTagName("*");
	for (var i = 0; i < elements.length; i++) {
		if (elements[i].className.indexOf(" ") >= 0) {
			var classes = elements[i].className.split(" ");
			for (var j = 0; j < classes.length; j++) {
				if (classes[j] == clsName) {
					retVal.push(elements[i]);
				}
			}
		} else {
			if (elements[i].className == clsName) {
				retVal.push(elements[i]);
			}
		}
	}
	return retVal;
};
function clearForm(formId) {
	var validateForm = document.getElementById(formId);
	var inputs = validateForm.getElementsByTagName("input");
	for (var i = 0; i < inputs.length; i++) {
		try {
			inputs[i].value = '';
		} catch (e) {
			continue;
		}
	}
	var textareas = validateForm.getElementsByTagName("textArea");
	for (var i = 0; i < textareas.length; i++) {
		try {
			textareas[i].value = '';
		} catch (e) {
			continue;
		}
	}
}
// 锟斤拷锟斤拷Ext锟侥帮拷钮
function _addExtButton(containerId, buttonId, buttonName, onclickEvent, icon) {
	extButton = new Ext.Toolbar.Button({
		text : buttonName,
		handler : onclickEvent
	});
	extButton.id = buttonId;
	if (icon) {
		extButton.icon = icon;
		extButton.cls = "x-btn-text-icon";
	}
	extButton.render(Ext.getDom(containerId));
	return extButton;
}
Ext.form.DropTree = Ext.extend(Ext.form.TriggerField, {
	cls : "x-combo-list",
	shadow : "sides",
	align : "tl-bl?",
	lazyInit : false,
	minWidth : 90,
	maxHeight : 200,
	minHeight : 100,
	editable : false,
	appselect : this.appselect || false,
	triggerClass : "x-form-function-trigger",
	expandAll : this.expandAll || false,
	initComponent : function() {
		Ext.form.DropTree.superclass.initComponent.call(this);
		this.addEvents("expand", "beforeexpand", "collapse", "nodedelete", "search");
	},
	onRender : function(ct, position) {
		Ext.form.DropTree.superclass.onRender.call(this, ct, position);

		// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷实锟斤拷值
		this.hiddenField = this.el.insertSibling({
			tag : "input",
			type : "hidden",
			name : this.hiddenName || this.name,
			id : (this.hiddenId || this.hiddenName || this.name)
		}, "before", true);

		this.el.dom.removeAttribute("name");

		if (Ext.isGecko) {
			this.el.dom.setAttribute("autocomplete", "off");
		}
	},
	loadLiveTree : function(url) {
		this.initLiveTree(url);
	},

	initLiveTree : function(url) {
		var me = this;
		if (this.dropLayer == null) {
			var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				leaf : false,
				text : "---请选择---"
			});
			var loader = new Ext.tree.TreeLoader({
				dataUrl : url
			})
			this.dropLayer = new Ext.tree.TreePanel({
				header : false,
				root : root,
				loader : loader,
				renderTo : Ext.getBody(),
				animate : false,
				autoScroll : true,
				rootVisible : this.rootVisible || false,
				border : false,
				hidden : true,
				shadow : true,
				floating : true,
				x : 10,
				y : 10
			});
			this.dropLayer.loader.on('beforeload', function(treeLoader, node) { // 锟斤拷锟斤拷锟斤拷
				this.baseParams.parentId = node.attributes.id;
				this.baseParams.appselect = me.appselect;

			});
			this.dropLayer.loader.on('load', function(treeLoader, node) { // 锟斤拷锟斤拷锟斤拷
				if (node == root) {
					if (me.dropLayer.rootVisible) {
						me.setValue(node);
						node.select();
					} else if (node.hasChildNodes) {
						var startNode = node.firstChild;
						me.setValue(startNode);
						startNode.select();
					}

				}
				me.fireEvent('load', this);
			});
			this.dropLayer.on('click', function(node) {
				var selModel = this.selectNodeModel;
				var isLeaf = node.isLeaf();
				var root = me.dropLayer.getRootNode();
				if ((node == root) && selModel != 'all' && !me.rootVisible) {
					return;
				}

				var oldNode = this.getNode();
				if (this.fireEvent('beforeselect', this, node, oldNode) !== false) {
					me.setValue(node);
					me.collapse();
					me.fireEvent('select', this, node, oldNode);
					(oldNode !== node) ? this.fireEvent('afterchange', this, node, oldNode) : '';
				}
				me.setValue(node);
				me.collapse();
			}, this);
			if (this.expandAll) {
				this.dropLayer.expandAll();
			}
			// this.dropLayer.expandAll();
		}

	},
	onTriggerClick : function() {
		if (this.disabled) {
			return;
		}
		if (this.isExpanded()) {
			this.collapse();
			this.el.focus();
		} else {
			this.onFocus({});
			this.onLoad();
			this.el.focus();
		}
	},

	onLoad : function() {
		if (!this.hasFocus) {
			return;
		}
		this.fireEvent("beforeexpand", this);

		if (this.dropLayer != null) {
			this.expand();
		}

	},
	setDefaultChecked : function() {
		if (this.store != null && this.dropLayer != null && this.hiddenField.value != null) {
			var checked = this.hiddenField.value.split(",");
			var valueField = this.store.valueField || "id";

			for (var i = 0; i < checked.length; i++) {
				var attrName = valueField;
				var attrValue = checked[i];
				var nodes = this.dropLayer.root.findChildDeep(attrName, attrValue);

				if (nodes.length > 0) {
					nodes[0].toggleCheck(true);
				}
			}
		}
	},

	expand : function() {
		var me = this;
		if (this.isExpanded() || !this.hasFocus) {
			return;
		}
		var wrapBox = this.wrap.getBox();
		var bodyBox = Ext.getBody().getBox();
		var dropLayerBox = this.dropLayer.getBox();
		var x = wrapBox.x;
		var y = wrapBox.y + wrapBox.height;
		var w = Math.max(this.minWidth, wrapBox.width);
		var h = Math.min(this.maxHeight, Math.max(this.minHeight, bodyBox.height - y - 10));
		this.dropLayer.setPagePosition(x, y);
		var boxWidth = this.boxWidth || w;
		this.dropLayer.setWidth(boxWidth);
		this.dropLayer.setHeight(h);
		this.dropLayer.show();

		if (Ext.isGecko2) {
			el.setOverflow("auto");
		}
		Ext.getDoc().on({
			scope : this,
			mousewheel : this.collapseIf,
			mousedown : this.collapseIf
		});
		// alert(11)
		this.fireEvent("expand", this);
	},
	collapse : function() {
		if (!this.isExpanded()) {
			return;
		}
		this.dropLayer.hide();
		this.fireEvent("collapse", this);
	},
	collapseIf : function(e) {
		if (!e.within(this.wrap) && !e.within(this.dropLayer.getEl())) {
			this.collapse();
		}
	},

	isExpanded : function() {
		return this.dropLayer && this.dropLayer.isVisible();
	},

	initValue : function() {
		Ext.form.DropTree.superclass.initValue.call(this);
		if (this.hiddenField != null) {
			this.hiddenField.value = Ext.isDefined(this.hiddenValue) ? this.hiddenValue : (Ext.isDefined(this.value) ? this.value : "");
		}
	},
	setValue : function(node) {
		if (node != null) {
			this.node = node;
			var text = node.text;
			this.lastSelectionText = text;
			if (this.hiddenField) {
				this.hiddenField.value = node.id;
			}
			Ext.form.ComboBox.superclass.setValue.call(this, text);
			this.value = node.id;
		}
	},

	getValue : function() {
		return typeof this.value != 'undefined' ? this.value : '';
	},

	getNode : function() {
		return this.node;
	},

	clearValue : function() {
		Ext.ux.ComboBoxTree.superclass.clearValue.call(this);
		this.node = null;
	},

	onDestroy : function() {
		Ext.destroy(this.view, this.dropLayer);
		Ext.form.DropTree.superclass.onDestroy.call(this);
	},

	onEnable : function() {
		Ext.form.DropTree.superclass.onEnable.apply(this, arguments);
		if (this.hiddenField) {
			this.hiddenField.disabled = false;
		}
	},

	onDisable : function() {
		Ext.form.DropTree.superclass.onDisable.apply(this, arguments);
		if (this.hiddenField) {
			this.hiddenField.disabled = true;
		}
	},

	loadStore : function(store) {
		this.setStore(store);
		this.initDropTree();
		this.setDefaultChecked();
	},

	setStore : function(store) {
		this.store = store;
	},

	getStore : function() {
		return this.store;
	}

});
Ext.reg("droptree", Ext.form.DropTree);

function gridSpan(grid, rowOrCol, cols, sepCol) {
	var array1 = new Array();
	var arraySep = new Array();
	var count1 = 0;
	var count2 = 0;
	var index1 = 0;
	var index2 = 0;
	var aRow = undefined;
	var preValue = undefined;
	var firstSameCell = 0;
	var allRecs = grid.getStore().getRange();
	if (rowOrCol == "row") {
		count1 = grid.getColumnModel().getColumnCount();
		count2 = grid.getStore().getCount();
	} else {
		count1 = grid.getStore().getCount();
		count2 = grid.getColumnModel().getColumnCount();
	}
	for (i = 0; i < count1; i++) {
		if (rowOrCol == "row") {
			var curColName = grid.getColumnModel().getDataIndex(i);
			var curCol = "[" + curColName + "]";
			if (cols.indexOf(curCol) < 0)
				continue;
		}
		preValue = undefined;
		firstSameCell = 0;
		array1[i] = new Array();
		for (j = 0; j < count2; j++) {
			if (rowOrCol == "row") {
				index1 = j;
				index2 = i;
			} else {
				index1 = i;
				index2 = j;
			}
			var colName = grid.getColumnModel().getDataIndex(index2);
			if (sepCol && colName == sepCol)
				arraySep[index1] = allRecs[index1].get(sepCol);
			var seqOldValue = seqCurValue = "1";
			if (sepCol && index1 > 0) {
				seqOldValue = arraySep[index1 - 1];
				seqCurValue = arraySep[index1];
			}

			if (allRecs[index1].get(colName) == preValue && (colName == sepCol || seqOldValue == seqCurValue)) {
				// alert(colName + "======" + seqOldValue + "======" +
				// seqCurValue);
				allRecs[index1].set(colName, "");
				array1[i].push(j);
				if (j == count2 - 1) {
					var index = firstSameCell + Math.round((j + 1 - firstSameCell) / 2 - 1);
					if (rowOrCol == "row") {
						allRecs[index].set(colName, preValue);
					} else {
						allRecs[index1].set(grid.getColumnModel().getColumnId(index), preValue);
					}
				}
			} else {
				if (j != 0) {
					var index = firstSameCell + Math.round((j + 1 - firstSameCell) / 2 - 1);
					if (rowOrCol == "row") {
						allRecs[index].set(colName, preValue);
					} else {
						allRecs[index1].set(grid.getColumnModel().getColumnId(index), preValue);
					}
				}
				firstSameCell = j;
				preValue = allRecs[index1].get(colName);
				allRecs[index1].set(colName, "");
				if (j == count2 - 1) {
					allRecs[index1].set(colName, preValue);
				}
			}
		}
	}
	grid.getStore().commitChanges();
	var rCount = grid.getStore().getCount();
	for (i = 0; i < rCount; i++) {
		for (j = 0; j < grid.getColumnModel().getColumnCount(); j++) {
			aRow = grid.getView().getCell(i, j);
			if (i == 0) {
				aRow.style.borderTop = "none";
				aRow.style.borderRight = "1px solid #ccc";
			} else if (i == rCount - 1) {
				aRow.style.borderTop = "1px solid #ccc";
				aRow.style.borderRight = "1px solid #ccc";
				aRow.style.borderBottom = "1px solid #ccc";
			} else {
				aRow.style.borderTop = "1px solid #ccc";
				aRow.style.borderRight = "1px solid #ccc";
			}
			if (j == grid.getColumnModel().getColumnCount() - 1)
				aRow.style.borderRight = "1px solid #ccc";
			if (i == rCount - 1)
				aRow.style.borderBottom = "1px solid #ccc";
		}
	}
	// 去锟斤拷锟较诧拷锟侥碉拷元锟斤拷姆指锟斤拷锟��
	for (i = 0; i < array1.length; i++) {
		if (!Ext.isEmpty(array1[i])) {
			for (j = 0; j < array1[i].length; j++) {
				if (rowOrCol == "row") {
					aRow = grid.getView().getCell(array1[i][j], i);
					aRow.style.borderTop = "none";
				} else {
					aRow = grid.getView().getCell(i, array1[i][j]);
					aRow.style.borderLeft = "none";
				}
			}
		}
	}
}
Ext.CirButton = Ext.extend(Ext.BoxComponent, {
	style : "",
	clickEvent : "click",
	disabled : false,
	disabledClass : "x-item-disabled",
	size : "normal",
	initComponent : function() {
		Ext.CirButton.superclass.initComponent.call(this);
		this.addEvents("click");
	},
	onRender : function(c, a) {
		if (!this.el) {
			var b = this.getAutoCreate();
			if (!b.name) {
				b.name = this.name || this.id
			}
			if (this.inputType) {
				b.type = this.inputType
			}
			this.autoEl = b;
		}
		Ext.form.Field.superclass.onRender.call(this, c, a);
		var d = Ext.getDom(this.id);
		var clses = this.iconCls ? this.iconCls + ' l-btn-icon-left' : ' l-btn-icon-mid';
		if (this.size != "normal") {
			clses = " l-btn-icon-l-btn-icon-small"
		}
		d.innerHTML = '<A class="l-btn" href="javascript:"><SPAN class="l-btn-left"><SPAN class="l-btn-text  ' + clses + '">' + this.text + '</SPAN></A>';

		this.onDisableChange(this.disabled);

		this.mon(this.el, this.clickEvent, this.onClick, this);
	},
	onDisableChange : function(a) {
		if (this.el) {
			if (!Ext.isIE6 || !this.text) {
				this.el[a ? "addClass" : "removeClass"](this.disabledClass)
			}
			this.el.dom.disabled = a
		}
		this.disabled = a
	},
	onClick : function(a) {
		if (a) {
			a.preventDefault()
		}
		if (!this.disabled) {
			this.toggle();
			this.fireEvent("click", this, a);
			if (this.handler) {
				this.handler.call(this.scope || this, this, a)
			}
		}
	},
	toggle : function(b, a) {
		this.el[b ? "addClass" : "removeClass"]("x-btn-pressed")
		return this
	},
	enable : function() {
		this.onDisableChange(false)
	},
	disable : function(a) {
		this.onDisableChange(true)
	}
});
Ext.reg("cirbutton", Ext.CirButton);
/** *******自定义下啦gird******** */
Ext.form.DropUserGirdField = Ext.extend(Ext.form.TriggerField, {
	cls : "x-combo-list",
	shadow : "sides",
	align : "tl-bl?",
	lazyInit : false,
	minWidth : 90,
	maxHeight : 400,
	minHeight : 300,
	width : 120,
	height : 400,
	editable : false,
	triggerClass : "x-form-function-trigger",
	store : null,
	hiddenValue : {},
	initComponent : function() {
		Ext.form.DropUserGirdField.superclass.initComponent.call(this);
		// Ext.form.DropUserGirdFieldd.superclass.initComponent.call(this);
		this.addEvents("rowclick");
	},
	getValue : function(hiddenValue) {
		return this.hiddenValue;
	},
	setValue : function(text) {
		Ext.form.DropUserGirdField.superclass.setValue.call(this, text);
		return this;
	},
	onTriggerClick : function() {
		if (this.menu == null) {
			if (!this.store) {
				this.store = new Ext.data.JsonStore({
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
					} ]
				});
			}

			this.sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
			this.viewgrid = new Ext.grid.GridPanel({
				columnLines : true,
				width : 350,
				height : 200,
				frame : false,
				header : false,
				region : 'center',
				store : this.store,
				cm : new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
					header : '用户名',
					dataIndex : 'USERNAME',
					sortable : false,
					width : 100,
					align : 'center'
				}, {
					header : '所属部门',
					dataIndex : 'ORGANIZATIONIDNAME',
					sortable : false,
					width : 100,
					align : 'center'
				} ]),
				bbar : new Ext.PagingToolbar({
					store : this.store,
					pageSize : 15,
					displayInfo : false
				}),
				sm : this.sm,
				view : new Ext.ux.grid.BufferView({
					// rowHeight : 30,
					scrollDelay : false
				}),
				loadMask : {
					msg : '正在努力加载,请稍后....'
				},
				listeners : {
					'render' : function(grid) {
						// this.store.setBaseParam('KEYNAME',
						// Ext.getCmp('S_KEYNAME').getValue());
						// this.store.setBaseParam('STATUS', 1);
						this.store.on("beforeload", function(thiz, options) {
							// thiz.baseParams["ORGANIZATIONID"] =
							// Ext.getCmp("droptreeSearch").getValue();
							// alert(Ext.getCmp('S_KEYNAME').getValue())
							thiz.baseParams["KEYNAME"] = Ext.getCmp('S_KEYNAME').getValue();
							thiz.baseParams["STATUS"] = 1;
						});
						this.reloadData();
					},
					'rowclick' : function(grid, rowindex, e) {
						var sm = this.viewgrid.getSelectionModel();
						// var cellIndex = sm.getSelected();
						var record = sm.getSelected();
						this.setValue(record.data.USERNAME);
						// var obj={};
						this.hiddenValue.USERNAME = record.data.USERNAME;
						this.hiddenValue.ORGANIZATIONID = record.data.ORGANIZATIONID;
						this.hiddenValue.USERID = record.data.USERID;
						this.fireEvent('rowclick', this, record);
						this.menu.hide();
					},
					scope : this
				},
				tbar : [ {
					text : '用户名'
				}, new Ext.form.TextField({
					id : 'S_KEYNAME',
					width : 100,
					allowBlank : true
				}), {
					text : "搜 索",
					iconCls : 'icon-utils-s-search',
					handler : function() {
						this.store.load({
							params : {
								start : 0,
								limit : 15
							}
						});
					},
					scope : this
				} ]
			});

			this.menu = new Ext.menu.Menu({
				scope : this,
				items : this.viewgrid
			});

		}
		if (this.disabled) {
			return;
		}
		this.onFocus();

		this.menu.show(this.el, "tl-bl?");
	},
	reloadData : function() {
		this.store.load({
			params : {
				start : 0,
				limit : 15
			}
		});
	}
});
Ext.reg('usergirdfield', Ext.form.DropUserGirdField);

/** *******自定义下啦gird******** */
Ext.form.DropOrgCheckField = Ext.extend(Ext.form.TriggerField, {
	cls : "x-combo-list",
	shadow : "sides",
	align : "tl-bl?",
	lazyInit : false,
	minWidth : 90,
	maxHeight : 400,
	minHeight : 300,
	width : this.width || 120,
	height : 300,
	editable : false,
	triggerClass : "x-form-function-trigger",
	store : null,
	hiddenValue : {},
	initComponent : function() {
		Ext.form.DropOrgCheckField.superclass.initComponent.call(this);
		// Ext.form.DropUserGirdFieldd.superclass.initComponent.call(this);
		// this.addEvents("rowclick");
	},
	getValue : function(hiddenValue) {
		return this.hiddenValue;
	},
	setValue : function(text) {
		Ext.form.DropOrgCheckField.superclass.setValue.call(this, text);
		return this;
	},
	onTriggerClick : function() {
		// alert(2222)
		if (this.menu == null) {
			if (!this.store) {
				this.store = new Ext.data.JsonStore({
					url : 'userAction!getUserOrgList.do',
					totalProperty : "total",
					root : 'rows',
					fields : [ {
						name : "USERID",
						type : "string"
					}, {
						name : "ORGANIZATIONID",
						type : "string"
					}, {
						name : "TEXT",
						type : "string"
					} ]
				});
			}
			// alert(5555)
			var sm = new Ext.grid.CheckboxSelectionModel();
			this.viewgrid = new Ext.grid.GridPanel({
				columnLines : true,
				width : 220,
				height : 300,
				frame : false,
				header : false,
				region : 'center',
				store : this.store,
				cm : new Ext.grid.ColumnModel([ sm, {
					header : '所属部门',
					dataIndex : 'TEXT',
					sortable : false,
					width : 170,
					align : 'center'
				} ]),
				hideHeaders : true,
				sm : sm,
				loadMask : {
					msg : '正在努力加载,请稍后....'
				},
				listeners : {
					'render' : function(grid) {

						this.store.on("beforeload", function(thiz, options) {

						});
						this.reloadData();

					},
					scope : this
				},
				buttonAlign : 'center',
				tbar : [ '->', '-', {
					text : "确定",
					iconCls : 'icon-system-refresh',
					handler : function() {
						var records = sm.getSelections();
						var ids = ""
						var text = "";
						for (var i = 0; i < records.length; i++) {
							var record = records[i];
							if (i == 0) {
								ids = ids + record.data.ORGANIZATIONID;
								text = text + record.data.TEXT;
							} else {
								ids = ids + "," + record.data.ORGANIZATIONID;
								text = text + "," + record.data.TEXT;
							}
						}
						this.setValue(text, ids);
						this.hiddenValue.TEXT = text;
						this.hiddenValue.IDS = ids;

						this.menu.hide();
					},
					scope : this
				}, '-' ]
			});
			this.menu = new Ext.menu.Menu({
				scope : this,
				items : this.viewgrid
			});

		}
		if (this.disabled) {
			return;
		}
		// alert(this.hiddenValue.IDS)
		this.onFocus();
		this.menu.show(this.el, "tl-bl?");
		// alert(777)
		// alert(234)
		// this.reloadData();
	},
	reloadData : function() {
		// alert(2222222)
		// alert(2222)
		this.store.load();
	}
});
Ext.reg('orgcheckgirdfield', Ext.form.DropOrgCheckField);

/** ******** */
Ext.form.DropOrgCheckALLField = Ext.extend(Ext.form.TriggerField, {
	cls : "x-combo-list",
	shadow : "sides",
	align : "tl-bl?",
	lazyInit : false,
	minWidth : 90,
	maxHeight : 400,
	minHeight : 300,
	width : this.width || 120,
	height : 300,
	editable : false,
	triggerClass : "x-form-function-trigger",
	store : null,
	hiddenValue : {},
	initComponent : function() {
		Ext.form.DropOrgCheckALLField.superclass.initComponent.call(this);
	},
	getValue : function() {
		return this.hiddenValue;
	},
	setValue : function(text, ids) {
		Ext.form.DropOrgCheckField.superclass.setValue.call(this, text);
		this.hiddenValue = new Object();
		this.hiddenValue.IDS = ids;
		this.hiddenValue.TEXT = text;
		// alert(this.hiddenValue)
		return this;
	},
	onTriggerClick : function() {
		if (this.menu == null) {
			if (!this.store) {
				this.store = new Ext.data.JsonStore({
					url : 'getOrganizationGridList.do?PARENID=0',
					totalProperty : "total",
					root : 'rows',
					fields : [ {
						name : "organizationId",
						type : "string"
					}, {
						name : "text",
						type : "string"
					} ]
				});
			}
			var sm = new Ext.grid.CheckboxSelectionModel();
			this.viewgrid = new Ext.grid.GridPanel({
				columnLines : true,
				width : 200,
				height : 300,
				frame : false,
				header : false,
				region : 'center',
				store : this.store,
				cm : new Ext.grid.ColumnModel([ sm, {
					header : '',
					dataIndex : 'text',
					sortable : false,
					width : 170,
					align : 'center'
				} ]),
				// hideHeaders : true,
				sm : sm,
				loadMask : {
					msg : '正在努力加载,请稍后....'
				},
				listeners : {
					'render' : function(grid) {

						this.store.on("beforeload", function(thiz, options) {

						});
						this.reloadData();

					},
					scope : this
				},
				buttonAlign : 'center',
				tbar : [ '->', '-', {
					text : "确定",
					iconCls : 'icon-system-refresh',
					handler : function() {
						var records = sm.getSelections();
						var ids = ""
						var text = "";
						for (var i = 0; i < records.length; i++) {
							var record = records[i];
							if (i == 0) {
								ids = ids + record.data.organizationId;
								text = text + record.data.text;
							} else {
								ids = ids + "," + record.data.organizationId;
								text = text + "," + record.data.text;
							}
						}
						this.setValue(text, ids);
						this.hiddenValue.TEXT = text;
						this.hiddenValue.IDS = ids;

						this.menu.hide();
					},
					scope : this
				}, '-' ]
			});
			this.menu = new Ext.menu.Menu({
				scope : this,
				items : this.viewgrid
			});

		}
		if (this.disabled) {
			return;
		}
		// alert(this.hiddenValue.IDS)
		this.onFocus();
		this.reloadData();

		this.menu.show(this.el, "tl-bl?");

	},
	reloadData : function() {
		var me = this;
		this.store.load({
			callback : function(records, options, success) {
				if (me.hiddenValue != null && me.hiddenValue.IDS != null) {
					var array = me.hiddenValue.IDS.split(",");
					for (var i = 0; i < me.store.getTotalCount(); i++) {
						var rec = me.store.getAt(i);
						for (var j = 0; j < array.length; j++) {
							// alert(array[i] == rec.data.organizationId);
							if (array[j] == rec.data.organizationId) {
								// alert(array[i] == rec.data.organizationId);
								me.viewgrid.getSelectionModel().selectRow(i, true);
							}
						}
					}
				}

			}
		});
	}
});
Ext.reg('orgcheckgirdALLfield', Ext.form.DropOrgCheckALLField);
