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
			<td align="right" width="30%">
				<%--<a href="advancedNanoparticleSearch.do" class="helpText">Advanced Search</a> &nbsp; &nbsp; --%>
				
				<jsp:include page="/webHelp/helpGlossary.jsp">
					<jsp:param name="topic" value="search_nano_help" />
					<jsp:param name="glossaryTopic" value="glossary_help" />
				</jsp:include>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=particle" />
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr>
						<td class="leftLabelWithTop" valign="top">
							<strong>Search Location</strong>
						</td>
						<td class="rightLabelWithTop" colspan="5">
							<strong><html:select property="searchLocations"
									multiple="true" size="4">
									<option value="local" selected>
										Local
									</option>
									<option value="caNanoLab-NCL_PROD">
										caNanoLab-NCL_PROD
									</option>
									<option value="caNanoLab-PROD">
										caNanoLab-PROD
									</option>
									<option value="caNanoLab-WUSTL">
										caNanoLab-WUSTL
									</option>
									<%--
									<c:forEach var="selectedNode" items="${selectedGridNodeHosts}">
										<option value="${selectedNode}" selected>
											${selectedNode}
										</option>
									</c:forEach>
									<c:forEach var="unselectedNode"
										items="${unselectedGridNodeHosts}">
										<option value="${unselectedNode}">
											${unselectedNode}
										</option>
									</c:forEach>
									--%>
								</html:select> </strong>
						</td>
					</tr>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle" colspan="6">
							<div align="justify">
								Basic Search
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Search by nanoparticle characterization keywords
								and text in characterization descriptions</strong>
							<br>
						</td>
						<td class="rightLabel" colspan="5">
							<html:textarea property="text" rows="3" cols="60" />
							<br>
							<em>case insensitive</em>
							<br>
							<em>words in quotes are searched together</em>
							<br>
						</td>
					</tr>
				</table>
				<br>

				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" summary="">
					<tr class="topBorder">
						<td class="formTitle" colspan="6">
							<div align="justify">
								Advanced Search
							</div>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" width="20%" valign="top">
							<strong> Nanoparticle Sample Source </strong>
						</td>
						<td class="rightLabel" colspan="5">
							<html:text property="particleSource" size="50" />
							<strong>*</strong> for searching wildcards
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Composition<br> Nanoparticle Entity</strong>
						</td>
						<td class="label">
							<strong><html:select property="nanoparticleEntityTypes"
									multiple="true" size="4">
									<html:options name="nanoparticleEntityTypes" />
								</html:select> </strong>
						</td>
						<td class="label" valign="top">
							<strong>Composition <br>Functionalizing Entity</strong>
						</td>
						<td class="label" valign="top">
							<strong><html:select
									property="functionalizingEntityTypes" multiple="true" size="3">
									<html:options name="functionalizingEntityTypes" />
								</html:select> </strong>
						</td>
						<td class="label" valign="top">
							<strong>Function</strong>
						</td>
						<td class="rightLabel" valign="top">
							<strong><html:select property="functionTypes"
									multiple="true" size="3">
									<html:options name="functionTypes" />
								</html:select> </strong>
						</td>
					</tr>
					<tr>
						<td class="leftLabel" valign="top">
							<strong>Characterization Type</strong>
							<html:hidden styleId="characterizationType"
								property="characterizationType" />
						</td>
						<td class="label">
							<c:forEach var="charType" items="${characterizationTypes}">
								<c:choose>
									<c:when test="${charType.key.hasGrandChildrenFlag eq false}">
										<span class="indented${charType.key.indentLevel}"><a
											href="#"
											onclick="javascript:dynamicDropdown('${charType.key.nodeName}', document.searchNanoparticleSampleForm.characterizations, charTypeChars); setHiddenCharType('${charType.key.nodeName}')">${charType.key.nodeName}
										</a> </span>
									</c:when>
									<c:otherwise>
										<span class="indented${charType.key.indentLevel}">${charType.key.nodeName}</span>
									</c:otherwise>
								</c:choose>
								<br>
							</c:forEach>
						</td>
						<td class="label" valign="top">
							<strong>Characterization</strong>
						</td>
						<td class="rightLabel" valign="top" colspan="3">
							<strong> <html:select property="characterizations"
									multiple="true" size="4">
									<c:forEach var="achar"
										items="${searchNanoparticleSampleForm.map.characterizations}">
										<html:option value="${achar}">${achar}</html:option>
									</c:forEach>
								</html:select> </strong>
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
											<input type="reset" value="Reset"
															onclick="javascript:location.reload()">
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
  <c:forEach var="item" items="${characterizationTypes}">  
    var chars=new Array();    
  
   <c:forEach var="achar" items="${characterizationTypes[item.key]}" varStatus="count">
  		chars[${count.index}]='${achar}'; 
    </c:forEach>
    charTypeChars['${item.key.nodeName}']=chars;
  </c:forEach>  
  
   function setHiddenCharType(charType) { 
     document.getElementById("characterizationType").value=charType;          
   }
  
//-->
</script>
<!--_____ main content ends _____-->
