package gov.nih.nci.cananolab.ui;

import gov.nih.nci.cananolab.exception.CaNanoLabException;
import gov.nih.nci.cananolab.ui.core.InitSetup;
import gov.nih.nci.cananolab.ui.particle.InitCompositionSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.impl.DefaultWebContextBuilder;

public class DWRManager {
	public DWRManager() {
	}

	public String getEntityIncludePage(String entityType)
			throws ServletException, IOException, CaNanoLabException {
		try {
			WebContext wctx = WebContextFactory.get();
			ServletContext appContext = wctx.getServletContext();
			String entityClassName = InitSetup.getInstance().getObjectName(
					entityType, appContext);
			String page = "/particle/composition/bodyNew" + entityClassName
					+ "Info.jsp";
			return wctx.forwardToString(page);
		} catch (Exception e) {
			return "";
		}
	}

	public String[] getComposingElementTypeOptions(String nanoparticleEntityType) {

		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
		org.directwebremoting.WebContext webContext = dwcb.get();
		HttpServletRequest request = webContext.getHttpServletRequest();

		if (nanoparticleEntityType.equals("emulsion")) {
			try {
				List<String> emulsionCompList = InitCompositionSetup
						.getInstance()
						.getEmulsionComposingElementTypes(request);
				String[] eleArray = new String[emulsionCompList.size()];
				return emulsionCompList.toArray(eleArray);
			} catch (Exception e) {
				System.out
						.println("getEmulsionComposingElementTypes exception.");
				e.printStackTrace();
			}
		} else {
			try {
				List<String> compList = InitCompositionSetup.getInstance()
						.getComposingElementTypes(request);
				String[] eleArray = new String[compList.size()];
				return compList.toArray(eleArray);
			} catch (Exception e) {
				System.out.println("getComposingElementTypes exception.");
				e.printStackTrace();
			}
		}
		return null;
	}

	public String[] getBiopolymerTypeOptions(String nanoparticleEntityType) {
		if (nanoparticleEntityType.equals("biopolymer")) {
			DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
			org.directwebremoting.WebContext webContext = dwcb.get();
			HttpServletRequest request = webContext.getHttpServletRequest();
			try {
				List<String> typeList = new ArrayList<String>(
						InitCompositionSetup.getInstance().getBiopolymerTypes(
								request));
				String[] eleArray = new String[typeList.size()];
				return typeList.toArray(eleArray);

			} catch (Exception e) {
				System.out.println("getBiopolymerTypeOptions exception.");
				e.printStackTrace();
			}
		}
		return new String[] { "" };
	}

	public String [] getAntibodyTypeOptions(String nanoparticleEntityType) {
		if (nanoparticleEntityType.equals("antibody")) {
			DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
			org.directwebremoting.WebContext webContext = dwcb.get();
			HttpServletRequest request = webContext.getHttpServletRequest();
			try {
				List<String> typeList = new ArrayList<String>(
						InitCompositionSetup.getInstance().getAntibodyTypes(
								request));
				String[] eleArray = new String[typeList.size()];
				return typeList.toArray(eleArray);

			} catch (Exception e) {
				System.out.println("getAntibodyTypeOptions exception.");
				e.printStackTrace();
			}
		}
		return new String[] {""};
	}
	
	public String [] getAntibodyIsotypeOptions(String nanoparticleEntityType) {
		if (nanoparticleEntityType.equals("antibody")) {
			DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
			org.directwebremoting.WebContext webContext = dwcb.get();
			HttpServletRequest request = webContext.getHttpServletRequest();
			try {
				List<String> typeList = new ArrayList<String>(
						InitCompositionSetup.getInstance().getAntibodyIsotypes(
								request));
				String[] eleArray = new String[typeList.size()];
				return typeList.toArray(eleArray);

			} catch (Exception e) {
				System.out.println("getAntibodyIsotypeOptions exception.");
				e.printStackTrace();
			}
		}
		return new String[] {""};
	}
}
