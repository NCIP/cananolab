<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<br>
<logic:present parameter="type">
	<logic:equal parameter="type" value="Input">
		<bean:define id="actions" name="inActions" type="java.util.List" />
	</logic:equal>
	<logic:equal parameter="type" value="Output">
		<bean:define id="actions" name="outActions" type="java.util.List" />
	</logic:equal>
	<logic:equal parameter="type" value="assay">
		<bean:define id="actions" name="assayActions" type="java.util.List" />
	</logic:equal>
	<logic:equal parameter="type" value="upload">
		<bean:define id="actions" name="uploadActions" type="java.util.List" />
	</logic:equal>
	<logic:present name="actions">
		<table>
			<tr>
				<logic:iterate name="actions" id="item" type="org.apache.struts.tiles.beans.MenuItem">
					<td class="formLabelGrey">
						<c:url var="linkVal" value="${item.link}">
							<c:choose>
								<%-- only pass GET parameters --%>
								<c:when test="${pageContext.request.method eq 'GET'}">
									<c:forEach var="paramItem" items="${paramValues}" varStatus="ind">
										<c:choose>
											<%-- exclude first parameter if key=forwardPage --%>
											<c:when test="${ind.count>1 || paramItem.key ne 'forwardPage'}">
												<c:param name="${paramItem.key}" value="${paramItem.value[0]}" />
											</c:when>
										</c:choose>
									</c:forEach>
								</c:when>
								<c:when test="${item.value eq 'Upload Input File'}">
									<c:param name="type" value="Input"/>
								</c:when>
								<c:when test="${item.value eq 'Upload Output File'}">
									<c:param name="type" value="Output"/>
								</c:when>
							</c:choose>
						</c:url>
						<a href="${linkVal}"><bean:write name="item" property="value" /></a> &nbsp; &nbsp;
					</td>
				</logic:iterate>
			</tr>
		</table>
	</logic:present>

</logic:present>
