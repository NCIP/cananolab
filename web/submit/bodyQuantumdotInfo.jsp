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
					Quantum Dot Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Core </strong>
			</td>
			<td class="label">
				<html:text property="particle.core" />
			</td>
			<td class="label">
				<strong>Shell</strong>
			</td>
			<td class="rightLabel">
				<html:text property="particle.shell" />
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Treatment</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<html:text property="particle.treatment" />
			</td>
		</tr>
	</tbody>
</table>
