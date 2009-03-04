<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table border="0" align="center" cellpadding="3" cellspacing="0"
	width="100%" class="topBorderOnly" summary="">
	<tr>
	<tr class="topBorder">
		<td class="formTitle" colspan="4">
			<div align="justify">
				Results
			</div>
		</td>
	</tr>
	<tr>
		<td class="completeLabelNoBottom" valign="top" colspan="4">
			<strong>DataSet</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<c:if
				test="${canCreateNanoparticle eq 'true' && location eq 'local'}">
				<a id="addDataSet" href="javascript:resetTheDataSet(true);"> <span
					class="addLink2">Add</span> </a>
				&nbsp;&nbsp;
			</c:if>
		</td>
	</tr>
	<tr>
		<td class="leftLabel" valign="top">
			<div id="existingDataSet" style="display: block;">
				<c:forEach var="dataSet" varStatus="dataSetIndex"
					items="${characterizationForm.map.achar.dataSets}">
					<div class="indented4">
						<table class="smalltable2" border="0">
							<tr>
								<td class="subformTitle" colspan="2">
									<b>DataSet #${dataSetIndex.index+1}&nbsp; <a
										href="javascript:setTheDataSet(${dataSet.domain.id});">
											edit</a> </b>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<b>Data</b>
								</td>
							</tr>

							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									<table class="smalltable3" border="1">
										<tr>
											<c:forEach var="col" items="${dataSet.columnBeans}">
												<td>
													<strong>${col.displayName}</strong>
												</td>
											</c:forEach>
										</tr>
										<c:forEach var="dataRow" items="${dataSet.dataRows}">
											<tr>
												<c:forEach var="condition" items="${dataRow.conditions}">
													<td>
														${condition.value}
													</td>
												</c:forEach>
												<c:forEach var="datum" items="${dataRow.data}">
													<td>
														${datum.value}
													</td>
												</c:forEach>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<b>File</b>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									<a href="">characterization/20071109_11-15-38-098_test_upload.txt</a>
								</td>
							</tr>
						</table>
					</div>
					<br>
				</c:forEach>
			</div>
			&nbsp;
		</td>
		<td class="rightLabel" valign="top" colspan="3">
			<div id="newDataSet" style="display: none;">
				<div class="indented4">
					<table class="smalltable2" border="0">
						<tr>
							<td valign="top" colspan="1" class="subformTitle">
								<a href="javascript:showhide('submitDatum');"><b>Data</b> </a>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<div id="submitDatum" style="display: none;" align="center">
									<jsp:include
										page="/particle/characterization/shared/bodySubmitDataSet.jsp" />
								</div>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td valign="top" colspan="1" class="subformTitle">
								<a href="javascript:showhide('loadDatumFile');"><b>File</b>
								</a>
							</td>
						</tr>
						<tr>
							<td valign="top">
								<div id="loadDatumFile" style="display: none;" align="center">
									<jsp:include
										page="/particle/characterization/shared/bodyCharacterizationFile.jsp" />
								</div>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td valign="top" align="right">
								<input type="reset" value="Cancel"
									onclick="javascript:hide('newDataSet');show('existingDataSet');">
								&nbsp;
							</td>
						</tr>
					</table>
				</div>
			</div>
			&nbsp;
		</td>
	</tr>
</table>
<br>
