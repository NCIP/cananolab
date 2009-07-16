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
					${nanomaterialEntity.liposome.polymerized}
				</td>
				<td>
					${nanomaterialEntity.liposome.polymerName}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Liposome Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Is Polymerized
				</td>
				<td class="cellLabel">
					<select name="nanomaterialEntity.liposome.polymerized">
						<option value="">
						</option>
						<c:choose>
							<c:when
								test="${compositionForm.map.nanomaterialEntity.liposome.polymerized eq 'true'}">
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
								test="${compositionForm.map.nanomaterialEntity.liposome.polymerized eq 'false'}">
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
					Polymer Name
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.liposome.polymerName"
						value="${compositionForm.map.nanomaterialEntity.liposome.polymerName}" />
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>