<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/publishReport" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Submit Nanoparticle Report
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caLAB_1.0_OH&amp;topic=nano_report_help')" class="helpText">Help</a>
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
									Description
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Particle ID*</strong>
							</td>
							<td class="rightLabel"">
								<html:select property="particleNames" multiple="true" size="5">
									<html:options name="allSampleNames" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Report Type*</strong>
							</td>
							<td class="rightLabel"">
								<html:select property="file.type">
									<html:options name="allReportTypes" />
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Report File*</strong>
							</td>
							<td class="rightLabel"">
								<html:file property="uploadedFile" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Report File Title*</strong>
							</td>
							<td class="rightLabel"">
								<html:text property="file.title" size="80"/>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Report File Description</strong>
							</td>
							<td class="rightLabel"">
								<html:textarea property="file.description" rows="3" cols="80" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Comments</strong>
							</td>
							<td class="rightLabel"">
								<html:textarea property="file.comments"  rows="3" cols="80" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="file.visibilityGroups" multiple="true" size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and ${applicationOwner}_PI are defaults if none of above is selected.)</i>								
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="32" border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="reset" value="Reset" onclick="javascript:resetSelect(document.publishReportForm.particleNames));">
												<input type="hidden" name="dispatch" value="submit">
												<input type="hidden" name="page" value="2">
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
