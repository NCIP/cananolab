<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:choose>
	<c:when
		test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.surface.isHydrophobic}">
				<table class="summaryViewNoGrid" align="left">
					<tr>
						<td class="cellLabel">
							Is Hydrophobic?
						</td>					
						<td>
							<c:out value="${charBean.surface.isHydrophobic}"/>
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
					<select name="achar.surface.isHydrophobic">
						<option value="">
						</option>
						<c:choose>
							<c:when
								test="${characterizationForm.map.achar.surface.isHydrophobic eq 'true'}">
								<option value="1" selected="selected">
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
								test="${characterizationForm.map.achar.surface.isHydrophobic eq 'false'}">
								<option value="0" selected="selected">
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
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>