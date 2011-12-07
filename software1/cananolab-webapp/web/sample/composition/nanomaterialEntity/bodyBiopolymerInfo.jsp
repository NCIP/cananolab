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
					Name
				</td>
				<td class="cellLabel">
					Type
				</td>
				<td class="cellLabel">
					Sequence
				</td>
			</tr>
			<tr>
				<td>
					<c:out value="${nanomaterialEntity.biopolymer.name}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.biopolymer.type}"/>
				</td>
				<td>
					<c:out value="${nanomaterialEntity.biopolymer.sequence}"/>
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
											<c:out value="${type}"/>
										</option>
									</c:when>
									<c:otherwise>
										<option value="${type}">
											<c:out value="${type}"/>
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
						rows="3"><c:out value="${compositionForm.map.nanomaterialEntity.biopolymer.sequence}" escapeXml="false"/></textarea>
				</td>
			</tr>
		</table>
		<br>
	</c:otherwise>
</c:choose>