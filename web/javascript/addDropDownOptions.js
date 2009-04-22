function validateOptions(newOption, optionsArray) {
	for (var i = 0; i < optionsArray.length; i++) {
		if(optionsArray[i].text == "enterNew")
			optionsArray[i] = null;

		if (optionsArray[i].text.toUpperCase() == newOption.toUpperCase()) {
			return false;
		}
	}
	return true;
}

function getValidatedProtocolVersion(newOption) {
	if (parseInt(newOption) == newOption && newOption.indexOf(".0")<0) {
		newOption = newOption + ".0";
	}
	return newOption;
}

function addOption(selectId, promptParentId) {
	var opt = document.getElementById("promptbox").value;
	if (opt != null && opt != "") {
		var selectEle = document.getElementById(selectId);
		if(opt == "other") return false;
		if (selectId=='protocolFileId'){
			opt = getValidatedProtocolVersion(opt);
		}
		if (!validateOptions(opt, selectEle.options)) {
			selectEle.options.selectedIndex = 0;
			alert(opt + " is already on the list!");
			return false;
		}
		var optObj = new Option(opt, opt);
		selectEle.options[selectEle.options.length] = optObj;
		for (var i = selectEle.options.length - 1; i > 0; i--) {
			selectEle.options[i].text = selectEle.options[i - 1].text;
			selectEle.options[i].value = selectEle.options[i - 1].value;
		}
		selectEle.options[0].text = "[" + opt + "]";
		selectEle.options[0].value = opt;
		selectEle.options.selectedIndex = 0;
	}
	//document.getElementsByTagName("body")[0].removeChild(document.getElementById("prompt"));
	document.getElementById(promptParentId).removeChild(document.getElementById("prompt"));
	return false;
}

function changeOption(selectId, promptParentId) {
	var opt = document.getElementById("promptbox").value;
	if (opt != null && opt != "") {
		var selectEle = document.getElementById(selectId);
		if(opt == "other") return false;
		if (selectId=='protocolFileId'){
			opt = getValidatedProtocolVersion(opt);
		}
		if (!validateOptions(opt, selectEle.options)) {
			selectEle.options.selectedIndex = 0;
			alert(opt + " is already on the list!");
			return false;
		}

		selectEle.options[selectEle.options.selectedIndex].text = "[" + opt + "]";
		selectEle.options[selectEle.options.selectedIndex].value = opt;
	}
	//document.getElementsByTagName("body")[0].removeChild(document.getElementById("prompt"));
	document.getElementById(promptParentId).removeChild(document.getElementById("prompt"));
	return false;
}

function removeOption(selectId, promptParentId) {
	var selectEle = document.getElementById(selectId);
	selectEle.options[selectEle.options.selectedIndex] = null;

	//document.getElementsByTagName("body")[0].removeChild(document.getElementById("prompt"));
	document.getElementById(promptParentId).removeChild(document.getElementById("prompt"));
	return false;
}

function addNewOption(message, parentId, promptParentId) {
	promptbox = document.createElement("div");
	promptbox.setAttribute("id", "prompt");
	//document.getElementsByTagName("body")[0].appendChild(promptbox);
	document.getElementById(promptParentId).appendChild(promptbox);

	document.getElementById("prompt").innerHTML = "<table cellspacing='5' cellpadding='0' border='0' width='100%' class='promptbox'>" +
		"<tr><td class='cellLabel'>" + message + "</td></tr>" +
		"<tr><td><input type='text' id='promptbox' onblur='this.focus()' class='promptbox'></td></tr>" +
		"<tr><td style='text-align: right'>" +
		"<input type='button' class='promptButton' value='Add' onMouseOver='mouseOverStyle();' onMouseOut='mouseOutStyle();' onClick='addOption(\"" + parentId + "\",\""+promptParentId+"\");' >" +
		"<input type='button' class='promptButton' value='Cancel' onMouseOver='mouseOverStyle();' onMouseOut='mouseOutStyle();' onClick='cancelAddOption(\""+promptParentId+"\");'>" +
		"</td></tr></table>";
	document.getElementById("promptbox").focus();
}

