<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<h2>
	<br>
	<logic:notPresent name="containerNum">
	The following sample is successfully created:
	</logic:notPresent>
</h2>
<blockquote>
	<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="90%" align="center" summary="" border="0">
		<tbody>
			<tr class="topBorder">
				<td class="dataTablePrimaryLabel" colspan="2">
					<div align="justify">
						<em>DESCRIPTION</em>
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
					<span class="formMessage"><span style="FONT-SIZE: 8pt"><bean:write name="sample" property="sampleId" /></span></span>
				</td>
			</tr>

			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<span class="formFieldWhite"><strong>Type</strong></span>
					</div>
				</td>

				<td class="formFieldWhite">
					<bean:write name="sample" property="sampleType" />
				</td>
			</tr>

			<tr>
				<td class="formLabel">
					<div align="left">
						<strong>Description</strong>
					</div>
				</td>

				<td class="formField">
					<span class="formFieldWhite"><span class="formMessage"><span style="FONT-SIZE: 8pt"><bean:write name="sample" property="sampleDescription" /></span></span></span>
				</td>
			</tr>

			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<strong>Source</strong>
					</div>
				</td>

				<td class="formFieldWhite">
					&nbsp;
					<bean:write name="sample" property="sampleSource" />
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
				</td>
			</tr>

			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<strong><strong>Date Received</strong></strong>
					</div>
				</td>

				<td class="formFieldWhite">
					&nbsp;
					<bean:write name="sample" property="dateReceived" />
				</td>
			</tr>

			<tr>
				<td class="formLabel">
					<div align="left">
						<span class="formField"><strong>Solubility</strong></span>
					</div>
				</td>

				<td class="formField">
					&nbsp;
					<bean:write name="sample" property="solubility" />
				</td>
			</tr>

			<tr>
				<td class="formLabelWhite">
					<div align="left">
						<strong>Lot ID*</strong>
					</div>
				</td>

				<td class="formFieldWhite">
					&nbsp;
					<bean:write name="sample" property="lotId" />
				</td>
			</tr>

			<tr>
				<td class="formLabel">
					<div align="left">
						<span class="formField"><strong>Lot Description</strong></span>
					</div>
				</td>

				<td class="formField">
					<bean:write name="sample" property="lotDescription" />
					&nbsp; &nbsp; &nbsp;
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
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="left">
						<strong>General Comments </strong>
					</div>
				</td>

				<td class="formFieldWhite">
					<bean:write name="sample" property="generalComments" />
				</td>
			</tr>
		</tbody>
	</table>
	<br>

	<logic:iterate name="sample" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="cnum">
		<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="90%" align="center" summary="" border="0">
			<tbody>
				<tr class="topBorder">
					<c:choose>
						<c:when test="${containerNum == cnum}">
							<c:set var="style" value="dataTableHighlightLabel" />
						</c:when>
						<c:otherwise>
							<c:set var="style" value="dataTablePrimaryLabel" />
						</c:otherwise>
					</c:choose>
					<td class="${style}" colspan="2">
						<div align="justify">
							<em>Container <c:out value="${cnum+1}" /> </em>
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
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="left">
							<span class="formField"><strong>Storage Conditions</strong></span>
						</div>
					</td>

					<td class="formField">
						<bean:write name="container" property="storageCondition" />
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite" colspan="2">
						<div align="left">
							<strong>Storage Location...</strong>
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
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Comments</strong>
						</div>
					</td>

					<td class="formFieldWhite">
						<bean:write name="container" property="containerComments" />
					</td>
				</tr>
			</tbody>
		</table>
		<br>
	</logic:iterate>

	<table class="topBorderOnly" cellspacing="0" cellpadding="3" align="center" width="90%" summary="" border="0">

		<tr>
			<td width="30%" class="formMessage">
				Accessioned by:
				<bean:write name="sample" property="sampleSubmitter" />
				Accession Date:
				<bean:write name="sample" property="accessionDate" />
			</td>
		</tr>
		<tr valign="bottom" align="right">
			<td colspan="8">
				<input type="button" onClick="javascript:history.go(-1);" value="Back">
			</td>
		</tr>

	</table>
	<br>
	<br>
</blockquote>

