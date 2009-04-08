<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" align="center" class="submissionView">
	<tr>
		<th colspan="4">
			Emulsion Properties
		</th>
	</tr>
	<tr>
		<td class="cellLabel">
			Is Polymerized
		</td>
		<td class="cellLabel">
			<select name="entity.emulsion.polymerized">
				<option value=""></option>
				<c:choose>
					<c:when
						test="${nanomaterialEntityForm.map.entity.emulsion.polymerized eq 'true'}">
						<option value="1" selected>
							Yes
						</option>
					</c:when>
					<c:otherwise>
						<option value="1">
							Yes
						</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when
						test="${nanomaterialEntityForm.map.entity.emulsion.polymerized eq 'false'}">
						<option value="0" selected>
							No
						</option>
					</c:when>
					<c:otherwise>
						<option value="">
							No
						</option>
					</c:otherwise>
				</c:choose>
			</select>
		</td>
		<td class="cellLabel">
			Polymer Name
		</td>
		<td class="cellLabel">
			<input type="text" name="entity.emulsion.polymerName"
				value="${nanomaterialEntityForm.map.entity.emulsion.polymerName}" />
		</td>
	</tr>
</table>
<br>