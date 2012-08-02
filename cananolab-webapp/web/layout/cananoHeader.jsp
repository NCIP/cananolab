<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="subhdrBG" cellspacing="0" cellpadding="0" width="100%" border="0" summary="layout">
	<tbody>
		<c:if test="${!empty existingSiteBean && !empty existingSiteBean.siteName}">
			<tr>
				<td colspan="2" class="subMenuPrimaryTitle">
					<c:out value="${existingSiteBean.siteName}"/> 
				</td>
			</tr>
		</c:if>	
		<tr>
			<td class="subhdrBG" align="left">				
				<img src="images/appLogo-nanolab.gif" alt="caNanoLab logo">
			</td>
			<c:if test="${!empty existingSiteBean && !empty existingSiteBean.siteLogoFilename}">
				<td class="subhdrBG">			
					<div align="right">
						<img src="admin.do?dispatch=getSiteLogo&page=0">
					</div>					
				</td> 
			</c:if>
		</tr>
	</tbody>
</table>