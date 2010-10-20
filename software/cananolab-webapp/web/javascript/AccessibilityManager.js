var publicGroup = "Public";
var readRoleLabel = "read";
var readRoleValue = "R";

function displayAccessNameLabel() {
	var byGroup = dwr.util.getValue("byGroup");
	var byUser = dwr.util.getValue("byUser");
	var byPublic = dwr.util.getValue("byPublic");
	enablePublicAccess(false);
	dwr.util.setValue("groupName", "");
	dwr.util.setValue("userName", "");
	dwr.util.setValue("roleName", "");
	show("browseIcon");
	if (byGroup == true && byUser == false && byPublic == false) {
		dwr.util.setValue("accessNameLabel", "Collaboration Group Name *");
		show("groupName");
		hide("userName");
	} else if (byGroup == false && byUser == true && byPublic == false) {
		dwr.util.setValue("accessNameLabel", "User Login Name *");
		hide("groupName");
		show("userName");
	} else if (byPublic == true && byGroup == false && byUser == false) {
		dwr.util.setValue("accessNameLabel", "Group Name");
		show("groupName");
		dwr.util.setValue("groupName", publicGroup);
		dwr.util.setValue("roleName", readRoleLabel);
		dwr.util.setValue("hiddenRoleName", readRoleValue);
		dwr.util.setValue("hiddenGroupName", publicGroup);
		enablePublicAccess(true);
		hide("userName");
		hide("browseIcon");
	}
}

function toggleAccessNameLabel() {
	var byGroup = dwr.util.getValue("byGroup");
	var byUser = dwr.util.getValue("byUser");
	var byPublic = dwr.util.getValue("byPublic");
	enablePublicAccess(false);
	show("browseIcon");
	if (byGroup == true && byUser == false && byPublic == false) {
		dwr.util.setValue("accessNameLabel", "Collaboration Group Name *");
		show("groupName");
		hide("userName");
	} else if (byGroup == false && byUser == true && byPublic == false) {
		dwr.util.setValue("accessNameLabel", "User Login Name *");
		hide("groupName");
		show("userName");
	} else if (byPublic == true && byGroup == false && byUser == false) {
		dwr.util.setValue("accessNameLabel", "Group Name");
		show("groupName");
		dwr.util.setValue("groupName", publicGroup);
		dwr.util.setValue("roleName", readRoleLabel);
		dwr.util.setValue("hiddenRoleName", readRoleValue);
		dwr.util.setValue("hiddenGroupName", publicGroup);
		enablePublicAccess(true);
		hide("userName");
		hide("browseIcon");
	}
}

function enablePublicAccess(enabled) {
	if (enabled) {
		document.getElementById("groupName").disabled = true;
		document.getElementById("roleName").disabled = true;
		dwr.util.setValue("accessNameLabel", "Group Name *");
	} else {
		document.getElementById("groupName").disabled = false;
		document.getElementById("roleName").disabled = false;
		dwr.util.setValue("accessNameLabel", "Collaboration Group Name *");
	}
}

function enableAccessBy(enabled) {
	if (enabled) {
		document.getElementById("byGroup").disabled = false;
		document.getElementById("byUser").disabled = false;
		if (document.getElementById("byPublic") != null)
			document.getElementById("byPublic").disabled = false;
	} else {
		document.getElementById("byGroup").disabled = true;
		document.getElementById("byUser").disabled = true;
		if (document.getElementById("byPublic") != null)
			document.getElementById("byPublic").disabled = true;
	}
}
function setTheAccess(parentFormName, accessType, accessName, parentType,
		protectedData) {
	enableAccessBy(false);
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
	enablePublicAccess(false);
	if (access != null) {
		if (access.accessBy == "group") {
			dwr.util.setValue("byGroup", true);
			dwr.util.setValue("groupName", access.groupName);
			show("groupName");
			hide("userName");
		} else if (access.accessBy == "public") {
			dwr.util.setValue("byPublic", true);
			dwr.util.setValue("groupName", access.groupName);
			enablePublicAccess(true);
			dwr.util.setValue("hiddenRoleName", readRoleValue);
			dwr.util.setValue("hiddenGroupName", publicGroup);
			show("groupName");
			hide("userName");
			hide("browseIcon");
		} else {
			dwr.util.setValue("byUser", true);
			dwr.util.setValue("userName", access.userBean.loginName);
			dwr.util.setValue("accessNameLabel", "User Login Name *");
			show("userName");
			hide("groupName");
		}
		dwr.util.setValue("roleName", access.roleName);
	} else {
		sessionTimeout();
	}
}

