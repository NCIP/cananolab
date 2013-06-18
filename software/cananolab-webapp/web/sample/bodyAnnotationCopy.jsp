<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

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
						<strong><label for="otherSamples">Copy to other samples with the same primary organization?</label></strong>
					</td>
					<td width="20%">
						<html:select property="otherSamples" size="10" multiple="true" styleId="otherSamples">
							<html:options name="otherSampleNames"/>
						</html:select>
					</td>
					<td>
						<c:if test="${param.annotation eq 'characterization'}">
							<html:checkbox property="copyData" styleId="copyData"/>
							<strong><label for="copyData">Also copy finding data and conditions ?</label></strong>
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
