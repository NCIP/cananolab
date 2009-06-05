<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when
		test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.transfection.cellLine}">
				<table class="summaryViewLayer4" align="center" width="95%">
					<tr>
						<th>
							Cell Line
						</th>
					</tr>
					<tr>
						<td>
							${charBean.transfection.cellLine}
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
					Transfection Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Cell Line
				</td>
				<td>
					<textarea name="achar.transfection.cellLine" rows="2" cols="80">${characterizationForm.map.achar.transfection.cellLine}
			</textarea>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>

