// for Composition Particle Entity:
/**********************************/
var peTableIdArray = new Array("biopolymerTable", "carbonNanotubeTable", "dendrimerTable", "emulsionTable",
		"fullereneTable", "liposomeTable", "metalParticleTable", "polymerTable", "quantumDotTable", "Table");
function displayCompositionProperty(selectEle) {
	var selectedValue = selectEle.options[selectEle.options.selectedIndex].value;
	for (var i = 0; i < peTableIdArray.length; i++) {
		var tableEle = document.getElementById(peTableIdArray[i]);
		if(tableEle == null) continue;
		if (peTableIdArray[i] == selectedValue + "Table") {
			tableEle.style.display = "block";
			
		} else {
			if (tableEle) {
				tableEle.style.display = "none";
			}
		}
	}
	var selectedPeType = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById("peFileTitle").firstChild.nodeValue = selectedPeType + " File Information";
	document.getElementById("compEleInfoTitle").firstChild.nodeValue = selectedPeType + " Composing Element Information";
	return false;
}

// get the select element that have triggered the onchange event
function getSelectElement(eventObj) {
	var selectEle = window.event ? window.event.srcElement : eventObj ? eventObj.currentTarget : null;
	while (selectEle.nodeName.toLowerCase() != "select") {
		selectEle = selectEle.parentNode;
	}
	if (selectEle.options[selectEle.options.selectedIndex].value != "other") {
		return false;
	}
	return selectEle;
}
/* 
 * for Add File link on Composition Particle Entity page
 */
function addFileClone() {
	var pattern = document.getElementById("filePatternDiv");
	var fileCount = document.getElementById("fileCount").firstChild.nodeValue;
	fileCount++;
	document.getElementById("fileCount").firstChild.nodeValue = fileCount;
	var clone = pattern.cloneNode(true);
	clone.setAttribute("id", "filePattern" + fileCount);
	clone.style.display = "block";
	
	//set File Name
	var fileNameEle = clone.getElementsByTagName("span")[0];
	fileNameEle.removeAttribute("id");
	fileNameEle.firstChild.nodeValue = "File #" + fileCount;
	
	//set File Type
	var fileTypeEle = clone.getElementsByTagName("select")[0];
	fileTypeEle.setAttribute("name", "fileType" + fileCount);
	fileTypeEle.setAttribute("id", "fileType" + fileCount);
	var linkEle = clone.getElementsByTagName("a")[0];
	var fileTd = document.getElementById("fileTd");
	fileTd.appendChild(clone);
	addEvent(linkEle, "click", removeFileTable, false);
	addEvent(fileTypeEle, "change", addNewFileTypeOption, false);
}
function addNewFileTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "File Type";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}
function removeFileTable(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	while (el.nodeName.toLowerCase() != "div") {
		el = el.parentNode;
	}
	var parentEle = el.parentNode;
	parentEle.removeChild(el);
	var fileCount = document.getElementById("fileCount").firstChild.nodeValue;
	fileCount--;
	document.getElementById("fileCount").firstChild.nodeValue = fileCount;
}
/* 
 * for Add Composing Element link on Composition Particle Entity page
 */
function addComposingElement() {
	var compEleCount = document.getElementById("compEleCount").firstChild.nodeValue;
	compEleCount++;
	document.getElementById("compEleCount").firstChild.nodeValue = compEleCount;
	var pattern = document.getElementById("compEle1");
	if(compEleCount == 1) {
		pattern.style.display = "block";
		return false;
	}
	
	var clone = pattern.cloneNode(true);
	clone.style.display = "block";
	clone.removeAttribute("id");
	
	/*
	 * add remove composing element event listener for non-IE browser.
	 * For IE, the event listener has been copied to the node with cloning the nodes.
	*/
	var removeLinkEle = clone.getElementsByTagName("a")[0];
	if (typeof removeLinkEle.attachEvent == "undefined") {
    	addEvent(removeLinkEle, "click", removeComposingElement, false);
	}
	
	//composing element title
	var composingElementTitleEle = clone.getElementsByTagName("span")[0];
	composingElementTitleEle.firstChild.nodeValue = "Composing Element #" + compEleCount;
	
	//composing element type
	var ceTypeEle = clone.getElementsByTagName("select")[0];
	ceTypeEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].type");
	addEvent(ceTypeEle, "change", addComposingElementTypeOption, false);
	
	//chemical name
	var cnEle = clone.getElementsByTagName("input")[0];
	cnEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].chemicalName");
	
	//Molecular Formula Type
	var mfTypeEle = clone.getElementsByTagName("select")[1];
	mfTypeEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].molecularFormulaType" );
	addEvent(mfTypeEle, "change", addMolecularFormulaTypeOption, false);
	
	//Molecular Formula
	var mfEle = clone.getElementsByTagName("input")[1];
	mfEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].molecularFormula" );
	
	//Value
	var vEle = clone.getElementsByTagName("input")[2];
	vEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].value");
	
	//Unit
	var unitEle = clone.getElementsByTagName("select")[2];
	unitEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].unit" );
	addEvent(unitEle, "change", addUnitOption, false);
	
	//Description
	var descEle = clone.getElementsByTagName("textarea")[0];
	descEle.setAttribute("name", "entity.composingElements[" + compEleCount + "].description");
	
	//inherent function
	var addInherentFuncLinkEle = clone.getElementsByTagName("a")[1];
	addInherentFuncLinkEle.removeAttribute("id");
	// save composing element count as this function index
	addInherentFuncLinkEle.getElementsByTagName("span")[0].firstChild.nodeValue = compEleCount;
	
	//Add this event listener for non-IE browser,
	//For IE, the event listener has been copied to the node with cloning the nodes.
	if (typeof addInherentFuncLinkEle.attachEvent == "undefined") {
		addEvent(addInherentFuncLinkEle, "click", addInherentFunction, false);
	}
	
	// remove non-pattern rows in the inherent function tbody
	var inherentFuncTbodyEle = clone.getElementsByTagName("tbody")[1];
	var inherentRows = inherentFuncTbodyEle.getElementsByTagName("tr");
	for(var i=inherentRows.length-1; i>=2; i--) {
		inherentFuncTbodyEle.removeChild(inherentRows[i]);
	}
	var divEle = inherentFuncTbodyEle;
	while (divEle.nodeName.toLowerCase() != "div") {
		divEle = divEle.parentNode;
	}
	divEle.style.display = "none";
	
	var ceTd = document.getElementById("compEleTd");
	ceTd.appendChild(clone);
	return false;
}

function addComposingElementTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "Composing Element Type";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}

function addMolecularFormulaTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "Molecular Formula Type";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}

function addUnitOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "Unit";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}

function removeComposingElement(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	if(el == null) return false;

	var compEleCount = document.getElementById("compEleCount").firstChild.nodeValue;
		alert("ce count:" + compEleCount);
	while (el.nodeName.toLowerCase() != "div") {
		el = el.parentNode;
	}
	
	if(el.getAttribute("id") == "compEle1") {
		el.style.display = "none";
		
		// remove non-pattern rows in the target tbody
		var targetTbodyEle = el.getElementsByTagName("tbody")[0];
		var inherentRows = targetTbodyEle.getElementsByTagName("tr");
		for(var i=inherentRows.length-1; i>=2; i--) {
			targetTbodyEle.removeChild(inherentRows[i]);
		}
	} else {
		var parentEle = el.parentNode;
		parentEle.removeChild(el);
	}
	
	document.getElementById("compEleCount").firstChild.nodeValue = --compEleCount;
		alert("last ce count:" + compEleCount);
	return false;
}

/* 
 * for Add Inherent Function link on Composition Particle Entity page
 */
function addInherentFunction(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	if(el == null) return false;

	var compEleCount = el.getElementsByTagName("span")[0].firstChild.nodeValue;
	//find parent tr element
	while(el.nodeName.toLowerCase() != "tr") {
		el = el.parentNode;
	}
	var inherentFuncCountEle = el.getElementsByTagName("span")[2];
	var inherentFuncCount = inherentFuncCountEle.firstChild.nodeValue;
	
	//var pattern = document.getElementById("inherentFuncPatternTr" + compEleCount);
	var pattern = el.getElementsByTagName("tr")[1];
	var clone = pattern.cloneNode(true);
	clone.removeAttribute("id");
	clone.removeAttribute("style");
		
	//set function type
	var funcTypeEle = clone.getElementsByTagName("select")[0];
	funcTypeEle.setAttribute("name", "funcType" + compEleCount + inherentFuncCount);
	funcTypeEle.setAttribute("id", "funcType" + compEleCount + inherentFuncCount);
	//funcTypeEle.setAttribute("name", "composition.composingElements[" + inherentFuncCount + "].chemicalName");

	//set function description
	var funcDescEle = clone.getElementsByTagName("textarea")[0];
	funcDescEle.setAttribute("name", "composition.composingElements[" + inherentFuncCount + "].chemicalName");
	var removeFuncLink = clone.getElementsByTagName("a")[0];
	addEvent(removeFuncLink, "click", removeInherentFunction, false);
	
	//var funcTbody = document.getElementById("inherentfuncTbody" + compEleCount);
	//funcTbody.appendChild(clone);
	el.getElementsByTagName("tbody")[0].appendChild(clone);
	addEvent(funcTypeEle, "change", addInherentFunctionTypeOption, false);
	
	inherentFuncCountEle.firstChild.nodeValue = ++inherentFuncCount;
	el.getElementsByTagName("div")[0].style.display = "block";
	
	return false;
}
function addInherentFunctionTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var optionName = "Inherent Function Type";
		var selectId = selectEle.getAttribute("id");
		prompt2("New " + optionName + ":", "myfunction", selectId);
	}
}
function removeInherentFunction(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	if(el == null) return false;
	
	while (el.nodeName.toLowerCase() != "tr") {
		el = el.parentNode;
	}
	var ifTr = el;
	var tbodyEle = el.parentNode;
	while (el.nodeName.toLowerCase() != "div") {
		el = el.parentNode;
	}
	var tdParent = el.parentNode;
	var ifCountEle = tdParent.getElementsByTagName("span")[0];
	var inherentFuncCount = ifCountEle.firstChild.nodeValue;
	if (inherentFuncCount <= 1) {
		el.style.display = "none";
	}
	tbodyEle.removeChild(ifTr);
	ifCountEle.firstChild.nodeValue = --inherentFuncCount;
	return false;
}

function getComposingElementOptions(particleEntityTypeId,
									compEleTypeId) {
	var compFuncTypeValue = dwr.util.getValue(particleEntityTypeId);
	
	CompositionEntityManager.getComposingElementTypeOptions(compFuncTypeValue, function(data) {
			
			dwr.util.removeAllOptions(compEleTypeId);
			dwr.util.addOptions(compEleTypeId, ['']);
    		dwr.util.addOptions(compEleTypeId, data);
    		dwr.util.addOptions(compEleTypeId, ['[Other]']);
  	});
}