
function setEntityInclude() {
	var entityType = document.getElementById("peType").value;
	CompositionEntityManager.getEntityIncludePage(entityType, populatePage);
}
function populatePage(pageData) {
	dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
}

function getComposingElementOptions() {
	var compFuncTypeValue = dwr.util.getValue("peType");
	
	CompositionEntityManager.getComposingElementTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions("compElemType");
			dwr.util.addOptions("compElemType", ['']);
    		dwr.util.addOptions("compElemType", data);
    		dwr.util.addOptions("compElemType", ['[Other]']);
  	});
}

function getBiopolymerOptions() {
	var compFuncTypeValue = dwr.util.getValue("peType");
	
	CompositionEntityManager.getBiopolymerTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions("biopolymerType");
			dwr.util.addOptions("biopolymerType", ['']);
    		dwr.util.addOptions("biopolymerType", data);
    		dwr.util.addOptions("biopolymerType", ['[Other]']);
  	});
}