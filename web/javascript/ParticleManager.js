var emptyOption = [{label:"", value:""}];

function retrieveParticleNames() {
	var particleType = document.getElementById("particleType").value;
	ParticleManager.getNewParticleNamesByType(particleType, populateParticleNames);
}
function resetParticleNames() {
	dwr.util.removeAllOptions("particleName");
	dwr.util.addOptions("particleName", emptyOption, "value", "label");
}
function populateParticleNames(particleNames) {
   //get previous selection
	var selectedParticleName = dwr.util.getValue("particleName");
	//remove option that's the same as previous selection
	var updatedParticleNames = new Array();
	if (particleNames != null) {
		for (i = 0; i < particleNames.length; i++) {
			if (particleNames[i]!= selectedParticleName) {
				updatedParticleNames.push(particleNames[i]);
			}
		}
	}
	dwr.util.addOptions("particleName", updatedParticleNames);
}

