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
					Is Polymerized
				</th>
				<th>
					Polymer Name
				</th>
			</tr>
			<tr>
				<td>
					${nanomaterialEntity.emulsion.polymerized}
				</td>
				<td>
					${nanomaterialEntity.emulsion.polymerName}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
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
					<c:choose>
						<c:when
							test="${empty compositionForm.map.nanomaterialEntity.emulsion.polymerized}">
							<c:set var="selectNoneStr" value="selected" />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${compositionForm.map.nanomaterialEntity.emulsion.polymerized eq 'true'}">
									<c:set var="selectYesStr" value="selected" />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value="selected" />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="nanomaterialEntity.emulsion.polymerized">
						<option value="" ${selectNoneStr}></option>
						<option value="1" ${selectYesStr}>
							Yes
						</option>
						<option value="0" ${selectNoStr}>
							No
						</option>
					</select>
				</td>
				<td class="cellLabel">
					Polymer Name
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.emulsion.polymerName"
						value="${compositionForm.map.nanomaterialEntity.emulsion.polymerName}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>