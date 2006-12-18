<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/maskFile">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Mask File
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=mask_files')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/workflow/bodyWorkflowInfo.jsp" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr class="topBorder">
						<td colspan="2" class="formTitle">
							<DIV align="justify">
								&nbsp;<STRONG>Confirm&nbsp;File Mask:</STRONG>
							</DIV>
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<strong>File Name : <c:out value="${param.itemName}" /></strong>
						</td>
						<td class="formField">
							<DIV align="left">
								&nbsp;<SPAN class="formField" align="left"><STRONG></STRONG>Are you sure you would you like to mask this File ?</SPAN>
							</DIV>

						</td>
					</tr>
				</table>
				<br>

				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr class="topBorder">
						<td class="dataTablePrimaryLabel">
							<div align="justify">
								&nbsp;*&nbsp;Explain reason for mask:
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<div align="left">
								<span class="formField"><span class="formFieldWhite"> <textarea name="description" cols="65" rows="3" wrap="ON"></textarea> </span></span>
							</div>
						</td>
					</tr>
					<tr>
						<td width="30%">
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<DIV align="left">
											<input type="hidden" name="maskType" value="file">
											<input type="hidden" name="dispatch" value="mask">											
											<html:hidden property="itemId" />
											<html:hidden property="itemName" />											
											<html:submit value="Yes" />
											<INPUT type="button" value="No " onclick="javascript:history.go(-1)">
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
