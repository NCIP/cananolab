var currentDataSet = null;
var rowCount = 0;
var dataRowCache = {};
var dataColumnCache = {};
var viewed = -1;
var headerColumnCount = 0;
var datumHeaderColumnCount = 0;
var conditionHeaderColumnCount = 0;

var addNewColumn = true;
var editDataSet = false;
var fixId = "-10000";
var fixConditionColIndex = 10000;
var tempDatum = null;

function setConditionPropertyOptionsByCharName(conditionName){
	if (document.getElementById('datumOrCondition').value=='Condition'){
		if (conditionName==null){
			conditionName = document.getElementById("name").value;
		}	
		DataSetManager.getConditionPropertyOptions(conditionName, function (data) {
			dwr.util.removeAllOptions("property");
			dwr.util.addOptions("property", [""]);
			dwr.util.addOptions("property", data);
			dwr.util.addOptions("property", ["[Other]"]);
		});
	}
}

function setDatumNameOptionsByCharName(){
	var charName = document.getElementById("charName").value;
	DataSetManager.getDatumNameOptions(charName, function (data) {
		dwr.util.removeAllOptions("name");
		dwr.util.addOptions("name", [""]);
		dwr.util.addOptions("name", data);
		dwr.util.addOptions("name", ["[Other]"]);
	});
}

function setConditionOptionsByCharName(){
	var charName = document.getElementById("charName").value;
	DataSetManager.getConditionOptions(function (data) {
		dwr.util.removeAllOptions("name");
		dwr.util.addOptions("name", [""]);
		dwr.util.addOptions("name", data);
		dwr.util.addOptions("name", ["[Other]"]);
	});
}

function showDatumConditionInfo(datumName){
	if (document.getElementById('datumOrCondition').value=='Condition'){
		$("conditionProperty").style.display = "";
		var charName = document.getElementById("charName").value;
		setConditionOptionsByCharName();
		if (datumName!=null){
			document.getElementById("name").value = datumName;
		}
		setConditionPropertyOptionsByCharName(datumName);
	}else{
		$("conditionProperty").style.display = 'none';
		setDatumNameOptionsByCharName();	
	}
}

