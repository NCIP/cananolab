<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/editableDropDown.js"></script>

<html:form action="/submitProtocol" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Submit Protocol
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=nano_protocol_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									Description
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Protocol Type*</strong>
							</td>
							<td class="rightLabel">
								<html:select property="protocolType" 
									onkeydown="javascript:fnKeyDownHandler(this, event);"
									onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
									onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
									onchange="fnChangeHandler_A(this, event); filterProtocolNames();">
									<option value="">--?--</option>
									<html:options name="protocolTypes" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" >
								<strong>Protocol Name* </strong>
							</td>
							<td class="rightLabel">
								<html:select styleId="pName" property="protocolId" 
									onkeydown="javascript:fnKeyDownHandler(this, event);"
									onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
									onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
									onchange="fnChangeHandler_A(this, event); javascript:filterProtocolVersions();">
									<option value="">--?--</option>
									<html:optionsCollection name="protocolNames" label="name" value="id"/>
								</html:select> &nbsp; &nbsp; <strong>Protocol Version* </strong>&nbsp; 
								<html:select styleId="protocolVersion" property="file.id" 
									onkeydown="javascript:fnKeyDownHandler(this, event);"
									onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
									onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
									onchange="fnChangeHandler_A(this, event); javascript:getProtocolFileData();">
									<option value="">--?--</option>
									<html:optionsCollection name="protocolVersions" label="label" value="value"/>
								</html:select> &nbsp; &nbsp;  
									<!-- html:text property="file.version" size="10"/ -->
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Protocol File</strong>
							</td>
							<!-- td class="rightLabel" nowrap-->
							<td class="rightLabel">
								<span id="filenameDiv">
									<c:choose>
										<c:when test="${not empty filename}">
											<strong>Uploaded File:&nbsp; &nbsp; </strong><!-- c:out value="${filename}"/> &nbsp; &nbsp; -->
											<a href="searchProtocol.do?dispatch=download&amp;fileId=<bean:write name="submitProtocolForm" property="file.id" />"> <bean:write name="submitProtocolForm" property="file.name" /></a>&nbsp; &nbsp;
										</c:when>
									</c:choose>
								</span>
								<html:file property="uploadedFile" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>File Title</strong>
							</td>
							<td class="rightLabel">
								<html:text styleId="fileTitle" property="file.title" size="80"/>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Description</strong>
							</td>
							<td class="rightLabel">
								<html:textarea styleId="fileDescription" property="file.description" rows="3" cols="80" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="file.visibilityGroups" multiple="true" size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and ${applicationOwner}_PI are defaults if none of above is selected.)</i>						
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="15" border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="15">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="javascript:location.href='publishReport.do?dispatch=setup&page=0'">
												<input type="hidden" name="dispatch" value="submit">
												<input type="hidden" name="page" value="2">
												<html:submit />
											</div>
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
  var typeNames=new Array();    
  <c:forEach var="item" items="${AllProtocolTypeNames}">
    var names=new Array();
    <c:forEach var="name" items="${AllProtocolTypeNames[item.key]}" varStatus="count">
  	    names[${count.index}]='${name}';  	
    </c:forEach>
    typeNames['${item.key}']=names;
  </c:forEach>  

  var typeIds=new Array();    
  <c:forEach var="item" items="${AllProtocolTypeIds}">
    var ids=new Array();
    <c:forEach var="id" items="${AllProtocolTypeIds[item.key]}" varStatus="count">
  	    ids[${count.index}]='${id}';  	
    </c:forEach>
    typeIds['${item.key}']=ids;
  </c:forEach> 
  
  function filterProtocolNames() {
  	if (!document.submitProtocolForm.protocolType[1].checked) {
		doubleDropdownForNameValuePairs(document.submitProtocolForm.protocolType, document.submitProtocolForm.protocolId, typeNames, typeIds);	
  		var theVersion = document.getElementById("protocolVersion");
  		var theName = document.getElementById("pName");
		var fileTitle = document.getElementById("fileTitle");
		var fileDescription = document.getElementById("fileDescription");
		fileTitle.value = "";
		fileDescription.value = "";
		document.getElementById('filenameDiv').innerHTML = '';
  		doubleDropdownForNameValuePairs(theName, theVersion, null, null);
	}
  }
  
  var nameVersions=new Array();    
  <c:forEach var="item" items="${AllProtocolNameVersions}">
    var versions=new Array();
    <c:forEach var="version" items="${AllProtocolNameVersions[item.key]}" varStatus="count">
  	    versions[${count.index}]='${version}';  	
    </c:forEach>
    nameVersions['${item.key}']=versions;
  </c:forEach>  

  var nameIds=new Array();    
  <c:forEach var="item" items="${AllProtocolNameFileIds}">
    var fileIds=new Array();
    <c:forEach var="fileId" items="${AllProtocolNameFileIds[item.key]}" varStatus="count">
  	    fileIds[${count.index}]='${fileId}';  	
    </c:forEach>
    nameIds['${item.key}']=fileIds;
  </c:forEach> 
  
  function filterProtocolVersions() {
  	var theVersion = document.getElementById("protocolVersion");
  	var theName = document.getElementById("pName");
   	var fileTitle = document.getElementById("fileTitle");
  	var fileDescription = document.getElementById("fileDescription");
  	fileTitle.value = "";
  	fileDescription.value = "";
  	document.getElementById('filenameDiv').innerHTML = '';
  	if (!theName[1].checked) {
  	   doubleDropdownForNameValuePairs(theName, theVersion, nameVersions, nameIds);	
  	}
  }
  function getProtocolFileData(){
  	var version = document.getElementById("protocolVersion");
    var i;
  	for (i = version.length - 1; i>=0; i--) {
    	if (version.options[i].selected){
    		if (version.options[i].value == ""){
  				var fileTitle = document.getElementById("fileTitle");
  				var fileDescription = document.getElementById("fileDescription");
  				//alert("fileTitle = " + fileTitle.value);
  				//alert("fileDescription = " + fileDescription.value);
  				fileTitle.value = "";
  				fileDescription.value = "";
  				document.getElementById('filenameDiv').innerHTML = '';
  			}
  			else{
  				document.submitProtocolForm.dispatch.value = "getFileData";
  				document.submitProtocolForm.submit();
  			}
  		}
  	}
  }
  function resetRest(){
  alert(" resetRest called");
  	var fileTitle = document.getElementById("fileTitle");
  	var fileDescription = document.getElementById("fileDescription");
  	alert("fileTitle = " + fileTitle.value);
  	alert("fileDescription = " + fileDescription.value);
  	fileTitle.text.value = "";
  	fileDescription.text.value = "";
  }
//-->
</script>
