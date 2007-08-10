<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=search_protocol_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle">
							<div align="justify">
								Search Criteria
							</div>
						</td>
						<td class="formTitle" colspan="3">
							<div align="justify">
								* for Search Wildcards
							</div>
						</td>
					</tr>

					<tr>
						<td class="leftLabel">
							<strong>Protocol Type</strong>
						</td>
						<td class="rightLabel"">
								<html:select property="protocolType" >
									<option/>
									<html:options name="protocolTypes" />
								</html:select>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Protocol Name </strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:text property="protocolName" size="50" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Protocol File Title</strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:text property="fileTitle" size="50" />
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td>
							<span class="formMessage"> </span>
							<br>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<div align="right">
											<input type="reset" value="Reset" >
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
