<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:form action="/useAliquot">
	<h2>
		<br>
		Use Aliquot
	</h2>
	<blockquote>
		<table width="75%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td colspan="2" class="dataTablePrimaryLabel">
					<div align="justify">
						<em>USE ALIQUOT </em>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<strong>Aliquot ID <img src="images/help.gif" width="15" height="15"></strong>
				</td>
				<td class="formField">
					<div align="left">
						* Hold down the shift key for multiple selections.
						<br>
						<span class="formField" align="left"><span class="mainMenu"><span class="formMessage"><strong> <html:select property="aliquotIds" multiple="true" size="3">
											<html:options name="allAliquotIds" />
										</html:select> </strong></span></span></span> <span class="formFieldWhite"> </span>
					</div>
					<html:errors />
				</td>
			</tr>
		</table>
		<br>

		<table width="60%" border="0" align="center" cellpadding="3" cellspacing="0" class="topBorderOnly" summary="">
			<tr class="topBorder">
				<td class="dataTablePrimaryLabel">
					<div align="justify">
						<em>GENERAL COMMENTS </em>
					</div>
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<div align="left">
						<span class="formField"><span class="formFieldWhite"> <textarea name="Input22222" cols="50" rows="3" wrap="OFF"></textarea> </span></span>
					</div>
				</td>
			</tr>
			<tr>
				<td width="30%">
					<table border="0" align="right" cellpadding="4" cellspacing="0">
						<tr>
							<td>
								<div align="left">
								    <input type="hidden" name="runId" value="<bean:write name="runId"/>">
									<html:reset/>
									<html:submit/>
									<input type="button" value="Cancel" onclick="javascript:history.go(-1)">
								</div>
							</td>
						</tr>
					</table>
					<div align="right"></div>
				</td>
			</tr>
		</table>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
		<p>
			&nbsp;
		</p>
	</blockquote>
</html:form>
