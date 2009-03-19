<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				${fn:toUpperCase(param.location)} ${sampleForm.map.sampleBean.domain.name}
				General Info
			</h3>
		</td>
		<td align="right" width="15%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="manage_nanoparticles_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3"
				width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="2">
							<div align="justify">
								Sample Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Sample Name </strong>
						</td>
						<td class="rightLabel">
							<bean:write name="sampleForm"
								property="sampleBean.domain.name" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Sample<br>Point of Contact </strong>
						</td>
						<td class="rightLabel">
							<bean:write name="sampleForm"
								property="sampleBean.pocBean.displayName" />
							&nbsp;
							<c:if
								test="${!sampleBean.pocBean.hidden}">
								<c:if
									test="${sampleForm.map.sampleBean.pocBean.displayName ne 'private' }">
									<c:url var="url" value="submitPointOfContact.do">
										<c:param name="page" value="0" />
										<c:param name="dispatch" value="detailView" />
										<c:param name="sampleId" value="${param.sampleId}" />
										<c:param name="pocId" value="${sampleForm.map.sampleBean.pocBean.domain.id}" />
										<c:param name="location" value="${param.location}" />
									</c:url>
									<a href="${url}">View Detail </a>
								</c:if>
							</c:if>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords</strong> <i>(one keyword per line)</i>
						</td>
						<td class="rightLabel">
							<c:forEach var="keyword"
								items="${sampleForm.map.sampleBean.keywordSet}">
							${keyword}
							<br>
							</c:forEach>
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
