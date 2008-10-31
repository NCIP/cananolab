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
				<strong>Type </strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:select property="achar.shape.type" styleId="shapeType"
							onchange="javascript:callPrompt('Type', 'shapeType');">
							<option value=""></option>
							<html:options name="shapeTypes" />
							<option value="other">
								[Other]
							</option>
						</html:select>
					</c:when>
					<c:otherwise>
										${characterizationForm.map.achar.shape.type}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Aspect Ratio </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:text property="achar.shape.aspectRatio"
							styleId="aspectRatio" />
					</c:when>
					<c:otherwise>
						${characterizationForm.map.achar.shape.aspectRatio}&nbsp;
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
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:text property="achar.shape.minDimension" />
										${characterizationForm.map.achar.shape.minDimensionUnit}&nbsp;
									</c:when>
					<c:otherwise>
										${characterizationForm.map.achar.shape.minDimension}&nbsp;
										${characterizationForm.map.achar.shape.minDimensionUnit}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Maximum Dimension </strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
						<html:text property="achar.shape.maxDimension" />
										${characterizationForm.map.achar.shape.maxDimensionUnit}&nbsp;
									</c:when>
					<c:otherwise>
										${characterizationForm.map.achar.shape.maxDimension}&nbsp;
										${characterizationForm.map.achar.shape.maxDimensionUnit}&nbsp;
									</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
</br>
