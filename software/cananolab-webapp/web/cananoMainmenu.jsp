<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table cellspacing="0" cellpadding="0" border="0">
	<tbody>
		<tr>
		    <td width="1">
				<!-- anchor to skip main menu -->
				<a href="#content"><img height="1" alt="Skip Menu" src="images/shim.gif" width="1" border="0"></a>
			</td>		
			<logic:present name="items">
				<logic:iterate name="items" id="item"
					type="org.apache.struts.tiles.beans.MenuItem">
					<c:choose>
						<c:when test="${fn:toUpperCase(menu) eq item.value}">
							<c:set var="style" value="mainMenuItemSelected" />
						</c:when>
						<c:otherwise>
							<c:set var="style" value="mainMenuItem" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when
							test="${item.value eq 'LOGOUT' && sessionScope.user == null ||
							        item.value eq 'ADMINISTRATION' && (user==null || !user.admin) ||
							        item.value eq 'CURATION' && (user==null || !user.curator)||
							        item.value eq 'COMMUNITY' && user==null ||
							        item.value eq 'LOGIN' && sessionScope.user !=null||
							        item.value eq 'LOGIN' && pageContext.request.requestURI eq '/caNanoLab/login.jsp' ||
							        item.value eq 'RESULTS' && !(user.curator && hasResultsWaiting)}">
							<td></td>
						</c:when>
						<c:otherwise>
							<c:set var="link" value="${item.link}"/>
							<c:if test="${item.value eq 'HELP' || item.value eq 'GLOSSARY'}">
							   <c:set var="link" value="openHelpWindow('${webHelp}/${item.link}')"/>
							</c:if>
							<th class="${style}" onclick="${link}" scope="row"><a
								class="mainMenuLink" href="#">${item.value}</a>
							</th>
							<td><img height="16" alt="menu separator"
								src="images/mainMenuSeparator.gif" width="1">
							</td>
						</c:otherwise>
					</c:choose>
				</logic:iterate>
			</logic:present>
		</tr>
	</tbody>
</table>
