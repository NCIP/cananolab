function setReportDropdowns() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	ReportManager.getReportCategories(searchLocations, function (data) {
			dwr.util.removeAllOptions("reportCategory");
			dwr.util.addOptions("reportCategory", data);
		});
	NanoparticleSampleManager.getNanoparticleEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("nanoparticleEntityTypes");
			dwr.util.addOptions("nanoparticleEntityTypes", data);
		});
	NanoparticleSampleManager.getFunctionalizingEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionalizingEntityTypes");
			dwr.util.addOptions("functionalizingEntityTypes", data);
		});
		
	NanoparticleSampleManager.getFunctionTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionTypes");
			dwr.util.addOptions("functionTypes", data);
		});
	return false;
}

function setPublicationDropdowns() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	PublicatioManager.getPublicatioCategories(searchLocations, function (data) {
			dwr.util.removeAllOptions("publicatioCategory");
			dwr.util.addOptions("publicatioCategory", data);
		});
	NanoparticleSampleManager.getNanoparticleEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("nanoparticleEntityTypes");
			dwr.util.addOptions("nanoparticleEntityTypes", data);
		});
	NanoparticleSampleManager.getFunctionalizingEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionalizingEntityTypes");
			dwr.util.addOptions("functionalizingEntityTypes", data);
		});
		
	NanoparticleSampleManager.getFunctionTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionTypes");
			dwr.util.addOptions("functionTypes", data);
		});
	return false;
}