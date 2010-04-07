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
					Type
				</td>
				<td class="cellLabel">
					Isotype
				</td>
			</tr>
			<tr>
				<td>
					${functionalizingEntity.biopolymer.type}
				</td>
				<td>
					${functionalizingEntity.biopolymer.sequence}
				</td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table width="100%" align="center" class="submissionView">
			<tr>
				<th colspan="4">
					Biopolymer Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel">
					Type*
				</td>
				<td colspan="2">
					<div id="biopolymerTypePrompt">
						<select name="functionalizingEntity.biopolymer.type"
							id="biopolymerType"
							onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType', 'biopolymerTypePrompt');">
							<option value=""></option>
							<c:forEach var="type" items="${biopolymerTypes}">
								<c:choose>
									<c:when
										test="${type eq compositionForm.map.functionalizingEntity.biopolymer.type}">
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
								[other]
							</option>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<td class="cellLabel">
					Sequence
				</td>
				<td colspan="2">
					<textarea name="functionalizingEntity.biopolymer.sequence"
						cols="80" rows="3"></textarea>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>