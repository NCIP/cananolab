/**
 *
 */
package gov.nih.nci.cananolab.ui.publication;

import gov.nih.nci.cananolab.dto.common.AccessibilityBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author tanq
 *
 */
public class PublicationForm extends DynaValidatorForm {
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		PublicationForm form = (PublicationForm) request.getSession()
				.getAttribute("publicationForm");
		if (form != null) {
			String dispatch = (String) form.get("dispatch");
			// if addAuthor and validator fail, do not reset
			if (dispatch != null && !dispatch.equalsIgnoreCase("addAuthor")
					&& !dispatch.equalsIgnoreCase("create")) {
				PublicationBean pubBean = (PublicationBean) form
						.get("publication");
				pubBean.setResearchAreas(new String[0]);
				pubBean.setGroupAccesses(new ArrayList<AccessibilityBean>());
				pubBean.setUserAccesses(new ArrayList<AccessibilityBean>());
				pubBean.setSampleNames(new String[0]);
				this.set("publication", pubBean);
			}
		}
	}
}
