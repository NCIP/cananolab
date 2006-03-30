<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<html:form action="/maskAliquot">
	<h2> &nbsp;<BR> &nbsp;Mask Aliquot </h2>
	<logic:messagesPresent message="true">
		<ul>
			<font color="red"> <html:messages id="msg" message="true" bundle="workflow">
					<li>
						<bean:write name="msg" />
					</li>
				</html:messages> </font>
		</ul>
	</logic:messagesPresent>
	<blockquote>
		<table width="75%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td colspan="2" class="dataTablePrimaryLabel">
					<DIV align="justify">
						&nbsp;<STRONG>Confirm Aliquot Mask:</STRONG>
					</DIV>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Aliquot ID</strong> <bean:write name="maskAliquotForm" property="aliquotId"/>								
				</td>
				<td class="formField">
					<DIV align="left"> &nbsp;<SPAN class="formField" align="left"><SPAN class="mainMenu"><SPAN class="formMessage"><STRONG>&nbsp;</STRONG>Are you sure you would you like to mask this aliquot?</SPAN></SPAN></SPAN>&nbsp;<SPAN class="formFieldWhite"> &nbsp;</SPAN> </DIV>
					 
				</td>
			</tr>
		</table>
		<br>

		<table width="60%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td class="dataTablePrimaryLabel">
					<div align="justify"> &nbsp;Reason for mask:</div>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="left">
						<span class="formField"><span class="formFieldWhite"> <textarea name="description" cols="50" rows="3" wrap="ON"></textarea> </span></span>
					</div>
				</td>
			</tr>
			<tr>
				<td width="30%">
					<table border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td>
								<DIV align="left">
									<input type="hidden" name="maskType" value="aliquot">																
									<html:hidden property="aliquotId"/>
									<html:submit value="Yes" />
									<INPUT type="button" value="No " onclick="javascript:history.go(-1)">
								</DIV>
								<div align="left"></div>
							</td>
						</tr>
					</table>
					<div align="right"></div>
				</td>
			</tr>
		</table>
		<p>
			 &nbsp;
		</p>
		<p>
			 &nbsp;
		</p>
		<p>
			 &nbsp;
		</p>
	</blockquote>
</html:form>
