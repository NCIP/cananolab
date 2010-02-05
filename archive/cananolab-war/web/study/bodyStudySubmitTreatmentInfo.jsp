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
	<jsp:param name="pageTitle" value="WUSTL Study Efficacy of nanoparticle Animal Model" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<html:form action="/studyAnimalModel">
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel">
				Administrative Route
			</td>
			<td>
				<SELECT >
					<option value="">
						Aspiration
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
			<td class="cellLabel">
				Surgery Type
			</td>
			<td>
				<input type="text" size="20" value="26">
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Age at Treatment
			</td>
			<td>
				<input type="text" size="20" value="26">
			</td>
			<td class="cellLabel">
				Age Unit
			</td>
			<td>
				<SELECT >
					<option value="">
						Weeks
					</option>
					<option value="">
						Days
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Regimen
			</td>
			<td colspan="3">
				<input type="text" size="20" value="26">
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th align="left" colspan="2">
				Dose Group Information &nbsp;&nbsp;&nbsp;
				<a href="studyAnimalInfo.do?dispatch=addDoseGroup" class="addlink">
				<img align="middle" src="images/btn_add.gif" border="0" />
				</a>
			</th>
		</tr>
		<tr>
			<td class="cellLabel">
				Dose Group Name
			</td>
			<td colspan="3">
				<input type="text" size="126">
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Description
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Sample Name
			</td>
			<td colspan="3">
				<SELECT >
					<option value="">
						NCL 49-2
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Number of Animals
			</td>
			<td>
				<input type="text" size="26">
			</td>
			<td class="cellLabel">
				Gender
			</td>
			<td>
				<SELECT >
					<option value="">
						Male
					</option>
					<option value="other">
						Female
					</option>
					<option value="other">
						Both
					</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Number of Doses
			</td>
			<td>
				<input type="text" size="26">
			</td>
			<td class="cellLabel">
				cdELMIR ID
			</td>
			<td>
				<SELECT >
					<option value="">
						2345
					</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Body Weight
			</td>
			<td>
				<input type="text" size="20" value="26">
			</td>
			<td class="cellLabel">
				Body Weight Unit
			</td>
			<td>
				<SELECT >
					<option value="">
						Mg
					</option>
					<option value="">
						g
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
		</tr>
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
										onclick="javascript:location.href='studyAnimalInfo.do?dispatch=setupNew&page=0'">
									<input type="hidden" name="dispatch" value="create">
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
