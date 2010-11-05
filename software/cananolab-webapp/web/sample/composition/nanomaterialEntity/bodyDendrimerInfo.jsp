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
					Branch
				</td>
				<td class="cellLabel">
					Generation
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.dendrimer.branch}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.dendrimer.generation}"/>
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Dendrimer Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Branch
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.dendrimer.branch"
						value="${compositionForm.map.nanomaterialEntity.dendrimer.branch}" />
				</td>
				<td class="cellLabel">
					Generation
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.dendrimer.generation"
						value="${compositionForm.map.nanomaterialEntity.dendrimer.generation}" />
				</td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>
<br>