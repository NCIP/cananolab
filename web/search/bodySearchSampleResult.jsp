<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h3>
	<br>
	<c:choose>
		<c:when test="${requestScope.showAliquot eq 'false'}">
			Sample 
	  </c:when>
		<c:otherwise>
	  		Aliquot
	  </c:otherwise>
	</c:choose>
	Search Results
</h3>
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
	<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr valign="bottom">
			<td align="right">
				<input type="button" onClick="javascript:history.go(-1);" value="Back">
			</td>
		</tr>
	</table>
	<br>
	<%--show sample results --%>

	<c:choose>
		<c:when test="${requestScope.showAliquot eq 'false'}">
			<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<div align="center">
					<td width="55" class="formTitle">
						Sample ID
					</td>
					<td width="76" class="formTitle">
						Sample Accessioned Date <strong></strong>
					</td>
					<td width="41" class="formTitle">
						Sample Type
					</td>
					<td width="44" class="formTitle">
						Sample Location
					</td>
					<td width="48" class="formTitle">
						Sample Creator
					</td>
					<td width="102" class="formTitle">
						Actions
					</td>
					</div>
				</tr>

				<c:set var="rowNum" value="-1" />
				<logic:iterate name="samples" id="sample" type="gov.nih.nci.calab.dto.administration.SampleBean" indexId="sampleNum">
					<logic:iterate name="sample" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="containerNum">
						<c:set var="rowNum" value="${rowNum+1}" />
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
								<bean:write name="sample" property="sampleName" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="sample" property="accessionDate" />
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="sample" property="sampleType" />
								&nbsp;
							</td>
							<td class="${style}">
								<logic:present name="container">
									<bean:write name="container" property="storageLocationStr" />
								</logic:present>
								&nbsp;
							</td>
							<td class="${style}">
								<bean:write name="sample" property="sampleSubmitter" />
								&nbsp;
							</td>
							<td class="${style}" valign="bottom">
								<c:url var="viewSampleDetailURL" value="/viewSampleDetail.do">
									<c:param name="sampleNum" value="${pageScope.sampleNum}" />
									<c:param name="containerNum" value="${pageScope.containerNum}" />
									<c:param name="showAliquot" value="${requestScope.showAliquot}" />
								</c:url>
								<a href="${viewSampleDetailURL}">View Details</a> &nbsp;

								<c:url value="/searchSample.do" var="showAliquotURL">
									<c:param name="showAliquot" value="true" />
									<c:param name="searchName" value="${sample.sampleName}-*" />
								</c:url>
								<a href="${showAliquotURL}">Show Aliquots</a> &nbsp;
							</td>
						</tr>
					</logic:iterate>
				</logic:iterate>
			</table>
		</c:when>
		<c:otherwise>
			<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<div align="center">
					<td width="55" class="formTitle">
						Sample ID
					</td>
					<td width="76" class="formTitle">
						Sample Accessioned Date
					</td>
					<td width="41" class="formTitle">
						Sample Type
					</td>
					<td width="44" class="formTitle">
						Sample Submitter
					</td>
					<td width="75" class="formTitle">
						Aliquot ID
					</td>
					<td width="76" class="formTitle">
						Aliquoted Date
					</td>
					<td width="76" class="formTitle">
						Aliquoted Location
					</td>
					<td width="76" class="formTitle">
						Aliquoted Creator
					</td>
					<td width="82" class="formTitle">
						Actions
					</td>
				</tr>
				<logic:iterate name="aliquots" id="aliquot" type="gov.nih.nci.calab.dto.administration.AliquotBean" indexId="aliquotNum">
					<c:choose>
						<c:when test="${aliquotNum % 2 == 0}">
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
							<bean:write name="aliquot" property="sample.sampleName" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="sample.accessionDate" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="sample.sampleType" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="sample.sampleSubmitter" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="aliquotName" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="creationDate" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="container.storageLocationStr" />
							&nbsp;
						</td>
						<td class="${style}">
							<bean:write name="aliquot" property="creator" />
							&nbsp;
						</td>
						<td class="${style}">
							<c:url var="viewAliquotDetailURL" value="/viewSampleDetail.do">
								<c:param name="aliquotNum" value="${pageScope.aliquotNum}" />
								<c:param name="showAliquot" value="${requestScope.showAliquot}" />
							</c:url>
							<a href="${viewAliquotDetailURL}">View Details</a>&nbsp;
						</td>
					</tr>
				</logic:iterate>
			</table>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
</blockquote>
