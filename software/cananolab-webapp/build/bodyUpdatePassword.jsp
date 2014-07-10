<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

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
				<label for="loginId">Login ID*</label>
			</td>
			<td>
				<html:text property="loginId" size="50" styleId="loginId"/>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="oldPass">Old Password*</label>
			</td>
			<td>
				<html:password property="password" size="50" styleId="oldPass"/>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="newPass">New Password*</label>
			</td>
			<td>
				<html:password property="newPassword" size="50" styleId="newPass"/>
				<br />
				<em>(Minimum of 5 characters are required)</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="newPassRetype">Retype New Password*</label>
			</td>
			<td>
				<html:password property="newPassword2" size="50" styleId="newPassRetype"/>
			</td>
		</tr>
	</table>
	<br/>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td width="30%">
				<table border="0" align="right" cellpadding="4" cellspacing="0" summary="layout">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset" onclick="this.form.reset()">
								<html:submit value="Update" />
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>


