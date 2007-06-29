<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<bean:define id="thisForm" name="${param.formName}" />
<table width="100%" border="0" align="center" cellpadding="3"
	cellspacing="0" class="topBorderOnly" summary="">
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Detail Information
			</div>
		</td>
	</tr>
	<tr>
		<td class="leftLabel">
			<strong>Cell Line</strong>
		</td>
		<c:choose>
			<c:when test="${canUserSubmit eq 'true'}">
				<td class="rightLabel">
					<html:select property="achar.cellLine"
						onkeydown="javascript:fnKeyDownHandler(this, event);"
						onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
						onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
						onchange="fnChangeHandler_A(this, event);">
						<option value="">
							--?--
						</option>
						<html:options name="allCellLines" />
					</html:select>
				</td>
			</c:when>
			<c:otherwise>
				<td class="rightLabel" colspan="3">
					${thisForm.map.achar.cellLine}&nbsp;
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
<br>


