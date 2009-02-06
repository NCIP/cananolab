
var currentDataSet = null;
var rowCount = 0;
var dataRowCache = {};
var dataColumnCache = {};
var viewed = -1;
var datumColumnCount = 0;
var addNewColumn = true;

function resetTheDataSet(isShow) {
	//alert('reset 333');
	datumColumnCount = 0;
	columnCount = 0;
	rowCount = 0;
	if (isShow) {
		show('newDataSet');
	} else {
		hide('newDataSet');
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
					return (tr.id != "datumMatrixPatternRow" &&
							tr.id != "matrixHeader");
				}
			});	
	
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
	$("datumMatrixPatternRow").style.display = "none";
	$("matrixHeader").style.display = "none";
	$("datumColumnPattern").style.display = "none";
	$("datumColumnPatternRow").style.display = "none";
	$("addRowButtons").style.display = "none";
	$("datumColumnsDivRow").style.display = "none";	
	$("datumMatrixDivRow").style.display = "none";	
	clearTheDataRow();
}

function saveDataSet(actionName){
	submitAction(document.forms[0],
			actionName, 'saveDataSet');
}
function addDatumColumn() {
	if (datumColumnCount==0){		
		var datumColumnPatternRow = document.getElementById("datumColumnPatternRow");
		var aCells = datumColumnPatternRow.getElementsByTagName('td')//cells collection in this row
		var aCellLength = aCells.length;
		var toDelete = new Array();	
		i =0;
		for(var j=0;j<aCellLength;j++){
			if (aCells[j].id!='datumColumnPattern'){
				toDelete[i]=aCells[j].id;
				i++;
			}
		}
		for(var j=0;j<toDelete.length;j++){
			datumColumnPatternRow.removeChild(document.getElementById(toDelete[j]));
		}
		$("datumColumnsDivRow").style.display = "";
	}
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
		addNewColumn = true;
		DataSetManager.addColumnHeader(datum,
				function(theDataSet) {
					currentDataSet = theDataSet;
				});
		$("addRowButtons").style.display = "";
		datumColumnCount++;
		createMatrixPattern();
		window.setTimeout("fillColumnTable()", 200);
		if (rowCount>0){
			window.setTimeout("fillMatrix()", 200);
		}
	}else{
		alert('Please fill in values');
	}
}

