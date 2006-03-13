<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<logic:present parameter="bundle">
<bean:parameter id="bundle" name="bundle"/>
<logic:messagesPresent>
	<font color="red">ERROR</font>
	<ul>
		<html:messages id="error" bundle="<%=bundle%>">
			<li>
				<bean:write name="error" />
			</li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<br>
<logic:messagesPresent message="true">
	<html:messages id="msg" message="true" bundle="<%=bundle%>">
		<bean:write name="msg" />
		<br />
	</html:messages>
</logic:messagesPresent>
</logic:present>

<logic:notPresent parameter="bundle">
<logic:messagesPresent>
	<font color="red">ERROR</font>
	<ul>
		<html:messages id="error">
			<li>
				<bean:write name="error" />
			</li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<br>
<logic:messagesPresent message="true">
	<html:messages id="msg" message="true">
		<bean:write name="msg" />
		<br />
	</html:messages>
</logic:messagesPresent>
</logic:notPresent>