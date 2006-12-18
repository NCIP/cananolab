<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="searchNanoparticleAdvanced">
	<table align="center" width="100%">
		<tr>
			<td>
				<h3>
					<br>
					Advanced Nanoparticle Search
				</h3>
			</td>
			<td align="right" width="25%">
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=search_nanoparticle')" class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />

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
								<strong>Particle Type*</strong>
							</td>
							<td class="rightLabel" colspan="3">
								<c:choose>
									<c:when test="${param.dispatch eq 'setupUpdate'}">
										${nanoparticleGeneralInfoForm.map.particleType}
										<html:hidden property="particleType" />
									</c:when>
									<c:otherwise>
										<html:select property="particleType" onchange="javascript:doubleDropdown(document.nanoparticleGeneralInfoForm.particleType, document.nanoparticleGeneralInfoForm.particleName, particleTypeParticles)">
											<option value=""></option>
											<html:options name="allSampleTypes" />
										</html:select>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong> Function Type </strong>
							</td>
							<td class="rightLabel" colspan="3">
								<strong> <html:select property="functionTypes" multiple="true" size="4">
										<html:options name="allParticleFunctionTypes" />
									</html:select></strong>
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									Physical Characterization
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Z-Average Size (nm) </strong>
							</td>
							<td class="rightLabel">
								<html:text property="size1.lowValue" size="5" />
								<strong>&nbsp;to&nbsp;</strong>
								<html:text property="size1.highValue"  size="5" />
								<html:hidden property="size1.classification" value="Size" />
								<html:hidden property="size1.type" value="Z-Average" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>PDI for Size</strong>
							</td>
							<td class="rightLabel">
								<html:text property="size2.lowValue"  size="5" />
								<strong>&nbsp;to&nbsp;</strong>
								<html:text property="size2.highValue"  size="5" />
								<html:hidden property="size2.classification" value="Size" />
								<html:hidden property="size2.type" value="PDI" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Molecular Weight (kDa)</strong>
							</td>
							<td class="rightLabel">
								<html:text property="molecularWeight.lowValue"  size="5" />
								<strong>&nbsp;to&nbsp;</strong>
								<html:text property="molecularWeight.highValue"  size="5" />
								<html:hidden property="molecularWeight.classification" value="Molecular Weight" />
								<html:hidden property="molecularWeight.type" value="Molecular Weight" />
							</td>
						</tr>
					</tbody>
				</table>
				<br>
				<table class="topBorderOnly" cellspacing="0" cellpadding="3" width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="2">
								<div align="justify">
									In Vitro Characterization
								</div>
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>Percent Cell Viability</strong>
							</td>
							<td class="rightLabel">
								<html:text property="cellViability.lowValue"  size="5" />
								<strong>&nbsp;to&nbsp;</strong>
								<html:text property="cellViability.highValue"  size="5"/>
								<html:hidden property="cellViability.classification" value="Cell Viability" />
								<html:hidden property="cellViability.type" value="Percent Cell Viability" />
							</td>
						</tr>
					</tbody>
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
<script language="JavaScript">
<!--//

/* populate a hashtable containing characterization type characterizations */
  var charTypeChars=new Array();    
  <c:forEach var="item" items="${allCharacterizationTypeCharacterizations}">  
    var chars=new Array();    
   <c:forEach var="char" items="${allCharacterizationTypeCharacterizations[item.key]}" varStatus="count">
  		chars[${count.index}]='${char}'; 
    </c:forEach>
    charTypeChars['${item.key}']=chars;
  </c:forEach>
//-->
</script>
<!--_____ main content ends _____-->
