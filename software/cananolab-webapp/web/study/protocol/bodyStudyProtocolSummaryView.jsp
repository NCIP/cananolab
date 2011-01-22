<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Protocols for Study ${studyName}" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<table  class="summaryViewNoGrid"
					align="center" width="99%">
	<tr>
		<th align="left"">
		</th>
	</tr>
	<c:forEach var="protocolBean" items="${studyProtocols}">
	<tr>
		<td>
			<table width="99%" align="center" class="summaryViewNoGrid"
				bgcolor="#dbdbdb">
				<tr>
					<th valign="top" align="left" height="6">
					</th>
				</tr>
				<tr>
					<td>										
							<table class="summaryViewNoGrid" width="99%" align="center"
								bgcolor="#F5F5f5">
								<tr>
									<td class="cellLabel" width="10%">
										Name
									</td>
									<td>
										${protocolBean.displayName}
									</td>
								</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Abbreviation
										</td>
										<td colspan="2">
											${protocolBean.domain.abbreviation}
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Type
										</td>
										<td>
											${protocolBean.domain.type}
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Version
										</td>
										<td>
											${protocolBean.domain.version}
										</td>
									</tr>
									<tr>
									<td class="cellLabel" width="10%">
										File
									</td>
									<td>
										${protocolBean.fileBean.keywordsStr}
									</td>
								</tr>
								
								<tr>
									<td class="cellLabel" width="10%">
										Description
									</td>
									<td>
										${protocolBean.fileBean.description}
									</td>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Created Date
									</td>
									<td>
										 ${protocolBean.createdDateStr}
									</td>
								</tr>
							</table>
							
					</td>
				</tr>
				<tr>
					<th valign="top" align="left" height="6">
					</th>
				</tr>
			</table>
		</td>
	</tr>
	</c:forEach>
</table>

