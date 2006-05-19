<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/register?method=register">

	<table width="600" align="center">
		<tr>
			<td width="10%">
				&nbsp;
			</td>
			<td align="center">
				<h3>
					<br>
					User Registration
				</h3>
			</td>
			<td align="right" width="10%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_sample')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<jsp:include page="/bodyMessage.jsp"/>
			</td>
		</tr>
	</table>
		
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="600" align="center" summary="" border="0">
		<tr class="topBorder">
			<td class="formTitle" colspan="2">
				<div align="justify">
					User Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="formLabel" width="20%">
				 <strong>Title</strong></TD>
			<TD class="formFieldWhite">
				<SELECT name="title" size="1">
						<OPTION value="" selected="selected"></OPTION>
						<OPTION value="Dr.">
							Dr.
						</OPTION>
						<OPTION value="Mr.">
							Mr.
						</OPTION>
						<OPTION value="Mrs.">
							Mrs.
						</OPTION>
						<OPTION value="Miss">
							Miss
						</OPTION>
						<OPTION value="Ms.">
							Ms.
						</OPTION>
					</SELECT>
			</TD>
		</tr>
		<tr>		
			<TD class="formLabelWhite">
				 <strong>First Name *</strong> 
			</TD>
			<TD class="formFieldWhite">
				<html:text property="firstName" size="14" />
			</TD>
		</tr>
		<tr>	
			<TD class="formLabel">
				<strong>Middle Initial</strong>
			</TD>
			<TD  class="formField">
				<html:text property="middleName" size="3" />
			</TD>
		</tr>
		<tr>
			<TD class="formLabelWhite">
				<strong>Last Name *</strong>
			</TD>
			<TD  class="formFieldWhite">
				<html:text property="lastName" size="14" />
			</TD>
		</TR>
		<TR>
			<TD class="formLabelWhite">
				<strong>Email *</strong>
			</TD>
			<td class="formFieldWhite">
				<html:text property="email" size="15" />
			</td>
		</tr>
		<tr>	
			<TD class="formLabel">
				<strong>Phone</strong>
			</TD>
			<td class="formField">
				<html:text property="phoneNumber" size="15" />
			</td>
		</tr>
		<tr>
			<td class="formLabelWhite">
				 <strong>Organization Name</strong>
			</td>
			<td class="formFieldWhite">
				<html:text property="organization" size="30" />
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				 <strong>Department</strong> 
			</td>
			<td class="formField">
				<html:text property="department" size="30" />
			</td>
		</tr>
	</table>
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width=600 align="center" summary="" border="0">
		<tr>
			<td class="formTitle" colspan="2">
				LOGIN INFORMATION
			</td>
		</tr>
		<tr>
			<td class="formLabel" colspan="2">
				Please enter a login ID and a password. Your selections will be saved and future visits will require input of this information.
			</td>
		</tr>
		<tr>
			<td class="formLabel" width="20%">
				<strong>Login ID *</strong>
			</td>
			<td class="formField">
				<html:text property="loginId" size="30" />
			</td>
		</tr>
		<tr>	
			<td class="formLabelWhite">
				<strong>Password * </strong><span class="formField">(<em>Minimum of 5 characters are required</em>)</span>
			</td>
			<td class="formFieldWhite">
				<span class="formFieldWhite"><span class="formField"><html:password property="password" size="30" /></span></span>
			</td>
		</tr>
		<tr>
			<td class="formLabel">
				<strong>Retype Password *</strong>
			</td>
			<td class="formField">
				<html:password property="password2" size="30" />
			</td>
		</tr>
		<tr>
			<td align="right" valign="bottom" colspan="2">
				<!-- action buttons begins -->

				<!-- action buttons end -->
				<TABLE border="0" cellpadding="2" cellspacing="0">
					<TR>
						<TD>
							<html:submit />
						</TD>
						<TD>
							<input type="reset" Value="Reset"/>			
						</TD>
					</TR>
				</TABLE>


			</td>
		</tr>
	</table>
	<BR>
	<BR>

</html:form>


