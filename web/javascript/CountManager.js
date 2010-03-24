function getPublicCounts() {
	var sites = dwr.util.getValue("sites");
	/* test whether selected sites contains 'all' */
	var isAllSites=false;
	for (var i=0; i<sites.length; i++) {
		if (sites[i]=="all") {
			isAllSites = true;
			break;
		}
	}
	var allSites=new Array();
	if (isAllSites) {
		var options=document.getElementById("sites").options;
		for(var i=0; i<options.length; i++) {
			allSites[i]=options[i].value;
		}
		sites=allSites;
	}
	getProtocolCounts(sites);
	getSampleCounts(sites);
	getPublicationCounts(sites);
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

function getSampleCounts(sites) {
	show("sampleLoaderImg");
	hide("sampleCount");
	SampleManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			hide("sampleLoaderImg");
			var link = "<a href=javascript:gotoSamples('search')>" + data
					+ "</a>";
			dwr.util.setValue("sampleCount", link, {
				escapeHtml : false
			});			
			show("sampleCount");
		} else {
			show("sampleLoaderImg");
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
	var selectEle = document.getElementById("sites");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchSample.do?dispatch=" + dispatch
			+ "&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function gotoPublications(dispatch) {
	var selectEle = document.getElementById("sites");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchPublication.do?dispatch=" + dispatch
			+ "&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function gotoProtocols(dispatch) {
	var selectEle = document.getElementById("sites");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchProtocol.do?dispatch=" + dispatch
			+ "&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function getSelectedOptions(selectEle) {
	var options = selectEle.options;
	var selectedValues = "";
	for ( var c = 0; c < options.length; c++) {
		if (options[c].selected) { // true if selected.
			selectedValues += options[c].value + "~";
		}
	}
	var cleanStr = selectedValues.substr(0, selectedValues.length - 1);
	return cleanStr;
}
