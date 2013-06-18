<%--L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L--%>

<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<logic:present name="user">
	<tiles:insert definition="calab.welcome" />
</logic:present>
<logic:notPresent name="user">
	<tiles:insert definition="calab.index" />
</logic:notPresent>

