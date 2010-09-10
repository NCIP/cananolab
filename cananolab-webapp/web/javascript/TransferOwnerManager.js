
function showMatchedUserDropdown(userLoginName, styleId,cancelStyleId) {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide(styleId);
	hide(cancelStyleId);
	var selected = dwr.util.getValue(styleId);
	var loginName = dwr.util.getValue(userLoginName);
	
	AccessibilityManager.getUsers( function(data) {
		dwr.util.removeAllOptions(styleId);
		dwr.util.addOptions(styleId, data, "loginName",
				"fullName");
		dwr.util.setValue(styleId, selected);
		hide("loaderImg");
		show(styleId);
		show(cancelStyleId);
	});
}

function updateOwnerLoginName(ownerLoginName, styleId, cancelStyleId) {
	var selected = dwr.util.getValue(styleId);
	dwr.util.setValue(ownerLoginName, selected);
	hide(styleId);
	hide(cancelStyleId);
}

function cancelBrowseSelect(styleId, cancelStyleId) {
	hide(styleId);
	hide(cancelStyleId);
}
