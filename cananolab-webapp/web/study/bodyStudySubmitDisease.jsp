<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="subSubmissionView" width="85%" align="center">
	<tr>
		<td class="cellLabel" width="15%">
			Disease Type
		</td>
		<td width="25%">
			<html:select property="studyBean.diseaseType">
				<option value=""></option>
				<option value="">
					cancer
				</option>
				<option value="other">
					[other]
				</option>
			</html:select>
		</td>
		<td class="cellLabel" width="20%">
				Disease Names
			</td>
			<td>
				<html:textarea property="studyBean.diseaseNames" cols="30" rows="2"
					value="lung cancer" />
				<em>one name per line</em>
			</td>
			<td>
				<table class="invisibleTable">
					<tr>
						<td>
							<a href="#sampleNameField" onclick="()"><img
									src="images/icon_browse.jpg" align="middle"
									alt="search EVS disease names" border="0" /> </a>
							<br>
							<em>browse EVS</em>
						</td>
						<td>
							<img src="images/ajax-loader.gif" border="0" class="counts"
								id="loaderImg" style="display: none">
						</td>
						<td>
							<select size="3" style="display: none">
								<option>
									EVS choices
								</option>
								<option>
									EVS choices
								</option>
							</select>
						</td>
						<td>
							<a href="#" id="selectMatchedSampleButton" style="display: none">select</a>
						</td>
					</tr>
				</table>
			</td>
	</tr>
	<tr>
		<td colspan="6">
			<table width="100%">
				<tr>
					<td>
						<div align="right">
							<input type="button" value="Save" onclick="addDisease('study')" />
							<input type="button" value="Cancel"
								onclick="closeSubmissionForm('Disease');" />
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>