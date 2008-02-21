var request;
function getLocalCounts(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	getGridCounts(selectEle);
}
function getGridCounts(selectEle) {
	var gridNode = selectEle.options[selectEle.options.selectedIndex].value;
	var url;
	if (gridNode == "local") {
		url = "/caNanoLab/searchNanoparticle.do?dispatch=publicCounts";
	} else {
		url = "/caNanoLab/remoteSearchNanoparticle.do?dispatch=publicCounts&gridNodeHost=";
		url += gridNode;
	}
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
function browseParitcles(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var gridNode = selectEle.options[selectEle.options.selectedIndex].value;
	var url;
	if (gridNode == "local") {
		url = "/caNanoLab/searchNanoparticle.do?dispatch=search";
	} else {
		url = "/caNanoLab/remoteSearchNanoparticle.do?dispatch=publicSearch&gridNodeHost=";
		url += gridNode;
	}
	gotoPage(url);
	return false;
}
function browseReports(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var gridNode = selectEle.options[selectEle.options.selectedIndex].value;
	var url;
	if (gridNode == "local") {
		url = "/caNanoLab/searchReport.do?dispatch=search";
	} else {
		var url = "/caNanoLab/remoteSearchReport.do?dispatch=publicSearch&gridNodeHost=";
		url += gridNode;
	}
	gotoPage(url);
	return false;
}
function browseProtocols(selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var gridNode = selectEle.options[selectEle.options.selectedIndex].value;
	var url;
	if (gridNode == "local") {
		url = "/caNanoLab/searchProtocol.do?dispatch=search";
	}
	gotoPage(url);
	return false;
}