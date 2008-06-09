<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
<!--//
function confirmDeletion()
{
	answer = confirm("Are you sure you want to delete the characterization?")
	if (answer !=0)
	{
		this.document.forms[0].dispatch.value="delete";
		this.document.forms[0].page.value="1";
		this.document.forms[0].submit(); 
		return true;
	}
}
//-->
</script>
<br>
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
		<td width="30%">
			<span class="formMessage"> </span>
			<br>
			<c:choose>
				<c:when
					test="${param.dispatch eq 'setupUpdate'&& canUserDelete eq 'true' && location eq 'local'}">
					<table height="32" border="0" align="left" cellpadding="4"
						cellspacing="0">
						<tr>
							<td height="32">
								<div align="left">
									<input type="button" value="Delete"
										onclick="confirmDeletion();">
								</div>
							</td>
						</tr>
					</table>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<table height="32" border="0" align="right" cellpadding="4"
						cellspacing="0">
						<tr>
							<td width="490" height="32">
								<div align="right">			
									<c:choose>
										<c:when test="${'setup' eq param.dispatch }">
											<c:remove var="dataId" scope="session" />
										</c:when>										
										<c:when test="${'setupUpdate' eq param.dispatch }">
											<c:set var="dataId" value="${param.dataId}" scope="session" />
										</c:when>																			
									</c:choose>						
									<c:set var="origUrl" value="${actionName}.do?particleId=${particleId}&submitType=${submitType}&page=0&dispatch=setup&location=${location}" />
									<c:if test="${!empty dataId}">
										<c:set var="origUrl" value="${actionName}.do?particleId=${particleId}&submitType=${submitType}&page=0&dispatch=setupUpdate&location=${location}&dataId=${dataId}" />
									</c:if>												
									<input type="reset" value="Reset" onclick="javascript:window.location.href='${origUrl}'">
									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="2">
									<c:choose>
										<c:when test="${!empty param.particleId }">
											<html:hidden property="particleId"
												value="${param.particleId}" />
										</c:when>
										<c:otherwise>
											<html:hidden property="particleId" />
										</c:otherwise>
									</c:choose>									
									<input type="hidden" name="submitType"
										value="${param.submitType}" />
									<html:hidden property="achar.createdBy"
										value="${user.loginName }" />
									<html:submit />
								</div>
							</td>
						</tr>
					</table>
				</c:when>
			</c:choose>
			<div align="right"></div>
		</td>
	</tr>
</table>
