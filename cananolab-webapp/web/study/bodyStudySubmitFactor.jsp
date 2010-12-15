<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<td class="cellLabel" width="15%">
			Factor Type *
		</td>
		<td width="20%">
			<html:select property="studyBean.factorType">
				<option value="" />
				<option value="other">
					[other]
				</option>
			</html:select>
		</td>
		<td class="cellLabel" width="15%">
			Factor Name *
		</td>
		<td>
			<html:text property="studyBean.factorName" size="60" />
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<table width="100%">
				<tr>
					<td>
						<div align="right">
							<input type="button" value="Save" onclick="addFactor('study')" />
							<input type="button" value="Cancel"
								onclick="clearFactor();closeSubmissionForm('Factor');" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>