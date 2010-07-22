function displayAccessNameLabel() {
	var byGroup = dwr.util.getValue("byGroup");
	var byUser = dwr.util.getValue("byUser");
	if (byGroup == true && byUser == false) {
		dwr.util.setValue("accessNameLabel", "Collaboration Group Name *");
		show("groupName");
		hide("userName");
	} else if (byGroup == false && byUser == true) {
		dwr.util.setValue("accessNameLabel", "User Login Name *");
		hide("groupName");
		show("userName");
	}
}

function setTheAccess(parentFormName, accessType, accessName, parentType,
		protectedData) {
	if (accessType == "group") {
		AccessibilityManager.getGroupAccess(parentFormName, accessName,
				parentType, protectedData, populateAccess);
	} else {
		AccessibilityManager.getUserAccess(parentFormName, accessName,
				parentType, protectedData, populateAccess);
	}
	show("deleteAccess");
	hide("newAccessLabel");
	openSubmissionForm("Access");
}

// Populate the Access form.
function populateAccess(access) {
	if (access != null) {
		if (access.groupAccess == true) {
			dwr.util.setValue("byGroup", true);
			dwr.util.setValue("groupName", access.groupName);
		} else {
			dwr.util.setValue("byUser", true);
			dwr.util.setValue("userName", access.userBean.loginName);
		}
		dwr.util.setValue("roleName", access.roleName);
	} else {
		sessionTimeout();
	}
}

function addAccess(actionName) {
	enableButtons( [ "submitButton", "resetButton" ]);
	submitAction(document.forms[0], actionName, "saveAccess", 4);
}

function clearAccess() {
	// go to server and clean form bean
	AccessibilityManager.resetTheAccess('sampleForm', 'sample', populateAccess);
	hide("deleteAccess");
	hide("newUser");
}

function deleteTheAccess() {
	var answer = confirmDelete("access");
	if (answer != 0) {
		submitAction(document.forms[0], "access", "delete", 2);
	}
}
