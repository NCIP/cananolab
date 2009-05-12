
function setNameOptionsByCharName(charName, columnNumber) {
	if (charName == "") {
		charName = document.getElementById("charName").value;
	}
	var columnType = document.getElementById("columnType" + columnNumber).value;
	if (columnType == "Datum") {
		hide("conditionPropertyPrompt" + columnNumber);
		hide("conditionPropertyLabel" + columnNumber);
		FindingManager.getDatumNameOptions(charName, function (data) {
			dwr.util.removeAllOptions("columnName" + columnNumber);
			dwr.util.addOptions("columnName" + columnNumber, [""]);
			dwr.util.addOptions("columnName" + columnNumber, data);
			dwr.util.addOptions("columnName" + columnNumber, ["[Other]"]);
		});
	} else {
		if (columnType == "Condition") {
			show("conditionPropertyPrompt" + columnNumber);
			show("conditionPropertyLabel" + columnNumber);
			FindingManager.getConditionOptions(function (data) {
				dwr.util.removeAllOptions("columnName" + columnNumber);
				dwr.util.addOptions("columnName" + columnNumber, [""]);
				dwr.util.addOptions("columnName" + columnNumber, data);
				dwr.util.addOptions("columnName" + columnNumber, ["[Other]"]);
			});
		} else {
			hide("conditionPropertyPrompt" + columnNumber);
			hide("conditionPropertyLabel" + columnNumber);
			dwr.util.removeAllOptions("columnName" + columnNumber);
			dwr.util.addOptions("columnName" + columnNumber, [""]);
			dwr.util.addOptions("columnName" + columnNumber, ["[Other]"]);
		}
	}
}
function setConditionPropertyOptionsByCharName(conditionName, columnNumber) {
	if (document.getElementById("columnType" + columnNumber).value == "Condition") {
		if (conditionName == null) {
			conditionName = document.getElementById("columnName" + columnNumber).value;
		}
		FindingManager.getConditionPropertyOptions(conditionName, function (data) {
			dwr.util.removeAllOptions("conditionProperty" + columnNumber);
			dwr.util.addOptions("conditionProperty" + columnNumber, [""]);
			dwr.util.addOptions("conditionProperty" + columnNumber, data);
			dwr.util.addOptions("conditionProperty" + columnNumber, ["[Other]"]);
		});
	}
}
function setColumnValueUnit(columnNumber) {
	var name = null, property = null;
	if (document.getElementById("columnType" + columnNumber).value == "Condition") {
		property = document.getElementById("conditionProperty" + columnNumber).value;
	}
	name = document.getElementById("columnName" + columnNumber).value;
	FindingManager.getColumnValueUnitOptions(name, property, function (data) {
		dwr.util.removeAllOptions("valueUnit" + columnNumber);
		dwr.util.addOptions("valueUnit" + columnNumber, [""]);
		dwr.util.addOptions("valueUnit" + columnNumber, data);
		dwr.util.addOptions("valueUnit" + columnNumber, ["[Other]"]);
	});
}
function resetTheFinding(form, isShow) {
	form.action = "characterization.do?dispatch=resetFinding&page=0";
	form.submit();
}
function setTheFinding(form, actionName, findingId) {
	form.action = actionName + ".do?dispatch=getFinding&findingId=" + findingId + "&page=0";
	form.submit();
}
function saveFinding(form) {
	form.action = "characterization.do?dispatch=saveFinding&page=0";
	form.submit();
}

