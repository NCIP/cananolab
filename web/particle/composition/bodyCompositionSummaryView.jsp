<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table summary="" cellpadding="0" cellspacing="0" border="0"
	width="100%">
	<tr>
		<td class="borderlessLabel">
			<a href="#Nanoparticle Entity"><strong>Nanoparticle
					Entity</strong> </a>
		</td>
		<td class="borderlessLabel">
			<a href="#Functionalizing Entity"><strong>Functionalizing Entity</strong>
			</a>
		</td>
		<td class="borderlessLabel">
			<a href="#Chemical Association"><strong>Chemical Association
				</strong> </a>
		</td>
		<td class="borderlessLabel">
			<a href="#Composition File"><strong>Composition File
					</strong> </a>
		</td>

		<td class="borderlessLabel" align="right" width="20%">
			<jsp:include page="/helpGlossary.jsp">
				<jsp:param name="topic" value="nano_entity_help" />
				<jsp:param name="glossaryTopic" value="glossary_help" />
			</jsp:include>
		</td>
	</tr>
</table>
<br>
<jsp:include page="nanoparticleEntity/bodyNanoparticleEntityView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<br>
<jsp:include page="functionalizingEntity/bodyFunctionalizingEntityView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<br>
<jsp:include page="bodyChemicalAssociationView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<br>
<jsp:include page="bodyCompositionFileView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<br>



