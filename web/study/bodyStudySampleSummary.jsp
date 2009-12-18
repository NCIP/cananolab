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

<table width="100%" align="center" class="summaryViewLayer2">
	<tr>
		<th align="left">
			Sample &nbsp;&nbsp;&nbsp;
			<a href="study.do?dispatch=sampleAdd"
				class="addlink"><img align="middle" src="images/btn_add.gif"
					border="0" /></a>
		</th>
	</tr>
	<tr>
		<td>
			<table width="100%" align="center" class="summaryViewLayer3">
				<tr>
					<th valign="top" align="left" width="20%">
						Sample Information
					</th>
					<th valign="top" align="right" width="80%">
						<a href="sample.do?dispatch=summaryEdit&page=0&sampleId=11337747">Edit</a>
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						MIT_MGH-KKellyIB2009-01
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Keywords
						<br>
						<i>(one keyword per line)</i>
					</td>
					<td>
						DEXTRAN
						<br>
						ENDOTHELIAL CELLS
						<br>
						EX VIVO
						<br>
						FITC
						<br>
						FITC IMMUNOASSAY
						<br>
						FLUORESCENCE
						<br>
						HUMAN CAROTID
						<br>
						IN VIVO
						<br>
						IRON OXIDE
						<br>
						MACROPHAGE
						<br>
						SUPERPARAMAGNETIC
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Point of Contact
					</td>
					<td>
					<table class="summaryViewLayer4" align="center" width="95%">
						<tbody><tr>
							<th>
								Primary Contact?
							</th>
							<th>
								Contact Person
							</th>
							<th>
								Organization
							</th>
							<th>
								Role
							</th>
							<th></th>
						</tr>
							<tr>
								<td>
									Yes
								</td>
								<td>
								</td>
								<td>
									MIT_MGH
								</td>
								<td>
								</td>
							</tr>
					</tbody></table>
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
						Sample Information
					</th>
					<th valign="top" align="right" width="80%">
						<a href="sample.do?dispatch=summaryEdit&page=0&sampleId=11337748">Edit</a>
					</th>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						MIT_MGH-KKellyIB2009-02
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Keywords
						<br>
						<i>(one keyword per line)</i>
					</td>
					<td>
						ANHYDRIDE
						<br>
						DEXTRAN
						<br>
						ENDOTHELIAL CELLS
						<br>
						FITC
						<br>
						FITC IMMUNOASSAY
						<br>
						FLUORESCENCE
						<br>
						IRON OXIDE
						<br>
						MACROPHAGE
						<br>
						SUPERPARAMAGNETIC
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Point of Contact
					</td>
					<td>
					<table class="summaryViewLayer4" align="center" width="95%">
						<tbody><tr>
							<th>
								Primary Contact?
							</th>
							<th>
								Contact Person
							</th>
							<th>
								Organization
							</th>
							<th>
								Role
							</th>
							<th></th>
						</tr>
							<tr>
								<td>
									Yes
								</td>
								<td>
								</td>
								<td>
									MIT_MGH
								</td>
								<td>
								</td>
							</tr>
					</tbody></table>
					</td>
				</tr>
			</table>
			<br>
		</td>
	</tr>
</table>
