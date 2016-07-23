function addTab(windowsId, title, url, icon, closable) {
	if (!$('#' + windowsId).tabs('exists', title)) {
		$('#' + windowsId).tabs('add', {
			title : title,
			content : createFrame(url),
			closable : closable,
			icon : icon
		});
	} else {
		$('#' + windowsId).tabs('select', subtitle);
	}
}
function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
			+ '" style="width:100%;height:100%;"></iframe>';
	return s;
}
