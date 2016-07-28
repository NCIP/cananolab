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
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.sample.CompositionService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.ui.form.CompositionForm;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component("compositionFileBO")
public class CompositionFileBO extends BaseAnnotationBO
{
	@Autowired
	private CompositionService compositionService;

	@Autowired
	private CurationService curationServiceDAO;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SpringSecurityAclService springSecurityAclService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public List<String> create(SimpleFileBean bean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FileBean theFile = transferFileBean(bean);
		Boolean newFile = true;

		msgs = validateFileBean(request, msgs, theFile);
		if(msgs.size()>0){
			return msgs;
		}
		// restore previously uploaded file from session.
		//restoreUploadedFile(request, theFile);

		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		String internalUriPath = Constants.FOLDER_PARTICLE + "/"
				+ sampleBean.getDomain().getName() + "/" + "compositionFile";

		theFile.setupDomainFile(internalUriPath, SpringSecurityUtil.getLoggedInUserName());
		if (theFile.getDomainFile().getId() != null) {
			newFile = true;
		}
		
		String timestamp = DateUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS");
		byte[] newFileData = (byte[]) request.getSession().getAttribute("newFileData");
			if(!theFile.getDomainFile().getUriExternal()){
			if(newFileData!=null){
				theFile.setNewFileData((byte[]) request.getSession().getAttribute("newFileData"));
				theFile.getDomainFile().setUri(Constants.FOLDER_PARTICLE + '/'
						+ sampleBean.getDomain().getName() + '/' + "compositionFile"+ "/" + timestamp + "_"
						+ theFile.getDomainFile().getName());
			}else if(theFile.getDomainFile().getId()!=null){
				theFile.getDomainFile().setUri(theFile.getDomainFile().getName());
			}else{
				theFile.getDomainFile().setUri(null);
			}
		}
		compositionService.saveCompositionFile(sampleBean, theFile);
		// retract from public if updating an existing public record and not
		// curator
		if (!newFile && !SpringSecurityUtil.getPrincipal().isCurator() && 
			springSecurityAclService.checkObjectPublic(sampleBean.getDomain().getId(), SecureClassesEnum.SAMPLE.getClazz()))
		{
//			retractFromPublic(theForm, request, sampleBean.getDomain().getId()
//					.toString(), sampleBean.getDomain().getName(), "sample");
//			ActionMessage msg = null;
//			msg = new ActionMessage("message.updateSample.retractFromPublic");
//			msgs.add(ActionMessages.GLOBAL_MESSAGE, msg);
//			saveMessages(request, msgs);
			
			}
		msgs.add("success");
		 request.getSession().removeAttribute("newFileData");
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
		SampleBean sampleBean = setupSampleById(bean.getSampleId(), request);
		compositionService.deleteCompositionFile(sampleBean.getDomain()
				.getSampleComposition(), fileBean.getDomainFile());
		compositionService.removeAccesses(comp.getDomain(), fileBean
				.getDomainFile());
		// retract from public if updating an existing public record and not curator
		if (!SpringSecurityUtil.getPrincipal().isCurator() && 
			springSecurityAclService.checkObjectPublic(sampleBean.getDomain().getId(), SecureClassesEnum.SAMPLE.getClazz()))
		{
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
		setLookups(request);
		return CompositionUtil.reformatLocalSearchDropdownsInSessionForCompositionFile(request.getSession());

	}

	public SimpleFileBean setupUpdate(String sampleId, String fileId,
			HttpServletRequest request)
			throws Exception {
		fileId = super.validateId(request, "dataId");
		FileBean fileBean = compositionService.findFileById(fileId);
//		CompositionBean compBean = form.getComp();
//		compBean.setTheFile(fileBean);
		request.getSession().setAttribute("sampleId", sampleId);
		SimpleFileBean simpleBean = new SimpleFileBean();
		fileBean.setPublicStatus(springSecurityAclService.checkObjectPublic(Long.valueOf(fileId), SecureClassesEnum.FILE.getClazz()));
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
		setupSampleById(form.getSampleId(), request);
		this.setLookups(request);
		CompositionBean comp = form.getComp();
		escapeXmlForFileUri(comp.getTheFile());
		InitCompositionSetup.getInstance().persistCompositionFileDropdowns(
				request, comp.getTheFile());

		// Save uploaded data in session to avoid asking user to upload again.
		FileBean theFile = comp.getTheFile();
		//preserveUploadedFile(request, theFile, "compositionFile");

		//return mapping.findForward("inputForm");
	}

	private void setLookups(HttpServletRequest request) throws Exception {
		InitSampleSetup.getInstance().setSharedDropdowns(request);
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return this.curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return this.sampleService;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

	@Override
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	
}

