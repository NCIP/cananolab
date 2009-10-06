var emptyOption = [ {
	label : " -- Please Select -- ",
	value : ""
} ];
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

var sampleQueryCache = {};
var numberOfSampleQueries = 0;
var currentSearchBean = null;

function clearSampleQuery() {
	dwr.util.setValue("sampleQueryId", "");
	dwr.util.setValue("nameType", "");
	dwr.util.setValue("sampleOperand", "");
	dwr.util.setValue("name", "");
	hide("deleteSampleQuery");
}

function addSampleQuery() {
	var queryId = dwr.util.getValue("sampleQueryId");
	if (queryId == null || queryId == "") {
		queryId = -1000 - numberOfSampleQueries;
	}
	var theQuery = {
		id : queryId,
		nameType : dwr.util.getValue("nameType"),
		name : dwr.util.getValue("name"),
		operand : dwr.util.getValue("sampleOperand")
	};
	if (theQuery.nameType != "" && theQuery.name != ""
			&& theQuery.operand != "") {
		SampleManager.addSampleQuery(theQuery, function(searchBean) {
			if (searchBean != null) {
				currentSearchBean = searchBean;
				populateSampleQueries();
			} else {
				sessionTimeout();
			}
		});
	} else {
		alert("Please make selections in the first two drop-down lists and fill in the text field. ");
	}
}

function displaySampleQueries() {
	SampleManager.addSampleQuery(null, function(searchBean) {
		if (searchBean != null) {
			currentSearchBean = searchBean;
			populateSampleQueries();
		} else {
			sessionTimeout();
		}
	});
}

function populateSampleQueries() {
	var queries = currentSearchBean.sampleQueries;
	dwr.util.removeAllRows("sampleQueryRows", {
		filter : function(tr) {
			return (tr.id != "samplePattern");
		}
	});
	var theQuery, id;
	if (queries.length > 0) {
		show("sampleQueryTable");
		// show operator only when there are more than one queries
		if (queries.length > 1) {
			show("sampleLogicalOperator");
		} else {
			hide("sampleLogicalOperator");
		}
	} else {
		hide("sampleQueryTable");
		hide("sampleLogicalOperator");
	}
	for ( var i = 0; i < queries.length; i++) {
		theQuery = queries[i];
		// alert("sample "+theQuery.id);
		if (theQuery.id == null || theQuery.id == "") {
			theQuery.id = -i - 1;
		}
		id = theQuery.id;
		dwr.util.cloneNode("samplePattern", {
			idSuffix : id
		});
		dwr.util.setValue("nameTypeValue" + id, theQuery.nameType);
		dwr.util.setValue("sampleOperandValue" + id, theQuery.operand);
		dwr.util.setValue("nameValue" + id, theQuery.name);
		dwr.util.setValue("sampleQueryId", id);
		$("samplePattern" + id).style.display = "";
		if (sampleQueryCache[id] == null) {
			numberOfSampleQueries++;
		}
		sampleQueryCache[id] = theQuery;
	}
	clearSampleQuery();
	// alert("sample :"+numberOfSampleQueries);
}

function editSampleQuery(eleid) {
	// we were an id of the form "edit{id}", eg "sampleEdit42". We lookup the
	// "42"
	var query = sampleQueryCache[eleid.substring(10)];
	dwr.util.setValue("nameType", query.nameType);
	dwr.util.setValue("sampleOperand", query.operand);
	dwr.util.setValue("name", query.name);
	dwr.util.setValue("sampleQueryId", query.id);
	show("deleteSampleQuery");
}

function deleteTheSampleQuery() {
	var eleid = document.getElementById("sampleQueryId").value;
	if (eleid != "") {
		var query = sampleQueryCache[eleid];
		if (confirm("Are you sure you want to delete this query?")) {
			SampleManager.deleteSampleQuery(query, function(searchBean) {
				if (searchBean != null) {
					currentSearchBean = searchBean;
					populateSampleQueries();
				} else {
					sessionTimeout();
				}
			});
		}
	}
}

