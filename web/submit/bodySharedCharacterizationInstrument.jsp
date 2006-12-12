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
				Instrument Information 
			</div>
		</td>
	</tr>	
	<tr>
		<td class="leftLabel">
			<strong>Instrument Type </strong>
		</td>
		<td class="label">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:select property="achar.instrument.type" onchange="javascript:doubleDropdownWithExraOption(this.form.elements[5], this.form.elements[7], instrumentTypeManufacturers, 'Other')">
						<option value="" />
							<html:options name="allInstrumentTypes" />				
					</html:select>
				</c:when>
				<c:otherwise>
					${thisForm.map.achar.instrument.type}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>Other Instrument Type </strong>
			
		</td>
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:text property="achar.instrument.otherInstrumentType" />
				</c:when>
				<c:otherwise>
					${thisForm.map.achar.instrument.otherInstrumentType}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="LeftLabel">
			<strong>Instrument Manufacturer </strong>
		</td>
		<td class="label">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:select property="achar.instrument.manufacturer">
						<option value="${thisForm.map.achar.instrument.manufacturer}" selected>
							${thisForm.map.achar.instrument.manufacturer}
						</option>
					</html:select>
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.instrument.manufacturer}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>Other Manufacturer </strong>
		</td>
		<td class="rightLabel">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:text property="achar.instrument.otherManufacturer" />
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.instrument.otherManufacturer}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<strong>Description</strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canUserUpdateParticle eq 'true'}">
					<html:textarea property="achar.instrument.description" rows="3" />
				</c:when>
				<c:otherwise>
						${thisForm.map.achar.instrument.description}&nbsp;
					</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<br>

<script language="JavaScript">
<!--//
  /* populate a hashtable containing instrument type manufacturers */
  var instrumentTypeManufacturers=new Array();    
  <c:forEach var="item" items="${allInstrumentTypeManufacturers}">  	
    var manufacturers=new Array();
    <c:forEach var="manufacturer" items="${allInstrumentTypeManufacturers[item.key]}" varStatus="count">
  		manufacturers[${count.index}]='${manufacturer}';  	  		
    </c:forEach>
    instrumentTypeManufacturers['${item.key}'] = manufacturers;
  </c:forEach> 
//-->
</script>

