<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
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
	<jsp:param name="pageTitle" value="Advanced Sample Search" />
	<jsp:param name="topic" value="search_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/advancedSampleSearch" enctype="multipart/form-data">
	<jsp:include page="/bodyMessage.jsp?bundle=sample" />
	<table width="100%" align="center" class="submissionView">
		<tr>
			<th>
				Composition Criteria
			</th>
		</tr>
		<tr>
			<td>
				<table id="compositionQueryTable" class="summaryViewLayer4"
					width="85%" style="display: none;">
					<tbody id="compositionQueryRows">
						<tr id="patternHeader">
							<td width="25%" class="cellLabel">
								Nanomaterial or Functionalizing Entity
							</td>
							<td width="25%" class="cellLabel">
								Entity Type
							</td>
							<td class="cellLabel">
								Chemical Name
							</td>
						</tr>
						<tr id="pattern" style="display: none;">
							<td>
								<span id="compTypeValue">Composition Type</span>
							</td>
							<td>
								<span id="entityTypeValue">Entity Type</span>
							</td>
							<td>
								<span id="chemicalNameValue">Chemical Name</span>
							</td>
							<td>
								<input class="noBorderButton" id="edit" type="button"
									value="Edit" />
							</td>
						</tr>
					</tbody>
				</table>
				<table id="newCompositionQuery" style="display: block;"
					class="promptbox">
					<tr>
						<td>
							<html:hidden property="searchBean.theCompositionQuery.id"
								styleId="compQueryId" />
							<html:select
								property="searchBean.theCompositionQuery.compositionType"
								styleId="compType" onchange="javascript:updateCompositionEntityDropdowns();">
								<option value="">
									-- Please Select --
								</option>
								<option value="nanomaterial entity">
									nanomaterial entity
								</option>
								<option value="functionalizing entity">
									functionalizing entity
								</option>
							</html:select>
						</td>
						<td>
							<html:select property="searchBean.theCompositionQuery.entityType"
								styleId="entityType">
								<option value="">
									-- Please Select --
								</option>
							</html:select>
						</td>
						<td class="cellLabel">
							Chemical Name
						</td>
						<td>
							<html:select property="searchBean.theCompositionQuery.operand"
								styleId="operand">
								<option value="equalsTo" />
									equals to
								</option>
								<option value="contains" />
									contains
								</option>
								<option value="startsWith" />
									starts with
								</option>
								<option value="endsWith" />
									ends with
								</option>
							</html:select>
						</td>
						<td>
							<html:text property="searchBean.theCompositionQuery.chemicalName"
								styleId="chemicalName" size="50" />
						</td>
						<td>
							<table cellspacing="0">
								<tr>
									<td>
										<input class="promptButton" type="button" value="Add"
											onclick="addCompositionQuery();show('compositionQueryTable');closeSubmissionForm('CompositionQuery');" />
									</td>
									<td>
										<input class="promptButton" type="button" value="Cancel"
											onclick="clearCompositionQuery();closeSubmissionForm('CompositionQuery');" />
									</td>
									<td>
										<input style="display: none;" class="promptButton"
											id="deleteCompositionQuery" type="button" value="Remove"
											onclick="deleteTheCompositionQuery()" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<div id="compositionLogicalOperator" style="display: none"
					<html:radio property="searchBean.compositionLogicalOperator"
					value="and" />
				&nbsp; AND
					<html:radio property="searchBean.compositionLogicalOperator"
					value="or" />
					OR</div>
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
								<input type="button" value="Search">
							</div>
						</td>
					</tr>
				</table>
				<div align="right"></div>
			</td>
		</tr>
	</table>
</html:form>
