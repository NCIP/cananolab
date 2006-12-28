<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="gov.nih.nci.calab.ui.core.InitSessionSetup,gov.nih.nci.calab.service.security.UserService"%>
<%
UserService userService = InitSessionSetup.getInstance()
					.getUserService();
userService.setFilteredMenuItem(session);
%>
<table cellspacing="0" cellpadding="0" summary="" border="0">
	<tbody>
		<tr>
			<%--
			<td width="1" valign="top">
				<!-- anchor to skip main menu -->
				<a href="#content"><img height="1" alt="Skip Menu" src="images/shim.gif" width="1" border="0"></a>
			</td>
        --%>
			<logic:present name="filteredItems">
				<logic:iterate name="filteredItems" id="item" type="org.apache.struts.tiles.beans.MenuItem">
					<c:choose>
						<c:when test="${fn:toUpperCase(menu) eq item.value || (menu eq 'search' && item.value eq 'SEARCH CHARACTERIZATIONS')}">
							<c:set var="style" value="mainMenuItemOver" />
						</c:when>
						<c:otherwise>
							<c:set var="style" value="mainMenuItem" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<%--when menu item has no link --%>
						<c:when test="${item.link eq ''}">
							<td class="${style}" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'${style}'),hideCursor()" height="20">
								<a class="mainMenuLink" href="#" onmouseover="s_show('${item.value}',event)" onmouseout="s_hide()">${item.value}</a>
							</td>
							<td>
								<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
						</c:when>
						<%--when menu item has link --%>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.value eq 'LOGOUT' && sessionScope.user == null}">
									<td></td>
								</c:when>
								<c:otherwise>
									<td class="${style}" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onclick="document.location.href='${item.link}'" onmouseout="changeMenuStyle(this,'${style}'),hideCursor()" height="20">
										<a class="mainMenuLink" href="${item.link}" onmouseover="s_show('${item.value}',event)" onmouseout="s_hide()">${item.value}</a>
									</td>
									<td>
										<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</logic:iterate>
			</logic:present>
			<logic:present name="alwaysShowItems">
				<logic:iterate name="alwaysShowItems" id="item" type="org.apache.struts.tiles.beans.MenuItem">
					<c:choose>
						<c:when test="${fn:toUpperCase(menu) eq item.value}">
							<c:set var="style" value="mainMenuItemOver" />
						</c:when>
						<c:otherwise>
							<c:set var="style" value="mainMenuItem" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<%--when menu item has no link --%>
						<c:when test="${item.link eq ''}">
							<td class="${style}" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'${style}'),hideCursor()" height="20">
								<a class="mainMenuLink" href="#" onmouseover="s_show('${item.value}',event)" onmouseout="s_hide()">${item.value}</a>
							</td>
							<td>
								<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
						</c:when>
						<%--when menu item has link --%>
						<c:otherwise>
							<c:choose>
								<c:when test="${item.value eq 'LOGOUT' && sessionScope.user == null}">
									<td></td>
								</c:when>
								<c:otherwise>
									<td class="${style}" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onclick="document.location.href='${item.link}'" onmouseout="changeMenuStyle(this,'${style}'),hideCursor()" height="20">
										<a class="mainMenuLink" href="${item.link}" onmouseover="s_show('${item.value}',event)" onmouseout="s_hide()">${item.value}</a>
									</td>
									<td>
										<img height="16" alt="" src="images/mainMenuSeparator.gif" width="1">
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</logic:iterate>
			</logic:present>
		</tr>
	</tbody>
</table>
