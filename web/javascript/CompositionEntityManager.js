
function setEntityInclude(selectEleId) {
	var entityType = document.getElementById(selectEleId).value;
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

function getBiopolymerOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if(compFuncTypeValue == 'biopolymer') {
		CompositionEntityManager.getBiopolymerTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions("biopolymerType");
			dwr.util.addOptions("biopolymerType", ['']);
    		dwr.util.addOptions("biopolymerType", data);
    		dwr.util.addOptions("biopolymerType", ['[Other]']);
  		});
  	}
}

function getAntibodyTypeOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if(compFuncTypeValue == 'antibody') {
		CompositionEntityManager.getAntibodyTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions("antibodyType");
			dwr.util.addOptions("antibodyType", ['']);
    		dwr.util.addOptions("antibodyType", data);
    		dwr.util.addOptions("antibodyType", ['[Other]']);
  		});
  	}
}

function getAntibodyIsotypeOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if(compFuncTypeValue == 'antibody') {
		CompositionEntityManager.getAntibodyIsotypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions("antibodyIsotype");
			dwr.util.addOptions("antibodyIsotype", ['']);
    		dwr.util.addOptions("antibodyIsotype", data);
    		dwr.util.addOptions("antibodyIsotype", ['[Other]']);
  		});
  	}
}

function getFETypesOptions(selectEleId) {
	getBiopolymerOptions(selectEleId);
	getAntibodyTypeOptions(selectEleId);
	getAntibodyIsotypeOptions(selectEleId);
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

function displayFEModality(functionIndex) {
	var functionType = document.getElementById("funcType_" + functionIndex).value;
	var modalityDiv = document.getElementById("modalityDiv_" + functionIndex);
	var modalityStrong = document.getElementById("modalityStrong_" + functionIndex);
	if(functionType == "imaging") {
		modalityDiv.style.display = "inline";
		modalityStrong.style.display = "inline";
	} else {
		modalityDiv.style.display = "none";
		modalityStrong.style.display = "none";
	}
	return false;
}

function displayTarget(functionIndex) {
	var functionType = document.getElementById("funcType_" + functionIndex).value;
	var targetSpan = document.getElementById("targetSpan_" + functionIndex);
	var targetDiv = document.getElementById("targetDiv_" + functionIndex);
	if(functionType == "targeting") {
		targetSpan.style.display = "block";
		targetDiv.style.display = "block";
	} else {
		targetSpan.style.display = "none";
		targetDiv.style.display = "none";
	}
	return false;
}

function displayModality(compEleIndex, functionIndex) {
	var functionType = document.getElementById("targetType_" + compEleIndex + "_" + functionIndex).value;
	var modalityTd = document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex);
	if(functionType == "imaging") {
		modalityTd.style.display = "inline";
	} else {
		modalityTd.style.display = "none";
	}
	return false;
}

function displayAntigenSpecies(parentIndex, childIndex) {
	var type = document.getElementById("targetType_" + parentIndex + "_" + childIndex).value;
	var sdiv = document.getElementById("speciesDiv_" + parentIndex + "_" + childIndex);
	var removeSpan = document.getElementById("removeSpan_" + parentIndex + "_" + childIndex);
	if(type == "antigen") {
		sdiv.style.display = "inline";
	} else {
		sdiv.style.display = "none";
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
