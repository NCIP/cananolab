<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/maskFile">
	<h2> &nbsp;<BR> &nbsp;Mask&nbsp;File </h2>
	<html:errors/>
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
						&nbsp;<STRONG>Confirm&nbsp;File Mask:</STRONG> </DIV>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>File Name : <bean:write name="fileMaskForm" property="fileName"/></strong>
				</td>
				<td class="formField">
					<DIV align="left"> &nbsp;<SPAN class="formField" align="left"><STRONG></STRONG>Are you sure you would you like to mask this File ?</SPAN></DIV>
		 
				</td>
			</tr>
		</table>
		<br>

		<table width="60%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td class="dataTablePrimaryLabel">
					<div align="justify"> &nbsp;*&nbsp;Explain  reason for mask:</div>
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
									<input type="hidden" name="maskType" value="file">																
									<input type="hidden" name="fileId" value="<bean:write name='fileMaskForm' property='fileId'/>">
									<html:submit value="Yes" />
									<INPUT type="button" value="No " onclick="javascript:history.go(-1)">
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