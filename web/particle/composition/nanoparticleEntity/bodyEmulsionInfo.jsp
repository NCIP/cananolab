<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="entity" value="${compositionForm.map.comp.nanoparticleEntities[param.entityIndex]}" />

<table cellpadding="0" cellspacing="0"
							border="0">
<tr><td valign="top">Description:</td><td valign="top">${entity.emulsion.description}</td></tr>
<tr><td>Is Polymerized:</td><td>${entity.emulsion.polymerized}</td></tr>
<tr><td>Polymer Name:</td><td>${entity.emulsion.polymerName}</td></tr>

<tr><td>Composing Element:</td><td>




</td></tr>
</table>