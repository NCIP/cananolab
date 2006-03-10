<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

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
