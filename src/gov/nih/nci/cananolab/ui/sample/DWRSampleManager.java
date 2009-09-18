package gov.nih.nci.cananolab.ui.sample;

import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.CompositionQueryBean;
import gov.nih.nci.cananolab.dto.particle.SampleQueryBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceRemoteImpl;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
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

	public DWRSampleManager() {
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
						.getReflectionDefaultAndOtherLookupTypesAsOptions(
								appContext,
								"defaultNanomaterialEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
								"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity");
			} else if (compType.equals("functionalizing entity")) {
				types = InitSetup
						.getInstance()
						.getReflectionDefaultAndOtherLookupTypesAsOptions(
								appContext,
								"defaultFunctionalizingEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
								"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity");
			} else if (compType.equals("function")) {
				types = InitSetup
						.getInstance()
						.getReflectionDefaultAndOtherLookupTypesAsOptions(
								appContext,
								"defaultFunctionTypes",
								"gov.nih.nci.cananolab.domain.particle.Function",
								"gov.nih.nci.cananolab.domain.function.OtherFunction");
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
			boolean isLocal = false;
			if (searchLocations == null) {
				isLocal = true;
			} else if (Constants.LOCAL_SITE.equals(searchLocations)) {
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup
						.getInstance()
						.getReflectionDefaultAndOtherLookupTypes(
								request,
								"defaultNanomaterialEntityTypes",
								"nanomaterialEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity",
								"gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity",
								true);
			} else {
				types = InitSetup
						.getInstance()
						.getServletContextDefaultTypesByReflection(appContext,
								"defaultNanomaterialEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.NanomaterialEntity");
			}

			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting nanomaterial entity types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String[] getFunctionalizingEntityTypes(String searchLocations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		ServletContext appContext = request.getSession().getServletContext();
		try {
			boolean isLocal = false;
			if (searchLocations == null) {
				isLocal = true;
			} else if (Constants.LOCAL_SITE.equals(searchLocations)) {
				isLocal = true;
			}

			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup
						.getInstance()
						.getReflectionDefaultAndOtherLookupTypes(
								request,
								"defaultFunctionalizingEntityTypes",
								"functionalizingEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity",
								"gov.nih.nci.cananolab.domain.agentmaterial.OtherFunctionalizingEntity",
								true);
			} else {
				InitSetup
						.getInstance()
						.getServletContextDefaultTypesByReflection(appContext,
								"defaultFunctionalizingEntityTypes",
								"gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity");
			}

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
			boolean isLocal = false;
			if (Constants.LOCAL_SITE.equals(searchLocations)) {
				isLocal = true;
			}
			SortedSet<String> types = null;
			if (isLocal) {
				types = InitSetup
						.getInstance()
						.getReflectionDefaultAndOtherLookupTypes(
								request,
								"defaultFunctionTypes",
								"functionTypes",
								"gov.nih.nci.cananolab.domain.particle.Function",
								"gov.nih.nci.cananolab.domain.function.OtherFunction",
								true);
			} else {
				InitSetup
						.getInstance()
						.getServletContextDefaultTypesByReflection(appContext,
								"defaultFunctionTypes",
								"gov.nih.nci.cananolab.domain.particle.Function");
			}
			String[] eleArray = new String[types.size()];
			return types.toArray(eleArray);
		} catch (Exception e) {
			logger.error("Problem setting function types: \n", e);
			e.printStackTrace();
		}
		return new String[] { "" };
	}

	public String getPublicCounts(String[] locations) {
		WebContext wctx = WebContextFactory.get();
		HttpServletRequest request = wctx.getHttpServletRequest();
		request.getSession().removeAttribute("sampleSearchResults");
		if (locations.length == 0) {
			return null;
		}
		Integer counts = 0;
		SampleService service = null;
		for (String location : locations) {
			if (location.equals(Constants.LOCAL_SITE)) {
				try {
					service = new SampleServiceLocalImpl();
					counts += service.getNumberOfPublicSamples();
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public samples from local site.");
				}
			} else {
				try {
					String serviceUrl = InitSetup.getInstance()
							.getGridServiceUrl(request, location);

					service = new SampleServiceRemoteImpl(serviceUrl);
					counts += service.getNumberOfPublicSamples();
				} catch (Exception e) {
					logger
							.error("Error obtaining counts of public samples from "
									+ location);
				}
			}
		}
		return counts.toString();
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

	public String getAdvancedSearchDetailContent(String url)
			throws ServletException, IOException, BaseException {
		try {
			WebContext wctx = WebContextFactory.get();			
			String content = wctx.forwardToString("/"+url);
			return content;
		} catch (Exception e) {
			return "";
		}
	}
}
