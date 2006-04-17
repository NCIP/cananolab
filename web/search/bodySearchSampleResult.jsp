<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<h2>
	<br>
	Sample Search Results
</h2>
<blockquote>
	<html:errors />
	<logic:messagesPresent message="true">
		<ul>
			<font color="red"> <html:messages id="msg" message="true" bundle="search">
					<li>
						<bean:write name="msg" />
					</li>
				</html:messages> </font>
		</ul>
	</logic:messagesPresent>
	<%--show sample results --%>
	<bean:define id="showAliquot" name="showAliquot" type="java.lang.Boolean" />
	<logic:equal name="showAliquot" value="false">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td width="55" align="left" class="dataTablePrimaryLabel">
					Sample ID
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					Sample Accessioned Date <strong></strong>
				</td>
				<td width="41" align="left" class="dataTablePrimaryLabel">
					Sample Type
				</td>
				<td width="44" align="left" class="dataTablePrimaryLabel">
					Sample Location
				</td>
				<td width="48" align="left" class="dataTablePrimaryLabel">
					Sample Submitter
				</td>
				<td width="102" align="center" class="dataTablePrimaryLabel">
					Actions
				</td>
			</tr>

			<c:set var="rowNum" value="-1" />
			<logic:iterate name="samples" id="sample" type="gov.nih.nci.calab.dto.administration.SampleBean" indexId="sampleNum">
				<logic:iterate name="sample" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="containerNum">
					<c:set var="rowNum" value="${rowNum+1}" />
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
								&nbsp;
								<bean:write name="sample" property="sampleName" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								&nbsp;
								<bean:write name="sample" property="accessionDate" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								&nbsp;
								<bean:write name="sample" property="sampleType" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								&nbsp;
								<logic:present name="container">
									<bean:write name="container" property="storageLocationStr" />
								</logic:present>
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								&nbsp;
								<bean:write name="sample" property="sampleSubmitter" />
							</div>
						</td>
						<td class="${style}" valign="bottom">
							<%java.util.Map viewSampleDetailParams = new java.util.HashMap();
			viewSampleDetailParams.put("sampleNum", sampleNum);
			viewSampleDetailParams.put("containerNum", containerNum);
			viewSampleDetailParams.put("showAliquot", showAliquot);

			pageContext.setAttribute("viewSampleDetailParams",
					viewSampleDetailParams);%>
							<div align="center">
								&nbsp;
								<html:link action="viewSampleDetail" name="viewSampleDetailParams">View</html:link>
							</div>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</table>
	</logic:equal>
	<logic:equal name="showAliquot" value="true">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td width="55" align="left" class="dataTablePrimaryLabel">
					Sample ID
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					Sample Accessioned Date
				</td>
				<td width="41" align="left" class="dataTablePrimaryLabel">
					Sample Type
				</td>
				<td width="44" align="left" class="dataTablePrimaryLabel">
					Sample Location
				</td>
				<td width="55" align="left" class="dataTablePrimaryLabel">
					Aliquot ID
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					Aliquoted Date
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					Aliquoted Location
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					Aliquoted Creator
				</td>
				<td width="102" align="center" class="dataTablePrimaryLabel">
					Actions
				</td>
			</tr>
			<logic:iterate name="aliquots" id="aliquot" type="gov.nih.nci.calab.dto.administration.AliquotBean" indexId="aliquotNum">
				<c:choose>
					<c:when test="${aliquotNum % 2 == 0}">
						<c:set var="style" value="formLabelGrey" />
					</c:when>
					<c:otherwise>
						<c:set var="style" value="formLabelWhite" />
					</c:otherwise>
				</c:choose>
				<tr>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="sample.sampleName" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="sample.accessionDate" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="sample.sampleType" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="sample.sampleSubmitter" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="aliquotName" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="creationDate" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="container.storageLocationStr" />
						</div>
					</td>
					<td class="${style}" valign="top">
						<div align="left">
							&nbsp;
							<bean:write name="aliquot" property="creator" />
						</div>
					</td>
					<td class="${style}">
						<%java.util.Map viewAliquotDetailParams = new java.util.HashMap();
			viewAliquotDetailParams.put("aliquotNum", aliquotNum);
			viewAliquotDetailParams.put("showAliquot", showAliquot);
			pageContext.setAttribute("viewAliquotDetailParams",
					viewAliquotDetailParams);%>
						<div align="center">
							&nbsp; <span class="${style}"><html:link action="viewSampleDetail" name="viewAliquotDetailParams">View</html:link></span>
						</div>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:equal>
	<br>
	<br>
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr valign="bottom">
			<td align="right">
				<input type="button" onClick="javascript:history.go(-1);" value="Back">
			</td>
		</tr>
	</table>
	<br>
	<br>
</blockquote>


