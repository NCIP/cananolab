<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<logic:present name="user">
	<tiles:insert definition="canano.welcome" />
</logic:present>
<logic:notPresent name="user">
	<logic:forward name="welcome" />
</logic:notPresent>

