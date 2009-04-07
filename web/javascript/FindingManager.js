var currentFinding = null;
var rowCount = 0;
var rowCache = {};
var dataColumnCache = {};
var headerColumnCount = 0;
var datumHeaderColumnCount = 0;
var conditionHeaderColumnCount = 0;

var addNewColumn = true;
var editFinding = false;
var fixId = "-10000";
var fixConditionColIndex = 10000; // starting index of condition
var tempDatum = null;

function setNameOptionsByCharName(charName) {
    if (charName=="") {
	  charName = document.getElementById("charName").value;
	}
	var datumOrCondition=document.getElementById("datumOrCondition").value;
	if (datumOrCondition=="Datum") {
	   $("conditionProperty").style.display = 'none';
	   FindingManager.getDatumNameOptions(charName, function(data) {
		 dwr.util.removeAllOptions("name");
		 dwr.util.addOptions("name", [ "" ]);
		 dwr.util.addOptions("name", data);
		 dwr.util.addOptions("name", [ "[Other]" ]);
	   });
	}
	else if (datumOrCondition=="Condition") {
	  $("conditionProperty").style.display = "";
	  FindingManager.getConditionOptions( function(data) {
		dwr.util.removeAllOptions("name");
		dwr.util.addOptions("name", [ "" ]);
		dwr.util.addOptions("name", data);
		dwr.util.addOptions("name", [ "[Other]" ]);
	  });
	}
	else {
	    $("conditionProperty").style.display = 'none';
	    dwr.util.removeAllOptions("name");
		dwr.util.addOptions("name", [ "" ]);
		dwr.util.addOptions("name", [ "[Other]" ]);
	}
}

function setConditionPropertyOptionsByCharName(conditionName) {
	if (document.getElementById('datumOrCondition').value == 'Condition') {
		if (conditionName == null) {
			conditionName = document.getElementById("name").value;
		}
		FindingManager.getConditionPropertyOptions(conditionName,
				function(data) {
					dwr.util.removeAllOptions("property");
					dwr.util.addOptions("property", [ "" ]);
					dwr.util.addOptions("property", data);
					dwr.util.addOptions("property", [ "[Other]" ]);
				});
	}
}

function setColumnValueUnit() {
	var name = null, property = null;
	if (document.getElementById('datumOrCondition').value == 'Condition') {
		property = document.getElementById("property").value;
	}
	name = document.getElementById("name").value;
	FindingManager.getColumnValueUnitOptions(name, property,
				function(data) {
					dwr.util.removeAllOptions("valueUnit");
					dwr.util.addOptions("valueUnit", [ "" ]);
					dwr.util.addOptions("valueUnit", data);
					dwr.util.addOptions("valueUnit", [ "[Other]" ]);
				});

}

function cancelFinding() {
	if (confirm("Are you sure you want to discard all the changes you made??")) {
		resetTheFinding(false);
	}
	return;
}

function resetTheFinding(isShow) {
	editFinding = false;
	headerColumnCount = 0;
	datumHeaderColumnCount = 0;
	conditionHeaderColumnCount = 0;
	columnCount = 0;
	rowCount = 0;
	if (isShow) {
		show('newFinding');
		/*hide('populateDataTable');*/
		/*hide('existingFinding');*/
	} else {
		hide('newFinding');
		show('existingFinding');
		return;
	}
	FindingManager.resetFinding( function(theFinding) {
		currentFinding = theFinding;
	});
	dwr.util.setValue("name", "");
	dwr.util.setValue("valueType", "");
	dwr.util.setValue("valueUnit", "");
	dwr.util.setValue("value", "");
	dwr.util
			.removeAllRows(
					"datumMatrix",
					{
						filter : function(tr) {
							return (tr.id != "datumMatrixPatternRow" && tr.id != "matrixHeader");
						}
					});

//	for ( var i = 1; i < columnCount + 1; i++) {
//		var cell = document.createElement("TD");
//		var span = document.createElement('SPAN');
//		span.setAttribute("id", "matrixHeaderColumn" + i);
//		span.setAttribute("class", "greyFont2");
//		span.appendChild(document.createTextNode('Header'));
//		cell.appendChild(span);
//		matrixHeader.appendChild(cell);
//	}
	$("matrixHeader").style.display = "";
	$("datumMatrixPatternRow").style.display = "none";
	$("matrixHeader").style.display = "none";
	$("datumColumnPattern").style.display = "none";
	$("columnPattern").style.display = "none";
	$("datumColumns").style.display = "none";
	$("datumColumnPatternRowDisplay").style.display = "none";
	$("addRowButtons").style.display = "none";
	// $("datumColumnsDivRow").style.display = "none";
	$(dataMatrixDiv).style.display = "none";
	$("columnLabelsDiv").style.display = "none";
	clearTheRow();
}