function setCompositionEntityOptions(selectedEntity) {
	var compositionType = dwr.util.getValue("compType");
	SampleManager.getDecoratedEntityTypes(compositionType, function(data) {
		dwr.util.removeAllOptions("entityType");
		dwr.util.addOptions("entityType", emptyOption, "value", "label");
		dwr.util.addOptions("entityType", data, "value", "label");
		if (selectedEntity != null) {
			dwr.util.setValue("entityType", selectedEntity);
		}
		if (compositionType == "function") {
			hide("compChemicalNameLabel");
			hide("compOperand");
			hide("chemicalName");
		} else {
			show("compChemicalNameLabel");
			show("compOperand");
			show("chemicalName");
		}
	});
}

var compositionQueryCache = {};
var numberOfCompQueries = 0;
var currentSearchBean = null;

function clearCompositionQuery() {
	dwr.util.setValue("compQueryId", "");
	dwr.util.setValue("compType", "");
	dwr.util.setValue("entityType", "");
	dwr.util.setValue("compOperand", "");
	dwr.util.setValue("chemicalName", "");
	hide("deleteCompositionQuery");
}

function addCompositionQuery() {
	var queryId = dwr.util.getValue("compQueryId");
	if (queryId == null || queryId == "") {
		queryId = -10000 - numberOfCompQueries;
	}
	var theQuery = {
		id : queryId,
		compositionType : dwr.util.getValue("compType"),
		entityType : dwr.util.getValue("entityType"),
		chemicalName : dwr.util.getValue("chemicalName"),
		operand : dwr.util.getValue("compOperand")
	};
	if (theQuery.entityType != "") {
		SampleManager.addCompositionQuery(theQuery, function(searchBean) {
			if (searchBean != null) {
				currentSearchBean = searchBean;
				populateCompositionQueries();
			} else {
				sessionTimeout();
			}
		});
	} else {
		alert("Please at least make a selection from the second drop-down list.");
	}
}

function displayCompositionQueries() {
	SampleManager.addCompositionQuery(null, function(searchBean) {
		if (searchBean != null) {
			currentSearchBean = searchBean;
			populateCompositionQueries();
		} else {
			sessionTimeout();
		}
	});

}

function populateCompositionQueries() {
	var queries = currentSearchBean.compositionQueries;
	dwr.util.removeAllRows("compositionQueryRows", {
		filter : function(tr) {
			return (tr.id != "compPattern");
		}
	});
	var theQuery, id;
	if (queries.length > 0) {
		show("compositionQueryTable");
		// show operator only when there are more than one queries
		if (queries.length > 1) {
			show("compositionLogicalOperator");
		} else {
			hide("compositionLogicalOperator");
		}
	} else {
		hide("compositionQueryTable");
		hide("compositionLogicalOperator");
	}
	for ( var i = 0; i < queries.length; i++) {
		theQuery = queries[i];
		// alert("comp: "+theQuery.id);
		if (theQuery.id == null || theQuery.id == "") {
			theQuery.id = -i - 1;
		}
		id = theQuery.id;
		dwr.util.cloneNode("compPattern", {
			idSuffix : id
		});
		dwr.util.setValue("compTypeValue" + id, theQuery.compositionType);
		dwr.util.setValue("entityTypeValue" + id, theQuery.entityType);
		dwr.util.setValue("compOperandValue" + id, theQuery.operand);
		dwr.util.setValue("chemicalNameValue" + id, theQuery.chemicalName);
		dwr.util.setValue("compQueryId", id);
		$("compPattern" + id).style.display = "";
		if (compositionQueryCache[id] == null) {
			numberOfCompQueries++;
		}
		compositionQueryCache[id] = theQuery;
	}
	clearCompositionQuery();
	// alert("comp: "+numberOfCompQueries);
}

