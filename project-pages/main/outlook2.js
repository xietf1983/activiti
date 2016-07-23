function addTab(windowsId, title, url, icon, closable) {
	alert(closable)
	if (!$('#' + windowsId).tabs('exists', title)) {
		var var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>'; 
		$('#' + windowsId).tabs('add', {
			title : title,
			content : content,
			closable : closable,
			icon : icon
		});
	} else {
		$('#' + windowsId).tabs('select', title);
	}
}