function setTheFinding(findingId) {
	resetTheFinding(true);
	editFinding = true;
	show('submitDatum');
	show('populateDataTable');
	FindingManager.findFindingById(findingId, populateFinding);
}

function populateFinding(finding) {
	if (finding != null && finding.rows != null
			&& finding.rows.length > 0) {
		currentFinding = finding;

		for ( var index = 0; index < currentFinding.rows.length; index++) {
			var conditions = currentFinding.rows[index].conditions;
			var id;
			for ( var i = 0; i < conditions.length; i++) {
				addColumn(conditions[i]);
			}
		}
		for ( var index = 0; index < currentFinding.rows.length; index++) {
			var data = currentFinding.rows[index].data;
			var id;
			if (index == 0) {
				setTheRow(currentFinding.rows[index]);
			}
			headerColumnCount = 0;
			datumHeaderColumnCount = 0;
			conditionHeaderColumnCount = 0;
			for ( var i = 0; i < data.length; i++) {
				addColumn(data[i]);
			}
		}
		for ( var index = 0; index < currentFinding.rows.length; index++) {
			if (rowCount == 0) {
				addNewColumn = false;
				$(dataMatrixDiv).style.display = "";
			}
			rowCount++;
		}
	}
}

function saveFinding(actionName) {
	document.getElementById("theFindingId").value=currentFinding.domain.id;
	submitAction(document.forms[0], actionName, 'saveFinding');
}

//add new column (datum or condition)
function addColumn() {
	addColumn(null);
}

//edit existing column (datum or condition)
function addColumn(myColumn) {
	var columnBean = {
		name :null,
		valueType :null,
		valueUnit :null,
		value :null,
		property :null,
		datumOrCondition :null
	};
	if (myColumn != null) {
		columnBean = myColumn;
	} else {
		dwr.util.getValues(columnBean);
		columnBean.id = document.getElementById("columnId").value;
		//ordering of columns
		if (columnBean.id == null || columnBean.id == ''
				|| columnBean.id == 'null') {
			if (columnBean.datumOrCondition == 'Condition') {
				columnBean.id = -fixConditionColIndex
						- conditionHeaderColumnCount - 1;
			} else {
				columnBean.id = -datumHeaderColumnCount - 1;
			}
		}
	}
	//redraw design columns and redraw data table columns
	if (columnBean.name != '' || columnBean.valueType != ''
			|| columnBean.valueUnit != '') {
		if (headerColumnCount == 0) {
			var datumColumnPatternRowDisplay = document
					.getElementById("datumColumnPatternRowDisplay");
			var aCellsDisplay = datumColumnPatternRowDisplay
					.getElementsByTagName('td')// cells collection in this row
			var aCellLengthDisplay = aCellsDisplay.length;
			var toDeleteDisplay = new Array();
			i = 0;

			for ( var j = 0; j < aCellLengthDisplay; j++) {
				if (aCellsDisplay[j].id != 'columnPattern') {
					toDeleteDisplay[i] = aCellsDisplay[j].id;
					i++;
				}
			}
			for ( var j = 0; j < toDeleteDisplay.length; j++) {
				datumColumnPatternRowDisplay.removeChild(document
						.getElementById(toDeleteDisplay[j]));
			}
			$("columnLabelsDiv").style.display = "";
		}

		addNewColumn = true;
		if (myColumn == null) {
			if (columnBean.id == null || columnBean.id == ''
					|| columnBean.id == 'null') {
				if (columnBean.datumOrCondition == 'Condition') {
					columnBean.id = -fixConditionColIndex
							- conditionHeaderColumnCount - 1;
				} else {
					columnBean.id = -datumHeaderColumnCount - 1;
				}
			}
			FindingManager.addColumnHeader(columnBean, function(theFinding) {
				currentFinding = theFinding;
			});
		}
		$("addRowButtons").style.display = "";
		// createMatrixPattern();
		// if edit existing column, headerColumnCount++ not work, use
		// incrementColumnCount
		//to ensure ordering of steps in AJAX
		window.setTimeout("updateColumnCount()", 80);
		window.setTimeout("fillColumnTable()", 100);
		window.setTimeout("createMatrixPattern()", 150);
		clearTheDataColumn();
		window.setTimeout("fillMatrix()", 200);
	} else {
		alert('Please fill in values');
	}
}

