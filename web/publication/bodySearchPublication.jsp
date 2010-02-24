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
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Search Publications" />
	<jsp:param name="topic" value="search_publications_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="searchPublication">
	<jsp:include page="/bodyMessage.jsp?bundle=publication" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Search Site
			</td>
			<td>
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
			<td colspan="2">
				<html:select property="category" styleId="publicationCategory"
					onchange="javascript:updateSearchFormBasedOnCategory();">
					<option value="" />
						<html:options name="publicationCategories" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Research Category
			</td>
			<td colspan="2">
				<c:forEach var="data" items="${publicationResearchAreas}">
					<html:multibox styleId="researchArea" property="researchArea">
												${data}
											</html:multibox>${data}
										</c:forEach>
			</td>
		</tr>
		<tr id="pubMedRow">
			<td class="cellLabel">
				PubMed ID
			</td>
			<td colspan="2">
				<html:text styleId="pubMedId" property="pubMedId" size="30" /> <em>exact match</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Digital Object ID
			</td>
			<td colspan="2">
				<html:text property="digitalObjectId" size="30" /> <em>exact match</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Publication Title
			</td>
			<td>
				<html:select property="titleOperand" styleId="titleOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td>
				<html:text property="title" size="80" />
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Authors
			</td>
			<td colspan="2">
				<html:textarea property="authorsStr" cols="77" rows="3" styleId="authorsStr"/> 
				<br/><em>enter one author per line</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Keywords
			</td>			
			<td colspan="2">
				<html:textarea property="keywordsStr" cols="77" rows="3" styleId="keywordsStr" />
				<br/><em>enter one keyword per line</em>
			</td>
		</tr>		
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
	    <tr>
			<td class="cellLabel">
				Sample Name
			</td>			
			<td valign="top" colspan="4">
				<html:text property="sampleName" size="80" /> <em>exact match</em>
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
				<span class="formMessage"> <em>Searching without any
						parameters would return all publications.</em> </span>
				<br>
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset" onclick="javascript:location.href='searchPublication.do?dispatch=setup&page=0';">
								<input type="button" value="Search" onclick="searchPublication();">
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
