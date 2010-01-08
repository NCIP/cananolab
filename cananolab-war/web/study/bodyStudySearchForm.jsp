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
<html:form action="searchStudy">
	<jsp:include page="/bodyMessage.jsp?bundle=study" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" width="20%">
				Search Site
			</td>
			<td>
				<SELECT multiple="true" size="4">
					<option value="${applicationOwner}">${applicationOwner}</option>
				</SELECT>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td width="16%" class="cellLabel">
				Study Name
			</td>
			<td colspan="3">
				<input type="text" size="100" value="Efficacy of nanoparticle"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
		<tr>
			<td width="16%" class="cellLabel">
				Study Type
			</td>
			<td colspan="3">
				<SELECT >
					<option value="Reproductive">Reproductive</option>
					<option value="Continuous Breeding">Continuous Breeding</option>
					<option value="Developmental">Developmental</option>
					<option value="Cancer Bioassay">Cancer Bioassay</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Sample Name
			</td>
			<td colspan="3">
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel" width="20%">
				Point of Contact
			</td>
			<td colspan="5">
				<br>
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
				<br>
				<em>searching organization name or first name or last name of a
					person </em>
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
				Disease Name
			</td>
			<td>
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Observations / Conclusions
			</td>
			<td>
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Keyword / Text
			</td>
			<td>
				<input type="text" size="100"/>
				<br>
				<em>case insensitive, * for wildcard search</em>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0" class="topBorderOnly" summary="">
		<tr>
			<td width="30%">
				<span class="formMessage"> <em>Searching without any
						parameters would return all studies.</em> </span>
				<br>
				<table width="498" height="15" border="0" align="right"
					cellpadding="4" cellspacing="0">
					<tr>
						<td width="490" height="15">
							<div align="right">
								<div align="right">
									<input type="reset" value="Clear"
										onclick="javascript:location.href='searchStudy.do?dispatch=setupNew&page=0'">
									<input type="hidden" name="dispatch" value="search">
									<input type="hidden" name="page" value="2">
									<html:submit value="Search"/>
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
