/* set the entity properties section */
function setEntityInclude(selectEleId, pagePath) {
	var entityType = document.getElementById(selectEleId).value;
	var inclueBlock = document.getElementById("entityInclude");
	CompositionManager.getEntityIncludePage(entityType, pagePath, populatePage);
}
function populatePage(pageData) {
	var inclueBlock = document.getElementById("entityInclude");
	if (pageData == "") {
		inclueBlock.style.display = "none";
	} else {
		dwr.util.setValue("entityInclude", pageData, {escapeHtml:false});
		inclueBlock.style.display = "inline";
	}
}
/* end of set the entity properties section */

/* set submit chemical association form */
function getEntityDisplayNameOptions(elementNumber) {
	var compositionType = dwr.util.getValue("compositionType" + elementNumber);
	if (compositionType == "Nanomaterial Entity") {
		show("materialEntitySelect" + elementNumber);
		hide("functionalizingEntitySelect" + elementNumber);
	} else {
		if (compositionType == "Functionalizing Entity") {
			show("functionalizingEntitySelect" + elementNumber);
			hide("materialEntitySelect" + elementNumber);
		}
	}
}
function displayBondType() {
	var type = document.getElementById("assoType").value;
	if (type == "attachment") {
		show("bondTypeLabel");
		show("bondTypePrompt");
	} else {
		hide("bondType");
		hide("bondTypePrompt");
	}
}
function setCompositionType(entityTypeId, displayNameEleId) {
	var selectEle = document.getElementById(entityTypeId);
	var selectedName = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById(displayNameEleId).value = selectedName;
	// alert(document.getElementById(displayNameEleId).value);
}
function setEntityDisplayName(entityTypeId, displayNameEleId) {
	var selectEle = document.getElementById(entityTypeId);
	var selectedName = selectEle.options[selectEle.options.selectedIndex].text;
	document.getElementById(displayNameEleId).value = selectedName;
	// alert(document.getElementById(displayNameEleId).value);
}
/* end of set submit chemical association form */

/* set submit file form */
function clearFile(type) {
	//go to server and clean form bean
	CompositionManager.resetTheFile(type, populateFile);
	hide("deleteFile");
    show("load");
	hide("link");
	dwr.util.setValue("uploadedFile", "");
	dwr.util.setValue("externalUrl", "");
}
function setTheFile(type, id) {
	CompositionManager.getFileById(type, id, populateFile);
	show("newFile");
	show("deleteFile");
}

function deleteCompositionData(type, actionName) {
var answer = confirmDelete(type);
	if (answer != 0) {
		form.action = actionName + ".do?dispatch=delete&page=0";
		form.submit();
	}
}
/* end of set submit file form */
