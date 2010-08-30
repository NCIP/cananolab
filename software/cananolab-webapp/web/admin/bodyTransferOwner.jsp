<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Transfer Ownership" />
	<jsp:param name="topic" value="site_preference_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="searchTransferOwner" >
	<table width="100%" align="left">
		<tr>
			<td colspan="4">
				<jsp:include page="/bodyMessage.jsp" />
				<table width="100%" align="left" class="submissionView">
					<tr>
						<td class="cellLabel" width="20%">
							Current Owner *
						</td>
						<td >
							<html:text styleId="currentOwner" property="currentOwner" size="30" />
						</td>
						
					</tr>
					<tr>
						<td class="cellLabel" width="20%">
							New Owner *
						</td>
						<td >
							<html:text styleId="newOwner" property="newOwner" size="30" />
						</td>
						
					</tr>
					<tr>
						<td class="cellLabel" width="20%">
							Data Type *
						</td>
						<td colspan="2">
							<html:select styleId="dataType" property="dataType"  multiple="true" size="4">
								<html:option value="Sample"  />
								<html:option value="Publication" />
								<html:option value="Protocol" />
								<html:option value="Collaboration Group" />
							</html:select>							
						</td>						
					</tr>
				</table>
				<br>
			
			
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset" onclick="this.form.reset()">
								
								<input type="submit" value="Submit"  onclick="javascript:location.href='searchTransferOwner.do?dispatch=search'"/>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
