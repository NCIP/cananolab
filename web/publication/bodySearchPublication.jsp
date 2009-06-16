<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/PublicationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/PublicationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
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
				<jsp:include page="/helpGlossary.jsp">
					<jsp:param name="topic" value="search_reports_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=publication" />
				<table width="100%" align="center" class="submissionView">
					<tr>
						<td class="cellLabel" width="20%">
							Search Site
						</td>
						<td colspan="5">
							<html:select property="searchLocations" styleId="searchLocations"
								onchange="javascript:setPublicationDropdowns()" multiple="true"
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
						<td class="cellLabel" width="15%">
							Publication Type
						</td>
						<td colspan="3">
							<html:select property="category" styleId="publicationCategories"
								onchange="javascript:setSearchReportFields();">
								<option value="" />
									<html:options name="publicationCategories" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Research Category
						</td>
						<td colspan="3">
							<c:forEach var="data" items="${publicationResearchAreas}">
								<html:multibox styleId="researchArea" property="researchArea">
												${data}
											</html:multibox>${data}
										</c:forEach>
						</td>
					</tr>
					<tr id="pubMedRow">
						<td class="cellLabel">
							PubMed ID&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td>
							<html:text property="pubMedId" size="30"
								onkeydown="return filterInteger(event)" />
							<br>
							<em>(exact numeric PubMed ID)</em>
							<br>
						</td>
						<td class="cellLabel">
							Digital Object ID
						</td>
						<td>
							<html:text property="digitalObjectId" size="30" />
						</td>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Publication Title
						</td>
						<td colspan="5">
							<html:text property="title" size="100" />
							<br>
							<em>* for wildcard search</em>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Authors
						</td>
						<td colspan="5">
							<html:textarea property="authorsStr" cols="80"
								styleId="authorsStr" rows="3">
							</html:textarea>
							<br>
							<em>case insensitive, words in quotes are searched together</em>
							<br>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Keywords
						</td>
						<td colspan="5">
							<html:textarea property="keywordsStr" cols="80"
								styleId="keywordsStr" rows="3">
							</html:textarea>
							<br>
							<em>case insensitive, words in quotes are searched together</em>
							<br>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" align="center" class="submissionView">
					<tr>
						<td class="cellLabel">
							Nanoparticle Name
						</td>
						<td colspan="5" valign="top">
							<html:text property="sampleName" size="80" />
							<br>
							<em>* for wildcard search</em>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Composition
							<br>
							Nanomaterial Entity
						</td>
						<td>
							<html:select property="nanomaterialEntityTypes"
								styleId="nanomaterialEntityTypes" multiple="true" size="4">
								<html:options name="nanomaterialEntityTypes" />
							</html:select>
						</td>
						<td class="cellLabel">
							Composition
							<br>
							Functionalizing Entity
						</td>
						<td>
							<html:select styleId="functionalizingEntityTypes"
								property="functionalizingEntityTypes" multiple="true" size="3">
								<html:options name="functionalizingEntityTypes" />
							</html:select>
						</td>
						<td class="cellLabel">
							Function
						</td>
						<td>
							<html:select property="functionTypes" styleId="functionTypes"
								multiple="true" size="3">
								<html:options name="functionTypes" />
							</html:select>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0">
					<tr>
						<td>
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
