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
					${nanomaterialEntity.polymer.initiator}
				</td>
				<td>
					${nanomaterialEntity.polymer.crossLinked}
				</td>
				<td>
					${nanomaterialEntity.polymer.crossLinkDegree}
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
					<select name="nanomaterialEntity.polymer.crossLinked">
						<option value="">
						</option>
						<c:choose>
							<c:when
								test="${compositionForm.map.nanomaterialEntity.polymer.crossLinked eq 'true'}">
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
								test="${compositionForm.map.nanomaterialEntity.polymer.crossLinked eq 'false'}">
								<option value="0" selected>
									No
								</option>
							</c:when>
							<c:otherwise>
								<option value="0">
									No
								</option>
							</c:otherwise>
						</c:choose>
					</select>
				</td>
				<td class="cellLabel">
					Cross Link Degree
				</td>
				<td class="cellLabel">
					<input type="text"
						id="crossLinkDegree"
						name="nanomaterialEntity.polymer.crossLinkDegree"
						<%--onkeydown="return filterFloatNumber(event)"--%>
						value="${compositionForm.map.nanomaterialEntity.polymer.crossLinkDegree}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>