<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/SampleManager.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<html:form action="/sample">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Submit A New Sample
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_nano_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" align="center" class="submissionView">
					<tr>
						<th colspan="2">
							Sample Information
						</th>
					</tr>
					<tr>
						<td class="cellLabel" width="20%">
							Sample Name *
						</td>
						<td>
							<html:text property="sampleBean.domain.name" size="80" />
							<c:if test="${!empty sampleForm.map.sampleBean.domain.id}">
								<html:hidden styleId="sampleId" property="sampleBean.domain.id"
									value="${sampleForm.map.sampleBean.domain.id}" />
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Keywords
							<i>(one keyword per line)</i>
						</td>
						<td>
							<html:textarea property="sampleBean.keywordsStr" rows="6"
								cols="80" />
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Point of Contact *
						</td>
						<td
							<a style="" id="addPointOfContact" href="#"
								onclick="javascript:clearPointOfContact(); show('newPointOfContact');"><img
									align="top" src="images/btn_add.gif" border="0" /

							> </a></td>
					</tr>
					<c:if test="${!empty sampleForm.map.sampleBean.primaryPOCBean.domain.id || ! empty sampleForm.map.sampleBean.otherPOCBeans }">
					<tr>
						<td colspan="2">
								<c:set var="edit" value="true" />
								<%@ include file="bodyPointOfContactView.jsp"%>
						</td>
					</tr>
					</c:if>
					<tr>
						<td colspan="2">
							<c:set var="newPOCStyle" value="display:none" />
							<%--
							<c:if
								test="${fn:length(compositionForm.map.nanomaterialEntity.composingElements)==0}">
								<c:set var="newPOCStyle" value="display:block" />
							</c:if>
							--%>
							<div style="display:none" id="newPointOfContact">
								<a name="submitPointOfContact"><%@ include
										file="bodySubmitPointOfContact.jsp"%></a>
							</div>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Visibility
						</td>
						<td>
							<html:select property="sampleBean.visibilityGroups"
								styleId="visibilityGroup" multiple="true" size="6">
								<html:options name="allVisibilityGroupsNoOrg" />
							</html:select>
							<br>
							<i>(${applicationOwner}_Researcher and
								${applicationOwner}_DataCurator are always selected by default.)</i>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0">
					<tr>
						<td width="30%">
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<c:set var="origUrl"
													value="sample.do?page=0&sampleId=${sampleId}&dispatch=${param.dispatch}&location=${applicationOwner}" />
												<input type="reset" value="Reset"
													onclick="javascript:window.location.href='${origUrl}'">
												<input type="hidden" name="dispatch" value="create">
												<input type="hidden" name="page" value="1">
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>

