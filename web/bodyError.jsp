<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<tr>
	<td width="100%" valign="top">
		<!-- target of anchor to skip menus -->
		<table summary="" cellpadding="0" cellspacing="0" border="0" width="600" height="100%">
			<!-- banner begins -->
			<!-- banner begins -->
			<tr>
				<td height="100%">
					<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
						<tr>
							<td width="100%" valign="top">
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<td class="contentPage">
											<logic:messagesPresent>
												<html:messages id="err" message="true" property="error">
													<bean:write name="err" />
													<br />
												</html:messages>
										    </logic:messagesPresent>
										    <br>
										    <logic:messagesPresent message="true">
												<html:messages id="msg" message="true" property="message">
													<bean:write name="msg" />
													<br />
												</html:messages>
											</logic:messagesPresent>
										</td>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
</tr>
<!--_____ main content ends _____-->
