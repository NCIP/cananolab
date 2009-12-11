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
	<jsp:param name="pageTitle" value="Submit Study Sample" />
	<jsp:param name="topic" value="submit_study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />
<table width="100%" align="center" class="submissionView">
	<tr>
		<th align="left">
			Sample &nbsp;&nbsp;&nbsp;
			<a href="nanomaterialEntity.do?dispatch=setupNew&sampleId=3735562"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<td>
			<a name="6160387">
				<table class="summaryViewLayer3" width="95%" align="center">
					<tr>
						<td class="cellLabel" width="10%">
							MIT_MGH-AWangCMC2008-01
						</td>
						<td colspan="2">
							<table class="summaryViewLayer4" width="95%" align="center">
								<tr>
									<td class="cellLabel">
										Point of Contact
									</td>
								</tr>
								<tr>
									<td>
										Primary Contact?: Yes
									</td>
									<td></td>
								</tr>
								<tr>
									<td style="word-wrap: break-word; max-width: 280px;">
										Contact Person:
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
									 	 Organization: WUSTL
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
										Role:
									</td>
									<td></td>
								</tr>
							</table>
							<br>
							<table class="summaryViewLayer4" width="95%" align="center">
								<tr>
									<td class="cellLabel">
										Keywords 
									</td>
									<td>
										<textarea name="sampleBean.keywordsStr" cols="80" rows="6">ACOUSTIC REFLECTIVITY
AVIDIN
BIOTIN
LIPID MONOLAYER
PERFLUOROCARBON
SMOOTH MUSCLE CELLS
TISSUE FACTOR
ULTRASOUND</textarea>
									</td>
								</tr>
							</table>
							<br>
						</td>
					</tr>
					<tr>
						<td class="cellLabel" width="10%">
							MIT_MGH-AWangCMC2008-02
						</td>
						<td colspan="2">
							<table class="summaryViewLayer4" width="95%" align="center">
								<tr>
									<td class="cellLabel">
										Point of Contact
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
										Primary Contact?: Yes
									</td>
									<td></td>
								</tr>
								<tr>
									<td style="word-wrap: break-word; max-width: 280px;">
										Contact Person:
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
									 	 Organization: WUSTL
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
										Role:
									</td>
									<td></td>
								</tr>
							</table>
							<br>
							<table class="summaryViewLayer4" width="95%" align="center">
								<tr>
									<td class="cellLabel">
										Keywords 
									</td>
									<td>
										<textarea name="sampleBean.keywordsStr" cols="80" rows="6">ACOUSTIC REFLECTIVITY
AVIDIN
BIOTIN
LIPID MONOLAYER
PERFLUOROCARBON
SMOOTH MUSCLE CELLS
TISSUE FACTOR
ULTRASOUND</textarea>
									</td>
								</tr>
							</table>
							<br>
						</td>
					</tr>
				</table> 
				</a>
		</td>
	</tr>
</table>
