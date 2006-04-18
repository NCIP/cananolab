<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<logic:present parameter="type">
	<logic:equal parameter="type" value="in">
		<bean:define id="actions" name="inActions" type="java.util.List" />
	</logic:equal>
	<logic:equal parameter="type" value="out">
		<bean:define id="actions" name="outActions" type="java.util.List" />
	</logic:equal>
	<logic:equal parameter="type" value="assay">
		<bean:define id="actions" name="assayActions" type="java.util.List" />
	</logic:equal>
	<logic:present name="actions">
		<table>
			<tr>
				<logic:iterate name="actions" id="item" type="org.apache.struts.tiles.beans.MenuItem">
					<td class="formLabelGrey">
						<c:url var="linkVal" value="${item.link}">
							<c:forEach var="paramItem" items="${paramValues}">
							<%--
								<c:choose>
									<c:when test="${paramItem.key ne 'type'}">
									--%>
										<c:param name="${paramItem.key}" value="${paramItem.value[0]}" />
										<%--
									</c:when>
								</c:choose>--%>
							</c:forEach>
						</c:url>
						<a href="${linkVal}"><bean:write name="item" property="value" /></a> &nbsp; &nbsp;
					</td>
				</logic:iterate>
			</tr>
		</table>
	</logic:present>
</logic:present>
