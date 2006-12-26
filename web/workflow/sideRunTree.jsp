<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ page import="java.util.*,gov.nih.nci.calab.dto.workflow.*,gov.nih.nci.calab.dto.inventory.*"%>

<!-- submenu begins -->
<table summary="" cellpadding="0" cellspacing="0" border="0" height="100%" width="250">
	<tr>
		<td class="subMenuPrimaryTitle" height="21">
			RUN TREE
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
d.add(0, -1, '${currentRun.sampleSourceName}');				
d.add(1, 0, '${currentRun.assayBean.assayType}');
d.add(2, 1, '${currentRun.assayBean.assayName}');
d.add(3,2,'${currentRun.name}', 'javascript:gotoPage(\'workflowForward.do?menuType=run\')', '','', 'images/tree/folder.gif');
					
<c:set var="aliquotSeq" value="5"/>
<c:set var="inputFileSeq" value="${aliquotSeq+fn:length(currentRun.aliquotBeans)}"/>
<c:set var="outputFileSeq" value="${aliquotSeq+fn:length(currentRun.inputFileBeans)}"/>

d.add(4,3,'In','javascript:gotoPage(\'workflowForward.do?menuType=in&inout=Input\')', '','', 'images/tree/folder.gif');
d.add(5,3,'Out','javascript:gotoPage(\'workflowForward.do?menuType=out&inout=Output\')', '','', 'images/tree/folder.gif');
								 											
<c:forEach var="aliquot" items="${currentRun.aliquotBeans}">
    <c:set var="aliquotSeq" value="${aliquotSeq+1}"/>  	
	    <c:choose>
	       <c:when test="${aliquot.maskStatus eq 'Active'}">  											    								
		     d.add(${aliquotSeq}, 4,'${aliquot.aliquotName}','javascript:gotoPage(\'viewAliquot.do?menuType=none&aliquotId=${aliquot.aliquotId}\')', '${aliquot.creator}, ${aliquot.creationDateStr}');
		   </c:when>
		   <c:otherwise>
  		     d.add(${aliquotSeq}, 4,'<i>${aliquot.aliquotName}</i>','javascript:gotoPage(\'viewAliquot.do?menuType=none&aliquotId=${aliquot.aliquotId}\')', '${aliquot.creator}, ${aliquot.creationDateStr}');
		   </c:otherwise>
		</c:choose>
</c:forEach>  					
				
<c:forEach var="inputFile" items="${currentRun.inputFileBeans}">
    <c:set var="inputFileSeq" value="${inputFileSeq+1}"/>
 		<c:choose>
           <c:when test="${inputFile.fileMaskStatus eq 'Active'}">
              d.add(${inputFileSeq},4,'${inputFile.shortFilename}', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/runFile.do?dispatch=downloadFile&fileName=${inputFile.filename}&inout=Input\')','${inputFile.filename}');
           </c:when>
           <c:otherwise>
              d.add(${inputFileSeq},4,'<i>${inputFile.shortFilename}</i>', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/runFile.do?dispatch=downloadFile&fileName=${inputFile.filename}&inout=Input\')','(Masked) ${inputFile.filename}');
           </c:otherwise>                                                   
        </c:choose>
</c:forEach>

  								
<c:forEach var="outputFile" items="${currentRun.outputFileBeans}">
   <c:set var="outputFileSeq" value="${outputFileSeq+1}"/>    											   
	   <c:choose>
	       <c:when test="${outputFile.fileMaskStatus eq 'Active'}">											
              d.add(${outputFileSeq},5,'${outputFile.shortFilename}', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/runFile.do?dispatch=downloadFile&fileName=${outputFile.filename}&inout=Output\')','${outputFile.filename}');
           </c:when>
	       <c:otherwise>
    	      d.add(${outputFileSeq},5, '<i>${outputFile.shortFilename}</i>', 'javascript:gotoPage(\'${pageContext.request.contextPath}\'+\'/runFile.do?dispatch=downloadFile&fileName=${outputFile.filename}&inout=Output\')','(Masked) ${outputFile.filename}');
 		   </c:otherwise>
	   </c:choose>
</c:forEach> 
document.write(d);			
d.openAll();

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
