function getPublicCounts() {
	getProtocolCounts();
	getSampleCounts();
	getPublicationCounts();
}

function getProtocolCounts() {
	var sites = dwr.util.getValue("sites");
	ProtocolManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			var link = "<a href=javascript:gotoProtocols('search')>" + data
					+ "</a>";
			dwr.util.setValue("protocolCount", link, {
				escapeHtml : false
			});
		} else {
			dwr.util.setValue("protocolCount", "");
		}
	});
}

function getSampleCounts() {
	var sites = dwr.util.getValue("sites");
	SampleManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			var link = "<a href=javascript:gotoSamples('search')>" + data
					+ "</a>";
			dwr.util.setValue("sampleCount", link, {
				escapeHtml : false
			});
		} else {
			dwr.util.setValue("sampleCount", "");
		}
	});
}

function getPublicationCounts() {
	var sites = dwr.util.getValue("sites");
	PublicationManager.getPublicCounts(sites, function(data) {
		if (data != null) {
			var link = "<a href=javascript:gotoPublications('search')>" + data
					+ "</a>";
			dwr.util.setValue("publicationCount", link, {
				escapeHtml : false
			});
		} else {
			dwr.util.setValue("publicationCount", "");
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
