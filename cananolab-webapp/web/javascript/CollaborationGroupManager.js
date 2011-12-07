var userAccessCache = {};
var currentGroup = {};
var numberOfUserAccesses = 0; // number of user accesses in the cache,

function setTheCollaborationGroup(groupId) {
	numberOfUserAccesses = 0;
	CollaborationGroupManager.getCollaborationGroupById(groupId,
			populateCollaborationGroup);
	show("deleteCollaborationGroup");
	hide("newCollaborationGroupLabel");
	document.getElementById("")
	openSubmissionForm("CollaborationGroup");
	// Feature request [26487] Deeper Edit Links.
	// window.setTimeout("openOneUserAccess()", 500);
}
// Populate user submission form and auto open it for user.
function openOneUserAccess() {
	if (currentGroup != null && currentGroup.userAccesses.length == 1) {
		var userAccess = currentGroup.userAccesses[0];
		populateUserAccessForm(userAccess);
	} else {
		hide("newUser");
	}
}
// Populate the Collaboration Group form.
function populateCollaborationGroup(group) {
	if (group != null) {
		currentGroup = group;
		if (group.name != null) {
			dwr.util.setValue("groupName", group.name);
			dwr.util.setValue("groupDescription", group.description);
			dwr.util.setValue("groupId", group.id);
			if (group.ownerName != null) {
				show("userSection");
				show("userLabel");
			} else {
				hide("userSection");
				hide("userLabel");
			}
		}
		// clear the cache for each new collaborationGroup
		userAccessCache = {};
		populateUserAccesses(group.ownerName);
	} else {
		sessionTimeout();
	}
}
// Populate the user access table inside collaboration group form.
function populateUserAccesses(groupOwner) {
	var userAccesses = currentGroup.userAccesses;
	dwr.util.removeAllRows("userRows", {
		filter : function(tr) {
			return (tr.id != "pattern" && tr.id != "patternHeader");
		}
	});
	var userAccess, id;
	for ( var i = 0; i < userAccesses.length; i++) {
		userAccess = userAccesses[i];
		id = userAccess.userBean.loginName;
		dwr.util.cloneNode("pattern", {
			idSuffix : id
		});
		dwr.util.setValue("rowUserLoginName" + id,
				userAccess.userBean.loginName);
		dwr.util.setValue("rowRoleName" + id, userAccess.roleDisplayName);
		$("pattern" + id).style.display = "";
		if (userAccessCache[id] == null) {
			numberOfUserAccesses++;
		}
		userAccessCache[id] = userAccess;
		// hide edit link
		if (userAccess.userBean.loginName == groupOwner) {
			hide("edit" + id);
		} else {
			show("edit" + id);
		}
	}
	if (userAccesses.length > 0) {
		show("userTable");
	} else {
		hide("userTable");
	}
	closeSubmissionForm('User');
}

function clearCollaborationGroup() {
	// go to server and clean form bean
	CollaborationGroupManager
			.resetTheCollaborationGroup(populateCollaborationGroup);
	hide("deleteCollaborationGroup");
	hide("newUser");
	numberOfUserAccesses = 0;
}

function deleteTheCollaborationGroup() {
	var answer = confirmDelete("collaboration group");
	if (answer != 0) {
		submitAction(document.forms[0], "collaborationGroup", "delete", 2);
	}
}

function addUserAccess() {
	var userBean = {
		loginName : null
	};
	var userAccess = {
		userBean : userBean,
		roleName : null,
		accessBy : "user"
	};

	dwr.util.getValues(userAccess);
	if (userAccess.userBean.loginName != "" && userAccess.roleName != "") {
		CollaborationGroupManager
				.addUserAccess(
						userAccess,
						function(group) {
							if (group == null) {
								sessionTimeout();
							}
							if (group.name == "!!invalid user") {
								alert("The entered user login name is invalid. ");
								return false;
							} else if (group.name == "!!user is a curator") {
								alert("The entered user is a curator.  Curators already have full access to all data.");
								return false;
							}
							currentGroup = group;
						});
		window.setTimeout("populateUserAccesses('" + currentGroup.ownerName
				+ "')", 200);
		return true;
	} else {
		alert("Please fill in both fields.");
		return false;
	}
}

function clearUserAccess() {
	dwr.util.setValue("userBean.loginName", "");
	dwr.util.setValue("roleName", "");
	hide("deleteUser");
	disableOuterButtons();
}

function editUserAccess(userLoginName) {
	var userAccess = userAccessCache[userLoginName.substring(4)];
	populateUserAccessForm(userAccess);
	disableOuterButtons();
}

function populateUserAccessForm(userAccess) {
	dwr.util.setValues(userAccess);
	show("deleteUser");
	show("newUser");
}
function deleteTheUserAccess() {
	var userLoginName = dwr.util.getValue("userBean.loginName");
	if (userLoginName != "") {
		var userAccess = userAccessCache[userLoginName];
		if (confirm("Are you sure you want to delete access for '"
				+ userLoginName + "'?")) {
			CollaborationGroupManager.deleteUserAccess(userAccess, function(
					group) {
				if (group == null) {
					sessionTimeout();
				}
				currentGroup = group;
			});
			window.setTimeout("populateUserAccesses()", 200);
			hide("newUser");
		}
	}
	enableOuterButtons();
}

function showMatchedUserDropdown() {
	// display progress.gif while waiting for the response.
	show("loaderImg");
	hide("matchedUserNameSelect");
	hide("cancelBrowse");
	var selected = dwr.util.getValue("matchedUserNameSelect");
	var loginName = dwr.util.getValue("userBean.loginName");
	if (currentGroup.ownerName == null) {
		ownerName = ""
	} else {
		ownerName = currentGroup.ownerName;
	}
	CollaborationGroupManager.getMatchedUsers(ownerName, loginName, function(
			data) {
		dwr.util.removeAllOptions("matchedUserNameSelect");
		dwr.util.addOptions("matchedUserNameSelect", data, "loginName",
				"displayName");
		dwr.util.setValue("matchedUserNameSelect", selected);
		hide("loaderImg");
		show("matchedUserNameSelect");
		show("cancelBrowse");
	});
}

function updateUserLoginName() {
	var selected = dwr.util.getValue("matchedUserNameSelect");
	dwr.util.setValue("userBean.loginName", selected);
	hide("matchedUserNameSelect");
	hide("cancelBrowse");
}

function cancelBrowseSelect() {
	hide("matchedUserNameSelect");
	hide("cancelBrowse");
}