function updateColumnCount() {
	headerColumnCount = currentFinding.columnBeans.length;
	datumHeaderColumnCount = currentFinding.datumColumnBeans.length;
	conditionHeaderColumnCount = currentFinding.conditionColumnBeans.length;
}

//data table pattern (data row)
function createMatrixPattern() {
	var matrixHeader = document.getElementById("matrixHeader");
	matrixHeader = removeAllColumns(matrixHeader);
	var headerName, headerValueType, headerValueUnit;

	for ( var i = fixConditionColIndex + 1; i < conditionHeaderColumnCount
			+ fixConditionColIndex + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "matrixHeaderColumn" + i);
		span.setAttribute("class", "greyFont2");
		headerName = dwr.util.getValue("columnLabel" + (-i));
		headerValueType = dwr.util.getValue("datumColumnValueType" + (-i));
		headerValueUnit = dwr.util.getValue("datumColumnValueUnit" + (-i));
		span.appendChild(document.createTextNode(headerName));
		cell.appendChild(span);
		matrixHeader.appendChild(cell);
	}
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "matrixHeaderColumn" + i);
		span.setAttribute("class", "greyFont2");
		headerName = dwr.util.getValue("columnLabel" + (-i));
		headerValueType = dwr.util.getValue("datumColumnValueType" + (-i));
		headerValueUnit = dwr.util.getValue("datumColumnValueUnit" + (-i));
		span.appendChild(document.createTextNode(headerName));
		cell.appendChild(span);
		matrixHeader.appendChild(cell);
	}
	$("matrixHeader").style.display = "";
	var datumMatrixPatternRow = document
			.getElementById("datumMatrixPatternRow");
	datumMatrixPatternRow = removeAllColumns(datumMatrixPatternRow);


	for ( var i = fixConditionColIndex + 1; i < conditionHeaderColumnCount
			+ fixConditionColIndex + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "datumMatrixValue" + i);
		span.setAttribute("class", "greyFont2");
		span.appendChild(document.createTextNode(''));
		cell.appendChild(span);
		datumMatrixPatternRow.appendChild(cell);
	}
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "datumMatrixValue" + i);
		span.setAttribute("class", "greyFont2");
		span.appendChild(document.createTextNode(''));
		cell.appendChild(span);
		datumMatrixPatternRow.appendChild(cell);
	}


	var buttonCell = document.createElement("TD");
	buttonCell.setAttribute("id", "selectButtonCell");
	var button = document.createElement('input');
	button.setAttribute("id", "editDatumRow");
	button.setAttribute("type", "button");
	button.setAttribute("class", "noBorderButton");
	button.setAttribute("value", "Select");
	button
			.setAttribute("style",
					"border:0px solid;text-decoration: underline;");
	buttonCell.appendChild(button);
	datumMatrixPatternRow.appendChild(buttonCell);
}

