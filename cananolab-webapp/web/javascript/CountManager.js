function getPublicCounts() {
	getProtocolCounts();
	getSampleCounts();
	getPublicationCounts();
}

function getProtocolCounts() {
	show("protocolLoaderImg");
	hide("protocolCount");
	ProtocolManager.getPublicCounts(function(data) {
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

function getSampleCounts() {
	show("sampleLoaderImg");
	hide("sampleRelatedCounts");
	hide("sampleCounts");
	SampleManager.getPublicCounts(function(data) {
		if (data != null) {
			hide("sampleLoaderImg");
			var link = "<a href=javascript:gotoSamples('search')>" + data
					+ "</a>";
			dwr.util.setValue("sampleCount", link, {
				escapeHtml : false
			});
			show("sampleCounts");
			show("moreStats");
		} else {
			show("sampleLoaderImg");
			hide("moreStats");
		}
	});
}

function getMoreSamplesStats() {
	hide("sampleRelatedCounts");
	show("sampleRelatedLoaderImg");
	var sampleRelatedCounts = document.getElementById("sampleRelatedCounts");
	// dwr.engine.beginBatch(); //doesn't work properly in IE7
	getSampleSourceCounts();
	getCharacterizationCounts("Characterization");
	getCharacterizationCounts("PhysicoChemicalCharacterization");
	getCharacterizationCounts("InvitroCharacterization");
	getCharacterizationCounts("InvivoCharacterization");
	getCharacterizationCounts("OtherCharacterization");
	/*
	 * dwr.engine.endBatch( { async : false });
	 */
}

function getSampleSourceCounts() {
	SampleManager.getPublicSourceCounts(function(data) {
		if (data != null) {
			dwr.util.setValue("sampleSourceCount", data);
			show("sampleRelatedCounts");
			hide("sampleRelatedLoaderImg");
		}
	});
}

function getCharacterizationCounts(charType) {
	// show an example of how to set async for individual call back
	CharacterizationManager.getPublicCharacterizationCounts(charType, {
		callback : function(data) {
			if (data != null) {
				dwr.util.setValue(charType + "Count", data);
				show("sampleRelatedCounts");
				hide("sampleRelatedLoaderImg");
			}
		},
		async : true
	});
}

function getPublicationCounts() {
	show("publicationLoaderImg");
	hide("publicationCount");
	PublicationManager.getPublicCounts(function(data) {
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
	var url = "/caNanoLab/searchSample.do?dispatch=" + dispatch;
	gotoPage(url);
	return false;
}

function gotoPublications(dispatch) {
	var url = "/caNanoLab/searchPublication.do?dispatch=" + dispatch;
	gotoPage(url);
	return false;
}

function gotoProtocols(dispatch) {
	var url = "/caNanoLab/searchProtocol.do?dispatch=" + dispatch;
	gotoPage(url);
	return false;
}
