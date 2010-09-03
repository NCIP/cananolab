<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type='text/javascript' src='javascript/addDropDownOptions.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<script type="text/javascript" src="javascript/SampleManager.js"></script>
<script type="text/javascript"
	src="/caNanoLab/dwr/interface/SampleManager.js"></script>
<script type='text/javascript' src='/caNanoLab/dwr/engine.js'></script>
<script type='text/javascript' src='/caNanoLab/dwr/util.js'></script>
<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<c:set var="title" value="Copy" />
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Generate Data Availability" />
	<jsp:param name="topic" value="copy_sample_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<html:form action="/generateBatchDataAvailability" >
	<table width="100%" align="left">
		<tr>
			<td colspan="4">
				<jsp:include page="/bodyMessage.jsp" />
				<table width="100%" align="left" class="submissionView">					
					<tr>
						<td class="cellLabel" width="20%">
							<html:radio styleId="option0"
						property="option" value="option1" />
					Generate Data Availability for submitted samples that data availability has not been generated.
					<br>
					&nbsp;&nbsp;or
					<br>
					<html:radio styleId="option1"
						property="option" value="option2" />
					Re-generate all Data Availability which includes update existing data availability as well as generate new ones.
					<br>
					&nbsp;&nbsp;or
					<br>
					
					<html:radio styleId="option1"
						property="option" value="option3" />
						Delete data availability
						<br>
					&nbsp;&nbsp;or
					<br>
					<html:radio styleId="option1"
						property="option" value="option4" />
						Update data availability
						</td>						
						
					</tr>
				</table>
				<br>
			
			
				<table border="0" align="right" cellpadding="4" cellspacing="0">
					<tr>
						<td>
							<div align="right">
								<input type="reset" value="Reset" onclick="this.form.reset()">
								
								<input type="submit" value="Submit"  onclick="batchDataAvailability.do?"/>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>

