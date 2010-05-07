<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Update Password" />
	<jsp:param name="topic" value="update_password_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/updatePassword">
	<jsp:include page="/bodyMessage.jsp" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Login ID*
			</td>
			<td>
				<html:text property="loginId" size="50" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Old Password*
			</td>
			<td>
				<html:password property="password" size="50" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				New Password*
			</td>
			<td>
				<html:password property="newPassword" size="50" />
				<br />
				<em>(Minimum of 5 characters are required)</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Retype New Password*
			</td>
			<td>
				<html:password property="newPassword2" size="50" />
			</td>
		</tr>
	</table>
	<br/>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td width="30%">
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset"
									onclick="javascript:location.href='updatePassword.do?'">
								<html:submit value="Update" />
							</div>
						</td>
					</tr>
				</table>				
			</td>
		</tr>
	</table>
</html:form>


