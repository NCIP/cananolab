function showMatchedDesignTypes() {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedTermSelect");
	hide("selectButton");
	hide("searchMessage");
	var searchStr = dwr.util.getValue("searchStr");
	StudyManager.getMatchedDesignTypes(searchStr, function(data) {
		if (data.length > 0) {
			dwr.util.removeAllOptions("matchedTermSelect");
			dwr.util.addOptions("matchedTermSelect", data);
			hide("loaderImg");
			hide("searchMessage");
			show("matchedTermSelect");
			show("addButton");
		} else {
			show("searchMessage");
		}
	});
}

function populateDesignTypes() {
	// display progress.gif while waiting for the response.
	hide("termBrowser");
	hide("matchedTermSelect");
	hide("addButton");
	populateTextAreaWithSelection("designTypes", "matchedTermSelect");
}