function resetTheDataSet(isShow) {
	editDataSet = false;
	headerColumnCount = 0;
	datumHeaderColumnCount = 0;
	conditionHeaderColumnCount = 0;
	columnCount = 0;
	rowCount = 0;
	if (isShow) {
		show('newDataSet');
		hide('populateDataTable');
		hide('existingDataSet');
	} else {
		hide('newDataSet');
		show('existingDataSet');
		return;
	}
	DataSetManager.resetDataSet( function(theDataSet) {
		currentDataSet = theDataSet;
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

	for ( var i = 1; i < columnCount + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "matrixHeaderColumn" + i);
		span.setAttribute("class", "greyFont2");
		span.appendChild(document.createTextNode('Header'));
		cell.appendChild(span);
		matrixHeader.appendChild(cell);
	}
	$("matrixHeader").style.display = "";
	$("datumMatrixPatternRow").style.display = "none";
	$("matrixHeader").style.display = "none";
	$("datumColumnPattern").style.display = "none";
	$("datumColumnPatternDisplay").style.display = "none";
	$("datumColumns").style.display = "none";
	$("datumColumnPatternRowDisplay").style.display = "none";
	$("addRowButtons").style.display = "none";
	// $("datumColumnsDivRow").style.display = "none";
	$("datumMatrixDivRow").style.display = "none";
	$("datumColumnsDivRowDisplay").style.display = "none";
	clearTheDataRow();
}

function setTheDataSet(dataSetId) {
	resetTheDataSet(true);
	editDataSet = true;
	show('submitDatum');
	show('populateDataTable');
	DataSetManager.findDataSetById(dataSetId, populateDataSet);
}

function populateDataSet(dataSet) {
	if (dataSet != null && dataSet.dataRows != null
			&& dataSet.dataRows.length > 0) {
		currentDataSet = dataSet;
		for ( var index = 0; index < currentDataSet.dataRows.length; index++) {
			var data = currentDataSet.dataRows[index].data;
			var id;
			if (index == 0) {
				setTheDataRow(currentDataSet.dataRows[index]);
			}
			headerColumnCount = 0;
			datumHeaderColumnCount = 0;
			conditionHeaderColumnCount = 0;
			for ( var i = 0; i < data.length; i++) {
				addDatumColumn(data[i]);
			}
		}
		for ( var index = 0; index < currentDataSet.dataRows.length; index++) {
			var conditions = currentDataSet.dataRows[index].conditions;
			var id;
			for ( var i = 0; i < conditions.length; i++) {
				addDatumColumn(conditions[i]);
			}
		}
		for ( var index = 0; index < currentDataSet.dataRows.length; index++) {
			if (rowCount == 0) {
				addNewColumn = false;
				$("datumMatrixDivRow").style.display = "";
			}
			rowCount++;
		}
	}
}


function saveDataSet(actionName) {
	submitAction(document.forms[0], actionName, 'saveDataSet');
}

function addDatumColumn() {
	addDatumColumn(null);
}

function addDatumColumn(myDatum) {
	var columnBean = {
		name :null,
		valueType :null,
		valueUnit :null,
		value :null,
		property :null,
		datumOrCondition :null
	};	
	if (myDatum != null) {
		columnBean = myDatum;
	}else{
		dwr.util.getValues(columnBean);
		columnBean.id = document.getElementById("columnId").value;
		// do not know why datum.id == 'null' sometimes, it happens in IE only
		if (columnBean.id == null || columnBean.id == '' || columnBean.id == 'null') {
			if (columnBean.datumOrCondition == 'Condition'){
				columnBean.id = -fixConditionColIndex-conditionHeaderColumnCount - 1;
			}else{
				columnBean.id = -datumHeaderColumnCount - 1;
			}
		}
	}
	if (columnBean.name != '' || columnBean.valueType != '' || columnBean.valueUnit != '') {
		if (headerColumnCount == 0) {
			var datumColumnPatternRowDisplay = document
					.getElementById("datumColumnPatternRowDisplay");
			var aCellsDisplay = datumColumnPatternRowDisplay
					.getElementsByTagName('td')// cells collection in this row
			var aCellLengthDisplay = aCellsDisplay.length;
			// TODO:: delete may not need to columnDisplay
			var toDeleteDisplay = new Array();
			i = 0;
			for ( var j = 0; j < aCellLengthDisplay; j++) {
				if (aCellsDisplay[j].id != 'datumColumnPatternDisplay') {
					toDeleteDisplay[i] = aCellsDisplay[j].id;
					i++;
				}
			}
			for ( var j = 0; j < toDeleteDisplay.length; j++) {
				datumColumnPatternRowDisplay.removeChild(document
						.getElementById(toDeleteDisplay[j]));
			}
			$("datumColumnsDivRowDisplay").style.display = "";
		}

		addNewColumn = true;
		if (myDatum == null) {
			if (columnBean.id == null || columnBean.id == '' || columnBean.id == 'null') {
				if (columnBean.datumOrCondition == 'Condition'){
					columnBean.id = -fixConditionColIndex-conditionHeaderColumnCount - 1;
				}else{
					columnBean.id = -datumHeaderColumnCount - 1;
				}
			}
			DataSetManager.addColumnHeader(columnBean, function(theDataSet) {
				currentDataSet = theDataSet;
			});
		}
		$("addRowButtons").style.display = "";
		// createMatrixPattern();
		//if edit existing column, headerColumnCount++ not work, use incrementColumnCount
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
	headerColumnCount = currentDataSet.columnBeans.length;
	datumHeaderColumnCount = currentDataSet.datumColumnBeans.length;
	conditionHeaderColumnCount = currentDataSet.conditionColumnBeans.length;
//	alert('######## headerColumnCount='+headerColumnCount+
//			'\n datumHeaderColumnCount='+datumHeaderColumnCount+
//			'\n conditionHeaderColumnCount='+conditionHeaderColumnCount);
}

function createMatrixPattern() {
	var matrixHeader = document.getElementById("matrixHeader");
	matrixHeader = removeAllColumns(matrixHeader);
	var headerName, headerValueType, headerValueUnit;
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "matrixHeaderColumn" + i);
		span.setAttribute("class", "greyFont2");
		headerName = dwr.util.getValue("columnDisplayName" + (-i));
		headerValueType = dwr.util.getValue("datumColumnValueType" + (-i));
		headerValueUnit = dwr.util.getValue("datumColumnValueUnit" + (-i));
		span.appendChild(document.createTextNode(headerName));
		cell.appendChild(span);
		matrixHeader.appendChild(cell);
	}
	for ( var i = fixConditionColIndex+1; i < conditionHeaderColumnCount + fixConditionColIndex+1; i++) {
		var cell = document.createElement("TD");
		var span = document.createElement('SPAN');
		span.setAttribute("id", "matrixHeaderColumn" + i);
		span.setAttribute("class", "greyFont2");
		headerName = dwr.util.getValue("columnDisplayName" + (-i));
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
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		var cell = document.createElement("TD");
		// var span = document.createElement('SPAN');
		// span.setAttribute("id", "datumMatrixValue"+i);
		// span.setAttribute("class", "greyFont2");
		// span.appendChild(document.createTextNode('Value'));
		// cell.appendChild(span);
		// datumMatrixPatternRow.appendChild(cell);
		//TODO:: may need to change for IE
		var textValue = document.createElement('input');
		textValue.setAttribute("id", "datumMatrixValue" + i);
		textValue.setAttribute("type", "text");
		textValue.setAttribute("class", "noBorderText");
		textValue.setAttribute("value", "");
		textValue.setAttribute("size", "5");
		textValue.setAttribute("readonly", "readonly");
		// textValue.setAttribute("value", "placeHolder");
		// textValue.setAttribute("style", "border:0px solid;text-decoration:
		// underline;");
		cell.appendChild(textValue);
		datumMatrixPatternRow.appendChild(cell);
	}
	
	for ( var i = fixConditionColIndex+1; i < conditionHeaderColumnCount + fixConditionColIndex+1; i++) {
		var cell = document.createElement("TD");
		// var span = document.createElement('SPAN');
		// span.setAttribute("id", "datumMatrixValue"+i);
		// span.setAttribute("class", "greyFont2");
		// span.appendChild(document.createTextNode('Value'));
		// cell.appendChild(span);
		// datumMatrixPatternRow.appendChild(cell);
		//TODO:: may need to change for IE
		var textValue = document.createElement('input');
		textValue.setAttribute("id", "datumMatrixValue" + i);
		textValue.setAttribute("type", "text");
		textValue.setAttribute("class", "noBorderText");
		textValue.setAttribute("value", "");
		textValue.setAttribute("size", "5");
		textValue.setAttribute("readonly", "readonly");
		// textValue.setAttribute("value", "placeHolder");
		// textValue.setAttribute("style", "border:0px solid;text-decoration:
		// underline;");
		cell.appendChild(textValue);
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
		datumOrCondition  = dwr.util.getValue("datumOrConditionColumn" + id);
		if (datumOrCondition=='Condition'){
			datum.property = dwr.util.getValue("conditionColumnProperty" + id);
		}else{
			datum.property = '';
		}
		datum.id = datumid;
		if (datum.id==null || datum.id==''){
			datum.id = (-rowCount-1)*1000+(-i-1);
		}
		if (datumOrCondition=='Condition'){
			conditionArray[conditionIndex] = datum;
			conditionIndex++;
		}else{
			datumArray[datumIndex] = datum;
			datumIndex++;
		}
	}
	
	
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount+fixConditionColIndex; i++) {
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
		datumOrCondition  = dwr.util.getValue("datumOrConditionColumn" + id);
		if (datumOrCondition=='Condition'){
			datum.property = dwr.util.getValue("conditionColumnProperty" + id);
		}else{
			datum.property = '';
		}
		datum.id = datumid;
		if (datum.id==null || datum.id==''){
			datum.id = (-rowCount-1)*1000+(-i-1);
		}
		if (datumOrCondition=='Condition'){
			conditionArray[conditionIndex] = datum;
			conditionIndex++;
		}else{
			datumArray[datumIndex] = datum;
			datumIndex++;
		}
	}
	if (datumIndex>0){
		DataSetManager.addRow(datumArray, conditionArray, function(theDataSet) {
			currentDataSet = theDataSet;
		});
	}else{
		alert('Please fill in at least one Datum');
	}
	if (rowCount == 0) {
		$("datumMatrixDivRow").style.display = "";
	}
	rowCount++;
	window.setTimeout("fillMatrix()", 200);
}

