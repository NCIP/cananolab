<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center" class="submissionView">
	<tbody>
		<c:choose>
			<c:when test="${!empty otherSampleNames}">
				<tr>
					<td width="20%">
						<strong>Copy to other samples with the same primary point
							of contact</strong>
					</td>
					<td>
						<html:select property="otherSamples" size="10" multiple="true">
							<html:options collection="otherSampleNames" property="name"
								labelProperty="name" />
						</html:select>
					</td>
					<td>
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
					<td colspan="3">
						There are no other samples to copy annotation to.
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
