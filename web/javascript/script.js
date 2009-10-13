// IE and firefox behaves differently for table row style
if (navigator.appName.indexOf("Microsoft") > -1) {
	var canSeeTableRow = "block";
} else {
	var canSeeTableRow = "table-row";
}
function changeMenuStyle(obj, new_style) {
	obj.className = new_style;
}
function showCursor() {
	document.body.style.cursor = "pointer";
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
 * moveItems is a function used in moving items in one multiple select to the
 * other.
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
			// arrFbox.sort();
			// arrTbox.sort();
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
function submitAction(form, actionName, dispatchName, page) {
	form.action = actionName + ".do?dispatch=" + dispatchName + "&page=" + page;
	form.submit();
}
function getElement(form, elementName) {
	var element;
	for (var i = 0; i < form.elements.length; ) {
		if (form.elements[i].name.indexOf(elementName) != -1) {
			element = form.elements[i];
		}
		i = i + 1;
	}
	return element;
}
function disableTextElement(form, elementName) {
	var element = getElement(form, elementName);
	element.value = "";
	element.disabled = true;
}
function enableTextElement(form, elementName) {
	var element = getElement(form, elementName);
	element.disabled = false;
}
function updateOtherField(form, elementName, otherElementName) {
	var element = getElement(form, elementName);
	if (element.value == "Other") {
		enableTextElement(form, otherElementName);
	} else {
		disableTextElement(form, otherElementName);
	}
}
var imgWindow = null;
// var t = null;
function popImage(event, imgSrc, imgId) {
	var popImg = new Image();
	popImg.src = imgSrc;
	if (imgWindow != null && imgWindow.open) {
		imgWindow.close();
		// t = null;
	}
	var topPos = 50;
	var leftPos = 50;
	var maxWidth = 800;
	var maxHeight = 800;
	if (popImg.width > 0) {
		var width = popImg.width + 20;
		var height = popImg.height + 20;
		if (width > height) {
			if (width > maxWidth) {
				var ratio = maxWidth / width;
				width = maxWidth;
				height = ratio * height;
			}
		} else {
			if (height > maxHeight) {
				var ratio = maxHeight / height;
				height = maxHeight;
				width = ratio * width;
			}
		}
		imgWindow = window.open("", "charFileWindow", "width=" + width + ",height=" + height + ",left=" + leftPos + ",top=" + topPos);
		imgWindow.document.writeln("<html><head><title>Characterization File</title></head>");
		imgWindow.document.writeln("<body onLoad=\"self.focus();\" bgcolor=\"#FFFFFF\">");
		imgWindow.document.writeln("<img width='" + (width - 20) + "' height='" + (height - 20) + "' styleId='" + imgId + "' src='" + imgSrc + "'/>");
		imgWindow.document.writeln("</body></html>");
	} else {
		imgWindow = window.open("", "charFileWindow", "left=" + leftPos + ",top=" + topPos);
		imgWindow.document.writeln("<html><head><title>Characterization File</title></head>");
		imgWindow.document.writeln("<body onLoad=\"resizePopup();\" bgcolor=\"#FFFFFF\">");
		imgWindow.document.writeln("<img id='popImage' styleId='" + imgId + "' src='" + imgSrc + "'/>");
		imgWindow.document.writeln("</body></html>");
	}
// t = setTimeout("imgWindow.close();", 15000);
}
/**
 * Try to resize the window After the image is loaded.
 */
function resizePopup() {
	var popImage = document.getElementById("popImage");
	if (popImage != null) {
		var maxWidth = 800;
		var maxHeight = 800;
		var width = popImage.width + 20;
		var height = popImage.height + 20;
		if (popImage.width > 0) {
			if (width > height) {
				if (width > maxWidth) {
					var ratio = maxWidth / width;
					width = maxWidth;
					height = ratio * height;
				}
			} else {
				if (height > maxHeight) {
					var ratio = maxHeight / height;
					height = maxHeight;
					width = ratio * width;
				}
			}
			popImage.width = width - 20;
			popImage.height = height - 20;
			window.resizeTo(width, height);
			self.focus();
		} else {
			imgWindow.close(); // close window as the image can't be shown.
		}
	}
}
function printPage0(url) {
	var obj = document.all.tags("link");
	for (i = 0; i < obj.length; i++) {
		if (obj[i].name == "printlink") {
			obj[i].href = url;
		}
	}
	// if IE directly call print, otherwise bring up a pop up window first
	if (navigator.appVersion.indexOf("MSIE") != -1) {
		window.print();
	} else {
		printWindow = window.open(url, "printWindow", "top=50,left=50,width=650,height=650,menubar=yes,scrollbars=yes");
		printWindow.focus();
	}
}
function printPage(url) {
	printWindow = window.open(url, "printWindow", "top=50,left=50,width=600,height=600,resizable=yes,menubar=yes,scrollbars=yes");
	printWindow.focus();
}
function cancel(parameter) {
	var url = document.referrer;
	if (url != null) {
		if (parameter != null) {
			url = url + "&" + parameter + "=true";
		}
		gotoPage(url);
	}
}
function validFloatNumber(floatString) {
	var re = /^[-+]?[0-9]+(\.[0-9]+)?$/;
	return re.test(floatString);
}
function filterFloatNumber(evt) {
	var keyCode = evt.which ? evt.which : evt.keyCode;
	return (keyCode >= "0".charCodeAt() && keyCode <= "9".charCodeAt())
			|| (keyCode >= 96 && keyCode <= 105) || keyCode == 190
			|| keyCode == 110 || keyCode == 46 || keyCode == 8
			|| keyCode == 109;
}
function filterInteger(evt) {
	var keyCode = evt.which ? evt.which : evt.keyCode;
	return (keyCode >= "0".charCodeAt() && keyCode <= "9".charCodeAt())
			|| (keyCode >= 96 && keyCode <= 105) || keyCode == 46
			|| keyCode == 8;
}
function getSelectedOptions(selectEle) {
	var options = selectEle.options;
	var selectedValues = "";
	for (var c = 0; c < options.length; c++) {
		if (options[c].selected) { // true if selected.
			selectedValues += options[c].value + "~";
		}
	}
	var cleanStr = selectedValues.substr(0, selectedValues.length - 1);
	return cleanStr;
}
function displayLogin() {
	showhide("loginBlock");
	return false;
}
function showhide(layer_ref, isTableRow) {
	var layerBlock = document.getElementById(layer_ref);
	if (layerBlock == null) {
		// alert(layer_ref + " is null (cannot show)");
		return;
	}
	if (layerBlock.style.display == "none") {
		if (isTableRow == true) {
			layerBlock.style.display = canSeeTableRow;
		} else {
			layerBlock.style.display = "block";
		}
	} else {
		layerBlock.style.display = "none";
	}
}
function show(layer_ref, isTableRow) {
	var layerBlock = document.getElementById(layer_ref);
	if (layerBlock == null) {
		// alert(layer_ref + " is null (cannot show)");
		return;
	}
	if (layerBlock.style.display == "none") {
		if (isTableRow == true) {
			layerBlock.style.display = canSeeTableRow;
		} else {
			layerBlock.style.display = "block";
		}
	}
}
function hide(layer_ref) {
	var layerBlock = document.getElementById(layer_ref);
	if (layerBlock == null) {
		// alert(layer_ref + " is null (cannot show)");
		return;
	}
	layerBlock.style.display = "none";
}
function showSummary(layer_ref, totalLayers) {
	var printLink = document.getElementById("printLink");
	var printUrl = document.getElementById("printUrlAll");
	var exportLink = document.getElementById("exportLink");
	var exportUrl = document.getElementById("exportUrlAll");
	if (layer_ref == "ALL") {
		document.getElementById("summaryTabALL").style.display = "block";
		for (var i = 1; i <= totalLayers; i++) {
			document.getElementById("summarySection" + i).style.display = "block";
			document.getElementById("summarySeparator" + i).style.display = "block";
			document.getElementById("summaryTab" + i).style.display = "none";
		}
		printLink.href = printUrl.href;
		exportLink.href = exportUrl.href;
		return;
	}
	document.getElementById("summaryTabALL").style.display = "none";
	for (var i = 1; i <= totalLayers; i++) {
		if (layer_ref == i) {
			document.getElementById("summaryTab" + i).style.display = "block";
			document.getElementById("summarySection" + i).style.display = "block";
			document.getElementById("summarySeparator" + i).style.display = "block";
			printUrl = document.getElementById("printUrl" + i);
			exportUrl = document.getElementById("exportUrl" + i);
		} else {
			document.getElementById("summaryTab" + i).style.display = "none";
			document.getElementById("summarySection" + i).style.display = "none";
			document.getElementById("summarySeparator" + i).style.display = "none";
		}
	}
	printLink.href = printUrl.href;
	exportLink.href = exportUrl.href;
}
function addFile(actionName, form) {
	// for characterization form, files are saved to the session per finding,
	// not directly
	// to the database
	if (actionName == "characterization") {
		dispatch = "addFile";
	} else {
		dispatch = "saveFile";
	}
	submitAction(form, actionName, dispatch, 3);
}
function confirmDelete(type) {
	answer = confirm("Are you sure you want to delete the " + type + "?");
	return answer;
}
function removeFile(actionName, form, index) {
	var answer = confirmDelete("file");
	if (answer != 0) {
		submitAction(form, actionName, "removeFile", 3);
	}
}
function populateFile(file) {
	if (file != null) {
		dwr.util.setValue("fileType", file.domainFile.type);
		dwr.util.setValue("fileTitle", file.domainFile.title);
		dwr.util.setValue("fileKeywords", file.keywordsStr);
		dwr.util.setValue("fileDescription", file.domainFile.description);
		dwr.util.setValue("fileVisibility", file.visibilityGroups);
		if (file.domainFile.uriExternal) {
			dwr.util.setValue("external1", 1);
			dwr.util.setValue("external0", 0);
		} else {
			dwr.util.setValue("external1", 0);
			dwr.util.setValue("external0", 1);
		}
		if (file.domainFile.id != null) {
			dwr.util.setValue("hiddenFileId", file.domainFile.id);
		}
		if (file.domainFile.uri != null) {
			dwr.util.setValue("hiddenFileUri", file.domainFile.uri);
			if (file.domainFile.uriExternal == 0) {
				dwr.util.setValue("uploadedUri", file.domainFile.uri);
				dwr.util.setValue("externalUrl", "");
				show("uploadedUri");
			} else {
				dwr.util.setValue("uploadedUri", "");
				dwr.util.setValue("externalUrl", file.domainFile.uri);
			}
		} else {
			dwr.util.setValue("uploadedUri", "");
		}
		if (file.domainFile.uriExternal) {
			show("link");
			hide("load");
		} else {
			show("load");
			hide("link");
		}
	} else {
		sessionTimeout();
	}
}
function displayFileRadioButton() {
	var external0 = dwr.util.getValue("external0");
	var external1 = dwr.util.getValue("external1");
	show("load");
	hide("link");
	show("uploadedUri");
	if (external0 == false && external1 == true) {
		show("link");
		hide("load");
		hide("uploadedUri");
	}
}
function deleteData(type, form, actionName) {
	var answer = confirmDelete(type);
	if (answer) {
		submitAction(form, actionName, "delete", 0);
	}
}
function sessionTimeout() {
	alert("Session has timed out.  Please log in again.");
	location.href = "welcome.do";
}
function openSubmissionForm(styleId) {
	show("new" + styleId);
	hide("add" + styleId);
}
function closeSubmissionForm(styleId) {
	hide("new" + styleId);
	show("add" + styleId);
}
function validateSavingTheData(dataStyleId, dataName) {
	var displayStatus = document.getElementById(dataStyleId).style.display;
	if (displayStatus == "block" || displayStatus == canSeeTableRow) {
		alert("Please click on either the Save button or the Cancel button in the " + dataName + " form.");
		return false;
	} else {
		return true;
	}
}

