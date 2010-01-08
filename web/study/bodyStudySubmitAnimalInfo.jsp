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
				caMOD ID *
			</td>
			<td colspan="3">
				<input type="text" size="20" value="26587">
				&nbsp;
				<a href="">Browse caMOD</a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Species
			</td>
			<td>
				<SELECT >
					<option value="">
						Rat (Rattus Rattus)
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
			<td class="cellLabel">
				Strain Name
			</td>
			<td>
				<SELECT >
					<option value="">
						Sprague Dawley
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Age
			</td>
			<td colspan="3">
				<input type="text" size="20" value="26">&nbsp;&nbsp;
				<SELECT >
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
				Description
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="4">
				Animal Diet
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<table width="95%" align="center" class="submissionView">
					<tr>
						<th align="left" colspan="2">
							Animal Diet Information
						</th>
					</tr>
					<tr>
						<td class="cellLabel">
							Is Diet Restricted?
						</td>
						<td class="cellLabel">
							<input type="radio" name="dietRestricted" value="true" checked="checked">Yes
							<input type="radio" name="dietRestricted" value="false">No
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Dietary Restriction
						</td>
						<td>
							<textarea name="achar.description" cols="126" rows="5"></textarea>
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Fee Lot Number
						</td>
						<td>
							<input type="text" size="126">
						</td>
					</tr>
					<tr>
						<td class="cellLabel">
							Fee
						</td>
						<td>
							<input type="text" size="126">
						</td>
					</tr>
				</table><br>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th align="left" colspan="4">
				Individual Animal &nbsp;&nbsp;&nbsp;
				<a href="studyAnimalInfo.do?dispatch=addAnimal" class="addlink">
				<img align="middle" src="images/btn_add.gif" border="0" />
				</a>
			</th>
		</tr>
		<tr>
			<td class="cellLabel">
				caELMIR ID
			</td>
			<td colspan="3">
				<input type="text" size="20" value="26587">
				&nbsp;
				<a href="">Browse caELMIR</a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Gender
			</td>
			<td colspan="3">
				<SELECT >
					<option value="">
						Male
					</option>
					<option value="">
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
				Age
			</td>
			<td colspan="3">
				<input type="text" size="20" value="26">&nbsp;&nbsp;
				<SELECT >
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
						mg
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
		<tr>
			<td class="cellLabel">
				Disposition
			</td>
			<td>
				<SELECT >
					<option value="">
						Accidental Death
					</option>
					<option value="">
						Natural Death
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
			</td>
			<td class="cellLabel">
				Disposition Date
			</td>
			<td>
				<input type="text" value="12/28/2009"/>
				<a href="javascript:cal1.popup();"><img
						src="images/calendar-icon.gif" width="22" height="18" border="0"
						alt="Click Here to Pick up the date"
						title="Click Here to Pick up the date" align="top"></a>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Behavior
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
			</td>
		</tr>
		<tr>
			<td class="cellLabel">
				Reproductive Behavior
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
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
			<th align="left">
				Treatment Group Information &nbsp;&nbsp;&nbsp;
				<a href="studyAnimalInfo.do?dispatch=addDoseGroup" class="addlink">
				<img align="middle" src="images/btn_add.gif" border="0" />
				</a>
			</th>
			<th colspan="3"></th>
		</tr>
		<tr>
			<td class="cellLabel">
				Treatment Group Name
			</td>
			<td class="cellLabel" colspan="3">
				<input type="text" size="56">&nbsp;&nbsp;&nbsp;&nbsp;
				Sample Name&nbsp;
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
				Description
			</td>
			<td colspan="3">
				<textarea name="achar.description" cols="136" rows="5"></textarea>
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
				Number of Treatments
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
				<input type="text" size="20" value="326">
			</td>
			<td class="cellLabel">
				Body Weight Unit
			</td>
			<td>
				<SELECT >
					<option value="">
						g
					</option>
					<option value="">
						mg
					</option>
					<option value="other">
						[other]
					</option>
				</SELECT>
				<br>
			</td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="4">
				Treatment &nbsp;&nbsp;&nbsp;
				<a href="studyAnimalInfo.do?dispatch=addAnimal" class="addlink">
				<img align="middle" src="images/btn_add.gif" border="0" />
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<table width="95%" align="center" class="submissionView">
					<tr>
						<th align="left" colspan="4">
							Treatment Information
						</th>
					</tr>
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
						<td colspan="3">
							<input type="text" size="20" value="26">&nbsp;&nbsp;
							<SELECT >
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
			</td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="4">
				Component &nbsp;&nbsp;&nbsp;
				<a href="studyAnimalInfo.do?dispatch=addAnimal" class="addlink">
				<img align="middle" src="images/btn_add.gif" border="0" />
				</a>
			</td>
		</tr>
		<tr>
			<td colspan="4"><%@ include
					file="bodyDoseGroupComponentInfoView.jsp"%><br></td>
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
