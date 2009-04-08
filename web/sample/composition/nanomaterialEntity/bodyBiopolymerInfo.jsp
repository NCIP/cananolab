<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center" class="submissionView">
	<tr>
		<th colspan="4">
			Biopolymer Properties
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			Name*
		</td>
		<td class="cellLabel">
			<input type="text" name="entity.biopolymer.name"
				value="${nanomaterialEntityForm.map.entity.biopolymer.name}">
		</td>
		<td class="cellLabel">
			Biopolymer Type*
		</td>
		<td class="cellLabel">
			<select name="entity.biopolymer.type" id="biopolymerType"
				onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType');">
				<option value=""></option>
				<c:forEach var="type" items="${biopolymerTypes}">
					<c:choose>
						<c:when
							test="${type eq nanomaterialEntityForm.map.entity.biopolymer.type}">
							<option value="${type}" selected>
								${type}
							</option>
						</c:when>
						<c:otherwise>
							<option value="${type}">
								${type}
							</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<option value="other">
					[Other]
				</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Sequence
		</td>
		<td class="cellLabel" colspan="3">
			<textarea name="entity.biopolymer.sequence" cols="80" rows="3">${nanomaterialEntityForm.map.entity.biopolymer.sequence}</textarea>
		</td>
	</tr>
</table>
<br>