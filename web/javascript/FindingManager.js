function setNameOptionsByCharName(columnNumber) {
	var charName = dwr.util.getValue("charName");
	var charType = dwr.util.getValue("charType");
	var assayType = dwr.util.getValue("assayType");
	var columnType = dwr.util.getValue("columnType" + columnNumber);
	var currentColumnName = dwr.util.getValue("theColumnName"+columnNumber);
	dwr.util.setValue("constantValue" + columnNumber, "");
	
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
					// add the current value to the list if not in the list
					if (currentColumnName!="" && data.toString().indexOf(currentColumnName)==-1) {
						dwr.util.addOptions("columnName"+columnNumber,[currentColumnName]);
					}
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
						// add the current value to the list if not in the list
						if (currentColumnName!="" && data.toString().indexOf(currentColumnName)==-1) {
							dwr.util.addOptions("columnName"+columnNumber,[currentColumnName]);
						}
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
	var currentConditionProperty=dwr.util.getValue("theConditionProperty"+columnNumber);
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
					// add the current value to the list if not in the list
					if (currentConditionProperty!="" && data.toString().indexOf(currentConditionProperty)==-1) {
						dwr.util.addOptions("conditionProperty"+columnNumber,[currentConditionProperty]);
					}
				});
	}
}
function setColumnValueUnit(columnNumber) {	
	var currentValueUnit=dwr.util.getValue("theValueUnit"+columnNumber);

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
		// add the current value to the list if not in the list
		if (currentValueUnit!="" && data.toString().indexOf(currentValueUnit)==-1) {
			dwr.util.addOptions("valueUnit"+columnNumber,[currentValueUnit]);
		}		
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
	var fileDiv = document.getElementById('newFile');
	if (fileDiv != null) {
		var displayStatus = fileDiv.style.display;
		if (displayStatus == 'block') {
			alert("Please click on either the Add button or the Cancel button in the File form.");
			return false;
		}
		else {
			if (validateMatrix() && validateShapeInfo() && validateSolubilityInfo() && 
				validateSavingTheData('newExperimentConfig', 'Technique and Instrument') 
				) {
			    submitAction(document.forms[0], actionName, "saveFinding", 4);
				return true;
			} else {
				return false;
			}
		}
	}
}
function validateMatrix() {
	var rowNum = document.getElementById("matrixRowNum").value;
	var colNum = document.getElementById("matrixColNum").value;
	// Iterate matrix to check for empty column headers
	for (var cInd = 0; cInd < colNum; cInd++) {
		var columnType=dwr.util.getValue("theColumnType"+cInd);
		var columnName=dwr.util.getValue("theColumnName"+cInd);
		
		if (columnType==""||columnName=="" && !(rowNum==0 &&colNum==0)) {
			alert("Please click on the column header to update the column type and column name.");
			return false;
		}
	}
	
	// Iterate matrix to check for empty row.
	for (var rInd = 0; rInd < rowNum; rInd++) {
		var cInd = 0;
		for (; cInd < colNum; cInd++) {
			var cell = document.getElementById("cellValue" + rInd + ":" + cInd);
			if (cell != null && cell.value != "") {
				break;
			}
		}
		if (cInd == colNum) {
			alert("An empty row is not allowed in Data and Conditions table.");
			return false;
		}
	}
	// Iterate matrix to check for empty column
	for (var cInd = 0; cInd < colNum; cInd++) {
		var rInd = 0;
		for (; rInd < rowNum; rInd++) {
			var cell = document.getElementById("cellValue" + rInd + ":" + cInd);
			if (cell != null && cell.value != "") {
				break;
			}
		}
		if (rInd == rowNum) {
			alert("An empty column is not allowed in Data and Conditions table.");
			return false;
		}		
	}
	// validate each datum entry for valid float number.
	for (var rInd = 0; rInd < rowNum; rInd++) {
		for (var cInd = 0; cInd < colNum; cInd++) {
			var header = document.getElementById("theColumnType" + cInd);
			var cell = document.getElementById("cellValue" + rInd + ":" + cInd);
			if (cell != null && header != null) {
				if (header.value == "datum" && cell.value != "" &&
					!validFloatNumber(cell.value)) {
					alert("Please enter a valid number for datum value.");
					return false;
				}
			}
		}
	}
	// validate each datum entry for valid boolean value.
	for (var rInd = 0; rInd < rowNum; rInd++) {
		for (var cInd = 0; cInd < colNum; cInd++) {
			var header = document.getElementById("theValueType" + cInd);
			var cell = document.getElementById("cellValue" + rInd + ":" + cInd);
			if (cell != null && header != null) {
				if (header.value == "boolean" && cell.value != "" &&
					cell.value != "0" && cell.value != "1") {
					alert("Please enter 1 or 0 for values of boolean type.");
					return false;
				}
			}
		}
	}
	return true;
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

function updateValueType(columnNumber) {
	
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
	if ((columnHeader.columnName == null || columnHeader.columnName == "")) {
		alert("Please specify Column Name when saving change(s).");
		return false;
	}
	if (columnHeader.columnType == null || columnHeader.columnType == "") {
		alert("Please specify Column Type when saving change(s).");
		return false;
	}
	
	var numberOfRows = dwr.util.getValue("rowNum");
	if (columnHeader.constantValue != null && columnHeader.constantValue != "") {
		if (columnHeader.columnType == "datum" && 
			!validFloatNumber(columnHeader.constantValue)) {
			alert("Please enter a valid number for constant datum value.");
			dwr.util.setValue("constantValue" + columnNumber, "");
			return false;
		}
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

/* set submit file form */
function clearFile() {
	// go to server and clean form bean
	FindingManager.resetTheFile(populateFile);
	hide("deleteFile");
	show("load");
	hide("link");
	dwr.util.setValue("uploadedFile", "");
	dwr.util.setValue("externalUrl", "");
	
	//FR# [26487], Must reset hidden file field for new file.
	dwr.util.setValue("hiddenFileId", "");
	dwr.util.setValue("hiddenFileUri", "");
	dwr.util.setValue("hiddenFileIndex", -1);
}
function setTheFile(index) {
	FindingManager.getFileFromList(index, populateFile);
	dwr.util.setValue("hiddenFileIndex", index);
	show("deleteFile");
	show("newFile");
}
/* end of set submit file form */
