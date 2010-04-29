<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="gov.nih.nci.cananolab.util.Constants"%>
<%@page import="gov.nih.nci.cananolab.util.PropertyUtils"%>
<table class="subhdrBG" cellspacing="0" cellpadding="0" width="100%" border="0">
	<tbody>
		<c:if test="${!empty siteName}">
			<tr>
				<td colspan="2" class="subMenuPrimaryTitle" height="21">
					${siteName}
				</td>
			</tr>
		</c:if>	
		<tr>
			<td class="subhdrBG" align="left" width="543" background="images/background.gif" height="60">
				<div align="left">
					<img width="304" height="83" src="images/appLogo-nanolab.gif">
				</div>
			</td>
			<c:if test="${hasSiteLogo}">
				<td class="subhdrBG" align="left" width="543" background="images/background.gif" >
					<div align="right">
						<img src="admin.do?dispatch=getSiteLogo">
					</div>
				</td>
			</c:if>
		</tr>
	</tbody>
</table>