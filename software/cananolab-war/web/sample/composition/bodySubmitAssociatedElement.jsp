<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/CompositionManager.js"></script>
<script type='text/javascript'
	src='/caNanoLab/dwr/interface/CompositionManager.js'></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<table>
	<tr>
		<td class="cellLabel" width="40%">
			Element*
		</td>
	</tr>
	<tr>
		<td>
			<html:select styleId="compositionType${elementNumber}"
				property="assoc.associatedElement${elementNumber}.compositionType"
				onchange="getAssociatedElementOptions('${elementNumber}');">
				<option value=""></option>
				<html:options name="associationCompositionTypes" />
			</html:select>
			<div id="entitySelect${elementNumber}" style="${entitySelectStyle}">
				<br>
				<html:select styleId="entityId${elementNumber}"
					property="assoc.associatedElement${elementNumber}.entityId"
					onchange="getComposingElementOptions('${elementNumber}');">
					<option value=""></option>
					<c:if test="${!empty entityList}">
						<html:options collection="entityList" property="domainId"
							labelProperty="displayName" />
					</c:if>
				</html:select>
			</div>
			<div id="composingElementSelect${elementNumber}" style="${composingElementSelectStyle}">
				<br>
				<html:select styleId="composingElementId${elementNumber}"
					property="assoc.associatedElement${elementNumber}.composingElement.id">
					<option value="" />
						<c:if test="${!empty ceList}">
							<html:options collection="ceList" property="domainId"
								labelProperty="displayName" />
						</c:if>
				</html:select>
				<html:hidden styleId="entityDisplay${elementNumber}"
					property="assoc.associatedElement${elementNumber}.entityDisplayName" />
			</div>
		</td>
	</tr>
</table>
