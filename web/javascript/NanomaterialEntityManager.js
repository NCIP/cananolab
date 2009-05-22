
var inherentFunctionCache = null;
var currentComposingElement = null;
var numberOfFunctions;
/* set the submit composing element form */
function clearComposingElement() {
	numberOfFunctions = 0;
	//go to server and clean form bean
	NanomaterialEntityManager.resetTheComposingElement(populateComposingElement);
	hide("deleteComposingElement");
}
function clearInherentFunction() {
	dwr.util.setValue("functionId", "");
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
function addInherentFunction() {
	var imagingFunction = {modality:dwr.util.getValue("imagingModality")};
	var funcId = dwr.util.getValue("functionId");
	if (funcId == null || funcId == "") {
		funcId = -1000 - numberOfFunctions;
	}
	var theFunction = {id:funcId, type:dwr.util.getValue("functionType"), imagingFunction:imagingFunction, description:dwr.util.getValue("functionDescription")};
	if (theFunction.type != "" || theFunction.description != "") {
		NanomaterialEntityManager.addInherentFunction(theFunction, function (composingElement) {
			currentComposingElement = composingElement;
			window.setTimeout("populateInherentFunctions()", 200);
		});
	} else {
		alert("Please fill in values");
	}
}
function populateInherentFunctions() {
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
		if (theFunction.id == null || theFunction.id == "") {
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
		dwr.util.setValue("functionId", id);
		$("pattern" + id).style.display = "";
		if (inherentFunctionCache[id] == null) {
			numberOfFunctions++;
		}
		inherentFunctionCache[id] = theFunction;
	}
}
function setTheComposingElement(id) {
	numberOfFunctions = 0;
	NanomaterialEntityManager.getComposingElementById(id, populateComposingElement);
	show("newComposingElement");
	hide("newInherentFunction");
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
		if (element.domain.type != null) {
			show("deleteComposingElement");
		}
		inherentFunctionCache = {};
		populateInherentFunctions();
	}
}
function editInherentFunction(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var func = inherentFunctionCache[eleid.substring(4)];
	alert("function id: "+func.id);
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
	dwr.util.setValue("functionId", func.id);
	show("deleteFunction");
}
function deleteTheInherentFunction() {
	var eleid = document.getElementById("functionId").value;
	if (eleid != "") {
		var func = inherentFunctionCache[eleid];
		if (confirm("Are you sure you want to delete this function?")) {
			NanomaterialEntityManager.deleteInherentFunction(func, function (composingElement) {
				currentComposingElement = composingElement;
			});
			window.setTimeout("populateInherentFunctions()", 200);
			hide("newInherentFunction");
		}
	}
}
function addComposingElement(actionName) {
	submitAction(document.forms[0], actionName, "saveComposingElement");
}
function removeComposingElement(actionName) {
	submitAction(document.forms[0], actionName, "removeComposingElement");
}
/* end of submit composing element form */

