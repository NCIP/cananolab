<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/main.css">

<table class="gridtable" width="95%" align="center">
<tr><th class="editTableWithGrid"><b>caNanoLab Availability Score: 67% (19 out of 29)</b>
		<b>&nbsp;&nbsp;&nbsp;MINChar Availability Score: 33% (3 out of 9)</b><br/></th></tr>
<tr><td><table class="gridtable" width="95%" align="center">
	<tr>
		<td class="cellLabel" align="center">
			caNanoLab
		</td>
		<td class="cellLabel" align="center">
			MINChar
		</td>
		<td class="cellLabel" align="center">
			<bean:write name="sampleForm" property="sampleBean.domain.name" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>agglomeration and/or aggregation </td>
		<td></td>
	</tr>
	<tr>
		<td></td>
		<td>crystal structure/crystallinity</td>
		<td></td>
	</tr>
	<tr>
		<td>
			General Sample Information
		</td>
		<td></td>
		<td align="center">
			<img src="images/icon_check.png" />
		</td>
	</tr>
	<tr>
		<td >
			Sample Composition
		</td>
		<td align="center">
			chemical composition
		</td>
		<td><img src="images/icon_check.png" /></td>
	</tr>
	<tr>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nanomaterial entities
		</td>		
		<td align="center">
			
		</td>
		<td></td>
	</tr>
	<tr>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;functionalizing entities
		</td>		
		<td align="center">
			
		</td>
		<td></td>
	</tr>
	<tr>		
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;chemical associations
		</td>		
		<td align="center">			
		</td>
		<td></td>
	</tr>
	<c:forEach var="chem" items="${chemicalAssocs}">
	<tr>
		
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${chem }
		</td>		
		<td align="center">
			<c:if test="${chem != 'encapsulation'}" >surface chemistry</c:if>			
		</td>
		<td></td>
	</tr>
	</c:forEach>
	<tr>
		<td>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sample function
		</td>		
		<td align="center">
			
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="3">
			Physico-Chemical Characterization
		</td>
	</tr>
	<c:forEach var="char" items="${physicoChars}">
		<c:if test="${char eq 'surface'}">
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${char}
			</td>
			<td ></td>
			<td></td>
		</tr>
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;surface area</td><td>surface area</td><td></td></tr>
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;surface charge</td><td>surface charge</td><td></td></tr>
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;zeta potential</td><td>surface charge</td><td></td>
		</c:if>
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${char}				
			</td>
			<td align="center">				
				<c:choose> 
				<c:when test="${char eq 'size'}">
					partical ${char}/size distribution
				</c:when>
				<c:when test="${char eq 'purity'}">
					${char}
				</c:when>
				<c:when test="${char eq 'shape'}">
					${char}
				</c:when>				
				</c:choose>
			</td>
			<td align="center">
				<c:if
					test="${char eq 'size' ||char eq 'shape' || char eq 'physical state' || char eq 'molecular weight' || char eq '[other]'}">
					<img src="images/icon_check.png" />
				</c:if>
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="3">
			In Vitro Characterization
		</td>
	</tr>
	<c:forEach var="char" items="${invitroChars}">
		<tr>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${char}
			</td>
			<td >				
			</td>
			<td align="center">
				<c:if
					test="${char eq 'cytotoxicity' || char eq 'blood contact' || char eq 'targeting'|| char eq 'sterility' || char eq '[other]'}">
					<img src="images/icon_check.png" />
				</c:if>
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
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${char}
			</td>
			<td align="center">				
			</td>
			<td align="center">
				<c:if
					test="${char eq 'pharmacokinetics' || char eq 'toxicology'}">
					<img src="images/icon_check.png" />
				</c:if>
			</td>
		</tr>
	</c:forEach>	
	<tr>
		<td>
			Publications
		</td>
		<td></td>
		<td align="center">
			<img src="images/icon_check.png" />
		</td>
	</tr>	
</table>
<table class="invisibleTable" width="95%" align="center">
	<tr><td width="650" align="left"><input type="button" value="Delete"
					onclick="javascript:deleteDataAvailability('Data Availability for the sample', sampleForm, 'sample', 'deleteDataAvailability');" />
		</td>
		<td colspan="2" width="600" align="right"> 
			<div>			
				<input type="button" value="Update"
					onclick="javascript:updateDataAvailability(sampleForm, 'sample', 'updateDataAvailability');" />
				<input type="button" value="Cancel"
					onclick="javascript:hide('dataAvailability');" />	
			</div>		
		</td>				
	</tr>
</table>
</td>
</tr>
</table>