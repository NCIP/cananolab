<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>
<script type='text/javascript' src='javascript/ProtocolManager.js'></script>
<script type='text/javascript' src='dwr/interface/ProtocolManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>

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
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<html:select property="achar.characterizationSource"
						onkeydown="javascript:fnKeyDownHandler(this, event);"
						onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
						onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
						onchange="fnChangeHandler_A(this, event);">
						<option value="">
							--?--
						</option>
						<html:options name="characterizationSources" />
					</html:select>
				</c:when>
				<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.characterizationSource}&nbsp;
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
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<html:text property="achar.viewTitle" size="30" />
				</c:when>
				<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.viewTitle}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Protocol Name - Version</strong>
		</td>
		<c:choose>
			<c:when test="${canCreateNanoparticle eq 'true'}">
				<td class="label" colspan="3">
					<c:choose>
						<c:when test="${!empty submitTypeProtocolFiles}">
							<html:select styleId="protocolId"
								property="achar.protocolFileBean.id"
								onchange="retrieveProtocolFile()">
								<option />
									<html:options collection="submitTypeProtocolFiles"
										property="id" labelProperty="displayName" />
							</html:select> &nbsp;<span id="protocolLink"><a
								href="searchProtocol.do?dispatch=download&amp;fileId=${nanoparticleCharacterizationForm.map.achar.protocolFileBean.id}">${nanoparticleCharacterizationForm.map.achar.protocolFileBean.uri}</a>
							</span>
						</c:when>
						<c:otherwise>
						    No protocols available.
						</c:otherwise>
					</c:choose>
				</td>
			</c:when>
			<c:otherwise>
				<td class="label" colspan="3">
					<c:choose>
						<c:when
							test="${empty nanoparticleCharacterizationForm.map.achar.protocolFileBean.id}">
									No protocol loaded
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${nanoparticleCharacterizationForm.map.achar.protocolFileBean.hidden eq 'false'}">
						${nanoparticleCharacterizationForm.map.achar.protocolFileBean.displayName}&nbsp;
						<a
										href="searchProtocol.do?dispatch=download&amp;fileId=${nanoparticleCharacterizationForm.map.achar.protocolFileBean.id}">${nanoparticleCharacterizationForm.map.achar.protocolFileBean.uri}</a>
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
				<c:when test="${canCreateNanoparticle eq 'true'}">
					<html:textarea property="achar.description" rows="3" cols="80" />
				</c:when>
				<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.description}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>

