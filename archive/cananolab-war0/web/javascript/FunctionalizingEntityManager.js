
var targetCache = null;
var currentFunction = null;
var numberOfTargets; //number of unique targets in the cache, used to generate target id
/* set the submit function form */
function clearFunction() {
	//go to server and clean form bean
	FunctionalizingEntityManager.resetTheFunction(populateFunction);
	hide("deleteFunction");
	numberOfTargets = 0;
}
function clearTarget() {
	dwr.util.setValue("targetId", "");
	dwr.util.setValue("targetType", "");
	dwr.util.setValue("targetName", "");
	dwr.util.setValue("antigenSpecies", "");
	dwr.util.setValue("targetDescription", "");
	hide("deleteTarget");
	hide("antigenSpeciesLabel");
	hide("antigenSpeciesPrompt");
}
function displayImageModality() {
	var functionType = dwr.util.getValue("functionType");
	if (functionType == "imaging function") {
		show("modalityLabel");
		show("imagingModalityPrompt");
	} else {
		hide("modalityLabel");
		hide("imagingModalityPrompt");
	}
}
function displayTargets() {
	var functionType = dwr.util.getValue("functionType");
	if (functionType == "targeting function") {
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
	var targetId = dwr.util.getValue("targetId");
	if (targetId == null || targetId == "") {
		targetId = -1000 - numberOfTargets;
	}
	var target = {id:targetId, type:dwr.util.getValue("targetType"), name:dwr.util.getValue("targetName"), antigen:antigen, description:dwr.util.getValue("targetDescription")};
	if (target.type != "" || target.name != "" || target.description != "") {
		FunctionalizingEntityManager.addTarget(target, function (theFunction) {
			if (theFunction != null) {
				currentFunction = theFunction;
				window.setTimeout("populateTargets()", 200);
			} else {
				sessionTimeout();
			}
		});
	} else {
		alert("Please fill in values");
	}
}
function setTheFunction(funcId) {
	numberOfTargets = 0;
	FunctionalizingEntityManager.getFunctionById(funcId, populateFunction);
	openSubmissionForm("Function");
	openSubmissionForm("Target");
	show("deleteFunction");
}
function populateFunction(func) {
	if (func != null) {
		currentFunction = func;
		dwr.util.setValue("functionId", func.id);
		dwr.util.setValue("functionType", func.type);
		dwr.util.setValue("imagingModality", func.imagingFunction.modality);
		dwr.util.setValue("functionDescription", func.description);
		if (func.type != null) {
			show("deleteFunction");
		}
		//clear the cache for each new function
		targetCache = {};
		populateTargets();
		displayTargets();
		displayImageModality();
	} else {
		sessionTimeout();
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
		if (targetCache[id] == null) {
			numberOfTargets++;
		}
		targetCache[id] = target;
	}
}
function editTarget(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var id = eleid.substring(4);
	var target = targetCache[id];
	dwr.util.setValue("targetType", target.type);
	if (target.type == "antigen") {
		show("antigenSpeciesLabel");
		show("antigenSpeciesPrompt");
		dwr.util.setValue("antigenSpecies", target.antigen.species);
	} else {
		hide("modalityLabel");
		hide("imagingModalityPrompt");
		dwr.util.setValue("imagingModality", "");
	}
	dwr.util.setValue("targetName", target.name);
	dwr.util.setValue("targetDescription", target.description);
	dwr.util.setValue("targetId", target.id);
	show("deleteTarget");
}
function deleteTheTarget() {
	var eleid = document.getElementById("targetId").value;
	if (eleid != "") {
		var target = targetCache[eleid];
		if (confirm("Are you sure you want to delete this target?")) {
			FunctionalizingEntityManager.deleteTarget(target, function (theFunction) {
				if (theFunction != null) {
					currentFunction = theFunction;
				} else {
					sessionTimeout();
				}
			});
			window.setTimeout("populateTargets()", 200);
			closeSubmissionForm("Target");
		}
	}
}
function addFunction(actionName) {
	if (validateAmountValue()) {
		submitAction(document.forms[0], actionName, "saveFunction", 2);
		return true;
	} else {
		return false;
	}
}
function removeFunction(actionName) {
	submitAction(document.forms[0], actionName, "removeFunction", 2);
}
/* end of submit composing element form */
function validateAmountValue() {
	var inputField = document.getElementById("amountValue");
	if (inputField != null && inputField.value != "" &&
		!validFloatNumber(inputField.value)) {
		alert("Please enter a valid number for Functionalizing Entity Amount.");
		return false;
	}
	return true;
}