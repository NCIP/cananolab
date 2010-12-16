<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="StyleSheet" type="text/css" href="css/promptBox.css">
<script type="text/javascript" src="javascript/addDropDownOptions.js"></script>
<script type='text/javascript' src='dwr/engine.js'></script>
<script type='text/javascript' src='dwr/util.js'></script>

<%--TODO: create online help topic for this page.--%>
<jsp:include page="/bodyTitle.jsp">
	<jsp:param name="pageTitle" value="Study Efficacy of nanoparticle Sample" />
	<jsp:param name="topic" value="study_help" />
	<jsp:param name="glossaryTopic" value="glossary_help" />
</jsp:include>
<jsp:include page="/bodyMessage.jsp?bundle=study" />

<table width="100%" align="center" class="summaryViewNoGrid"
	bgcolor="#dbdbdb">	
	<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>
	<tr>
		<td>
			<table width="99%" align="center" class="summaryViewNoGrid"
				bgcolor="#F5F5f5">				
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						MIT_MGH-KKellyIB2009-01
					</td>
					<td width="5%" align="right"><a href="sample.do?dispatch=summaryView&page=0&sampleId=11337747">View</a></td>
				</tr>
				<tr>
					<td class="cellLabel">
						Description						
					</td>
					<td>
						This nanoparticle is described in
Kelly KA, Shaw SY, Nahrendorf M, Kristoff K, Aikawa E, Schreiber SL, Clemons PA and Weissleder R.
Unbiased discovery of in vivo imaging probes through in vitro profiling of nanoparticle libraries.
Integr Biol 1:311-317 (2009).

Superparamagnetic iron oxide core, crosslinked dextran coat, and fluorophore conjugate
						
					</td>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>
	<tr>
		<td>
			<table width="99%" align="center" class="summaryViewNoGrid"
				bgcolor="#F5F5f5">
				<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>				
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td>
						NCL-23-1
					</td>
					<td width="5%" align="right"><a href="sample.do?dispatch=summaryView&page=0&sampleId=11337748">View</a></td>
				</tr>
				<tr>
					<td class="cellLabel">
						Description						
					</td>
					<td>
						G4.5 COONa terminated PAMAM dendrimer-Magnevist® complex
						
					</td>
				</tr>				
			</table>
		</td>
	</tr>
	<tr>
		<th valign="top" align="left" height="6">
		</th>
	</tr>
</table>
