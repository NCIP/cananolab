package gov.nih.nci.cananolab.ui.study;

import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRStudyManager {

	private Logger logger = Logger.getLogger(DWRStudyManager.class);

	public String[] getMatchedDesignTypes(String searchStr) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		String[] typeArray = new String[] { "" };
		try {
			SortedSet<String>types=InitSetup.getInstance().getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
					"studyDesignTypes", "study", "designType",
					"otherType", true);

			if (!types.isEmpty()) {
				typeArray = types.toArray(new String[types.size()]);
			}
		} catch (Exception e) {
			logger.error("Problem getting matched design types", e);
		}
		return typeArray;
	}
}
