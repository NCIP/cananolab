var emptyOption = [{label:"", value:""}];

function retrieveParticleNames() {
	var sampleType = document.getElementById("sampleType").value;
	ParticleManager.getNewParticleNamesByType(sampleType, populateParticleNames);
}
function resetParticleNames() {
	dwr.util.removeAllOptions("sampleName");
	dwr.util.addOptions("sampleName", emptyOption, "value", "label");
}
function populateParticleNames(sampleNames) {
   //get previous selection
	var selectedParticleName = dwr.util.getValue("sampleName");
	//remove option that's the same as previous selection
	var updatedParticleNames = new Array();
	if (sampleNames != null) {
		for (i = 0; i < sampleNames.length; i++) {
			if (sampleNames[i]!= selectedParticleName) {
				updatedParticleNames.push(sampleNames[i]);
			}
		}
	}
	dwr.util.addOptions("sampleName", updatedParticleNames);
}

function removeOrgVisibility(selectId) {
	var pocField = document.getElementById(selectId);	
	if (pocField==null){
		return false;
	}
	var poc = pocField.value;
	if(poc.charAt(0) == "[" &&
			poc.charAt(otext.length - 1) == "]") 
			return false;
			
	SampleManager.removeOrgVisibility(poc, function (data) {
		dwr.util.removeAllOptions("visibilityGroup");
		dwr.util.addOptions("visibilityGroup", data);
	});
	return false;
}

function removeOrgVisibilityByName(selectId, visibilityGroupName) {
	var pocField = document.getElementById(selectId);	
	if (pocField==null){
		return false;
	}
	var poc = pocField.value;
	if(poc.charAt(0) == "[" &&
			poc.charAt(otext.length - 1) == "]") 
			return false;
	
	SampleManager.removeOrgNameVisibility(poc, function (data) {
		dwr.util.removeAllOptions(visibilityGroupName);
		dwr.util.addOptions(visibilityGroupName, data);
	});
	return false;
}

function setSampleDropdowns() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	SampleManager.getNanomaterialEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("nanomaterialEntityTypes");
			dwr.util.addOptions("nanomaterialEntityTypes", data);
		});
	SampleManager.getFunctionalizingEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionalizingEntityTypes");
			dwr.util.addOptions("functionalizingEntityTypes", data);
		});
		
	SampleManager.getFunctionTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionTypes");
			dwr.util.addOptions("functionTypes", data);
		});
	return false;
}

