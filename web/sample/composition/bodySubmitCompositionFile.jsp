<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<c:set var="fileParent" value="comp" />
<c:set var="theFile" value="${compositionForm.map.comp.theFile}" />
<html:form action="/compositionFile" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h4>
					${sampleName} Sample Composition - Composition File
				</h4>
			</td>
			<td align="right" width="15%">
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="function_entity_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
			</td>
		</tr>
	</table>
	<c:set var="fileForm" value="compositionForm" />
	<c:set var="theFile" value="${compositionForm.map.comp.theFile}" />
	<c:set var="actionName" value="compositionFile" />
	<%@include file="../bodySubmitFile.jsp"%>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td width="30%">
				<c:choose>
					<c:when
						test="${!empty compositionForm.map.comp.theFile.domainFile.id && canDelete eq 'true'}">
						<table height="32" border="0" align="left" cellpadding="4"
							cellspacing="0">
							<tr>
								<td height="32">
									<div align="left">
										<input type="button" value="Delete"
											onclick="deleteCompositionFile()">
									</div>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
				<table width="498" height="32" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="32">
							<div align="right">
								<div align="right">
									<input type="reset" value="Reset"
										onclick="javascript:window.location.href='${origUrl}'">
									<input type="hidden" name="dispatch" value="create">
									<input type="hidden" name="page" value="2">
									<html:hidden property="sampleId" value="${sampleId}" />
									<html:submit/>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>