function setTheFile(index) {
	FindingManager.getFileFromList(index, populateFile);
	dwr.util.setValue("hiddenFileIndex", index);
}
function populateFile(file) {
	if (file != null) {
		dwr.util.setValue("fileType", file.domainFile.type);
		dwr.util.setValue("fileTitle", file.domainFile.title);
		dwr.util.setValue("fileKeywords", file.keywordsStr);
		dwr.util.setValue("fileVisibility", file.visibilityGroups);
		if (file.domainFile.uriExternal) {
			dwr.util.setValue("external1", 1);
			dwr.util.setValue("external0", 0);
		} else {
			dwr.util.setValue("external1", 0);
			dwr.util.setValue("external0", 1);
		}
		if (file.domainFile.id != null) {
			dwr.util.setValue("hiddenFileId", file.domainFile.id);
		}
		if (file.domainFile.uri != null) {
			dwr.util.setValue("hiddenFileUri", file.domainFile.uri);
			if (file.domainFile.uriExternal == 0) {
				dwr.util.setValue("uploadedUri", file.domainFile.uri);
			} else {
				dwr.util.setValue("uploadedUri", "");
			}
		} else {
			dwr.util.setValue("uploadedUri", "");
		}
		if (file.domainFile.uriExternal) {
			show("link");
			hide("load");
		} else {
			show("load");
			hide("link");
		}
	}
	show("newFile");
	show("deleteFile");
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
	form.action = "characterization.do?dispatch=drawMatrix&page=0&remove" + type + "=" + index;
	form.submit();
}
function openColumnForm(characterizationName, columnNumber) {
	var promptbox = document.createElement("div");
	promptbox.setAttribute("id", "newColumn" + columnNumber);
	document.getElementById("column" + columnNumber).appendChild(promptbox);
	FindingManager.getSubmitColumnPage(columnNumber, function (pageData) {
		document.getElementById("newColumn" + columnNumber).innerHTML = pageData;
		//populate column entry table from hidden values, use timeout to populate drop down first
		dwr.util.setValue("columnType" + columnNumber, dwr.util.getValue("theColumnType" + columnNumber));
		setNameOptionsByCharName(characterizationName, columnNumber);
		window.setTimeout("delaySetColumnName(" + columnNumber + ")", 100);
		dwr.util.setValue("valueType" + columnNumber, dwr.util.getValue("theValueType" + columnNumber));
		dwr.util.setValue("constantValue" + columnNumber, dwr.util.getValue("theConstantValue" + columnNumber));
	});
	    //set style of the column entry table
	document.getElementById("newColumn" + columnNumber).style.display = "block";
	document.getElementById("newColumn" + columnNumber).style.position = "absolute";
	document.getElementById("newColumn" + columnNumber).style.top = "20px";
	document.getElementById("newColumn" + columnNumber).style.left = "10px";
	document.getElementById("newColumn" + columnNumber).style.width = "350px";
	document.getElementById("newColumn" + columnNumber).style.zIndex = "5";
}
function delaySetColumnName(columnNumber) {
	dwr.util.setValue("columnName" + columnNumber, dwr.util.getValue("theColumnName" + columnNumber));
	setConditionPropertyOptionsByCharName(null, columnNumber);
	setColumnValueUnit(columnNumber);
	window.setTimeout("delaySetConditionProperty(" + columnNumber + ")", 100);
	window.setTimeout("delaySetColumnValueUnit(" + columnNumber + ")", 100);
}
function delaySetConditionProperty(columnNumber) {
	dwr.util.setValue("conditionProperty" + columnNumber, dwr.util.getValue("theConditionProperty" + columnNumber));
	setColumnValueUnit(columnNumber);
	window.setTimeout("delaySetColumnValueUnit(" + columnNumber + ")", 100);
}
function delaySetColumnValueUnit(columnNumber) {
	dwr.util.setValue("valueUnit" + columnNumber, dwr.util.getValue("theValueUnit" + columnNumber));
}
function cancelColumn(columnNumber) {
	document.getElementById("column" + columnNumber).removeChild(document.getElementById("newColumn" + columnNumber));
}
function addColumnHeader(columnNumber) {
	var columnHeader = {"columnName":dwr.util.getValue("columnName" + columnNumber), "columnType":dwr.util.getValue("columnType" + columnNumber), "conditionProperty":dwr.util.getValue("conditionProperty" + columnNumber), "valueType":dwr.util.getValue("valueType" + columnNumber), "valueUnit":dwr.util.getValue("valueUnit" + columnNumber), "constantValue":dwr.util.getValue("constantValue" + columnNumber)};
	var numberOfRows = dwr.util.getValue("rowNum");
	if (columnHeader.constantValue != null) {
		for (i = 0; i < numberOfRows; i++) {
			dwr.util.setValue("cellValue" + i + ":" + columnNumber, columnHeader.constantValue);
		}
	}
	FindingManager.addColumnHeader(columnHeader, function (displayName) {
		dwr.util.setValue("columnHeaderDisplayName" + columnNumber, displayName, {escapeHtml:false});
		//set the hidden properties
		dwr.util.setValue("theColumnType" + columnNumber, columnHeader.columnType);
		dwr.util.setValue("theColumnName" + columnNumber, columnHeader.columnName);
		dwr.util.setValue("theConditionProperty" + columnNumber, columnHeader.conditionProperty);
		dwr.util.setValue("theValueType" + columnNumber, columnHeader.valueType);
		dwr.util.setValue("theValueUnit" + columnNumber, columnHeader.valueUnit);
		dwr.util.setValue("theConstantValue" + columnNumber, columnHeader.constantValue);
	});
	hide("newColumn" + columnNumber);
}

