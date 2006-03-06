<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html>
<head>
<title><tiles:getAsString name="title" ignore="true"/></title>
<link rel="stylesheet" type="text/css" href="css/caLab.css" />
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<script type="text/javascript" src="javascript/script.js"></script>
</head>

<body>
  <table height="100%" cellspacing="0" cellpadding="0" width="100%" summary="" border="0">
    <!-- nci hdr begins -->
    <tbody>
    <%-- include NCI header --%>
    <tiles:insert attribute="nciHeader"/>
    <tr>
        <td valign="top" height="100%">
          <table height="100%" cellspacing="0" cellpadding="0" summary="" border="0">
              <tbody>
              <%-- include caLAB header --%>
              <tiles:insert attribute="calabHeader"/>
              <tr>
                <td class="sideMenu" valign="top" width="190">
        		  <%-- include sidemenu on the left --%>
        		  <tiles:insert attribute="calabSidemenu"/>
                </td>

                <td valign="top" width="100%">
                  <table height="100%" cellspacing="0" cellpadding="0" width="100%" summary="" border="0">
                    <tbody>                  
                      <%-- include caLAB main menu --%>
                      <tiles:insert attribute="calabMainmenu"/>
                      <%-- include caLAB main content --%>
                      <tiles:insert attribute="calabContent"/>
                      <%-- include caLAB footer --%>
                      <tiles:insert attribute="calabFooter"/>
                    </tbody>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
        </td>
     </tr>
     <%-- include NCI footer --%>
     <tiles:insert attribute="nciFooter"/>
	 </tbody>
	</table>
</body>  