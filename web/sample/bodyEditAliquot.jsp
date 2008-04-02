<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				Edit Aliquot
			</h3>
		</td>
		<td align="right" width="10%">
			<a
				href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=edit_aliquot')"
				class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=sample" />

			<html:form action="/editAliquot">
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" width="30%">
								<div align="justify">
									<em>Aliquot <bean:write name="editAliquotForm"
											property="aliquot.aliquotName" /> </em>
								</div>
							</td>
						</tr>

						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Container Type* <span class="formFieldWhite">
											<html:select styleId="containerType"
												property="aliquot.container.containerType"
												onchange="javascript:callPrompt('Container Type', 'containerType');">
												<html:options name="allAliquotContainerTypes" />
												<option value="other">[Other]</option>
											</html:select> </span> </strong>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite style1">
								<div align="left">
									<strong>Quantity <span class="formFieldWhite"><html:text
												size="5" property="aliquot.container.quantity" /> </span> <span
										class="formFieldWhite"> <html:select
												property="aliquot.container.quantityUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="quantityUnits" />
											</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text
												size="5" property="aliquot.container.concentration" /> </span><span
										class="formFieldWhite"> <html:select
												property="aliquot.container.concentrationUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="concentrationUnits" />
											</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text
												size="5" property="aliquot.container.volume" /> </span><span
										class="formFieldWhite"> <html:select
												property="aliquot.container.volumeUnit">
												<option/>
												<html:options name="aliquotContainerInfo"
													property="volumeUnits" />
											</html:select> </span> </strong> &nbsp;&nbsp;&nbsp;
								</div>

								<div align="justify"></div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Diluents/Solvent <html:text
											property="aliquot.container.solvent" size="10" /> &nbsp;
										&nbsp; &nbsp; How Created <html:select
											property="aliquot.howCreated">
											<option/>
											<html:options collection="aliquotCreateMethods"
												property="value" labelProperty="label" />
										</html:select> &nbsp; &nbsp; &nbsp;</strong>
								</div>
							</td>
						</tr>

						<tr>
							<td class="formLabelWhite style1">
								<div align="justify">
									<strong>Storage Conditions <span class="formField"><html:text
												property="aliquot.container.storageCondition" size="50" />
									</span> </strong>
								</div>

								<div align="justify"></div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<strong>Storage Location<br> <br>
									<table class="topBorderOnly" cellspacing="0" cellpadding="3"
										align="left" summary="" border="0">
										<strong>
										<tr>
											<td class="borderlessLabel" width="20%">
												<strong>Room</strong>
											</td>
											<td class="borderlessLabel">
												<html:select styleId="room"
													property="aliquot.container.storageLocation.room"
													onchange="javascript:callPrompt('Room', 'room');">
													<html:options name="aliquotContainerInfo"
														property="storageRooms" />
													<option value="other">[Other]</option>
												</html:select>
											</td>											<td class="borderlessLabel">
												<strong>Freezer</strong>
											</td>
											<td class="borderlessLabel">
												<html:select styleId="freezer"
													property="aliquot.container.storageLocation.freezer"
													onchange="javascript:callPrompt('Freezer', 'freezer');">
													<html:options name="aliquotContainerInfo"
														property="storageFreezers" />
													<option value="other">[Other]</option>
												</html:select>
											</td>
											<td class="borderlessLabel">
												<strong>Shelf</strong>
											</td>
											<td class="borderlessLabel">
												<html:select styleId="shelf"
													property="aliquot.container.storageLocation.shelf"
													onchange="javascript:callPrompt('Shelf', 'shelf');">
													<html:options name="aliquotContainerInfo"
														property="storageShelves" />
													<option value="other">[Other]</option>
												</html:select>
											</td>
											<td class="borderlessLabel">
												<strong>Box</strong>
											</td>
											<td class="borderlessLabel">
												<html:select styleId="box"
													property="aliquot.container.storageLocation.box"
													onchange="javascript:callPrompt('Box', 'box');">
													<html:options name="aliquotContainerInfo"
														property="storageBoxes" />
													<option value="other">[Other]</option>
												</html:select>
											</td>
										</tr>
									</table>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="left">
									<strong>General Comments</strong>
									<br>
									<span class="formField"><span class="formFieldWhite">
											<html:textarea property="aliquot.container.containerComments"
												cols="70" /> </span> </span>
								</div>
							</td>
						</tr>
						<tr>
							<td height="32">
								<div align="right">
									<html:hidden property="rowNum" />
									<html:hidden property="colNum" />
									<html:hidden property="aliquot.aliquotName" />
									<html:hidden property="aliquot.creator" />
									<input type="reset" value="Reset">
									<input type="button" value="Cancel"
										onclick="javascript:cancel()">
									<input type="hidden" name="dispatch" value="edit">
									<input type="hidden" name="page" value="1">
									<html:submit />
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</html:form>
		</td>
	</tr>
</table>
