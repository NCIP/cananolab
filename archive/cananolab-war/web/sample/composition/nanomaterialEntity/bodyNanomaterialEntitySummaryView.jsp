<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table id="summarySection${index}" width="100%" align="center"
	style="display: block" class="summaryViewLayer2">
	<tr>
		<th align="left">
			nanomaterial entity
		</th>
	</tr>
	<logic:iterate name="compositionForm"
		property="comp.nanomaterialEntities" id="nanomaterialEntity"
		indexId="ind">
		<%@include file="bodySingleNanomaterialEntitySummaryView.jsp"%>
	</logic:iterate>
</table>
<div id="summarySeparator${index}">
	<br>
</div>