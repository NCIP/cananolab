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
	<jsp:param name="pageTitle" value="Samples for Study MIT_MGH_Kelly" />
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
					<td></td><td></td>
					<td width="5%" align="right">
						<a href="sample.do?dispatch=summaryEdit&page=0&sampleId=11337748">Edit</a>
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td colspan="2">
						MIT_MGH-KKellyIB2009-01
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Description
					</td>
					<td colspan="2">
						This nanoparticle is described in Kelly KA, Shaw SY, Nahrendorf M,
						Kristoff K, Aikawa E, Schreiber SL, Clemons PA and Weissleder R.
						Unbiased discovery of in vivo imaging probes through in vitro
						profiling of nanoparticle libraries. Integr Biol 1:311-317 (2009).

						Superparamagnetic iron oxide core, crosslinked dextran coat, and
						fluorophore conjugate

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
					<td></td><td></td>
					<td width="5%" align="right">
						<a href="sample.do?dispatch=summaryView&page=0&sampleId=11337748">edit</a>
					</td>
				</tr>
				<tr>
					<td class="cellLabel" width="20%">
						Sample Name
					</td>
					<td colspan="2">
						MIT_MGH-KKellyIB2009-02
					</td>
				</tr>
				<tr>
					<td class="cellLabel">
						Description
					</td>
					<td colspan="2">
						This nanoparticle is described in Kelly KA, Shaw SY, Nahrendorf M,
						Kristoff K, Aikawa E, Schreiber SL, Clemons PA and Weissleder R.
						Unbiased discovery of in vivo imaging probes through in vitro
						profiling of nanoparticle libraries. Integr Biol 1:311-317 (2009).
						Superparamagnetic iron oxide core, crosslinked dextran coat,
						fluorophore, and anhydride conjugate
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
