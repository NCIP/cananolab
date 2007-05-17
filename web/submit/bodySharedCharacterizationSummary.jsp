<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<bean:define id="thisForm" name="${param.formName}"/>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
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
				<c:when test="${canUserSubmit eq 'true'}">
					<html:select property="achar.characterizationSource">
						<html:options name="characterizationSources" />
					</html:select>
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.characterizationSource}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>View Title*</strong><br>
			<em>(text will be truncated after 20 characters)</em>
		</td>
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:text property="achar.viewTitle" size="30"/>
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.viewTitle}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Protocol Name </strong>
		</td>
		<td class="label" colspan="3">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<%-- html:text property="achar.characterizationProtocol.name" / 
					<html:select styleId="protocolIds" property="achar.protocolFileBean.protocolBean.id"
						onchange="javascript:filterProtocolVersions();"> --%>
					<html:select property="achar.protocolFileBean.id">
						<%-- 
						<html:option value=""></html:option>
						<html:options name="AllProtocolNameByType" />
						--%>
						<html:optionsCollection name="AllProtocolNameVersionsByType" label="label" value="value"/>
					</html:select>
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.protocolFileBean.protocolBean.name}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<%--
		<td class="label">
			<strong>Protocol Version</strong>
		</td>
		--%>
		<%-- html:options name="AllProtocolNameByType" / --%>
		<%--
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:select styleId="protocolVersions" property="achar.protocolFileBean.version">
						<option/>
					</html:select>
				</c:when>
				<c:otherwise>
				<html:text property="achar.protocolFileBean.version" />
						${thisForm.map.achar.protocolFileBean.version}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		--%>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:textarea property="achar.description" rows="3" cols="80" />
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.description}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>
<%--
<script language="JavaScript">
<!--//
  /* populate a hashtable containing sampleName aliquots */
  var versionNames=new Array();    
  <c:forEach var="item" items="${AllProtocolNameVersionsByType}">
    var versions=new Array();
    <c:forEach var="version" items="${AllProtocolNameVersionsByType[item.key]}" varStatus="count">
  	    versions[${count.index}]='${version}';  	
    </c:forEach>
    versionNames['${item.key}']=versions;
  </c:forEach>  
  
  function filterProtocolVersions() {
  	var name = document.getElementById("protocolIds");
  	var version = document.getElementById("protocolVersions");
  	if (!name[1].checked) {
  	   doubleDropdown(name, version, versionNames);	
  	}
  }
//-->
</script>
--%>
