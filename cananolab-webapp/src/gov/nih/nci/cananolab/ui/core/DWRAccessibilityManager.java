package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.ProtocolBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.BaseService;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu
 *
 */
public class DWRAccessibilityManager {
	private BaseService service;

	private BaseService getService() {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		service = new BaseServiceLocalImpl(user);
		return service;
	}

	public AccessibilityBean resetTheAccess(String parentFormName,
			String parentType) {
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		AccessibilityBean access = new AccessibilityBean();
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}

	public AccessibilityBean getGroupAccess(String parentFormName,
			String groupName, String parentType, String protectedData)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		AccessibilityBean access = getService().findAccessibilityByGroupName(
				groupName, protectedData);
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}

	public AccessibilityBean getUserAccess(String parentFormName,
			String userLoginName, String parentType, String protectedData)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		UserBean user = (UserBean) wctx.getSession().getAttribute("user");
		if (user == null) {
			return null;
		}
		AccessibilityBean access = getService().findAccessibilityByUserLoginName(
				userLoginName, protectedData);
		DynaValidatorForm accessForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute(parentFormName));
		if (accessForm == null) {
			return null;
		}
		if (parentType.equalsIgnoreCase("sample")) {
			SampleBean sampleBean = (SampleBean) accessForm.get("sampleBean");
			sampleBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("protocol")) {
			ProtocolBean protocolBean = (ProtocolBean) accessForm
					.get("protocol");
			protocolBean.setTheAccess(access);
		} else if (parentType.equalsIgnoreCase("publication")) {
			PublicationBean publicationBean = (PublicationBean) accessForm
					.get("publication");
			publicationBean.setTheAccess(access);
		}
		return access;
	}
}
