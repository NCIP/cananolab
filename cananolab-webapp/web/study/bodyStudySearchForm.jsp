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
	<jsp:param name="pageTitle" value="Search Study" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="searchStudy">
	<jsp:include page="/bodyMessage.jsp?bundle=study" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td width="15%" class="cellLabel">
				Study Name
			</td>
			<td>
				<html:select property="nameOperand" styleId="nameOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td >
				<html:text property="studyName" size="60" />
				<!-- input type="text" size="80" value="Efficacy of nanoparticle"/-->
			</td>
		</tr>

		<tr>
			<td class="cellLabel" width="15%">
				Study Point of Contact
			</td>
			<td >
				<!-- input type="text" size="100"/-->

				<html:select property="pocOperand" styleId="pocOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td>
					<html:text property="studyPointOfContact" size="60" />
					<br />
					<em>searching organization name or person name</em>
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Study Type
			</td>
			<td colspan="2">
				<SELECT >
					<option value=""></option>
					<option value="reproductive">reproductive</option>
					<option value="continuous breeding">continuous breeding</option>
					<option value="developmental">developmental</option>
					<option value="cancer bioassay">cancer bioassay</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Study Design Type
			</td>
			<td colspan="2">
				<SELECT >
					<option value="parallel group">parallel group</option>
					<option value="crossover">crossover</option>
				</SELECT>
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Animal Study?
			</td>
			<td colspan="2">
				Yes <input type="radio" value="yes"/> No<input type="radio" value="no"/>
			</td>
		</tr>

		<tr>
			<td width="15%" class="cellLabel">
				Sample Name
			</td>
			<td>
				<html:select property="nameOperand" styleId="nameOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td >
				<html:text property="sampleName" size="60" />
				<!-- input type="text" size="80" value="Efficacy of nanoparticle"/-->
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Disease Name
			</td>
			<td>
				<html:select property="nameOperand" styleId="nameOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td >
				<html:text property="diseaseName" size="60" />
				<!-- input type="text" size="80" value="Efficacy of nanoparticle"/-->
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Keywords
			</td>
			<td colspan="2">
				<html:textarea property="text" rows="3" cols="57" />
				<br>
				<em>searching study descriptions and outcomes</em>
				<br>
				<em>enter one keyword per line</em>
			</td>
		</tr>
		<tr>
			<td width="15%" class="cellLabel">
				Study Owner
			</td>
			<td>
				<html:select property="nameOperand" styleId="nameOperand">
					<html:options collection="stringOperands" property="value"
						labelProperty="label" />
				</html:select>
			</td>
			<td >
				<html:text property="studyOwner" size="60" />
				<!-- input type="text" size="80" value="Efficacy of nanoparticle"/-->
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
