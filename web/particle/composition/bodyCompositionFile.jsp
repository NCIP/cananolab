<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				${particleName} Sample Composition - Composition File
			</h3>
		</td>
		<td align="right" width="20%">
			<jsp:include page="/webHelp/helpGlossary.jsp">
				<jsp:param name="topic" value="compo_file_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>	
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=publication" />
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<%@ include file="bodyCompositionFileUpdate.jsp"%>
				</c:when>
				<c:otherwise>
					<%@ include file="bodyCompositionFileReadOnly.jsp"%>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
