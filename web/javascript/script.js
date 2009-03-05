
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
			selection2.options[i] = new Option(value2Arr[i], value2Arr[i]);
		}
	}
}
/* filter second drop-down by first drop-down selection*/
function doubleDropdownForTheEditable(selection1, selection2, value1ToValue2) {
	/* initialize selection2 options */
	selection2.options.length = 0;
	selection2.options[0] = new Option("--?--", "");
	var value1 = selection1.options[selection1.selectedIndex].value;
	var value2Arr = value1ToValue2[value1];
	if (value2Arr != null) {
		for (i = 0; i < value2Arr.length; i++) {
			selection2.options[i + 1] = new Option(value2Arr[i], value2Arr[i]);
		}
	}
}
function doubleDropdownForNameValuePairs(selection1, selection2, value1ToValue2I, value1ToValue2II) {
	/* initialize selection2 options */
	selection2.options.length = 0;
	selection2.options[0] = new Option("--?--", "");
	var value1 = selection1.options[selection1.selectedIndex].value;
	var value2Arr1 = value1ToValue2I[value1];
	var value2Arr2 = value1ToValue2II[value1];
	if (value2Arr1 != null) {
		for (i = 0; i < value2Arr1.length; i++) {
			selection2.options[i + 1] = new Option(value2Arr1[i], value2Arr2[i]);
		}
	}
}
/* filter second drop-down by first drop-down selection, add an extra option at the end*/
function doubleDropdownWithExraOption(selection1, selection2, value1ToValue2, extraOptionName) {
	/* initialize selection2 options */
	selection2.options.length = 0;
	selection2.options[0] = new Option("", "");
	var value1 = selection1.options[selection1.selectedIndex].value;
	var value2Arr = value1ToValue2[value1];
	var last = 1;
	if (value2Arr != null) {
		for (i = 0; i < value2Arr.length; i++) {
			selection2.options[i] = new Option(value2Arr[i], value2Arr[i]);
			last = i + 1;
		}
	}
	selection2.options[last] = new Option(extraOptionName, extraOptionName);
}
/* filter a drop-down by a value*/
function dynamicDropdown(value, selection, value1ToValue2) {
	/* initialize selection options */
	selection.options.length = 0;
	selection.options[0] = new Option("", "");
	var value2Arr = value1ToValue2[value];
	if (value2Arr != null) {
		for (i = 0; i < value2Arr.length; i++) {
			selection.options[i] = new Option(value2Arr[i], value2Arr[i]);
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
		if (value2s != null) {
			for (var j = 0; j < value2s.length; j++) {
				value2Arr.push(value2s[j]);
			}
		}
	}
	for (var i = 0; i < value2Arr.length; i++) {
		selection2.options[i] = new Option(value2Arr[i], value2Arr[i]);
	}
}
/* filter second drop-down by first multi-box selections */
function multiboxToDropDownWithEditableOption(selection1, selection2, value1ToValue2) {
	selection2.options.length = 0;
	selection2.options[0] = new Option("--?--", "");
	var value1Arr = new Array();
	for (var i = 0; i < selection1.options.length; i++) {
		if (selection1.options[i].selected) {
			value1Arr.push(selection1[i].value);
		}
	}
	var value2Arr = new Array();
	for (var i = 0; i < value1Arr.length; i++) {
		var value2s = value1ToValue2[value1Arr[i]];
		if (value2s != null) {
			for (var j = 0; j < value2s.length; j++) {
				value2Arr.push(value2s[j]);
			}
		}
	}
	for (var i = 0; i < value2Arr.length; i++) {
		selection2.options[i + 1] = new Option(value2Arr[i], value2Arr[i]);
	}
}
function clearMultibox(selection) {
	for (var i = 0; i < selection.options.length; i++) {
		selection.options[i].text = "";
		selection.options[i].value = "";
	}
}
function submitAction(form, actionName, dispatchName) {
	form.action = actionName+".do?dispatch="+dispatchName+"&page=0";
	form.submit();
}
function addComponent(form, actionName, dispatchName) {
	form.action = actionName + ".do?dispatch=" + dispatchName + "&page=1";
	form.submit();
}
function removeComponent(form, actionName, compInd, dispatchName) {
	form.action = actionName + ".do?dispatch=" + dispatchName + "&page=1&compInd=" + compInd;
	form.submit();
}
function addChildComponent(form, actionName, compInd, dispatchName) {
	form.action = actionName + ".do?dispatch=" + dispatchName + "&page=1&compInd=" + compInd;
	form.submit();
}
function removeChildComponent(form, actionName, compInd, childCompInd, dispatchName) {
	form.action = actionName + ".do?dispatch=" + dispatchName + "&page=1&compInd=" + compInd + "&childCompInd=" + childCompInd;
	form.submit();
}
function loadFile(form, actionName, fileNumber) {
	form.action = actionName + ".do?dispatch=loadFile&page=0&fileNumber=" + fileNumber;
	form.submit();
}
function refreshManufacturers(form, action) {
	form.dispatch.value = action;
	form.submit();
}
/* form has "." in the property names */
function doubleDropdownWithNestedProperties(form, elementName1, elementName2, value1ToValue2) {
	var select1, select2;
	select1 = getElement(form, elementName1);
	select2 = getElement(form, elementName2);
	doubleDropdown(select1, select2, value1ToValue2);
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
var t = null;
function popImage(event, imgSrc, imgId) {
	var popImg = new Image();
	var ratio = 1;
	var maxHeight = 800;
	popImg.src = imgSrc;
	width = popImg.width + 20;
	height = popImg.height + 20;
	if (width>height){
		if (width>maxHeight){
		 	ratio = maxHeight/width;
		    width = maxHeight+2;
		    height = ratio * height;
		}
	}else{
		if (height>maxHeight){
		 	ratio = maxHeight/height*100;
		    height = maxHeight+2;
		    width = ratio * width/100;
		}
	}
//    leftPos=event.clientX-width-200;
//    topPos=event.clientY-200;
	leftPos = 50;
	topPos = 50;
	if (imgWindow && imgWindow.open) {
		imgWindow.close();
		t = null;
	}
	//myScript = "<html><head>n" + "<script>n" + "var t=null;n" + "function closeMe() {n" + "t=setTimeout("window.close();", 5000);n" + "}n" + "</script>n" + "<title>Characterization File</title></head>n";
	myScript = "<html><head><title>Characterization File</title></head>\n";
	imgWindow = window.open("", "charFileWindow", "width=" + width + ",height=" + height + ",left=" + leftPos + ",top=" + topPos);
	imgWindow.document.write(myScript);
	//imgWindow.document.write("<body onLoad="closeMe();self.focus();" bgcolor="#FFFFFF">n");
	imgWindow.document.write("<body onLoad=\"self.focus();\" bgcolor=\"#FFFFFF\">\n");
	imgWindow.document.write("<img width="+(width-10)+" styleId='" + imgId + "' src='" + imgSrc + "'/>\n");
	imgWindow.document.write("</body></html>");
	t = setTimeout("imgWindow.close();", 15000);
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
function radLinkOrUpload() {
	var linkEle = document.getElementById("link");
	var loadEle = document.getElementById("load");
	if (document.getElementById("external0").checked) {
		loadEle.style.display = "inline";
		linkEle.style.display = "none";
	} else {
		loadEle.style.display = "none";
		linkEle.style.display = "inline";
	}
}
function filterFloatNumber(evt) {
    var keyCode = evt.which ? evt.which : evt.keyCode;
    return (keyCode >= '0'.charCodeAt() && keyCode <= '9'.charCodeAt()) ||
     (keyCode >= 96 && keyCode <= 105) || keyCode == 190 || keyCode == 110 || keyCode == 46 || keyCode == 8 ;
}
function filterInteger(evt) {
    var keyCode = evt.which ? evt.which : evt.keyCode;
    return (keyCode >= '0'.charCodeAt() && keyCode <= '9'.charCodeAt()) ||
     (keyCode >= 96 && keyCode <= 105) || keyCode == 46 || keyCode == 8 ;
}
function getSelectedOptions(selectEle) {
	var options = selectEle.options;
	var selectedValues = "";
	for (var c=0; c<options.length; c++ ) {
		if (options[c].selected) { //true if selected.
			selectedValues += options[c].value + "~";
		}
	}
	var cleanStr = selectedValues.substr(0, selectedValues.length - 1);
	return cleanStr;
}
function displayLogin() {
	showhide('loginBlock');
	return false;
}

function showhide(layer_ref) {
	var layerBlock = document.getElementById(layer_ref);
	if(layerBlock.style.display == 'none')
		layerBlock.style.display = 'block';
	else
		layerBlock.style.display = 'none';
}

function show(layer_ref) {
	var layerBlock = document.getElementById(layer_ref);
	if (layerBlock==null){
		alert(layer_ref +" is null (cannot show)");
		return;
	}
	if(layerBlock.style.display == 'none')
		layerBlock.style.display = 'block';
}

function hide(layer_ref) {
	var layerBlock = document.getElementById(layer_ref);
	if (layerBlock==null){
		alert(layer_ref +" is null (cannot show)");
		return;
	}
	if(layerBlock.style.display == 'block')
		layerBlock.style.display = 'none';
}

function showSummary(layer_ref) {
	if (layer_ref=='ALL'){
		document.getElementById("summaryTabALL").style.display = 'block';
		for (var i=1; i<5; i++){
			document.getElementById("summarySection"+i).style.display = 'block';
			document.getElementById("summarySeparator"+i).style.display = 'block';
			document.getElementById("summaryTab"+i).style.display = 'none';
		}
		return;
	}
	document.getElementById("summaryTabALL").style.display = 'none';
	for (var i=1; i<5; i++){
		if (layer_ref==i){
			document.getElementById("summaryTab"+i).style.display = 'block';
			document.getElementById("summarySection"+i).style.display = 'block';
			document.getElementById("summarySeparator"+i).style.display = 'block';
		}else{
			document.getElementById("summaryTab"+i).style.display = 'none';
			document.getElementById("summarySection"+i).style.display = 'none';
			document.getElementById("summarySeparator"+i).style.display = 'none';
		}
	}
}



