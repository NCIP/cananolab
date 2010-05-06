function getPublicCounts() {
	var sites = getSites();
	getProtocolCounts(sites);
	getSampleCounts(sites);
	getPublicationCounts(sites);
}

function getSites() {
	var sites = dwr.util.getValue("sites");
	/* test whether selected sites contains 'all' */
	var isAllSites = false;
	for ( var i = 0; i < sites.length; i++) {
		if (sites[i] == "all") {
			isAllSites = true;
			break;
		}
	}
	var allSites = new Array();
	if (isAllSites) {
		var options = document.getElementById("sites").options;
		for ( var i = 0; i < options.length; i++) {
			if (options[i].value != "all") {
				allSites[i] = options[i].value;
			}
		}
		sites = allSites;
	}
	return sites;
}

function getProtocolCounts(sites) {
	show("protocolLoaderImg");
	hide("protocolCount");
	ProtocolManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			hide("protocolLoaderImg");
			var link = "<a href=javascript:gotoProtocols('search')>" + data
					+ "</a>";
			dwr.util.setValue("protocolCount", link, {
				escapeHtml : false
			});
			show("protocolCount");
		} else {
			show("protocolLoaderImg");
		}
	});
}

var currentSites=null;
function getSampleCounts(sites) {
	show("sampleLoaderImg");
	hide("sampleRelatedCounts");
	hide("sampleCounts");
	hide("moreStats");
	SampleManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			hide("sampleLoaderImg");
			var link = "<a href=javascript:gotoSamples('search')>" + data
					+ "</a>";
			dwr.util.setValue("sampleCount", link, {
				escapeHtml : false
			});
			show("sampleCounts");
			show("sampleCount");
			show("moreStats");
		} else {
			show("sampleLoaderImg");
			hide("moreStats");
			
		}
	});
	currentSites=sites;
	//getSampleSourceCounts(sites);
	//getCharacterizationCounts("Characterization", sites);
	//window.setTimeout("getIndividualCharaCounts()", 500);
}

function getMoreSamplesStats(){
	var sampleRelatedCounts = document.getElementById("sampleRelatedCounts");
	if (sampleRelatedCounts == null) {
		return;
	}else if(sampleRelatedCounts.style.display == "none"){	
		getSampleSourceCounts(currentSites);
		getCharacterizationCounts("Characterization", currentSites);
		//window.setTimeout("getIndividualCharaCounts()", 500);
		getCharacterizationCounts("PhysicoChemicalCharacterization", currentSites);
		getCharacterizationCounts("InvitroCharacterization", currentSites);
		getCharacterizationCounts("InvivoCharacterization", currentSites);
		getCharacterizationCounts("OtherCharacterization", currentSites);				
		show("sampleRelatedCounts");		
	}else{
		hide("sampleRelatedCounts");
	}
}

function getSampleSourceCounts(sites) {
	hide("sampleSourceCount");
	show("sampleSourceCountLoaderImg");
	SampleManager.getPublicSourceCounts(sites, function(data) {
		if (data != null) {
			dwr.util.setValue("sampleSourceCount", data);
			show("sampleSourceCount");
			hide("sampleSourceCountLoaderImg");
		}else{
			show("sampleSourceCountLoaderImg");
		}
	});
}

function getIndividualCharaCounts() {
	getCharacterizationCounts("PhysicoChemicalCharacterization", currentSites);
	getCharacterizationCounts("InvitroCharacterization", currentSites);
	getCharacterizationCounts("InvivoCharacterization", currentSites);
	getCharacterizationCounts("OtherCharacterization", currentSites);
}

function getCharacterizationCounts(charType, sites) {
	hide(charType + "Count");
	show("sample"+charType +"LoaderImg");
	CharacterizationManager.getPublicCharacterizationCounts(charType, sites,
			function(data) {
				if (data != null) {
					dwr.util.setValue(charType + "Count", data);
					show(charType + "Count");
					hide("sample"+charType +"LoaderImg");
				}else{
					show("sample"+charType +"LoaderImg");
				}
			});
}

function getPublicationCounts(sites) {
	show("publicationLoaderImg");
	hide("publicationCount");
	PublicationManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			hide("publicationLoaderImg");
			var link = "<a href=javascript:gotoPublications('search')>" + data
					+ "</a>";
			dwr.util.setValue("publicationCount", link, {
				escapeHtml : false
			});
			show("publicationCount");
		} else {
			show("publicationLoaderImg");
		}
	});
}

function gotoSamples(dispatch) {
	var gridNodesStr = getSitesAsString();
	var url = "/caNanoLab/searchSample.do?dispatch=" + dispatch;
	if (gridNodesStr != null) {
		url += "&searchLocations=" + gridNodesStr;
	}
	gotoPage(url);
	return false;
}

function gotoPublications(dispatch) {
	var gridNodesStr = getSitesAsString();
	var url = "/caNanoLab/searchPublication.do?dispatch=" + dispatch;
	if (gridNodesStr != null) {
		url += "&searchLocations=" + gridNodesStr;
	}
	gotoPage(url);
	return false;
}

function gotoProtocols(dispatch) {
	var gridNodesStr = getSitesAsString();
	var url = "/caNanoLab/searchProtocol.do?dispatch=" + dispatch;
	if (gridNodesStr != null) {
		url += "&searchLocations=" + gridNodesStr;
	}
	gotoPage(url);
	return false;
}

function getSitesAsString() {
	var sites = getSites();
	if (sites.length == 0) {
		return null;
	}
	var sitesStr = "";
	for ( var i = 0; i < sites.length - 1; i++) {
		sitesStr += sites[i] + "~";
	}
	sitesStr += sites[i];
	return sitesStr;
}