function addRow() {
	addNewColumn = false;
	var id = -1;
	var datumArray = new Array();
	var conditionArray = new Array();
	var datumid = null;
	var datumOrCondition = null;
	var datumIndex = 0, conditionIndex = 0;

	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount
			+ fixConditionColIndex; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datumid = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datumid == null || datumid == 'datumColumnId') {
			datumid = null;
		}
		id = datum.id;
		if (id == null) {
			id = (-i - 1);
		}
		datum.name = dwr.util.getValue("datumColumnName" + id);
		datum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		datum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		datum.value = dwr.util.getValue("datumColumnValue" + id);
		datumOrCondition = dwr.util.getValue("datumOrConditionColumn" + id);
		if (datumOrCondition == 'Condition') {
			datum.property = dwr.util.getValue("conditionColumnProperty" + id);
		} else {
			datum.property = '';
		}
		datum.id = datumid;
		if (datum.id == null || datum.id == '') {
			datum.id = (-rowCount - 1) * 1000 + (-i - 1);
		}
		if (datumOrCondition == 'Condition') {
			conditionArray[conditionIndex] = datum;
			conditionIndex++;
		} else {
			datumArray[datumIndex] = datum;
			datumIndex++;
		}
	}

	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datumid = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datumid == null || datumid == 'datumColumnId') {
			datumid = null;
		}
		id = datum.id;
		if (id == null) {
			id = (-i - 1);
		}
		datum.name = dwr.util.getValue("datumColumnName" + id);
		datum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		datum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		datum.value = dwr.util.getValue("datumColumnValue" + id);
		datumOrCondition = dwr.util.getValue("datumOrConditionColumn" + id);
		if (datumOrCondition == 'Condition') {
			datum.property = dwr.util.getValue("conditionColumnProperty" + id);
		} else {
			datum.property = '';
		}
		datum.id = datumid;
		if (datum.id == null || datum.id == '') {
			datum.id = (-rowCount - 1) * 1000 + (-i - 1);
		}
		if (datumOrCondition == 'Condition') {
			conditionArray[conditionIndex] = datum;
			conditionIndex++;
		} else {
			datumArray[datumIndex] = datum;
			datumIndex++;
		}
	}
	if (datumIndex > 0) {
		FindingManager.addRow(datumArray, conditionArray, function(theFinding) {
			currentFinding = theFinding;
		});
	} else {
		alert('Please fill in at least one Datum');
	}
	if (rowCount == 0) {
		$(dataMatrixDiv).style.display = "";
	}
	rowCount++;
	window.setTimeout("fillMatrix()", 200);
}

function deleteRow() {
	var datumArray = new Array();
	var conditionArray = new Array();
	var datumIndex = 0, conditionIndex = 0;
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount
			+ fixConditionColIndex; i++) {
		var datum = {
			name :null,
			valueType :null,
			datumOrCondition :null
		};
		dwr.util.getValues(datum);
		datum.id = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datum.datumOrCondition == 'Condition') {
			conditionArray[conditionIndex] = datum;
		} else {
			datumArray[datumIndex] = datum;
		}
		conditionIndex++;
		datumIndex++;
	}
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			datumOrCondition :null
		};
		dwr.util.getValues(datum);
		datum.id = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datum.datumOrCondition == 'Condition') {
			conditionArray[conditionIndex] = datum;
		} else {
			datumArray[datumIndex] = datum;
		}
		conditionIndex++;
		datumIndex++;
	}
	FindingManager.deleteRow(datumArray, conditionArray, function(theFinding) {
		currentFinding = theFinding;
	});
	rowCount--;
	window.setTimeout("fillMatrix()", 200);
}

function clearTheRow() {
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount
		+ fixConditionColIndex; i++) {
		document.getElementById("datumColumnId" + (-i - 1)).value = '';
		document.getElementById("datumColumnValue" + (-i - 1)).value = "";
	}
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		document.getElementById("datumColumnId" + (-i - 1)).value = '';
		document.getElementById("datumColumnValue" + (-i - 1)).value = "";
	}

}

function clearTheDataColumn() {
	// IE works only set value = '' instead of null
	document.getElementById("columnId").value = '';
	document.getElementById("datumOrCondition").value = "";
	document.getElementById("property").value = "";
	tempDatum = null;
	window.setTimeout("setDatumValues()", 100);
}

function setTheRow(row) {
	currentFinding.theRow = row;
	// var datum = currentFinding.theRow.data[headerColumnCount-1];
	// document.getElementById("name").value = datum.name;
	// document.getElementById("value").value = datum.value;
	// document.getElementById("name").value = datum.name;
	// document.getElementById("name").value = datum.name;
}

function removeColumns(columnName, exceptColumn) {
	// remove datumColumnPatternRowDisplay all column except
	// columnPattern
	var columnVariable = document.getElementById(columnName);
	var aCellsDisplay = columnVariable.getElementsByTagName('td')// cells
																	// collection
																	// in this
																	// row
	var aCellLengthDisplay = aCellsDisplay.length;
	var toDeleteDisplay = new Array();
	i = 0;
	for ( var j = 0; j < aCellLengthDisplay; j++) {
		if (aCellsDisplay[j].id != exceptColumn) {
			toDeleteDisplay[i] = aCellsDisplay[j].id;
			i++;
		}
	}
	for ( var j = 0; j < toDeleteDisplay.length; j++) {
		datumColumnPatternRowDisplay.removeChild(document
				.getElementById(toDeleteDisplay[j]));
	}
	// $("columnLabelsDiv").style.display = "";

}

