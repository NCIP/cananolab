
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
function setTheExperimentConfig(configId) {
	show('newExperimentConfig');
	ExperimentConfigManager.findExperimentConfigById(configId, populateExperimentConfig);
}

var instrumentCount = 1;
function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		dwr.util.setValue("techniqueType", experimentConfig.technique.type);
		dwr.util.setValue("techniqueAbbr", experimentConfig.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.description);
		ExperimentConfigManager.findInstrumentTypesByTechniqueType(experimentConfig.technique.type,updateInstrumentDropDown);
		instrumentCount = experimentConfig.instrumentCollection.length;
		for (var i = 0; i < experimentConfig.instrumentCollection.length; i++) {
			dwr.util.setValue("instrumentType" + i, experimentConfig.instrumentCollection[i].type);
			dwr.util.setValue("instrumentManufacturer" + i, experimentConfig.instrumentCollection[i].manufacturer);
			dwr.util.setValue("instrumentModelName" + i, experimentConfig.instrumentCollection[i].modelName);
		}	
	}	
}

function resetTheExperimentConfig() {
	show('newExperimentConfig');
	dwr.util.setValue("techniqueType", "");
	dwr.util.setValue("techniqueAbbr", "");
	dwr.util.setValue("configDescription", "");
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