function editCompositionQuery(eleid) {
	// we were an id of the form "edit{id}", eg "compEdit42". We lookup the "42"
	var query = compositionQueryCache[eleid.substring(8)];
	dwr.util.setValue("compType", query.compositionType);
	setCompositionEntityOptions(query.entityType);
	dwr.util.setValue("compOperand", query.operand);
	dwr.util.setValue("chemicalName", query.chemicalName);
	dwr.util.setValue("compQueryId", query.id);
	show("deleteCompositionQuery");
}

function deleteTheCompositionQuery() {
	var eleid = document.getElementById("compQueryId").value;
	if (eleid != "") {
		var query = compositionQueryCache[eleid];
		if (confirm("Are you sure you want to delete this query?")) {
			SampleManager.deleteCompositionQuery(query, function(searchBean) {
				if (searchBean != null) {
					currentSearchBean = searchBean;
					populateCompositionQueries();
				} else {
					sessionTimeout();
				}
			});
		}
	}
}

// characterization specific
function setCharacterizationOptions(selectedChar, selectedDatumName,
		selectedOperand, selectedUnit) {
	var charType = dwr.util.getValue("charType");
	// turn null values into empty strings
	if (selectedChar == null) {
		selectedChar = "";
	}
	if (selectedDatumName == null) {
		selectedDatumName = "";
	}
	if (selectedOperand == null) {
		selectedOperand = "";
	}
	if (selectedUnit == null) {
		selectedUnit = "";
	}
	CharacterizationManager.getDecoratedCharacterizationOptions(charType,
			function(data) {
				dwr.util.removeAllOptions("charName");
				dwr.util.addOptions("charName", emptyOption, "value", "label");
				dwr.util.addOptions("charName", data, "value", "label");
				dwr.util.setValue("charName", selectedChar);
				setDatumNameOptionsByCharName(selectedDatumName,
						selectedOperand, selectedUnit);
			});
}

function setDatumNameOptionsByCharName(selectedName, selectedOperand,
		selectedUnit) {
	if (selectedName == null) {
		selectedName = "";
	}
	if (selectedOperand == null) {
		selectedOperand = "";
	}
	if (selectedUnit == null) {
		selectedUnit = "";
	}
	var charType = dwr.util.getValue("charType");
	var charName = dwr.util.getValue("charName");
	if (charName.match("^ --")) {
		var assayType = charName.replace(" --", "");
	}
	FindingManager
			.getDecoratedDatumNameOptions(
					charType,
					charName,
					assayType,
					function(data) {
						dwr.util.removeAllOptions("datumName");
						dwr.util.addOptions("datumName", emptyOption, "value",
								"label");
						dwr.util
								.addOptions("datumName", data, "value", "label");
						// if there is only one in the option, preselect it
						if (data.length == 1) {
							dwr.util.setValue("datumName", data[0].value);
						} else {
							dwr.util.setValue("datumName", selectedName);
						}
						setDatumValueOptions();
						setCharacterizationOperandOptions(selectedOperand);
						setDatumValueUnitOptions(selectedUnit);
					});
}

function setDatumValueUnitOptions(selectedUnit) {
	if (selectedUnit == null) {
		selectedUnit = "";
	}
	var datumName = dwr.util.getValue("datumName");
	FindingManager.getColumnValueUnitOptions(datumName, null,
			function(data) {
				if (data != null && data.length > 0) {
					show("datumValueUnitBlock");
					dwr.util.removeAllOptions("datumValueUnit");
					dwr.util.addOptions("datumValueUnit", emptyOption, "value",
							"label");
					dwr.util.addOptions("datumValueUnit", data);
					// if there is only one in the option, preselect it
			if (data.length == 1) {
				dwr.util.setValue("datumValueUnit", data[0]);
			} else {
				dwr.util.setValue("datumValueUnit", selectedUnit);
			}
		} else {
			hide("datumValueUnitBlock");
		}
	});
}

