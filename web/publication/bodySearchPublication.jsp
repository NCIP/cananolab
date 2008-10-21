<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/PublicationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/PublicationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/NanoparticleSampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<html:form action="searchPublication">
	<table align="center" width="100%">
		<tr>
			<td>
				<h3>
					Search Publications
				</h3>
			</td>
			<td align="right" width="30%">
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="search_reports_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=publication" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle" colspan="6">
							<div align="justify">
								Search Criteria
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Search Location</strong>
						</td>
						<td class="rightLabel" colspan="5">
							<strong><html:select property="searchLocations"
									styleId="searchLocations"
									onchange="javascript:setReportDropdowns()" multiple="true"
									size="4">
									<html:option value="local">
										Local
									</html:option>
									<c:if test="${! empty allGridNodes}">
										<html:options collection="allGridNodes"
											property="hostName" labelProperty="hostName" />
									</c:if>
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Publication Type</strong>
						</td>
						<td class="label" valign="top">
							<html:select styleId="category" property="category"
								onchange="javascript:setSearchReportFields('category');">
								<option value="" />
									<html:options name="publicationCategories" />
							</html:select>
						</td>					
						<td class="label" valign="top">
							<strong>Research<br>Category</strong>
						</td>
						<td class="label" valign="top">
							<strong><html:select property="researchArea"
									styleId="researchArea" multiple="true" size="3">
									<html:options name="publicationResearchAreas" />
								</html:select> </strong>
						</td>
						<td class="rightLabel" colspan="2" valign="top" align="left">												
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Publication Title</strong>
						</td>
						<td class="rightLabel" colspan="5">
							<html:text property="title" size="100" />
							<br><em>* for searching wildcards</em>
						</td>
					</tr>
					<tr valign="top" id="pubMedRow">									
						<td class="leftLabel" valign="top" colspan="2">
							<strong>PubMed ID</strong>&nbsp;&nbsp;&nbsp;&nbsp;
							<html:text property="pubMedId" size="20" onkeydown="return filterInteger(event)"/>
							<br><i>(Please enter the exact numeric PubMed ID)</i><br><br>
						</td>
						<td class="label" valign="top">
							<strong>Digital Object ID</strong>
						</td>
						<td class="label" valign="top">
							<html:text property="digitalObjectId" size="20" />
						</td>
						<td class="rightLabel" colspan="2" valign="top" align="left">												
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Authors</strong>
						</td>
						<td class="rightLabel" colspan="5" valign="top">
							<strong><html:textarea property="authorsStr"
									cols="30" styleId="authorsStr" rows="3">
							</html:textarea> </strong>
							<br>
							<em>case insensitive</em>
							<br>
							<em>words in quotes are searched together</em>
							<br>
						</td>
					</tr>
					
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Keywords</strong>
						</td>
						<td class="rightLabel" colspan="5" valign="top">
							<strong><html:textarea property="keywordsStr"
									cols="30" styleId="keywordsStr" rows="3">
							</html:textarea> </strong>
							<br>
							<em>case insensitive</em>
							<br>
							<em>words in quotes are searched together</em>
							<br>
						</td>
					</tr>
				</table>
				<br>
				
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle" colspan="2">
							<div align="justify">
								Advance Search
							</div>
						</td>
						<td class="formTitle" colspan="4">&nbsp;
						</td>
					</tr>
					<tr>
						<td class="leftLabel"  valign="top">
							<strong>Nanoparticle Name</strong>
						</td>
						<td class="rightLabel" colspan="5" valign="top">
							<html:text property="nanoparticleName" size="20" />
							<em>* for searching wildcards</em>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Composition<br> Nanoparticle Entity</strong>
						</td>
						<td class="label" valign="top">
							<strong><html:select property="nanoparticleEntityTypes"
									styleId="nanoparticleEntityTypes" multiple="true" size="4">
									<html:options name="nanoparticleEntityTypes" />
								</html:select> </strong>
						</td>
						<td class="label" valign="top">
							<strong>Composition <br>Functionalizing Entity</strong>
						</td>
						<td class="label" valign="top">
							<strong><html:select
									styleId="functionalizingEntityTypes"
									property="functionalizingEntityTypes" multiple="true" size="3">
									<html:options name="functionalizingEntityTypes" />
								</html:select> </strong>
						</td>
						<td class="label" valign="top">
							<strong>Function</strong>
						</td>
						<td class="rightLabel" valign="top">
							<strong><html:select property="functionTypes"
									styleId="functionTypes" multiple="true" size="3">
									<html:options name="functionTypes" />
								</html:select> </strong>
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
											<input type="button" value="Reset"
												onclick="javascript:location.href='searchPublication.do?dispatch=setup&page=0'">
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
