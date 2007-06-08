<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" align="center">
	<tr>
		<td>
			<center>
				<h3>
					Particle Disclaimer
				</h3>
			</center>
			Please do not make conclusion with individual data and graph for this particle.  Please review the final report
			<c:forEach var="aReport" items="${particleReports}">
				<bean:define id="fileId" name='aReport' property='id' type="java.lang.String" />
				<html:hidden name="aReport" property="id" value="${fileId}" indexed="true" />
				<span class="indented"> <a href="publishReport.do?dispatch=download&amp;fileId=${fileId}">${aReport.name}</a> </span>
									&nbsp;&nbsp;
								</c:forEach>

		</td>
	</tr>
</table>