function deleteRow() {
	var datumArray = new Array();
	var conditionArray = new Array();
	var datumIndex = 0, conditionIndex = 0;
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			datumOrCondition :null
		};
		dwr.util.getValues(datum);
		datum.id = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datum.datumOrCondition=='Condition'){
			conditionArray[conditionIndex] = datum;
		}else{
			datumArray[datumIndex] = datum;
		}
		conditionIndex++;
		datumIndex++;
	}
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount+fixConditionColIndex; i++) {
		var datum = {
			name :null,
			valueType :null,
			datumOrCondition :null
		};
		dwr.util.getValues(datum);
		datum.id = dwr.util.getValue("datumColumnId" + (-i - 1));
		if (datum.datumOrCondition=='Condition'){
			conditionArray[conditionIndex] = datum;
		}else{
			datumArray[datumIndex] = datum;
		}
		conditionIndex++;
		datumIndex++;
	}
	DataSetManager.deleteRow(datumArray, conditionArray, function(theDataSet) {
			currentDataSet = theDataSet;
	});	
	rowCount--;
	window.setTimeout("fillMatrix()", 200);	
}

function clearTheDataRow() {
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		document.getElementById("datumColumnId" + (-i - 1)).value = '';
		document.getElementById("datumColumnValue" + (-i - 1)).value = "";
	}
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount+fixConditionColIndex; i++) {
		document.getElementById("datumColumnId" + (-i - 1)).value = '';
		document.getElementById("datumColumnValue" + (-i - 1)).value = "";
	}
}

