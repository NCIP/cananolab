<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript"
	src="javascript/CharacterizationManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/CharacterizationManager.js"></script>

<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Sample Range Search" />
	<jsp:param name="topic" value="search_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="rangeSearchSample">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th colspan="2">
				Composition
			</th>
		</tr>
		<tr>
			<td class="cellLabel" width="20%">
				Nanomaterial Entity
			</td>
			<td>
				<html:select styleId="nanomaterialEntityTypes"
					property="nanomaterialEntityTypes" multiple="true" size="4">
					<html:options name="nanomaterialEntityTypes" />
				</html:select>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th colspan="3">
				Physico-Chemical Characterization
			</th>
		</tr>
		<tr>
			<td class="cellLabel" colspan="3">
				Size
			</td>
		</tr>
		<tr>
			<td width="5%">
			</td>
			<td width="15%">
				Z-Average
			</td>
			<td>
				from
				<html:text property="sizeZAverageLow.value" size="10"/>
				&nbsp;
				<html:select property="sizeZAverageLow.valueUnit">
					<option></option>
					<html:options name="zAverageUnits" />
				</html:select>
				&nbsp;to&nbsp;
				<html:text property="sizeZAverageHigh.value" size="10"/>
				&nbsp;
				<html:select property="sizeZAverageLow.valueUnit">
					<option></option>
					<html:options name="zAverageUnits" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				PDI
			</td>
			<td>
				from
				<html:text property="sizePdiLow.value" size="10"/>
				&nbsp;&nbsp;to&nbsp;
				<html:text property="sizePdiHigh.value" size="10"/>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="3">
				Molecular Weight
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				Molecular Weight
			</td>
			<td>
				from
				<html:text property="molecularWeightLow.value" size="10"/>
				&nbsp;
				<html:select property="molecularWeightLow.valueUnit">
					<option></option>
					<html:options name="molecularWeightUnits" />
				</html:select>
				&nbsp;to&nbsp;
				<html:text property="molecularWeightHigh.value" size="10"/>
				&nbsp;
				<html:select property="molecularWeightHigh.valueUnit">
					<option></option>
					<html:options name="molecularWeightUnits" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="cellLabel" colspan="3">
				Surface
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				Zeta Potential
			</td>
			<td>
				from
				<html:text property="surfaceZetaPotentialLow.value" size="10"/>
				&nbsp;
				<html:select property="surfaceZetaPotentialLow.valueUnit">
					<option></option>
					<html:options name="zetaPotentialUnits" />
				</html:select>
				&nbsp;to&nbsp;
				<html:text property="surfaceZetaPotentialHigh.value" size="10"/>
				&nbsp;
				<html:select property="surfaceZetaPotentialHigh.valueUnit">
					<option></option>
					<html:options name="zetaPotentialUnits" />
				</html:select>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th colspan="3">
				In Vitro Characterization
			</th>
		</tr>
		<tr>
			<td class="cellLabel" colspan="3">
				Cell Viability
			</td>
		</tr>
		<tr>
			<td width="5%">
			</td>
			<td width="15%">
				LC50
			</td>
			<td>
				from
				<html:text property="cellViabilityLC50Low.value" size="10"/>
				&nbsp;
				<html:select property="cellViabilityLC50Low.valueUnit">
					<option></option>
					<html:options name="lc50Units" />
				</html:select>
				&nbsp;to&nbsp;
				<html:text property="cellViabilityLC50High.value" size="10"/>
				&nbsp;
				<html:select property="cellViabilityLC50Low.valueUnit">
					<option></option>
					<html:options name="lc50Units" />
				</html:select>
			</td>
		</tr>
	</table>
	<br />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<td class="cellLabel" colspan="3">
				Logical Operator
			</td>
		</tr>
		<tr>
			<td width="5%"></td>
			<td colspan="2">
				and
				<html:radio property="joinType" value="and" />
				&nbsp; or
				<html:radio property="joinType" value="or" />
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="0" align="center" cellpadding="3"
		cellspacing="0">
		<tr>
			<td>
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="button" value="Reset">
								<input type="hidden" name="dispatch" value="search">
								<input type="hidden" name="page" value="1">
								<%--<html:submit value="Search" />--%>
								<input type="button" value="Submit">
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>