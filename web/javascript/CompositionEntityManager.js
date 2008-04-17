
function setEntityInclude() {
	var entityType = document.getElementById("entity.type").value;
	CompositionEntityManager.getEntityIncludePage(entityType, populatePage);
}
function populatePage(pageData) {
	dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
}

