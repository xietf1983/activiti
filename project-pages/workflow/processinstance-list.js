var ds;
var grid;
var deployWin;
function init(){
    Ext.QuickTips.init();
    var cm = new Ext.grid.ColumnModel([{
        header: '����',
        dataIndex: 'NAME_',
        sortable: false,
        align: 'center'
    }, {
        header: '����ʱ��',
        sortable: true,
        dataIndex: 'START_TIME_SHOWVALUE',
        sortable: false,
        align: 'center'
    }, {
        header: '����ʱ��',
        dataIndex: 'END_TIME_SHOWVALUE',
        sortable: false,
        align: 'center'
    }, {
        header: '����ʱ��',
        dataIndex: 'DURATION_SHOWVALUE',
        sortable: false,
        renderer: renderer,
        align: 'center'
    }, {
        header: '������',
        dataIndex: 'START_USER_SHOWVALUE',
        sortable: false,
        renderer: renderer,
        align: 'center'
    }, {
        header: '���̼��',
        dataIndex: 'START_USER_SHOWVALUE',
        sortable: false,
        renderer: renderer,
        align: 'center'
    }]);
    tb = new Ext.Toolbar(['->',{
        text: '�鿴',
        handler: onItemView, // assign menu by instanc
        iconCls: 'icon-system-view'
    },'-',{
        id: "updategroup",
        text: "ɾ��",
        handler: onItemDeleteCheck,
        iconCls: "icon-utils-s-delete"
    }]);
    ds = new Ext.data.JsonStore({
        url: 'workflowAction!getDeploymentDef.do',
        totalProperty: "totalCount",
        root: 'results',
        fields: [{
            name: "ID_",
            type: "string"
        }, {
            name: "NAME_",
            type: "string"
        }, {
            name: "DEPLOY_TIME_SHOWVALUE",
            type: "string"
        }, {
            name: "VERSION_",
            type: "string"
        }, {
            name: "DEPLOYMENT_ID_",
            type: "string"
        }, {
            name: "RESOURCE_NAME_",
            type: "string"
        }, {
            name: "DGRM_RESOURCE_NAME_",
            type: "string"
        }, {
            name: "indexSuspect",
            type: "string"
        }]
    });
    grid = new Ext.grid.GridPanel({
        enableColumnMove: false,
        enableHdMenu: false,
        ds: ds,
        cm: cm,
        tbar: tb,
        viewConfig: {
            forceFit: true
        },
        collapsible: false,
        el: "mygrid",
        region: "center"
    
    });
    
    new Ext.Viewport({ // ע��������Viewport��NO ViewPort
        enableTabScroll: true,
        layout: "border",
        items: [grid]
    });
}

function onItemView(){
    document.getElementById("inner").innerHTML = '<input type="file" id="inputFile" name="inputFile" style="height:22px;">';
    if (deployWin == null) {
        deployWin = _window('window-win-deploy');
        deployWin.height = 105;
        deployWin.addButton('ȷ��', doSaveItem);
        deployWin.addButton('ȡ��', docolse);
    }
    deployWin.show();
    
}

function doSaveItem(){
    Ext.Ajax.request({
        url: '../uploadFile?uploadType=deployProcess',
        isUpload: true,
        form: "upForm",
        success: function(data, options){
            _alert("���̷����ɹ�");
            docolse();
            ds.load();
        }
    });
}


function onItemDeleteCheck(){
    var list = grid.getSelectionModel().getSelections();
    if (list.length > 0) {
        _confirm('ɾ���󲻿ɻָ�����ȷ��Ҫɾ����', deleteUserConFirm);
    }
    else {
        _alert("��ѡ��Ҫɾ��������");
    }
    
    
    
}

function deleteUserConFirm(btn){
    if (btn == 'no') {
        return false;
    }
    if (btn == 'yes') {
        var selectedRows = grid.getSelectionModel().getSelections();
        if (selectedRows.length < 1) {
            _alert("��ѡ��Ҫɾ�������̶���");
            return;
        }
        else {
            var buf = "[";
            for (var i = 0; i < selectedRows.length; i++) {
                if (i == selectedRows.length - 1) {
                    buf = buf + '{"ID":"' + selectedRows[i].data['DEPLOYMENT_ID_'] + '"}'
                }
                else {
                    buf = buf + '{"ID":"' + selectedRows[i].data['DEPLOYMENT_ID_'] + '"},'
                }
            }
            var buf = buf + "]";
            Ext.Ajax.request({
                url: 'workflowAction!deleteDeployment.do',
                method: 'post',
                params: {
                    ROWS: buf
                },
                success: function(data, options){
                    ds.load();
                }
            });
        }
    }
}

function docolse(){
    deployWin.hide();
}

function renderer(value, p, record, rowIndex){
    var deploymentId = record.data.DEPLOYMENT_ID_;
    var resourceName = record.data.RESOURCE_NAME_;
    var buffer = "<a target='_blank' href='workflowAction!loadByDeployment.do?deploymentId='" + deploymentId + "&resourceName=" + resourceName + "><img src src='../images/pkg.gif'/></a>";
    return buffer;
}




