/* 
 * DWR for creating options of associated elements
 */
 
//element A
function selectEntityTypeOptionsA() {
	getAssociatedElementOptions("sampleFuncEntityTypeA",
		"entityTypeA", "compEleTypeA");
}

//element B
function selectEntityTypeOptionsB() {
	getAssociatedElementOptions("sampleFuncEntityTypeB",
		"entityTypeB", "compEleTypeB");

}

function getAssociatedElementOptions(sampleFuncEntityTypeId,
									entityTypeId, compEleTypeId) {
	var compFuncTypeValue = dwr.util.getValue(sampleFuncEntityTypeId);
	var entityTypeEle = document.getElementById(entityTypeId);
	var compEleTypeEle = document.getElementById(compEleTypeId);
	if(compFuncTypeValue == null || compFuncTypeValue.length == 0) {
		entityTypeEle.style.display = "none";
		compEleTypeEle.style.display = "none";
	} else {
		AddFileManager.getEntityTypeOptions(compFuncTypeValue, function(data) {
			dwr.util.removeAllOptions(entityTypeId);
    		dwr.util.addOptions(entityTypeId, data);
  		});
  		
  		entityTypeEle.style.display = "inline";
  			
  		if(compFuncTypeValue == "nanomaterialEntity") {
  			compEleTypeEle.style.display = "inline";
  		} else {
  			compEleTypeEle.style.display = "none";
  		}
	}
}	
	