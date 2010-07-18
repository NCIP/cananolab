var userAccessCache = null;
var currentGroup = null;
var numberOfUserAccesses = 0; // number of user accesses in the cache,

function setTheCollaborationGroup(groupId) {
	numberOfUserAccesses = 0;
	CollaborationGroupManager.getCollaborationGroupById(groupId,
			populateCollaborationGroup);
	show("deleteCollaborationGroup");
	openSubmissionForm("CollaborationGroups");
	// Feature request [26487] Deeper Edit Links.
	window.setTimeout("openOneUserAccess()", 200);
}
// Populate Instrument submission form and auto open it for user.
function openOneUserAccess() {
	if (currentExperimentConfig != null
			&& currentExperimentConfig.instruments.length == 1) {
		var instrument = currentExperimentConfig.instruments[0];
		populateInstrumentForm(instrument);
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
		}
		// clear the cache for each new collaborationGroup
		userAccessCache = {};
		populateUserAccesses();
	} else {
		sessionTimeout();
	}
}
// Populate the user access table inside collaboration group form.
function populateUserAccesses() {
	var userAccesses = currentGroup.userAccessibilities;
	dwr.util.removeAllRows("userRows", {
		filter : function(tr) {
			return (tr.id != "pattern" && tr.id != "patternHeader");
		}
	});
	var userAccess, id;
	if (userAccess.length > 0) {
		show("userTable");
	} else {
		hide("userTable");
	}
	for ( var i = 0; i < userAccesses.length; i++) {
		userAccess = userAccesses[i];
		id = userAccess.user.loginName;
		dwr.util.cloneNode("pattern", {
			idSuffix : id
		});
		dwr.util.setValue("userLoginName" + id, userAccess.user.loginName);
		dwr.util.setValue("roleName" + id, userAccess.roleName);
		$("pattern" + id).style.display = "";
		if (userAccessCache[id] == null) {
			numberOfUserAccesses++;
		}
		userAccessCache[id] = userAccess;
	}
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
		submitAction(document.forms[0], "collaborationGroup",
				"deleteCollaborationGroup", 2);
	}
}

function addUserAccess() {
	var user = {
		loginName : dwr.util.getValue("userLoginName")
	};
	var userAccess = {
		user : user,
		roleName : dwr.util.getValue("roleName")
	};
//	dwr.util.getValues(userAccess);
	if (userAccess.user.loginName != "" || userAccess.roleName != "") {
		CollaborationGroupManager.addUserAccess(userAccess, function(group) {
			if (group == null) {
				// sessionTimeout();
			}
			currentGroup = group;
		});
		window.setTimeout("populateUserAccesses()", 200);
		return true;
	} else {
		alert("Please fill in values");
		return false;
	}
}
function clearUserAccess() {
	dwr.util.setValue("userLoginName") = "";
	dwr.util.setValue("roleName") = "";
	hide("deleteUser");
}
function editUserAccess(userLoginName) {
	var userAccess = userAccessCache[userLoginName];
	populateUserAccessForm(userAccess);
}
function populateUserAccessForm(userAccess) {
	dwr.util.setValues(userAccess);
	show("deleteUser");
	show("newUser");
}
function deleteTheUserAccess() {
	var userLoginName = dwr.util.getValue("userLoginName");
	if (userLoginName != "") {
		var userAccess = userAccessCache[userLoginName];
		if (confirm("Are you sure you want to delete access for '"
				+ userLoginName + "'?")) {
			CollaborationGroupManager.deleteUserAccess(user, function(group) {
				if (group == null) {
					sessionTimeout();
				}
				currentGroup = group;
			});
			window.setTimeout("populateUserAccesses()", 200);
			hide("newUser");
		}
	}
}