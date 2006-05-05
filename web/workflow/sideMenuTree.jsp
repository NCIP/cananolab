<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
								<c:set var="outputFileSeq" value="${aliquotSeq+sessionScope.workflow.inputFileCount}"/>
													    		
								<c:forEach var="assayType" items="${allAssayTypes}" varStatus="assayTypeNum">
								    d.add(${assayTypeNum.count}, 0, '${assayType}');									    

								    <c:forEach var="assay" items="${sessionScope.workflow.assayBeanMap[assayType]}">
    								    <c:set var="assaySeq" value="${assaySeq+1}"/>
								        d.add(${assaySeq},${assayTypeNum.count},'${assay.assayName}','javascript:gotoPage(\'initSession.do?forwardPage=createAssayRun&assayId=${assay.assayId}&assayName=${assay.assayName}\')', '', '', '');
	 									
										<c:forEach var="run" items="${assay.runBeans}">																
										    <c:set var="runSeq" value="${runSeq+1+2}"/>
  											d.add(${runSeq},${assaySeq}, '${run.name}'); 											  				
  											d.add(${runSeq+1},${runSeq},'In','javascript:gotoPage(\'workflowForward.do?type=in&runId=${run.id}&runName=${run.name}&inout=Input\')');
  											d.add(${runSeq+2},${runSeq},'Out','javascript:gotoPage(\'workflowForward.do?type=out&runId=${run.id}&inout=Output\')');
											 											
											<c:forEach var="aliquot" items="${run.aliquotBeans}">
  											    <c:set var="aliquotSeq" value="${aliquotSeq+1}"/>  	
  											    <c:choose>
  	    	      							       <c:when test="${aliquot.maskStatus eq 'Active'}">  											    								
												     d.add(${aliquotSeq}, ${runSeq+1},'${aliquot.aliquotName}','javascript:gotoPage(\'viewAliquot.do?aliquotId=${aliquot.aliquotId}\')', '', '', '');
												   </c:when>
												   <c:otherwise>
  												     d.add(${aliquotSeq}, ${runSeq+1},'<i>${aliquot.aliquotName}</i>','javascript:gotoPage(\'viewAliquot.do?aliquotId=${aliquot.aliquotId}\')', '', '', '');
												   </c:otherwise>
												</c:choose>
  											</c:forEach>
  											
											<c:forEach var="inputFile" items="${run.inputFileBeans}">
  											    <c:set var="inputFileSeq" value="${inputFileSeq+1}"/>
  											    <c:choose>
  	    	      							       <c:when test="${inputFile.fileMaskStatus eq 'Active'}">
                                                      d.add(${inputFileSeq},${runSeq+1},'${inputFile.shortFilename}', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/fileDownload.do?method=downloadFile&fileName=${inputFile.filename}&runId=${run.id}&inout=Input\')','${inputFile.filename}');
                                                   </c:when>
                                                   <c:otherwise>
                                                      d.add(${inputFileSeq},${runSeq+1},'<i>${inputFile.shortFilename}</i>', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/fileDownload.do?method=downloadFile&fileName=${inputFile.filename}&runId=${run.id}&inout=Input\')','${inputFile.filename}');
                                                   </c:otherwise>                                                   
                                                </c:choose>
  											</c:forEach>
  											
  											<c:forEach var="outputFile" items="${run.outputFileBeans}">
  											   <c:set var="outputFileSeq" value="${outputFileSeq+1}"/>    											   
  											   <c:choose>
  	    	      							       <c:when test="${outputFile.fileMaskStatus eq 'Active'}">											
 								                     d.add(${outputFileSeq},${runSeq+2},'${outputFile.shortFilename}', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/fileDownload.do?method=downloadFile&fileName=${outputFile.filename}&runId=${run.id}&inout=Output\')','${outputFile.filename}');
 								                   </c:when>
 								                   <c:otherwise>
	  								                 d.add(${outputFileSeq},${runSeq+2},'<i>${outputFile.shortFilename}</i>', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/fileDownload.do?method=downloadFile&fileName=${outputFile.filename}&runId=${run.id}&inout=Output\')','${outputFile.filename}');
 								                   </c:otherwise>
 								               </c:choose>
  											</c:forEach> 
                                        </c:forEach>
									</c:forEach>
								</c:forEach>							
								document.write(d);			
								d.config.closeSameLevel=true;
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
