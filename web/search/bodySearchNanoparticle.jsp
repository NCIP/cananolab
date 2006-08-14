<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:form action="searchNanoparticle">
	<table align="center">
		<tr>
			<td>
				<h3>
					<br>
					Search Nanoparticle - Basic Search
				</h3>
			</td>
			<td align="right" width="25%">
				<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; <a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=search_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle" colspan="4">
							<div align="justify">
								Search Criteria
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong> Particle Source </strong>
						</td>
						<td class="label">
							<strong> <html:select property="particleSource">
									<option value="" />
										<html:options name="allParticleSources" />
								</html:select></strong>
						</td>
						<td class="label">
							<strong> Particle Type </strong>
						</td>
						<td class="rightLabel">
							<strong> <html:select property="particleType">
									<option value="" />
										<html:options name="allParticleTypes" />
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong> Function Type </strong>
						</td>
						<td class="label">
							<strong> <html:select property="functionType">
									<option value="" />
										<html:options name="allParticleFunctionTypes" />
								</html:select></strong>
						</td>
						<td class="label">
							<strong> Characterization Type </strong>
						</td>
						<td class="rightLabel">
							<strong> <html:select property="characterizationType">
									<option value="" />
										<html:options name="allCharacterizationTypes" />
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel">
							<strong> Keywords <em>(one per line)</em> </strong>
						</td>
						<td class="rightLabel" colspan="3">
							<html:textarea property="keywords" rows="4" />
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
