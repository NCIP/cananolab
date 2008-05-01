<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				${nanoparticleSampleForm.map.particleSampleBean.domainParticleSample.name}
				General Info
			</h3>
		</td>
		<td align="right" width="15%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=annotate_nano_help')"
				class="helpText">Help</a>
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
								Nanoparticle Sample Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Nanoparticle Sample Name </strong>
						</td>
						<td class="rightLabel">
							<bean:write name="nanoparticleSampleForm"
								property="particleSampleBean.domainParticleSample.name" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Nanoparticle Sample Source </strong>
						</td>
						<td class="rightLabel">
							<bean:write name="nanoparticleSampleForm"
								property="particleSampleBean.domainParticleSample.source.organizationName" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords <em>(one keyword per line)</em> </strong>
						</td>
						<td class="rightLabel">
							<c:forEach var="keyword"
								items="${nanoparticleSampleForm.map.particleSampleBean.keywordSet}">
							${keyword}
							<br>
							</c:forEach>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel">
							<c:forEach var="visibility"
								items="${nanoparticleSampleForm.map.particleSampleBean.visibilityGroups}">
								<c:out value="${visibility}" />
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
