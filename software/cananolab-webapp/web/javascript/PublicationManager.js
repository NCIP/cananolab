var authorCache = {};
var currentPublication = null;
var numberOfAuthors = 0; // number of unique authors in the cache, used to
// generate author id
var confirmMessage = "test";
var updateFormPrompt = "false";
var redirect = null;
var appOwner = "";
function clearPublication() {
	enableOuterButtons();
	show("accessBlock");
	// clear submission form first
	PublicationManager.clearPublication(function(publication) {
		dwr.util.setValues(publication);
		currentPublication = publication;
		// not sure if we need to clear status, description, samples, file, and
			// visibility
			populateAuthors(false);
			enableAutoFields();
		});
}
function showAuthors(publicationId) {
}

function updateSubmitFormBasedOnCategory() {
	var category = dwr.util.getValue("category");
	if (category != "report" && category != "book chapter" && category != "") {
		show("pubMedRow", true);
		show("doiRow", true);
		show("journalRow", true);
		show("volumePageRow", true);
	} else {
		hide("pubMedRow");
		hide("doiRow");
		hide("journalRow");
		hide("volumePageRow");
	}
	// if report, set publish status to published
	if (category == "report") {
		dwr.util.setValue("status", "published");
	}
}

function enableAutoFields() {
	document.getElementById("domainFile.digitalObjectId").readOnly = false;
	document.getElementById("domainFile.title").readOnly = false;
	document.getElementById("domainFile.journalName").readOnly = false;
	document.getElementById("domainFile.year").readOnly = false;
	document.getElementById("domainFile.volume").readOnly = false;
	document.getElementById("domainFile.startPage").readOnly = false;
	document.getElementById("domainFile.endPage").readOnly = false;
	show("addAuthor");
	show("fileSection");
}

function disableAutoFields() {
	document.getElementById("domainFile.digitalObjectId").readOnly = true;
	document.getElementById("domainFile.title").readOnly = true;
	document.getElementById("domainFile.journalName").readOnly = true;
	document.getElementById("domainFile.year").readOnly = true;
	document.getElementById("domainFile.volume").readOnly = true;
	document.getElementById("domainFile.startPage").readOnly = true;
	document.getElementById("domainFile.endPage").readOnly = true;
	hide("addAuthor");
	hide("fileSection");
}

function updateSearchFormBasedOnCategory() {
	var category = dwr.util.getValue("publicationCategory");
	if (category != "report" && category != "book chapter" && category != "") {
		show("pubMedRow", true);
	} else {
		hide("pubMedRow");
	}
}
function showMatchedSampleNameDropdown() {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedSampleSelect");
	hide("selectMatchedSampleButton");
	var associatedSampleNames = dwr.util.getValue("associatedSampleNames");
	PublicationManager.getMatchedSampleNames(associatedSampleNames, function(
			data) {
		if (data.length == 0) {
			dwr.util.setValue("associatedSampleNames", "");
		}
		dwr.util.removeAllOptions("matchedSampleSelect");
		dwr.util.addOptions("matchedSampleSelect", data);
		hide("loaderImg");
		show("matchedSampleSelect");
		show("selectMatchedSampleButton");
	});
}
function updateAssociatedSamples() {
	var selected = dwr.util.getValue("matchedSampleSelect");
	if (selected.length == 0) {
		return;
	}
	hide("matchedSampleSelect");
	hide("selectMatchedSampleButton");
	if (selected.length == 1) {
		dwr.util.setValue("associatedSampleNames", selected);
		return;
	}
	var sampleNames = "";
	for (i = 0; i < selected.length - 1; i++) {
		sampleNames = sampleNames + selected[i] + "\n";
	}
	sampleNames = sampleNames + selected[i];
	dwr.util.setValue("associatedSampleNames", sampleNames, {
		escapeHtml : false
	});
}
function setPublicationDropdowns() {
	var searchLocations = getSelectedOptions(document
			.getElementById("searchLocations"));
	PublicationManager.getPublicationCategories(searchLocations,
			function(data) {
				dwr.util.removeAllOptions("publicationCategories");
				dwr.util.addOptions("publicationCategories", data);
			});
	SampleManager.getNanomaterialEntityTypes(searchLocations, function(data) {
		dwr.util.removeAllOptions("nanomaterialEntityTypes");
		dwr.util.addOptions("nanomaterialEntityTypes", data);
	});
	SampleManager.getFunctionalizingEntityTypes(searchLocations,
			function(data) {
				dwr.util.removeAllOptions("functionalizingEntityTypes");
				dwr.util.addOptions("functionalizingEntityTypes", data);
			});
	SampleManager.getFunctionTypes(searchLocations, function(data) {
		dwr.util.removeAllOptions("functionTypes");
		dwr.util.addOptions("functionTypes", data);
	});
	return false;
}

