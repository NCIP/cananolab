
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
function getAssociatedElementOptions(elementNumber) {
	var compositionType = dwr.util.getValue("compositionType" + elementNumber);
	CompositionManager.getAssociatedElementOptions(compositionType, function (entities) {
		if (entities != null) {
			dwr.util.removeAllOptions("entityId" + elementNumber);
			dwr.util.addOptions("entityId" + elementNumber, [""]);
			//requires getters for domainId and displayName in BaseCompositionEntityBean
			dwr.util.addOptions("entityId" + elementNumber, entities, "domainId", "displayName");
		} else {
			sessionTimeout();
		}
	});
	show("entitySelect" + elementNumber);
	hide("composingElementSelect" + elementNumber);
}
function displayBondType() {
	var type = document.getElementById("assoType").value;
	if (type == "attachment") {
		show("bondTypeLabel");
		show("bondTypePrompt");
	} else {
		hide("bondTypeLabel");
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
	var selectedName = dwr.util.getText(entityTypeId);
	dwr.util.setValue(displayNameEleId, selectedName);
	//alert(selectedName);
}
function getComposingElementOptions(elementNumber) {
	var compositionType = dwr.util.getValue("compositionType" + elementNumber);
	if (compositionType == "Nanomaterial Entity") {
		var entityId = dwr.util.getValue("entityId" + elementNumber);
		CompositionManager.getComposingElementsByNanomaterialEntityId(entityId, function (composingElements) {
			if (composingElements != null) {
				dwr.util.removeAllOptions("composingElementId" + elementNumber);
				dwr.util.addOptions("composingElementId" + elementNumber, [""]);
			//requires getters for domainId and displayName in ComposingElementBean
				dwr.util.addOptions("composingElementId" + elementNumber, composingElements, "domainId", "displayName");
			} else {
				sessionTimeout();
			}
		});
		show("composingElementSelect" + elementNumber);
	} else {
		hide("composingElementSelect" + elementNumber);
	}
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
/* end of set submit file form */

