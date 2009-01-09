<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type="text/javascript" src="javascript/POCManager.js"></script>

<html:form action="/submitExperimentConfig">
	<logic:iterate name="submitExperimentConfigForm"
		property="experimentConfigs" id="config">
		<table>
			<tr>
				<td>
					Technique
				</td>
				<td>
					${config.technique.type}
				</td>
				<td>
					Abbreviation
				</td>
				<td>
					<html:text property="" />
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<table>
						<tr>
							<td>
								Instrument Type
							</td>
							<td>
								Manufacturer
							</td>
							<td>
								Model Name
							</td>
						</tr>
						<logic:iterate name="config" property="instrumentCollection"
							id="instrument">
							<tr>
								<td>
									<html:select property="">
									</html:select>
								</td>
								<td>
									<html:select property="">
									</html:select>
								</td>
								<td>
									<html:text property=""></html:text>
								</td>
								<td>
									<a href="">Remove</a>
								</td>
							</tr>
						</logic:iterate>
						<tr>
							<td colspan="4">
								<a href="">Add Instrument</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td valign="top">
					Description
				</td>
				<td colspan="3">
					<html:textarea property="" />
				</td>
			</tr>
		</table>
		<br>
	</logic:iterate>
</html:form>