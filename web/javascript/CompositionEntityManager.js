
function setEntityInclude() {
	var entityType = document.getElementById("peType").value;
	CompositionEntityManager.getEntityIncludePage(entityType, populatePage);
}
function populatePage(pageData) {
	dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
}

