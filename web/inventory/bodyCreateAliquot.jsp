<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/editableDropDown.js"></script>
<script type="text/javascript">
<!--//
function refreshAliquots() {
  document.createAliquotForm.action="createAliquot.do?dispatch=update";
  document.createAliquotForm.submit();
}

function openLink() {
  for (var i=0; i<document.createAliquotForm.elements.length; i++) {
     var elementName=document.createAliquotForm.elements[i].name;
     if (elementName=="template.howCreated") {
       var uriPrefix = "${pageContext.request.contextPath}";
       var url= document.createAliquotForm.elements[i].value;
         if (url!='') {
            openWindow(uriPrefix + url, 'aliquotProtocol', 600, 400);
         }
     }
  } 
}
//-->
</script>
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Create Aliquot
			</h3>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=create_aliquot')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">

			<jsp:include page="/bodyMessage.jsp?bundle=inventory" />
			<html:form action="/createAliquot">
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr class="topBorder">
						<td class="formTitle">
							<div align="justify">
								Description
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="30%">
							<strong>Create from </strong>
							<br>
							<html:radio property="fromAliquot" value="false"
								onclick="javascript:disableTextElement(createAliquotForm, 'aliquotSampleName');disableTextElement(createAliquotForm, 'parentAliquotName');
							  					  enableTextElement(createAliquotForm, 'sampleName');enableTextElement(createAliquotForm, 'containerName');" />
							<strong>Sample Container</strong>&nbsp;&nbsp;
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<strong>Sample ID*</strong>
							<html:select property="sampleName"
								onchange="javascript:filterContainers();">
								<option/>
								<html:options name="allSampleNames" />
							</html:select>
							<strong>&nbsp; => &nbsp; Container ID*</strong>
							<html:select property="containerName">
								<c:forEach var="container"
								items="${allSampleContainers[createAliquotForm.map.sampleName]}">
									<html:option value="${container.containerName}">
										${container.containerName}
									</html:option>
								</c:forEach>							
							</html:select>
							<br>
							<br>
							<html:radio property="fromAliquot" value="true"
								onclick="javascript:enableTextElement(createAliquotForm, 'aliquotSampleName');enableTextElement(createAliquotForm, 'parentAliquotName');
							     				disableTextElement(createAliquotForm, 'sampleName');disableTextElement(createAliquotForm, 'containerName');" />
							<strong> Aliquot</strong> &nbsp;&nbsp;
							<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<strong>Sample ID*</strong>
							<html:select property="aliquotSampleName"
								onchange="javascript:filterAliquots();">
								<option/>
								<html:options name="allSampleNamesWithAliquots" />
							</html:select>
							<strong>&nbsp; => &nbsp; Aliquot ID*</strong>
							<html:select property="parentAliquotName">
								<c:forEach var="aliquot"
								items="${allUnmaskedSampleAliquots[createAliquotForm.map.aliquotSampleName]}">
									<html:option value="${aliquot.aliquotName}">
										${aliquot.aliquotName}
									</html:option>
								</c:forEach>							
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite">
							<div align="justify">
								<strong><span class="formFieldWhite"> </span>Number of
									Aliquots *<span class="formFieldWhite"> <html:text
											property="numberOfAliquots" size="5" /> &nbsp; </span> </strong>(Please
								click on "Update Aliquots" button below if number of aliquots
								has been changed.)
							</div>
						</td>
					</tr>
				</table>

				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" width="30%">
								<div align="justify">
									Template Aliquot
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Container Type* <span class="formFieldWhite">
											<html:select property="template.container.containerType"
												onkeydown="javascript:fnKeyDownHandler(this, event);"
												onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
												onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
												onchange="fnChangeHandler_A(this, event);">
												<option value="">--?--</option>
												<html:options name="allAliquotContainerTypes" />
											</html:select> </span> </strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="left">
									<strong>Quantity <span class="formFieldWhite"><html:text
												size="5" property="template.container.quantity" /> </span> <span
										class="formFieldWhite"> <html:select
												property="template.container.quantityUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="quantityUnits" />
											</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text
												size="5" property="template.container.concentration" /> </span><span
										class="formFieldWhite"> <html:select
												property="template.container.concentrationUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="concentrationUnits" />
											</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text
												size="5" property="template.container.volume" /> </span><span
										class="formFieldWhite"> <html:select
												property="template.container.volumeUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="volumeUnits" />
											</html:select> </span> </strong> &nbsp;&nbsp;&nbsp;
								</div>

								<div align="justify"></div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Diluents/Solvent <html:text
											property="template.container.solvent" size="10" /> &nbsp;
										&nbsp; &nbsp; How Created <html:select
											property="template.howCreated">
											<option/>
											<html:options collection="aliquotCreateMethods"
												property="value" labelProperty="label" />
										</html:select> &nbsp; &nbsp; &nbsp;</strong>
									<input type="button" value="View Protocol"
										onclick="javascript:openLink()">
								</div>
							</td>
						</tr>

						<tr>
							<td class="formLabelWhite style1">
								<div align="justify">
									<strong>Storage Conditions <span class="formField"><html:text
												property="template.container.storageCondition" size="50" />
									</span> </strong>
								</div>

								<div align="justify"></div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Storage Location<br> <br>
									<table class="topBorderOnly" cellspacing="0" cellpadding="3"
										align="left" summary="" border="0">
										<strong>
										<tr>
											<td class="borderlessLabel" width="20%">
												<strong>Room</strong>
											</td>
											<td class="borderlessLabel">
												<html:select
													property="template.container.storageLocation.room"
													onkeydown="javascript:fnKeyDownHandler(this, event);"
													onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
													onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
													onchange="fnChangeHandler_A(this, event);">
													<option value="">--?--</option>
													<html:options name="aliquotContainerInfo"
														property="storageRooms" />
												</html:select>
											</td>										
											<td class="borderlessLabel">
												<strong>Freezer</strong>
											</td>
											<td class="borderlessLabel">
												<html:select
													property="template.container.storageLocation.freezer"
													onkeydown="javascript:fnKeyDownHandler(this, event);"
													onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
													onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
													onchange="fnChangeHandler_A(this, event);">
													<option value="">--?--</option>
													<html:options name="aliquotContainerInfo"
														property="storageFreezers" />
												</html:select>
											</td>
											<td class="borderlessLabel">
												<strong>Shelf</strong>
											</td>
											<td class="borderlessLabel">
												<html:select
													property="template.container.storageLocation.shelf"
													onkeydown="javascript:fnKeyDownHandler(this, event);"
													onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
													onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
													onchange="fnChangeHandler_A(this, event);">
													<option value="">--?--</option>
													<html:options name="aliquotContainerInfo"
														property="storageShelves" />
												</html:select>
											</td>
											<td class="borderlessLabel">
												<strong>Box</strong>
											</td>
											<td class="borderlessLabel">
												<html:select
													property="template.container.storageLocation.box"
													onkeydown="javascript:fnKeyDownHandler(this, event);"
													onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
													onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
													onchange="fnChangeHandler_A(this, event);">
													<option value="">--?--</option>
													<html:options name="aliquotContainerInfo"
														property="storageBoxes" />
												</html:select>
											</td>
										</tr>
									</table>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="left">
									<strong>General Comments</strong>
									<br>
									<span class="formField"><span class="formFieldWhite">
											<html:textarea
												property="template.container.containerComments" cols="70" />
									</span> </span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>

				<br>
				<logic:present name="aliquotMatrix">
					<table width="100%" border="0" align="center" cellpadding="3"
						cellspacing="0" class="topBorderOnly" summary="">
						<logic:iterate name="aliquotMatrix" id="aliquotRow"
							type="gov.nih.nci.calab.dto.inventory.AliquotBean[]"
							indexId="rowNum">
							<tr>
								<td class="formLabelBoxWhite">
									<table align="left" border="0" align="center" cellpadding="3"
										cellspacing="0">
										<tr>
											<logic:iterate name="aliquotRow" id="aliquot"
												type="gov.nih.nci.calab.dto.inventory.AliquotBean"
												indexId="colNum">
												<td width="85">
													<table border="0" cellspacing="0" cellpadding="0">
														<tr>
															<td class="formLabelBoxWhite">
																<logic:present name="aliquot">
																	<%java.util.Map editAliquotParams = new java.util.HashMap();
			editAliquotParams.put("rowNum", rowNum);
			editAliquotParams.put("colNum", colNum);
			editAliquotParams.put("dispatch", "setup");
			editAliquotParams.put("page", "0");
			pageContext.setAttribute("editAliquotParams", editAliquotParams);%>

																	<html:link action="editAliquot"
																		name="editAliquotParams">
																		<bean:write name="aliquot" property="aliquotName" />
																		<br>
																		<bean:write name="aliquot"
																			property="container.quantity" />
																		<bean:write name="aliquot"
																			property="container.quantityUnit" />
																		<br>
																		<bean:write name="aliquot"
																			property="container.concentration" />
																		<bean:write name="aliquot"
																			property="container.concentrationUnit" />
																		<br>
																		<bean:write name="aliquot" property="container.volume" />
																		<bean:write name="aliquot"
																			property="container.volumeUnit" />
																	</html:link>
																</logic:present>
															</td>
														</tr>
													</table>
												</td>
											</logic:iterate>
										</tr>
									</table>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</logic:present>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> Aliquoted by: <bean:write
									name="creator" /> <br> Aliquoted Date: <bean:write
									name="creationDate" /> </span>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td height="32">
										<div align="right">
											<input type="button" value="Update Aliquots"
												onclick="javascript:refreshAliquots();">
											<input type="button" value="Reset"
												onClick="javascript:location.href='createAliquot.do?dispatch=setup&page=0'">
											<input type="hidden" name="dispatch" value="create">
											<input type="hidden" name="page" value="1">
											<html:submit />
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>
		</td>
	</tr>
