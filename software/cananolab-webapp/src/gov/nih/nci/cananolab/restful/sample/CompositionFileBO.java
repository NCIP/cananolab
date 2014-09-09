package gov.nih.nci.cananolab.restful.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.composition.CompositionBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.util.CompositionUtil;
import gov.nih.nci.cananolab.restful.util.PropertyUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFileBean;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.CompositionServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompositionFileBO extends BaseAnnotationBO{
	
	public List<String> create(SimpleFileBean bean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FileBean theFile = transferFileBean(bean);
		Boolean newFile = true;

		msgs = validateFileBean(request, theFile);
		if(msgs.size()>0){
			return msgs;
		}
		CompositionService service = this.setServicesInSession(request);
		// restore previously uploaded file from session.
		restoreUploadedFile(request, theFile);

		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/" + "compositionFile";

		theFile.setupDomainFile(internalUriPath, user.getLoginName());
		if (theFile.getDomainFile().getId() != null) {
			newFile = true;
		}
		
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
		if(newFileData!=null){
			theFile.setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
			theFile.getDomainFile().setUri(Constants.FOLDER_PARTICLE + '/'
					+ sampleBean.getDomain().getName() + '/' + "compositionFile"+ "/" + timestamp + "_"
					+ theFile.getDomainFile().getName());
		}
		
		service.saveCompositionFile(sampleBean, theFile);
		// retract from public if updating an existing public record and not
		// curator
		if (!newFile && !user.isCurator() && sampleBean.getPublicStatus()) {
//			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
//					.toString(), sampleBean.getDomain().getName(), "sample");
//			ActionMessage msg = null;
//			msg = new ActionMessage("message.updateSample.retractFromPublic");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
			
			}
		msgs.add("success");
		// to preselect composition file after returning to the summary page
		request.getSession().setAttribute("tab", "4");
		return msgs;
	}

	private FileBean transferFileBean(SimpleFileBean bean) {
		// TODO Auto-generated method stub
		FileBean fileBean = new FileBean();
		File file = new File();
		
		file.setDescription(bean.getDescription());
		if(bean.getId()!=null){
			file.setId(bean.getId());
			file.setCreatedBy(bean.getCreatedBy());
			file.setCreatedDate(bean.getCreatedDate());
		}
		file.setType(bean.getType());
		file.setTitle(bean.getTitle());
		file.setUri(bean.getUri());
		file.setUriExternal(bean.getUriExternal());
		fileBean.setExternalUrl(bean.getExternalUrl());
		fileBean.setKeywordsStr(bean.getKeywordsStr());
		fileBean.setDomainFile(file);
		return fileBean;
	}

	public List<String> delete(SimpleFileBean bean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		CompositionBO compBO = new CompositionBO();
		CompositionForm form = new CompositionForm();
		form.setSampleId(bean.getSampleId());
		CompositionBean comp = compBO.summaryView(form, request);
		FileBean fileBean = transferFileBean(bean);
		CompositionService compService = this.setServicesInSession(request);
		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		compService.deleteCompositionFile(sampleBean.getDomain()
				.getSampleComposition(), fileBean.getDomainFile());
		compService.removeAccesses(comp.getDomain(), fileBean
				.getDomainFile());
		// retract from public if updating an existing public record and not
		// curator
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		if (!user.isCurator() && sampleBean.getPublicStatus()) {
//			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
//					.toString(), sampleBean.getDomain().getName(), "sample");
//			ActionMessage msg = null;
//			msg = new ActionMessage("message.updateSample.retractFromPublic");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
		}

		msgs.add(PropertyUtil.getProperty("sample","message.deleteCompositionFile"));

		return msgs;
	}

	public Map<String, Object> setupNew(HttpServletRequest request)
			throws Exception {
		this.setServicesInSession(request);
		setLookups(request);
		return CompositionUtil.reformatLocalSearchDropdownsInSessionForCompositionFile(request.getSession());

	}

	public SimpleFileBean setupUpdate(String sampleId, String fileId,
			HttpServletRequest request)
			throws Exception {
		fileId = super.validateId(request, "dataId");
		CompositionService compService = (CompositionService) request
				.getSession().getAttribute("compositionService");
		FileBean fileBean = compService.findFileById(fileId);
//		CompositionBean compBean = form.getComp();
//		compBean.setTheFile(fileBean);
		request.getSession().setAttribute("sampleId", sampleId);
		SimpleFileBean simpleBean = new SimpleFileBean();
		simpleBean.transferSimpleFileBean(fileBean, request);
		return simpleBean;
	}

	/**
	 * Handle input request, when validation failed this handler will be called
	 * too.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void input(CompositionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.setServicesInSession(request);
		setupSampleById(form.getSampleId(), request);
		this.setLookups(request);
		CompositionBean comp = form.getComp();
		escapeXmlForFileUri(comp.getTheFile());
		InitCompositionSetup.getInstance().persistCompositionFileDropdowns(
				request, comp.getTheFile());

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = comp.getTheFile();
		preserveUploadedFile(request, theFile, "compositionFile");

		//return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
	}

	private CompositionService setServicesInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		CompositionService compService = new CompositionServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("compositionService", compService);
		SampleService sampleService = new SampleServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("sampleService", sampleService);
		return compService;
	}
}

