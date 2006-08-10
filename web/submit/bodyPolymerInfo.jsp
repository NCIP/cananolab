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
					Polymer Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Is Crosslinked </strong>
			</td>
			<td class="label">
				<html:select property="particle.crosslinked">
					<option value="yes">
						Yes
					</option>
					<option value="no">
						No
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Crosslink Degree</strong>
			</td>
			<td class="rightLabel">
				<html:text property="particle.crosslinkDegree" size="3" />
				<strong>%</strong>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Initiator </strong>
			</td>
			<td class="label">
				<html:select property="particle.initiator">
					<option value="free radicals">
						Free Radicals
					</option>
					<option value="peroxide">
						Peroxide
					</option>
				</html:select>
			</td>
			<td class="label">
				<strong>Number of Monomers</strong>
			</td>
			<td class="rightLabel">
				<html:text property="particle.numberOfMonomers"/>				
			</td>
		</tr>
	</tbody>
</table>

