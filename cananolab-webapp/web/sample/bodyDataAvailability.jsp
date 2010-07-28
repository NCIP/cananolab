<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/sample/bodyHideSearchDetailView.jsp"%>
<link rel="StyleSheet" type="text/css" href="css/main.css">
<link rel="StyleSheet" type="text/css" href="css/summaryView.css">

<table class="gridtable" width="95%" align="center">
<tr><th class="editTableWithGrid" align="center" ><b>caNanoLab Availability Score: ${sampleBean.caNanoLabScore}</b><br/>
		<b>MINChar Availability Score: ${sampleBean.mincharScore}</b><br/></th></tr>
<tr><td><table class="gridtable" width="100%" align="center">
	<tr>
		<td width="40%" class="cellLabel" align="center">
			caNanoLab
		</td>
		<td width="30%" class="cellLabel" align="center">
			MINChar
		</td>
		<td width="30%"class="cellLabel" align="center">
			${sampleBean.domain.name}
		</td>
	</tr>
	<tr>
		<td width="40%"></td>
		<td width="30%">agglomeration and/or aggregation </td>
		<td width="30%"></td>
	</tr>
	<tr>
		<td width="40%"></td>
		<td width="30%">crystal structure/crystallinity</td>
		<td width="30%"></td>
	</tr>	
	<tr>
		<td width="70%" colspan="2">
			General Sample Information
		</td>
		<td width="30%" align="center">
			<img src="images/icon_check.png" />
		</td>
	</tr>
	<tr>
		<td width="40%">
			Sample Composition
		</td>
		<td width="30%">
			chemical composition
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq 'sample composition'}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	<tr>
		<td width="40%">
			&nbsp;&nbsp;nanomaterial entities
		</td>		
		<td width="30%">			
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq 'nanomaterial entities'}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	<tr>
		<td width="40%">
			&nbsp;&nbsp;functionalizing entities
		</td>		
		<td width="30%" >			
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq 'functionalizing entities'}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	<tr>		
		<td width="40%" >
			&nbsp;&nbsp;chemical associations
		</td>		
		<td width="30%">			
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq 'chemical associations'}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	<c:forEach var="chem" items="${chemicalAssocs}">
	<tr>		
		<td width="40%">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${chem}
		</td>		
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${nano eq chem}">${minchar}</c:if>
			</c:forEach>					
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq chem}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	</c:forEach>
	<tr>
		<td width="40%">
			&nbsp;&nbsp;sample function
		</td>		
		<td width="30%">
			
		</td>
		<td width="30%" align="center">
		<c:forEach var="data" items="${availableEntityNames}">
		<c:if test="${data eq 'sample function'}" >
		<img src="images/icon_check.png" /></c:if>
		</c:forEach>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			Physico-Chemical Characterization
		</td>
	</tr>
	
	<c:forEach var="char" items="${physicoChars}">
	<c:choose>
	<c:when test="${char eq 'surface'}">
		<tr>
			<td width="40">
				&nbsp;&nbsp;${char}
			</td>
			<td width="30" ></td>
			<td width="30%" align="center">
				<c:forEach var="data" items="${availableEntityNames}">
					<c:if test="${data eq char}">
						<img src="images/icon_check.png" />
					</c:if>									
				</c:forEach>
			</td>
		</tr>
		<tr><td width="40%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;surface area</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${nano eq 'surface area '}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">
				<c:if test="${data eq 'surface area'}"> 
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>
		<tr><td width="40%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;surface charge</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${'surface charge' == nano}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">			
				<c:if test="${data == 'surface charge'}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zeta potential</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${'zeta potential' == nano}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">			
				<c:if test="${data == 'zeta potential'}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>
	</c:when>
		<c:otherwise>
		<tr>
		<td width="40%">&nbsp;&nbsp;${char}</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${char == nano}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">
				<c:if test="${data == char}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>			
		</c:otherwise>
		</c:choose>
	</c:forEach>	
	
	<tr>
		<td colspan="3">
			In Vitro Characterization
		</td>
	</tr>
	<c:forEach var="char" items="${invitroChars}">
		<tr>
		<td width="40%">&nbsp;&nbsp;${char}</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${char == nano}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">
				<c:if test="${data == char}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>		
	</c:forEach>
	<tr>
		<td colspan="3">
			In Vivo Characterization
		</td>
	</tr>
	<c:forEach var="char" items="${invivoChars}">
		<tr>
		<td width="40%">&nbsp;&nbsp;${char}</td>
		<td width="30%">
			<c:forEach var="nano2minchar" items="${caNano2MINChar}" >
		    		<c:set var="nano" value="${nano2minchar.key }"/>
					<c:set var="minchar" value="${nano2minchar.value}" />
					<c:if test="${char == nano}">${minchar}</c:if>
			</c:forEach>
		</td>
		<td  width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">
				<c:if test="${data == char}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>	
		</tr>	
	</c:forEach>	
	<tr>
		<td width="40%">
			Publications
		</td>
		<td width="30%"></td>
		<td width="30%" align="center">
			<c:forEach var="data" items="${availableEntityNames}">
				<c:if test="${data == 'publications'}">
					<img src="images/icon_check.png" />
				</c:if>			
		</c:forEach>
		</td>
	</tr>	
</table>
<c:if test="${!empty updateSample}">	
<table class="invisibleTable" width="95%" align="center">
	<tr><td width="650" align="left"><input type="button" value="Delete"
					onclick="javascript:deleteDataAvailability('Data Availability for the sample', sampleForm, 'sample', 'deleteDataAvailability');" />
		</td>
		<td colspan="2" width="600" align="right"> 
			<div>			
				<input type="button" value="Update"
					onclick="javascript:updateDataAvailability(sampleForm, 'sample', 'updateDataAvailability');" />
				<input type="button" value="Cancel"
					onclick="javascript:hide('dataAvailability');show('editDataAvailability');" />	
			</div>		
		</td>				
	</tr>
</table>
</c:if>
</td>
</tr>
</table>