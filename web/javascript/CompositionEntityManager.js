
function setEntityInclude() {
	var entityType = document.getElementById("peType").value;
	CompositionEntityManager.getEntityIncludePage(entityType, populatePage);
}
function populatePage(pageData) {
	dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
}

function getComposingElementOptions(particleEntityTypeId,
									compEleTypeId) {
	var compFuncTypeValue = dwr.util.getValue(particleEntityTypeId);
	
	CompositionEntityManager.getComposingElementTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions(compEleTypeId);
			dwr.util.addOptions(compEleTypeId, ['']);
    		dwr.util.addOptions(compEleTypeId, data);
    		dwr.util.addOptions(compEleTypeId, ['[Other]']);
  	});
}