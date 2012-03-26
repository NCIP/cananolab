var emptyOption = [ {
	label : "",
	value : ""
} ];

function resetProtocolOnType() {
	dwr.util.setValue("protocolName", null);
	dwr.util.setValue("protocolVersion", null);
	clearProtocol();
}

var appOwner;
function retrieveProtocol(applicationOwner) {
	appOwner = applicationOwner;
	var protocolType = document.getElementById("protocolType").value;
	var protocolName = document.getElementById("protocolName").value;
	waitCursor();
	var protocolVersion = document.getElementById("protocolVersion").value;
	ProtocolManager.getProtocol(protocolType, protocolName, protocolVersion,
			populateProtocol);
}

function clearProtocol() {
	enableOuterButtons();
	show("addAccess");
	show("addAccessLabel");
	writeLink(null);
	dwr.util.setValue("protocolId", null);
	dwr.util.setValue("fileId", null);
	dwr.util.setValue("fileUri", null);
	dwr.util.setValue("fileName", null);
	dwr.util.setValue("fileTitle", "");
	dwr.util.setValue("fileDescription", "");
	dwr.util.setValue("protocolAbbreviation", "");
}

function populateProtocol(protocol) {
	if (protocol == null) {
		hideCursor();
		//clearProtocol();
		// gotoInputPage();
		return;
	}
	// has existing protocol in database
	if (protocol !== null) {
		var confirmMessage = "A database record with the same protocol type and protocol name already exists.  Load it and update?";
		if (confirm(confirmMessage)) {
			waitCursor();
			// reload the page
			if (protocol.userUpdatable == true) {
				gotoUpdatePage(protocol);
			} else {
				alert("The protocol already exists and you don't have update and delete privilege on this protocol");
				disableOuterButtons();
				hide("addAccess");
				hide("addAccessLabel");
			}
		}
		else {
			hideCursor();
			return;
		}
	}
}

function writeLink(protocol) {
	if (protocol == null) {
		document.getElementById("uploadedUri").innerHTML = "";
		return;
	}
	var uri = null;
	var fileId = null;
	if (protocol.fileBean != null) {
		uri = protocol.fileBean.domainFile.uri;
		fileId = protocol.fileBean.domainFile.id;
	}
	if (uri != null && fileId != null) {
		document.getElementById("uploadedUri").innerHTML = "<a href='protocol.do?dispatch=download&amp;location="
				+ appOwner + "&amp;fileId=" + fileId + "'>" + uri + "</a>";
	} else {
		document.getElementById("uploadedUri").innerHTML = "";
	}
}

function gotoUpdatePage(protocol) {
	var form = document.getElementById("protocolForm");
	form.action = "protocol.do?dispatch=setupUpdate&page=0&protocolId="
			+ protocol.domain.id;
	form.submit();
}

function NewPage() {
	var form = document.getElementById("protocolForm");
	form.action = "protocol.do?dispatch=setupNew&page=0";
	form.submit();
}

function gotoInputPage() {
	var form = document.getElementById("protocolForm");
	form.action = "protocol.do?dispatch=input&page=0";
	form.submit();
}