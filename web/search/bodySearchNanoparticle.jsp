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
					Search Nanoparticles
				</h3>
			</td>
			<td align="right" width="25%">
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=search_nano_help')"
					class="helpText">Help</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=search" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
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
								</html:select> </strong>
						</td>
						<td class="label">
							<strong> Particle Type </strong>
						</td>
						<td class="rightLabel">
							<strong> <html:select property="particleType">
									<option value="" />
										<html:options name="allSampleTypes" />
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Function Type </strong>
						</td>
						<td class="rightLabel" colspan="3">
							<strong> <html:select property="functionTypes"
									multiple="true" size="4">
									<html:options name="allFunctionTypes" />
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Characterization Type </strong>							
						</td>						
						<td class="label">
							<c:forEach var="charType" items="${allCharacterizationTypes}">
								<c:choose>
									<c:when test="${charType.hasAction}">
										<span class="indented${charType.indentLevel}"><a href="#"
											onclick="javascript:dynamicDropdown('${charType.type}', document.searchNanoparticleForm.characterizations, charTypeChars); setHiddenCharType('${charType.type}')">${charType.type}
										</a></span>
									</c:when>
									<c:otherwise>
										<span class="indented${charType.indentLevel}">${charType.type}</span>
									</c:otherwise>
								</c:choose>
								<br>
							</c:forEach>
							<html:hidden styleId="characterizationType" property="characterizationType"/>
						</td>

						<td class="label" valign="top">
							<strong> Characterization </strong>
						</td>
						<td class="rightLabel" valign="top">
							<strong> <html:select property="characterizations"
									multiple="true" size="4">
									<c:forEach var="char"
										items="${allCharTypeChars[searchNanoparticleForm.map.characterizationType]}">
										<html:option value="${char}">${char}</html:option>
									</c:forEach>
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong> Summary/Description <em>(one per line)</em> </strong>
						</td>
						<td class="label">
							<html:textarea property="summaries" rows="4" />
						</td>
						<td class="rightLabel" colspan="2">
							<strong>for<br> <html:radio property="summaryType"
									value="characterization">Nanoparticle</html:radio> <br> <html:radio
									property="summaryType" value="assayResult">Characterization File</html:radio>
							</strong>
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
							<strong>for<br> <html:radio property="keywordType"
									value="nanoparticle">Nanoparticle</html:radio> <br> <html:radio
									property="keywordType" value="assayResult">Characterization File</html:radio>
							</strong>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td>
							<span class="formMessage"> </span>
							<br>
							<table border="0" align="right" cellpadding="4" cellspacing="0">
								<tr>
									<td>
										<div align="right">
											<input type="button" value="Reset"
												onClick="javascript:location.href='searchNanoparticle.do?dispatch=setup&page=0'">
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
  <c:forEach var="item" items="${allCharTypeChars}">  
    var chars=new Array();    
   <c:forEach var="char" items="${allCharTypeChars[item.key]}" varStatus="count">
  		chars[${count.index}]='${char}'; 
    </c:forEach>
    charTypeChars['${item.key}']=chars;
  </c:forEach>
  
  function setHiddenCharType(charType) { 
     document.getElementById("characterizationType").value=charType;          
  }
//-->
</script>
<!--_____ main content ends _____-->