function fillColumnTable() {
	$("datumColumns").style.display = "";
	$("datumColumnPatternRowDisplay").style.display = "";
	$("datumColumnPattern").style.display = "none";
	$("columnPattern").style.display = "none";
	var start = 0;
	var columnBean, id;
	var columnBeanid = null;
	var valueTypeUnit = null;

	// REMOVE
	// remove datumColumnPatternRowDisplay all column except
	// columnPattern
	removeColumns('datumColumnPatternRowDisplay', 'columnPattern')
	// remove "datumColumns" all rows except datumColumnPattern
	dwr.util
			.removeAllRows(
					"datumColumns",
					{
						filter : function(tr) {
							return (tr.id != "datumColumnPattern" && tr.id != "datumColumnsDivRow2");
						}
					});

	if (currentFinding.conditionColumnBeans != null
			&& currentFinding.conditionColumnBeans.length > 0) {
		fillColumns(editFinding, fixConditionColIndex,
				currentFinding.conditionColumnBeans)
	}
	fillColumns(editFinding, start, currentFinding.datumColumnBeans);

}

function fillColumns(editFinding, start, columnBeans) {
	var columnBean, id;
	var columnBeanid = null;
	var valueTypeUnit = null;
	// TODO:: verify when editing, i< columnBeans.length or
	// i<columnBeans.length+start

	for ( var i = start; i < columnBeans.length + start; i++) {
		columnBean = columnBeans[i - start];
		columnBeanid = columnBean.id;
		id = -i - 1;
		if (columnBeanid == null) {
			columnBeanid = id;
		}
		dwr.util.cloneNode("datumColumnPattern", {
			idSuffix :id
		});
		dwr.util.cloneNode("columnPattern", {
			idSuffix :id
		});
		dwr.util.setValue("datumColumnId" + id, columnBeanid);
		// TODO:::?? this one or the one above??
		// dwr.util.setValue("datumColumnId" + (-i-1), datumid);
		dwr.util.setValue("datumColumnName" + id, columnBean.name);
		dwr.util.setValue("datumColumnValueType" + id, columnBean.valueType);
		dwr.util.setValue("datumColumnValueUnit" + id, columnBean.valueUnit);
		dwr.util.setValue("datumColumnValue" + id, columnBean.value);
		dwr.util.setValue("datumOrConditionColumn" + id,
				columnBean.datumOrCondition);
		dwr.util.setValue("conditionColumnProperty" + id, columnBean.property);
		dwr.util
				.setValue("datumColumnNameDisplay" + id, columnBean.columnLabel);
		dwr.util.setValue("columnLabel" + id, columnBean.columnLabel);
		if (!editFinding) {
			dwr.util.setValue("datumColumnValue" + id, document
					.getElementById("value").value);
			dwr.util.setValue("datumColumnId" + id, document
					.getElementById("columnId").value);
		}
		// dummmy get data, todo: check if needed,
		// actually getting data from colDatum.name =
		// dwr.util.getValue("datumColumnName" + id); etc...
		var colDatum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(colDatum);
		colDatum.id = dwr.util.getValue("datumColumnId" + id);// ==datumid
		if (colDatum.id == '' || colDatum.id == null) {
			colDatum.id = id;
		}
		colDatum.name = dwr.util.getValue("datumColumnName" + id);
		colDatum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		colDatum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		colDatum.value = dwr.util.getValue("datumColumnValue" + id);
		colDatum.datumOrCondition = dwr.util.getValue("datumOrConditionColumn"
				+ id);
		colDatum.property = dwr.util.getValue("conditionColumnProperty" + id);
		colDatum.columnLabel = dwr.util.getValue("columnLabel" + id);
		dataColumnCache[id] = colDatum;
		$("datumColumnPattern" + id).style.display = "";
		$("columnPattern" + id).style.display = "";
	}
}

