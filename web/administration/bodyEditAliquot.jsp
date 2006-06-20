<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<table width="90%" align="center"><tr>
	<td width="10%">&nbsp;</td>
	<td>
		<h3><br>Edit Aliquot</h3>
	</td>
	<td align="right" width="10%">
		<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=edit_aliquot')" class="helpText">Help</a>
	</td>
</table>

<blockquote>
	<jsp:include page="/bodyMessage.jsp?bundle=administration" />
	<html:form action="/editAliquot">
		<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="90%" align="center" summary="" border="0">
			<tbody>
				<tr class="topBorder">
					<td class="formTitle" width="30%">
						<div align="justify">
							<em>Aliquot <bean:write name="editAliquotForm" property="aliquot.aliquotName" /></em>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Container Type* <span class="formFieldWhite"> <html:select property="aliquot.container.containerType">
										<option value=""></option>
										<html:options name="allAliquotContainerTypes" />
									</html:select></span> &nbsp; &nbsp; &nbsp; Other <span class="formFieldWhite"><html:text property="aliquot.container.otherContainerType" size="8" /></span> &nbsp; &nbsp; &nbsp; </strong>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite style1">
						<div align="left">
							<strong>Quantity <span class="formFieldWhite"><html:text size="5" property="aliquot.container.quantity" /></span> <span class="formFieldWhite"> <html:select property="aliquot.container.quantityUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="quantityUnits" />
									</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text size="5" property="aliquot.container.concentration" /></span><span class="formFieldWhite"> <html:select property="aliquot.container.concentrationUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="concentrationUnits" />
									</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text size="5" property="aliquot.container.volume" /></span><span class="formFieldWhite"> <html:select property="aliquot.container.volumeUnit">
										<option value=""></option>
										<html:options name="aliquotContainerInfo" property="volumeUnits" />
									</html:select></span></strong> &nbsp;&nbsp;&nbsp;
						</div>

						<div align="justify"></div>
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Diluents/Solvent <html:text property="aliquot.container.solvent" size="10" /> &nbsp; &nbsp; &nbsp; How Created <html:select property="aliquot.howCreated">
									<option value=""></option>
									<html:options collection="aliquotCreateMethods" property="value" labelProperty="label" />
								</html:select> &nbsp; &nbsp; &nbsp;</strong>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite style1">
						<div align="justify">
							<strong>Storage Conditions <span class="formField"><html:text property="aliquot.container.storageCondition" size="50" /></span></strong>
						</div>

						<div align="justify"></div>
					</td>
				</tr>
				<tr>
					<td class="formLabel">
						<div align="left">
							<strong>Storage Location<br> <br> Room&nbsp; <html:select property="aliquot.container.storageLocation.room">
									<option value=""></option>
									<html:options name="aliquotContainerInfo" property="storageRooms" />
								</html:select> &nbsp; Freezer&nbsp; <html:select property="aliquot.container.storageLocation.freezer">
									<option value=""></option>
									<html:options name="aliquotContainerInfo" property="storageFreezers" />
								</html:select> &nbsp;Shelf &nbsp; <html:text property="aliquot.container.storageLocation.shelf" size="5" /> &nbsp; Box &nbsp; <html:text property="aliquot.container.storageLocation.box" size="5" /> &nbsp;</strong>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>General Comments</strong>
							<br>
							<span class="formField"><span class="formFieldWhite"> <html:textarea property="aliquot.container.containerComments" cols="70" /></span></span>
						</div>
					</td>
				</tr>
				<tr>
					<td height="32">
						<div align="right">
							<html:hidden property="rowNum" />
							<html:hidden property="colNum" />
							<html:hidden property="aliquot.aliquotName" />
							<html:hidden property="aliquot.howCreated" />
							<html:hidden property="aliquot.creator"/>
							<html:hidden property="aliquot.creationDateStr"/>
							<input type="reset" value="Reset">
							<input type="button" value="Cancel" onclick="javascript:history.go(-1)">
							<html:submit />
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</html:form>
</blockquote>
