<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td>
			<h3>
				<br>
				${particle.sampleName} General Information
			</h3>
		</td>
		<td align="right" width="15%">
			<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=create_nanoparticle')" class="helpText">Help</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<jsp:include page="/bodyMessage.jsp?bundle=submit" />
			<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
				<tbody>
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								General Information
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Particle Type</strong>
						</td>
						<td class="rightLabel">
							${particle.sampleType}
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Particle ID</strong>
						</td>
						<td class="rightLabel">
							${particle.sampleName}
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Keywords <em>(one per line)</em></strong>
						</td>
						<td class="rightLabel">						
						    <c:out value="${particle.keywords}" escapeXml="false"/>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong>Visibility</strong>
						</td>
						<td class="rightLabel">
							&nbsp;
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