function fillMatrix() {
	dwr.util
			.removeAllRows(
					"datumMatrix",
					{
						filter : function(tr) {
							return (tr.id != "datumMatrixPatternRow" && tr.id != "matrixHeader");
						}
					});
	var datum, condition, id;
	var rowId;
	var datumMatrixPatternRow = document
			.getElementById("datumMatrixPatternRow");
	var aCells = datumMatrixPatternRow.getElementsByTagName('td')
	var aCellLength = aCells.length;
	for ( var row = 0; row < currentFinding.rows.length; row++) {
		// DATUM
		var data = currentFinding.rows[row].data;
		rowId = currentFinding.rows[row].rowNumber;
		if (rowId == null || rowId == '') {
			rowId = -row - 1;
		}
		dwr.util.cloneNode("datumMatrixPatternRow", {
			idSuffix :rowId
		});
		var colIndex = 0;
		for ( var i = 0; i < data.length; i++) {
			datum = data[i];
			if (datum.id == null || datum.id == '') {
				datum.id = -i - 1;
			}
			id = datum.id;
			id = rowId;
			dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + rowId,
					datum.value);

		}
		colIndex = fixConditionColIndex;
		// CONDITION
		var conditions = currentFinding.rows[row].conditions;
		for ( var i = 0; i < conditions.length; i++) {
			condition = conditions[i];
			if (condition.id == null || condition.id == '') {
				condition.id = -colIndex - 1;
			}
			id = condition.id;
			id = rowId;
			dwr.util.setValue("datumMatrixValue" + (colIndex + 1) + "" + rowId,
					condition.value);
			colIndex++;
		}

		// select button
		var editRow = document.getElementById("editDatumRow" + rowId);
		editRow.onclick = function() {
			editDatumClicked(this.id)
		};
		editRow.setAttribute("style", "border:0px solid;");
		// editRow.style = "border:0px solid;text-decoration: underline;";
		$("datumMatrixPatternRow" + rowId).style.display = "";
		rowCache[rowId] = currentFinding.rows[row];
		rowCache[rowId].rowNumber = rowId;
	}
	clearTheRow();
	cloneEditRow();
}

//draw input box one column after another at the last row
function cloneEditRow() {
	$(dataMatrixDiv).style.display = "";
	var rowIndex = rowCount;
	var rowId = fixId;
	dwr.util.cloneNode("datumMatrixPatternRow", {
		idSuffix :rowId
	});

	// remove all columns in datumMatrixPatternRow_RowId
	var datumMatrixPatternRow = document.getElementById("datumMatrixPatternRow"
			+ rowId);
	datumMatrixPatternRow = removeAllColumns(datumMatrixPatternRow);

	for ( var i = fixConditionColIndex + 1; i < conditionHeaderColumnCount
			+ fixConditionColIndex + 1; i++) {
		var cell = document.createElement("TD");
		var textValue = document.createElement('input');
		textValue.setAttribute("id", "datumMatrixValue" + i + rowId);
		textValue.setAttribute("type", "text");
		textValue.setAttribute("value", "");
		textValue.setAttribute("size", "5");
		cell.appendChild(textValue);
		datumMatrixPatternRow.appendChild(cell);
	}
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		var cell = document.createElement("TD");
		var textValue = document.createElement('input');
		textValue.setAttribute("id", "datumMatrixValue" + i + rowId);
		textValue.setAttribute("type", "text");
		textValue.setAttribute("value", "");
		textValue.setAttribute("size", "5");
		cell.appendChild(textValue);
		datumMatrixPatternRow.appendChild(cell);
	}

	var buttonCell = document.createElement("TD");
	buttonCell.setAttribute("id", "saveButtonCell");
	var button = document.createElement('input');
	button.setAttribute("id", "editDatumRow" + rowId);
	button.onclick = function() {
		saveRowClicked(this.id)
	};
	button.setAttribute("type", "button");
	button.setAttribute("class", "noBorderButton");
	button.setAttribute("value", "Save");
	buttonCell.appendChild(button);

	var deleteButton = document.createElement('input');
	deleteButton.setAttribute("id", "deleteDatumRow" + rowId);
	deleteButton.onclick = function() {
		deleteRowClicked(this.id)
	};
	deleteButton.setAttribute("type", "button");
	deleteButton.setAttribute("class", "noBorderButton");
	deleteButton.setAttribute("value", "Delete");
	buttonCell.appendChild(deleteButton);

	datumMatrixPatternRow.appendChild(buttonCell);

	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datum.id = -i - 1;
		id = rowId;
		if (currentFinding.datumColumnBeans[i].id < 0) {
			dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + id,
					currentFinding.datumColumnBeans[i].value);
		}

		var tempValue = document.getElementById("datumMatrixValue" + (i + 1)
				+ "" + id);
	}
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount
			+ fixConditionColIndex; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datum.id = -i - 1 - fixConditionColIndex;
		id = rowId;
		// fixConditionColIndex
		if (currentFinding.conditionColumnBeans[i - fixConditionColIndex].id < 0) {
			dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + id,
					currentFinding.conditionColumnBeans[i
							- fixConditionColIndex].value);
		}
		var tempValue = document.getElementById("datumMatrixValue" + (i + 1)
				+ "" + id);
	}
	$("datumMatrixPatternRow" + rowId).style.display = "";
}

