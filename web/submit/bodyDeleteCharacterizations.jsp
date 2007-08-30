<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the characterization?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="deleteConfirmed";
		this.document.forms[0].submit(); 
		return true;
	}
}
//-->
</script>
<html:form action="/deleteAction">
	<c:choose>
		<c:when test="${charBeansValue != null}">
			<table width="100%" align="center" border="0">
				<tr>
					<td>
						<h3>
							<br>
							Delete Characterizations
							<br>
						</h3>
					</td>
					<td align="right" width="15%">
						<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=delete_characterization_help')" class="helpText">Help</a>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<jsp:include page="/bodyMessage.jsp?bundle=submit" />
						<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
							<tbody>
								<tr>
									<td class="formTitle" colspan="2">
										Please select characterizations you wish to delete:
									</td>
								</tr>
								<logic:iterate name="multipCharacterizationDeleteSetupForm" property="charBeans" id="achar" indexId="charInd">
									<tr>
										<td class="leftBorderedFormFieldWhite">
											<input type="checkbox" name="charIds" value="${achar.id}" />
											<bean:write name="achar" property="name" />
											--
											<bean:write name="achar" property="viewTitle" />
										</td>
									</tr>
								</logic:iterate>
							</tbody>
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
														<input type="reset" value="Reset" onclick="">
														<input type="hidden" name="dispatch" value="delete">
														<input type="hidden" name="particleType" value="${param.particleType}">
														<input type="hidden" name="particleName" value="${param.particleName}">
														<input type="button" value="Delete" onclick="confirmDeletion();" />
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
		</c:when>
		<c:otherwise>
			<font color="blue" size="-1"><b>MESSAGE: </b>
				No Characterization exists under this category.
			</font>
		</c:otherwise>
	</c:choose>
</html:form>

