
var editOption = [{label:"--?--", value:""}];
function retrieveProtocols() {
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolBeans(protocolType, populateProtocols);
}
function resetProtocols() {
	dwr.util.removeAllOptions("protocolName");
	dwr.util.addOptions("protocolName", editOption, "value", "label");
	dwr.util.removeAllOptions("protocolId");
	dwr.util.addOptions("protocolId", editOption, "value", "label");
	writeLink(null);
}
function populateProtocols(protocols) {
    //get previous selection
	var selectedProtocolName = dwr.util.getValue("protocolName");
	//remove option that's the same as previous selection
	var updatedProtocols = new Array();
	if (protocols != null) {
		for (i = 0; i < protocols.length; i++) {
			if (protocols[i].name != selectedProtocolName) {
				updatedProtocols.push(protocols[i]);
			}
		}
	}
	dwr.util.addOptions("protocolName", updatedProtocols, "name");
}
function retrieveProtocolFileVersions() {
	var protocolName = document.getElementById("protocolName").value;
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocolFileBeans(protocolName, protocolType, populateProtocolFileVersions);
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
	ProtocolManager.getProtocolFileBean(fileId, writeLink);
}
function writeLink(protocolFile) {
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

