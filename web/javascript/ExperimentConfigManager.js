
function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != "other") {
		ExperimentConfigManager.findInstrumentTypesByTechniqueType(techniqueType,updateInstrumentDropDown);
		ExperimentConfigManager.findTechniqueByType(techniqueType, updateTechniqueAbbreviation);
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
	for (var i = 0; i < instrumentCount; i++) {	
		var id = "instrumentType" + i;
		var selectedValue = dwr.util.getValue(id);		
		dwr.util.removeAllOptions(id);
		dwr.util.addOptions(id, [""]);
		dwr.util.addOptions(id, instrumentTypes);
		dwr.util.addOptions(id, ["[Other]"]);
		dwr.util.setValue(id, selectedValue);
	}
}
var thisConfigId = 0;
function setTheExperimentConfig(configId) {
	show('newExperimentConfig');
	ExperimentConfigManager.findInstrumentTypesByConfigId(configId,updateInstrumentDropDown);
	thisConfigId = configId;
	window.setTimeout("doSetTheExperimentConfig()", 300);
}

function doSetTheExperimentConfig() {
	ExperimentConfigManager.findExperimentConfigById(thisConfigId, populateExperimentConfig);
}


var instrumentCount = 1;
function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		ExperimentConfigManager.findInstrumentTypesByTechniqueType(experimentConfig.technique.type,updateInstrumentDropDown);		
	}	
}

function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		dwr.util.setValue("techniqueType", experimentConfig.technique.type);
		dwr.util.setValue("techniqueAbbr", experimentConfig.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.description);
		dwr.util.setValue("configId", experimentConfig.id);		
		instrumentCount = experimentConfig.instrumentCollection.length;
		for (var i = 0; i < experimentConfig.instrumentCollection.length; i++) {
			dwr.util.setValue("instrumentType" + i, experimentConfig.instrumentCollection[i].type);
			dwr.util.setValue("instrumentManufacturer" + i, experimentConfig.instrumentCollection[i].manufacturer);
			dwr.util.setValue("instrumentModelName" + i, experimentConfig.instrumentCollection[i].modelName);
		}	
	}	
}

function resetTheExperimentConfig(isShow) {
	if (isShow){
		show('newExperimentConfig');
	}else{
		hide('newExperimentConfig');
	}
	dwr.util.setValue("techniqueType", "");
	dwr.util.setValue("techniqueAbbr", "");
	dwr.util.setValue("configDescription", "");
	dwr.util.setValue("configId", "0");
	instrumentCount = 1;
	for ( var i = 0; i < instrumentCount; i++) {
		var id = "instrumentType" + i;
		dwr.util.removeAllOptions(id);
		dwr.util.addOptions(id, [""]);
		dwr.util.addOptions(id, ["[Other]"]);
		dwr.util.setValue("instrumentManufacturer" + i, "");
		dwr.util.setValue("instrumentModelName" + i, "");
	}
}


var cellFuncs = [
	function(data) {return "<select name='manufacturer'>" +
		"<option value='Malvern'>Malvern</option></select>"},
    function(data) {return "<input size='17' type='text'>";},
	function(data) {return "<select name='type'>" +
			"<option value='Dynamic Light Scattering'>Dynamic Light Scattering</option></select>"},
    function(data) {return "<a href='javascript:deleteRow(\"instrumentTable\","+data+",\"true\");'>" +
    		"<span class='addLink2'>remove</span></a>&nbsp;";}
];
function addRows_test(tableId) {
	dwr.util.addRows( tableId, [''], cellFuncs, { escapeHtml:false });
}
var rowCount = 0;
function addRows(tableId) {    
	rowCount = document.getElementById("instrumentTable").rows.length;
	rowCount = rowCount-3;
	dwr.util.addRows( tableId, [rowCount], cellFuncs, { escapeHtml:false });
}

function deleteRow(tableId, i, hasHeader){
	var index = i;
	if (hasHeader == 'true'){
		index = i+1;
	}
    document.getElementById(tableId).deleteRow(index);
}