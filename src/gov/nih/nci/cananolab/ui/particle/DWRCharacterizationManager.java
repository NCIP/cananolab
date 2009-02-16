package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCharacterizationServiceHelper;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRCharacterizationManager {
	private NanoparticleCharacterizationServiceHelper helper = new NanoparticleCharacterizationServiceHelper();

	public String[] getCharacterizationOptions(String characterizationType)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		ServletContext appContext = wctx.getServletContext();
		String fullCharTypeClass = InitSetup.getInstance().getFullClassName(
				characterizationType, appContext);
		SortedSet<String> charNames = new TreeSet<String>();
		List<String> charClassNames = ClassUtils
				.getChildClassNames(fullCharTypeClass);
		for (String charClass : charClassNames) {
			if (!charClass.startsWith("Other")) {
				String shortClassName = ClassUtils.getShortClassName(charClass);
				charNames.add(InitSetup.getInstance().getDisplayName(
						shortClassName, appContext));
			}
		}

		List<String> otherCharNames = helper
				.findOtherCharacterizationByAssayCategory(characterizationType);
		if (!otherCharNames.isEmpty()) {
			charNames.addAll(otherCharNames);
		}
		String[] charNameArray = new String[charNames.size()];
		charNames.toArray(charNameArray);
		return charNameArray;
	}
}
