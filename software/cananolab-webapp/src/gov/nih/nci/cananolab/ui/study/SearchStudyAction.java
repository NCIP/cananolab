package gov.nih.nci.cananolab.ui.study;

/**
 * This class sets up the search study.
 *
 * @author lethai
 */
import gov.nih.nci.cananolab.dto.common.StudyBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.study.StudyService;
import gov.nih.nci.cananolab.service.study.impl.StudyServiceLocalImpl;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class SearchStudyAction extends AbstractDispatchAction {
	// logger
	// private static Logger logger = Logger.getLogger(StudyAction.class);

	public ActionForward setup(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaActionForm theForm = (DynaActionForm) form;
		theForm.set("isAnimalModel", "yes");
		return mapping.getInputForward();
	}

	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaActionForm theForm = (DynaActionForm) form;
		List<StudyBean> studyBeans = new ArrayList<StudyBean>();
		
		String studyName =(String) theForm.get("studyName");
		String studyNameOperand = (String) theForm.get("studyNameOperand");
		if (studyNameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyName)) {
			studyName = "*" + studyName + "*";
		}
		System.out.println("studyName: " + studyName);
		String studyPointOfContact = (String) theForm
		.get("studyPointOfContact");
		// strip wildcards at either end if entered by user
		studyPointOfContact = StringUtils.stripWildcards(studyPointOfContact);
		String pocOperand = (String) theForm.get("pocOperand");
		if (pocOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyPointOfContact)) {
			studyPointOfContact = "*" + studyPointOfContact + "*";
		}
		System.out.println("studyPOCName: " + studyPointOfContact
				);
		String sampleName = (String) theForm.get("sampleName");
		// strip wildcards at either end if entered by user
		sampleName = StringUtils.stripWildcards(sampleName);
		String sampleNameOperand = (String) theForm.get("sampleNameOperand");
		if (sampleNameOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(sampleName)) {
			sampleName = "*" + sampleName + "*";
		}
		
		System.out.println("sampleName: " + sampleName);
		
		String disease = (String) theForm.get("disease");
		// strip wildcards at either end if entered by user
		disease = StringUtils.stripWildcards(disease);
		String diseaseOperand = (String) theForm.get("diseaseOperand");
		if (diseaseOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(disease)) {
			disease = "*" + disease + "*";
		}
		
		System.out.println("disease: " + disease);
		String studyOwner = (String) theForm.get("studyOwner");
		// strip wildcards at either end if entered by user
		studyOwner = StringUtils.stripWildcards(studyOwner);
		String ownerOperand = (String) theForm.get("ownerOperand");
		if (ownerOperand.equals(Constants.STRING_OPERAND_CONTAINS)
				&& !StringUtils.isEmpty(studyOwner)) {
			studyOwner = "*" + studyOwner + "*";
		}
		
		System.out.println("studyOwner: " + studyOwner);
		
		StudyService studyService = setServiceInSession(request);
		List<String> studyIds = studyService.findStudyIdsBy();
		
		for (String id : studyIds) {
			// empty studyBean that only has id;
			StudyBean studyBean = new StudyBean(id);
			studyBeans.add(studyBean);
		}

		StudyBean study1 = new StudyBean();
		study1.setName("MIT_KELLY");
		study1.setTitle("in vitro profiling of nanoparticle libraries");
		study1.setStudySample(new SampleBean());
		study1.setOwnerName("michal");
		study1.setPointOfContact("MIT_MGH (Stanley Y Shaw)");

		StudyBean study2 = new StudyBean();
		study1.setName("Efficacy of nanoparticle Sample");
		//SampleBean studySample = new SampleBean();
		//studySample.se
		study2.setStudySample(new SampleBean());
		study2.setOwnerName("Guest2");
		study2.setPointOfContact("Guest2");
		List<StudyBean> studyBeansPerPage = new ArrayList<StudyBean>();
		studyBeansPerPage.add(study1);
		studyBeansPerPage.add(study2);
		request.setAttribute("studies", studyBeansPerPage);
		// get the total size of collection , required for display tag to
		// get the pagination to work
		request.setAttribute("resultSize", new Integer(2));
		return mapping.findForward("searchResult");
	}
	
	private StudyService setServiceInSession(HttpServletRequest request)
		throws Exception {
	SecurityService securityService = super
			.getSecurityServiceFromSession(request);
	StudyService studyService = new StudyServiceLocalImpl(
			securityService);
	request.getSession().setAttribute("studyService", studyService);
	return studyService;
	}
}
