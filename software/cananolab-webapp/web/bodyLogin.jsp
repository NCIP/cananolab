<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Log into caNanoLab" />
	<jsp:param name="topic" value="welcome_login_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>

<html:form action="/login">
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Login ID
			</td>
			<td>
				<input type="text" name="loginId" size="30" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Password
			</td>
			<td>
				<input type="password" name="password" size="30" />
			</td>
		</tr>
	</table>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td width="30%">
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset" onclick="this.form.reset();">
								<html:submit value="Login" />
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>