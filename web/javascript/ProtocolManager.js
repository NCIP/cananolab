
var emptyOption = [{label:"", value:""}];
function retrieveProtocols() {
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.findProtocolNames(protocolType, populateProtocolNames);
}
function resetProtocols() {
	dwr.util.removeAllOptions("protocolName");
	dwr.util.addOptions("protocolName", emptyOption, "value", "label");
	dwr.util.removeAllOptions("protocolFileId");
	dwr.util.addOptions("protocolFileId", emptyOption, "value", "label");
	writeLink(null);
}
function populateProtocolNames(protocolNames) {
	dwr.util.addOptions("protocolName", protocolNames);
	dwr.util.addOptions("protocolName", ["[Other]"]);
}
function retrieveProtocolFileVersions() {
	var protocolName = document.getElementById("protocolName").value;
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.findProtocolFilesBy(protocolType, null, protocolName, populateProtocolFileVersions);
}
function resetProtocolFiles() {
	dwr.util.removeAllOptions("protocolFileId");
	dwr.util.addOptions("protocolFileId", emptyOption, "value", "label");
	writeLink(null);
}
function populateProtocolFileVersions(protocolFiles) {
	dwr.util.addOptions("protocolFileId", protocolFiles, "domainFileId", "domainFileVersion");
	dwr.util.addOptions("protocolFileId", ["[Other]"]);
}
function retrieveProtocolFile() {
	var fileId = document.getElementById("protocolFileId").value;
	//ProtocolManager.findProtocolFileById(fileId, writeLink); //not working on linux
	ProtocolManager.findProtocolFileUriById(fileId, writeLink);
}
function writeLink(uri) {
	var fileId = document.getElementById("protocolFileId").value;
	if (uri != null) {
		var fileUri = uri;
		if (fileUri != null) {
			document.getElementById("protocolFileLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
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
		var fileUri = protocolFile.uri;
		if (fileUri != null) {
			document.getElementById("protocolFileLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
		} else {
			document.getElementById("protocolFileLink").innerHTML = "";
		}
	} else {
		document.getElementById("protocolFileLink").innerHTML = "";
	}
}

