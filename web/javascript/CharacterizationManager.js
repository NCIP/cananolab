function retrieveInstrumentAbbreviation() {
	var instrumentType = document.getElementById("instrumentType").value;
	CharacterizationManager.getInstrumentAbbreviation(instrumentType, writeText);
}
function writeText(textValue) {
	if (textValue != null) {
		document.getElementById("instrumentAbbr").innerHTML = "<b>Abbreviation</b>&nbsp;"+textValue;
	} else {
		document.getElementById("instrumentAbbr").innerHTML = "";
	}
}

function getUnit(fileInd, datumInd) {
	var datumName=document.getElementById("datumName"+fileInd+"-"+datumInd).value;
	CharacterizationManager.getDerivedDatumValueUnits(datumName,function(data) {			
			dwr.util.removeAllOptions("unit"+fileInd+"-"+datumInd);
			dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['']);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, data);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['[Other]']);
  	});
}

