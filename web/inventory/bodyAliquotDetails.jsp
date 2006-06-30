<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<br>
<h4>
	The following aliquots are successfully created for
	<bean:write name="fullParentName" />
</h4>
<blockquote>
	<logic:iterate name="aliquotMatrix" id="aliquotRow" type="gov.nih.nci.calab.dto.inventory.AliquotBean[]" indexId="cnum">
		<logic:iterate name="aliquotRow" id="aliquot" type="gov.nih.nci.calab.dto.inventory.AliquotBean">
			<logic:present name="aliquot">
			<%--
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="90%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="dataTablePrimaryLabel" width="30%">
								<div align="justify">
									<em>ALIQUOT <bean:write name="aliquot" property="aliquotName" /></em>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Container Type </strong> <span class="formFieldWhite"> <logic:present name="aliquot" property="container.containerType">
											<bean:write name="aliquot" property="container.containerType" />
										</logic:present> <logic:notPresent name="aliquot" property="container.containerType">
											<bean:write name="aliquot" property="container.otherContainerType" />
										</logic:notPresent> </span>
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite style1">
								<div align="left">
									<strong>Quantity </strong><span class="formFieldWhite"><bean:write name="aliquot" property="container.quantity" /></span> <span class="formFieldWhite"> <bean:write name="aliquot" property="container.quantityUnit" /></span> &nbsp; <strong>Concentration
									</strong><span class="formFieldWhite"><bean:write name="aliquot" property="container.concentration" /></span> <span class="formFieldWhite"> <bean:write name="aliquot" property="container.concentrationUnit" /> </span>&nbsp; <strong> Volume </strong><span
										class="formFieldWhite"><bean:write name="aliquot" property="container.volume" /></span><span class="formFieldWhite"> <bean:write name="aliquot" property="container.volumeUnit" /> </span> &nbsp;&nbsp;&nbsp;
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="justify">
									<strong>Diluents/Solvent </strong>
									<bean:write name="aliquot" property="container.solvent" />
									&nbsp; &nbsp; &nbsp; <strong>How Created</strong>
									<bean:write name="aliquot" property="howCreated" />
									&nbsp; &nbsp; &nbsp;
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite style1">
								<div align="justify">
									<strong>Storage Conditions </strong><span class="formField"><bean:write name="aliquot" property="container.storageCondition" /></span>
								</div>

								<div align="justify"></div>
							</td>
						</tr>
						<tr>
							<td class="formLabel">
								<div align="left">
									<strong>Storage Location<br> <br> Room&nbsp; </strong>
									<bean:write name="aliquot" property="container.storageLocation.room" />
									&nbsp;<strong> Freezer&nbsp; </strong>
									<bean:write name="aliquot" property="container.storageLocation.freezer" />
									&nbsp;<strong>Shelf </strong>&nbsp;
									<bean:write name="aliquot" property="container.storageLocation.shelf" />
									&nbsp;<strong> Box </strong>&nbsp;
									<bean:write name="aliquot" property="container.storageLocation.box" />
									&nbsp;
								</div>
							</td>
						</tr>
						<tr>
							<td class="formLabelWhite">
								<div align="left">
									<strong>General Comments</strong>
									<br>
									<span class="formFieldWhite"> <bean:write name="aliquot" property="container.containerComments" /></span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				--%>
				<bean:write name="aliquot" property="aliquotName"/>
				<br>
			</logic:present>

		</logic:iterate>
	</logic:iterate>

	<br>
</blockquote>