function setCharacterizationOperandOptions(selectedOperand) {
	if (selectedOperand == null) {
		selectedOperand = "";
	}
	var datumName = dwr.util.getValue("datumName");
	SampleManager.getCharacterizationOperandOptions(datumName, function(data) {
		dwr.util.removeAllOptions("charOperand");
		dwr.util.addOptions("charOperand", emptyOption, "value", "label");
		dwr.util.addOptions("charOperand", data, "value", "label");
		if (data.length == 1) {
			dwr.util.setValue("charOperand", data[0].value, {escapeHtml : false});			
		} else {
			dwr.util.setValue("charOperand", selectedOperand, {escapeHtml : false});
		}
	});
}

function setDatumValueOptions() {
	var datumName = dwr.util.getValue("datumName");
	if (datumName.match("^is ")) {
		hide("datumValueTextBlock");
		show("datumValueSelectBlock");
	} else {
		show("datumValueTextBlock");
		hide("datumValueSelectBlock");
	}
}

var characterizationQueryCache = {};
var numberOfCharQueries = 0;
var currentSearchBean = null;

function clearCharacterizationQuery() {
	dwr.util.setValue("charQueryId", "");
	dwr.util.setValue("charType", "");
	dwr.util.setValue("charName", "");
	dwr.util.setValue("datumName", "");
	dwr.util.setValue("charOperand", "");
	dwr.util.setValue("datumValue", "");
	dwr.util.setValue("datumValueUnit", "");
	hide("deleteCharacterizationQuery");
}

function addCharacterizationQuery() {
	var queryId = dwr.util.getValue("charQueryId");
	if (queryId == null || queryId == "") {
		queryId = -100000 - numberOfCharQueries;
	}
	var datumName = dwr.util.getValue("datumName");
	var datumValueUnit=dwr.util.getValue("datumValueUnit");
	if (datumName.match("^is ")) {
		var datumValue = dwr.util.getValue("datumValueSelect");
		var datumValueBoolean = true;
		datumValueUnit="";
	} else {
		datumValue = dwr.util.getValue("datumValue");
		datumValueBoolean = false;
	}
	var theQuery = {
		id : queryId,
		characterizationType : dwr.util.getValue("charType"),
		characterizationName : dwr.util.getValue("charName"),
		datumName : dwr.util.getValue("datumName"),
		operand : dwr.util.getValue("charOperand"),
		datumValue : datumValue,
		datumValueUnit : datumValueUnit,
		datumValueBoolean : datumValueBoolean
	};
	if (theQuery.characterizationName != "") {
		SampleManager.addCharacterizationQuery(theQuery, function(searchBean) {
			if (searchBean != null) {
				currentSearchBean = searchBean;
				populateCharacterizationQueries();
			} else {
				sessionTimeout();
			}
		});
	} else {
		alert("Please at least make a selection from the second drop-down list.");
	}
}

function displayCharacterizationQueries() {
	SampleManager.addCharacterizationQuery(null, function(searchBean) {
		if (searchBean != null) {
			currentSearchBean = searchBean;
			populateCharacterizationQueries();
		} else {
			sessionTimeout();
		}
	});
}

