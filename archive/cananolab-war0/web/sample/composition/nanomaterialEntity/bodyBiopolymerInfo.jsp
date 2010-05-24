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
					Name
				</th>
				<th>
					Type
				</th>
				<th>
					Sequence
				</th>
			</tr>
			<tr>
				<td>
					${nanomaterialEntity.biopolymer.name}
				</td>
				<td>
					${nanomaterialEntity.biopolymer.type}
				</td>
				<td>
					${nanomaterialEntity.biopolymer.sequence}
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
					Name*
				</td>
				<td class="cellLabel">
					<input type="text" name="nanomaterialEntity.biopolymer.name"
						value="${compositionForm.map.nanomaterialEntity.biopolymer.name}">
				</td>
				<td class="cellLabel">
					Biopolymer Type*
				</td>
				<td class="cellLabel">
					<div id="biopolymerTypePrompt">
						<select name="nanomaterialEntity.biopolymer.type"
							id="biopolymerType"
							onchange="javascript:callPrompt('Biopolymer Type', 'biopolymerType', 'biopolymerTypePrompt');">
							<option value=""></option>
							<c:forEach var="type" items="${biopolymerTypes}">
								<c:choose>
									<c:when
										test="${type eq compositionForm.map.nanomaterialEntity.biopolymer.type}">
										<option value="${type}" selected="selected">
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
				<td class="cellLabel" colspan="3">
					<textarea name="nanomaterialEntity.biopolymer.sequence" cols="80"
						rows="3">${compositionForm.map.nanomaterialEntity.biopolymer.sequence}</textarea>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>