function clearTheDataColumn() {
	//IE works only set value = '' instead of null
	document.getElementById("columnId").value = '';
	document.getElementById("datumOrCondition").value = "Datum";
	document.getElementById("property").value = "";
	showDatumConditionInfo(null);
	tempDatum = null;
	window.setTimeout("setDatumValues()", 100);
}

function setTheDataRow(dataRow) {
	currentDataSet.theDataRow = dataRow;
	// var datum = currentDataSet.theDataRow.data[headerColumnCount-1];
	// document.getElementById("name").value = datum.name;
	// document.getElementById("value").value = datum.value;
	// document.getElementById("name").value = datum.name;
	// document.getElementById("name").value = datum.name;
}

function removeColumns(columnName, exceptColumn){
	//remove datumColumnPatternRowDisplay all column except datumColumnPatternDisplay
	var columnVariable = document
		.getElementById(columnName);
	var aCellsDisplay = columnVariable
		.getElementsByTagName('td')// cells collection in this row
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
	//$("datumColumnsDivRowDisplay").style.display = "";

}

function fillColumnTable() {	
	$("datumColumns").style.display = "";
	$("datumColumnPatternRowDisplay").style.display = "";
	$("datumColumnPattern").style.display = "none";
	$("datumColumnPatternDisplay").style.display = "none";
	var start = 0;
	var columnBean, id;
	var columnBeanid = null;
	var valueTypeUnit = null;	
	
	//REMOVE
	//remove datumColumnPatternRowDisplay all column except datumColumnPatternDisplay
	removeColumns('datumColumnPatternRowDisplay', 'datumColumnPatternDisplay')	
	//remove "datumColumns" all rows except datumColumnPattern
	dwr.util
			.removeAllRows(
					"datumColumns",
					{
						filter : function(tr) {
							return (tr.id != "datumColumnPattern" && tr.id != "datumColumnsDivRow2");
						}
	});
	fillColumns(editDataSet, start, currentDataSet.datumColumnBeans);
	if (currentDataSet.conditionColumnBeans!=null && currentDataSet.conditionColumnBeans.length>0){
		fillColumns(editDataSet, fixConditionColIndex, currentDataSet.conditionColumnBeans)
	}

}

