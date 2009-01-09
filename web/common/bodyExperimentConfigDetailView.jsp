<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<logic:iterate name="experimentConfigs" id="config">
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
				${config.technique.abbreviation}
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
								${instrument.type}
							</td>
							<td>
								${instrument.manufacturer}
							</td>
							<td>
								${instrument.modelName}
							</td>
						</tr>
					</logic:iterate>
				</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
				Description
			</td>
			<td colspan="3">
				${config.description}
			</td>
		</tr>
	</table>
	<br>
</logic:iterate>
