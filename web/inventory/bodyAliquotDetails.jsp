<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center">
	<tr>
		<td>
			<c:choose>
				<c:when test="${empty param.aliquotId}">
					<h3>
						<br>
						The following aliquot is successfully created:
					</h3>
				</c:when>
			</c:choose>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=aliquot_details_help')" class="helpText">Help</a>&nbsp;&nbsp;
			<c:choose>
				<c:when test="${not empty param.aliquotId}">
					<a href="javascript:history.go(-1)" class="helpText">back</a>
				</c:when>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=inventory" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="2">
							<div align="justify">
								Sample
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="28%">
							<div align="left">
								<strong>Sample ID</strong>
							</div>
						</td>

						<td class="formField" width="72%">
							<bean:write name="aliquot" property="sample.sampleName" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Type</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="sample.sampleType" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Description</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="sample.sampleDescription" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Source</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="sample.sampleSource" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Source ID</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="sample.sourceSampleId" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong><strong>Date Received</strong></strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="sample.dateReceivedStr" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<span class="formField"><strong>Solubility</strong></span>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="sample.solubility" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Lot ID</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="sample.lotId" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<span class="formField"><strong>Lot Description</strong></span>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="sample.lotDescription" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Number of Containers </strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="sample.numberOfContainers" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>General Comments </strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="sample.generalComments" />
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
			<br>
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="dataTableHighlightLabel" colspan="2">
							<div align="justify">
								Aliquot
								<bean:write name="aliquot" property="aliquotName" />
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabel" width="28%">
							<div align="left">
								<strong>Container Type</strong>
							</div>
						</td>
						<td class="formField" width="72%">
							<logic:notEqual name="aliquot" property="container.containerType" value="Other">
								<bean:write name="aliquot" property="container.containerType" />
							</logic:notEqual>
							<logic:equal name="aliquot" property="container.containerType" value="Other">
								<bean:write name="aliquot" property="container.otherContainerType" />
							</logic:equal>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Initial Quantity</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="container.quantity" />
							<bean:write name="aliquot" property="container.quantityUnit" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Concentration</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.concentration" />
							&nbsp;
							<bean:write name="aliquot" property="container.concentrationUnit" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Volume</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="container.volume" />
							&nbsp;
							<bean:write name="aliquot" property="container.volumeUnit" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Diluents/Solvent</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.solvent" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Safety Precautions</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="container.safetyPrecaution" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Storage Conditions</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.storageCondition" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite" colspan="2">
							<div align="left">
								<strong>Storage Location</strong>
							</div>
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Room</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.storageLocation.room" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Freezer</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="container.storageLocation.freezer" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Shelf</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.storageLocation.shelf" />
							&nbsp;
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Box</strong>
							</div>
						</td>

						<td class="formFieldWhite">
							<bean:write name="aliquot" property="container.storageLocation.box" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Comments</strong>
							</div>
						</td>

						<td class="formField">
							<bean:write name="aliquot" property="container.containerComments" />
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
			<br>
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" align="center" width="100%" summary="" border="0">
				<tr>
					<td width="30%" class="formMessage">
						Aliquoted by:
						<bean:write name="aliquot" property="creator" />
						<br>
						Aliquoted Date:
						<bean:write name="aliquot" property="creationDateStr" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
