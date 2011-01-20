<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<div style="position: relative">
	<table class="promptbox" id="termBrowser${ind}"
		style="display: none; position: absolute; z-index: 10; left: -50px; top: -10px;">
		<tr>
			<td></td>
			<td></td>
			<td align="right">
				<img src="images/delete.gif" onclick="hide('termBrowser${ind}');">
			</td>
		</tr>
		<tr>
			<td>
				<input type="text" id="searchStr${ind}" size="40">
			</td>
			<td>
				<input type="button" value="search" onclick="${searchFunction}"
					id="searchButton${ind}">
			</td>
			<td></td>
		</tr>
		<tr>
			<td width="90%" colspan="2">
				<input type="radio" name="algorithm${ind}" value="exact match"
					checked/>
				exact match &nbsp;&nbsp;
				<input type="radio" name="algorithm${ind}" value="starts with" />
				starts with
				<input type="radio" name="algorithm${ind}" value="contains" />
				contains
			</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<img src="images/ajax-loader.gif" border="0" class="counts"
					id="loaderImg${ind}" style="display: none">
				<select multiple size="5" id="matchedTermSelect${ind}"
					style="display: none">
				</select>
				<span id="searchMessage${ind}" style="display: none">No
					matches found.</span>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="add" onclick="${addFunction}"
					style="display: none" id="addButton${ind}">
			</td>
			<td></td>
			<td></td>
		</tr>
	</table>
</div>