var editRowId = -1;
function editDatumClicked(eleid) {
	currentFinding.theRow = rowCache[eleid.substring(12)];
	var rowid = eleid.substring(12);
	var data = rowCache[rowid].data;
	var datum, condition;
	for ( var i = 0; i < data.length; i++) {
		datum = data[i];
		// var datumid = datum.id;
		document.getElementById("datumColumnId" + (-i - 1)).value = datum.id;
		document.getElementById("datumMatrixValue" + (i + 1) + fixId).value = datum.value;
	}
	var conditions = rowCache[rowid].conditions;
	// var condIndex = data.length;
	var condIndex = fixConditionColIndex;
	for ( var i = 0; i < conditions.length; i++) {
		condition = conditions[i];
		// var conditionid = condition.id;
		document.getElementById("datumColumnId" + (-condIndex - 1)).value = condition.id;
		document.getElementById("datumMatrixValue" + (condIndex + 1) + fixId).value = condition.value;
		// alert("######## condition datumMatrixValue "+(condIndex + 1) + fixId
		// + " ==>"+condition.value);
		condIndex++;
	}
}

function saveRowClicked(eleid) {
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		document.getElementById("datumColumnValue" + (-i)).value = dwr.util
				.getValue("datumMatrixValue" + i + fixId);
	}
	for ( var i = fixConditionColIndex + 1; i < conditionHeaderColumnCount
			+ fixConditionColIndex + 1; i++) {
		document.getElementById("datumColumnValue" + (-i)).value = dwr.util
				.getValue("datumMatrixValue" + i + fixId);
	}
	addRow();
}

function deleteRowClicked(eleid) {
	if (confirm("Are you sure you want to delete this row ?")) {
		for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
			document.getElementById("datumColumnValue" + (-i)).value = dwr.util
					.getValue("datumMatrixValue" + i + fixId);
		}
		for ( var i = fixConditionColIndex + 1; i < conditionHeaderColumnCount
				+ fixConditionColIndex + 1; i++) {
			document.getElementById("datumColumnValue" + (-i)).value = dwr.util
					.getValue("datumMatrixValue" + i + fixId);
		}
		deleteRow();
	}
}

//add a new row, clear input fields
function newRowClicked(eleid) {
	clearTheRow();
	var row = fixId;
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + fixId, '');

	}
	// colIndex = data.length;
	var colIndex = fixConditionColIndex;
	// CONDITION
	for ( var i = 0; i < conditionHeaderColumnCount; i++) {
		dwr.util.setValue("datumMatrixValue" + (colIndex + 1) + "" + fixId, '');
		colIndex++;
	}

}

function editColumn(eleid) {
	var id = eleid.substring(22);
	var datum = dataColumnCache[id];
	tempDatum = datum;
	document.getElementById("columnId").value = datum.id;
	document.getElementById("datumOrCondition").value = datum.datumOrCondition;
	document.getElementById("name").value = datum.name;
	//showDatumConditionInfo(datum.name);
	show('columnDesign');
	window.setTimeout("setDatumValues()", 100);

}

