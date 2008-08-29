<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/updatePassword">
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width=100% align="center" summary="" border="0">
		<tr>
			<td align="right" width="15%" colspan="2">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=update_password_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td class="sidebarContent" colspan="2">
				<jsp:include page="/bodyMessage.jsp" />
			</td>
		</tr>
		<tr>
			<td class="formTitle" colspan="2">
				Update Password
			</td>
		</tr>
		<tr>
			<td class="formLabel" width="20%">
				<strong>Login ID*</strong>
			</td>
			<td class="formField">
				<html:text property="loginId" size="30" />
			</td>
		</tr>
		<tr>
			<td class="formLabelWhite">
				<strong>Old Password* </strong><span class="formField">
			</td>
			<td class="formFieldWhite">
				<span class="formFieldWhite"><span class="formField"><html:password property="password" size="30" /></span></span>
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				<strong>New Password* </strong><span class="formField">(<em>Minimum of 5 characters are required</em>)</span>
			</td>
			<td class="formField">
				<span class="formFieldWhite"><span class="formField"><html:password property="newPassword" size="30" /></span></span>
			</td>
		</tr>
		<tr>
			<td class="formLabelWhite">
				<strong>Retype New Password*</strong>
			</td>
			<td class="formFieldWhite">
				<html:password property="newPassword2" size="30" />
			</td>
		</tr>
		<tr>
			<td align="right" valign="bottom" colspan="2">
				<!-- action buttons begins -->

				<!-- action buttons end -->
				<TABLE border="0" cellpadding="2" cellspacing="0">
					<TR>
						<TD>
							<html:submit value="Update" />
						</TD>
						<TD>
							<input type="button" value="Reset" onclick="javascript:location.href='updatePassword.do?'">
						</TD>
					</TR>
				</TABLE>
			</td>
		</tr>
	</table>
	<BR>
	<BR>

</html:form>


