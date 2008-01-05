
var editOption = [{label:"--?--", value:""}];
function retrieveProtocols() {
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolNames(protocolType, populateProtocolNames);
}
function resetProtocols() {
	dwr.util.removeAllOptions("protocolName");
	dwr.util.addOptions("protocolName", editOption, "value", "label");
	dwr.util.removeAllOptions("protocolId");
	dwr.util.addOptions("protocolId", editOption, "value", "label");
	writeLink(null);
}
function populateProtocolNames(protocolNames) {
    //get previous selection
	var selectedProtocolName = dwr.util.getValue("protocolName");
	//remove option that's the same as previous selection
	var updatedProtocolNames = new Array();
	if (protocolNames != null) {
		for (i = 0; i < protocolNames.length; i++) {
			if (protocolNames[i]!= selectedProtocolName) {
				updatedProtocolNames.push(protocolNames[i]);
			}
		}
	}
	dwr.util.addOptions("protocolName", updatedProtocolNames);
}
function retrieveProtocolFileVersions() {
	var protocolName = document.getElementById("protocolName").value;
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolFiles(protocolName, protocolType, populateProtocolFileVersions);
}
function resetProtocolFiles() {
	dwr.util.removeAllOptions("protocolId");
	dwr.util.addOptions("protocolId", editOption, "value", "label");
	writeLink(null);
}
function populateProtocolFileVersions(protocolFiles) {
	dwr.util.addOptions("protocolId", protocolFiles, "id", "version");
}
function retrieveProtocolFile() {
	var fileId = document.getElementById("protocolId").value;
	//ProtocolManager.getProtocolFileBean(fileId, writeLink); //not working on linux
	ProtocolManager.getProtocolFileUri(fileId, writeLink);
}

function writeLink(uri) {
	var fileId = document.getElementById("protocolId").value;
	if (uri != null) {
		var fileUri = uri;
		if (fileUri != null) {
			document.getElementById("protocolLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
		} else {
			document.getElementById("protocolLink").innerHTML = "";
		}
	} else {
		document.getElementById("protocolLink").innerHTML = "";
	}
}
	
//not working on linux
function writeLink0(protocolFile) {
	var fileId = document.getElementById("protocolId").value;
	if (protocolFile != null) {
		var fileUri = protocolFile.uri;
		if (fileUri != null) {
			document.getElementById("protocolLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;fileId=" + fileId + "'>" + fileUri + "</a>";
		} else {
			document.getElementById("protocolLink").innerHTML = "";
		}
	} else {
		document.getElementById("protocolLink").innerHTML = "";
	}
}

