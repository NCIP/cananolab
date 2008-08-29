<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/ProtocolManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/ProtocolManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<html:form action="searchProtocol">
	<table align="center" width="100%">
		<tr>
			<td>
				<h3>
					<br>
					Search Protocols
				</h3>
			</td>
			<td align="right" width="25%">
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="search_protocol_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=protocol" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle">
							<div align="justify">
								Search Criteria
							</div>
						</td>
						<td class="formTitle">
							<div align="justify">
								* for searching wildcards
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Search Location</strong>
						</td>
						<td class="rightLabel">
							<strong><html:select styleId="searchLocations"
									property="searchLocations"
									onchange="javascript:setProtocolNameDropdown()" multiple="true"
									size="4">
									<html:option value="local">
										Local
									</html:option>
									<c:if test="${! empty allGridNodes}">
										<html:options collection="allGridNodes"
											property="value.hostName" labelProperty="value.hostName" />
									</c:if>
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Protocol Type</strong>
						</td>
						<td class="rightLabel"">
							<html:select styleId="protocolType" property="protocolType">
								<option />
									<html:options name="protocolTypes" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Protocol Name </strong>
						</td>
						<td class="rightLabel">
							<html:text property="protocolName" size="50" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Protocol File Title</strong>
						</td>
						<td class="rightLabel">
							<html:text property="fileTitle" size="50" />
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td>
							<span class="formMessage"> </span>
							<br>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<div align="right">
											<input type="reset" value="Reset"
												onclick="javascript:location.href='searchProtocol.do?dispatch=setup&page=0'">
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
			</td>
		</tr>
	</table>
</html:form>
<!--_____ main content ends _____-->
