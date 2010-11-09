<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.surface.isHydrophobic}">
				<table class="summaryViewNoGrid" align="left">
					<tr>
						<td class="cellLabel">
							Is Hydrophobic?
						</td>
						<td>
							<c:out value="${charBean.surface.isHydrophobic}" />
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>N/A
	</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="2">
					Surface Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel" width="20%">
					Is Hydrophobic?
				</td>
				<td>
					<c:choose>
						<c:when
							test="${empty characterizationForm.map.achar.isHydrophobic}">
							<c:set var="selectNoneStr" value='selected="selected"' />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${characterizationForm.map.achar.isHydrophobic eq 'true'}">
									<c:set var="selectYesStr" value='selected="selected"' />
								</c:when>
								<c:otherwise>
									<c:set var="selectNoStr" value='selected="selected"' />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
					<select name="achar.isHydrophobic">
						<option value=""${selectNoneStr}></option>
						<option value="true"${selectYesStr}>
							Yes
						</option>
						<option value="false"${selectNoStr}>
							No
						</option>
					</select>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>