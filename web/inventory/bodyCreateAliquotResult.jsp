<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/bodyMessage.jsp?bundle=inventory" />
<table width="100%" align="center">
	<tr>
		<td>
			<logic:iterate name="aliquotMatrix" id="aliquotRow">
				<logic:iterate name="aliquotRow" id="aliquot">
					<logic:present name="aliquot">
						<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
							<tbody>
								<tr class="topBorder">
									<td class="formTitle" colspan="2">
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
					</logic:present>
				</logic:iterate>
			</logic:iterate>
			<br>
		</td>
	</tr>
</table>
