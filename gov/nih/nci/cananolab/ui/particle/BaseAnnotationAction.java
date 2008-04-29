package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.core.AbstractDispatchAction;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.validator.DynaValidatorForm;

public abstract class BaseAnnotationAction extends AbstractDispatchAction {
	public ParticleBean setupParticle(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		String particleId = request.getParameter("particleId");
		if (particleId == null) {
			particleId = theForm.getString("particleId");
		}
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");

		NanoparticleSampleService service = new NanoparticleSampleService();
		ParticleBean particleBean = service
				.findNanoparticleSampleById(particleId);
		request.setAttribute("theParticle", particleBean);
		InitNanoparticleSetup.getInstance().setOtherParticleNames(
				request,
				particleBean.getDomainParticleSample().getName(),
				particleBean.getDomainParticleSample().getSource()
						.getOrganizationName(), user);

		return particleBean;
	}

	public boolean loginRequired() {
		return false;
	}

	public boolean canUserExecute(UserBean user)
			throws CaNanoLabSecurityException {
		return InitSecuritySetup.getInstance().userHasCreatePrivilege(user,
				CaNanoLabConstants.CSM_PG_PARTICLE);
	}

	public void setupDataTree(DynaValidatorForm theForm,
			HttpServletRequest request) throws Exception {
		request.setAttribute("updateDataTree", "true");
		String particleId = request.getParameter("particleId");
		if (particleId == null) {
			particleId = theForm.getString("particleId");
		}
		InitNanoparticleSetup.getInstance().getDataTree(particleId, request);
	}
}
