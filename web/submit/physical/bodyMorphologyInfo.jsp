<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="6">
				<div align="justify">
					Morphology Property
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Type *</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:select property="morphology.type"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event);">
							<option value="">
								--?--
							</option>
							<html:options name="allMorphologyTypes" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCharacterizationForm.map.morphology.type}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br />
