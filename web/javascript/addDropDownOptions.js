
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
/*
function addOptionPrompt(optionName, tdId) {
	var opt = prompt("New " + optionName + ":", "");
	if (opt != null && opt != "") {
		var parentEle = document.getElementById(tdId);
		var selectEle = parentEle.getElementsByTagName("select");
		if (selectEle.length == 1) {
			if (!validateOptions(opt, selectEle[0].options)) {
				selectEle[0].options.selectedIndex = 0;
				alert(opt + " is already on the list!");
				return false;
			}
			var optObj = new Option(opt, opt);
			selectEle[0].options[selectEle[0].options.length] = optObj;
			for (var i = selectEle[0].options.length - 1; i > 0; i--) {
				selectEle[0].options[i].text = selectEle[0].options[i - 1].text;
				selectEle[0].options[i].value = selectEle[0].options[i - 1].value;
			}
			selectEle[0].options[0].text = opt;
			selectEle[0].options[0].value = opt;
			selectEle[0].options.selectedIndex = 0;
		}
	}
	return false;
}
*/

function addOptionPrompt(opt, selectId) {
	if (opt != null && opt != "") {
		var selectEle = document.getElementById(selectId);
		if(opt == "enterNew") return false;
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
		selectEle.options[0].text = opt;
		selectEle.options[0].value = opt;
		selectEle.options.selectedIndex = 0;
	}
	return false;
}
var response = null;

function prompt2(message, sendto, parentId) {
	promptbox = document.createElement("div");
	promptbox.setAttribute("id", "prompt");
	document.getElementsByTagName("body")[0].appendChild(promptbox);
	promptbox = eval("document.getElementById('prompt').style");
		/*
	promptbox.position = "absolute";
	promptbox.top = 150;
	promptbox.left = 200;
	promptbox.width = 300;
	promptbox.border = "outset #bbbbbb 1px";
			*/
	document.getElementById("prompt").innerHTML = "<table cellspacing='5' cellpadding='0' border='0' width='100%' class='promptbox'>" + 
		"<tr><td>" + message + "</td></tr>" + "<tr><td><input type='text' id='promptbox' onblur='this.focus()' class='promptbox'></td></tr>" + 
		"<tr><td align='right'><br><input type='button' class='prompt' value='Add' onMouseOver='this.style.border=\"1 outset #dddddd\"' onMouseOut='this.style.border=\"1 solid transparent\"' onClick='" + 
		sendto + "(document.getElementById(\"promptbox\").value, \"" + parentId + 
		"\"); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"))'> <input type='button' class='prompt' value='Cancel' onMouseOver='this.style.border=\"1 outset transparent\"' onMouseOut='this.style.border=\"1 solid transparent\"' onClick='" + 
		sendto + "(\"\"); document.getElementsByTagName(\"body\")[0].removeChild(document.getElementById(\"prompt\"))'></td></tr></table>";
		
	document.getElementById("promptbox").focus();
}
function myfunction(value, parentId) {
	if (value.length <= 0) {
		return false;
	} else {
		addOptionPrompt(value, parentId);
	}
}

function callPrompt(optionName, selectId) {
	var selectEle = document.getElementById(selectId);
	if(selectEle.options[selectEle.options.selectedIndex].value != "enterNew" &&
		selectEle.options[selectEle.options.selectedIndex].value != "Enter New")
		return false;
		
	prompt2("New " + optionName + ":", "myfunction", selectId);
}

