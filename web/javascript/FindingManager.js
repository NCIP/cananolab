function setNameOptionsByCharName(columnNumber) {
	var charName = dwr.util.getValue("charName");
	var charType = dwr.util.getValue("charType");
	var assayType = dwr.util.getValue("assayType");
	var columnType = dwr.util.getValue("columnType" + columnNumber);
	if (columnType == "datum") {
		hide("conditionPropertyPrompt" + columnNumber);
		hide("conditionPropertyLabel" + columnNumber);
		FindingManager.getDatumNameOptions(charType, charName, assayType,
				function(data) {
					dwr.util.removeAllOptions("columnName" + columnNumber);
					dwr.util.addOptions("columnName" + columnNumber, [ "" ]);
					dwr.util.addOptions("columnName" + columnNumber, data);
					dwr.util.addOptions("columnName" + columnNumber,
							[ "[other]" ]);
				});
	} else {
		if (columnType == "condition") {
			show("conditionPropertyPrompt" + columnNumber);
			show("conditionPropertyLabel" + columnNumber);
			FindingManager
					.getConditionOptions( function(data) {
						dwr.util.removeAllOptions("columnName" + columnNumber);
						dwr.util
								.addOptions("columnName" + columnNumber, [ "" ]);
						dwr.util.addOptions("columnName" + columnNumber, data);
						dwr.util.addOptions("columnName" + columnNumber,
								[ "[other]" ]);
					});
		} else {
			hide("conditionPropertyPrompt" + columnNumber);
			hide("conditionPropertyLabel" + columnNumber);
			dwr.util.removeAllOptions("columnName" + columnNumber);
			dwr.util.addOptions("columnName" + columnNumber, [ "" ]);
			dwr.util.addOptions("columnName" + columnNumber, [ "[other]" ]);
		}
	}
}
function setConditionPropertyOptionsByCharName(conditionName, columnNumber) {
	if (dwr.util.getValue("columnType" + columnNumber) == "condition") {
		if (conditionName == null) {
			conditionName = dwr.util.getValue("columnName" + columnNumber);
		}
		FindingManager.getConditionPropertyOptions(conditionName,
				function(data) {
					dwr.util.removeAllOptions("conditionProperty"
							+ columnNumber);
					dwr.util.addOptions("conditionProperty" + columnNumber,
							[ "" ]);
					dwr.util.addOptions("conditionProperty" + columnNumber,
							data);
					dwr.util.addOptions("conditionProperty" + columnNumber,
							[ "[other]" ]);
				});
	}
}
function setColumnValueUnit(columnNumber) {
	var name = null, property = null;
	if (dwr.util.getValue("columnType" + columnNumber) == "condition") {
		property = dwr.util.getValue("conditionProperty" + columnNumber);
	}
	name = dwr.util.getValue("columnName" + columnNumber);
	FindingManager.getColumnValueUnitOptions(name, property, function(data) {
		dwr.util.removeAllOptions("valueUnit" + columnNumber);
		dwr.util.addOptions("valueUnit" + columnNumber, [ "" ]);
		dwr.util.addOptions("valueUnit" + columnNumber, data);
		dwr.util.addOptions("valueUnit" + columnNumber, [ "[other]" ]);
	});
}
function resetTheFinding(form) {
	form.action = "characterization.do?dispatch=resetFinding&page=0";
	form.submit();
}
function setTheFinding(form, actionName, findingId) {
	form.action = actionName + ".do?dispatch=getFinding&findingId=" + findingId
			+ "&page=0";
	form.submit();
}
function saveFinding(actionName) {
//	if (validateDataMatrix()) {
	    submitAction(document.forms[0], actionName, "saveFinding", 4);
//	}
}
function deleteTheFinding(form) {
	var answer = confirmDelete("finding");
	if (answer != 0) {
		form.action = "characterization.do?dispatch=deleteFinding&page=2";
		form.submit();
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
	form.action = "characterization.do?dispatch=drawMatrix&page=0&remove"
			+ type + "=" + index;
	form.submit();
}
function openColumnForm(characterizationName, columnNumber) {
	var promptbox = document.createElement("div");
	promptbox.setAttribute("id", "newColumn" + columnNumber);
	document.getElementById("column" + columnNumber).appendChild(promptbox);
	FindingManager
			.getSubmitColumnPage(
					columnNumber,
					function(pageData) {
						document.getElementById("newColumn" + columnNumber).innerHTML = pageData;
						// populate column entry table from hidden values, use
						// timeout to populate drop down first
						dwr.util.setValue("columnType" + columnNumber, dwr.util
								.getValue("theColumnType" + columnNumber));
						setNameOptionsByCharName(columnNumber);
						window.setTimeout("delaySetColumnName(" + columnNumber
								+ ")", 100);
						dwr.util.setValue("valueType" + columnNumber, dwr.util
								.getValue("theValueType" + columnNumber));
						dwr.util.setValue("constantValue" + columnNumber,
								dwr.util.getValue("theConstantValue"
										+ columnNumber));
					});
	// set style of the column entry table
	document.getElementById("newColumn" + columnNumber).style.display = "block";
	document.getElementById("newColumn" + columnNumber).style.position = "absolute";
	document.getElementById("newColumn" + columnNumber).style.top = "20px";
	document.getElementById("newColumn" + columnNumber).style.left = "10px";
	document.getElementById("newColumn" + columnNumber).style.width = "350px";
	document.getElementById("newColumn" + columnNumber).style.zIndex = "5";
}
function delaySetColumnName(columnNumber) {
	dwr.util.setValue("columnName" + columnNumber, dwr.util
			.getValue("theColumnName" + columnNumber));
	setConditionPropertyOptionsByCharName(null, columnNumber);
	setColumnValueUnit(columnNumber);
	window.setTimeout("delaySetConditionProperty(" + columnNumber + ")", 100);
	window.setTimeout("delaySetColumnValueUnit(" + columnNumber + ")", 100);
}
function delaySetConditionProperty(columnNumber) {
	dwr.util.setValue("conditionProperty" + columnNumber, dwr.util
			.getValue("theConditionProperty" + columnNumber));
	setColumnValueUnit(columnNumber);
	window.setTimeout("delaySetColumnValueUnit(" + columnNumber + ")", 100);
}
function delaySetColumnValueUnit(columnNumber) {
	dwr.util.setValue("valueUnit" + columnNumber, dwr.util
			.getValue("theValueUnit" + columnNumber));
}
function cancelColumn(columnNumber) {
	document.getElementById("column" + columnNumber).removeChild(
			document.getElementById("newColumn" + columnNumber));
}
function addColumnHeader(columnNumber) {
	var columnHeader = {
		"columnName" : dwr.util.getValue("columnName" + columnNumber),
		"columnType" : dwr.util.getValue("columnType" + columnNumber),
		"conditionProperty" : dwr.util.getValue("conditionProperty"
				+ columnNumber),
		"valueType" : dwr.util.getValue("valueType" + columnNumber),
		"valueUnit" : dwr.util.getValue("valueUnit" + columnNumber),
		"constantValue" : dwr.util.getValue("constantValue" + columnNumber)
	};
	var numberOfRows = dwr.util.getValue("rowNum");
	if (columnHeader.constantValue != null) {
		for (i = 0; i < numberOfRows; i++) {
			dwr.util.setValue("cellValue" + i + ":" + columnNumber,
					columnHeader.constantValue);
		}
	}
	FindingManager.addColumnHeader(columnHeader, function(displayName) {
		dwr.util.setValue("columnHeaderDisplayName" + columnNumber,
				displayName, {
					escapeHtml : false
				});
		// set the hidden properties
			dwr.util.setValue("theColumnType" + columnNumber,
					columnHeader.columnType);
			dwr.util.setValue("theColumnName" + columnNumber,
					columnHeader.columnName);
			dwr.util.setValue("theConditionProperty" + columnNumber,
					columnHeader.conditionProperty);
			dwr.util.setValue("theValueType" + columnNumber,
					columnHeader.valueType);
			dwr.util.setValue("theValueUnit" + columnNumber,
					columnHeader.valueUnit);
			dwr.util.setValue("theConstantValue" + columnNumber,
					columnHeader.constantValue);
		});
	hide("newColumn" + columnNumber);
}

function filterFloatForColumn(event, columnTypeId) {
	var columnType=dwr.util.getValue(columnTypeId);
	if (columnType=="datum") {
		return filterFloatNumber(event);
	}
	else {
		return true;
	}
}

function validateDataMatrix() {
	var rowNum=dwr.util.getValue("rowNum");
	var colNum=dwr.util.getValue("colNum");
	for (i=0; i<rowNum; i++) {
		for (j=0; j<colNum; j++) {
			var cellValue=dwr.util.getValue("cellValue"+i+":"+j);			
			if (cellValue=="") {
				alert("Please fill in value(s) in the data and condition table");
				return false;
			}
		}
	}
	return true;
}

/* set submit file form */
function clearFile() {
	// go to server and clean form bean
	FindingManager.resetTheFile(populateFile);
	hide("deleteFile");
	show("load");
	hide("link");
	dwr.util.setValue("uploadedFile", "");
	dwr.util.setValue("externalUrl", "");
}
function setTheFile(index) {
	FindingManager.getFileFromList(index, populateFile);
	dwr.util.setValue("hiddenFileIndex", index);
	openSubmissionForm("File");
}
/* end of set submit file form */

