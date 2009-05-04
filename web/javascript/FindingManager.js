
function setNameOptionsByCharName(charName, columnNumber) {
	if (charName == "") {
		charName = document.getElementById("charName").value;
	}
	var columnType = document.getElementById("columnType"+columnNumber).value;
	if (columnType == "Datum") {
		hide("conditionPropertyPrompt"+columnNumber);
		hide("conditionPropertyLabel"+columnNumber);
		FindingManager.getDatumNameOptions(charName, function (data) {
			dwr.util.removeAllOptions("columnName"+columnNumber);
			dwr.util.addOptions("columnName"+columnNumber, [""]);
			dwr.util.addOptions("columnName"+columnNumber, data);
			dwr.util.addOptions("columnName"+columnNumber, ["[Other]"]);
		});
	} else {
		if (columnType == "Condition") {
			show("conditionPropertyPrompt"+columnNumber);
			show("conditionPropertyLabel"+columnNumber);
			FindingManager.getConditionOptions(function (data) {
				dwr.util.removeAllOptions("columnName"+columnNumber);
				dwr.util.addOptions("columnName"+columnNumber, [""]);
				dwr.util.addOptions("columnName"+columnNumber, data);
				dwr.util.addOptions("columnName"+columnNumber, ["[Other]"]);
			});
		} else {
			hide("conditionPropertyPrompt"+columnNumber);
			hide("conditionPropertyLabel"+columnNumber);
			dwr.util.removeAllOptions("columnName"+columnNumber);
			dwr.util.addOptions("columnName"+columnNumber, [""]);
			dwr.util.addOptions("columnName"+columnNumber, ["[Other]"]);
		}
	}
}
function setConditionPropertyOptionsByCharName(conditionName, columnNumber) {
	if (document.getElementById("columnType"+columnNumber).value == "Condition") {
		if (conditionName == null) {
			conditionName = document.getElementById("columnName"+columnNumber).value;
		}
		FindingManager.getConditionPropertyOptions(conditionName, function (data) {
			dwr.util.removeAllOptions("conditionProperty"+columnNumber);
			dwr.util.addOptions("conditionProperty"+columnNumber, [""]);
			dwr.util.addOptions("conditionProperty"+columnNumber, data);
			dwr.util.addOptions("conditionProperty"+columnNumber, ["[Other]"]);
		});
	}
}
function setColumnValueUnit(columnNumber) {
	var name = null, property = null;
	if (document.getElementById("columnType"+columnNumber).value == "Condition") {
		property = document.getElementById("conditionProperty"+columnNumber).value;
	}
	name = document.getElementById("columnName"+columnNumber).value;
	FindingManager.getColumnValueUnitOptions(name, property, function (data) {
		dwr.util.removeAllOptions("valueUnit"+columnNumber);
		dwr.util.addOptions("valueUnit"+columnNumber, [""]);
		dwr.util.addOptions("valueUnit"+columnNumber, data);
		dwr.util.addOptions("valueUnit"+columnNumber, ["[Other]"]);
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
	form.action = "characterization.do?dispatch=drawMatrix&page=0&remove" + type + "=" + index;
	form.submit();
}
function openColumnForm(columnNumber) {
	if (document.getElementById("newColumn"+columnNumber) != null) {
        //retrieve values from hidden properties
		show("newColumn"+columnNumber);
		var columnHeader = {"columnName":dwr.util.getValue("theColumnName" + columnNumber), "columnType":dwr.util.getValue("theColumnType" + columnNumber), "conditionProperty":dwr.util.getValue("theConditionProperty" + columnNumber), "valueType":dwr.util.getValue("theValueTYpe" + columnNumber), "valueUnit":dwr.util.getValue("theValueUnit" + columnNumber), "constantValue":dwr.util.getValue("theConstantValue" + columnNumber)};
		dwr.util.setValue(columnHeader);
	} else {
		var promptbox = document.createElement("div");
		promptbox.setAttribute("id", "newColumn"+columnNumber);
		document.getElementById("column" + columnNumber).appendChild(promptbox);
		FindingManager.getSubmitColumnPage(columnNumber, function (pageData) {
			document.getElementById("newColumn"+columnNumber).innerHTML = pageData;
		});
		//set style of the column entry table
		document.getElementById("newColumn"+columnNumber).style.display = "block";
		document.getElementById("newColumn"+columnNumber).style.position = "absolute";
		document.getElementById("newColumn"+columnNumber).style.top = "20px";
		document.getElementById("newColumn"+columnNumber).style.width = "350px";
		document.getElementById("newColumn"+columnNumber).style.zIndex = "5";
	}
}
function cancelColumn(columnNumber) {
	//existing column, just hide
	if (document.getElementById("newColumn"+columnNumber) != null) {
		hide("newColumn"+columnNumber);
	}
	//new column
	else {
		document.getElementById("column" + columnNumber).removeChild(document.getElementById("newColumn"+columnNumber));
	}
	return false;
}
function addColumnHeader(columnNumber) {
	var columnHeader = {
	"columnName":dwr.util.getValue("columnName" + columnNumber),
	"columnType":dwr.util.getValue("columnType" + columnNumber),
	"conditionProperty":dwr.util.getValue("conditionProperty" + columnNumber),
	"valueType":dwr.util.getValue("valueType" + columnNumber),
	"valueUnit":dwr.util.getValue("valueUnit" + columnNumber),
	"constantValue":dwr.util.getValue("constantValue" + columnNumber)};

	var numberOfRows = dwr.util.getValue("rowNum");
	if (columnHeader.constantValue != null) {
		for (i = 0; i < numberOfRows; i++) {
			dwr.util.setValue("cellValue" + i + ":" + columnNumber, columnHeader.constantValue);
		}
	}
	FindingManager.addColumnHeader(columnHeader, function (displayName) {
		dwr.util.setValue("columnHeaderDisplayName" + columnNumber, displayName);
		//set the hidden properties
		dwr.util.setValue("theColumnType" + columnNumber, columnHeader.columnType);
		dwr.util.setValue("theColumnName" + columnNumber, columnHeader.columnName);
		dwr.util.setValue("theConditionProperty" + columnNumber, columnHeader.conditionProperty);
		dwr.util.setValue("theValueType" + columnNumber, columnHeader.valueType);
		dwr.util.setValue("theValueUnit" + columnNumber, columnHeader.valueUnit);
		dwr.util.setValue("theConstantValue" + columnNumber, columnHeader.constantValue);
	});
	hide("newColumn"+columnNumber);
}

