<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/ProtocolManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/ProtocolManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Search Protocols" />
	<jsp:param name="topic" value="search_protocol_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="searchProtocol">
	<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Search Site
			</td>
			<td>
				<html:select styleId="searchLocations" property="searchLocations"
					onchange="javascript:setProtocolNameDropdown()" multiple="true"
					size="4">
					<html:option value="${applicationOwner}">
										${applicationOwner}
									</html:option>
					<c:if test="${! empty allGridNodes}">
						<html:options collection="allGridNodes" property="hostName"
							labelProperty="hostName" />
					</c:if>
				</html:select>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				Protocol Type
			</td>
			<td colspan="2">
				<html:select styleId="protocolType" property="protocolType">
					<option />
						<html:options name="protocolTypes" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol Name
			</td>
			<td>
				<html:select property="nameOperand" styleId="nameOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td>
				<html:text property="protocolName" size="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol Abbreviation
			</td>
			<td>
				<html:select property="abbreviationOperand"
					styleId="abbreviationOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td>
				<html:text property="protocolAbbreviation" size="80" />				
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Protocol File Title
			</td>
			<td>
				<html:select property="titleOperand"
					styleId="titleOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td>
				<html:text property="fileTitle" size="80" />

			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td>
				<span class="formMessage"> <em>Searching without any
						parameters would return all protocols.</em> </span>
				<br>
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="reset" value="Reset" onclick="javascript:location.href='searchProtocol.do?dispatch=setup&page=0';">
								<input type="hidden" name="dispatch" value="search">
								<input type="hidden" name="page" value="1">
								<html:submit value="Search" />
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>
<!--_____ main content ends _____-->
