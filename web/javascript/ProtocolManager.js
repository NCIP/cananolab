
var emptyOption = [{label:"", value:""}];
function retrieveProtocols() {
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolNames(protocolType, populateProtocolNames);
}
function resetProtocols() {
	dwr.util.removeAllOptions("protocolName");
	dwr.util.addOptions("protocolName", emptyOption, "value", "label");
	resetProtocolFiles();
}
function populateProtocolNames(protocolNames) {
	resetProtocols();
	dwr.util.addOptions("protocolName", protocolNames);
	dwr.util.addOptions("protocolName", ["[Other]"]);
}
function retrieveProtocolFileVersions() {
	var protocolName = document.getElementById("protocolName").value;
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolFiles(protocolType, protocolName, populateProtocolFileVersions);
}
function resetProtocolFiles() {
	dwr.util.removeAllOptions("protocolFileId");
	dwr.util.addOptions("protocolFileId", emptyOption, "value", "label");
	writeLink(null);
}
function populateProtocolFileVersions(protocolFiles) {
	resetProtocolFiles();
	dwr.util.addOptions("protocolFileId", protocolFiles, "domainFileId", "domainFileVersion");
	dwr.util.addOptions("protocolFileId", ["[Other]"]);
}
function retrieveProtocolFile() {
	var fileId = document.getElementById("protocolFileId").value;
	//ProtocolManager.findProtocolFileById(fileId, writeLink); //not working on linux
	ProtocolManager.getProtocolFileUriById(fileId, writeLink);
	if (document.getElementById("updatedUri") != null) {
		ProtocolManager.getProtocolFileUriById(fileId, function (data) {
			document.getElementById("updatedUri").value = data;
		});
	}
	if (document.getElementById("updatedName") != null) {
		ProtocolManager.getProtocolFileNameById(fileId, function (data) {
			document.getElementById("updatedName").value = data;
		});
	}
	if (document.getElementById("updatedVersion") != null) {
		ProtocolManager.getProtocolFileVersionById(fileId, function (data) {
			document.getElementById("updatedVersion").value = data;
		});
	}
}
function writeLink(uri) {
	var fileId = document.getElementById("protocolFileId").value;
	if (uri != null) {
		var fileUri = uri;
		if (fileUri != null) {
			document.getElementById("protocolFileLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;location=local&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
		} else {
			document.getElementById("protocolFileLink").innerHTML = "";
		}
	} else {
		document.getElementById("protocolFileLink").innerHTML = "";
	}
}
	
//not working on linux
function writeLink0(protocolFile) {
	var fileId = document.getElementById("protocolFileId").value;
	if (protocolFile != null) {
		var fileUri = protocolFile.domainUri;
		if (fileUri != null) {
			document.getElementById("protocolFileLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;location=local&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
		} else {
			document.getElementById("protocolFileLink").innerHTML = "";
		}
	} else {
		document.getElementById("protocolFileLink").innerHTML = "";
	}
}

function setProtocolNameDropdown() {
	var searchLocations = getSelectedOptions(document.getElementById("searchLocations"));
	ProtocolManager.getProtocolTypes(searchLocations, function (data) {
			dwr.util.removeAllOptions("protocolType");
			dwr.util.addOptions("protocolType", data);
		});
	
	return false;
}

