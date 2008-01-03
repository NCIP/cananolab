function retrieveInstrumentAbbreviation() {
	var instrumentType = document.getElementById("instrumentType").value;
	InstrumentManager.getInstrumentAbbreviation(instrumentType, writeText);
}
function writeText(textValue) {
	if (textValue != null) {
		document.getElementById("instrumentAbbr").innerHTML = "<b>Abbreviation</b>&nbsp;"+textValue;
	} else {
		document.getElementById("instrumentAbbr").innerHTML = "";
	}
}

