<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:form action="searchNanoparticle">
	<table align="center" width="100%">
		<tr>
			<td>
				<h3>
					<br>
					Search Nanoparticle
				</h3>
			</td>
			<td align="right" width="25%">
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				<a href="javascript:openHelpWindow('webHelp/caLAB_0.5/index.html?single=true&amp;context=caLAB_0.5&amp;topic=search_nanoparticle')" class="helpText">Help</a>
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
										<html:options name="allSampleTypes" />
								</html:select></strong>
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
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Characterization Type </strong>
						</td>
						<td class="label">
							<a href="#" onclick="javascript:dynamicDropdown('physical', document.searchNanoparticleForm.characterizations, charTypeChars)">Physical Characterization</a>
							<br>
							In Vitro Characterization
							<br>
							<span class="indented"> <a href="#" onclick="javascript:dynamicDropdown('toxicity', document.searchNanoparticleForm.characterizations, charTypeChars)">Toxicity</a> <br> <span class="indented2"> <a href="#"
									onclick="javascript:dynamicDropdown('cytoTox', document.searchNanoparticleForm.characterizations, charTypeChars)">Cytotoxicity</a> </span> <br> <span class="indented2">Immunotoxicity</span> <br> <span class="indented3"><a href="#"
									onclick="javascript:dynamicDropdown('bloodContactTox', document.searchNanoparticleForm.characterizations, charTypeChars)">Blood Contact </a></span> <br> <span class="indented3"><a href="#"
									onclick="javascript:dynamicDropdown('immuneCellFuncTox', document.searchNanoparticleForm.characterizations, charTypeChars)">Immune Cell Function </a></span> 									
						</td>
						<td class="label" valign="top">
							<strong> Characterization </strong>
						</td>
						<td class="rightLabel" valign="top">
							<strong> <html:select property="characterizations" multiple="true" size="4">
									<c:forEach var="char" items="${searchNanoparticleForm.map.characterizations}">
										<option value="${char}" selected>
											${char}
										</option>
									</c:forEach>
								</html:select></strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Keywords <em>(one per line)</em> </strong>
						</td>
						<td class="label">
							<html:textarea property="keywords" rows="4" />
						</td>
						<td class="rightLabel" colspan="2">
							<strong>for<br> <html:radio property="keywordType" value="nanoparticle">Nanoparticle</html:radio> <br> <html:radio property="keywordType" value="assayResult">Assay Result</html:radio> </strong>
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
