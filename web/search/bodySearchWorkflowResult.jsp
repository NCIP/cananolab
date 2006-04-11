<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<h2>
	<br>
	Search Results
</h2>
<blockquote>
	<logic:present name="workflows">
		<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
			<tr>
				<td width="111" class="dataTablePrimaryLabel">
					File Name
				</td>
				<td width="33" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						Assay Type
					</div>
				</td>
				<td width="51" align="left" class="dataTablePrimaryLabel">
					Assay Name
				</td>
				<td width="51" align="left" class="dataTablePrimaryLabel">
					Assay Run Date
				</td>
				<td width="53" align="left" class="dataTablePrimaryLabel">
					Aliquot ID
				</td>
				<td width="53" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						File Submission Date
					</div>
				</td>
				<td width="61" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						File Submitter&#13;
					</div>
				</td>
				<td width="44" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						File Status
					</div>
				</td>
			</tr>
			<logic:iterate name="workflows" id="workflow" type="gov.nih.nci.calab.dto.search.WorkflowResultBean" indexId="rowNum">
				<c:choose>
					<c:when test="${rowNum % 2 == 0}">
						<c:set var="style" value="formLabelGrey" />
					</c:when>
					<c:otherwise>
						<c:set var="style" value="formLabelWhite" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td class="${style}" valign="top">
						<div align="left">
							<a href="#" class="style2" align="left"><bean:write name="workflow" property="fileName" /></a>
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							<bean:write name="workflow" property="assayType" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<bean:write name="workflow" property="assayName" />
					</td>
					<td class="${style}" valign="top">
						<bean:write name="workflow" property="assayRunDate" />
					</td>
					<td class="${style}" valign="top">
						<bean:write name="workflow" property="aliquotName" />
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							<bean:write name="workflow" property="fileSubmissionDate" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							<bean:write name="workflow" property="fileSubmitter" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							<strong><bean:write name="workflow" property="fileMaskStatus" /></strong>
						</div>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="workflows">
		<logic:messagesPresent message="true">
			<ul>
				<font color="red"> <html:messages id="msg" message="true" bundle="search">
						<li>
							<bean:write name="msg" />
						</li>
					</html:messages> </font>
			</ul>
		</logic:messagesPresent>
	</logic:notPresent>
	<p>
		&nbsp;
	</p>
	<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> </span>
				<br>
				<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="32">
							<div align="right">
								<input type="button" value="Back" onclick="javascript:history.go(-1)">
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
	<br>
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


