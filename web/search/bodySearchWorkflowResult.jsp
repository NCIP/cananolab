<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<h2>
	<br>
	Search Results
</h2>
<blockquote>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr valign="bottom">
			<td align="right">
				<input type="button" onClick="javascript:history.go(-1);" value="Back">
			</td>
		</tr>
	</table>
	<br>
	<logic:present name="workflows">
		<table width="100%" border="0" align="left" cellpadding="0" cellspacing="0">
			<tr>
				<td width="111" class="dataTablePrimaryLabel">
					File Name
				</td>
				<td width="33" class="dataTablePrimaryLabel">
					Assay Type
				</td>
				<td width="51" class="dataTablePrimaryLabel">
					Assay Name
				</td>
				<td width="51" class="dataTablePrimaryLabel">
					Assay Run Date
				</td>
				<td width="53" class="dataTablePrimaryLabel">
					Aliquot ID
				</td>
				<td width="53" class="dataTablePrimaryLabel">
					File Submission Date
				</td>
				<td width="61" class="dataTablePrimaryLabel">
					File Submitter&#13;
				</td>
				<td width="44" class="dataTablePrimaryLabel">
					File Status
				</td>
			</tr>
			<logic:iterate name="workflows" id="workflow" type="gov.nih.nci.calab.dto.search.WorkflowResultBean" indexId="rowNum">
				<c:choose>
					<c:when test="${rowNum % 2 == 0}">
						<c:set var="style" value="formFieldGrey" />
					</c:when>
					<c:otherwise>
						<c:set var="style" value="formFieldWhite" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td class="${style}">
						<a href="#"><bean:write name="workflow" property="fileName" /></a> &nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="assayType" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="assayName" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="assayRunDate" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="aliquotName" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="fileSubmissionDate" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="fileSubmitter" />
						&nbsp;
					</td>
					<td class="${style}">
						<strong><bean:write name="workflow" property="fileMaskStatus" /></strong> &nbsp;
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
	<p>
		&nbsp;
	</p>
</blockquote>


