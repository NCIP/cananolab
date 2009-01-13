
function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != "other") {
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
	ExperimentConfigManager.findExperimentConfigById(configId, populateExperimentConfig);
}
function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		dwr.util.setValue("techniqueType", experimentConfig.technique.type);
		dwr.util.setValue("techniqueAbbr", experimentConfig.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.description);
		for (var i = 0; i < experimentConfig.instrumentCollection.length; i++) {
			dwr.util.setValue("instrumentType" + i, experimentConfig.instrumentCollection[i].type);
			dwr.util.setValue("instrumentManufacturer" + i, experimentConfig.instrumentCollection[i].manufacturer);
			dwr.util.setValue("instrumentModelName" + i, experimentConfig.instrumentCollection[i].modelName);
		}
	}
}

