function displayAccessNameLabel() {
	var byGroup = document.getElementById("accessByGroup");
	var byUser = document.getElementById("accessByUser");
	if (byGroup.checked && !byUser.checked) {
		dwr.util.setValue("accessNameLabel", "Collaboration Group Name");
		show("groupName");
		hide("userName");
	} else {
		dwr.util.setValue("accessNameLabel", "User Login Name");
		hide("groupName");
		show("userName");
	}
}

function setTheAccess(accessId) {
	AccessManager.getAccessById(accessId, populateAccess);
	show("deleteAccess");
	hide("newAccessLabel");
	openSubmissionForm("Access");
}

// Populate the Access form.
function populateAccess(access) {
	if (access != null) {
		if (access.name != null) {
			dwr.util.setValue("accessName", access.name);
			dwr.util.setValue("accessDescription", access.description);
			dwr.util.setValue("accessId", access.id);
		}
	} else {
		sessionTimeout();
	}
}

function clearAccess() {
	// go to server and clean form bean
	AccessManager.resetTheAccess(populateAccess);
	hide("deleteAccess");
	hide("newUser");
	numberOfUserAccesses = 0;
}

function deleteTheAccess() {
	var answer = confirmDelete("access");
	if (answer != 0) {
		submitAction(document.forms[0], "access", "delete", 2);
	}
}
