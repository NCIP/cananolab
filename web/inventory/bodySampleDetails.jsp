<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table width="100%" align="center">
	<tr>
		<td>
			<c:choose>
				<c:when test="${empty param.containerId}">
					<h3>
						<br>
						The following sample is successfully created:
					</h3>
				</c:when>
			</c:choose>
		</td>
		<td align="right" width="15%">
			<c:choose>
				<c:when test="${not empty param.containerId}">
					<input type="button" onClick="javascript:history.go(-1);" value="Back">
				</c:when>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
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
							<strong>Sample ID*</strong>
						</div>
					</td>

					<td class="formField" width="72%">
						<bean:write name="sample" property="sampleName" />
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>Type</strong>
						</div>
						&nbsp;
					</td>

					<td class="formFieldWhite">
						<logic:notEqual name="sample" property="sampleType" value="Other">
							<bean:write name="sample" property="sampleType" />
						</logic:notEqual>
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Description</strong>
						</div>
					</td>

					<td class="formField">
						<bean:write name="sample" property="sampleDescription" />
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
						<bean:write name="sample" property="sampleSource" />
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
						<bean:write name="sample" property="sourceSampleId" />
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>Date Received</strong>
						</div>
					</td>

					<td class="formFieldWhite">
						<bean:write name="sample" property="dateReceivedStr" />
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Solubility</strong>
						</div>
					</td>

					<td class="formField">
						<bean:write name="sample" property="solubility" />
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>Lot ID*</strong>
						</div>
					</td>

					<td class="formFieldWhite">
						<bean:write name="sample" property="lotId" />
						&nbsp;
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Lot Description</strong>&nbsp;
						</div>
					</td>

					<td class="formField">
						<bean:write name="sample" property="lotDescription" />
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
						<bean:write name="sample" property="numberOfContainers" />
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
						<bean:write name="sample" property="generalComments" />
						&nbsp;
					</td>
				</tr>				
			</table>
			<br>

			<logic:iterate name="sample" property="containers" id="container" indexId="cnum">
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<c:choose>
								<c:when test="${param.containerId==container.containerId}">
									<c:set var="style" value="dataTableHighlightLabel" />
								</c:when>
								<c:otherwise>
									<c:set var="style" value="formTitle" />
								</c:otherwise>
							</c:choose>
							<td class="${style}" colspan="2">
								<div align="justify">
									Container
									<c:out value="${container.containerName}" />
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
								<logic:notEqual name="container" property="containerType" value="Other">
									<bean:write name="container" property="containerType" />
								</logic:notEqual>
								<logic:equal name="container" property="containerType" value="Other">
									<bean:write name="container" property="otherContainerType" />
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
								<bean:write name="container" property="quantity" />
								<bean:write name="container" property="quantityUnit" />
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
								<bean:write name="container" property="concentration" />
								&nbsp;
								<bean:write name="container" property="concentrationUnit" />
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
								<bean:write name="container" property="volume" />
								&nbsp;
								<bean:write name="container" property="volumeUnit" />
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
								<bean:write name="container" property="solvent" />
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
								<bean:write name="container" property="safetyPrecaution" />
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
								<bean:write name="container" property="storageCondition" />
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
								<bean:write name="container" property="storageLocation.room" />
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
								<bean:write name="container" property="storageLocation.freezer" />
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
								<bean:write name="container" property="storageLocation.shelf" />
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
								<bean:write name="container" property="storageLocation.box" />
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
								<bean:write name="container" property="containerComments" />
								&nbsp;
							</td>
						</tr>
					</tbody>
				</table>
				<br>
			</logic:iterate>

			<table class="topBorderOnly" cellspacing="0" cellpadding="3" align="center" width="100%" summary="" border="0">
				<tr>
					<td width="30%" class="formMessage">
						Accessioned by:
						<bean:write name="sample" property="sampleSubmitter" />
						<br>
						Accession Date:
						<bean:write name="sample" property="accessionDateStr" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
