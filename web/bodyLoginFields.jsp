<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="loginDisplay" value="display: none" />
<c:if test="${!empty param.loginDisplay}">
	<c:set var="loginDisplay" value="display: block" />
</c:if>
<table summary="" cellpadding="2" cellspacing="0" border="0"
	class="sidebarSection" width="100%">
	<tr>
		<th>
			<span class="loginLink"><a href="#" class="loginText"
				onClick="displayLogin();">Login</a> </span>
			<span class="loginLink"><a href="#" class="loginText"
				onClick="javascript:location.href='changePassword.jsp'">Update
					Password</a> </span>
			<span class="loginLink"><a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=welcome_login')"
				class="loginText">Help</a> </span>
		</th>
	</tr>
	<tr>
		<td>
			<html:form action="/login">
				<table cellpadding="0" cellspacing="0" border="0" id="loginBlock"
					style="${loginDisplay}">
					<tr>
						<td align="right">
							<label for="loginID">
								LOGIN ID
							</label>
						</td>
						<td>
							<input type="text" name="loginId" size="14" />
						</td>
					</tr>
					<tr>
						<td align="right">
							<label for="password">
								PASSWORD
							</label>
						</td>
						<td>
							<input type="password" name="password" size="14" />
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<html:submit value="Login" />
						</td>
					</tr>
				</table>
				<input type="hidden" name="loginDisplay" value="display:block" />
			</html:form>
		</td>
	</tr>
</table>