<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${param.topic}')"
				class="helpText">Help</a>&nbsp;				
<c:set var="glossary" value="${param.glossaryTopic}" />		
<c:choose>
   <c:when test="${empty glossary}" >
      <c:set var="glossary" value="glossary_help" />
   </c:when>  
</c:choose>
<a href="javascript:openHelpWindow('webHelp/index.html?single=true&amp;context=caNanoLab&amp;topic=${glossary}')"
				class="helpText">Glossary</a>&nbsp;



