
var currentDataSet = null;

var instrumentCache = {};
var viewed = -1;
function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != "other") {
		ExperimentConfigManager.findInstrumentTypesByTechniqueType(
				techniqueType, updateInstrumentDropDown);
		ExperimentConfigManager.findTechniqueByType(techniqueType,
				updateTechniqueAbbreviation);
	}
}
function updateTechniqueAbbreviation(technique) {
	if (technique != null) {
		dwr.util.setValue("techniqueAbbr", technique.abbreviation);
	} else {
		document.getElementById("techniqueAbbr").value = "";
	}
}

function updateInstrumentDropDown(instrumentTypes) {
	var id = "type";
	var selectedValue = dwr.util.getValue(id);
	dwr.util.removeAllOptions(id);
	dwr.util.addOptions(id, [ "" ]);
	dwr.util.addOptions(id, instrumentTypes);
	dwr.util.addOptions(id, [ "[Other]" ]);
	dwr.util.setValue(id, selectedValue);
}

function setManufacturerOptions(manufacturerTypes) {
	dwr.util.removeAllOptions("instrumentManufacturer"
			+ instrumentManufacturerIndex);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			[ "" ]);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			manufacturerTypes);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			[ "[Other]" ]);
}

var thisConfigId = 0;
function setTheExperimentConfig(configId) {
	show('newExperimentConfig');
	ExperimentConfigManager.findInstrumentTypesByConfigId(configId,
			updateInstrumentDropDown);
	thisConfigId = configId;
	window.setTimeout("doSetTheExperimentConfig()", 300);
}

function doSetTheExperimentConfig() {
	ExperimentConfigManager.findExperimentConfigById(thisConfigId,
			populateExperimentConfig);
}


function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		currentDataSet = experimentConfig;
		dwr.util.setValue("techniqueType", experimentConfig.domain.technique.type);
		dwr.util.setValue("techniqueAbbr",
				experimentConfig.domain.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.domain.description);
		dwr.util.setValue("configId", experimentConfig.domain.id);
		fillTable();
	}
}

var instrumentManufacturerIndex = 0;
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
function addInstrument() {
	var instrument = {
		id :null,
		manufacturer :null,
		modelName :null,
		type :null
	};
	dwr.util.getValues(instrument);
	if (instrument.manufacturer!='' || 
			instrument.modelName!='' ||
			instrument.type!=''){
		ExperimentConfigManager.addInstrument(currentDataSet, instrument,
				function(experimentConfig) {
					currentDataSet = experimentConfig;
				});
		window.setTimeout("fillTable()", 200);
	}else{
		alert('Please fill in values');
	}
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
		window.setTimeout("fillColumnTable()", 200);
//		ExperimentConfigManager.addInstrument(currentDataSet, instrument,
//				function(experimentConfig) {
//					currentDataSet = experimentConfig;
//				});
//		window.setTimeout("fillTable()", 200);
	}else{
		alert('Please fill in values');
	}
}


function addRow() {
	var id = -1;	
	var datumArray = new Array();
	for ( var i = 0; i < columnCount; i++) {
		id = -i - 1;
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
		alert('name='+datum.name+' valueType='+datum.valueType+
				' valueUnit='+datum.valueUnit+' value='+datum.value);
		datumArray[i] = datum;
		if (datum.name!='' || 
				datum.valueType!='' ||
				datum.valueUnit!=''){
			
			//window.setTimeout("fillColumnTable()", 200);
	//		ExperimentConfigManager.addInstrument(currentDataSet, instrument,
	//				function(experimentConfig) {
	//					currentDataSet = experimentConfig;
	//				});
	//		window.setTimeout("fillTable()", 200);
		}else{
			alert('Please fill in values');
		}
		
	}
	alert('bef');
	DataSetManager.addRow(datumArray,
			function(theDataSet) {
				currentDataSet = theDataSet;
			});
	alert('done');
}

function clearInstrument() {
	viewed = -1;
	document.getElementById("id").value = "";
	document.getElementById("manufacturer").value = "";
	document.getElementById("modelName").value = "";
	document.getElementById("type").value = "";
}

var columnCount = 0;

function fillColumnTable() {
	var data = currentDataSet.theDataRow.data;
//	dwr.util
//			.removeAllRows(
//					"instrumentRows",
//					{
//						filter : function(tr) {
//							return (tr.id != "pattern"
//									&& tr.id != "patternHeader" && tr.id != "patternAddRow");
//						}
//					});
	var datum, id;	
	columnCount = 0;
	for ( var i = 0; i < data.length; i++) {
		columnCount++;
		datum = data[i];
		if (datum.id == null) {
			datum.id = -i - 1;
		}
		id = datum.id;
		alert(id+" datum.name="+datum.name+" datum.value="+datum.value);
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
		//instrumentCache[id] = instrument;
	}
	//clearInstrument();
}

function editClicked(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var instrument = instrumentCache[eleid.substring(4)];
	dwr.util.setValues(instrument);
	document.getElementById("manufacturer").focus();
}

function deleteClicked() {
	var eleid = document.getElementById("id").value;
	// we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	//var instrument = instrumentCache[eleid.substring(6)];
	if (eleid!=''){
		var instrument = instrumentCache[eleid];
		if (confirm("Are you sure you want to delete '" + instrument.manufacturer
				+ " " + instrument.modelName+"'?")) {
			ExperimentConfigManager.deleteInstrument(currentDataSet,
					instrument, function(experimentConfig) {
						currentDataSet = experimentConfig;
					});
			window.setTimeout("fillTable()", 200);
		}
	}
}

function addClicked() {
	document.getElementById("manufacturer").focus();	
}

