<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.calab.dto.particle.*"%>

<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Core Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Chemical Name</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:text property="quantumDot.core.chemicalName" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.quantumDot.core.chemicalName}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Description</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:textarea property="quantumDot.core.description" rows="3" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.quantumDot.core.description}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
</table>
