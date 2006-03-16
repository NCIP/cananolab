<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@	taglib uri="/WEB-INF/c.tld" prefix="c"%>

<script type="text/javascript" src="javascript/calendar2.js"></script>
<script type="text/javascript">

function refreshContainers() {
  window.location.href="preCreateSample.do?numberOfContainers="+document.createSampleForm.numberOfContainers.value;
}

function openLink() {
  var url=document.createSampleForm.sampleSOP.value;
  if (url!='') {
    openWindow(url, 'sampleSOP', 600, 400);
  }
}
</script>

<html:form action="/createSample">
	<h2>
		<br>
		Create Sample
	</h2>
	<blockquote>
		<html:errors />
		<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
			<tbody>
				<tr class="topBorder">
					<td class="dataTablePrimaryLabel">
						<div align="justify">
							<em>DESCRIPTION</em>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Sample ID* <span class="formField"><span class="formFieldWhite"><html:text property="sampleId" size="10" /></span></span> &nbsp; &nbsp; Sample Type <span class="formFieldWhite"> <html:select property="sampleType">
										<option value=""></option>
										<html:options name="allSampleTypes" />
									</html:select> &nbsp; &nbsp; SOP <html:select property="sampleSOP" onchange="javascript:openLink()">
										<option value=""></option>
										<html:options name="allSampleSOPs" />
									</html:select></span></strong>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabelWhite">
						<div align="justify">
							<strong>Description <br> <span class="formField"><span class="formFieldWhite"><html:textarea property="sampleDescription" cols="70" /></span></span></strong>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Source <span class="formFieldWhite"> <html:text property="vendor" size="10" /></span> &nbsp; &nbsp; Source ID <span class="formFieldWhite"><html:text property="vendorSampleId" size="10" /> </span> &nbsp; &nbsp; Date Received <html:text
									property="dateReceived" size="7" /> <span class="formFieldWhite"> <a href="javascript:cal.popup();"><img height="18" src="images/calendar-icon.gif" width="22" border="0" alt="Click Here to Pick up the date"></a></span> </strong>
						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite">
						<div align="justify">
							<strong>Solubility <br> <span class="formFieldWhite"><html:textarea property="solubility" cols="70" /></span> &nbsp;</strong>
						</div>
					</td>
				</tr>

				<tr>
					<td class="formLabel">
						<div align="justify">
							<strong>Lot ID*&nbsp; <html:text property="lotId" size="5" /> &nbsp; &nbsp; &nbsp; Lot Description <span class="formFieldWhite"><html:text property="lotDescription" size="20" /></span> &nbsp; &nbsp; &nbsp; Number of Containers* <span
								class="formFieldWhite"> <html:text property="numberOfContainers" size="2" onchange="javascript:refreshContainers();" /> &nbsp;</span></strong>

						</div>
					</td>
				</tr>
				<tr>
					<td class="formLabelWhite">
						<div align="left">
							<strong>General Comments</strong>
							<br>
							<span class="formField"><span class="formFieldWhite"> <html:textarea property="generalComments" cols="70" /></span></span>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<br>
		<%-- if request parameter cnum is not entered assume cnum=1 --%>
		<c:choose>
			<c:when test='${param.numberOfContainers== null}'>
				<c:set var="cnum" value="1" />
			</c:when>
			<c:otherwise>
				<c:set var="cnum" value="${param.numberOfContainers}" />
				<%-- if numberOfContainers is not a number, set it to 0 --%>
				<c:catch var="e">
					<c:if test="${cnum>0}">
						<c:out value="${e}" />
					</c:if>
				</c:catch>
				<c:if test="${e!=null}">
					<c:set var="cnum" value="0" />
				</c:if>
			</c:otherwise>
		</c:choose>
		<%--create container for each container number --%>
		<%--		<c:forEach var="cnum" begin="1" end="${cnum}">--%>
		<%--		<logic:iterate name="createSampleForm" property="containers" id="container" type="gov.nih.nci.calab.dto.administration.ContainerBean" indexId="cnum">--%>
		<c:forEach var="containers" items="${createSampleForm.map.containers}" varStatus="status">
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="dataTablePrimaryLabel" width="30%">
							<div align="justify">
								<em>CONTAINER <c:out value="${status.index+1}" /></em>
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<div align="justify">
								<strong>Container Type* <span class="formFieldWhite"> <html:select name="containers" indexed="true" property="containerType">
											<html:options name="sampleContainerInfo" property="containerTypes" />
										</html:select></span> &nbsp; &nbsp; &nbsp; Other <span class="formFieldWhite"><html:text name="containers" indexed="true" property="otherContainerType" size="8" /></span> &nbsp; &nbsp; &nbsp; </strong>
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite style1">
							<div align="left">
								<strong>Quantity <span class="formFieldWhite"><html:text size="5" name="containers" indexed="true" property="quantity" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true" property="quantityUnit">
											<option value=""></option>
											<html:options name="sampleContainerInfo" property="quantityUnits" />
										</html:select> </span> &nbsp; Concentration <span class="formFieldWhite"><html:text size="8" name="containers" indexed="true" property="concentration" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true"
											property="concentrationUnit">
											<option value=""></option>
											<html:options name="sampleContainerInfo" property="concentrationUnits" />
										</html:select> </span>&nbsp; Volume <span class="formFieldWhite"><html:text size="8" name="containers" indexed="true" property="volume" /></span><span class="formFieldWhite"> <html:select name="containers" indexed="true" property="volumeUnit">
											<option value=""></option>
											<html:options name="sampleContainerInfo" property="volumeUnits" />
										</html:select></span></strong> &nbsp;&nbsp;&nbsp;
							</div>

							<div align="justify"></div>
						</td>
					</tr>
					<tr>
						<td class="formLabel">
							<div align="justify">
								<strong>Diluents/Solvent <html:text name="containers" indexed="true" property="solvent" size="10" /> &nbsp; &nbsp; &nbsp; Safety Precautions <html:text name="containers" indexed="true" property="safetyPrecaution" size="30" /> &nbsp; &nbsp; &nbsp;</strong>
							</div>
						</td>
					</tr>

					<tr>
						<td class="formLabelWhite style1">
							<div align="justify">
								<strong>Storage Conditions <span class="formField"><html:text name="containers" indexed="true" property="storageCondition" size="50" /></span></strong>
							</div>

							<div align="justify"></div>
						</td>
					</tr>

					<tr>
						<td class="formLabel">
							<div align="left">
								<strong>Storage Location<br> <br> Room&nbsp; <html:select name="containers" indexed="true" property="storageRoom">
										<option value=""></option>
										<html:options name="sampleContainerInfo" property="storageRooms" />
									</html:select> &nbsp; Freezer&nbsp; <html:select name="containers" indexed="true" property="storageFreezer">
										<option value=""></option>
										<html:options name="sampleContainerInfo" property="storageFreezers" />
									</html:select> &nbsp;Shelf &nbsp; <html:text name="containers" indexed="true" property="storageShelf" size="5" /> &nbsp; Box &nbsp; <html:text name="containers" indexed="true" property="storageBox" size="5" /> &nbsp;</strong>
							</div>
						</td>
					</tr>
					<tr>
						<td class="formLabelWhite">
							<div align="left">
								<strong>Comments</strong>
								<br>
								<span class="formField"><span class="formFieldWhite"> <html:textarea name="containers" indexed="true" property="containerComments" cols="70" /> </span></span>
								<br>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<br>
		</c:forEach>
		<%--		</logic:iterate>--%>

		<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
			<tbody>
				<tr>
					<td width="30%" class="formMessage">
						Accessioned by: Jane Doe Accession Date: 02/06/2006

						<table height="32" cellspacing="0" cellpadding="4" width="200" align="right" border="0">
							<tbody>
								<tr>
									<td width="198" height="32">
										<div align="right">
											<input type="reset" value="Reset">
											<html:submit />
										</div>
									</td>
								</tr>
							</tbody>
						</table>

						<div align="right"></div>
					</td>
				</tr>
			</tbody>
		</table>

		<p>
			&nbsp;
		</p>

		<p>
			&nbsp;
		</p>

		<p>
			&nbsp;
		</p>
	</blockquote>
</html:form>
<script language="JavaScript">
<!-- //
 var cal = new calendar2(document.forms['createSampleForm'].elements['dateReceived']);
 cal.year_scroll = true;
 cal.time_comp = false;
//-->
</script>
