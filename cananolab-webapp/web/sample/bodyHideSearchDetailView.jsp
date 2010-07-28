<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<c:if test="${! empty param.styleId}">
	<table width="100%" class="gridtableNoBorder">
		<tr>
			<td style="text-align:right;">
				<a href="#"
					onclick="javascript:hideDetailView('${param.styleId}');return false;">hide</a>
			</td>
		</tr>
	</table>
</c:if>