</table>
</html:form>

<script language="JavaScript">
<!--//
  /* populate a hashtable containing sampleName aliquots */
  var sampleAliquots=new Array();    
  <c:forEach var="item" items="${allUnmaskedSampleAliquots}">
    var aliquots=new Array();
    <c:forEach var="aliquot" items="${allUnmaskedSampleAliquots[item.key]}" varStatus="count">
  	    aliquots[${count.index}]='${aliquot.aliquotName}';  	
    </c:forEach>
    sampleAliquots['${item.key}']=aliquots;
  </c:forEach>  
  
  /* populate a hashtable containing sampleName containers */
  var sampleContainers=new Array();    
  <c:forEach var="item" items="${allSampleContainers}">
    var containers=new Array();
    <c:forEach var="container" items="${allSampleContainers[item.key]}" varStatus="count">
  	    containers[${count.index}]='${container.containerName}';  	
    </c:forEach>
    sampleContainers['${item.key}']=containers;
  </c:forEach>  
  
  function filterAliquots() {
  	if (document.createAliquotForm.fromAliquot[1].checked) {
  	   doubleDropdown(document.createAliquotForm.aliquotSampleName, document.createAliquotForm.parentAliquotName, sampleAliquots);
  	}
  }
  
  function filterContainers() {
  	if (!document.createAliquotForm.fromAliquot[1].checked) {
  	   doubleDropdown(document.createAliquotForm.sampleName, document.createAliquotForm.containerName, sampleContainers);
  	}
  }
//-->
</script>
