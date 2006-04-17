<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@	taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ page import="java.util.*,gov.nih.nci.calab.dto.workflow.*,gov.nih.nci.calab.dto.administration.*"%>

<tr>
	<td width="190" valign="top" class="subMenu">
		<!-- submenu begins -->
		<table summary="" cellpadding="0" cellspacing="0" border="0" width="220" height="100%">
			<tr>
				<td class="subMenuPrimaryTitle" height="21">
					ASSAY TEMPLATE
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="formMessage" height="100%">
					<table width="100%" height="95%" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="left" valign="top" class="formMessage">
								<div class="dtree" style="white-space: nowrap;">								
									<script type="text/javascript">
									
							<!--
							    d = new dTree('d');								
								d.config.target="";								
								d.add(0,-1,'Workflow');
								
								<c:set var="assaySeq" value="${fn:length(allAssayTypes)}"/>
								<c:set var="runSeq" value="${assaySeq+sessionScope.workflow.assayCount}"/>		
								<c:set var="aliquotSeq" value="${runSeq+sessionScope.workflow.runCount*3+2}"/>
								<c:set var="inputFileSeq" value="${aliquotSeq+sessionScope.workflow.aliquotCount}"/>
								<c:set var="outFileSeq" value="${aliquotSeq+sessionScope.workflow.inputFileCount}"/>
													    		
								<c:forEach var="assayType" items="${allAssayTypes}" varStatus="assayTypeNum">
								    d.add(${assayTypeNum.count}, 0, '${assayType}','javascript:void(0)', '', '', '');									    

								    <c:forEach var="assay" items="${sessionScope.workflow.assayBeanMap[assayType]}">
    								    <c:set var="assaySeq" value="${assaySeq+1}"/>
								        d.add(${assaySeq},${assayTypeNum.count},'${assay.assayName}','javascript:gotoPage(\'/calab/workflowForward.do?type=assay\')', '', '', '');
	 									
										<c:forEach var="run" items="${assay.runBeans}">																
										    <c:set var="runSeq" value="${runSeq+1+2}"/>
  											d.add(${runSeq},${assaySeq}, '${run.name}','javascript:void(0)'); 											  				
  											d.add(${runSeq+1},${runSeq},'In','javascript:gotoPage(\'/calab/workflowForward.do?type=in&runId=${run.id}\')');
  											d.add(${runSeq+2},${runSeq},'Out','javascript:gotoPage(\'/calab/workflowForward.do?type=out&runId=${run.id}\')');
											 											
											<c:forEach var="aliquot" items="${run.aliquotBeans}">
  											    <c:set var="aliquotSeq" value="${aliquotSeq+1}"/>  											
												d.add(${aliquotSeq}, ${runSeq+1},'${aliquot.aliquotName}','javascript:gotoPage(\'/calab/viewAliquot.do?aliquotId=${aliquot.aliquotId}\')', '', '', '');
  											</c:forEach>
  											
											<c:forEach var="inputFile" items="${run.inputFileBeans}">
  											    <c:set var="inputFileSeq" value="${inputFileSeq+1}"/>
                                                d.add(${inputFileSeq},${runSeq+1},'${inputFile.filename}', 'javascript:gotoPage(\'${inputFile.path}\')');
  											</c:forEach>
  											
  											<c:forEach var="outputFile" items="${run.outputFileBeans}">
  											   <c:set var="outputFileSeq" value="${outputFileSeq+1}"/>  											
								                d.add(${outputFileSeq},${runSeq+2},'${outputFile.filename}', 'javascript:gotoPage(\'${outputFile.path}\')');
  											</c:forEach> 
                                        </c:forEach>
									</c:forEach>
								</c:forEach>							
								document.write(d);								
							//-->
							</script>
								</div>
								<p>
									&nbsp;				
								</p>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="subMenuFooter" height="22">
					&nbsp;
				</td>
			</tr>
		</table>
	</td>
