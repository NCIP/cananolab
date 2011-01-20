function showMatchedDesignTypes(ind) {
	// display progress.gif while waiting for the response.
	show("loaderImg" + ind);
	hide("matchedTermSelect" + ind);
	hide("selectButton" + ind);
	hide("searchMessage" + ind);
	var searchStr = dwr.util.getValue("searchStr" + ind);
	StudyManager.getMatchedDesignTypes(searchStr, function(data) {
		if (data.length > 0) {
			dwr.util.removeAllOptions("matchedTermSelect" + ind);
			dwr.util.addOptions("matchedTermSelect" + ind, data);
			hide("loaderImg" + ind);
			hide("searchMessage" + ind);
			show("matchedTermSelect" + ind);
			show("addButton" + ind);
		} else {
			show("searchMessage" + ind);
		}
	});
}

function populateDesignTypes(ind) {
	// display progress.gif while waiting for the response.
	hide("termBrowser" + ind);
	hide("matchedTermSelect" + ind);
	hide("addButton" + ind);
	populateTextAreaWithSelection("designTypes", "matchedTermSelect" + ind);
}

function showMatchedDiseases(ind) {
	// display progress.gif while waiting for the response.
	show("loaderImg" + ind);
	hide("matchedTermSelect" + ind);
	hide("selectButton" + ind);
	hide("searchMessage" + ind);
	var searchStr = dwr.util.getValue("searchStr" + ind);
	var algorithm = dwr.util.getValue("algorithm" + ind);
	StudyManager.getMatchedDiseases(searchStr, algorithm, function(data) {
		if (data.length > 0) {
			dwr.util.removeAllOptions("matchedTermSelect" + ind);
			dwr.util.addOptions("matchedTermSelect" + ind, data);
			hide("loaderImg" + ind);
			hide("searchMessage" + ind);
			show("matchedTermSelect" + ind);
			show("addButton" + ind);
		} else {
			show("searchMessage" + ind);
		}
	});
}

function populateDiseases(ind) {
	// display progress.gif while waiting for the response.
	hide("termBrowser" + ind);
	hide("matchedTermSelect" + ind);
	hide("addButton" + ind);
	populateTextAreaWithSelection("diseases", "matchedTermSelect" + ind);
}
