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
	ExperimentConfigManager.findExperimentConfigById(configId, populateExperimentConfig);
}

function populateExperimentConfig(experimentConfigBean) {	
	alert('########### populateExperimentConfig');
	
	
	if (experimentConfigBean != null) {		
		alert('experimentConfig configDescription = '+configDescription);
	} else {
		alert('Error getting Technique and Instrument data');
	}
	
	dwr.util.removeAllOptions("techniqueType");
	dwr.util.addOptions("techniqueType", experimentConfigBean, "techniqueAbbr", "techniqueAbbr");
}

