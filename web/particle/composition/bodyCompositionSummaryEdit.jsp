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
			<a href="#Nanoparticle Entity"><strong>Nanoparticle
					xxx</strong> </a>
		</td>
		<td class="borderlessLabel">
			<a href="#Nanoparticle Entity"><strong>Nanoparticle
					Entity</strong> </a>
		</td>
		<td class="borderlessLabel">
			<a href="#Nanoparticle Entity"><strong>Nanoparticle
					Entity</strong> </a>
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
<a name="Nanoparticle Entity"></a>
<c:url var="entityAddUrl" value="nanoparticleEntity.do">
						<c:param name="page" value="0" />
						<c:param name="dispatch" value="setup" />
						<c:param name="location" value="local" />
						<c:param name="particleId" value="${particleId}" />
						<c:param name="submitType" value="Nanoparticle Entity" />
</c:url>
<table class="smalltable3" cellpadding="0" cellspacing="0" border="0"
	width="90%">
	<tr>
		<th colspan="4" align="left">
			Nanoparticle Entity &nbsp;&nbsp;&nbsp;
			<a href="${entityAddUrl}" class="addlink"><img align="absmiddle"
					src="images/btn_add.gif" border="0" /> </a>
		</th>
	</tr>

	<tr>
		<td colspan="4" align="left">
			<jsp:include page="/bodyMessage.jsp?bundle=particle" />
		</td>
	</tr>

	<c:if test="${!empty compositionForm.map.comp.nanoparticleEntities}">
		<logic:iterate name="compositionForm" property="comp.nanoparticleEntities"
			id="entity" indexId="ind">
			<tr>
				<td>
					<c:if test="${!empty entity.type}">
						<c:set var="entityType"
							value="${entity.type}" scope="page" />
							
							<!--
						
									String entityClass = gov.nih.nci.cananolab.ui.core.InitSetup
									.getInstance().getObjectName(
									(String) pageContext.getAttribute("entityType"),
									application);
							pageContext.setAttribute("entityClass", entityClass);
						
						<jsp:include
							page="/particle/composition/nanoparticleEntity/body${entityClass}Info.jsp" />
							-->
					</c:if>

				</td>
			</tr>
		</logic:iterate>
	</c:if>
</table>



