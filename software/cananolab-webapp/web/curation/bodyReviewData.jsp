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
				<tr valign="top">
					<td>
						Sample
					</td>
					<td>
						NCL-23
					</td>
					<td align="right">
						<a href="sample.do?dispatch=summaryEdit&page=0&sampleId=20917507">Edit</a>&nbsp;
					</td>
				</tr>
				<tr valign="top">
					<td>
						Publication
					</td>
					<td>
						publication title
					</td>
					<td align="right">
						<a href="publication.do?page=0&dispatch=setupUpdate&publicationId=23178496">Edit</a>&nbsp;
					</td>
				</tr>
				<tr valign="top">
					<td>
						Protocol
					</td>
					<td>
						Protocol Name
					</td>
					<td align="right">
						<a href="protocol.do?dispatch=setupUpdate&protocolId=24390913">Edit</a>&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table>