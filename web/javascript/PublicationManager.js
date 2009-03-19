function setPublicationDropdowns() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	PublicationManager.getPublicationCategories(searchLocations, function (data) {
			dwr.util.removeAllOptions("publicationCategories");
			dwr.util.addOptions("publicationCategories", data);
		});
	SampleManager.getNanomaterialEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("nanomaterialEntityTypes");
			dwr.util.addOptions("nanomaterialEntityTypes", data);
		});
	SampleManager.getFunctionalizingEntityTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionalizingEntityTypes");
			dwr.util.addOptions("functionalizingEntityTypes", data);
		});

	SampleManager.getFunctionTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("functionTypes");
			dwr.util.addOptions("functionTypes", data);
		});
	return false;
}

/*
 * in bodySubmitPublication.jsp
 * set publication status to "published" when user selects "report" publication category.
 */
function setReportFields(pubCategoryId, pubStatusId) {
	var pubCategory = document.getElementById(pubCategoryId);
	var pubmedRow = document.getElementById("pubMedRow");
	var doiRow = document.getElementById("doiRow");
	var journalRow = document.getElementById("journalRow");

	var volumeTitleEle = document.getElementById("volumeTitle");
	var volumeValueEle = document.getElementById("volumeValue");

	var spageTitleEle = document.getElementById("spageTitle");
	var spageValueEle = document.getElementById("spageValue");
	var epageTitleEle = document.getElementById("epageTitle");
	var epageValueEle = document.getElementById("epageValue");

	var otext = pubCategory.options[pubCategory.options.selectedIndex].text;
	if (otext == "report") {
		var pubStatus = document.getElementById(pubStatusId);
		for (var i = pubStatus.options.length - 1; i > 0; i--) {
			if(pubStatus.options[i].text == "published")
				pubStatus.options[i].selected = true;
		}

		//hide pubMedId and DOI
		pubmedRow.style.display = "none";
		doiRow.style.display = "none";
		journalRow.style.display = "none";

		volumeTitleEle.style.display = "none";
		volumeValueEle.style.display = "none";

		spageTitleEle.style.display = "none";
		spageValueEle.style.display = "none";
		epageTitleEle.style.display = "none";
		epageValueEle.style.display = "none";
	} else {
		pubmedRow.style.display = "";
		doiRow.style.display = "";
		journalRow.style.display = "";

		volumeTitleEle.style.display = "inline";
		volumeValueEle.style.display = "inline";

		spageTitleEle.style.display = "inline";
		spageValueEle.style.display = "inline";
		epageTitleEle.style.display = "inline";
		epageValueEle.style.display = "inline";
	}

}

/*
 * in bodySubmitPublication.jsp
 * set publication status to "published" when user selects "report" publication category.
 */
function setSearchReportFields() {
	var pubCategory = document.getElementById("publicationCategories");
	var pubmedRow = document.getElementById("pubMedRow");

	var otext = pubCategory.options[pubCategory.options.selectedIndex].text;
	if (otext == "report") {

		//hide pubMedId and DOI
		pubmedRow.style.display = "none";
	} else {
		pubmedRow.style.display = "";
	}
}

function addPubmed(form, sampleId) {
	var pubmedId = document.getElementById('pubmedId').value;
	form.action = "publication.do?dispatch=setupPubmed&page=0&location=local&sampleId=" + sampleId + "&pubmedId=" + pubmedId;
	form.submit();
}
function setupReport(form, sampleId) {
	var selectEle = document.getElementById('file.domainFile.category');
	if(selectEle.options[selectEle.options.selectedIndex].value == 'report') {
		form.action = "publication.do?dispatch=setupReport&page=0&location=local&sampleId=" + sampleId;
		form.submit();
	}
	return false;
}