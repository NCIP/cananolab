<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/calendar2.js"></script>

<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Summary
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Characterization Source* </strong>
		</td>
		<td class="label">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<html:select property="achar.characterizationSource"
						styleId="charSource"
						onchange="javascript:callPrompt('Characterization Source', 'charSource');">
						<option value=""></option>
						<html:options name="characterizationSources" />
						<option value="other">
							[Other]
						</option>
					</html:select>
				</c:when>
				<c:otherwise>
						${characterizationForm.map.achar.characterizationSource}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>View Title*</strong>
			<br>
			<em>(text will be truncated after 20 characters)</em>
		</td>
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<html:text property="achar.viewTitle" size="30" />
				</c:when>
				<c:otherwise>
						${characterizationForm.map.achar.viewTitle}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Characterization Date</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<html:text property="achar.dateString" size="10" styleId="charDate" />
					<a href="javascript:cal1.popup();"><img
							src="images/calendar-icon.gif" width="22" height="18" border="0"
							alt="Click Here to Pick up the date" 
							title="Click Here to Pick up the date" align="middle">
					</a>
				</c:when>
				<c:otherwise>
						${characterizationForm.map.achar.dateString}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
	<tr>
		<td class="leftLabel">
			<strong>Protocol Name - Version</strong>
		</td>
		<c:choose>
			<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
				<html:hidden styleId="updatedUri" property="achar.protocolFileBean.domainFile.uri" />
				<td class="rightLabel" colspan="3">
					<c:choose>
						<c:when test="${!empty characterizationProtocolFiles}">
							<html:select styleId="protocolFileId"
								property="achar.protocolFileBean.domainFile.id"
								onchange="retrieveProtocolFile()">
								<option />
									<html:options collection="characterizationProtocolFiles"
										property="domainFile.id" labelProperty="displayName" />
							</html:select> &nbsp;<span id="protocolFileLink"><a
								href="searchProtocol.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.protocolFileBean.domainFile.id}&amp;location=${location}">${characterizationForm.map.achar.protocolFileBean.domainFile.uri}</a>
							</span>
						</c:when>
						<c:otherwise>
						    No protocols available.
						</c:otherwise>
					</c:choose>
				</td>
			</c:when>
			<c:otherwise>
				<td class="rightLabel" colspan="3">
					<c:choose>
						<c:when
							test="${empty characterizationForm.map.achar.protocolFileBean.domainFile.id}">
									No protocol loaded
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${characterizationForm.map.achar.protocolFileBean.hidden eq 'false'}">
						${characterizationForm.map.achar.protocolFileBean.displayName}&nbsp;
						<a
										href="searchProtocol.do?dispatch=download&amp;fileId=${characterizationForm.map.achar.protocolFileBean.domainFile.id}&amp;location=${location}">${characterizationForm.map.achar.protocolFileBean.domainFile.uri}</a>
								</c:when>
								<c:otherwise>
									The protocol file is private
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
					<html:textarea property="achar.description" rows="3" cols="80" />
				</c:when>
				<c:otherwise>
						${characterizationForm.map.achar.description}&nbsp;
					</c:otherwise>
			</c:choose>
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
