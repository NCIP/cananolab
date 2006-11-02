
function changeMenuStyle(obj, new_style) {
    obj.className = new_style;
}
function showCursor() {
    document.body.style.cursor = "hand";
}
function hideCursor() {
    document.body.style.cursor = "default";
}
function confirmDelete() {
    if (confirm("Are you sure you want to delete?")) {
        return true;
    } else {
        return false;
    }
}
function get_tree() {
    d = new dTree("d");
    d.config.target = "";
    d.add(0, -1, "Workflow");
    d.add(1, 0, "Pre-screening Assays", "javascript:void(0)", "", "", "");
    d.add(2, 1, "STE-1", "javascript:gotoPage('assayActionMenu.jsp')", "", "", "");
    d.add(3, 2, "run 1", "javascript:void(0)");
    d.add(4, 3, "In", "javascript:gotoPage('inputActionMenu.jsp')");
    d.add(5, 4, "NCL6-7105-1", "javascript:gotoPage('viewAliquot.do?aliquotId=NCL6-7105-1')", "", "", "");
    d.add(6, 4, "NCL6-7105-2", "javascript:void(0)", "", "", "");
    d.add(7, 3, "Out", "javascript:gotoPage('outputActionMenu.jsp')");
    d.add(8, 7, "NCL6.vaf", "doc/astra_5.doc");
    d.add(9, 2, "run 2", "javascript:void(0)");
    d.add(10, 9, "In", "javascript:gotoPage('inputActionMenu.jsp')");
    d.add(11, 9, "Out", "javascript:gotoPage('outputActionMenu.jsp')");
    d.add(12, 1, "STE-2", "javascript:gotoPage('assayActionMenu.jsp')");
    d.add(13, 1, "STE-3", "javascript:gotoPage('assayActionMenu.jsp')", "", "", "");
    d.add(14, 1, "PCC-1", "javascript:gotoPage('assayActionMenu.jsp')", "", "", "");
    d.add(15, 0, "In Vitro Assays", "javascript:void(0)", "", "", "");
    d.add(16, 0, "In Vivo Assays", "javascript:void(0)", "", "", "");
    d.add(17, 0, "Physical Characterization Assays", "javascript:void(0)", "", "", "");
    document.write(d);
}
function add_hist() {
    document.getElementById("hists").options[document.getElementById("hists").options.length] = new Option("new text", "new value");
}
function gotoPage(pageURL) {
    window.location.href = pageURL;
}
function openWindow(pageURL, name, width, height) {
    window.open(pageURL, name, "alwaysRaised,dependent,status,scrollbars,resizable,width=" + width + ",height=" + height);
}
function openHelpWindow(pageURL) {
    window.open(pageURL, "Help", "alwaysRaised,dependent,status,scrollbars,resizable,width=800,height=500");
}
/**
moveItems is a function used in moving items in one multiple select to the other.
*/
function moveItems(fbox, tbox) {
    var arrFbox = new Array();
    var arrTbox = new Array();
    var arrLookup = new Array();
    var i;
    for (i = 0; i < tbox.options.length; i++) {
        arrLookup[tbox.options[i].text] = tbox.options[i].value;
        arrTbox[i] = tbox.options[i].text;
    }
    var fLength = 0;
    var tLength = arrTbox.length;
    for (i = 0; i < fbox.options.length; i++) {
        arrLookup[fbox.options[i].text] = fbox.options[i].value;
        if (fbox.options[i].selected && fbox.options[i].value != "") {
            arrTbox[tLength] = fbox.options[i].text;
            tLength++;
        } else {
            arrFbox[fLength] = fbox.options[i].text;
            fLength++;
        }
    }
			//arrFbox.sort();
			//arrTbox.sort();
    fbox.length = 0;
    tbox.length = 0;
    var c;
    for (c = 0; c < arrFbox.length; c++) {
        var no = new Option();
        no.value = arrLookup[arrFbox[c]];
        no.text = arrFbox[c];
        fbox[c] = no;
    }
    for (c = 0; c < arrTbox.length; c++) {
        var no = new Option();
        no.value = arrLookup[arrTbox[c]];
        no.text = arrTbox[c];
        tbox[c] = no;
    }
}
function resetSelect(selectObj) {
    for (var i = 0; i < selectObj.options.length; i++) {
        if (selectObj.options[i].selected) {
            selectObj.options[i].selected = false;
        }
    }
}
function removeSelectOptions(selectObj) {
    selectObj.options.length = 0;
}
/* filter second drop-down by first drop-down selection*/
function doubleDropdown(selection1, selection2, value1ToValue2) {
    /* initialize selection2 options */
    selection2.options.length = 0;
    selection2.options[0] = new Option("", "");
    var value1 = selection1.options[selection1.selectedIndex].value;
    var value2Arr = value1ToValue2[value1];
    if (value2Arr != null) {
        for (i = 0; i < value2Arr.length; i++) {
            selection2.options[i] = value2Arr[i];
        }
    }
}
/* filter a drop-down by a value*/
function dynamicDropdown(value, selection, value1ToValue2) {
    /* initialize selection options */
    selection.options.length = 0;
    selection.options[0] = new Option("", "");
    var value2Arr = value1ToValue2[value];
    if (value2Arr != null) {
        for (i = 0; i < value2Arr.length; i++) {
            selection.options[i] = value2Arr[i];
        }
    }
}
/* filter second multi-box by first multi-box selections */
function doubleMultibox(selection1, selection2, value1ToValue2) {
    selection2.options.length = 0;
    selection2.options[0] = new Option("", "");
    var value1Arr = new Array();
    for (var i = 0; i < selection1.options.length; i++) {
        if (selection1.options[i].selected) {
            value1Arr.push(selection1[i].value);
        }
    }
    var value2Arr = new Array();
    for (var i = 0; i < value1Arr.length; i++) {
        var value2s = value1ToValue2[value1Arr[i]];
        for (var j = 0; j < value2s.length; j++) {
            value2Arr.push(value2s[j]);
        }
    }
    for (var i = 0; i < value2Arr.length; i++) {
        selection2.options[i] = value2Arr[i];
    }
}
function submitAction(form, actionName) {
    form.action = actionName;
    form.submit();
}
function updateComposition() {
    document.nanoparticleCompositionForm.action = "nanoparticleComposition.do?dispatch=update&page=0";
    document.nanoparticleCompositionForm.submit();
}
function updateSize() {
    document.nanoparticleSizeForm.action = "nanoparticleSize.do?dispatch=update&page=0";
    document.nanoparticleSizeForm.submit();
}
function updateSurface() {
    document.nanoparticleSurfaceForm.action = "nanoparticleSurface.do?dispatch=update&page=0";
    document.nanoparticleSurfaceForm.submit();
}
function updateMolecularWeight() {
    document.nanoparticleMolecularWeightForm.action = "nanoparticleMolecularWeight.do?dispatch=update&page=0";
    document.nanoparticleMolecularWeightForm.submit();
}
function updateMorphology() {
    document.nanoparticleMorphologyForm.action = "nanoparticleMorphology.do?dispatch=update&page=0";
    document.nanoparticleMorphologyForm.submit();
}
function updateShape() {
    document.nanoparticleShapeForm.action = "nanoparticleShape.do?dispatch=update&page=0";
    document.nanoparticleShapeForm.submit();
}
function updateStability() {
    document.nanoparticleStabilityForm.action = "nanoparticleStability.do?dispatch=update&page=0";
    document.nanoparticleStabilityForm.submit();
}
function updatePurity() {
    document.nanoparticlePurityForm.action = "nanoparticlePurity.do?dispatch=update&page=0";
    document.nanoparticlePurityForm.submit();
}
function updateSolubility() {
    document.nanoparticleSolubilityForm.action = "nanoparticleSolubility.do?dispatch=update&page=0";
    document.nanoparticleSolubilityForm.submit();
}
function updateHemolysis() {
    document.invitroImmunotoxicityHemolysisForm.action = "invitroImmunotoxicityHemolysis.do?dispatch=update&page=0";
    document.invitroImmunotoxicityHemolysisForm.submit();
}
function updateHemolysisControlCondition() {
    document.invitroImmunotoxicityHemolysisForm.action = "invitroImmunotoxicityHemolysis.do?dispatch=update&page=0";
    document.invitroImmunotoxicityHemolysisForm.submit();
}
function updateCoagulation() {
    document.invitroImmunotoxicityCoagulationForm.action = "invitroImmunotoxicityCoagulation.do?dispatch=update&page=0";
    document.invitroImmunotoxicityCoagulationForm.submit();
}
function updatePlateAggregation() {
    document.invitroImmunotoxicityPlateAggregationForm.action = "invitroImmunotoxicityPlateAggregation.do?dispatch=update&page=0";
    document.invitroImmunotoxicityPlateAggregationForm.submit();
}
function updateProteinBinding() {
    document.invitroImmunotoxicityProteinBindingForm.action = "invitroImmunotoxicityProteinBinding.do?dispatch=update&page=0";
    document.invitroImmunotoxicityProteinBindingForm.submit();
}
function loadSizeFile(particleName, fileNumber) {
    document.nanoparticleSizeForm.action = "nanoparticleSize.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleSizeForm.submit();
}

