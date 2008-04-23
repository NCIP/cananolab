
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

function displayModality(compEleIndex, functionIndex) {
	var functionType = document.getElementById("funcType_" + compEleIndex + "_" + functionIndex).value;
	var modalityTd = document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex);
	if(functionType == "imaging") {
		modalityTd.style.display = "inline";
	} else {
		modalityTd.style.display = "none";
	}
	return false;
}


/*
 * the following functions using AJAX to display modality dropdown menu in the 
 * bodyNanoparticleEntityUpdate.jsp and bodyFunctionUpdate.jsp
 *
 */
/*
function setModalityTypeOptions(compEleIndex, functionIndex) {
	var functionType = dwr.util.getValue("funcType_" + compEleIndex + "_" + functionIndex);
	CompositionEntityManager.getModalityTypeOptions(functionType, function(data) {
			
			dwr.util.removeAllOptions("modalityType_" + compEleIndex + "_" + functionIndex);
			dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, ['']);
    		dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, data);
    		dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, ['[Other]']);
  	});
}

function setModalityInclude(compEleIndex, functionIndex) {
	var functionType = dwr.util.getValue("funcType_" + compEleIndex + "_" + functionIndex);
	
	CompositionEntityManager.getModalityIncludePage(compEleIndex, functionIndex, functionType, function(pageData) {
	
		document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex).innerHTML = "";
		dwr.util.setValue("modalityTypeTd_" + compEleIndex + "_" + functionIndex, pageData, {escapeHtml:false});
		
	});
	
  	if(functionType != "imaging") {
  		document.getElementById("modalityType_" + compEleIndex + "_" + functionIndex).innerHTML =
  			"&nbsp;";
  	}
}
*/
