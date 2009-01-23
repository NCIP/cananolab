
var currentExperimentConfig = null;

var instrumentCache = {};
var viewed = -1;
function retrieveTechniqueAbbreviation() {
	var techniqueType = document.getElementById("techniqueType").value;
	if (techniqueType != null && techniqueType != "other") {
		ExperimentConfigManager.findInstrumentTypesByTechniqueType(
				techniqueType, updateInstrumentDropDown);
		ExperimentConfigManager.findTechniqueByType(techniqueType,
				updateTechniqueAbbreviation);
	}
}
function updateTechniqueAbbreviation(technique) {
	if (technique != null) {
		dwr.util.setValue("techniqueAbbr", technique.abbreviation);
	} else {
		document.getElementById("techniqueAbbr").value = "";
	}
}

function updateInstrumentDropDown(instrumentTypes) {
	var id = "type";
	var selectedValue = dwr.util.getValue(id);
	dwr.util.removeAllOptions(id);
	dwr.util.addOptions(id, [ "" ]);
	dwr.util.addOptions(id, instrumentTypes);
	dwr.util.addOptions(id, [ "[Other]" ]);
	dwr.util.setValue(id, selectedValue);
}

function setManufacturerOptions(manufacturerTypes) {
	dwr.util.removeAllOptions("instrumentManufacturer"
			+ instrumentManufacturerIndex);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			[ "" ]);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			manufacturerTypes);
	dwr.util.addOptions("instrumentManufacturer" + instrumentManufacturerIndex,
			[ "[Other]" ]);
}

var thisConfigId = 0;
function setTheExperimentConfig(configId) {
	show('newExperimentConfig');
	ExperimentConfigManager.findInstrumentTypesByConfigId(configId,
			updateInstrumentDropDown);
	thisConfigId = configId;
	window.setTimeout("doSetTheExperimentConfig()", 300);
}

function doSetTheExperimentConfig() {
	ExperimentConfigManager.findExperimentConfigById(thisConfigId,
			populateExperimentConfig);
}

var instrumentCount = 1;

function populateExperimentConfig(experimentConfig) {
	if (experimentConfig != null) {
		currentExperimentConfig = experimentConfig;
		dwr.util.setValue("techniqueType", experimentConfig.domain.technique.type);
		dwr.util.setValue("techniqueAbbr",
				experimentConfig.domain.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.domain.description);
		dwr.util.setValue("configId", experimentConfig.domain.id);
		instrumentCount = experimentConfig.instruments.length;
		rowCount = document.getElementById("instrumentRows").rows.length;
		fillTable();
	}
}

var instrumentManufacturerIndex = 0;
function resetTheExperimentConfig(isShow) {
	if (isShow) {
		show('newExperimentConfig');
	} else {
		hide('newExperimentConfig');
	}
	ExperimentConfigManager.resetExperimentConfig( function(experimentConfig) {
		currentExperimentConfig = experimentConfig;
	});
	dwr.util.setValue("techniqueType", "");
	dwr.util.setValue("techniqueAbbr", "");
	dwr.util.setValue("configDescription", "");
	dwr.util.setValue("configId", "0");
	dwr.util
			.removeAllRows(
					"instrumentRows",
					{
						filter : function(tr) {
							return (tr.id != "pattern"
									&& tr.id != "patternHeader" && tr.id != "patternAddRow");
						}
					});
	clearInstrument();
	hide('patternAddRow');
	hide('instrumentTableDiv');
}

var rowCount = 0;

function addInstrument() {
	var instrument = {
		id :null,
		manufacturer :null,
		modelName :null,
		type :null
	};
	dwr.util.getValues(instrument);
	ExperimentConfigManager.addInstrument(currentExperimentConfig, instrument,
			function(experimentConfig) {
				currentExperimentConfig = experimentConfig;
			});
	hide('patternAddRow');
	window.setTimeout("fillTable()", 200);
}

function clearInstrument() {
	viewed = -1;
	document.getElementById("id").value = "";
	document.getElementById("manufacturer").value = "";
	document.getElementById("modelName").value = "";
	document.getElementById("type").value = "";
}

function fillTable() {
	var instruments = currentExperimentConfig.instruments;
	dwr.util
			.removeAllRows(
					"instrumentRows",
					{
						filter : function(tr) {
							return (tr.id != "pattern"
									&& tr.id != "patternHeader" && tr.id != "patternAddRow");
						}
					});
	var instrument, id;
	hide('patternAddRow');
	if (instruments.length>0){
		show('instrumentTableDiv');
	}
	for ( var i = 0; i < instruments.length; i++) {
		instrument = instruments[i];
		if (instrument.id == null) {
			instrument.id = i + 1000;
		}
		id = instrument.id;
		dwr.util.cloneNode("pattern", {
			idSuffix :id
		});
		dwr.util.setValue("instrumentId" + id, instrument.id);
		dwr.util.setValue("instrumentManufacturer" + id,
				instrument.manufacturer);
		dwr.util.setValue("instrumentModelName" + id, instrument.modelName);
		dwr.util.setValue("instrumentType" + id, instrument.type);
		$("pattern" + id).style.display = "";
		instrumentCache[id] = instrument;
	}
	clearInstrument();
}

function editClicked(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var instrument = instrumentCache[eleid.substring(4)];
	dwr.util.setValues(instrument);
	show('patternAddRow');
}

function deleteClicked(eleid) {
	// we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	var instrument = instrumentCache[eleid.substring(6)];
	if (confirm("Are you sure you want to delete '" + instrument.manufacturer
			+ " " + instrument.modelName+"'?")) {
		ExperimentConfigManager.deleteInstrument(currentExperimentConfig,
				instrument, function(experimentConfig) {
					currentExperimentConfig = experimentConfig;
				});
		window.setTimeout("fillTable()", 200);
	}
}

function deleteRow(target) {
	do {
		if (target.nodeName.toUpperCase() == 'TR') {
			target.parentNode.removeChild(target);
			break;
		}
	} while (target = target.parentNode);
}

function deleteRowByIndex(index) {
	document.getElementById("instrumentRows").deleteRow(index);
}
