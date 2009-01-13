function getUnit(fileInd, datumInd) {
	var datumName=document.getElementById("datumName"+fileInd+"-"+datumInd).value;
	CharacterizationManager.getDerivedDatumValueUnits(datumName,function(data) {
			dwr.util.removeAllOptions("unit"+fileInd+"-"+datumInd);
			dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['']);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, data);
    		dwr.util.addOptions("unit"+fileInd+"-"+datumInd, ['[Other]']);
  	});
}

