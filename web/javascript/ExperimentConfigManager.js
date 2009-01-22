function init() {
  fillTable();
}

var currentExperimentConfig = null;

var instrumentCache = { };
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
	ExperimentConfigManager.resetExperimentConfig(function(experimentConfig) {
				currentExperimentConfig = experimentConfig;
			});	
	
}

var cellFuncs = [
		function(data) {
			return "<select name='achar.theExperimentConfig.instruments["
					+ data + "].manufacturer' " + " id='instrumentManufacturer"
					+ data + "'>"
					+ " <option value='Malvern'>Malvern</option></select>"
		},
		function(data) {
			return "<input size='17' name='achar.theExperimentConfig.instruments["
					+ data
					+ "].modelName' "
					+ " id='instrumentModelName"
					+ data + "'" + " type='text'>";
		},
		function(data) {
			return "<select name='achar.theExperimentConfig.instruments["
					+ data
					+ "].type' "
					+ " id='instrumentType"
					+ data
					+ "'>"
					+ "<option value='Dynamic Light Scattering Instrument'>Dynamic Light Scattering Instrument</option></select>"
		},
		function(data) {
			return "<input class=\"invisibleButton\" type='button' value='Remove"
					+ data + "' onClick='deleteRow(this)'>&nbsp;";
		} ];

var rowCount = 0;

function addInitRow(tableId) {
	rowCount = document.getElementById(tableId).rows.length;
	dwr.util.addRows(tableId, [ rowCount ], cellFuncs, {
		escapeHtml :false
	});
}

function addAInstrument() {;
//alert("####### settimeout11 addAInstrument ");
//alert("manufacturer =  "+dwr.util.getValue("manufacturer"));
	var instrument = {id:null, manufacturer:null, modelName:null, type:null};
	dwr.util.getValues(instrument);
	 //alert("newInstrument id="+ instrument.id+" Manufacturer="+ instrument.manufacturer+" type="+ instrument.type);
	//instrument.type = "ABCType";
	//instrument.modelName = "ABCmodelName";
	ExperimentConfigManager.addInstrument(currentExperimentConfig,
			instrument, function(experimentConfig) {
				alert("#######11111111111 updateCurrentExperimentConfig ");
				currentExperimentConfig = experimentConfig;
				var instruments = currentExperimentConfig.instrumentCollection;
			   // alert('updateCurrentExperimentConfig instruments.length= '+instruments.length);
			    var instrument = instruments[0];
			    alert(" id="+ instrument.id+" Manufacturer="+ instrument.manufacturer+" type="+ instrument.type);
			});
	//fillTable();
	window.setTimeout("fillTable()", 2000);
	//clearInstrument();
}


function clearInstrument() {
	viewed = -1;
	dwr.util.setValues({instrumentId:null, instrumentManufacturer:null, instrumentModel:null, instrumentType:null});
}


function fillTable() {
	alert("xxxfillTable");
		var instruments = currentExperimentConfig.instrumentCollection;
    	$("pattern").style.display = "block";
    	dwr.util.removeAllRows("instrumentRows", { filter:function(tr) {
    	      return (tr.id != "pattern");
    	}});
	    var instrument, id;
	    alert('instruments.length= '+instruments.length);
	    for (var i = 0; i < instruments.length; i++) {
	      instrument = instruments[i];
	      if (instrument.id==null){
	    	  instrument.id = i+1;
	      }
	      id = instrument.id;
	      alert('i = '+i + " id="+ instrument.id+" type="+ instrument.type+" manufacturer="+ instrument.manufacturer);
	     // alert(i+'  instrument.id '+id+ ' '+instrument.manufacturer);
	      dwr.util.cloneNode("pattern", { idSuffix:id});
	      dwr.util.setValue("instrumentId"+id, instrument.id);
	      dwr.util.setValue("instrumentManufacturer"+id, instrument.manufacturer);
	      dwr.util.setValue("instrumentModelName"+id, instrument.modelName);
	      dwr.util.setValue("instrumentType"+id, instrument.type);
	     // $("pattern"+id).style.display = "table-row";
	      alert("instrumentType"+id+" == "+dwr.util.getValue("instrumentType"+id));
	      instrumentCache[id] = instrument;
       }
	    $("pattern").style.display = "none";

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