function searchPublication() {
	var pubMedId = dwr.util.getValue("pubMedId");
	if (pubMedId != null && pubMedId != 0) {
		PublicationManager.searchPubMedById(pubMedId, validatePubMedInfo);
	} else {
		submitAction(document.forms[0], "searchPublication", "search", 1);
	}
}
function validatePubMedInfo(publication) {
	if (publication.domainFile.pubMedId == null) {
		alert("Invalid PubMed ID entered.");
	} else {
		submitAction(document.forms[0], "searchPublication", "search", 1);
	}
}

function fillPubMedInfo(updatePrompt, theRedirect) {
	var pubMedId = dwr.util.getValue("domainFile.pubMedId");
	updateFormPrompt = updatePrompt;
	redirect = theRedirect;
	if (pubMedId != null && pubMedId != 0) {
		PublicationManager.retrievePubMedInfo(pubMedId, populatePubMedInfo);
	} else {
		PublicationManager.retrieveCurrentPub(populateAuthorInfo);
	}
}

function populatePubMedInfo(publication) {
	if (publication != null) {
		currentPublication = publication;
		dwr.util.setValues(publication, {
			escapeHtml : false
		});
		// If PubMedId is null in returned pub -> pubMedId not found.
		if (publication.domainFile.pubMedId == null) {
			clearPublication();
			// populateAuthors(false);
			// enableAutoFields();
		} else {
			// Set keywordsStr & description again for special characters
			dwr.util.setValue("keywordsStr", publication.keywordsStr, {
				escapeHtml : false
			});
			dwr.util.setValue("domainFile.description",
					publication.domainFile.description, {
						escapeHtml : false
					});
			populateAuthors(true);
			disableAutoFields();
			waitCursor();
			confirmMessage = "A database record with the same PubMed ID already exists.  Load saved information?";
			PublicationManager.updatePubMedWithExistingPublication(
					publication.domainFile.pubMedId, populateNonPubMedFields);
		}
	} else {
		sessionTimeout();
	}
}

function populateNonPubMedFields(publication) {
	if (publication != null) {
		if (publication.userUpdatable == true) {
			if (updateFormPrompt == "true" && confirm(confirmMessage)) {
				dwr.util.setValue("category", publication.domainFile.category);
				dwr.util.setValue("status", publication.domainFile.status);
				dwr.util.setValue("keywordsStr", publication.keywordsStr, {
					escapeHtml : false
				});
				dwr.util.setValue("domainFile.description",
						publication.domainFile.description, {
							escapeHtml : false
						});
				dwr.util.setValue("associatedSampleNames",
						publication.sampleNamesStr, {
							escapeHtml : false
						});
				dwr.util.setValue("researchAreas", publication.researchAreas);
				currentPublication = publication;
			}
			// populateAuthors(true);
			if (redirect == "setupUpdate") {
				gotoUpdatePage(publication);
			}
		} else {
			alert("The publication already exists and you don't have update privilege on this publication");
			disableOuterButtons();
			hide("accessBlock");
		}
	}
	updateFormPrompt = "false";
	confirmMessage = "";
	hideCursor();
}

function updateWithExistingDOI(updatePrompt) {
	var doi = dwr.util.getValue("domainFile.digitalObjectId");
	updateFormPrompt = updatePrompt;
	var pubMedId = dwr.util.getValue("domainFile.pubMedId");
	confirmMessage = "A database record with the same DOI already exists.  Load saved information?";
	if ((pubMedId == null || pubMedId == 0) && doi != null) {
		waitCursor();
		PublicationManager.updateWithExistingDOIPublication(doi,
				populateAllFields);
	}
}

function updateWithExistingNonPubMedDOIPublication(updatePrompt,
		applicationOwner, uri) {
	appOwner = applicationOwner;
	var pubMedId = dwr.util.getValue("domainFile.pubMedId");
	var doi = dwr.util.getValue("domainFile.digitalObjectId");
	var category = dwr.util.getValue("category");
	var title = dwr.util.getValue("domainFile.title");
	var firstAuthor = null;
	if (currentPublication != null) {
		var authors = currentPublication.authors;
		if (authors != null) {
			firstAuthor = authors[0];
		}
	}
	updateFormPrompt = updatePrompt;
	confirmMessage = "A database record with the same publication type, title, first author already exists.  Load saved information?";
	if ((pubMedId == null || pubMedId == 0) && doi == ""
			&& (uri == null || uri == "")) {
		waitCursor();
		PublicationManager.updateWithExistingNonPubMedDOIPublication(category,
				title, firstAuthor, populateAllFields);
	}
}

