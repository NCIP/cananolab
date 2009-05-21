
var targetCache = {};
var currentFunction = null;
/* set the submit function form */
function clearFunction() {
	//go to server and clean form bean
	FunctionalizingEntityManager.resetTheFunction(populateFunction);
	hide("deleteFunction");
}
function clearTarget() {
	dwr.util.setValue("targetId", "");
	dwr.util.setValue("targetType", "");
	dwr.util.setValue("targetName", "");
	dwr.util.setValue("targetDescription", "");
	hide("deleteTarget");
	hide("antigenSpeciesLabel");
	hide("antigenSpeciesPrompt");
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
function displayTargets() {
	var functionType = dwr.util.getValue("functionType");
	if (functionType == "targeting") {
		show("targetLabel");
		show("targetSection");
	} else {
		hide("targetLabel");
		hide("targetSection");
	}
}
function displaySpecies() {
	var targetType = dwr.util.getValue("targetType");
	if (targetType == "antigen") {
		show("antigenSpeciesLabel");
		show("antigenSpeciesPrompt");
	} else {
		hide("antigenSpeciesLabel");
		hide("antigenSpeciesPrompt");
	}
}
function addTarget() {
	var antigen = {species:dwr.util.getValue("antigenSpecies")};
	var target = {id:dwr.util.getValue("targetId"), type:dwr.util.getValue("targetType"), name:dwr.util.getValue("targetName"), antigen:antigen, description:dwr.util.getValue("targetDescription")};
	if (target.type != "" || target.description != "") {
		FunctionalizingEntityManager.addTarget(target, function (theFunction) {
			currentFunction = theFunction;
			window.setTimeout("populateTargets()", 200);
		});
	} else {
		alert("Please fill in values");
	}
}
function setTheFunction(index) {
	FunctionalizingEntityManager.getFunctionFromList(index, populateFunction);
	show("newFunction");
	hide("newTarget");
	show("deleteFunction");
}
function populateFunction(func) {
	if (func != null) {
		currentFunction = func;
		dwr.util.setValue("functionType", func.type);
		dwr.util.setValue("imagingModality", func.imagingFunction.modality);
		dwr.util.setValue("functionDescription", func.description);
		show("deleteFunction");
		displayTargets();
		populateTargets();
	}
}
function populateTargets() {
	var targets = currentFunction.targets;
	dwr.util.removeAllRows("targetRows", {filter:function (tr) {
		return (tr.id != "pattern" && tr.id != "patternHeader");
	}});
	var target, id;
	if (targets.length > 0) {
		show("targetTable");
	} else {
		hide("targetTable");
	}
	for (var i = 0; i < targets.length; i++) {
		target = targets[i];
		if (target.id == null || target.id == "") {
			target.id = -i - 1;
		}
		id = target.id;
		dwr.util.cloneNode("pattern", {idSuffix:id});
		dwr.util.setValue("targetTypeValue" + id, target.type);
		if (target.type == "antigen") {
			dwr.util.setValue("antigenSpeciesValue" + id, target.antigen.species);
			show("antigenSpeciesHeader");
			show("antigenSpeciesValue" + id);
		} else {
			dwr.util.setValue("antigenSpeciesValue" + id, "");
			hide("antigenSpeciesHeader");
			hide("antigenSpeciesValue" + id);
		}
		dwr.util.setValue("targetNameValue" + id, target.name);
		dwr.util.setValue("targetDescriptionValue" + id, target.description);
		dwr.util.setValue("targetId", id);
		$("pattern" + id).style.display = "";
		targetCache[id] = target;
	}
}
function editTarget(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var target = targetCache[eleid.substring(4)];
	dwr.util.setValue("targetType", target.type);
	if (target.type == "antigen") {
		show("antigenSpeciesLabel");
		show("antigenSpeciesPrompt");
		dwr.util.setValue("antigenSpeciesValue", target.antigen.species);
	} else {
		hide("modalityLabel");
		hide("imagingModalityPrompt");
		dwr.util.setValue("imagingModality", "");
	}
	dwr.util.setValue("targetName", target.name);
	dwr.util.setValue("targetDescription", target.description);
	show("deleteTarget");
}
function deleteTheTarget() {
	var eleid = document.getElementById("targetId").value;
	if (eleid != "") {
		var target = targetCache[eleid];
		if (confirm("Are you sure you want to delete this target?")) {
			FunctionalizingEntityManager.deleteTarget(target, function (theFunction) {
				currentFunction = theFunction;
			});
			alert(currentFunction.targets.length);
			window.setTimeout("populateTargets()", 200);
			hide("newTarget");
		}
	}
}
function addFunction(actionName) {
	submitAction(document.forms[0], actionName, "saveFunction");
}
function removeFunction(actionName) {
	submitAction(document.forms[0], actionName, "removeFunction");
}
/* end of submit composing element form */

