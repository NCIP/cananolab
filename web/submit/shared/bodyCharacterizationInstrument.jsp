<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/editableDropDown.js"></script>
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
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
				<c:when test="${canUserSubmit eq 'true'}">
					<html:select styleId="instrumentType"
						property="achar.instrumentConfigBean.instrumentBean.type"
						onkeydown="javascript:fnKeyDownHandler(this, event);"
						onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
						onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
						onchange="fnChangeHandler_A(this, event);doubleDropdownForTheEditable(document.getElementById('instrumentType'), document.getElementById('instrumentManufacturer'), instrumentTypeManufacturers); ">
						<option value="">
							--?--
						</option>
						<html:options name="allInstrumentTypes" />
					</html:select>
				</c:when>
				<c:otherwise>
					${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.type}&nbsp;
				</c:otherwise>
			</c:choose>
		</td>
		<td class="label">
			<strong>Instrument Type Abbreviation </strong>
		</td>
		<td class="rightLabel">
			${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.abbreviation}			
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Instrument Manufacturer </strong>
		</td>
		<td class="rightLabel" colspan="3">
			<c:choose>
				<c:when test="${canUserSubmit eq 'true'}">
					<html:select styleId="instrumentManufacturer"
						property="achar.instrumentConfigBean.instrumentBean.manufacturer"
						onkeydown="javascript:fnKeyDownHandler(this, event);"
						onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
						onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
						onchange="fnChangeHandler_A(this, event);">
						<option value="">
							--?--
						</option>
					</html:select>
				</c:when>
				<c:otherwise>
						${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.instrumentBean.manufacturer}&nbsp;
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
				<c:when test="${canUserSubmit eq 'true'}">
					<html:textarea property="achar.instrumentConfigBean.description"
						rows="3" cols="80" />
				</c:when>
				<c:otherwise> 
						${nanoparticleCharacterizationForm.map.achar.instrumentConfigBean.description}&nbsp; 
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
  <c:forEach var="item" items="${allInstrumentTypeToManufacturers}">  	
    var manufacturers=new Array();
    <c:forEach var="manufacturer" items="${allInstrumentTypeToManufacturers[item.key]}" varStatus="count">
  		manufacturers[${count.index}]='${manufacturer}';  	  		
    </c:forEach>
    instrumentTypeManufacturers['${item.key}'] = manufacturers;
  </c:forEach> 
//-->
</script>

