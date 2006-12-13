<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"> </script>

<html:form action="/createRun">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Create Run
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
						<td colspan="4" class="formTitle">
							<div align="justify">
								Description
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" width="15%">
							<strong>Assay Type </strong>
						</td>
						<td class="label" width="20%">
							<html:select property="assayType" onchange="javascript:doubleDropdown(document.createRunForm.assayType, document.createRunForm.assayName, assayTypeAssays);">
								<option value=""></option>
								<html:options name="allAvailableAssayTypes" />
							</html:select>
						</td>
						<td class="label">
							<strong>Assay Name *</strong>
						</td>
						<td class="rightLabel" width="40%">
							<html:select property="assayName">
								<option value="${createRunForm.map.assayName}" selected>
									${createRunForm.map.assayName}
								</option>
							</html:select>
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
						<td class="leftLabel" valign="top">
							<strong>Source Name</strong>
						</td>
						<td class="label" valign="top">
							<html:select property="sourceName" onchange="javascript:doubleMultibox(document.createRunForm.sourceName, document.createRunForm.sampleNames, sampleSourceSamples)">
								<option />
									<html:options name="allSampleSourcesWithUnmaskedAliquots" />
							</html:select>
						</td>
						<td colspan="2" class="rightLabel">
							<table width="100%" cellspacing="0">
								<tr>
									<td class="borderlessLabel" valign="top">
										<strong>Sample ID </strong>
									</td>
									<td class="borderlessLabel">
										<html:select property="sampleNames" multiple="true" size="8" onchange="javascript:doubleMultibox(document.createRunForm.sampleNames, document.createRunForm.assignedAliquots, sampleAliquots)">
											<c:forEach var="sample" items="${createRunForm.map.sampleNames}">
												<option value="${sample}" selected>
													${sample}
												</option>
											</c:forEach>
										</html:select>
									</td>
									<td valign="top" class="borderlessLabel">
										<strong>Aliquot ID*</strong>
									</td>
									<td class="borderlessLabel">
										<span class="mainMenu"><html:select multiple="true" property="assignedAliquots" size="8">
												<c:forEach var="aliquot" items="${createRunForm.map.assignedAliquots}">
													<option value="${aliquot}" selected>
														${aliquot}
													</option>
												</c:forEach>												
											</html:select> </span>

									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="4" class="formLabel">
							<strong>Comments </strong>&nbsp;&nbsp;
							<div align="justify">
								<span class="formFieldWhite"><html:textarea property="aliquotComment" cols="80" /></span>
							</div>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="javascript:removeSelectOptions(document.createRunForm.assayName);resetSelect(document.createRunForm.sampleNames);removeSelectOptions(document.createRunForm.assignedAliquots);">
												<input type="hidden" name="dispatch" value="create">
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
  var cal = new calendar2(document.forms['createRunForm'].elements['runDate']);
  cal.year_scroll = true;
  cal.time_comp = false;
  cal.context = '${pageContext.request.contextPath}';
  
  /* populate a hashtable containing assayType assays */
  var assayTypeAssays=new Array();    
  <c:forEach var="item" items="${allAssayTypeAssays}">
    var assays=new Array();
    <c:forEach var="assay" items="${allAssayTypeAssays[item.key]}" varStatus="count">
  		assays[${count.index}]='${assay.assayName}';  	
    </c:forEach>
    assayTypeAssays['${item.key}']=assays;
  </c:forEach>
  
  /* populate a hashtable containing sampleName aliquots */
  var sampleAliquots=new Array();    
  <c:forEach var="item" items="${allUnmaskedSampleAliquots}">
    var aliquots=new Array();
    <c:forEach var="aliquot" items="${allUnmaskedSampleAliquots[item.key]}" varStatus="count">
  	    aliquots[${count.index}]='${aliquot.aliquotName}';  	
    </c:forEach>
    sampleAliquots['${item.key}']=aliquots;
  </c:forEach>
  
   /* populate a hashtable containing sampleSource samples */
  var sampleSourceSamples=new Array();    
  <c:forEach var="item" items="${sampleSourceSamplesWithUnmaskedAliquots}">
    var samples=new Array();
    <c:forEach var="sample" items="${sampleSourceSamplesWithUnmaskedAliquots[item.key]}" varStatus="count">
  	    samples[${count.index}]='${sample.sampleName}';  	
    </c:forEach>
    sampleSourceSamples['${item.key}']=samples;
  </c:forEach>

//-->
</script>