function addAccess(actionName, page, isPublic) {
	if (isPublic) {
		if (confirm("Data is already public.  Assigning a different accessibility will retract it from public.  Continue?")) {
			enableOuterButtons();
			submitAction(document.forms[0], actionName, "saveAccess", page);
		}
	} else {
		enableOuterButtons();
		submitAction(document.forms[0], actionName, "saveAccess", page);
	}
}

function clearAccess(parentFormName, parentType) {
	// go to server and clean form bean
	AccessibilityManager.resetTheAccess(parentFormName, parentType,
			populateAccess);
	hide("deleteAccess");
	hide("newUser");
	enableAccessBy(true);
}

function deleteTheAccess(actionName, page) {
	var answer = confirmDelete("access");
	if (answer != 0) {
		submitAction(document.forms[0], actionName, "deleteAccess", page);
	}
	enableOuterButtons();
}

function showMatchedGroupOrUserDropdown(dataOwner) {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedUserNameSelect");
	hide("matchedGroupNameSelect");
	hide("cancelBrowse");
	var byGroup = dwr.util.getValue("byGroup");
	var byUser = dwr.util.getValue("byUser");
	if (byGroup == true && byUser == false) {
		var selectedGroup = dwr.util.getValue("matchedGroupNameSelect");
		var groupName = dwr.util.getValue("groupName");
		AccessibilityManager.getMatchedGroupNames(loginName, function(data) {
			if (data.length == 0) {
				alert("No matching collaboration groups found.  Please create a collaboration group first.");
				hide("loaderImg");
				dwr.util.setValue("groupName", "");
				return;
			}
			dwr.util.removeAllOptions("matchedGroupNameSelect");
			dwr.util.addOptions("matchedGroupNameSelect", data);
			dwr.util.setValue("matchedGroupNameSelect", selectedGroup);
			hide("loaderImg");
			show("matchedGroupNameSelect");
			show("cancelBrowse");
		});
	} else {
		var selectedUser = dwr.util.getValue("matchedUserNameSelect");
		var loginName = dwr.util.getValue("userName");
		AccessibilityManager.getMatchedUsers(dataOwner, loginName, function(data) {
			if (data.length == 0) {
				alert("no matching users found. ");
				hide("loaderImg");
				dwr.util.setValue("userName", "");
				return;
			}
			dwr.util.removeAllOptions("matchedUserNameSelect");
			dwr.util.addOptions("matchedUserNameSelect", data, "loginName",
					"fullName");
			dwr.util.setValue("matchedUserNameSelect", selectedUser);
			hide("loaderImg");
			show("matchedUserNameSelect");
			show("cancelBrowse");
		});
	}
}

function cancelBrowseSelect() {
	hide("matchedUserNameSelect");
	hide("matchedGroupNameSelect");
	hide("cancelBrowse");
}

function updateUserLoginName() {
	var selected = dwr.util.getValue("matchedUserNameSelect");
	dwr.util.setValue("userName", selected);
	hide("matchedUserNameSelect");
	hide("cancelBrowse");
}

function updateGroupName() {
	var selected = dwr.util.getValue("matchedGroupNameSelect");
	dwr.util.setValue("groupName", selected);
	hide("matchedGroupNameSelect");
	hide("cancelBrowse");
}