function populateAllFields(publication) {
	if (publication != null) {
		if (updateFormPrompt == "true" && confirm(confirmMessage)) {
			dwr.util.setValue("category", publication.domainFile.category);
			dwr.util.setValue("status", publication.domainFile.status);
			dwr.util.setValues(publication, {
				escapeHtml : false
			});
			// load again for special characters
			dwr.util.setValue("keywordsStr", publication.keywordsStr, {
				escapeHtml : false
			});
			dwr.util.setValue("domainFile.description",
					publication.domainFile.description, {
						escapeHtml : false
					});
			dwr.util.setValue("associatedSampleNames",
					publication.sampleNamesStr, {
						escapeHtml : false
					});
			// dwr.util.setValue("visibilityGroups",
			// publication.visibilityGroups);
			// dwr.util.setValue("researchAreas", publication.researchAreas);
			currentPublication = publication;
			// if there's a valid PubMed ID disable PubMed related fields
			if (publication.domainFile.pubMedId != null
					&& publication.domainFile.pubMedId != 0) {
				populateAuthors(true);
				disableAutoFields();
			} else {
				populateAuthors(false);
				enableAutoFields();
			}

			showExisitingFileInfo(publication);
		}
	}
	updateFormPrompt = "false";
	confirmMessage = "";
	hideCursor();
}

function showExisitingFileInfo(publication) {
	dwr.util.setValue("uploadedFileField", "");
	dwr.util.setValue("externalUrlField", "");
	if (publication.domainFile.uri != null) {
		dwr.util.setValue("existingFileInfo",
				"Submitted publication <a href='publication.do?dispatch=download&amp;fileId="
						+ publication.domainFile.id + "&amp;location="
						+ appOwner + "' target=" + publication.urlTarget + ">"
						+ publication.domainFile.uri + "</a>", {
					escapeHtml : false
				});
	}
	show("existingFileInfo");
	hide("existingFileInfoFromUpdate");
}

function hideExistingFileInfo() {
	hide("existingFileInfo");
}

function populateAuthorInfo(publication) {
	if (publication != null) {
		currentPublication = publication;
		populateAuthors(false);
	} else {
		sessionTimeout();
	}
}
function populateAuthors(hideEdit) {
	var authors = currentPublication.authors;
	dwr.util.removeAllRows("authorRows", {
		filter : function(tr) {
			return (tr.id != "pattern" && tr.id != "patternHeader");
		}
	});
	var author, id;
	if (authors.length > 0) {
		show("authorTable");
	} else {
		hide("authorTable");
	}
	for ( var i = 0; i < authors.length; i++) {
		author = authors[i];
		if (author.id == null || author.id == "") {
			author.id = -1000 - numberOfAuthors;
		}
		id = author.id;
		dwr.util.cloneNode("pattern", {
			idSuffix : id
		});
		dwr.util.setValue("id" + id, author.id);
		dwr.util.setValue("firstNameValue" + id, author.firstName);
		dwr.util.setValue("lastNameValue" + id, author.lastName);
		dwr.util.setValue("initialsValue" + id, author.initial);
		$("pattern" + id).style.display = "";
		if (authorCache[id] == null) {
			numberOfAuthors++;
		}
		authorCache[id] = author;
		if (hideEdit == true) {
			hide("edit" + id);
		}
	}
}
function addAuthor() {
	var author = {
		id : null,
		firstName : null,
		lastName : null,
		initial : null
	};
	dwr.util.getValues(author);
	if (author.id == null || author.id.length == 0) {
		author.id = -1000 - numberOfAuthors;
	}
	if (author.firstName != "" || author.lastName != "" || author.initial != "") {
		PublicationManager.addAuthor(author, function(publication) {
			if (publication == null) {
				sessionTimeout();
			}
			currentPublication = publication;
		});
		window.setTimeout("populateAuthors()", 200);
	} else {
		alert("Please fill in values");
	}
}
function clearAuthor() {
	var author = {
		id : null,
		firstName : "",
		lastName : "",
		initial : ""
	};
	dwr.util.setValues(author);
	hide("deleteAuthor");
}
function editAuthor(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var author = authorCache[eleid.substring(4)];
	dwr.util.setValues(author);
	// document.getElementById("manufacturer").focus(); this doesn't work in IE
	show("deleteAuthor");
}
function deleteTheAuthor() {
	var eleid = document.getElementById("id").value;
	// we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	// var author = authorCache[eleid.substring(6)];
	if (eleid != null && eleid != NaN) {
		var author = authorCache[eleid];
		if (confirm("Are you sure you want to delete '" + author.firstName
				+ " " + author.lastName + "'?")) {
			PublicationManager.deleteAuthor(author, function(publication) {
				if (publication == null) {
					sessionTimeout();
				}
				currentPublication = publication;
			});
			window.setTimeout("populateAuthors()", 200);
			closeSubmissionForm("Author");
		}
	}
}

function gotoUpdatePage(publication) {
	var form = document.forms[0];
	form.action = "publication.do?dispatch=setupUpdate&page=0&publicationId="
			+ publication.domainFile.id;
	form.submit();
}

function gotoSubmitNewPage() {
	var form = document.forms[0];
	form.action = "publication.do?dispatch=setupNew&page=0";
	form.submit();
}

function gotoInputPage() {
	var form = document.forms[0];
	form.action = "publication.do?dispatch=input&page=0";
	form.submit();
}