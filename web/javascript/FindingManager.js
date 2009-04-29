
function setNameOptionsByCharName(charName) {
	if (charName == "") {
		charName = document.getElementById("charName").value;
	}
	var columnType = document.getElementById("columnType").value;
	if (columnType == "Datum") {
		hide("conditionPropertyPrompt");
		hide("conditionPropertyLabel");
		FindingManager.getDatumNameOptions(charName, function (data) {
			dwr.util.removeAllOptions("columnName");
			dwr.util.addOptions("columnName", [""]);
			dwr.util.addOptions("columnName", data);
			dwr.util.addOptions("columnName", ["[Other]"]);
		});
	} else {
		if (columnType == "Condition") {
			show("conditionPropertyPrompt");
			show("conditionPropertyLabel");
			FindingManager.getConditionOptions(function (data) {
				dwr.util.removeAllOptions("columnName");
				dwr.util.addOptions("columnName", [""]);
				dwr.util.addOptions("columnName", data);
				dwr.util.addOptions("columnName", ["[Other]"]);
			});
		} else {
			hide("conditionPropertyPrompt");
			hide("conditionPropertyLabel");
			dwr.util.removeAllOptions("columnName");
			dwr.util.addOptions("columnName", [""]);
			dwr.util.addOptions("columnName", ["[Other]"]);
		}
	}
}
function setConditionPropertyOptionsByCharName(conditionName) {
	if (document.getElementById("columnType").value == "Condition") {
		if (conditionName == null) {
			conditionName = document.getElementById("columnName").value;
		}
		FindingManager.getConditionPropertyOptions(conditionName, function (data) {
			dwr.util.removeAllOptions("conditionProperty");
			dwr.util.addOptions("conditionProperty", [""]);
			dwr.util.addOptions("conditionProperty", data);
			dwr.util.addOptions("conditionProperty", ["[Other]"]);
		});
	}
}
function setColumnValueUnit() {
	var name = null, property = null;
	if (document.getElementById("columnType").value == "Condition") {
		property = document.getElementById("conditionProperty").value;
	}
	name = document.getElementById("columnName").value;
	FindingManager.getColumnValueUnitOptions(name, property, function (data) {
		dwr.util.removeAllOptions("valueUnit");
		dwr.util.addOptions("valueUnit", [""]);
		dwr.util.addOptions("valueUnit", data);
		dwr.util.addOptions("valueUnit", ["[Other]"]);
	});
}
function resetTheFinding(isShow) {
	editFinding = false;
	headerColumnCount = 0;
	datumHeaderColumnCount = 0;
	conditionHeaderColumnCount = 0;
	columnCount = 0;
	rowCount = 0;
	if (isShow) {
		show("newFinding");
	} else {
		hide("newFinding");
		show("existingFinding");
		return;
	}
}
function clearFile() {
	dwr.util.setValue("fileType", "");
	dwr.util.setValue("fileTitle", "");
	dwr.util.setValue("fileKeywords", "");
	dwr.util.setValue("fileVisibility", "");
	dwr.util.setValue("external0", 1);
	dwr.util.setValue("uploadedFile", null);
	show("load");
	dwr.util.setValue("externalUrl", "");
	FindingManager.resetFile(function (file) {
		currentFile = file;
	});
}
function addFile(actionName) {
	submitAction(document.forms[0], actionName, "addFile");
}
function setTheFile(index) {
	show("newFile");
	show("deleteFile");
	currentFileIndex = index;
	window.setTimeout("updateFile()", 300);
}
function updateFile() {
	FindingManager.getFileFromList(currentFileIndex, populateFile);
}
function populateFile(file) {
	if (file != null) {
		currentFile = file;
		dwr.util.setValue("fileType", file.domainFile.type);
		dwr.util.setValue("fileTitle", file.domainFile.Title);
		dwr.util.setValue("fileKeywords", file.keywordStr);
		dwr.util.setValue("fileVisibility", file.visibilities);
		dwr.util.setValue("external0", 1);
	    //dwr.util.setValue("uploadedFile", null);
	}
}
function updateMatrix(form) {
	var colNum = dwr.util.getValue("colNum");
	var rowNum = dwr.util.getValue("rowNum");
	if (colNum > 0 && rowNum > 0) {
		form.action = "characterization.do?dispatch=drawMatrix&page=0";
		form.submit();
	}
}

function reduceMatrix(form, type, index) {
	form.action = "characterization.do?dispatch=drawMatrix&page=0&remove"+type+"="+index;
	form.submit();
}

function openColumnForm(columnNumber) {
	var promptbox = document.createElement("div");
	promptbox.setAttribute("id", "newColumn");
	document.getElementById("column" + columnNumber).appendChild(promptbox);
	FindingManager.getSubmitColumnPage(columnNumber, function (pageData) {
		document.getElementById("newColumn").innerHTML = pageData;
	});
}
function cancelColumn(columnNumber) {
	document.getElementById("column" + columnNumber).removeChild(document.getElementById("newColumn"));
	return false;
}
function addColumnHeader(columnNumber) {
	var columnHeader = {"columnName":null, "valueType":null, "valueUnit":null, "conditionProperty":null, "columnType":null, "constantValue":null};
	dwr.util.getValues(columnHeader);
	alert(columnHeader.name0);
	FindingManager.addColumnHeader(columnHeader, function (displayName) {
		dwr.util.setValue("columnHeaderDisplayName", displayName);
	});
}

