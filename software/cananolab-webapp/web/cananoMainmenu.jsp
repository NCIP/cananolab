<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table cellspacing="0" cellpadding="0" summary="" border="0">
	<tbody>
		<tr>
			<logic:present name="items">
				<logic:iterate name="items" id="item"
					type="org.apache.struts.tiles.beans.MenuItem">
					<c:choose>
						<c:when test="${fn:toUpperCase(menu) eq item.value}">
							<c:set var="style" value="mainMenuItemOver" />
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
							<td class="${style}" onclick="${item.link}"
								onmouseover="changeMenuStyle(this, 'mainMenuItemOver');" onmouseout="changeMenuStyle(this,'${style}')" height="20">
								<a class="mainMenuLink" href="#">${item.value}</a>
							</td>
							<td>
								<img height="16" alt="" src="images/mainMenuSeparator.gif"
									width="1">
							</td>
						</c:otherwise>
					</c:choose>
				</logic:iterate>
			</logic:present>
		</tr>
	</tbody>
</table>
