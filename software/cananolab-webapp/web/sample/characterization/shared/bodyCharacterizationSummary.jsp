<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/calendar2.js"></script>
<table width="100%" align="center" class="submissionView">
	<%-- can't change characterization type and name in edit mode --%>
	<c:if test="${empty characterizationForm.map.achar.domainChar.id}">
		<tr>
			<td class="cellLabel">
				Characterization Type *
			</td>
			<td>
				<div id="charTypePrompt">
					<html:select property="achar.characterizationType"
						styleId="charType"
						onchange="javascript:callPrompt('Characterization Type', 'charType', 'charTypePrompt');setCharacterizationOptionsByCharTypeWithOther()">
						<option value=""></option>
						<html:options name="characterizationTypes" />
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
			<td class="cellLabel">
				Characterization Name *
			</td>
			<td>
				<div id="charNamePrompt">
					<html:select property="achar.characterizationName"
						styleId="charName"
						onchange="javascript:callPrompt('Characterization', 'charName', 'charNamePrompt');setAssayTypeOptionsByCharName();setCharacterizationDetail();">
						<option value=""></option>
						<c:if test="${!empty charTypeChars }">
							<html:options name="charTypeChars" />
						</c:if>
						<option value="other">
							[other]
						</option>
					</html:select>
				</div>
			</td>
		</tr>
	</c:if>
	<tr>
		<td class="cellLabel">
			Assay Type
		</td>
		<td>
			<div id="assayTypePrompt">
				<html:select property="achar.assayType" styleId="assayType"
					onchange="javascript:callPrompt('Assay Type', 'assayType', 'assayTypePrompt');">
					<option value=""></option>
					<c:if
						test="${characterizationForm.map.achar.characterizationType eq 'physico chemical characterization'}">
						<option
							value="${characterizationForm.map.achar.characterizationName}" selected="selected">
							<c:out value="${characterizationForm.map.achar.characterizationName}"/>
						</option>
					</c:if>
					<c:if test="${!empty charNameAssays }">
						<html:options name="charNameAssays" />
					</c:if>
					<option value="other">
						[other]
					</option>
				</html:select>
			</div>
		</td>
		<td class="cellLabel">
			Protocol Name - Version
		</td>
		<html:hidden styleId="updatedUri"
			property="achar.protocolBean.fileBean.domainFile.uri" />
		<td colspan="3">
			<c:choose>
				<c:when test="${!empty characterizationProtocols}">
					<html:select styleId="protocolId"
						property="achar.protocolBean.domain.id">
						<%-- onchange="retrieveProtocolFile()"> --%>
						<option />
							<html:options collection="characterizationProtocols"
								property="domain.id" labelProperty="displayName" />
					</html:select> &nbsp;<span id="protocolFileLink"><a
						href="protocol.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.protocolBean.fileBean.domainFile.id}">${characterizationForm.map.achar.protocolBean.fileBean.domainFile.uri}</a>
					</span>
				</c:when>
				<c:otherwise>
				    No protocols available.
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="cellLabel">
			Characterization Source
		</td>
		<td>
			<html:select property="achar.pocBean.domain.id" styleId="charSource">
				<option value=""></option>
				<html:options collection="samplePointOfContacts"
					labelProperty="displayName" property="domain.id" />
			</html:select>
		</td>
		<td class="cellLabel">
			Characterization Date
		</td>
		<td>
			<html:text property="achar.dateString" size="10" styleId="charDate" />
			<a href="javascript:cal1.popup();"><img
					src="images/calendar-icon.gif" width="22" height="18" border="0"
					alt="Click Here to Pick up the date"
					title="Click Here to Pick up the date" align="top"> </a>
		</td>
	</tr>
</table>
<br>
<script language="JavaScript">
	<!-- //
		var cal1 = new calendar2(document.getElementById('charDate'));
	    cal1.year_scroll = true;
		cal1.time_comp = false;
		cal1.context = '${pageContext.request.contextPath}';
  	//-->
</script>