function modifyOption(message, parentId, selectedText, promptParentId) {
	promptbox = document.createElement("div");
	promptbox.setAttribute("id", "prompt");
	//document.getElementsByTagName("body")[0].appendChild(promptbox);
    document.getElementById(promptParentId).appendChild(promptbox);
	document.getElementById("prompt").innerHTML = "<table cellspacing='5' cellpadding='0' border='0' width='100%' class='promptbox'>" +
		"<tr><td class='cellLabel'>" + message + "</td></tr>" +
		"<tr><td><input type='text' id='promptbox' onblur='this.focus()' class='promptbox' value='" + selectedText + "'></td></tr>" +
		"<tr><td style='text-align: right'>" +
		"<input type='button' class='promptButton' value='Save' onMouseOver='mouseOverStyle();' onMouseOut='mouseOutStyle();' onClick='changeOption(\"" + parentId + "\",\""+promptParentId+"\");' >" +
		"<input type='button' class='promptButton' value='Remove' onMouseOver='mouseOverStyle();' onMouseOut='mouseOutStyle();' onClick='removeOption(\"" + parentId + "\",\""+promptParentId+"\");' >" +
		"<input type='button' class='promptButton' value='Cancel' onMouseOver='mouseOverStyle();' onMouseOut='mouseOutStyle();' onClick='cancelAddOption(\""+promptParentId+"\");'>" +
		"</td></tr></table>";
	document.getElementById("promptbox").focus();
}

function cancelAddOption(promptParentId) {
	//document.getElementsByTagName("body")[0].removeChild(document.getElementById("prompt"));
	document.getElementById(promptParentId).removeChild(document.getElementById("prompt"));
	return false;
}

function resetAddOption(selectId) {
    var selectEle = document.getElementById(selectId);
    if (selectEle!=null){
		var opt = selectEle.value;
		if (opt != null && opt != "") {
			if (selectEle.options[0]!= null && selectEle.options[0] != "" && selectEle.options.selectedIndex==0) {
				if (selectEle.options[selectEle.options.selectedIndex].text.charAt(0)=='['){
					selectEle.options[selectEle.options.selectedIndex] = null;
				}
			}
		}
	}
	return false;
}


function mouseOverStyle(event) {
	var targetObj = getTargetElement(event);
	if (targetObj == null) {
		return false;
	}
	//var inputObj = ascendDOM(targetObj, 'input');
	targetObj.style.border = "1 outset #dddddd";
}
function getTargetElement(e) {
	var el;
	if (window.event && window.event.srcElement) {
		el = window.event.srcElement;
	}
	if (e && e.target) {
		el = e.target;
	}
	if (!el) {
		return null;
	}
	return el;
}
function mouseOutStyle(event) {
	var targetObj = getTargetElement(event);
	if (targetObj == null) {
		return false;
	}
	targetObj.style.border = "1 solid transparent";
}

/*
function callPrompt(optionName, selectId) {
	var selectEle = document.getElementById(selectId);
	var ovalue = selectEle.options[selectEle.options.selectedIndex].value;
	if(ovalue == "other")
		addNewOption("New " + optionName + ":", selectId);
	else if(ovalue.charAt(0) == "[" &&
			ovalue.charAt(ovalue.length - 1) == "]") {
		var otext = selectEle.options[selectEle.options.selectedIndex].text;
		modifyOption("Edit " + optionName + " option:", selectId, otext);
	}
	return false;
}
*/

function callPrompt(optionName, selectId, promptParentId) {
	var selectEle = document.getElementById(selectId);
	var otext = selectEle.options[selectEle.options.selectedIndex].text;
	if(otext == "[Other]")
		addNewOption("New " + optionName + ":", selectId, promptParentId);
	else if(otext.charAt(0) == "[" &&
			otext.charAt(otext.length - 1) == "]") {
		var text = removeBracket(otext);
		modifyOption("Edit " + optionName + " Option:", selectId, text, promptParentId);
	}
	return false;
}

function removeBracket(bracketStr) {
	return bracketStr.substring(1, bracketStr.length - 1);
}