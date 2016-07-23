var ds;
var grid;
var deployWin;
function init(){
    Ext.QuickTips.init();
    var cm = new Ext.grid.ColumnModel([{
        header: '查询结果',
        width: 500,
        sortable: true,
        dataIndex: 'roleName'
    }]);
    tb = new Ext.Toolbar();
    tb.add('-');
    tb.addButton([{
    
        id: "updategroup",
        text: "修改",
        handler: alert(1),
        scale: 'small',
        iconCls: "update"
    }]);
    ds = new Ext.data.JsonStore({
        url: 'searchAction!searchResult.do',
        totalProperty: "totalCount",
        root: 'results',
        searchTime: 'searchTime',
        fields: [{
            name: "entryClassPK",
            type: "string"
        }, {
            name: "entryClassName",
            type: "string"
        }, {
            name: "creatorName",
            type: "string"
        }, {
            name: "createDate",
            type: "string"
        }, {
            name: "casePlace",
            type: "string"
        }, {
            name: "roleName",
            type: "string"
        }, {
            name: "imageId",
            type: "string"
        }, {
            name: "indexSuspect",
            type: "string"
        }, {
            name: "indexApplication",
            type: "string"
        }, {
            name: "indexComment",
            type: "string"
        }, {
            name: "categoryNames",
            type: "string"
        }, {
            name: "indexAttention",
            type: "string"
        }]
    });
    grid = new Ext.grid.GridPanel({
        ds: ds,
        cm: cm,
        tbar: tb,
        hideHeaders: true,
        viewConfig: {
            forceFit: true
        },
        title: '搜索结果',
        collapsible: false,
        el: "mygrid",
        region: "center"
    
    });
    
    new Ext.Viewport({ // 注意这里是Viewport，NO ViewPort
        enableTabScroll: true,
        layout: "border",
        items: [grid]
    });
}

function onItemAddCheck(){
    //document.getElementById("inputFile").value = "";
    if (deployWin == null) {
        deployWin = _window('window-win-deploy');
        deployWin.height = 105;
        deployWin.addButton('确定', doSaveItem);
        deployWin.addButton('取消', docolse);
    }
    deployWin.show();
    
}
function doSaveItem(){
	  Ext.Ajax.request({
        url: 'upload.jsp',
        isUpload : true,
        form: "upForm",
        success: function(response, options){
        }
           
    });
   
}

function docolse(){
    deployWin.hide();
}



