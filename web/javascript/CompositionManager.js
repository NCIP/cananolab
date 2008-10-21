
function setEntityInclude(selectEleId, pagePath) {
	var entityType = document.getElementById(selectEleId).value;
	var inclueBlock = document.getElementById("entityInclude");
	if (entityType == "metal particle" || entityType == "quantum dot") {
		inclueBlock.style.display = "none";
	} else {
		inclueBlock.style.display = "inline";
		CompositionManager.getEntityIncludePage(entityType, pagePath, populatePage);
	}
}
function populatePage(pageData) {
	dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
}
function getComposingElementOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	CompositionManager.getComposingElementTypeOptions(compFuncTypeValue, function (data) {
		dwr.util.removeAllOptions("compElemType");
		dwr.util.addOptions("compElemType", [""]);
		dwr.util.addOptions("compElemType", data);
		dwr.util.addOptions("compElemType", ["[Other]"]);
	});
}
function getBiopolymerOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "biopolymer") {
		CompositionManager.getBiopolymerTypeOptions(compFuncTypeValue, {callback:function (data) {
			dwr.util.removeAllOptions("biopolymerType");
			dwr.util.addOptions("biopolymerType", [""]);
			dwr.util.addOptions("biopolymerType", data);
			dwr.util.addOptions("biopolymerType", ["[Other]"]);
		}, async:true});
	}
}
function getWallTypeOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "carbon nanotube") {
		CompositionManager.getWallTypeOptions(compFuncTypeValue, {callback:function (data) {
			var ele = document.getElementById("wallType");
			dwr.util.removeAllOptions("wallType");
			dwr.util.addOptions("wallType", [""]);
			dwr.util.addOptions("wallType", data);
		}, async:true});
	}
}
function getDiameterUnitOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "carbon nanotube") {
		CompositionManager.getCarbonNanotubeDiameterUnitOptions(compFuncTypeValue, {callback:function (data) {
			var ele = document.getElementById("diameterUnit");
			dwr.util.removeAllOptions("diameterUnit");
			dwr.util.addOptions("diameterUnit", [""]);
			dwr.util.addOptions("diameterUnit", data);
			dwr.util.addOptions("diameterUnit", ["[Other]"]);
		}, async:true});
	}
}
function getAverageLengthUnitOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "carbon nanotube") {
		CompositionManager.getCarbonNanotubeDiameterUnitOptions(compFuncTypeValue, {callback:function (data) {
			var ele = document.getElementById("averageLengthUnit");
			dwr.util.removeAllOptions("averageLengthUnit");
			dwr.util.addOptions("averageLengthUnit", [""]);
			dwr.util.addOptions("averageLengthUnit", data);
			dwr.util.addOptions("averageLengthUnit", ["[Other]"]);
		}, async:true});
	}
}
function getAverageDiameterUnitOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "fullerene") {
		CompositionManager.getFullereneAverageDiameterUnitOptions(compFuncTypeValue, {callback:function (data) {
			var ele = document.getElementById("averageDiameterUnit");
			dwr.util.removeAllOptions("averageDiameterUnit");
			dwr.util.addOptions("averageDiameterUnit", [""]);
			dwr.util.addOptions("averageDiameterUnit", data);
			dwr.util.addOptions("averageDiameterUnit", ["[Other]"]);
		}, async:true});
	}
}
var timeoutInterval = 500;
function getNETypeOptions() {
	window.setTimeout("getNETypes()", timeoutInterval);
}
function getNETypes() {
	var selectEleId = "peType";
	
	getBiopolymerOptions(selectEleId);
	getWallTypeOptions(selectEleId);
	getComposingElementOptions(selectEleId);
	getDiameterUnitOptions(selectEleId);
	getAverageLengthUnitOptions(selectEleId);
	getAverageDiameterUnitOptions(selectEleId);
	setEntityTypeTitle(selectEleId);
}
function getAntibodyTypeOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "antibody") {
		CompositionManager.getAntibodyTypeOptions(compFuncTypeValue, {callback:function (data) {
			dwr.util.removeAllOptions("antibodyType");
			dwr.util.addOptions("antibodyType", [""]);
			dwr.util.addOptions("antibodyType", data);
			dwr.util.addOptions("antibodyType", ["[Other]"]);
		}, async:true});
	}
}
function getAntibodyIsotypeOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "antibody") {
		CompositionManager.getAntibodyIsotypeOptions(compFuncTypeValue, {callback:function (data) {
			dwr.util.removeAllOptions("antibodyIsotype");
			dwr.util.addOptions("antibodyIsotype", [""]);
			dwr.util.addOptions("antibodyIsotype", data);
			dwr.util.addOptions("antibodyIsotype", ["[Other]"]);
		}, async:true});
	}
}
function getAntibodySpeciesOptions(selectEleId) {
	var compFuncTypeValue = dwr.util.getValue(selectEleId);
	if (compFuncTypeValue == "antibody") {
		CompositionManager.getAntibodySpeciesOptions(compFuncTypeValue, function (data) {
			dwr.util.removeAllOptions("antibodySpecies");
			dwr.util.addOptions("antibodySpecies", [""]);
			dwr.util.addOptions("antibodySpecies", data);
		});
	}
}
function setEntityTypeTitle(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	document.getElementById("entityTypeTitle").innerHTML = selectEle.options[selectEle.options.selectedIndex].text;
}
function getFETypeOptions() {
	window.setTimeout("getFETypes()", timeoutInterval);
}
function getFETypes() {
	var selectEleId = "feType";
	getBiopolymerOptions(selectEleId);
	getAntibodyTypeOptions(selectEleId);
	getAntibodyIsotypeOptions(selectEleId);
	getAntibodySpeciesOptions(selectEleId);
	setEntityTypeTitle(selectEleId);
}
function displayFEModality(functionIndex) {
	var functionType = document.getElementById("funcType_" + functionIndex).value;
	var modalityDiv = document.getElementById("modalityDiv_" + functionIndex);
	var modalityStrong = document.getElementById("modalityStrong_" + functionIndex);
	if (functionType == "imaging") {
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
	if (functionType == "targeting") {
		targetSpan.style.display = "block";
		targetDiv.style.display = "block";
	} else {
		targetSpan.style.display = "none";
		targetDiv.style.display = "none";
	}
	return false;
}
function displayModality(compEleIndex, functionIndex) {
	var functionType = document.getElementById("funcType_" + compEleIndex + "_" + functionIndex).value;
	var modalityTd = document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex);
	var modalityTitle = document.getElementById("modalityTitle_" + compEleIndex + "_" + functionIndex);
	if (functionType == "imaging") {
		modalityTd.style.display = "inline";
		modalityTitle.style.display = "inline";
	} else {
		modalityTd.style.display = "none";
		modalityTitle.style.display = "none";
	}
	return false;
}
function displayAntigenSpecies(parentIndex, childIndex) {
	var type = document.getElementById("targetType_" + parentIndex + "_" + childIndex).value;
	var sdiv = document.getElementById("speciesDiv_" + parentIndex + "_" + childIndex);
	var removeSpan = document.getElementById("removeSpan_" + parentIndex + "_" + childIndex);
	if (type == "antigen") {
		sdiv.style.display = "inline";
	} else {
		sdiv.style.display = "none";
	}
	return false;
}
function radLinkOrUpload(fileIndex) {
	var linkEle = document.getElementById("link_" + fileIndex);
	var loadEle = document.getElementById("load_" + fileIndex);
	if (document.getElementById("external_" + fileIndex).checked) {
		loadEle.style.display = "inline";
		linkEle.style.display = "none";
	} else {
		loadEle.style.display = "none";
		linkEle.style.display = "inline";
	}
}
/* 
 * for chemical association 
 */
