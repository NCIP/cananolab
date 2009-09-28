<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/CharacterizationManager.js"></script>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle"
		value="${applicationOwner} Basic Sample Search" />
	<jsp:param name="topic" value="search_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
	<jsp:param name="other" value="Advanced Search" />
	<jsp:param name="otherLink" value="advancedSampleSearch.do?dispatch=setup&searchLocations=${param.location}" />
</jsp:include>
<html:form action="searchSample">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Search Site
			</td>
			<td>
				<html:select property="searchLocations" styleId="searchLocations"
					multiple="true" size="4"
					onchange="javascript:setSampleDropdowns();">
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
			<td class="cellLabel" width="20%">
				Keywords
			</td>
			<td>
				<html:textarea property="text" rows="3" cols="60" />
				<br>
				<em>case insensitive, words in quotes are searched together</em>
				<br>
				<em>searching characterization keywords, publication keywords
					and text in characterization descriptions</em>
				<br>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Sample
				<br>
				Point of Contact
			</td>
			<td colspan="5">
				<br>
				<html:text property="samplePointOfContact" size="60" />
				<br>
				<em>case insensitive, * for wildcard search</em>
				<br>
				<em>searching organization name or first name or last name of a
					person </em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Composition
				<br>
				Nanomaterial Entity
			</td>
			<td>
				<html:select styleId="nanomaterialEntityTypes"
					property="nanomaterialEntityTypes" multiple="true" size="4">
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
				<html:select styleId="functionTypes" property="functionTypes"
					multiple="true" size="3">
					<html:options name="functionTypes" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Characterization Type
				<html:hidden styleId="characterizationType"
					property="characterizationType" />
			</td>
			<td>
				<html:select property="characterizationType" styleId="charType"
					onchange="javascript:setCharacterizationOptionsByCharType()">
					<option value="" />
						<html:options name="characterizationTypes" />
				</html:select>
			</td>
			<td class="cellLabel">
				Characterization
			</td>
			<td colspan="3">
				<html:select property="characterizations" styleId="charName"
					multiple="true" size="4">
					<%--<c:forEach var="achar"
										items="${searchSampleForm.map.characterizations}">
										<html:option value="${achar}">${achar}</html:option>
									</c:forEach>--%>
				</html:select>
			</td>
		</tr>
	</table>
	<br>

	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td>
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="reset" value="Reset"
									onclick="javascript:location.href='searchSample.do?dispatch=setup&page=0'">
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