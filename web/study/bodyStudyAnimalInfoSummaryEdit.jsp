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
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Animal Model Summary Edit" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />

<table width="100%" align="center" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Animal Model &nbsp;&nbsp;&nbsp;
			<a href="studyAnimalModel.do?dispatch=setupNew"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<td>
			<table width="100%" align="center" class="summaryViewLayer3">
				<tr>
					<th valign="top" align="left" width="20%">
						Animal Model Information
					</th>
					<th valign="top" align="right" width="80%">
						<a href="studyAnimalModel.do?dispatch=setupUpdate&page=0&sampleId=11337747">Edit</a>
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						caMOD ID
					</td>
					<td>
						26578
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Species
					</td>
					<td>
						Rat (Rattus Rattus)
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Strain Name
					</td>
					<td>
						Sprague Dawley
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Age
					</td>
					<td>
						18 Days
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Description
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
			<br>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" align="center" class="summaryViewLayer3">
				<tr>
					<th valign="top" align="left" width="20%">
						Animal Model Information
					</th>
					<th valign="top" align="right" width="80%">
						<a href="studyAnimalModel.do?dispatch=setupUpdate&page=0&sampleId=11337747">Edit</a>
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						caMOD ID
					</td>
					<td>
						16237
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Species
					</td>
					<td>
						Rabit
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Strain Name
					</td>
					<td>
						Sprague Dawley
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Age
					</td>
					<td>
						38 Days
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Description
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
			<br>
		</td>
	</tr>
</table>
