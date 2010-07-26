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
			dwr.util.setValue("accessNameLabel", "User Login Name *");
		}
		displayAccessNameLabel();
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
		submitAction(document.forms[0], "sample", "deleteAccess", 2);
	}
}

function confirmRemovePublicAccess() {
	var answer = confirm("Are you sure you want to remove the data from Public access?");
	if (answer != 0) {
		submitAction(document.forms[0], "sample", "deletePublicAccess", 2);
	}
}

function showMatchedGroupOrUserDropdown() {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedUserNameSelect");
	hide("matchedGroupNameSelect");
	var byGroup = dwr.util.getValue("byGroup");
	var byUser = dwr.util.getValue("byUser");
	if (byGroup == true && byUser == false) {
		var selectedGroup = dwr.util.getValue("matchedGroupNameSelect");
		var groupName = dwr.util.getValue("groupName");
		AccessibilityManager.getMatchedGroupNames(loginName, function(data) {
			dwr.util.removeAllOptions("matchedGroupNameSelect");
			dwr.util.addOptions("matchedGroupNameSelect", data);
			dwr.util.setValue("matchedGroupNameSelect", selectedGroup);
			hide("loaderImg");
			show("matchedGroupNameSelect");
		});
	} else {
		var selectedUser = dwr.util.getValue("matchedUserNameSelect");
		var loginName = dwr.util.getValue("userName");
		AccessibilityManager.getMatchedUsers(loginName, function(data) {
			dwr.util.removeAllOptions("matchedUserNameSelect");
			dwr.util.addOptions("matchedUserNameSelect", data, "loginName",
					"fullName");
			dwr.util.setValue("matchedUserNameSelect", selectedUser);
			hide("loaderImg");
			show("matchedUserNameSelect");
		});
	}
}

function updateUserLoginName() {
	var selected = dwr.util.getValue("matchedUserNameSelect");
	dwr.util.setValue("userName", selected);
	hide("matchedUserNameSelect");
}

function updateGroupName() {
	var selected = dwr.util.getValue("matchedGroupNameSelect");
	dwr.util.setValue("groupName", selected);
	hide("matchedGroupNameSelect");
}
