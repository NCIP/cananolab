<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/cananolab.tld" prefix="cananolab" %>
<c:if test="${!empty param.printLink}" >
	<a class="helpText" href="javascript:printPage('${param.printLink}')" id="printLink">Print</a>
	&nbsp;
</c:if>  
<c:if test="${!empty param.exportLink}" >
	<a class="helpText" href="${param.exportLink}" id="exportLink">Export</a>
	&nbsp;
</c:if>  
<c:if test="${!empty param.otherLink}" >
	<a class="helpText" href="${param.otherLink}">${param.other}</a>
	&nbsp;
</c:if>  
<cananolab:cshelp topic="${param.topic}" key="Help" text="Help"/>
&nbsp;				
<c:set var="glossary" value="${param.glossaryTopic}" />		
<c:if test="${empty glossary}" >
   <c:set var="glossary" value="glossary_help" />
</c:if>  
<cananolab:cshelp topic="${glossary}" key="Help" text="Glossary"/>
&nbsp;
