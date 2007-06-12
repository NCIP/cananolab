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
					Shape Property
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Type *</strong>
			</td>
			<td class="rightLabel" colspan="3">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:select property="shape.type"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event);">
							<option value="">
								--?--
							</option>
							<html:options name="allShapeTypes" />
						</html:select>
					</c:when>
					<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.type}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Minimum Dimension </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="shape.minDimension" />
										${nanoparticleCharacterizationForm.map.shape.minDimensionUnit}&nbsp;
									</c:when>
					<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.minDimension}&nbsp;
										${nanoparticleCharacterizationForm.map.shape.minDimensionUnit}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Maximum Dimension </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canUserSubmit eq 'true'}">
						<html:text property="shape.maxDimension" />
										${nanoparticleCharacterizationForm.map.shape.maxDimensionUnit}&nbsp;
									</c:when>
					<c:otherwise>
										${nanoparticleCharacterizationForm.map.shape.maxDimension}&nbsp;
										${nanoparticleCharacterizationForm.map.shape.maxDimensionUnit}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br />
