<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="User Registration" />
	<jsp:param name="topic" value="register_user" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/register">
	<jsp:include page="/bodyMessage.jsp" />
	
	<table width="100%" align="center" class="submissionView">		
		<tr>
			<td class="cellLabel" width="30%"><label for="title">Title</label><br/>
					<html:select property="title" styleId="title">
								<html:options collection="titleOperands" property="value"
									labelProperty="label" />
					</html:select>
			</td>
			<td class="cellLabel" width="30%">
				<label for="first name">First Name*</label> <br/>			
				<html:text property="firstName" size="30" styleId="first name"/>
			</td>
			<td class="cellLabel">
				<label for="last name">Last Name*</label><br/>			
				<html:text property="lastName" size="30" styleId="last name"/>
			</td>
		</tr>	
	</table>
	<br/>
	<table width="100%" align="center" class="submissionView">
		
		<tr>
			<td class="cellLabel">
				<label for="email">Email*</label><br/><html:text property="email" size="30" styleId="email"/>
			</td>
			<td class="cellLabel">
				<label for="phone">Phone*</label><br/>
				<html:text property="phone" size="30" styleId="phone"/>				
			</td>			
		</tr>
		<tr>
			<td class="cellLabel">
				<label for="organization">Organization*</label><br/><html:text property="organization" size="30" styleId="organization"/>
			</td>
			<td class="cellLabel">
				<label for="fax">Fax</label><br/>
				<html:text property="fax" size="30" styleId="fax" />				
			</td>			
		</tr>
	</table>
	<br/>
	<table width="100%" align="center" class="submissionView">		
		<tr>
			<td  class="cellLabel"><label for="description">Brief Description of your request<br/>
			<html:textarea property="comment" rows="8" cols="100" styleId="description"/>
				<br>
				<em>Please limit your description to 4000 characters</em>
				<br>
			</td>
		</tr>
	</table>
	<br/>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				<html:multibox styleId="register user group list" property="registerToUserList">
								checked
				</html:multibox><label for="register user group list">Also register me for the caNanoLab user group email list.</label>										
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
								<input type="button" value="Reset" onclick="this.form.reset()">
								<html:submit value="Register" onclick="javascript:location.href='register.do?'"/>
							</div>
						</td>
					</tr>
				</table>				
			</td>
		</tr>
	</table>
</html:form>


