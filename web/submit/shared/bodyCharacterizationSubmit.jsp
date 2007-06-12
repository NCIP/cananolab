<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${canUserSubmit eq 'true'}">
		<br>
		<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr>
				<td width="30%">
					<span class="formMessage"> </span>
					<br>
					<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td width="490" height="32">
								<div align="right">
									<div align="right">
										<input type="reset" value="Reset" onclick="">
										<input type="hidden" name="dispatch" value="create">
										<input type="hidden" name="page" value="2">
										<c:choose>
											<c:when test="${canUserSubmit eq 'true'}">
												<html:hidden property="particleType" />
											</c:when>
										</c:choose>
										<html:submit />
									</div>
								</div>
							</td>
						</tr>
					</table>
					<div align="right"></div>
				</td>
			</tr>
		</table>
	</c:when>
</c:choose>
