<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center" class="submissionView">
	<tr>
		<th colspan="2">
			Cytotoxicity Properties
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Cell Line
		</td>
		<td>
			<textarea name="achar.cytotoxicity.cellLine" rows="2" cols="80">${characterizationForm.map.achar.cytotoxicity.cellLine}
			</textarea>
		</td>
	</tr>
</table>
<br>


