<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<table width="99%" align="center" class="summaryViewNoGrid"
	bgcolor="#F5F5f5">
	<c:if test="${edit eq 'true'}">
	<tr>
		<td></td>
		<td width="95%"></td>
		<td align="right">
			<a href="study.do?dispatch=setupUpdate">Edit</a>
		</td>
	</tr>
	</c:if>
	<tr>
		<td class="cellLabel" width="20%">
			Study Name
		</td>
		<td colspan="3">
			MIT_MGH-KKellyIB2009-01
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Title
		</td>
		<td colspan="3">
			study title
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Type
		</td>
		<td colspan="3">
			reproductive
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Design Types
		</td>
		<td colspan="3">
			crossover
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Diseases
		</td>
		<td colspan="3">
			Disease 1, Disease 2
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Factors
		</td>
		<td colspan="3">
			Factor 1, Factor2
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Description
		</td>
		<td colspan="3">
			Description information about the study
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Outcome
		</td>
		<td colspan="3">
			Study Outcome informaiton
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Date Range
		</td>
		<td colspan="3">
			12/15/2010 - 12/15/2010
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Point of Contact
		</td>
		<td colspan="3">
			<table class="invisibleTable" align="center" width="100%">
				<tbody>
					<tr>
						<td class="cellLabel">
							Primary Contact?
						</td>
						<td class="cellLabel">
							Contact Person
						</td>
						<td class="cellLabel">
							Organization
						</td>
						<td class="cellLabel">
							Role
						</td>
						<td></td>
					</tr>
					<tr>
						<td>
							Yes
						</td>
						<td>
						</td>
						<td>
							MIT_MGH
						</td>
						<td>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
