/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.util.StringUtils;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("experimentConfigManager")
public class ExperimentConfigManager
{

	public ExperimentConfigBean resetTheExperimentConfig() {
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		if (charForm == null) {
//			return null;
//		}
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		ExperimentConfigBean newExperimentConfigBean = new ExperimentConfigBean();
//		charBean.setTheExperimentConfig(newExperimentConfigBean);
//		return newExperimentConfigBean;
		return null;
	}

	public ExperimentConfigBean getExperimentConfigById(String id)
			throws Exception {
//		WebContext wctx = WebContextFactory.get();
//		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
//		if (user == null) {
//			return null;
//		}
//		ExperimentConfig config = getHelper().findExperimentConfigById(id);
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		ExperimentConfigBean configBean = new ExperimentConfigBean(config);
//		charBean.setTheExperimentConfig(configBean);
//		return configBean;
		return null;
	}

	public String getTechniqueAbbreviation(HttpServletRequest request, String techniqueType)
			throws Exception {
		SortedSet<String> abbrevs = InitSetup.getInstance()
				.getDefaultTypesByLookup(request.getSession().getServletContext(),
						"defaultAbbreviations", techniqueType, "abbreviation");
		String abbreviation = "";
		if (abbrevs != null && !abbrevs.isEmpty()) {
			abbreviation = abbrevs.first();
		}
		return abbreviation;
	}

	public List<String> getInstrumentTypesByTechniqueType(HttpServletRequest request, String techniqueType)
			throws ExperimentConfigException, BaseException {
		SortedSet<String> types = InitSetup.getInstance()
				.getDefaultAndOtherTypesByLookup(request,
						"techniqueInstruments", techniqueType, "instrument",
						"otherInstrument", true);
		
		List<String> instTypes = new ArrayList<String>();

		if (types != null && types.size() > 0) {
			instTypes.addAll(types); 
		} 
		
		instTypes.add("other");
		return instTypes;
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
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		if (charForm == null) {
//			return null;
//		}
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		ExperimentConfigBean theExperimentConfig = charBean
//				.getTheExperimentConfig();
//		theExperimentConfig.addInstrument(instrument);
//		return theExperimentConfig;
		
		return null;
	}

	public ExperimentConfigBean deleteInstrument(Instrument instrument)
			throws ExperimentConfigException {
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		if (charForm == null) {
//			return null;
//		}
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		ExperimentConfigBean theExperimentConfig = charBean
//				.getTheExperimentConfig();
//		theExperimentConfig.removeInstrument(instrument);
//		return theExperimentConfig;
		
		return null;
	}
}
