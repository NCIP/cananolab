<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html:form action="/loadFile" enctype="multipart/form-data">
	<table width="100%" align="center">
		<tr>
			<td>
				<h3>
					<br>
					Load Charaterization File
				</h3>
			</td>
			<td align="right" width="15%">
				<a
					href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=load_characterization_file_help')"
					class="helpText">Help</a> &nbsp;&nbsp;
				<a href="javascript:history.go(-1)" class="helpText">back</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<jsp:include page="/bodyMessage.jsp?bundle=submit" />
				<table class="topBorderOnly" cellspacing="0" cellpadding="3"
					width="100%" align="center" summary="" border="0">
					<tbody>
						<tr class="topBorder">
							<td class="formTitle" colspan="4">
								<div align="justify">
									File Information
								</div>
							</td>
						</tr>
						<tr>
							<td class="completeLabel" colspan="2">
								<html:radio property="fileSource" value="new">
									<strong> Upload New File</strong>
								</html:radio>
								&nbsp; &nbsp;
								<html:file size="60" property="uploadedFile" />
								<br>
								<html:radio property="fileSource" value="chooseExisting">
									<strong> Choose File from the caNanoLab workflow</strong>
								</html:radio>&nbsp;&nbsp;
								<html:select property="file.id">
									<html:options collection="allRunFiles" property="id"
										labelProperty="displayName" />
								</html:select>	
							</td>
						</tr>
						<tr>
							<td class="leftLabel">
								<strong>File Title*</strong>
							</td>
							<td class="rightLabel">
								<html:text property="file.title" size="60" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Keywords <em>(one per line)</em> </strong>
							</td>
							<td class="rightLabel">
								<html:textarea property="file.keywordsStr" rows="3" />
							</td>
						</tr>
						<tr>
							<td class="leftLabel" valign="top">
								<strong>Visibility</strong>
							</td>
							<td class="rightLabel">
								<html:select property="file.visibilityGroups" multiple="true"
									size="6">
									<html:options name="allVisibilityGroups" />
								</html:select>
								<br>
								<i>(${applicationOwner}_Researcher and
									${applicationOwner}_PI are defaults if none of above is
									selected.)</i>
							</td>
						</tr>										
					</tbody>
				</table>
				<br>
				<table width="100%" border="0" align="center" cellpadding="3"
					cellspacing="0" class="topBorderOnly" summary="">
					<tr>
						<td width="30%">
							<span class="formMessage"> </span>
							<br>
							<table width="498" height="32" border="0" align="right"
								cellpadding="4" cellspacing="0">
								<tr>
									<td width="490" height="32">
										<div align="right">
											<div align="right">
												<input type="button" value="Cancel"
													onclick="javascript:history.go(-1);">
												<input type="reset" value="Reset">
												<input type="hidden" name="dispatch" value="submit">
												<input type="hidden" name="page" value="2">
												<html:hidden property="forwardPage" />												
												<html:hidden property="particleName" />
												<html:hidden property="characterizationName" />
												<html:submit />
											</div>
										</div>
									</td>
								</tr>
							</table>
							<div align="right"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>
<script language="JavaScript">
<!--//

/* populate a hashtable containing assayType assays */ 
var assayTypeAssays=new Array();
<c:forEach var="item" items="${allAssayTypeAssays}">
    var assays=new Array();
    <c:forEach var="assay" items="${allAssayTypeAssays[item.key]}" varStatus="count">
  		assays[${count.index}]='${assay.assayName}';  	
    </c:forEach>
    assayTypeAssays['${item.key}']=assays;
  </c:forEach>
//-->
</script>
