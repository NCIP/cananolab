
/* set the submit point of contact form */
function clearPointOfContact() {
	//go to server and clean form bean
	POCManager.resetThePointOfContact(populatePointOfContact);
	hide("deletePointOfContact");
}
function setThePointOfContact(id, isPrimary) {
	POCManager.getPointOfContactById(id, isPrimary, populatePointOfContact);
	show("newPointOfContact");
	show("deletePointOfContact");
}
function populatePointOfContact(poc) {
	if (poc != null) {
		dwr.util.setValues(poc);
		if (poc.domain.id != null) {
			show("deletePointOfContact");
		}
		if (poc.primaryStatus == true) {
			show("primaryTitle");
			hide("secondaryTitle");
		} else {
			hide("primaryTitle");
			show("secondaryTitle");
		}
	} else {
		sessionTimeout();
	}
}
function addPointOfContact(actionName) {
	submitAction(document.forms[0], actionName, "savePointOfContact", 2);
}
function removePointOfContact(actionName) {
	submitAction(document.forms[0], actionName, "removePointOfContact", 1);
}
function updateOrganizationInfo() {
	var orgName = dwr.util.getValue("domain.organization.name");
	if (orgName != "other") {
		POCManager.getOrganizationByName(orgName, populateOrganization);
	}
}
function populateOrganization(organization) {
	if (organization != null) {
		dwr.util.setValue("domain.organization.streetAddress1", organization.streetAddress1);
		dwr.util.setValue("domain.organization.streetAddress2", organization.streetAddress2);
		dwr.util.setValue("domain.organization.city", organization.city);
		dwr.util.setValue("domain.organization.state", organization.state);
		dwr.util.setValue("domain.organization.postalCode", organization.postalCode);
		dwr.util.setValue("domain.organization.country", organization.country);
	} else {
		dwr.util.setValue("domain.organization.streetAddress1", "");
		dwr.util.setValue("domain.organization.streetAddress2", "");
		dwr.util.setValue("domain.organization.city", "");
		dwr.util.setValue("domain.organization.state", "");
		dwr.util.setValue("domain.organization.postalCode", "");
		dwr.util.setValue("domain.organization.country", "");
	}
}
/* end of submit point of contact form */

