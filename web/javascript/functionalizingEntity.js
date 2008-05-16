var feTableIdArray = new Array("biopolymerTable", "antibodyTable", "smallMoleculeTable", "Table");
function displayFunctionalizingEntity(selectEle) {
	var selectedValue = selectEle.options[selectEle.options.selectedIndex].value;
	for (var i = 0; i < feTableIdArray.length; i++) {
		var tableEle = document.getElementById(feTableIdArray[i]);
		if(tableEle == null) continue;
		if (feTableIdArray[i] == selectedValue + "Table") {
			tableEle.style.display = "block";
			
		} else {
			if (tableEle) {
				tableEle.style.display = "none";
			}
		}
	}
	var selectedPeType = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById("feFileTitle").firstChild.nodeValue = selectedPeType + " File Information";
	document.getElementById("funcInfoTitle").firstChild.nodeValue = selectedPeType + " Function Information";
}

/* 
 * for Add Composing Element link on Composition Particle Entity page
 */
function addFunction() {
	var functionCount = document.getElementById("functionCount").firstChild.nodeValue;
	functionCount++;
	document.getElementById("functionCount").firstChild.nodeValue = functionCount;
	var pattern = document.getElementById("func1");
	if(functionCount == 1) {
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
    	addEvent(removeLinkEle, "click", removeFunction, false);
	}
	
	//function title
	var functionTitleEle = clone.getElementsByTagName("span")[0];
	functionTitleEle.firstChild.nodeValue = "Function #" + functionCount;
	
	//function type
	var ceTypeEle = clone.getElementsByTagName("select")[0];
	ceTypeEle.setAttribute("name", "functionType" + functionCount);
	addEvent(ceTypeEle, "change", addFunctionTypeOption, false);
	
	//Description
	var descEle = clone.getElementsByTagName("textarea")[0];
	descEle.setAttribute("name", "description" + functionCount);
	
	//target
	var addTargetEle = clone.getElementsByTagName("a")[1];
	addTargetEle.removeAttribute("id");
	// save function count as this function index
	addTargetEle.getElementsByTagName("span")[0].firstChild.nodeValue = functionCount;
	
	//Add this event listener for non-IE browser,
	//For IE, the event listener has been copied to the node with cloning the nodes.
	if (typeof addTargetEle.attachEvent == "undefined") {
		addEvent(addTargetEle, "click", addTarget, false);
	}
	
	// remove non-pattern rows in the target tbody
	var targetTbodyEle = clone.getElementsByTagName("tbody")[1];
	var inherentRows = targetTbodyEle.getElementsByTagName("tr");
	for(var i=inherentRows.length-1; i>=2; i--) {
		targetTbodyEle.removeChild(inherentRows[i]);
	}
	var divEle = targetTbodyEle;
	while (divEle.nodeName.toLowerCase() != "div") {
		divEle = divEle.parentNode;
	}
	divEle.style.display = "none";
	
	var ceTd = document.getElementById("functionTd");
	ceTd.appendChild(clone);
	return false;
}

function addFunctionTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var selectId = selectEle.getAttribute("id");
		prompt2("New Function Type:", "myfunction", selectId);
	}
}

function removeFunction(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	if(el == null) return false;

	var functionCount = document.getElementById("functionCount").firstChild.nodeValue;
	while (el.nodeName.toLowerCase() != "div") {
		el = el.parentNode;
	}
	
	if(el.getAttribute("id") == "func1") {
		el.style.display = "none";
		
		// remove non-pattern rows in the target tbody
		var targetTbodyEle = el.getElementsByTagName("tbody")[1];
		var inherentRows = targetTbodyEle.getElementsByTagName("tr");
		for(var i=inherentRows.length-1; i>=2; i--) {
			targetTbodyEle.removeChild(inherentRows[i]);
		}
	} else {
		var parentEle = el.parentNode;
		parentEle.removeChild(el);
	}
	
	document.getElementById("functionCount").firstChild.nodeValue = --functionCount;
	return false;
}

/* 
 * for Add Target link on Functionalizing Entity page
 */
function addTarget(e) {
	var el = window.event ? window.event.srcElement : e ? e.currentTarget : null;
	if(el == null) return false;

	var functionCount = el.getElementsByTagName("span")[0].firstChild.nodeValue;
	
	//find parent tr element
	while(el.nodeName.toLowerCase() != "tr") {
		el = el.parentNode;
	}
	var targetCountEle = el.getElementsByTagName("span")[2];
	var targetCount = targetCountEle.firstChild.nodeValue;
	
	var pattern = el.getElementsByTagName("tr")[1];
	var clone = pattern.cloneNode(true);
	clone.removeAttribute("style");
		
	//set target type
	var targetTypeEle = clone.getElementsByTagName("select")[0];
	targetTypeEle.setAttribute("name", "targetType" + functionCount + targetCount);
	targetTypeEle.setAttribute("id", "targetType" + functionCount + targetCount);
	addEvent(targetTypeEle, "change", addTargetTypeOption, false);
	//targetTypeEle.setAttribute("name", "composition.composingElements[" + targetCount + "].chemicalName");

	//set target name
	var targetNameEle = clone.getElementsByTagName("input")[0];
	targetNameEle.setAttribute("name", "targetName" + functionCount + targetCount);
	
	//set species
	var speciesEle = clone.getElementsByTagName("select")[1];
	speciesEle.setAttribute("name", "species" + functionCount + targetCount);
	speciesEle.setAttribute("id", "species" + functionCount + targetCount);
	addEvent(speciesEle, "change", addSpeciesOption, false);
	
	//set target description
	var targetDescEle = clone.getElementsByTagName("textarea")[0];
	targetDescEle.setAttribute("name", "composition.composingElements[" + targetCount + "].chemicalName");
	
	var removeTargetLink = clone.getElementsByTagName("a")[0];
	addEvent(removeTargetLink, "click", removeTarget, false);
	
	el.getElementsByTagName("tbody")[0].appendChild(clone);
	
	targetCountEle.firstChild.nodeValue = ++targetCount;
	el.getElementsByTagName("div")[0].style.display = "block";
	
	return false;
}
function addTargetTypeOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var selectId = selectEle.getAttribute("id");
		prompt2("New Target Type:", "myfunction", selectId);
	}
}
function addSpeciesOption(e) {
	var selectEle = getSelectElement(e);
	if (selectEle) {
		var selectId = selectEle.getAttribute("id");
		prompt2("New Species:", "myfunction", selectId);
	}
}

function removeTarget(e) {
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
	var targetCount = ifCountEle.firstChild.nodeValue;
	if (targetCount <= 1) {
		el.style.display = "none";
	}
	tbodyEle.removeChild(ifTr);
	ifCountEle.firstChild.nodeValue = --targetCount;
	return false;
}

