<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Copy" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${title} Sample" />
	<jsp:param name="topic" value="copy_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/sample"
	onsubmit="return validateSavingTheData('newPointOfContact', 'point of contact');" styleId="cloneSampleForm">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView" summary="layout">
		<tr>
			<td>
				<table>
					<tr>
						<td class="cellLabel" width="150">
							<label for="cloningSampleName">Existing <br>Sample *</label>
						</td>
						<td>
							<html:text property="sampleBean.cloningSampleName" size="50" styleId="cloningSampleName"/>
						</td>
						<td colspan="2">
							<a href="#" onclick="showMatchedSampleNameDropdown()"><img src="images/icon_browse.jpg" align="middle"
								alt="search existing samples" border="0"/></a>
						</td>						
					</tr>
					<tr>
						<td class="cellLabel" valign="top">
							<label for="newSampleName">New Sample <br>Name *</label>
						</td>
						<td colspan="2" valign="top">
							<html:text property="sampleBean.domain.name" size="50" styleId="newSampleName"/>
						</td>
					</tr>
				</table>
			</td>
			<td width="40%">
				<table class="invisibleTable">
					<tr>
						<td valign="top"><img src="images/ajax-loader.gif" border="0" class="counts"
								id="loaderImg" style="display: none" alt="loading existing sample">
							<label for="matchedSampleSelect">&nbsp;</label>							
							<html:select property="sampleBean.cloningSampleName"
								styleId="matchedSampleSelect" size="5" style="display:none" onclick="updateCloningSample()">							
							</html:select>							
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br>	
	<c:set var="resetOnclick" value="this.form.reset();hide('loaderImg'); hide('matchedSampleSelect');"/>	
	<c:set var="submitOnclick" value="submitAction('cloneSampleForm', 'sample', 'clone', 1);show('waitMessage')"/>	
	<%@include file="../bodySubmitButtons.jsp"%>
	<br />
	<span id="waitMessage" style="display: none" class="welcomeContent"><img
			src="images/ajax-loader.gif" border="0" class="counts"> Please
		wait. Copying might take a while to finish if the original sample is fully curated.</span>
</html:form>

