<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${canUserSubmit eq 'true'}">
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
					<c:when test="${!empty allOtherParticleNames}">
						<tr>
							<td class="leftLabel" valign="top" width="30%">
								<strong>Copy to Other ${param.particleSource} Particles</strong>
							</td>
							<td class="label">
								<html:select property="otherParticles" size="6" multiple="true">
									<html:options name="allOtherParticleNames" />
								</html:select>
							</td>
							<td class="rightLabel" valign="top">
								<html:checkbox property="copyData" />
								<strong>Also copy derived data?</strong>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td class="completeLabel" colspan="3">
								There are no other particles from source ${particleSource} to
								copy characterization to.
							</td>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</c:when>
</c:choose>
