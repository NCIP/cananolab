package gov.nih.nci.cananolab.ui;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRManager {
	public DWRManager() {

	}

	public String getEntityIncludePage(String entityType)
			throws ServletException, IOException, CaNanoLabException {
		WebContext wctx = WebContextFactory.get();
		ServletContext appContext = wctx.getServletContext();
		String entityClassName = InitSetup.getInstance().getObjectName(
				entityType, appContext);
		String page = "/particle/composition/body" + entityClassName
				+ "Info.jsp";
		return wctx.forwardToString(page);
	}
}
