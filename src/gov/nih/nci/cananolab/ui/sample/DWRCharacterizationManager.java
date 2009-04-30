package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.exception.BaseException;

import java.io.IOException;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRCharacterizationManager {
	public String[] getCharacterizationOptions(String characterizationType)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> charNames = InitCharacterizationSetup.getInstance()
				.getCharNamesByCharType(wctx.getHttpServletRequest(),
						characterizationType);
		String[] charNameArray = new String[charNames.size()];
		charNames.toArray(charNameArray);
		return charNameArray;
	}

	public String[] getAssayTypeOptions(String characterizationName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> assayTypes = InitCharacterizationSetup.getInstance()
				.getAssayTypesByCharName(wctx.getHttpServletRequest(),
						characterizationName);
		return assayTypes.toArray(new String[assayTypes.size()]);
	}

	public String getCharacterizationDetailPage(String charName)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			ServletContext appContext = wctx.getServletContext();
			String includePage = InitCharacterizationSetup.getInstance()
					.getDetailPage(appContext, charName);
			if (includePage != null) {
				String content=wctx.forwardToString(includePage);
				return content;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
}
