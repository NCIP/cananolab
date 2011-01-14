function showMatchedDesignTypes() {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedTermSelect");
	hide("selectButton");
	hide("searchMessage");
	var searchStr = dwr.util.getValue("searchStr");
	StudyManager.getMatchedDesignTypes(searchStr, function(data) {
		if (data.length>0) {
		  dwr.util.removeAllOptions("matchedTermSelect");
		  dwr.util.addOptions("matchedTermSelect", data);
		  hide("loaderImg");
		  hide("searchMessage");
		  show("matchedTermSelect");
		  show("addButton");
		}
		else {
		  show("searchMessage");
		}
	});
}

function populateDesignTypes() {
	// display progress.gif while waiting for the response.
	hide("termBrowser");
	hide("matchedTermSelect");
	hide("addButton");
	var selected = dwr.util.getValue("matchedTermSelect");
	if (selected.length == 0) {
		return;
	}
	var existingTypes=dwr.util.getValue("designTypes");
	var types="";
	if (existingTypes=="") {
		types=existingTypes;
	}
	else {
		types=existingTypes+"\n";
	}
	for (i=0; i<selected.length-1; i++) {
		types=types+selected[i]+"\n";
	}
	types=types+selected[i];
	dwr.util.setValue("designTypes", types, {
		escapeHtml : false
	});
}
