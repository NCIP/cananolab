var emptyOption = [ {
	label : "",
	value : ""
} ];

function retrieveParticleNames() {
	var sampleType = document.getElementById("sampleType").value;
	ParticleManager
			.getNewParticleNamesByType(sampleType, populateParticleNames);
}
function resetParticleNames() {
	dwr.util.removeAllOptions("sampleName");
	dwr.util.addOptions("sampleName", emptyOption, "value", "label");
}
function populateParticleNames(sampleNames) {
	// get previous selection
	var selectedParticleName = dwr.util.getValue("sampleName");
	// remove option that's the same as previous selection
	var updatedParticleNames = new Array();
	if (sampleNames != null) {
		for (i = 0; i < sampleNames.length; i++) {
			if (sampleNames[i] != selectedParticleName) {
				updatedParticleNames.push(sampleNames[i]);
			}
		}
	}
	dwr.util.addOptions("sampleName", updatedParticleNames);
}

function setSampleDropdowns() {
	var searchLocations = getSelectedOptions(document
			.getElementById("searchLocations"));
	SampleManager.getNanomaterialEntityTypes(searchLocations, function(data) {
		dwr.util.removeAllOptions("nanomaterialEntityTypes");
		dwr.util.addOptions("nanomaterialEntityTypes", data);
	});
	SampleManager.getFunctionalizingEntityTypes(searchLocations,
			function(data) {
				dwr.util.removeAllOptions("functionalizingEntityTypes");
				dwr.util.addOptions("functionalizingEntityTypes", data);
			});

	SampleManager.getFunctionTypes(searchLocations, function(data) {
		dwr.util.removeAllOptions("functionTypes");
		dwr.util.addOptions("functionTypes", data);
	});
	return false;
}

function updateCompositionEntityDropdowns() {
	var compositionType = dwr.util.getValue("compType");
	if (compositionType == "nanomaterial entity") {
		SampleManager.getNanomaterialEntityTypes(null, function(data) {
			dwr.util.removeAllOptions("entityType");
			dwr.util.addOptions("entityType", emptyOption, "value", "label");
			dwr.util.addOptions("entityType", data);
		});
	} else if (compositionType = "functionalizing entity") {
		SampleManager.getFunctionalizingEntityTypes(null, function(data) {
			dwr.util.removeAllOptions("entityType");
			dwr.util.addOptions("entityType", emptyOption, "value", "label");
			dwr.util.addOptions("entityType", data);
		});
	}
}
