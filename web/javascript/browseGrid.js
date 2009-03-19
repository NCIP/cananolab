
function getLocalCounts(selectEleId, countType, dispatch) {
	var selectEle = document.getElementById(selectEleId);
	getAllGridCounts(selectEle);
}

function getAllGridCounts(location) {
	getGridCounts(location, "protocolCount", "countProtocols");
	getGridCounts(location, "sampleCount", "countSamples");
    getGridCounts(location, "publicationCount", "countPublications");
}

function getGridCounts(location, countType, dispatch) {
    var request;
	// display progress.gif while waiting for the response.
	var loaderimg = "<img src=\"images/ajax-loader.gif\" border=\"0\" class=\"counts\">";
	document.getElementById(countType).innerHTML = loaderimg;

	//var gridNode = selectEle.options[location.options.selectedIndex].value;
	var gridNodesStr = getSelectedOptions(location);

	var url = "/caNanoLab/publicCount.do?dispatch=" + dispatch + "&searchLocations=";
	url += gridNodesStr;
	//alert(countType);
    // Perform the AJAX request using a non-IE browser.
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();

      // Register callback function that will be called when
      // the response is generated from the server.
		request.onreadystatechange = function(){ updateCount(request, countType); };
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
				request.onreadystatechange = function(){ updateCount(request, countType); };
				request.open("GET", url, true);
				request.send();
			}
		}
	}
}

function  updateCount(request, countType) {
	if (request.readyState == 4) {
		if (request.status == 200) {
			var countStr = request.responseText;
			document.getElementById(countType).innerHTML = countStr;
		} else {
			alert("Unable to retrieve counts from server for "+countType);
		}
	}
}

function gotoSamples(dispatch) {
	var selectEle = document.getElementById("location");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchSample.do?dispatch="+dispatch+"&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function gotoPublications(dispatch) {
	var selectEle = document.getElementById("location");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchPublication.do?dispatch="+dispatch+"&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function gotoProtocols(dispatch) {
	var selectEle = document.getElementById("location");
	var gridNodesStr = getSelectedOptions(selectEle);
	var url = "/caNanoLab/searchProtocol.do?dispatch="+dispatch+"&searchLocations=";
	url += gridNodesStr;
	gotoPage(url);
	return false;
}

function getSelectedOptions(selectEle) {
	var options = selectEle.options;
	var selectedValues = "";
	for (var c = 0; c < options.length; c++) {
		if (options[c].selected) { //true if selected.
			selectedValues += options[c].value + "~";
		}
	}
	var cleanStr = selectedValues.substr(0, selectedValues.length - 1);
	return cleanStr;
}

