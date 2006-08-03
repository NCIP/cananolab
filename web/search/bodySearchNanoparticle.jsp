<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:form action="searchNanoparticle">
	<table width="90%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Search Nanoparticle Metadata
				</h3>
			</td>
			<td align="right" width="15%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=search_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
					<TR>
						<td class="formTitle" width="30%">
							Search
						</td>
						<td class="formTitle">
							* Search for Wildcards
						</td>
					</TR>
					<tr>
						<td class="formLabel" width="30%">
							<strong> Particle Type </strong>
						</td>
						<td class="formField">
							<strong> <html:select property="particleType">
									<option value="" />
										<html:options name="allSampleTypes" />
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="3">
							<!-- action buttons begins -->
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td>
							<span class="formMessage"> </span>
							<br>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<div align="right">
											<input type="button" value="Reset" onClick="javascript:location.href='searchNanoparticle.do?dispatch=setup&page=0'">
											<input type="hidden" name="dispatch" value="search">
											<input type="hidden" name="page" value="1">
											<html:submit value="Search" />
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
<!--_____ main content ends _____-->
