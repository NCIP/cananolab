<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"> </script>

<html:form action="/searchRun">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Search Run
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_assay_run')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=workflow" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr class="topBorder">
						<td colspan="2" class="formTitle">
							<div align="justify">
								Description
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Sample Source </strong>
						</td>
						<td class="rightLabel">
							<html:select property="sampleSource">
								<option></option>
								<html:options name="allSampleSources" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Assay Type </strong>
						</td>
						<td class="rightLabel">
							<html:select property="assayType" onchange="javascript:doubleDropdownWithExraOption(document.searchRunForm.assayType, document.searchRunForm.assayName, assayTypeAssays, '');">
								<option value=""></option>
								<html:options name="allAvailableAssayTypes" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Assay Name</strong>
						</td>
						<td class="rightLabel">
							<html:select property="assayName">								    					
								<option value="${searchRunForm.map.assayName}" selected>
									${searchRunForm.map.assayName}
								</option>
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Run By </strong>
						</td>
						<td class="rightLabel">
							<html:select property="runBy">
								<option value=""></option>
								<html:options collection="allUsers" property="loginName" labelProperty="fullName" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Run Date </strong>
						</td>
						<td class="rightLabel">
							<html:text property="runDate" size="10" />
							<span class="formFieldWhite"> <a href="javascript:cal.popup();"> <img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"> </a> </span>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="javascript:removeSelectOptions(document.searchRunForm.assayName);">
												<input type="hidden" name="dispatch" value="search">
												<input type="hidden" name="page" value="1">
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

  /* 
  	 cal is java script variable instantiated from calendar2 function
  	 in script.js file
  */ 
  var cal = new calendar2(document.forms['searchRunForm'].elements['runDate']);
  cal.year_scroll = true;
  cal.time_comp = false;
  cal.context = '${pageContext.request.contextPath}';
  
  /* populate a hashtable containing assayType assays */
  var assayTypeAssays=new Array();    
  <c:forEach var="item" items="${allAssayTypeAssays}">
    var assays=new Array();
    /* add an empty one */
    <c:forEach var="assay" items="${allAssayTypeAssays[item.key]}" varStatus="count">
  		assays[${count.index}]='${assay.assayName}';  	
    </c:forEach>
    assayTypeAssays['${item.key}']=assays;
  </c:forEach>
  
//-->
</script>
