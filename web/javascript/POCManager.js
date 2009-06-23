/* set the submit point of contact form */
function clearPointOfContact() {
	//go to server and clean form bean
	POCManager.resetThePointOfContact(populatePointOfContact);
	hide("deletePointOfContact");
}

function setThePointOfContact(id) {
	POCManager.getPointOfContactById(id, populatePointOfContact);
	show("newPointOfContact");
	show("deletePointOfContact");
}
function populatePointOfContact(poc) {
	if (poc != null) {
		dwr.util.getValues(poc);
		if (poc.domain.id != null) {
			show("deletePointOfContact");
		}
	}
}

function addPointOfContact(actionName) {
	submitAction(document.forms[0], actionName, "savePointOfContact", 1);
}
function removePointOfContact(actionName) {
	submitAction(document.forms[0], actionName, "removePointOfContact", 1);
}
/* end of submit point of contact form */
