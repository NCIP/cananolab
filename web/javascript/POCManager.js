
function setupPOC(form, selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var otext = selectEle.options[selectEle.options.selectedIndex].text;
	if(otext == "[Other]") {
		form.action = "submitNanoparticleSample.do?dispatch=setupPointOfContact&page=0&location=local";
		form.submit();
	}
	return false;
}

function setupOrgDetailView(form, selectEleId) {
	var selectEle = document.getElementById(selectEleId);
	var org = selectEle.options[selectEle.options.selectedIndex].text;
	if(org != "[Other]") {
		form.action = "submitPointOfContact.do?dispatch=detailView&page=0&location=local" + 
			"&organizationName=" + org;
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