<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<table class="summaryViewNoGrid" align="left">
			<tr>
				<td class="cellLabel">
					Initiator
				</td>
				<td class="cellLabel">
					Is Cross Linked
				</td>
				<td class="cellLabel">
					Cross Link Degree
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.polymer.initiator}" />
				</td>
				<td>
					<c:out value="${nanomaterialEntity.polymer.crossLinked}" />
				</td>
				<td>
					<c:out value="${nanomaterialEntity.polymer.crossLinkDegree}" />
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="6">
					Polymer Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Initiator
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.polymer.initiator"
						value="${compositionForm.map.nanomaterialEntity.polymer.initiator}" />
				</td>
				<td class="cellLabel">
					Is Cross Linked
				</td>
				<td class="cellLabel">
					<c:choose>
						<c:when
							test="${empty compositionForm.map.nanomaterialEntity.isCrossLinked}">
							<c:set var="selectNoneStr" value='selected="selected"' />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${compositionForm.map.nanomaterialEntity.isCrossLinked eq 'true'}">
									<c:set var="selectYesStr" value='selected="selected"' />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value='selected="selected"' />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="nanomaterialEntity.isCrossLinked">
						<option value=""${selectNoneStr}></option>
						<option value="true"${selectYesStr}>
							Yes
						</option>
						<option value="false"${selectNoStr}>
							No
						</option>
					</select>
				</td>
				<td class="cellLabel">
					Cross Link Degree
				</td>
				<td class="cellLabel">
					<input type="text" id="crossLinkDegree"
						name="nanomaterialEntity.polymer.crossLinkDegree"
						<%--onkeydown="return filterFloatNumber(event)"--%>
						value="${compositionForm.map.nanomaterialEntity.polymer.crossLinkDegree}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>