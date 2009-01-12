function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != 'other'){
		CharacterizationManager.getTechniqueAbbreviation(techniqueType, updateValue);
	}
}
function updateValue(textValue) {
	if (textValue != null) {		
		document.getElementById("techniqueAbbr").value = textValue;
	} else {
		document.getElementById("techniqueAbbr").value = "";
	}
}

function getUnit(fileInd, datumInd) {
	var datumName=document.getElementById("datumName"+fileInd+"-"+datumInd).value;
	CharacterizationManager.getDerivedDatumValueUnits(datumName,function(data) {			
			dwr.util.removeAllOptions("unit"+fileInd+"-"+datumInd);
			dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['']);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, data);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['[Other]']);
  	});
}

function setTheExperimentConfig(configId) {
	alert('setTheExperimentConfig '+configId);
	ExperimentConfigManager.findExperimentConfigById(configId, updateExperimentConfig);
}

function updateExperimentConfig(experimentConfig) {	
	if (experimentConfig != null) {		
		alert('experimentConfig = '+experimentConfig.id);
		document.getElementById("techniqueAbbr").value = textValue;
	} else {
		alert('Error getting Technique and Instrument data');
	}
}

