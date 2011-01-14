<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<div style="position: relative">
	<table class="promptbox" id="termBrowser"
		style="display: none; position: absolute; z-index: 10; left: -50px; top: -10px;">
		<tr>
			<td></td>
			<td></td>
			<td align="right">
				<img src="images/delete.gif" onclick="hide('termBrowser');">
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" id="searchStr" size="40">
			</td>
			<td>
				<input type="button" value="search" onclick="${searchFunction}">
			</td>
			<td></td>
		</tr>
		<tr>
			<td>
				<input type="radio" />
				exact match &nbsp;&nbsp;
				<input type="radio" />
				contains
			</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<img src="images/ajax-loader.gif" border="0" class="counts"
					id="loaderImg" style="display: none">
				<select multiple size="5" id="matchedTermSelect"
					style="display: none">
				</select>
				<span id="searchMessage" style="display: none">No matches
					found.</span>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="add" onclick="${addFunction}"
					style="display: none" id="addButton">
			</td>
			<td></td>
			<td></td>
		</tr>
	</table>
</div>
