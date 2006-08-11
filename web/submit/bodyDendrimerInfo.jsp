<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.calab.dto.particle.*"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Dendrimer Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Core </strong>
			</td>
			<td class="label">
				<html:select property="particle.core">
					<option name="Diamine">
						Diamine
					</option>
					<option name="Ethyline">
						Ethyline
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Branch</strong>
			</td>
			<td class="rightLabel">
				<html:text property="particle.branch" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Repeat Unit</strong>
			</td>
			<td class="label">
				<html:text property="particle.repeatUnit" />
			</td>
			<td class="label">
				<strong>Generation</strong>
			</td>
			<td class="rightLabel">
				<html:text property="particle.generation" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Number of Surface Groups</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="particle.numberOfSurfaceGroups" />
			</td>
		</tr>
	</tbody>
</table>
