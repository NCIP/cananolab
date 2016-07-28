/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.restful.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;

@Component("pointOfContactManager")
public class PointOfContactManager
{
	@Autowired
	private SampleServiceHelper sampleServiceHelper;

	public PointOfContactBean getPointOfContactById(HttpServletRequest request, String id,
			Boolean primaryStatus) throws Exception {
//		DefaultWebContextBuilder dwcb = new DefaultWebContextBuilder();
//		org.directwebremoting.WebContext webContext = dwcb.get();
//		//HttpServletRequest request = webContext.getHttpServletRequest();
//		UserBean user = (UserBean) request.getSession().getAttribute("user");
//		if (user == null) {
//			return null;
//		}
//		PointOfContactBean poc = getService(request).findPointOfContactById(id);
//		poc.setPrimaryStatus(primaryStatus);
//		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("sampleForm"));
//		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
//		sample.setThePOC(poc);
//		return poc;
		
		return null;
	}

//	public PointOfContactBean resetThePointOfContact(HttpServletRequest request) throws Exception {
//		DynaValidatorForm sampleForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("sampleForm"));
//		if (sampleForm == null) {
//			return null;
//		}
//		SampleBean sample = (SampleBean) sampleForm.get("sampleBean");
//		PointOfContactBean poc = new PointOfContactBean();
//		sample.setThePOC(poc);
//		// if primary POC already exists, the POC is secondary
//		if (sample.getPrimaryPOCBean().getDomain().getId() != null) {
//			poc.setPrimaryStatus(false);
//		}
//		return poc;
//	}

	public Organization getOrganizationByName(HttpServletRequest request, String name) throws Exception {
		System.out.println("Getting data for org: " + name);
		Organization org = sampleServiceHelper.findOrganizationByName(name);
		return org;
	}

	public PointOfContactBean getPointOfContactByNameAndOrg(HttpServletRequest request, String firstName,
			String lastName, String orgName) throws Exception {
		PointOfContact poc = sampleServiceHelper.findPointOfContactByNameAndOrg(firstName, lastName, orgName);
		if (poc != null) {
			return new PointOfContactBean(poc);
		} else {
			return null;
		}
	}
}
