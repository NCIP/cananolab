package gov.nih.nci.cananolab.ui.core;

import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.dto.common.LabFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.ParticleDataLinkBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.NanoparticleEntityBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCompositionService;
import gov.nih.nci.cananolab.service.particle.NanoparticleSampleService;
import gov.nih.nci.cananolab.ui.particle.InitNanoparticleSetup;
import gov.nih.nci.cananolab.ui.report.InitReportSetup;
import gov.nih.nci.cananolab.ui.security.InitSecuritySetup;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.PropertyReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * Base action for all annotation actions
 * 
 * @author pansu
 * 
 */
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
		InitNanoparticleSetup.getInstance().getOtherParticleNames(
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

	public Map<String, SortedSet<ParticleDataLinkBean>> setupDataTree(
			DynaValidatorForm theForm, HttpServletRequest request)
			throws Exception {
		request.setAttribute("updateDataTree", "true");
		String particleId = request.getParameter("particleId");
		if (particleId == null) {
			particleId = theForm.getString("particleId");
		}
		InitReportSetup.getInstance().getReportCategories(request);
		return InitNanoparticleSetup.getInstance().getDataTree(particleId,
				request);
	}

	public ActionForward setupDeleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String submitType = request.getParameter("submitType");
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		Map<String, SortedSet<ParticleDataLinkBean>> dataTree = setupDataTree(
				theForm, request);
		SortedSet<ParticleDataLinkBean> dataToDelete = dataTree.get(submitType);
		request.getSession().setAttribute("actionName",
				dataToDelete.first().getDataLink());
		request.getSession().setAttribute("dataToDelete", dataToDelete);
		return mapping.findForward("annotationDeleteView");
	}

	// check for cases where delete can't happen
	protected boolean checkDelete(HttpServletRequest request,
			ActionMessages msgs, String id) throws Exception {
		return true;
	}

	public ActionForward deleteAll(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DynaValidatorForm theForm = (DynaValidatorForm) form;
		String submitType = request.getParameter("submitType");
		String className = InitSetup.getInstance().getObjectName(submitType,
				request.getSession().getServletContext());
		String fullClassName = ClassUtils.getFullClass(className)
				.getCanonicalName();

		String[] dataIds = (String[]) theForm.get("idsToDelete");
		NanoparticleSampleService sampleService = new NanoparticleSampleService();
		ActionMessages msgs = new ActionMessages();
		for (String id : dataIds) {
			if (!checkDelete(request, msgs, id)) {
				return mapping.findForward("annotationDeleteView");
			}
			sampleService.deleteAnnotationById(fullClassName, new Long(id));
		}
		setupDataTree(theForm, request);
		ActionMessage msg = new ActionMessage("message.deleteAnnotations",
				submitType);
		msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
		saveMessages(request, msgs);
		return mapping.findForward("success");
	}

	/**
	 * Download action to handle file downloading and viewing
	 * 
	 * @param
	 * @return
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileId = request.getParameter("fileId");
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		FileService service = new FileService();
		LabFileBean fileBean = service.findFile(fileId, user);
		if (fileBean.getDomainFile().getUriExternal()) {
			response.sendRedirect(fileBean.getDomainFile().getUri());
			return null;
		}
		String fileRoot = PropertyReader.getProperty(
				CaNanoLabConstants.FILEUPLOAD_PROPERTY, "fileRepositoryDir");
		File dFile = new File(fileRoot + File.separator
				+ fileBean.getDomainFile().getUri());
		if (dFile.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="
					+ fileBean.getDomainFile().getName());
			response.setHeader("cache-control", "Private");

			java.io.InputStream in = new FileInputStream(dFile);
			java.io.OutputStream out = response.getOutputStream();

			byte[] bytes = new byte[32768];

			int numRead = 0;
			while ((numRead = in.read(bytes)) > 0) {
				out.write(bytes, 0, numRead);
			}
			out.close();
		} else {
			ActionMessages msgs = new ActionMessages();
			ActionMessage msg = new ActionMessage("error.noFile");
			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
			this.saveErrors(request, msgs);
			return mapping.findForward("fileMessage");
		}
		return null;
	}

	protected NanoparticleSample[] prepareCopy(HttpServletRequest request,
			DynaValidatorForm theForm) throws Exception {
		String[] otherParticles = (String[]) theForm.get("otherParticles");
		if (otherParticles.length == 0) {
			return null;
		}
		NanoparticleSample[] particleSamples = new NanoparticleSample[otherParticles.length];
		NanoparticleSampleService sampleService = new NanoparticleSampleService();
		int i = 0;
		for (String other : otherParticles) {
			NanoparticleSample particleSample = sampleService
					.findNanoparticleSampleByName(other);
			particleSamples[i] = particleSample;
			i++;
		}
		// retrieve file contents

		// FileService fileService = new FileService();
		// for (DerivedBioAssayDataBean file : entityBean.getFiles()) {
		// byte[] content = fileService.getFileContent(new Long(file.getId()));
		// file.setFileContent(content);
		// }
		//
		// NanoparticleSampleService service = new NanoparticleSampleService();
		// UserBean user = (UserBean) request.getSession().getAttribute("user");
		// int i = 0;
		// for (String particleName : otherParticles) {
		// NanoparticleEntityBean newEntityBean = entityBean.copy();
		// // overwrite particle
		// ParticleBean otherParticle = service.findNanoparticleSampleByName(
		// particleName, user);
		// newrBean.setParticle(otherParticle);
		// // reset view title
		// String timeStamp = StringUtils.convertDateToString(new Date(),
		// "MMddyyHHmmssSSS");
		// String autoTitle =
		// CaNanoLabConstants.AUTO_COPY_CHARACTERIZATION_VIEW_TITLE_PREFIX
		// + timeStamp;
		//
		// newCharBean.setViewTitle(autoTitle);
		// List<DerivedBioAssayDataBean> dataList = newCharBean
		// .getDerivedBioAssayDataList();
		// // replace particleName in path and uri with new particleName
		// for (DerivedBioAssayDataBean derivedBioAssayData : dataList) {
		// String origUri = derivedBioAssayData.getUri();
		// if (origUri != null)
		// derivedBioAssayData.setUri(origUri.replace(particle
		// .getSampleName(), particleName));
		// }
		// charBeans[i] = newCharBean;
		// i++;
		// }
		return particleSamples;
	}

}