function getAssociatedElementOptions(compositionTypeId, entityTypeId, compEleId) {
	var compositionType = dwr.util.getValue(compositionTypeId);
	var compEle = document.getElementById(compEleId);
	if (compositionType != "") {
		CompositionManager.getAssociatedElementOptions(compositionType, function (data) {
			dwr.util.removeAllOptions(entityTypeId);
			if (data != null) {
				dwr.util.addOptions(entityTypeId, [""]);
				dwr.util.addOptions(entityTypeId, data, "dataId", "dataDisplayType");
			}
		});
	} else {
		dwr.util.removeAllOptions(entityTypeId);
	}
	if (compositionType != "Nanoparticle Entity") {
		compEle.style.display = "none";
	}
}
function getAssociatedComposingElements(compositionTypeId, entityTypeId, compEleTypeId, compEleId) {
	var compositionType = dwr.util.getValue(compositionTypeId);
	var compEle = document.getElementById(compEleId);
	if (compositionType == "Nanoparticle Entity") {
		var entityId = dwr.util.getValue(entityTypeId);
		if (entityId != "") {
			CompositionManager.getAssociatedComposingElements(entityId, function (data) {
				dwr.util.removeAllOptions(compEleTypeId);
				if (data != null) {
					dwr.util.addOptions(compEleTypeId, [""]);
					dwr.util.addOptions(compEleTypeId, data, "domainComposingElementId", "displayName");
				}
			});
		} else {
			dwr.util.removeAllOptions(compEleTypeId);
		}
		compEle.style.display = "inline";
	} else {
		dwr.util.removeAllOptions(compEleTypeId);
		compEle.style.display = "none";
	}
	return false;
}
function displayBondType() {
	var type = document.getElementById("assoType").value;
	var btTitleEle = document.getElementById("bondTypeTitle");
	var btLineEle = document.getElementById("bondTypeLine");
	if (type == "attachment") {
		btTitleEle.style.display = "inline";
		btLineEle.style.display = "inline";
	} else {
		btTitleEle.style.display = "none";
		btLineEle.style.display = "none";
	}
}
function setCompositionType(entityTypeId, displayNameEleId) {
	var selectEle = document.getElementById(entityTypeId);
	var selectedName = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById(displayNameEleId).value = selectedName;
	// alert(document.getElementById(displayNameEleId).value);
}
function setEntityDisplayName(entityTypeId, displayNameEleId) {
	var selectEle = document.getElementById(entityTypeId);
	var selectedName = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById(displayNameEleId).value = selectedName;
	// alert(document.getElementById(displayNameEleId).value);
}
/*
 * the following functions using AJAX to display modality dropdown menu in the 
 * bodyNanoparticleEntityUpdate.jsp and bodyFunctionUpdate.jsp
 *
 */
/*
function setModalityTypeOptions(compEleIndex, functionIndex) {
	var functionType = dwr.util.getValue("funcType_" + compEleIndex + "_" + functionIndex);
	CompositionManager.getModalityTypeOptions(functionType, function(data) {
			
			dwr.util.removeAllOptions("modalityType_" + compEleIndex + "_" + functionIndex);
			dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, ['']);
    		dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, data);
    		dwr.util.addOptions("modalityType_" + compEleIndex + "_" + functionIndex, ['[Other]']);
  	});
}

function setModalityInclude(compEleIndex, functionIndex) {
	var functionType = dwr.util.getValue("funcType_" + compEleIndex + "_" + functionIndex);
	
	CompositionManager.getModalityIncludePage(compEleIndex, functionIndex, functionType, function(pageData) {
	
		document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex).innerHTML = "";
		dwr.util.setValue("modalityTypeTd_" + compEleIndex + "_" + functionIndex, pageData, {escapeHtml:false});
		
	});
	
  	if(functionType != "imaging") {
  		document.getElementById("modalityType_" + compEleIndex + "_" + functionIndex).innerHTML =
  			"&nbsp;";
  	}
}
*/

