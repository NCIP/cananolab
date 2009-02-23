
function getUnit(fileInd, datumInd) {
	var datumName = document.getElementById("datumName" + fileInd + "-" + datumInd).value;
	CharacterizationManager.getDerivedDatumValueUnits(datumName, function (data) {
		dwr.util.removeAllOptions("unit" + fileInd + "-" + datumInd);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, [""]);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, data);
		dwr.util.addOptions("unit" + fileInd + "-" + datumInd, ["[Other]"]);
	});
}
function setCharacterizationOptionsByCharType() {
	var charType = document.getElementById("charType").value;
	CharacterizationManager.getCharacterizationOptions(charType, function (data) {
		dwr.util.removeAllOptions("charName");
		dwr.util.addOptions("charName", [""]);
		dwr.util.addOptions("charName", data);
		dwr.util.addOptions("charName", ["[Other]"]);
	});
}

function setCharacterizationOptions(charType, elementName) {
	CharacterizationManager.getCharacterizationOptions(charType, function (data) {
		dwr.util.removeAllOptions(elementName);
		dwr.util.addOptions(elementName, data);
	});
}

function setAssayEndpointOptionsByCharName() {
	var charName = document.getElementById("charName").value;
	CharacterizationManager.getAssayEndpointOptions(charName, function (data) {
		dwr.util.removeAllOptions("assayType");
		dwr.util.addOptions("assayType", [""]);
		dwr.util.addOptions("assayType", data);
		dwr.util.addOptions("assayType", ["[Other]"]);
	});
}