function loadSurfaceFile(particleName, fileNumber) {
    document.nanoparticleSurfaceForm.action = "nanoparticleSurface.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleSurfaceForm.submit();
}
function loadMolecularWeightFile(particleName, fileNumber) {
    document.nanoparticleMolecularWeightForm.action = "nanoparticleMolecularWeight.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleMolecularWeightForm.submit();
}
function loadMorphologyFile(particleName, fileNumber) {
    document.nanoparticleMorphologyForm.action = "nanoparticleMorphology.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleMorphologyForm.submit();
}
function loadShapeFile(particleName, fileNumber) {
    document.nanoparticleShapeForm.action = "nanoparticleShape.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleShapeForm.submit();
}
function loadSolubilityFile(particleName, fileNumber) {
    document.nanoparticleSolubilityForm.action = "nanoparticleSolubility.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleSolubilityForm.submit();
}
function loadPurityFile(particleName, fileNumber) {
    document.nanoparticlePurityForm.action = "nanoparticlePurity.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticlePurityForm.submit();
}
function loadStabilityFile(particleName, fileNumber) {
    document.nanoparticleStabilityForm.action = "nanoparticleStability.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleStabilityForm.submit();
}


/*function loadFile(particleName, fileNumber, charType) {
	var action;
	if (charType == "size" ) {
		action="nanoparticleSize.do";
	} else if (charType == "
	document.nanoparticleSizeForm.action = "nanoparticleSize.do?dispatch=loadFile&page=0&particleName=" + particleName + "&fileNumber=" + fileNumber;
    document.nanoparticleSizeForm.submit();
}*/

function refreshManufacturers(form, action) {
    form.dispatch.value = action;
    form.submit();
}


