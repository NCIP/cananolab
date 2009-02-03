
var currentDataSet = null;
var rowCount = 0;
var dataRowCache = {};
var viewed = -1;


function resetTheDataSet(isShow) {
	if (isShow) {
		show('newDataSet');
	} else {
		hide('newDataSet');
	}
	DataSetManager.resetDataSet( function(theDataSet) {
		currentDataSet = theDataSet;
	});
//	dwr.util.setValue("techniqueType", "");
//	dwr.util.setValue("techniqueAbbr", "");
//	dwr.util.setValue("configDescription", "");
//	dwr.util.setValue("configId", "0");
//	dwr.util
//			.removeAllRows(
//					"instrumentRows",
//					{
//						filter : function(tr) {
//							return (tr.id != "pattern"
//									&& tr.id != "patternHeader" && tr.id != "patternAddRow");
//						}
//					});
//	clearInstrument();
}

function validateSaveConfig(actionName){
	var techniqueType = document.getElementById('techniqueType');
	if (techniqueType.value==''){
		alert('Please select a technique');
		return false;
	}
	var patternAddRow = document.getElementById('patternAddRow');
	//if(patternAddRow.style.display == 'block'){
		//addInstrument();
	//}
	submitAction(document.forms[0],
			actionName, 'saveExperimentConfig');
}

function addDatumColumn() {	
	var datumOrCondition = document.getElementById("datumOrCondition").value;
	var datum = {
		name :null,
		valueType :null,
		valueUnit :null
	};
	
	dwr.util.getValues(datum);
	if (datum.name!='' || 
			datum.valueType!='' ||
			datum.valueUnit!=''){
		DataSetManager.addColumnHeader(datum,
				function(theDataSet) {
					currentDataSet = theDataSet;
				});
		$("addRowButtons").style.display = "";
		window.setTimeout("fillColumnTable()", 200);
	}else{
		alert('Please fill in values');
	}
}

function createMatrixPattern(){
	var matrixHeader = document.getElementById("matrixHeader");
	for (var i=1; i<columnCount+1; i++){
		 var cell = document.createElement("TD");
		 var span = document.createElement('SPAN');
		 span.setAttribute("id", "matrixHeaderColumn"+i);
		 span.setAttribute("class", "greyFont2");
		 span.appendChild(document.createTextNode('Header'));
		 cell.appendChild(span);	 		 
		 matrixHeader.appendChild(cell);
	}
	$("matrixHeader").style.display = "";
	var datumMatrixPatternRow = document.getElementById("datumMatrixPatternRow");			    
	for (var i=1; i<columnCount+1; i++){
		 var cell = document.createElement("TD");
		 var span = document.createElement('SPAN');
		 span.setAttribute("id", "datumMatrixValue"+i);
		 span.setAttribute("class", "greyFont2");
		 span.appendChild(document.createTextNode('Value'));
		 cell.appendChild(span);		 		 
		 datumMatrixPatternRow.appendChild(cell);
	}
	var buttonCell = document.createElement("TD");
	var button = document.createElement('input');
	button.setAttribute("id", "edit");
	button.setAttribute("type", "button");
	button.setAttribute("class", "noBorderButton");
	button.setAttribute("value", "Select");
	button.setAttribute("onclick", "editClicked(this.id)");
	buttonCell.appendChild(button);		 		 
	datumMatrixPatternRow.appendChild(buttonCell);	
}


function addRow() {
	var id = -1;	
	
	var datumArray = new Array();	
	for ( var i = 0; i < columnCount; i++) {
		id = i+1;
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		datum.name = dwr.util.getValue("datumColumnName" + id);
		datum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		datum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		datum.value = dwr.util.getValue("datumColumnValue" + id);
		datumArray[i] = datum;
		if (datum.name!='' || 
				datum.valueType!='' ||
				datum.valueUnit!=''){			
		}else{
			alert('Please fill in values');
			return;
		}
		
	}
	DataSetManager.addRow(datumArray,
			function(theDataSet) {
				currentDataSet = theDataSet;
			});			
	if (rowCount==0){
		createMatrixPattern();
	}
	rowCount++;
	window.setTimeout("fillMatrix()", 200);
}

function clearTheDataRow() {
	
}

var columnCount = 0;

function fillColumnTable() {
	var data = currentDataSet.theDataRow.data;

	var datum, id;	
	columnCount = 0;
	for ( var i = 0; i < data.length; i++) {
		columnCount++;
		datum = data[i];
		datum.id = i+1;
		id = datum.id;
		dwr.util.cloneNode("datumColumnPattern", {
			idSuffix :id
		});
		dwr.util.setValue("datumColumnName" + id, datum.name);
		dwr.util.setValue("datumColumnValueType" + id, datum.valueType);
		dwr.util.setValue("datumColumnValueUnit" + id, datum.valueUnit);
		if (i==data.length-1){
			dwr.util.setValue("datumColumnValue" + id, document.getElementById("value").value);
		}
		$("datumColumnPattern" + id).style.display = "";
	}
}


function fillMatrix() {
	alert("fillMatrix");
	dwr.util
			.removeAllRows(
					"datumMatrix",
					{
						filter : function(tr) {
							return (tr.id != "datumMatrixPatternRow" &&
									tr.id != "matrixHeader");
						}
					});
	var datum, id;	
	var rowId;
	for ( var row = 0; row < currentDataSet.dataRows.length; row++) {		
		var data = currentDataSet.dataRows[row].data;
		rowId = currentDataSet.dataRows[row].domain.id;
		rowId = row + 1;
		dwr.util.cloneNode("datumMatrixPatternRow", {
			idSuffix :rowId
		});					
		for ( var i = 0; i < data.length; i++) {	
			datum = data[i];
			datum.id = i+1;
			id = datum.id;
			id = rowId;
			if (row == 0){
				dwr.util.setValue("matrixHeaderColumn"+(i+1), datum.name+" "+datum.valueType
						+" "+datum.valueUnit);
			}
			dwr.util.setValue("datumMatrixValue"+(i+1)+"" + id, datum.value);
		}
		$("datumMatrixPatternRow" + rowId).style.display = "";
		dataRowCache[rowId] = currentDataSet.dataRows[row];
	}
	clearTheDataRow();
}

var editRowId = -1;
function editClicked(eleid) {
	editRowId = eleid.substring(4);
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var data = dataRowCache[eleid.substring(4)].data;
	for ( var i = 0; i < data.length; i++) {	
		datum = data[i];
		document.getElementById("datumColumnValue"+(i+1)).value=datum.value;		
	}
}

function deleteClicked() {	
	// we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	//var instrument = instrumentCache[eleid.substring(6)];
	if (editRowId!='' && editRowId!='-1'){
		if (confirm("Are you sure you want to delete this row ?")) {
			DataSetManager.deleteDataRow(dataRowCache[editRowId],
					function(theDataSet) {
						currentDataSet = theDataSet;
					});
			window.setTimeout("fillMatrix()", 200);
		}
	}
}

function addClicked() {
	document.getElementById("manufacturer").focus();	
}

