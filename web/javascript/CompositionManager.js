
var inherentFunctionCache = {};
var currentComposingElement = null;
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
function getEntityDisplayNameOptions(elementNumber) {
	var compositionType = dwr.util.getValue("compositionType" + elementNumber);
	if (compositionType == "Nanomaterial Entity") {
		show("materialEntitySelect" + elementNumber);
		hide("functionalizingEntitySelect" + elementNumber);
	} else {
		if (compositionType == "Functionalizing Entity") {
			show("functionalizingEntitySelect" + elementNumber);
			hide("materialEntitySelect" + elementNumber);
		}
	}
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
function setModalityInclude(compEleIndex, functionIndex) {
	var functionType = dwr.util.getValue("funcType_" + compEleIndex + "_" + functionIndex);
	CompositionManager.getModalityIncludePage(compEleIndex, functionIndex, functionType, function (pageData) {
		document.getElementById("modalityTypeTd_" + compEleIndex + "_" + functionIndex).innerHTML = "";
		dwr.util.setValue("modalityTypeTd_" + compEleIndex + "_" + functionIndex, pageData, {escapeHtml:false});
	});
	if (functionType != "imaging") {
		document.getElementById("modalityType_" + compEleIndex + "_" + functionIndex).innerHTML = "&nbsp;";
	}
}
function setTheFile(type, index) {
	CompositionManager.getFileFromList(type, index, populateFile);
	dwr.util.setValue("hiddenFileIndex", index);
}
function clearComposingElement() {
	dwr.util.setValue("elementType", "");
	dwr.util.setValue("elementName", "");
	dwr.util.setValue("elementValue", "");
	dwr.util.setValue("elementValueUnit", "");
	dwr.util.setValue("elementDescription", "");
	dwr.util.setValue("molFormulaType", "");
	dwr.util.setValue("molFormula", "");
	dwr.util.setValue("hiddenFileIndex", -1);
	hide("deleteComposingElement");
}
function clearFunction() {
	dwr.util.setValue("functionType", "");
	dwr.util.setValue("imagingModality", "");
	dwr.util.setValue("functionDescription", "");
	hide("deleteFunction");
	hide("modalityLabel");
	hide("imagingModalityPrompt");
}
function displayImageModality() {
	var functionType = dwr.util.getValue("functionType");
	if (functionType == "imaging") {
		show("modalityLabel");
		show("imagingModalityPrompt");
	} else {
		hide("modalityLabel");
		hide("imagingModalityPrompt");
	}
}
function addFunction() {
	var theFunction = {id:dwr.util.getValue("functionId"), type:dwr.util.getValue("functionType"), description:dwr.util.getValue("functionDescription")};
	if (theFunction.type != "" || theFunction.description != "") {
		CompositionManager.addInherentFunction(currentComposingElement, theFunction, function (composingElement) {
			currentComposingElement = composingElement;
			alert(currentComposingElement.inherentFunctions.length);
			window.setTimeout("populateFunctions()", 200);
		});
	} else {
		alert("Please fill in values");
	}
}
function populateFunctions() {
	var functions = currentComposingElement.inherentFunctions;
	dwr.util.removeAllRows("functionRows", {filter:function (tr) {
		return (tr.id != "pattern" && tr.id != "patternHeader");
	}});
	var theFunction, id;
	if (functions.length > 0) {
		show("functionTable");
	} else {
		hide("functionTable");
	}
	for (var i = 0; i < functions.length; i++) {
		theFunction = functions[i];
		if (theFunction.id == null) {
			theFunction.id = -i - 1;
		}
		id = theFunction.id;
		dwr.util.cloneNode("pattern", {idSuffix:id});
		dwr.util.setValue("functionTypeValue" + id, theFunction.type);
		if (theFunction.type == "imaging") {
			dwr.util.setValue("functionModalityTypeValue" + id, theFunction.imagingFunction.modality);
			show("modalityHeader");
			show("functionModalityTypeValue" + id);
		} else {
			dwr.util.setValue("functionModalityTypeValue" + id, "");
			hide("modalityHeader");
			hide("functionModalityTypeValue" + id);
		}
		dwr.util.setValue("functionDescriptionValue" + id, theFunction.description);
		$("pattern" + id).style.display = "";
		inherentFunctionCache[id] = theFunction;
	}
}
function setTheComposingElement(index) {
	dwr.util.setValue("hiddenComposingElementIndex", index);
	CompositionManager.getComposingElementFromList(index, populateComposingElement);
	show("newComposingElement");
	hide("newFunction");
	show("deleteComposingElement");
}
function populateComposingElement(element) {
	if (element != null) {
		currentComposingElement = element;
		dwr.util.setValue("elementType", element.domain.type);
		dwr.util.setValue("elementName", element.domain.name);
		dwr.util.setValue("elementValue", element.domain.value);
		dwr.util.setValue("elementValueUnit", element.domain.valueUnit);
		dwr.util.setValue("elementDescription", element.domain.description);
		dwr.util.setValue("molFormulaType", element.domain.molecularFormulaType);
		dwr.util.setValue("molFormula", element.domain.molecularFormula);
		show("deleteComposingElement");
		functionCount = element.inherentFunctions.length;
		rowCount = document.getElementById("functionRows").rows.length;
		populateFunctions();
	}
}
function editFunction(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var func = inherentFunctionCache[eleid.substring(4)];
	dwr.util.setValue("functionType", func.type);
	if (func.type == "imaging") {
		show("modalityLabel");
		show("imagingModalityPrompt");
		dwr.util.setValue("imagingModality", func.imagingFunction.modality);
	} else {
		hide("modalityLabel");
		hide("imagingModalityPrompt");
		dwr.util.setValue("imagingModality", "");
	}
	dwr.util.setValue("functionDescription", func.description);
	show("deleteFunction");
}
function deleteFunction() {
	var eleid = document.getElementById("functionId").value;
	if (eleid != "") {
		var func = inherentFunctionCache[eleid];
		if (confirm("Are you sure you want to delete this function?")) {
			CompositionManager.deleteInherentFunction(currentComposingElement, func, function (composingElement) {
				currentComposingElement = composingElement;
			});
			window.setTimeout("populateFunctions()", 200);
		}
	}
}

