<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${disableOuterButtons eq 'true'}">
	<c:set var="disableButtonStr" value="disabled" />
</c:if>
<table width="100%" class="invisibleTable">
	<tr>
		<td align="left" width="600">
			<c:if test="${showDelete eq 'true'}">
				<input type="button" value="${deleteButtonName}"
					onclick="${deleteOnclick}" id="deleteButton" ${disableButtonStr} >
			</c:if>
			<c:if test="${!empty updateId && !empty cloneOnclick }">
				<input type="button" value="Copy" onclick="${cloneOnclick}"
					id="copyButton" ${disableButtonStr} >
			</c:if>
		</td>
		<td align="right" width="300">
			<c:set var="submitButtonName" value="Submit" />
			<c:if test="${!empty updateId}">
				<c:set var="submitButtonName" value="Update" />
			</c:if>
			<input type="reset" value="Reset" onclick="${resetOnclick}"
				id="resetButton"/>
			&nbsp;
			<c:if test="${!empty review && review eq 'true'}">
				<input type="button" value="Submit for Review" id="reviewButton"
					onclick="${submitForReviewOnclick}" ${disableButtonStr}/>
			</c:if>
			<c:choose>
				<c:when test="${!empty submitOnclick }">
					<input type="button" value="${submitButtonName}"
						onclick="${submitOnclick}" styleId="submitButton" ${disableButtonStr}>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${validate eq 'true'}">
							<html:submit value="${submitButtonName}" styleId="submitButton"
								disabled="${disableOuterButtons}"
								onclick="javascript:return confirmPublicDataUpdate();" />
						</c:when>
						<c:otherwise>
							<html:submit value="${submitButtonName}" styleId="submitButton"
								disabled="${disableOuterButtons}" />
						</c:otherwise>
					</c:choose>
					<input type="hidden" name="dispatch" value="${hiddenDispatch}">
					<input type="hidden" name="page" value="${hiddenPage}">
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
