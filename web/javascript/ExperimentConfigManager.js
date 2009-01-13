function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != 'other'){
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

function setTheExperimentConfig(configId) {
	alert('setTheExperimentConfig '+configId);
	ExperimentConfigManager.findExperimentConfigById(configId, populateExperimentConfig);
}

function populateExperimentConfig(experimentConfig) {
	alert('########### populateExperimentConfig');
	if (experimentConfig != null) {
		alert('experimentConfig technique type = '+experimentConfig.technique.type);
	} else {
		alert('Error getting Technique and Instrument data');
	}
	dwr.util.setValue("techniqueType", experimentConfig.technique.type);
	dwr.util.setValue("techniqueAbbr", experimentConfig.technique.abbreviation);
	dwr.util.setValue("configDescription", experimentConfig.description);
}

