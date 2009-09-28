
var emptyOption = [{label:"", value:""}];
function retrieveProtocols() {
	var protocolType = document.getElementById("protocolType").value;
	ProtocolManager.getProtocols(protocolType, populateProtocols);
}
function resetProtocols() {
	dwr.util.removeAllOptions("protocolName");
	dwr.util.removeAllOptions("protocolVersion");
	dwr.util.addOptions("protocolName", emptyOption, "value", "label");
	dwr.util.addOptions("protocolVersion", emptyOption, "value", "label");
	populateProtocol(null);
}
function populateProtocols(protocols) {
	resetProtocols();
	if (protocols != null) {
		var protocolNames = new Array();
		var protocolVersions = new Array();
		for (i = 0; i < protocols.length; i++) {
			protocolNames[i] = protocols[i].domain.name;
			protocolVersions[i] = protocols[i].domain.version;
		}
		dwr.util.addOptions("protocolName", protocolNames);
		dwr.util.addOptions("protocolName", ["[other]"]);
		dwr.util.addOptions("protocolVersion", protocolVersions);
		dwr.util.addOptions("protocolVersion", ["[other]"]);
	} else {
		dwr.util.addOptions("protocolName", ["[other]"]);
		dwr.util.addOptions("protocolVersion", ["[other]"]);
	}
}
function retrieveProtocol() {
	var protocolType = document.getElementById("protocolType").value;
	var protocolName = document.getElementById("protocolName").value;
	var protocolVersion = document.getElementById("protocolVersion").value;
	ProtocolManager.getProtocol(protocolType, protocolName, protocolVersion, populateProtocol);
}
function populateProtocol(protocol) {
	if (protocol == null) {
		writeLink(null);
		dwr.util.setValue("fileTitle", "");
		dwr.util.setValue("fileDescription", "");
		dwr.util.setValue("visibility", [""]);
		dwr.util.setValue("protocolAbbreviation", "");
		return;
	}
	dwr.util.setValue("protocolId", protocol.domain.id);
	dwr.util.setValue("protocolAbbreviation", protocol.domain.abbreviation);
	dwr.util.setValue("visibility", protocol.visibilityGroups);
	if (protocol.fileBean != null) {
		dwr.util.setValue("fileTitle", protocol.fileBean.domainFile.title);
		dwr.util.setValue("fileDescription", protocol.fileBean.domainFile.description);
		writeLink(protocol);
	}
}
function writeLink(protocol) {
	if (protocol == null) {
		document.getElementById("protocolFileLink").innerHTML = "";
		return;
	}
	var uri = null;
	var fileId = null;
	if (protocol.fileBean != null) {
		uri = protocol.fileBean.domainFile.uri;
		fileId = protocol.fileBean.domainFile.id;
	}
	if (uri != null && fileId != null) {
		document.getElementById("protocolFileLink").innerHTML = "<a href='searchProtocol.do?dispatch=download&amp;location=${applicationOwner}&amp;fileId=" + fileId + "'>" + uri + "</a>";
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

