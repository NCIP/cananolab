<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<table width="100%" align="center" class="submissionView">
	<tr>
		<td class="cellLabel">
			Data Pending Review
		</td>
	</tr>
	<c:if test="${!empty dataPendingReview}">
		<tr>
			<td>
				<table class="editTableWithGrid" width="95%" align="center">
					<tr>
						<th>
							Data Type
						</th>
						<th>
							Data ID
						</th>
						<th></th>
					</tr>
					<c:forEach var="data" items="dataPendingReview">
						<tr valign="top">
							<td>
								${data.dataType}
							</td>
							<td>
								${data.dataName}
							</td>
							<td align="right">
								<a href="${data.reviewLink}">Edit</a>&nbsp;
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:if>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
</table>