function fillColumns(editDataSet, start, columnBeans){
	var columnBean, id;
	var columnBeanid = null;
	var valueTypeUnit = null;
	//TODO:: verify when editing, i< columnBeans.length or  i<columnBeans.length+start
	for ( var i = start; i < columnBeans.length+start; i++) {		
		columnBean = columnBeans[i-start];
		columnBeanid = columnBean.id;		
		id = -i - 1;
		if (columnBeanid == null) {
			columnBeanid = id;
		}
		dwr.util.cloneNode("datumColumnPattern", {
			idSuffix :id
		});
		dwr.util.cloneNode("datumColumnPatternDisplay", {
			idSuffix :id
		});
		dwr.util.setValue("datumColumnId" + id, columnBeanid);
		// TODO:::?? this one or the one above??
		// dwr.util.setValue("datumColumnId" + (-i-1), datumid);
		dwr.util.setValue("datumColumnName" + id, columnBean.name);
		dwr.util.setValue("datumColumnValueType" + id, columnBean.valueType);
		dwr.util.setValue("datumColumnValueUnit" + id, columnBean.valueUnit);
		dwr.util.setValue("datumColumnValue" + id, columnBean.value);
		dwr.util.setValue("datumOrConditionColumn" + id, columnBean.datumOrCondition);
		dwr.util.setValue("conditionColumnProperty" + id, columnBean.property);
		dwr.util.setValue("datumColumnNameDisplay" + id, columnBean.displayName);		
		dwr.util
			.setValue("columnDisplayName" + id, columnBean.displayName);
		if (!editDataSet) {
			// TODO:: how to handle both situation??
			// if change column name, do not need it
			// TODO::: why need??, add row and get value from the name/value
			// dwr.util.setValue("datumColumnName" + id,
			// document.getElementById("name").value);
			dwr.util.setValue("datumColumnValue" + id, document
					.getElementById("value").value);
			dwr.util.setValue("datumColumnId" + id, document
					.getElementById("columnId").value);
		}
		//dummmy get data, todo: check if needed,
		//actually getting data from colDatum.name = dwr.util.getValue("datumColumnName" + id); etc...
		var colDatum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(colDatum);
		colDatum.id = dwr.util.getValue("datumColumnId" + id);// ==datumid		
		if (colDatum.id=='' || colDatum.id==null){
			colDatum.id = id;
		}
		colDatum.name = dwr.util.getValue("datumColumnName" + id);
		colDatum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		colDatum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		colDatum.value = dwr.util.getValue("datumColumnValue" + id);		
		colDatum.datumOrCondition = dwr.util.getValue("datumOrConditionColumn" + id);
		colDatum.property = dwr.util.getValue("conditionColumnProperty" + id);
		colDatum.displayName = dwr.util.getValue("columnDisplayName" + id);
		dataColumnCache[id] = colDatum;
		$("datumColumnPattern" + id).style.display = "";
		$("datumColumnPatternDisplay" + id).style.display = "";
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
	var aCells = datumMatrixPatternRow.getElementsByTagName('td')// cells
	// collection
	// in this
	// row
	var aCellLength = aCells.length;
	for ( var row = 0; row < currentDataSet.dataRows.length; row++) {		
		//DATUM
		var data = currentDataSet.dataRows[row].data;
		rowId = currentDataSet.dataRows[row].domain.id;
		// TODO:: how to deal with rowId/datumID??
		if (rowId == null || rowId == '') {
			rowId = -row - 1;
		}
		dwr.util.cloneNode("datumMatrixPatternRow", {
			idSuffix :rowId
		});
		var colIndex = 0;
		for (var i = 0; i < data.length; i++) {
			datum = data[i];
			if (datum.id == null || datum.id == '') {
				datum.id = -i - 1;
			}
			id = datum.id;
			id = rowId;
			dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + rowId,
					datum.value);
			
		}
		//colIndex = data.length;
		colIndex = fixConditionColIndex;
		//CONDITION
		var conditions = currentDataSet.dataRows[row].conditions;
		for (var i=0; i < conditions.length; i++) {
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
		dataRowCache[rowId] = currentDataSet.dataRows[row];
		//TODO::: if editing, ok??
		dataRowCache[rowId].domain.id = rowId;
	}	
	clearTheDataRow();
	cloneEditRow();
}

function cloneEditRow() {
	$("datumMatrixDivRow").style.display = "";
	var rowIndex = rowCount;
	var rowId = fixId;
	dwr.util.cloneNode("datumMatrixPatternRow", {
		idSuffix :rowId
	});
	for ( var i = 0; i < datumHeaderColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datum.id = -i - 1;
		id = rowId;
		var editRow = document.getElementById("editDatumRow" + rowId);
		editRow.onclick = function() {
			saveRowClicked(this.id)
		};
		editRow.setAttribute("value", "Save");
		editRow.setAttribute("style", "border:0px solid;");
		if (currentDataSet.datumColumnBeans[i].id<0){
			dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + id,
				currentDataSet.datumColumnBeans[i].value);
		}

		var tempValue = document.getElementById("datumMatrixValue" + (i + 1)
				+ "" + id);
		tempValue.setAttribute("style", "border:1px solid;");
		tempValue.removeAttribute("readonly");

		if (document.getElementById("deleteDatumRow")==null){		
			var selectButtonCell = document.getElementById("selectButtonCell" + rowId);			
			var deleteButton = document.createElement("input");
			deleteButton.setAttribute("id", "deleteDatumRow");
			deleteButton.setAttribute("type", "button");
			deleteButton.setAttribute("class", "noBorderButton");
			deleteButton.setAttribute("value", "Delete");
			deleteButton
					.setAttribute("style",
							"border:0px solid;text-decoration: underline;");
			deleteButton.onclick = function() {
				deleteRowClicked(this.id)
			};
			selectButtonCell.appendChild(deleteButton);			
		}
	}
	for ( var i = fixConditionColIndex; i < conditionHeaderColumnCount+fixConditionColIndex; i++) {
			var datum = {
				name :null,
				valueType :null,
				valueUnit :null
			};
			dwr.util.getValues(datum);
			datum.id = -i - 1-fixConditionColIndex;
			id = rowId;
			//dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + id, datum.value);
			
			var editRow = document.getElementById("editDatumRow" + rowId);
			editRow.onclick = function() {
				saveRowClicked(this.id)
			};
			editRow.setAttribute("value", "Save");
			editRow.setAttribute("style", "border:0px solid;");
			//fixConditionColIndex
			if (currentDataSet.conditionColumnBeans[i-fixConditionColIndex].id<0){				
				dwr.util.setValue("datumMatrixValue" + (i + 1) + "" + id,
					currentDataSet.conditionColumnBeans[i-fixConditionColIndex].value);
			}
			
			var tempValue = document.getElementById("datumMatrixValue" + (i + 1)
					+ "" + id);
			tempValue.setAttribute("style", "border:1px solid;");
			tempValue.removeAttribute("readonly");

			if (document.getElementById("deleteDatumRow")==null){		
				var selectButtonCell = document.getElementById("selectButtonCell" + rowId);			
				var deleteButton = document.createElement("input");
				deleteButton.setAttribute("id", "deleteDatumRow");
				deleteButton.setAttribute("type", "button");
				deleteButton.setAttribute("class", "noBorderButton");
				deleteButton.setAttribute("value", "Delete");
				deleteButton
						.setAttribute("style",
								"border:0px solid;text-decoration: underline;");
				deleteButton.onclick = function() {
					deleteRowClicked(this.id)
				};
				selectButtonCell.appendChild(deleteButton);			
			}
		}		
		$("datumMatrixPatternRow" + rowId).style.display = "";
	
}

