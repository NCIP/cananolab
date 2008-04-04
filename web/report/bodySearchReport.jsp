<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="searchReport">
	<table align="center" width="100%">
		<tr>
			<td>
				<h3>
					<br>
					Search Reports 
				</h3>
			</td>
			<td align="right" width="30%">
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=search_reports_help')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=report" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle">
							<div align="justify">
								Search Criteria
							</div>
						</td>
						<td class="formTitle" colspan="4">
							<div align="justify">
								* for Search Wildcards
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Report Title</strong>
						</td>
						<td class="rightLabel" colspan="4">
							<html:text property="reportTitle" size="50" />
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Report Type</strong>
						</td>
						<td class="label"">
							<html:select property="reportType">
								<option value=""/>
								<html:options name="allReportTypes" />
							</html:select>
						</td>
						<td class="label">&nbsp;</td>
						<td class="label">&nbsp;</td>
						<td class="rightLabel">&nbsp;</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Particle Composition</strong>
						</td>
						<td class="label" valign="top">
							Nanoparticle Entity
						</td>
						<td class="label">
							<strong><html:select property="particleType"
							multiple="true" size="4">
										<html:options name="allSampleTypes" />
								</html:select> </strong>
						</td>
						<td class="label" valign="top">
							Functionalizing Entity
						</td>
						<td class="rightLabel">
							<strong><html:select property="functionTypes"
									multiple="true" size="3">
									<option value=antibody>Antibody</option>
									<option value=biomolecule>Biomolecule</option>
									<option value=smallMolecule>Small Molecule</option>
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Particle Function Type</strong>
						</td>
						<td class="rightLabel" colspan="4">
							<strong> <html:select property="functionTypes" multiple="true" size="4">
									<html:options name="allFunctionTypes"/>
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Search Location</strong>
						</td>
						<td class="rightLabel" colspan="4">
								<strong><html:select property="gridNodes"
										multiple="true" size="3">
									<option value="local" selected>Local</option>
									<option value="caNanoLab-NCL_PROD">caNanoLab-NCL_PROD</option>
									<option value="caNanoLab-PROD">caNanoLab-PROD</option>
									<option value="caNanoLab-WUSTL">caNanoLab-WUSTL</option>
									<c:forEach var="selectedNode" items="${selectedGridNodeHosts}">
										<option value="${selectedNode}" selected>${selectedNode}</option>
									</c:forEach>
									<c:forEach var="unselectedNode" items="${unselectedGridNodeHosts}">
										<option value="${unselectedNode}">${unselectedNode}</option>
									</c:forEach>
								</html:select></strong>
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
											<input type="button" value="Reset" onClick="javascript:location.href='searchReport.do?dispatch=setup&page=0'">
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
