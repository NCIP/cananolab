<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr>
			<td class="formTitle" colspan="3">
				<div align="justify">
					&nbsp;
				</div>
			</td>
		</tr>
		<c:choose>
			<c:when test="${!empty otherParticleNames}">
				<tr>
					<td class="leftLabel" valign="top" width="30%">
						<strong>Copy to other samples with the same
							primary point of contact</strong>
					</td>

					<td class="label">
						<html:select property="otherParticles" size="10" multiple="true">
							<html:options collection="otherParticleNames" property="name"
								labelProperty="name" />
						</html:select>
					</td>
					<td class="rightLabel" valign="top">
						<c:if test="${param.annotation eq 'characterization'}">
							<html:checkbox property="copyData" />
							<strong>Also copy characterization results?</strong>
						</c:if>
						&nbsp;
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td class="completeLabel" colspan="3">
						There are no other particles to copy annotation to.
					</td>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
