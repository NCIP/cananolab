<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>
<script language="JavaScript">
	<!-- //
		var cal1 = new calendar2(document.getElementById('charDate'));
	    cal1.year_scroll = true;
		cal1.time_comp = false;
		cal1.context = '${pageContext.request.contextPath}';
  	//-->
</script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="${pageTitle}" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/study">
	<jsp:include page="/bodyMessage.jsp?bundle=study" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td width="16%" class="cellLabel">
				Study Name*
			</td>
			<td colspan="3">
				<input type="text" size="100" value="Efficacy of nanoparticle"/>
			</td>
		</tr>
		<tr>
			<td width="16%" class="cellLabel">
				Study Type
			</td>
			<td colspan="3" class="cellLabel">
				<SELECT >
					<option value="Reproductive">Reproductive</option>
					<option value="Continuous Breeding">Continuous Breeding</option>
					<option value="Developmental">Developmental</option>
					<option value="Cancer Bioassay">Cancer Bioassay</option>
				</SELECT>
				&nbsp;&nbsp;&nbsp;&nbsp;
				Is Animal Study?&nbsp;
				<input type="checkbox" checked="checked">
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<textarea rows="3" cols="97"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Point of Contact
			</td>
			<td>
				<a href="#"
					onclick="javascript:openSubmissionForm('PointOfContact');"
					id="addPointOfContact" style="display:block"><img align="top"
						src="images/btn_add.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<div style="display:none" id="newPointOfContact">
					<a name="submitPointOfContact"><jsp:include
							page="bodyStudySubmitPointOfContact.jsp"/></a>
				</div>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Disease Type
			</td>
			<td>
				<SELECT >
					<option value="">
						Cancer
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="study.do?dispatch=addDisease" class="addlink"><img
					align="middle" src="images/btn_add.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Disease Name
			</td>
			<td>
				<textarea rows="2" cols="50"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Start Date
			</td>
			<td>
				<input type="text" value="10/01/2009"/>
				<a href="javascript:cal1.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"></a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<b>End Date</b>&nbsp;&nbsp;
				<input type="text" value="12/01/2009"/>
				<a href="javascript:cal1.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"> </a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Observations/Conclusions
			</td>
			<td>
				<textarea rows="3" cols="97">Use of nanoparticle as delivery device for drug x demonstrated decrease in tumor size greater than use of drug x alone.
				</textarea>
			</td>
		</tr>
		<!-- 
		<c:set var="groupAccesses" value="${sampleForm.map.sampleBean.groupAccesses}"/>
		<c:set var="userAccesses" value="${sampleForm.map.sampleBean.userAccesses}"/>
		<c:set var="accessParent" value="studyBean"/>
		<c:set var="dataType" value="Study"/>
		<c:set var="parentAction" value="study"/>
		<c:set var="parentForm" value="studyForm"/>
		<c:set var="parentPage" value="4"/>
		<c:set var="protectedData" value="${sampleForm.map.sampleBean.domain.id}${sampleForm.map.sampleBean.domain.id}"/>
		<c:set var="newData" value="true"/>
		<c:set var="isPublic" value="true"/>
		<c:set var="isOwner" value="true"/>
		<c:set var="ownerName" value="lethai"/>
		
		 %@include file="../bodyManageAccessibility.jsp" %> 
		-->
			
		
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<table width="498" height="15" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="15">
							<div align="right">
								<div align="right">
									<input type="reset" value="Reset"
										onclick="javascript:location.href='study.do?dispatch=setupNew&page=0'">
									<input type="hidden" name="dispatch" value="studyEdit">
									<input type="hidden" name="page" value="2">
									<html:submit />
								</div>
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>
