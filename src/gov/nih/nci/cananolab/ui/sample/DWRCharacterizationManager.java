package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.ui.core.InitSetup;

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
			String charClassName = InitSetup.getInstance().getClassName(
					charName, appContext);
			String includePage = null;
			if (charClassName.equals("PhysicalState")
					|| charClassName.equals("Shape")
					|| charClassName.equals("Solubility")
					|| charClassName.equals("Surface")) {
				includePage = "/sample/characterization/physical/body"
						+ charClassName + "Info.jsp";
			} else if (charClassName.equals("Cytotoxicity")
					|| charClassName.equals("EnzymeInduction")) {
				includePage = "/sample/characterization/invitro/body"
						+ charClassName + "Info.jsp";
			}
			if (includePage != null) {
				return wctx.forwardToString(includePage);
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}
}
