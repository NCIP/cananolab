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
			<td class="cellLabel" width="30%">Title <br/>
					<html:select property="title" >
								<html:options collection="titleOperands" property="value"
									labelProperty="label" />
					</html:select>
			</td>
			<td class="cellLabel" width="30%">
				First Name* <br/>			
				<html:text property="firstName" size="30" />
			</td>
			<td class="cellLabel">
				Last Name*<br/>			
				<html:text property="lastName" size="30" />
			</td>
		</tr>	
	</table>
	<br/>
	<table width="100%" align="center" class="submissionView">
		
		<tr>
			<td class="cellLabel">
				Email*<br/><html:text property="email" size="30" />
			</td>
			<td class="cellLabel">
				Phone*<br/>
				<html:text property="phone" size="30" />				
			</td>			
		</tr>
		<tr>
			<td class="cellLabel">
				Organization*<br/><html:text property="organization" size="30" />
			</td>
			<td class="cellLabel">
				Fax<br/>
				<html:text property="fax" size="30" />				
			</td>			
		</tr>
	</table>
	<br/>
	<table width="100%" align="center" class="submissionView">		
		<tr>
			<td  class="cellLabel">Brief Description of your request<br/>
			<html:textarea property="comment" rows="8" cols="100" />
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
				<html:multibox styleId="researchArea" property="registerToUserList">
								checked
				</html:multibox>Also register me for the caNanoLab user group email list.
										
			</td>
		</tr>
	</table>
	
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td width="30%">
				<table border="0" align="left" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="left">
								<input type="button" value="Cancel"
									onclick="javascript:location.href='register.do?'">
								<html:submit value="Register" />
							</div>
						</td>
					</tr>
				</table>				
			</td>
		</tr>
	</table>
</html:form>


