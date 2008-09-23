<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<logic:present parameter="bundle">
	<bean:parameter id="bundle" name="bundle" />
	<logic:messagesPresent>
		<font color="red">
			<ul>
				<html:messages id="error" bundle="<%=bundle%>">
					<li>
						<bean:write name="error" />
					</li>
				</html:messages>
			</ul> </font>
	</logic:messagesPresent>
	<logic:messagesPresent message="true">
		<font color="blue">
			<ul>
				<html:messages id="msg" message="true" bundle="<%=bundle%>">
					<li>
						<bean:write name="msg" />
					</li>
				</html:messages>
			</ul> </font>
	</logic:messagesPresent>
</logic:present>

<logic:notPresent parameter="bundle">
	<logic:messagesPresent>
		<font color="red">
			<ul>
				<html:messages id="error">
					<li>
						<bean:write name="error" />
					</li>
				</html:messages>
			</ul> </font>
	</logic:messagesPresent>
	<logic:messagesPresent message="true">
		<font color="blue">
			<ul>
				<html:messages id="msg" message="true">
					<li>
						<bean:write name="msg" />
					</li>
				</html:messages>
			</ul> </font>
	</logic:messagesPresent>
</logic:notPresent>
