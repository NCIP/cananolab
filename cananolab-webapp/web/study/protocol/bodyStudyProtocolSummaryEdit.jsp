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
	<jsp:param name="pageTitle" value="Study Efficacy of nanoparticle Protocol" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<table  class="summaryViewNoGrid"
					align="center" width="100%">
	<tr>
		<th align="left" valign="baseline">
			Protocol &nbsp;&nbsp;
			<a href="studyProtocol.do?dispatch=add"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<th align="left"">
		</th>
	</tr>
	<tr>
		<td>
			<table width="100%" align="center" class="summaryViewNoGrid"
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
										GTA-1
									</td>
									<td><a href="protocol.do?dispatch=setupUpdate&protocolId=22129928">Edit</a></td>
								</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Abbreviation
										</td>
										<td colspan="2">
											GTA-1  
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Type
										</td>
										<td>
											in vitro assay
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Version
										</td>
										<td>
											1.0
										</td>
									</tr>
									<tr>
									<td class="cellLabel" width="10%">
										File
									</td>
									<td>
										MTT AND LDH RELEASE (PORCINE RENAL PROXIMAL TUBULE CELL)
									</td>
								</tr>
								
								<tr>
									<td class="cellLabel" width="10%">
										Description
									</td>
									<td>
										
									</td>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Created Date
									</td>
									<td>
										08-31-2007
									</td>
								</tr>
							</table>
							
					</td>
				</tr>
				
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
										GTA-1
									</td>
									<td><a href="protocol.do?dispatch=setupUpdate&protocolId=22129928">Edit</a></td>
								</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Abbreviation
										</td>
										<td colspan="2">
											GTA-1  
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Type
										</td>
										<td>
											in vitro assay
										</td>
									</tr>
								
									<tr>
										<td class="cellLabel" width="10%">
											Version
										</td>
										<td>
											1.0
										</td>
									</tr>
									<tr>
									<td class="cellLabel" width="10%">
										File
									</td>
									<td>
										MTT AND LDH RELEASE (PORCINE RENAL PROXIMAL TUBULE CELL)
									</td>
								</tr>
								
								<tr>
									<td class="cellLabel" width="10%">
										Description
									</td>
									<td>
										
									</td>
								</tr>
								<tr>
									<td class="cellLabel" width="10%">
										Created Date
									</td>
									<td>
										08-31-2007
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
</table>
