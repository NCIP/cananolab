var authorCache = {};
var currentPublication = null;
var numberOfAuthors=0; //number of unique authors in the cache, used to generate author id

function updateFormBasedOnCategory() {
	var category=dwr.util.getValue("domainFile.category");
	if (category!="report" && category!="book chapter" && category!="") {
		show("pubMedRow", true);
		show("doiRow", true);
		show("journalRow", true);		
		show("volumePageRow", true);
	}
	else {
		hide("pubMedRow");
		hide("doiRow");
		hide("journalRow");
		hide("volumePageRow");
		dwr.util.setValue("domainFile.status", true);
	}
	//if report, set publish status to published
	if (category=="report") {
		dwr.util.setValue("domainFile.status", "published");
	}
}

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

function fillPubMedInfo() {
	var pubMedId=dwr.util.getValue("domainFile.pubMedId");
	PublicationManager.retrievePubMedInfo(pubMedId, populatePubMedInfo);	
}

function populatePubMedInfo(publication) {
	if (publication!=null) {		
		dwr.util.setValues(publication);
		document.getElementById("domainFile.digitalObjectId").readOnly=true;
		document.getElementById("domainFile.title").readOnly=true;
		document.getElementById("domainFile.journalName").readOnly=true;
		document.getElementById("domainFile.year").readOnly=true;
		document.getElementById("domainFile.volume").readOnly=true;
		document.getElementById("domainFile.startPage").readOnly=true;
		document.getElementById("domainFile.endPage").readOnly=true;
		currentPublication=publication;
		populateAuthors(true);
		hide("addAuthor");		
	}
	else {
		sessionTimeout();
	}
}

function populateAuthors(hideEdit) {
	var authors = currentPublication.authors;
	dwr.util.removeAllRows("authorRows", {filter:function (tr) {
		return (tr.id != "pattern" && tr.id != "patternHeader");
	}});
	var author, id;
	if (authors.length > 0) {		
		show("authorTable");
	} else {
		hide("authorTable");
	}
	for (var i = 0; i < authors.length; i++) {
		author = authors[i];
		if (author.id == null || author.id == "") {
			author.id = -1000 - numberOfAuthors;
		}
		id = author.id;
		dwr.util.cloneNode("pattern", {idSuffix:id});
		dwr.util.setValue("id" + id, author.id);
		dwr.util.setValue("firstNameValue" + id, author.firstName);
		dwr.util.setValue("lastNameValue" + id, author.lastName);
		dwr.util.setValue("initialsValue" + id, author.initial);
		$("pattern" + id).style.display = "";
		if (authorCache[id] == null) {
			numberOfAuthors++;
		}
		authorCache[id] = author;
		if (hideEdit==true) {
			hide("edit"+id);
		}
	}
}

function addAuthor() {
	var author = {id: null, firstName:null, lastName:null, initial:null};
	dwr.util.getValues(author);
	if (author.id == null || author.id == "") {
		author.id = -1000 - numberOfAuthors;
	}
	
	if (author.firstName != "" || author.lastName != "" || author.initial != "") {
		PublicationManager.addAuthor(author, function (publication) {
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
	var author={id: null, firstName:"", lastName:"", initial:""};
	dwr.util.setValues(author);
	hide("deleteAuthor");
}
function editAuthor(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var author = authorCache[eleid.substring(4)];
	dwr.util.setValues(author);
	//document.getElementById("manufacturer").focus(); this doesn't work in IE
	show("deleteAuthor");
}
function deleteTheAuthor() {
	var eleid = document.getElementById("id").value;
	// we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	//var author = authorCache[eleid.substring(6)];
	if (eleid != "") {
		var author = authorCache[eleid];
		if (confirm("Are you sure you want to delete '" + author.firstName + " " + author.lastName + "'?")) {
			PublicationManager.deleteAuthor(author, function (publication) {
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