var editRowId = -1;
function editDatumClicked(eleid) {
	currentDataSet.theDataRow = dataRowCache[eleid.substring(12)];
	var rowid = eleid.substring(12);
	var data = dataRowCache[rowid].data;
	var datum, condition;
	for ( var i = 0; i < data.length; i++) {
		datum = data[i];
		//var datumid = datum.id;
		document.getElementById("datumColumnId" + (-i - 1)).value = datum.id;
		document.getElementById("datumMatrixValue" + (i + 1) + fixId).value = datum.value;
	}
	var conditions = dataRowCache[rowid].conditions;
	//var condIndex = data.length;
	var condIndex = fixConditionColIndex;
	for ( var i = 0; i < conditions.length; i++) {
		condition = conditions[i];
		//var conditionid = condition.id;
		document.getElementById("datumColumnId" + (-condIndex - 1)).value = condition.id;
		document.getElementById("datumMatrixValue" + (condIndex + 1) + fixId).value = condition.value;
		//alert("######## condition datumMatrixValue "+(condIndex + 1) + fixId + " ==>"+condition.value);
		condIndex++;
	}
}

function saveRowClicked(eleid) {
	for ( var i = 1; i < datumHeaderColumnCount + 1; i++) {
		document.getElementById("datumColumnValue" + (-i)).value = dwr.util
				.getValue("datumMatrixValue" + i + fixId);
	}
	for ( var i = fixConditionColIndex+1; i < conditionHeaderColumnCount + fixConditionColIndex+1; i++) {
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
		for ( var i = fixConditionColIndex+1; i < conditionHeaderColumnCount + fixConditionColIndex+1; i++) {
			document.getElementById("datumColumnValue" + (-i)).value = dwr.util
					.getValue("datumMatrixValue" + i + fixId);
		}
		deleteRow();
	}
}