function populateCharacterizationQueries() {
	var queries = currentSearchBean.characterizationQueries;
	dwr.util.removeAllRows("characterizationQueryRows", {
		filter : function(tr) {
			return (tr.id != "charPattern");
		}
	});
	var theQuery, id;
	if (queries.length > 0) {
		show("characterizationQueryTable");
		// show operator only when there are more than one query
		if (queries.length > 1) {
			show("characterizationLogicalOperator");
		} else {
			hide("characterizationLogicalOperator");
		}
	} else {
		hide("characterizationQueryTable");
		hide("characterizationLogicalOperator");
	}
	for ( var i = 0; i < queries.length; i++) {
		theQuery = queries[i];
		if (theQuery.id == null || theQuery.id == "") {
			theQuery.id = -i - 1;
		}
		id = theQuery.id;
		dwr.util.cloneNode("charPattern", {
			idSuffix : id
		});
		dwr.util.setValue("charTypeValue" + id, theQuery.characterizationType);
		// combine assay type into characterization name
		if (theQuery.assayType != "") {
			theQuery.characterizationName = theQuery.characterizationName + ":"
					+ theQuery.assayType;
			theQuery.assayType = "";
		}
		dwr.util.setValue("charNameValue" + id, theQuery.characterizationName);

		dwr.util.setValue("datumNameValue" + id, theQuery.datumName);
		dwr.util.setValue("charOperandValue" + id, theQuery.operand);

		// display the words true/false if boolean
		if (theQuery.datumValueBoolean == true) {
			if (theQuery.datumValue == 1) {
				var datumValueString = "true";
			} else {
				datumValueString = "false";
			}
			dwr.util.setValue("datumValueValue" + id, datumValueString);
		} else {
			dwr.util.setValue("datumValueValue" + id, theQuery.datumValue);
		}
		if (theQuery.operand == "") {
			dwr.util.setValue("datumValueUnitValue" + id, "");
		} else {
			dwr.util.setValue("datumValueUnitValue" + id,
					theQuery.datumValueUnit);
		}
		dwr.util.setValue("charQueryId", id);
		$("charPattern" + id).style.display = "";
		if (characterizationQueryCache[id] == null) {
			numberOfCharQueries++;
		}
		characterizationQueryCache[id] = theQuery;
	}
	clearCharacterizationQuery();
}

function editCharacterizationQuery(eleid) {
	// we were an id of the form "edit{id}", eg "charEdit42". We lookup the "42"
	var query = characterizationQueryCache[eleid.substring(8)];
	dwr.util.setValue("charType", query.characterizationType);
	setCharacterizationOptions(query.characterizationName, query.datumName,
			query.operand, query.unit);
	dwr.util.setValue("datumValue", query.datumValue);
	dwr.util.setValue("charQueryId", query.id);
	show("deleteCharacterizationQuery");
}

function deleteTheCharacterizationQuery() {
	var eleid = document.getElementById("charQueryId").value;
	if (eleid != "") {
		var query = characterizationQueryCache[eleid];
		if (confirm("Are you sure you want to delete this query?")) {
			SampleManager.deleteCharacterizationQuery(query, function(
					searchBean) {
				if (searchBean != null) {
					currentSearchBean = searchBean;
					populateCharacterizationQueries();
				} else {
					sessionTimeout();
				}
			});
		}
	}
}

function showDetailView(styleId, url) {
	url=url+"&advancedSearch="+styleId;
	SampleManager.getAdvancedSearchDetailContent(url, function(pageData) {
		var contentElement = document.getElementById("content" + styleId);
		if (pageData == "") {
			hide("detailView" + styleId);
		} else {
			dwr.util.setValue("content" + styleId, pageData, {
				escapeHtml : false
			});
			hideOtherLinksAndViews(styleId);
			show("detailView" + styleId);
		}
	});
}

function hideOtherLinksAndViews(styleId) {
	// hide other detailViews
	var all = document.body.all ? document.body.all : document.body
			.getElementsByTagName('*');
	var test = "";
	for ( var i = 0; i < all.length; i++) {
		var element = all[i];
		if (element.id) {
			if (element.id.match("detailView")
					&& element.id != "detailView" + styleId) {
				hide(element.id);
			}
			// hide other detail link when IE
			if (navigator.appName.indexOf("Microsoft") > -1) {
				if (element.id.match("detailLink")
						&& element.id != "detailLink" + styleId) {
					hide(element.id);
				}
			}
		}
	}
}

function hideDetailView(styleId) {
	// show detail link when IE
	if (navigator.appName.indexOf("Microsoft") > -1) {
		showDetailLinks();
	}
	hide("detailView" + styleId);
}

function showDetailLinks() {
	var all = document.body.all ? document.body.all : document.body
			.getElementsByTagName('*');
	for ( var i = 0; i < all.length; i++) {
		var element = all[i];
		if (element.id) {
			if (element.id.match("detailLink")) {
				show(element.id);
			}
		}
	}
}