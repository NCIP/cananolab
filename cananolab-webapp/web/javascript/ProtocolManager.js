var emptyOption = [ {
	label : "",
	value : ""
} ];

function resetProtocolOnType() {
	dwr.util.setValue("protocolName", null);
	dwr.util.setValue("protocolVersion", null);
	clearProtocol();
}

function retrieveProtocol() {
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
	document.getElementById("uploadedUri").innerHTML = "";
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