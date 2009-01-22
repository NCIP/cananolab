function init() {
	fillTable();
}

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
	var id = "newInstrumentType";
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
		dwr.util.setValue("techniqueType", experimentConfig.technique.type);
		dwr.util.setValue("techniqueAbbr",
				experimentConfig.technique.abbreviation);
		dwr.util.setValue("configDescription", experimentConfig.description);
		dwr.util.setValue("configId", experimentConfig.id);
		instrumentCount = experimentConfig.instrumentCollection.length;
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
	dwr.util.setValue("configId",  "0");
	dwr.util.removeAllRows("instrumentRows", {
		filter : function(tr) {
			return (tr.id != "pattern" && tr.id != "patternHeader");
		}
	});
}

var rowCount = 0;

function addInitRow(tableId) {
	rowCount = document.getElementById(tableId).rows.length;
	dwr.util.addRows(tableId, [ rowCount ], cellFuncs, {
		escapeHtml :false
	});
}

function addInstrument() {
	var instrument = {
		newInstrumentId :null,
		newInstrumentManufacturer :null,
		newInstrumentModelName :null,
		newInstrumentType :null
	};
	instrument.id = dwr.util.getValue(newInstrumentId);
	instrument.manufacturer = dwr.util.getValue(newInstrumentManufacturer);
	instrument.modelName = dwr.util.getValue(newInstrumentModelName);
	instrument.type = dwr.util.getValue(newInstrumentType);
	ExperimentConfigManager.addInstrument(currentExperimentConfig, instrument,
			function(experimentConfig) {
				currentExperimentConfig = experimentConfig;
			});
	window.setTimeout("fillTable()", 1000);
}

function clearInstrument() {
	viewed = -1;
	document.getElementById("newInstrumentId").value = "";
	document.getElementById("newInstrumentManufacturer").value = "";
	document.getElementById("newInstrumentModelName").value = "";
	document.getElementById("newInstrumentType").value = "";
}

function fillTable() {
	var instruments = currentExperimentConfig.instrumentCollection;
	dwr.util.removeAllRows("instrumentRows", {
		filter : function(tr) {
			return (tr.id != "pattern" && tr.id != "patternHeader");
		}
	});
	var instrument, id;
	for ( var i = 0; i < instruments.length; i++) {
		instrument = instruments[i];
		if (instrument.id == null) {
			instrument.id = i + 1;
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
		 $("pattern"+id).style.display = "table-row";
		instrumentCache[id] = instrument;
	}
	clearInstrument();
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