function setDatumValues() {
	if (tempDatum != null) {
		document.getElementById("name").value = tempDatum.name;
		if (tempDatum.datumOrCondition == 'Condition') {
			document.getElementById("property").value = tempDatum.property;
		}
		document.getElementById("valueType").value = tempDatum.valueType;
		document.getElementById("valueUnit").value = tempDatum.valueUnit;
		document.getElementById("value").value = tempDatum.value;
		tempDatum = null;
	} else {
		document.getElementById("name").value = "";
		document.getElementById("valueUnit").value = "";
		document.getElementById("valueType").value = "";
		document.getElementById("value").value = "";
	}
}

function deleteDatumColumn() {
	if (confirm("Are you sure you want to delete this column?")) {
		var columnBean = {
			datumOrCondition :null
		};
		dwr.util.getValues(columnBean);
		columnBean.id = document.getElementById("columnId").value;
		FindingManager.removeColumnHeader(columnBean, function(theFinding) {
			currentFinding = theFinding;
		});

		$("addRowButtons").style.display = "";
		// createMatrixPattern();
		// if edit existing column, headerColumnCount++ not work, use
		window.setTimeout("updateColumnCount()", 80);
		window.setTimeout("fillColumnTable()", 100);
		window.setTimeout("createMatrixPattern()", 150);
		clearTheDataColumn();
		window.setTimeout("fillMatrix()", 200);
	}
}

function removeAllColumns(theRow) {
	var aCells = theRow.getElementsByTagName('TD')// cells collection in this
	// row
	var aCellLength = aCells.length;
	var toDelete = new Array();
	var i = 0;
	// aCells[j] is dynamically updated if removeChild
	// so put it to toDelete, then removeChild
	for ( var j = 0; j < aCellLength; j++) {
		toDelete[j] = aCells[j];
	}
	for ( var j = 0; j < toDelete.length; j++) {
		theRow.removeChild(toDelete[j]);
	}
	return theRow;
}
// for dbug
function checkNumOfColumns(rowId) {
	var theRow = document.getElementById(rowId);
	var aCells = theRow.getElementsByTagName('TD')// cells collection in this
	// row
	var aCellLength = aCells.length;
	alert(rowId + " has " + aCellLength + " columns");
}

function removeNode(elementId) {
	dwr.util._temp = dwr.util.byId(elementId);
	if (dwr.util._temp) {
		dwr.util._temp.parentNode.removeChild(dwr.util._temp);
		dwr.util._temp = null;
	}
}

function addSaveRowButton(rowId) {
	if (document.getElementById("saveDatumRow") == null) {
		var selectButtonCell = document.getElementById("selectButtonCell"
				+ rowId);
		var saveRowButton = document.createElement("input");
		saveRowButton.setAttribute("id", "saveDatumRow");
		saveRowButton.setAttribute("type", "button");
		saveRowButton.setAttribute("class", "noBorderButton");
		saveRowButton.setAttribute("value", "Save");
		saveRowButton.setAttribute("style",
				"border:0px solid;text-decoration: underline;");
		saveRowButton.onclick = function() {
			saveRowClicked(this.id)
		};
		selectButtonCell.appendChild(saveRowButton);
	}
}

//not currently used
function addEditButton(rowId) {
	if (document.getElementById("editDatumRow") == null) {
		var selectButtonCell = document.getElementById("selectButtonCell"
				+ rowId);
		var editButton = document.createElement("input");
		editButton.setAttribute("id", "editDatumRow");
		editButton.setAttribute("type", "button");
		editButton.setAttribute("class", "noBorderButton");
		editButton.setAttribute("value", "Save");
		editButton.setAttribute("style",
				"border:0px solid;text-decoration: underline;");
		editButton.onclick = function() {
			editRowClicked(this.id)
		};
		selectButtonCell.appendChild(editButton);
	}
}

//not currently used
function addDeleteButton(rowId) {
	if (document.getElementById("deleteDatumRow") == null) {
		var selectButtonCell = document.getElementById("selectButtonCell"
				+ rowId);
		var deleteButton = document.createElement("input");
		deleteButton.setAttribute("id", "deleteDatumRow");
		deleteButton.setAttribute("type", "button");
		deleteButton.setAttribute("class", "noBorderButton");
		deleteButton.setAttribute("value", "Delete");
		deleteButton.setAttribute("style",
				"border:0px solid;text-decoration: underline;");
		deleteButton.onclick = function() {
			deleteRowClicked(this.id)
		};
		selectButtonCell.appendChild(deleteButton);
	}
}
