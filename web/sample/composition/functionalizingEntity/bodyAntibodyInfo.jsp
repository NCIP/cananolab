<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewLayer4" align="center" width="95%">
			<tr>
				<th>
					Type
				</th>
				<th>
					Isotype
				</th>
				<th>
					Species
				</th>
			</tr>
			<tr>
				<td>${functionalizingEntity.antibody.type}</td>
				<td>${functionalizingEntity.antibody.isotype}</td>
				<td>${functionalizingEntity.antibody.species}</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Antibody Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Type
				</td>
				<td>
					<select name="functionalizingEntity.antibody.type"
						id="antibodyType"
						onchange="javascript:callPrompt('Type', 'antibodyType');">
						<option value="" />
						<option value="other">
							[Other]
						</option>
					</select>
				</td>
				<td>
					Isotype
				</td>
				<td>
					<select name="functionalizingEntity.antibody.isotype"
						id="antibodyIsotype"
						onchange="javascript:callPrompt('Isotype', 'antibodyIsotype');">
						<option value="" />
						<option value="other">
							[Other]
						</option>
					</select>
				</td>
				<td>
					Species
				</td>
				<td>
					<select name="functionalizingEntity.antibody.species"
						id="antibodySpecies"
						onchange="javascript:callPrompt('Species', 'species');">
						<option value="" />
					</select>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>