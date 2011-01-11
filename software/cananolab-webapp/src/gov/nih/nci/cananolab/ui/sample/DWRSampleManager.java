package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.CompositionQueryBean;
import gov.nih.nci.cananolab.dto.particle.SampleQueryBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Comparators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

public class DWRSampleManager {

	private Logger logger = Logger.getLogger(DWRSampleManager.class);
	private SampleServiceLocalImpl service;

	private SampleServiceLocalImpl getService() {
		WebContext wctx = WebContextFactory.get();
		SecurityService securityService = (SecurityService) wctx.getSession()
				.getAttribute("securityService");

		service = new SampleServiceLocalImpl(securityService);
		return service;
	}

	public List<LabelValueBean> getDecoratedEntityTypes(String compType) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		ServletContext appContext = request.getSession().getServletContext();
		List<LabelValueBean> types = new ArrayList<LabelValueBean>();
		try {
			if (compType.equals("nanomaterial entity")) {
				types = InitSetup
						.getInstance()
						.getDefaultAndOtherTypesByReflectionAsOptions(
								appContext,
								"defaultNanomaterialEntityTypes",
								"gov.nih.nci.cananolab.domain.common.NanomaterialEntity",
								"gov.nih.nci.cananolab.domain.material.OtherNanomaterialEntity");
			} else if (compType.equals("functionalizing entity")) {
				types = InitSetup
						.getInstance()
						.getDefaultAndOtherTypesByReflectionAsOptions(
								appContext,
								"defaultFunctionalizingEntityTypes",
								"gov.nih.nci.cananolab.domain.common.FunctionalizingEntity",
								"gov.nih.nci.cananolab.domain.material.agentmaterial.OtherFunctionalizingEntity");
			} else if (compType.equals("function")) {
				types = InitSetup
						.getInstance()
						.getDefaultAndOtherTypesByReflectionAsOptions(
								appContext,
								"defaultFunctionTypes",
								"gov.nih.nci.cananolab.domain.common.Function",
								"gov.nih.nci.cananolab.domain.material.OtherFunction");
			}
		} catch (Exception e) {
			return null;
		}
		return types;
	}

	public String[] getNanomaterialEntityTypes(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		ServletContext appContext = request.getSession().getServletContext();
		try {
			SortedSet<String> types = InitSetup
					.getInstance()
					.getDefaultAndOtherTypesByReflection(
							request,
							"defaultNanomaterialEntityTypes",
							"nanomaterialEntityTypes",
							"gov.nih.nci.cananolab.domain.common.NanomaterialEntity",
							"gov.nih.nci.cananolab.domain.material.OtherNanomaterialEntity",
							true);

			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting nanomaterial entity types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getFunctionalizingEntityTypes() {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		ServletContext appContext = request.getSession().getServletContext();
		try {
			SortedSet<String> types = InitSetup
					.getInstance()
					.getDefaultAndOtherTypesByReflection(
							request,
							"defaultFunctionalizingEntityTypes",
							"functionalizingEntityTypes",
							"gov.nih.nci.cananolab.domain.common.FunctionalizingEntity",
							"gov.nih.nci.cananolab.domain.material.agentmaterial.OtherFunctionalizingEntity",
							true);

			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting functionalizing entity types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getFunctionTypes(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		ServletContext appContext = request.getSession().getServletContext();

		try {
			SortedSet<String> types = InitSetup
					.getInstance()
					.getDefaultAndOtherTypesByReflection(
							request,
							"defaultFunctionTypes",
							"functionTypes",
							"gov.nih.nci.cananolab.domain.common.Function",
							"gov.nih.nci.cananolab.domain.material.OtherFunction",
							true);

			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting function types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String getPublicCounts() {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		request.getSession().removeAttribute("sampleSearchResults");

		Integer counts = 0;
		try {
			counts += getService().getNumberOfPublicSamples();
		} catch (Exception e) {
			logger
					.error("Error obtaining counts of public samples from local site.");

		}
		return counts.toString() + " Samples";
	}

	public String getPublicSourceCounts() {
		WebContext wctx = WebContextFactory.get();
		Integer counts = 0;
		try {
			counts += getService().getNumberOfPublicSampleSources();
		} catch (Exception e) {
			logger
					.error("Error obtaining counts of public sample sources from local site.");
		}

		return counts.toString() + " Sample sources";
	}

	public AdvancedSampleSearchBean addSampleQuery(SampleQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		if (theQuery != null)
			searchBean.addQuery(theQuery);
		return searchBean;
	}

	public AdvancedSampleSearchBean addCompositionQuery(
			CompositionQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		if (theQuery != null)
			searchBean.addQuery(theQuery);
		return searchBean;
	}

	public AdvancedSampleSearchBean addCharacterizationQuery(
			CharacterizationQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		if (theQuery != null)
			searchBean.addQuery(theQuery);
		return searchBean;
	}

	public AdvancedSampleSearchBean deleteSampleQuery(SampleQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		searchBean.removeQuery(theQuery);
		return searchBean;
	}

	public AdvancedSampleSearchBean deleteCompositionQuery(
			CompositionQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		searchBean.removeQuery(theQuery);
		return searchBean;
	}

	public AdvancedSampleSearchBean deleteCharacterizationQuery(
			CharacterizationQueryBean theQuery) {
		DynaValidatorForm searchForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("advancedSampleSearchForm"));
		if (searchForm == null) {
			return null;
		}
		AdvancedSampleSearchBean searchBean = (AdvancedSampleSearchBean) searchForm
				.get("searchBean");
		searchBean.removeQuery(theQuery);
		return searchBean;
	}

	public LabelValueBean[] getCharacterizationOperandOptions(String datumName) {
		WebContext wctx = WebContextFactory.get();
		if (datumName.startsWith("is ") && datumName.endsWith("?")) {
			return (LabelValueBean[]) wctx.getServletContext().getAttribute(
					"booleanOperands");
		} else {
			return (LabelValueBean[]) wctx.getServletContext().getAttribute(
					"numberOperands");
		}
	}

	public String getDetailContent(String url) throws ServletException,
			IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();
			String content = wctx.forwardToString("/" + url);
			return content;
		} catch (Exception e) {
			return "";
		}
	}

	public String[] getMatchedSampleNames(String searchStr) throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		String[] nameArray = new String[] { "" };
		try {
			List<String> names = ((SampleServiceLocalImpl) getService())
					.getHelper().findSampleNamesBy(searchStr);
			Collections.sort(names, new Comparators.SortableNameComparator());

			if (!names.isEmpty()) {
				nameArray = names.toArray(new String[names.size()]);
			}
		} catch (Exception e) {
			logger.error("Problem getting matched sample names", e);
		}
		return nameArray;
	}
}
