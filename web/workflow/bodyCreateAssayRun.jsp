<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"> </script>

<html:form action="/createAssayRun">
	<table width="80%" align="center">
		<tr>
			<td width="10%">
				&nbsp;
			</td>
			<td>
				<h3>
					Create Assay Run
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=create_assay_run')" class="helpText">Help</a>
			</td>
	</table>
	<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
	<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr class="topBorder">
			<td colspan="4" class="formTitle">
				<div align="justify">
					Description
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Run By* </strong>
			</td>
			<td class="label">
				<html:select property="runBy">
					<option value=""></option>
					<html:options collection="allUsers" property="loginName" labelProperty="fullName" />
				</html:select>
			</td>
			<td class="label">
				<strong>Run Date* </strong>
			</td>
			<td class="rightLabel">
				<html:text property="runDate" size="10" />
				<span class="formFieldWhite"> <a href="javascript:cal.popup();"> <img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"> </a> </span>
			</td>
		</tr>
		<tr>
			<td class="leftLabel" valign="top" width="20%">
				<strong>Sample ID </strong>
			</td>
			<td class="label" width="20%">
				<html:select property="sampleNames" multiple="true" size="8" onchange="javascript:doubleMultibox(document.createAssayRunForm.sampleNames, document.createAssayRunForm.assignedAliquots, sampleAliquots)">
					<html:options name="allSampleNamesWithAliquots" />
				</html:select>
			</td>
			<td valign="top" class="label" width="20%">
				<strong>Aliquot ID</strong>
			</td>
			<td class="rightLabel">
				<span class="mainMenu"><html:select multiple="true" property="assignedAliquots" size="8">
						<c:forEach var="aliquot" items="${paramValues.assignedAliquots}">
							<option value="${aliquot}" selected>
								${aliquot}
							</option>
						</c:forEach>
					</html:select> </span>
			</td>
		</tr>
		<tr>
			<td colspan="4" class="formLabel">
				<strong>General Comments </strong>&nbsp;&nbsp;
				<div align="justify">
					<span class="formFieldWhite"><html:textarea property="aliquotComment" cols="60" /></span>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="80%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="32">
							<div align="right">
								<div align="right">
									<input type="reset" value="Reset" onclick="resetSelect(document.createRunForm.sampleNames);resetSelect(document.createAssayRunForm.assignedAliquots);">
									<html:hidden property="assayName" value="${param.assayName}" />
									<html:hidden property="assayType" value="${param.assayType}" />
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
	<p>
		&nbsp;
	</p>
</html:form>
<script language="JavaScript">
<!--//

  /* 
  	 cal is java script variable instantiated from calendar2 function
  	 in script.js file
  */ 
  var cal = new calendar2(document.forms['createAssayRunForm'].elements['runDate']);
  cal.year_scroll = true;
  cal.time_comp = false;
  cal.context = '${pageContext.request.contextPath}';
  
  /* populate a hashtable containing sampleName aliquots */
  var sampleAliquots=new Array();    
  <c:forEach var="item" items="${allUnmaskedSampleAliquots}">
    var aliquots=new Array();
    <c:forEach var="aliquot" items="${allUnmaskedSampleAliquots[item.key]}" varStatus="count">
  	    aliquots[${count.index}]='${aliquot.aliquotName}';  	
    </c:forEach>
    sampleAliquots['${item.key}']=aliquots;
  </c:forEach>
  
//-->
</script>
