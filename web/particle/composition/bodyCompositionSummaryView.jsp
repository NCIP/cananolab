<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!--<table summary="" cellpadding="0" cellspacing="0" border="0"
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
	</tr>
</table>

--> 
<div class="animatedtabs">
	<ul>
		<li class="selected">
			<a href="javascript:showComposition('ALL')" title="Summary"><span>Summary</span>
			</a>
		</li>
		<li>
			<a href="javascript:showComposition('1')" title="Nanoparticle Entity">
			<span>Nanoparticle Entity</span>
			</a>
		</li>
		<li>
			<a href="javascript:showComposition('2')" title="Functionalizing Entity">
			<span>Functionalizing Entity</span>
			</a>
		</li>
		<li>
			<a href="javascript:showComposition('3')" title="Chemical Association"><span>
				Chemical Association</span>
			</a>
		</li>
		<li>
			<a href="javascript:showComposition('4')" title="Composition File"><span>Composition File</span>
			</a>
		</li>	
	</ul>
</div>

<br><br>
<jsp:include page="nanoparticleEntity/bodyNanoparticleEntityView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<jsp:include
	page="functionalizingEntity/bodyFunctionalizingEntityView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<jsp:include page="bodyChemicalAssociationView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
<jsp:include page="bodyCompositionFileView.jsp">
	<jsp:param name="particleId" value="${param.particleId}" />
</jsp:include>
