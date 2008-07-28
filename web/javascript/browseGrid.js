var request;
function getLocalCounts(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	getGridCounts(selectEle);
}
function getGridCounts(selectEle) {

	// display progress.gif while waiting for the response.
	var loaderimg = "<img src=\"images/ajax-loader.gif\" border=\"0\" class=\"counts\">";
	document.getElementById("particleCount").innerHTML = loaderimg;
	document.getElementById("reportCount").innerHTML = loaderimg;
	document.getElementById("protocolCount").innerHTML = loaderimg;
	
	//var gridNode = selectEle.options[selectEle.options.selectedIndex].value;
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/publicCount.do?dispatch=publicCounts&searchLocations=";
	url += gridNodesStr;
	
    // Perform the AJAX request using a non-IE browser.
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
   
      // Register callback function that will be called when
      // the response is generated from the server.
		request.onreadystatechange = updateParticleCount;
		try {
			request.open("GET", url, true);
		}
		catch (e) {
			alert("Unable to connect to server to retrieve counts.");
		}
		request.send(null);
    // Perform the AJAX request using an IE browser.
	} else {
		if (window.ActiveXObject) {
			request = new ActiveXObject("Microsoft.XMLHTTP");
			if (request) {
				request.onreadystatechange = updateParticleCount;
				request.open("GET", url, true);
				request.send();
			}
		}
	}
}
function updateParticleCount() {
	if (request.readyState == 4) {
		if (request.status == 200) {
			var countStr = request.responseText;
			var countarray = countStr.split("\t");
			document.getElementById("particleCount").innerHTML = countarray[0];
			document.getElementById("reportCount").innerHTML = countarray[1];
			document.getElementById("protocolCount").innerHTML = countarray[2];
		} else {
			alert("Unable to retrieve particle count from server.");
		}
	}
}
function browseParitcles() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchNanoparticle.do?dispatch=search&searchLocations=";
	url += gridNodesStr;

	gotoPage(url);
	return false;
}
function searchParitcles() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchNanoparticle.do?dispatch=setup&searchLocations=";
	url += gridNodesStr;
	
	gotoPage(url);
	return false;
}
function searchDocuments() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchDocument.do?dispatch=setup&searchLocations=";
	url += gridNodesStr;
	
	gotoPage(url);
	return false;
}
function browseDocuments() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchDocument.do?dispatch=search&searchLocations=";
	url += gridNodesStr;

	gotoPage(url);
	return false;
}
function searchProtocols() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchProtocol.do?dispatch=setup&searchLocations=";
	url += gridNodesStr;

	gotoPage(url);
	return false;
}
function browseProtocols() {
	var selectEle = document.getElementById('location');
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchProtocol.do?dispatch=search&searchLocations=";
	url += gridNodesStr;

	gotoPage(url);
	return false;
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