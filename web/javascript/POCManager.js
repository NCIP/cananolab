

function setupPOC(form, selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var otext = selectEle.options[selectEle.options.selectedIndex].text;
	if(otext == "[Other]") {
		form.action = "submitPointOfContact.do?dispatch=setup&page=0&location=local";
		form.submit();
	}
	return false;
}

function setupOrgDetailView(form, selectEleId, particleIdEle) {
	var selectEle = document.getElementById(selectEleId);
	var org = selectEle.options[selectEle.options.selectedIndex].text;
	var pocId = selectEle.options[selectEle.options.selectedIndex].value;
	var particleId = null;
	if (document.getElementById(particleIdEle)!=null){
		particleId = document.getElementById(particleIdEle).value;
	}
	var particleStr = "";
	if (particleId!=null){
		particleStr = "&particleId="+particleId;
	}
	if(org != "[Other]") {
		form.action = "submitPointOfContact.do?dispatch=detailView&page=0&location=local" + 
			particleStr+"&pocId="+pocId+"&organizationName=" + org;
		form.submit();
	}
	return false;
}

function setPOCDetailLink(selectEleId, linkId) {
	var selectEle = document.getElementById(selectEleId);
	var linkEle = document.getElementById(linkId);
	var org = selectEle.options[selectEle.options.selectedIndex].text;
	if(org != "[Other]" && org != "") {
		linkEle.style.display = "inline";
	} else {
		linkEle.style.display = "none";	
	}
}

function setSecondaryPOC(form, selectEleId, pocIndex) {
	var selectEle = document.getElementById(selectEleId);
	var pocId = selectEle.options[selectEle.options.selectedIndex].value;
	if(pocId != "other" && pocId != "") {
		form.action = "submitPointOfContact.do?dispatch=getPointOfContact&pocIndex=" + pocIndex + "&pocId=" + pocId;
		form.submit();
	}
}
/*
function setSecondaryPOC(selectEleId, pocIndex) {
	var selectEle = document.getElementById(selectEleId);
	var pocId = selectEle.options[selectEle.options.selectedIndex].value;
	CommonManager.getPointOfContact(pocIndex + "_" + pocId, function (data) {
			dwr.util.removeAllOptions("nanoparticleEntityTypes");
			dwr.util.addOptions("nanoparticleEntityTypes", data);
		});
}


function setSecondaryPOC(selectEleId, pocIndex) {
	var selectEle = document.getElementById(selectEleId);
	var pocId = selectEle.options[selectEle.options.selectedIndex].value;
    var request;
	
	
	var url = "/caNanoLab/submitPointOfContact.do?dispatch=getPointOfContact&pocIndex=" + pocIndex + "&pocId=" + pocId;
	
    // Perform the AJAX request using a non-IE browser.
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
  
      // Register callback function that will be called when
      // the response is generated from the server.
		//request.onreadystatechange = function(){ updateCount(request, countType); };
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
				//request.onreadystatechange = function(){ updateCount(request, countType); };
				request.open("GET", url, true);
				request.send();
			}
		}
	}
}

*/
