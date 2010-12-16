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
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Publication Summary" />
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
					<tr>
						<td>
							<table width="99%" align="center" class="summaryViewNoGrid"
								bgcolor="#dbdbdb">
								<tr>
									<th valign="top" align="left" height="6">Peer Review Report
									</th>
								</tr>
								<tr>
									<td>										
											<table class="summaryViewNoGrid" width="99%" align="center"
												bgcolor="#F5F5f5">
												<tr>
													<td class="cellLabel" width="10%">
														Bibliography Info
													</td>
													<td>
														Winter, PM, Neubauer, AM, Caruthers, SD, Harris, TD, Robertson, JD, Williams, TA, Schmieder, AH, Hu, G, Allen, JS, Lacy, EK, Zhang, H, Wickline, SA, Lanza, GM. Endothelial alpha(v)beta3 integrin-targeted fumagillin nanoparticles inhibit angiogenesis in atherosclerosis. Arteriosclerosis, thrombosis, and vascular biology. 2006; 26:2103-2109. PMID: 16825592.
													</td>
												</tr>
												
													<tr>
														<td class="cellLabel" width="10%">
															Research Category
														</td>
														<td colspan="2">
															animal<br/>
															characterization<br/>
															in vitro<br/>
															synthesis   
														</td>
													</tr>
												
													<tr>
														<td class="cellLabel" width="10%">
															Description
														</td>
														<td>
															<c:out value="${pubBean.description}" escapeXml="false" />
															&nbsp;
														</td>
													</tr>
												
													<tr>
														<td class="cellLabel" width="10%">
															Keywords
														</td>
														<td>
															*DRUG DELIVERY SYSTEMS<br/>
															*NANOSTRUCTURES<br/>
															ANGIOGENESIS INHIBITORS/*ADMINISTRATION & DOSAGE/PHARMACOLOGY<br/>
															ANIMALS<br/>
															AORTA, ABDOMINAL/PATHOLOGY<br/>
															ATHEROSCLEROSIS/COMPLICATIONS/DIAGNOSIS/*METABOLISM<br/>
															CYCLOHEXANES<br/>
															ENDOTHELIUM, VASCULAR/*METABOLISM<br/>
															FATTY ACIDS, UNSATURATED/*ADMINISTRATION & DOSAGE/PHARMACOLOGY<br/>
															HYPERLIPIDEMIAS/BLOOD<br/>
															INTEGRIN ALPHAVBETA3/*METABOLISM<br/>
															MAGNETIC RESONANCE IMAGING<br/>
															NEOVASCULARIZATION, PATHOLOGIC/ETIOLOGY/*PREVENTION & CONTROL<br/>
															RABBITS<br/>
															SESQUITERPENES
															&nbsp;
														</td>
													</tr>
												
												<tr>
													<td class="cellLabel" width="10%">
														Publication Status
													</td>
													<td>
														Publised
														&nbsp;
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
