<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">

<html:form action="/submitProtocol" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					Site Preference
				</h3>
			</td>
			<td align="right" width="20%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="submit_protocol_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<jsp:include page="/bodyMessage.jsp?bundle=admin" />
				<table width="100%" align="center" class="submissionView">
					<tr>
						<td class="cellLabel">
							Upload Logo File
						</td>
						<td colspan="3">
							<html:file property="protocol.fileBean.uploadedFile"
								onchange="javascript:writeLink(null);" />
							&nbsp;&nbsp;
							(Recommended file size: 299 * 80)
							<%--
							<span id="protocolFileLink"> <c:if
									test="${!empty submitProtocolForm.map.protocol.fileBean.domainFile.uri }">&nbsp;&nbsp;
									<a
										href="searchProtocol.do?dispatch=download&amp;fileId=${submitProtocolForm.map.protocol.fileBean.domainFile.id}&amp;location=local">
										${submitProtocolForm.map.protocol.fileBean.domainFile.uri }</a>
								</c:if> </span>&nbsp;
							 --%>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Site Name
						</td>
						<td colspan="3">
							<html:text styleId="fileTitle"
								property="protocol.fileBean.domainFile.title" size="50" />
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<table width="498" height="15" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="15">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset"
													onclick="javascript:location.href='admin.do?dispatch=setup&page=0'">
												<input type="hidden" name="dispatch" value="sitePreference">
												<input type="hidden" name="page" value="2">
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
			</td>
		</tr>
	</table>
</html:form>
