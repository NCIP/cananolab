<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/useAliquot">

	<table width="100%" align="center">
		<tr>
			<td>
				<br>
				<h3>
					Use Aliquot
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=create_assay_run')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr class="topBorder">
						<td colspan="4" class="formTitle">
							<div align="justify">
								Use Aliquot
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top" width="20%">
							<strong>Sample ID* </strong>
						</td>
						<td class="label" width="20%" valign="top">
							<html:select property="sampleNames" multiple="true" size="8" onchange="javascript:doubleMultibox(document.useAliquotForm.sampleNames, document.useAliquotForm.assignedAliquots, sampleAliquots)">
								<c:forEach var="sampleName" items="${useAliquotForm.map.sampleNames}">
									<option value="${sampleName}">
										${sampleName}
									</option>
								</c:forEach>							
							</html:select>
						</td>
						<td valign="top" class="label" width="20%">
							<strong>Aliquot ID*</strong>
						</td>
						<td class="rightLabel">
							<span class="mainMenu"><html:select multiple="true" property="assignedAliquots" size="8">
									<c:forEach var="aliquot" items="${useAliquotForm.map.assignedAliquots}">
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
								<span class="formFieldWhite"><html:textarea property="comments" cols="60" /></span>
							</div>
						</td>
					</tr>
				</table>
				<br>

				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<div align="left">
											<input type="hidden" name="runId" value="${currentRun.id}" />
											<input type="reset" value="Reset" onclick="resetSelect(document.createRunForm.sampleNames);resetSelect(document.useAliquotForm.assignedAliquots);">
											<input type="button" value="Cancel" onclick="javascript:history.go(-1)">
											<input type="hidden" name="dispatch" value="use" />
											<input type="hidden" name="page" value="1" />
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
//-->
</script>
