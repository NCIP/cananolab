<tr>
	<td class="sidebarTitle"><a href="#" class="loginText"
		onClick="showhide('loginBlock');">Login</a> &nbsp; <a href="#"
		class="loginText" onClick="javascript:location.href='register.jsp';">Register</a>&nbsp;&nbsp;
		<cananolab:cshelp topic="welcome_login" key="Help" text="Help" styleClass="loginText"/></td>
</tr>
<tr>
	<td class="sidebarContent">No account is required to browse
		publicly available data.</td>
</tr>
<tr>
	<td><div id="loginBlock" style="display:none">
			<div class="spacer"/>
			<table class="loginBlock" summary="layout">				
				<tr>
					<td><label for="login ID">LOGIN ID</label></td>
					<td><input type="text" name="loginId" size="14" id="login ID" />
					</td>
				</tr>
				<tr>
					<td><label for="password">PASSWORD</label></td>
					<td><input type="password" name="password" size="14"
						id="password" autocomplete="off"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><html:submit value="Login" /></td>
				</tr>
			</table>
		</div></td>
</tr>
