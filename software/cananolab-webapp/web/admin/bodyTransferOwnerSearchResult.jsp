<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="gov.nih.nci.cananolab.util.Constants"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/AccessibilityManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/AccessibilityManager.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Transfer Ownership Search Result" />
	<jsp:param name="topic" value="site_preference_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="transferOwner" >
	<table width="80%" align="left">
		<tr>
			<td colspan="4">
				<jsp:include page="/bodyMessage.jsp?bundle=admin" />
				<table width="80%" align="left" class="submissionView" border="1">
					<tr><td>Current Owner: <td>${transferOwnerBean.currentOwner}</td></tr>
					<tr><td>New Owner: <td>${transferOwnerBean.newOwner}</td></tr>
					<html:hidden styleId="transferOwnerBean.currentOwner" property="transferOwnerBean.currentOwner"
						value="${transferOwnerBean.currentOwner}" />
						<html:hidden styleId="transferOwnerBean.newOwner" property="transferOwnerBean.newOwner"
						value="${transferOwnerBean.newOwner}" />
					<br/>
					<c:if test="${!empty transferOwnerBean.samples}">
					
					<tr>
						<th class="cellLabel">
							Sample Name
						</th><th></th>				
					</tr>
					<c:forEach var="sample"	items="${transferOwnerBean.samples}">
						<tr><td>${sample.value}</td>
						<td colspan="3" align="left"><input type="checkbox" id="${sample.key}" checked="checked"/>
						</td>
						</tr>
					</c:forEach>		
					</c:if>				
					
				</table>
			</td>
		</tr>	
		<tr>
			<td>
				<div align="right">
					<input type="button" value="Reset" onclick="this.form.reset()">
					
					<input type="submit" value="Submit"  onclick="javascript:location.href='transferOwner.do?"/>
				</div>
			</td>
		</tr>	
		<tr>
			<td colspan="4" align="right">
				
				
				<c:set var="resetOnclick"
					value="javascript:location.href='transferOwner.do?dispatch=setup'" />
				<c:set var="submitOnclick"
					value="confirmTransferOwner(); javascript:location.href='transferOwner.do?" />				
				<%@include file="../bodySubmitButtons.jsp"%>
			</td>
		</tr>
	</table>
</html:form>
