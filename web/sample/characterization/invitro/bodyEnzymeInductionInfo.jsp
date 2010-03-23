<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
	<c:when test="${param.summary eq 'true'}">
		<c:choose>
			<c:when test="${! empty charBean.enzymeInduction.enzyme}">
				<table class="summaryViewNoGrid" align="left">
					<tr>
						<td class="cellLabel">
							Enzyme Name
						</td>
						<td>
							${charBean.enzymeInduction.enzyme}
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
					Enzyme Induction Properties
				</th>
			</tr>
			<tr>
				<td class="cellLabel" width="20%">
					Enzyme Name
				</td>
				<td>
					<div id="enzymePrompt">
						<select name="achar.enzymeInduction.enzyme" id="enzyme"
							onchange="javascript:callPrompt('Enzyme', 'enzyme', 'enzymePrompt');" />
							<option value=""></option>
							<c:forEach var="name" items="${enzymeNames}">
								<c:choose>
									<c:when
										test="${name eq characterizationForm.map.achar.enzymeInduction.enzyme}">
										<option value="${name}" selected="selected">
											${name}
										</option>
									</c:when>
									<c:otherwise>
										<option value="${name}">
											${name}
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
		</table>
		<br>
	</c:otherwise>
</c:choose>

