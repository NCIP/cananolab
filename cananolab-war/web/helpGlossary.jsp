<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${!empty param.otherLink}" >
	<a class="helpText" href="${param.otherLink}">${param.other}</a>
	&nbsp;
</c:if>  
<a	class="helpText" 
	href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${param.topic}')">
Help</a>
&nbsp;				
<c:set var="glossary" value="${param.glossaryTopic}" />		
<c:if test="${empty glossary}" >
   <c:set var="glossary" value="glossary_help" />
</c:if>  
<a	class="helpText" 
	href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${glossary}')">
Glossary</a>
&nbsp;
