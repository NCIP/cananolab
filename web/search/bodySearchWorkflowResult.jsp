<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
					Assay Run Name
				</td>
				<td width="51" class="dataTablePrimaryLabel">
					Assay Run Date
				</td>
				<td width="51" class="dataTablePrimaryLabel">
					Input File or Output File
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
				<td width="53" class="dataTablePrimaryLabel">
					Aliquot ID
				</td>
				<td width="53" class="dataTablePrimaryLabel">
					Aliquot Status
				</td>
			</tr>
			<logic:iterate name="workflows" id="workflow" type="gov.nih.nci.calab.dto.search.WorkflowResultBean" indexId="rowNum">
				<c:choose>
					<c:when test="${rowNum % 2 == 0}">
						<c:set var="style" value="formFieldGrey" />
						<c:set var="style0" value="leftBorderedFormFieldGrey" />
					</c:when>
					<c:otherwise>
						<c:set var="style" value="formFieldWhite" />
						<c:set var="style0" value="leftBorderedFormFieldWhite" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td class="${style0}">
						<a href="#"><bean:write name="workflow" property="file.filename" /></a> &nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="assay.assayType" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="assay.assayName" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="run.name" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="run.runDate" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="file.inoutType" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="file.createDateStr" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="file.fileSubmitter" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="file.fileMaskStatus" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="aliquot.aliquotName" />
						&nbsp;
					</td>
					<td class="${style}">
						<bean:write name="workflow" property="aliquot.maskStatus" />
						&nbsp;
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


