<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Sample" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />

<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
		<input type="radio" name="type" value="false" onclick="javascript:hide('selectStudySample');show('newStudySample');show('submitButtons');">
		Create new Sample
		<br>
		&nbsp;&nbsp;or
		<br>
		<input type="radio" name="type" value="true" onclick="javascript:hide('newStudySample');show('selectStudySample');show('submitButtons');">
		Select an existing Sample
		</td>
	</tr>
	<tr>
		<td>
			<div style="display:none" id="newStudySample">
				<jsp:include page="bodyStudySubmitSample.jsp"/>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<div style="display:none" id="selectStudySample">
			<table width="100%" align="left" class="summaryViewLayer2">
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						<select name="otherSamples" multiple="multiple" size="5"
							id="allSampleNameSelect" style="display: block;">
							<option value="BROWN_STANFORD-HLeeJNM2008-01">
								BROWN_STANFORD-HLeeJNM2008-01
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-02">
								BROWN_STANFORD-HLeeJNM2008-02
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-03">
								BROWN_STANFORD-HLeeJNM2008-03
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-04">
								BROWN_STANFORD-HLeeJNM2008-04
							</option>
							<option value="BROWN_STANFORD-HLeeJNM2008-05">
								BROWN_STANFORD-HLeeJNM2008-05
							</option>
						</select>
					</td>
				</tr>
			</table>
			</div>
		</td>
	</tr>
</table>
<br>
<div style="display:none" id="submitButtons">
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0">
	<tr>
		<td width="30%">
			<table width="498" height="32" border="0" align="right"
				cellpadding="4" cellspacing="0">
				<tr>
					<td width="490" height="32">
						<div align="right">
							<div align="right">
								<c:set var="origUrl"
									value="studySample.do?page=0&sampleId=${sampleId}&dispatch=${param.dispatch}&location=${applicationOwner}" />
								<input type="reset" value="Reset"
									onclick="javascript:window.location.href='${origUrl}'">
								<input type="hidden" name="dispatch" value="create">
								<input type="hidden" name="page" value="1">
								<html:submit/>
							</div>
						</div>
					</td>
				</tr>
			</table>
			<div align="right"></div>
		</td>
	</tr>
</table>
</div>
