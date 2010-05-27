package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.SortedSet;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
public class DWRExperimentConfigManager {
	private CharacterizationServiceHelper helper;

	private CharacterizationServiceHelper getHelper() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		helper = new CharacterizationServiceHelper(user);
		return helper;
	}

	public ExperimentConfigBean resetTheExperimentConfig() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		if (charForm == null) {
			return null;
		}
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		ExperimentConfigBean newExperimentConfigBean = new ExperimentConfigBean();
		charBean.setTheExperimentConfig(newExperimentConfigBean);
		return newExperimentConfigBean;
	}

	public ExperimentConfigBean getExperimentConfigById(String id)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		ExperimentConfig config = getHelper().findExperimentConfigById(id);
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		ExperimentConfigBean configBean = new ExperimentConfigBean(config);
		charBean.setTheExperimentConfig(configBean);
		return configBean;
	}

	public String getTechniqueAbbreviation(String techniqueType)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> abbrevs = InitSetup.getInstance()
				.getDefaultTypesByLookup(wctx.getServletContext(),
						"defaultAbbreviations", techniqueType, "abbreviation");
		String abbreviation = "";
		if (abbrevs != null && !abbrevs.isEmpty()) {
			abbreviation = abbrevs.first();
		}
		return abbreviation;
	}

	public String[] getInstrumentTypesByTechniqueType(String techniqueType)
			throws ExperimentConfigException, BaseException {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> types = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(wctx.getHttpServletRequest(),
						"techniqueInstruments", techniqueType, "instrument",
						"otherInstrument", true);

		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}

	public String[] getInstrumentTypesByConfigId(String configId)
			throws Exception {
		String techniqueType = null;
		SortedSet<String> types = null;
		WebContext wctx = WebContextFactory.get();
		ExperimentConfigBean config = getExperimentConfigById(configId);

		if (config != null
				&& config.getDomain().getTechnique() != null
				&& !StringUtils.isEmpty(config.getDomain().getTechnique()
						.getType())) {
			techniqueType = config.getDomain().getTechnique().getType();
			types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(
					wctx.getHttpServletRequest(), "techniqueInstruments",
					techniqueType, "instrument", "otherInstrument", true);
		}
		if (types != null && types.size() > 0) {
			String[] typeArray = new String[types.size()];
			types.toArray(typeArray);
			return typeArray;
		} else {
			return null;
		}
	}

	public ExperimentConfigBean addInstrument(Instrument instrument)
			throws ExperimentConfigException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		if (charForm == null) {
			return null;
		}
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		ExperimentConfigBean theExperimentConfig = charBean
				.getTheExperimentConfig();
		theExperimentConfig.addInstrument(instrument);
		return theExperimentConfig;
	}

	public ExperimentConfigBean deleteInstrument(Instrument instrument)
			throws ExperimentConfigException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		if (charForm == null) {
			return null;
		}
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		ExperimentConfigBean theExperimentConfig = charBean
				.getTheExperimentConfig();
		theExperimentConfig.removeInstrument(instrument);
		return theExperimentConfig;
	}
}
