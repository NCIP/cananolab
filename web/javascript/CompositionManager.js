
function setEntityInclude(selectEleId, pagePath) {
	var entityType = document.getElementById(selectEleId).value;
	var inclueBlock = document.getElementById("entityInclude");
	CompositionManager.getEntityIncludePage(entityType, pagePath, populatePage);
}
function populatePage(pageData) {
	var inclueBlock = document.getElementById("entityInclude");
	if (pageData == "") {
		inclueBlock.style.display = "none";
	} else {
		dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
		inclueBlock.style.display = "inline";
	}
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
	if (compositionType != "Nanomaterial Entity") {
		compEle.style.display = "none";
	}
}

function getEntityDisplayNameOptions(elementNumber) {
	var compositionType = dwr.util.getValue("compositionType"+elementNumber);
	if (compositionType=="Nanomaterial Entity") {
		show("materialEntitySelect"+elementNumber);
		hide("functionalizingEntitySelect"+elementNumber);
	}
	else if (compositionType=="Functionalizing Entity") {
		show("functionalizingEntitySelect"+elementNumber);
		hide("materialEntitySelect"+elementNumber);
	}
}
function getAssociatedComposingElements(compositionTypeId, entityTypeId, compEleTypeId, compEleId) {
	var compositionType = dwr.util.getValue(compositionTypeId);
	var compEle = document.getElementById(compEleId);
	if (compositionType == "Nanomaterial Entity") {
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
	if (type == "attachment") {
		show("bondTypeLabel");
		show("bondTypePrompt");
	} else {
		hide("bondType");
		hide("bondTypePrompt");
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
 * bodyNanomaterialEntityUpdate.jsp and bodyFunctionUpdate.jsp
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

