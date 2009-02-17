
var currentDataSet = null;
var rowCount = 0;
var dataRowCache = {};
var dataColumnCache = {};
var viewed = -1;
var datumColumnCount = 0;
var addNewColumn = true;
var editDataSet = false;

function resetTheDataSet(isShow) {
	//alert('reset 1');
	editDataSet = false;
	datumColumnCount = 0;
	columnCount = 0;
	rowCount = 0;
	if (isShow) {
		show('newDataSet');
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
	$("datumColumnPatternDisplay").style.display = "none";
	$("datumColumns").style.display = "none";
	$("datumColumnPatternRowDisplay").style.display = "none";
	$("addRowButtons").style.display = "none";
	$("datumColumnsDivRow").style.display = "none";		
	$("datumMatrixDivRow").style.display = "none";	
	
	$("datumColumnsDivRowDisplay").style.display = "none";	
	
	clearTheDataRow();
}

function setTheDataSet(dataSetId) {
	resetTheDataSet(true);
	editDataSet = true;
	show('submitDatum');
	show('populateDataTable');
	DataSetManager.findDataSetById(dataSetId,
			populateDataSet);	
}

function populateDataSet(dataSet) {
	alert('populateDataSet 99  ');
	if (dataSet != null && dataSet.dataRows!=null && dataSet.dataRows.length>0) {
		currentDataSet = dataSet;
		for (var index=0; index<currentDataSet.dataRows.length; index++) {
			var data = currentDataSet.dataRows[index].data;
			var id;	
			//setTheDataRow(currentDataSet.dataRows[index]);
			datumColumnCount = 0;
			for (var i = 0; i < data.length; i++) {
				addDatumColumn(data[i]);
			}		
		}		
		for (var index=0; index<currentDataSet.dataRows.length; index++) {
			var data = currentDataSet.dataRows[index].data;
			var datum, id;	
			setTheDataRow(currentDataSet.dataRows[index]);
			addRow2(currentDataSet.theDataRow);
		}		
		window.setTimeout("fillMatrix()", 200);
	}
}


function saveDataSet(actionName){
	submitAction(document.forms[0],
			actionName, 'saveDataSet');
}

function addDatumColumn(){
	addDatumColumn(null);
}

function addDatumColumn(myDatum) {
	var datumOrCondition = document.getElementById("datumOrCondition").value;
	var datum = {
		name :null,
		valueType :null,
		valueUnit :null
	};	
	dwr.util.getValues(datum);
	datum.id = document.getElementById("columnId").value;
	if (datum.id==null){
		datum.id == - datumColumnCount - 1; 
	}
	if (myDatum!=null){
		datum = myDatum;
	}
	if (datum.name!='' || 
			datum.valueType!='' ||
			datum.valueUnit!=''){	
		if (datumColumnCount==0){		
			//TODO::
//			var datumColumnPatternRow = document.getElementById("datumColumnPatternRow");
//			var aCells = datumColumnPatternRow.getElementsByTagName('td')//cells collection in this row
//			var aCellLength = aCells.length;
//			var toDelete = new Array();	
//			i =0;
//			for(var j=0;j<aCellLength;j++){
//				if (aCells[j].id!='datumColumnPattern'){
//					toDelete[i]=aCells[j].id;
//					i++;
//				}
//			}
//			for(var j=0;j<toDelete.length;j++){
//				datumColumnPatternRow.removeChild(document.getElementById(toDelete[j]));
//			}
			$("datumColumnsDivRow").style.display = "";
			
			
			var datumColumnPatternRowDisplay = document.getElementById("datumColumnPatternRowDisplay");
			var aCellsDisplay = datumColumnPatternRowDisplay.getElementsByTagName('td')//cells collection in this row
			var aCellLengthDisplay = aCellsDisplay.length;
			//TODO:: delete may not need to columnDisplay
			var toDeleteDisplay = new Array();	
			i =0;
			for(var j=0;j<aCellLengthDisplay;j++){
				if (aCellsDisplay[j].id!='datumColumnPatternDisplay'){
					toDeleteDisplay[i]=aCellsDisplay[j].id;
					i++;
				}
			}
			for(var j=0;j<toDeleteDisplay.length;j++){
				datumColumnPatternRowDisplay.removeChild(document.getElementById(toDeleteDisplay[j]));
			}
			$("datumColumnsDivRowDisplay").style.display = "";
		}
		
		addNewColumn = true;
		if (myDatum==null){
			DataSetManager.addColumnHeader(datum,
					function(theDataSet) {
						currentDataSet = theDataSet;
					});
		}
		$("addRowButtons").style.display = "";
		
		//datumColumnCount = currentDataSet.theDataRow.data.length;
		//alert('currentDataSet.theDataRow.data.length '+currentDataSet.theDataRow.data.length);
		//TODO:: if edit existing column, datumColumnCount++ not work
		datumColumnCount++;		
		createMatrixPattern();
		window.setTimeout("fillColumnTable()", 200);
		clearTheDataColumn();
		//if (rowCount>0 && !editDataSet){
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
//		 var span = document.createElement('SPAN');
//		 span.setAttribute("id", "datumMatrixValue"+i);
//		 span.setAttribute("class", "greyFont2");
//		 span.appendChild(document.createTextNode('Value'));
//		 cell.appendChild(span);		 		 
//		 datumMatrixPatternRow.appendChild(cell);
		 
		 
		 var textValue = document.createElement('input');
		 textValue.setAttribute("id", "datumMatrixValue"+i);
		 textValue.setAttribute("type", "text");
		 textValue.setAttribute("class", "noBorderText");
		 textValue.setAttribute("value", "placeHolder");
		 //textValue.setAttribute("style", "border:0px solid;text-decoration: underline;");
		 cell.appendChild(textValue);		 		 
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
	var datumid = null;
	for ( var i = 0; i < datumColumnCount; i++) {
		var datum = {
			name :null,
			valueType :null,
			valueUnit :null
		};
		dwr.util.getValues(datum);
		//datum.id = dwr.util.getValue("datumColumnId" + id);		
		datumid = dwr.util.getValue("datumColumnId" + (-i-1));	
		if (datumid ==null || datumid == 'datumColumnId'){
			datumid = null;
		}
		//TODO::
		id = datum.id;
		if (id==null){
			//id = (-rowCount-1)+""+(-i-1);
			id = (-i-1);
		}
		datum.name = dwr.util.getValue("datumColumnName" + id);
		datum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		datum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		datum.value = dwr.util.getValue("datumColumnValue" + id);
		datum.id = datumid;
		
//		if (currentDataSet.domain.id!=null){
//			datum.dataSet = currentDataSet;
//			datum.dataSet.id = dwr.util.getValue("datumColumnDataSetId" + id);
//			
//		}
//		if (currentDataSet.theDataRow.domain.id!=null){
//			datum.dataRow = currentDataSet.theDataRow;
//			datum.dataRow.id = dwr.util.getValue("datumColumnDataRowId" + id);
//		}		
		if (datum.id==null || datum.id<0){
			//if datum.id<0, it is not compatible with DataSetManager.addRow
			datum.id = null;
		}
		//alert('xxxxxxxxxx addRow::: datum.dataSet.id='+datum.dataSet.id);
		//alert('xxxxxxxxxx addRow::: datum.dataRow.id='+datum.dataRow.id);
		//alert('xxxxxxxxxx addRow::: datum.id='+datum.id);
		datumArray[i] = datum;
	}
	
	//alert('bef DataSetManager.addRow =');
	//TODO:::xxxxxxxxxxxxxxxx
	DataSetManager.addRow(datumArray,
			function(theDataSet) {
				currentDataSet = theDataSet;
			});		
	//alert('after DataSetManager.addRow =');
	if (rowCount==0){
		$("datumMatrixDivRow").style.display = "";	
	}
	rowCount++;
	window.setTimeout("fillMatrix()", 200);
}



function clearTheDataRow() {	
	for (var i= 0; i<datumColumnCount; i++){
		document.getElementById("datumColumnId"+(-i-1)).value = null;
		document.getElementById("datumColumnValue"+(-i-1)).value = "";
	}
}

function clearTheDataColumn() {
	document.getElementById("columnId").value = null;
	document.getElementById("datumOrCondition").value = "";
	document.getElementById("name").value = "";
	document.getElementById("valueUnit").value = "";
	document.getElementById("valueType").value = "";
	document.getElementById("value").value = "";
}

function setTheDataRow(dataRow) {
	currentDataSet.theDataRow = dataRow;
	//var datum = currentDataSet.theDataRow.data[datumColumnCount-1];
	//document.getElementById("name").value = datum.name;
	//document.getElementById("value").value = datum.value;
	//document.getElementById("name").value = datum.name;
	//document.getElementById("name").value = datum.name;	
}

function fillColumnTable() {
	var data = currentDataSet.theDataRow.data;
	var datum, id;	
	$("datumColumns").style.display = "";
	$("datumColumnPatternRowDisplay").style.display = "";
	$("datumColumnPattern").style.display = "none";
	$("datumColumnPatternDisplay").style.display = "none";
	var start = data.length-1;
	if (editDataSet){
		start = 0;
	}
	var datumid = null;
	for ( var i = start; i < data.length; i++) {
		datum = data[i];
		//TODO::::
		datumid = datum.id;
//		if (datum.id==null){
//			datum.id = -i-1;
//		}
		//the column input always use -1, -2, not actual id
		datum.id = -i-1;
		id = datum.id;
		dwr.util.cloneNode("datumColumnPattern", {
			idSuffix :id
		});
		dwr.util.cloneNode("datumColumnPatternDisplay", {
			idSuffix :id
		});
		dwr.util.setValue("datumColumnId" + id, datumid);
		//TODO:::?? this one or the one above??
		//dwr.util.setValue("datumColumnId" + (-i-1), datumid);
		
//		if (currentDataSet.domain.id!=null){
//			datum.dataSet = currentDataSet;
//			dwr.util.setValue("datumColumnDataSetId" + id, datum.dataSet.id);
//		}
//		if (currentDataSet.theDataRow.domain.id!=null){
//			datum.dataRow = currentDataSet.theDataRow;
//			dwr.util.setValue("datumColumnDataRowId" + id, datum.dataRow.id);
//		}
		
		dwr.util.setValue("datumColumnName" + id, datum.name);
		dwr.util.setValue("datumColumnValueType" + id, datum.valueType);
		dwr.util.setValue("datumColumnValueUnit" + id, datum.valueUnit);
		dwr.util.setValue("datumColumnValue" + id, datum.value);
		
		dwr.util.setValue("datumColumnNameDisplay" + id, datum.name);
		dwr.util.setValue("datumColumnValueTypeDisplay" + id, datum.valueType);
		dwr.util.setValue("datumColumnValueUnitDisplay" + id, datum.valueUnit);
		
		if (!editDataSet){
			//TODO:: how to handle both situation??
			//if change column name, do not need it
			//TODO::: why need??, add row and get value from the name/value
			//dwr.util.setValue("datumColumnName" + id, document.getElementById("name").value);
			dwr.util.setValue("datumColumnValue" + id, document.getElementById("value").value);
			dwr.util.setValue("datumColumnId" + id, document.getElementById("columnId").value);
		}		
		var colDatum = {
				name :null,
				valueType :null,
				valueUnit :null
			};		
		dwr.util.getValues(colDatum);
		colDatum.id = dwr.util.getValue("datumColumnId" + id);//==datumid
		
		
//		if (currentDataSet.domain.id!=null){
//			colDatum.dataSet = currentDataSet;
//			colDatum.dataSet.id = dwr.util.getValue("datumColumnDataSetId" + id);
//		}
//		if (currentDataSet.theDataRow.domain.id!=null){
//			colDatum.dataRow = currentDataSet.theDataRow;
//			colDatum.dataRow.id = dwr.util.getValue("datumColumnDataRowId" + id);
//		}		
		
		colDatum.name = dwr.util.getValue("datumColumnName" + id);
		colDatum.valueType = dwr.util.getValue("datumColumnValueType" + id);
		colDatum.valueUnit = dwr.util.getValue("datumColumnValueUnit" + id);
		colDatum.value = dwr.util.getValue("datumColumnValue" + id);
		dataColumnCache[datum.id] = colDatum;
		//TODO:: XXXXXX
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
	var datum, id;	
	var rowId;	
	var datumMatrixPatternRow = document.getElementById("datumMatrixPatternRow");
	var aCells = datumMatrixPatternRow.getElementsByTagName('td')//cells collection in this row
	var aCellLength = aCells.length;	
	for (var row = 0; row < currentDataSet.dataRows.length; row++) {		
		var data = currentDataSet.dataRows[row].data;
		rowId = currentDataSet.dataRows[row].domain.id;
		//TODO:: how to deal with rowId/datumID??	
		if (rowId==null || rowId==''){
			rowId = -row - 1;
		}
		//TODO::: 
//		if (addNewColumn && 
//				document.getElementById("datumMatrixPatternRow"+rowId)!=null){
//			removeNode("datumMatrixPatternRow"+rowId);
//		}		
		dwr.util.cloneNode("datumMatrixPatternRow", {
			idSuffix :rowId
		});				
		for ( var i = 0; i < data.length; i++) {	
			datum = data[i];
			//TODO:: how to deal with rowId/datumID??	
			if (datum.id==null || datum.id==''){
				datum.id = -i-1;
			}
			//datum.id = -i-1;
			//TODO:: how to deal with rowId/datumID??			
			id = datum.id;
			id = rowId;
			if (row == 0){
				dwr.util.setValue("matrixHeaderColumn"+(i+1), datum.name+" "+datum.valueType
						+" "+datum.valueUnit);
			}
			//TODO::: 
			dwr.util.setValue("datumMatrixValue"+(i+1)+"" + id, datum.value);
		}
		//select button
		var editRow = document.getElementById("editDatumRow"+rowId);
		editRow.onclick = function(){editDatumClicked(this.id)};		
		editRow.setAttribute("style", "border:0px solid;");
		//editRow.style = "border:0px solid;text-decoration: underline;";
		$("datumMatrixPatternRow" + rowId).style.display = "";
		dataRowCache[rowId] = currentDataSet.dataRows[row];
		dataRowCache[rowId].domain.id = rowId;
		//alert('dataRowCache['+rowId+'].length= '+dataRowCache[rowId].data.length);
		//alert('fillMatrix::: row: '+rowId+'  edit datum ==> data[0].id='+dataRowCache[rowId].data[0].id );
	}
	clearTheDataRow();
}

var editRowId = -1;
function editDatumClicked(eleid) {
	// we were an id of the form "editDatumRow{id}", eg "editDatumRow42". We lookup the "42"
	currentDataSet.theDataRow = dataRowCache[eleid.substring(12)];
	var data = dataRowCache[eleid.substring(12)].data;
	
	var rowid = eleid.substring(12);
	for ( var i = 0; i < data.length; i++) {	
		datum = data[i];
		//alert('edit datum '+datum.id +"xx"+datumColumnValue+(-i-1)+"xx");
		var datumid = datum.id;
		//alert('row: '+eleid.substring(12)+'  edit datum '+i+" "+data[i].id );
		//TODO:::
//		if (datum.id==null){
//			document.getElementById("datumColumnValue"+(-i-1)).value=datum.value;	
//			document.getElementById("datumColumnId"+(-i-1)).id=datum.id;
//			
////			document.getElementById("datumColumnValue"+(-i-1)).value=datum.value;	
////			document.getElementById("datumColumnId"+(-i-1)).id=datum.id;
//		}else{
//			document.getElementById("datumColumnValue"+(datumid)).value=datum.value;	
//			document.getElementById("datumColumnId"+(datumid)).id=datum.id;
//		}
		
//		if (datum.id!=null && datum.id>0){
//			document.getElementById("datumColumnValue"+(datum.id)).value=datum.value;	
//			document.getElementById("datumColumnId"+(datum.id)).value=datum.id;
//		}
		document.getElementById("datumColumnValue"+(-i-1)).value=datum.value;	
		document.getElementById("datumColumnId"+(-i-1)).value=datum.id;		
		
//		if (currentDataSet.domain.id!=null){
//			datum.dataSet = currentDataSet;
//			document.getElementById("datumColumnDataSetId"+(-i-1)).value=datum.dataSet.id;
//		}
//		if (currentDataSet.theDataRow.domain.id!=null){
//			datum.dataRow = currentDataSet.theDataRow;
//			document.getElementById("datumColumnDataRowId"+(-i-1)).value=datum.dataRow.id;
//		}
	}
}

function editColumn(eleid) {
	var id = eleid.substring(22);
	var datum = dataColumnCache[id];
	alert("id="+id+" datum.id="+datum.id);
	document.getElementById("columnId").value=datum.id;
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

function addRow2(theDataRow) {
	addNewColumn = false;	
	if (rowCount==0){
		$("datumMatrixDivRow").style.display = "";	
	}
	rowCount++;
	//window.setTimeout("fillMatrix()", 200);
}

