<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<h2>
	<br>
	Sample Search Results
</h2>
<blockquote>
	<table width="590" border="0" align="center" cellpadding="0" cellspacing="0">
		<logic:present name="samples">
			<tr>
				<td width="55" align="left" class="dataTablePrimaryLabel">
					Sample ID
				</td>
				<td width="76" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						Accessioned Date <strong></strong>
					</div>
				</td>
				<td width="41" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						Sample Type
					</div>
				</td>
				<td width="44" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						Location
					</div>
				</td>
				<td width="48" align="left" class="dataTablePrimaryLabel">
					<div align="left">
						Submitter
					</div>
				</td>
				<td width="102" align="left" class="dataTablePrimaryLabel">
					<div align="center">
						Actions
					</div>
				</td>
			</tr>
			<c:set var="rowNum" value="-1"/>
			<logic:iterate name="samples" id="sample" type="gov.nih.nci.calab.dto.administration.SampleBean" indexId="sampleNum">
				<logic:iterate name="sample" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="containerNum">
				<c:set var="rowNum" value="${rowNum+1}"/>
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
								<bean:write name="sample" property="sampleId" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								<bean:write name="sample" property="accessionDate" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								<bean:write name="sample" property="sampleType" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								<bean:write name="container" property="storageLocationStr" />
							</div>
						</td>
						<td class="${style}" valign="top">
							<div align="left">
								<bean:write name="sample" property="sampleSubmitter" />
							</div>
						</td>
						<td class="${style}" valign="bottom">
							<%java.util.Map viewSampleDetailParams = new java.util.HashMap();
			viewSampleDetailParams.put("sampleNum", sampleNum);
			viewSampleDetailParams.put("containerNum", containerNum);
			pageContext.setAttribute("viewSampleDetailParams",
					viewSampleDetailParams);%>
							<div align="center">
								<span class="${style}"><html:link action="viewSampleDetail" name="viewSampleDetailParams">View</html:link></span>
							</div>
						</td>
					</tr>
				</logic:iterate>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="samples">
			<tr>
				<td>
					<logic:messagesPresent message="true">
						<ul>
							<font color="red"> <html:messages id="msg" message="true" bundle="search">
									<li>
										<bean:write name="msg" />
									</li>
								</html:messages> </font>
						</ul>
					</logic:messagesPresent>
				</td>
			</tr>
		</logic:notPresent>
		<tr>
			<td colspan="8">
				<br>
				<br>
			</td>
		</tr>
		<tr valign="bottom" align="right">
			<td colspan="8">
				<input type="button" onClick="javascript:history.go(-1);" value="Back">
			</td>
		</tr>

	</table>
	<br>
	<br>
</blockquote>