function editColumn(eleid) {
	var id = eleid.substring(22);
	var datum = dataColumnCache[id];
	tempDatum = datum;
	//TODO:: after delete id mess up, delete error
	document.getElementById("columnId").value = datum.id;
	document.getElementById("datumOrCondition").value = datum.datumOrCondition;	
	document.getElementById("name").value = datum.name;
	showDatumConditionInfo(datum.name);
//	document.getElementById("name").value = datum.name;
//	if (datum.datumOrCondition=='Condition'){
//		document.getElementById("property").value = datum.property;
//	}	
//	document.getElementById("valueType").value = datum.valueType;
//	document.getElementById("valueUnit").value = datum.valueUnit;
//	document.getElementById("value").value = datum.value;
	window.setTimeout("setDatumValues()", 100);
	
}

function setDatumValues(){
	if (tempDatum!=null){
		document.getElementById("name").value = tempDatum.name;
		if (tempDatum.datumOrCondition=='Condition'){
			document.getElementById("property").value = tempDatum.property;
		}	
		document.getElementById("valueType").value = tempDatum.valueType;
		document.getElementById("valueUnit").value = tempDatum.valueUnit;
		document.getElementById("value").value = tempDatum.value;
		tempDatum = null;
	}else{
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
		DataSetManager.removeColumnHeader(columnBean, function(theDataSet) {
			currentDataSet = theDataSet;
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

