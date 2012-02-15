<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			<label for="charConclusion">Analysis and Conclusion</label>
		</td>
		<td>
			<html:textarea property="achar.conclusion" cols="120" rows="3" styleId="charConclusion"/>
			&nbsp;
		</td>
	</tr>
</table>
<br>