function createMatrixPattern(){
	var matrixHeader = document.getElementById("matrixHeader");
	matrixHeader = removeAllColumns(matrixHeader);	
	for (var i=1; i<datumColumnCount+1; i++){
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
	datumMatrixPatternRow = removeAllColumns(datumMatrixPatternRow);
	for (var i=1; i<datumColumnCount+1; i++){
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
	button.setAttribute("id", "editDatumRow");
	button.setAttribute("type", "button");
	button.setAttribute("class", "noBorderButton");
	button.setAttribute("value", "Select");
	button.setAttribute("style", "border:0px solid;text-decoration: underline;");
	buttonCell.appendChild(button);		 		 
	datumMatrixPatternRow.appendChild(buttonCell);	
}

function addRow() {
	addNewColumn = false;
	var id = -1;	
	var datumArray = new Array();
	//alert('datumColumnCount='+datumColumnCount);
	for ( var i = 0; i < datumColumnCount; i++) {
		//id = i+1;
		id = -i-1;
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
		//createMatrixPattern();
		$("datumMatrixDivRow").style.display = "";	
	}
	rowCount++;
	window.setTimeout("fillMatrix()", 200);
}

function clearTheDataRow() {
	
}

function fillColumnTable() {
	var data = currentDataSet.theDataRow.data;
	var datum, id;	
	$("datumColumnPatternRow").style.display = "";
	$("datumColumnPattern").style.display = "none";
	//alert('data.length='+data.length+'  datumColumnCount='+datumColumnCount);
	for ( var i = data.length - 1; i < data.length; i++) {
		datum = data[i];
		datum.id = -i-1;
		id = datum.id;
		dwr.util.cloneNode("datumColumnPattern", {
			idSuffix :id
		});
		dwr.util.setValue("datumColumnName" + id, datum.name);
		dwr.util.setValue("datumColumnValueType" + id, datum.valueType);
		dwr.util.setValue("datumColumnValueUnit" + id, datum.valueUnit);
		if (i==data.length-1){
			dwr.util.setValue("datumColumnName" + id, document.getElementById("name").value);
			dwr.util.setValue("datumColumnValue" + id, document.getElementById("value").value);
		}		
		var colDatum = {
				name :null,
				valueType :null,
				valueUnit :null
			};		
		dwr.util.getValues(colDatum);
		colDatum.name = dwr.util.getValue("datumColumnName" + id);
		colDatum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		colDatum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		colDatum.value = dwr.util.getValue("datumColumnValue" + id);
		dataColumnCache[datum.id] = colDatum;
		//TODO:: XXXXXX
		//alert('dataColumnCache'+datum.id+": "+dataColumnCache[datum.id].name+"= "+
			//	dataColumnCache[datum.id].value);
		$("datumColumnPattern" + id).style.display = "";
	}
}


function fillMatrix() {
	//alert("fillMatrix");
	dwr.util
			.removeAllRows(
					"datusMatrix",
					{
						filter : function(tr) {
							return (tr.id != "datumMatrixPatternRow" &&
									tr.id != "matrixHeader");
						}
					});
	var datum, id;	
	var rowId;	
	var datumMatrixPatternRow = document.getElementById("datumMatrixPatternRow");
	//checkNumOfColumns("datumMatrixPatternRow");
	var aCells = datumMatrixPatternRow.getElementsByTagName('td')//cells collection in this row
	var aCellLength = aCells.length;
	for ( var row = 0; row < currentDataSet.dataRows.length; row++) {		
		var data = currentDataSet.dataRows[row].data;
		rowId = currentDataSet.dataRows[row].domain.id;
		if (rowId==null || rowId==''){
			rowId = -row - 1;
		}
		if (addNewColumn && 
				document.getElementById("datumMatrixPatternRow"+rowId)!=null){
			removeNode("datumMatrixPatternRow"+rowId);
		}
		dwr.util.cloneNode("datumMatrixPatternRow", {
			idSuffix :rowId
		});				
		for ( var i = 0; i < data.length; i++) {	
			datum = data[i];
			if (datum.id==null || datum.id==''){
				datum.id = -i-1;
			}
			//TODO:: how to deal with rowId/datumID??			
			id = datum.id;
			id = rowId;
			if (row == 0){
				dwr.util.setValue("matrixHeaderColumn"+(i+1), datum.name+" "+datum.valueType
						+" "+datum.valueUnit);
			}
			dwr.util.setValue("datumMatrixValue"+(i+1)+"" + id, datum.value);
		}
		//select button
		var editRow = document.getElementById("editDatumRow"+rowId);
		editRow.onclick = function(){editClicked(this.id)};		
		editRow.setAttribute("style", "border:0px solid;");
		//editRow.style = "border:0px solid;text-decoration: underline;";
		$("datumMatrixPatternRow" + rowId).style.display = "";
		dataRowCache[rowId] = currentDataSet.dataRows[row];
	}
	clearTheDataRow();
}

var editRowId = -1;
function editClicked(eleid) {
	// we were an id of the form "editDatumRow{id}", eg "editDatumRow42". We lookup the "42"
	var data = dataRowCache[eleid.substring(12)].data;
	for ( var i = 0; i < data.length; i++) {	
		datum = data[i];
		document.getElementById("datumColumnValue"+(-i-1)).value=datum.value;		
	}
}

function editColumn(eleid) {
	var id = eleid.substring(15);
	var datum = dataColumnCache[id];
	document.getElementById("name").value=datum.name;	
	document.getElementById("valueType").value=datum.valueType;	
	document.getElementById("valueUnit").value=datum.valueUnit;	
	document.getElementById("value").value=datum.value;	
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

function deleteDatumColumn() {	
	alert('Under Constrution');
//	if (datumColumnCount==0){		
//		var datumColumnPatternRow = document.getElementById("datumColumnPatternRow");
//		var aCells = datumColumnPatternRow.getElementsByTagName('td')//cells collection in this row
//		var aCellLength = aCells.length;
//		var toDelete = new Array();	
//		i =0;
//		for(var j=0;j<aCellLength;j++){
//			if (aCells[j].id!='datumColumnPattern'){
//				toDelete[i]=aCells[j].id;
//				i++;
//			}
//		}
//		for(var j=0;j<toDelete.length;j++){
//			datumColumnPatternRow.removeChild(document.getElementById(toDelete[j]));
//		}
//		$("datumColumnsDivRow").style.display = "";
//	}
//	var datumOrCondition = document.getElementById("datumOrCondition").value;
//	var datum = {
//		name :null,
//		valueType :null,
//		valueUnit :null
//	};
//	
//	dwr.util.getValues(datum);
//	if (datum.name!='' || 
//			datum.valueType!='' ||
//			datum.valueUnit!=''){		
//		addNewColumn = true;
//		DataSetManager.addColumnHeader(datum,
//				function(theDataSet) {
//					currentDataSet = theDataSet;
//				});
//		$("addRowButtons").style.display = "";
//		datumColumnCount++;
//		createMatrixPattern();
//		window.setTimeout("fillColumnTable()", 200);
//		if (rowCount>0){
//			window.setTimeout("fillMatrix()", 200);
//		}
//	}else{
//		alert('Please fill in values');
//	}
	
}

function addClicked() {
	document.getElementById("manufacturer").focus();	
}

function removeAllColumns(theRow) {
	var aCells = theRow.getElementsByTagName('TD')//cells collection in this row
	var aCellLength = aCells.length;
	var toDelete = new Array();	
	var i = 0;
	//aCells[j] is dynamically updated if removeChild
	//so put it to toDelete, then removeChild
	for(var j=0;j<aCellLength;j++){
		toDelete[j]=aCells[j];		
	}
	for(var j=0;j<toDelete.length;j++){
		theRow.removeChild(toDelete[j]);
	}	
	return theRow;
}
//for dbug
function checkNumOfColumns(rowId) {
	var theRow = document.getElementById(rowId);
	var aCells = theRow.getElementsByTagName('TD')//cells collection in this row
	var aCellLength = aCells.length;
	alert(rowId +" has "+aCellLength+" columns");
}

function removeNode(elementId) {
	dwr.util._temp = dwr.util.byId(elementId);
	if (dwr.util._temp) {
		dwr.util._temp.parentNode.removeChild(dwr.util._temp);
		dwr.util._temp = null;
	}
}


