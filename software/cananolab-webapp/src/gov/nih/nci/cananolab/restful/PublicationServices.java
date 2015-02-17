package gov.nih.nci.cananolab.restful;

import java.util.List;
import java.util.Map;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.DataReviewStatusBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.publication.PublicationBO;
import gov.nih.nci.cananolab.restful.publication.PublicationManager;
import gov.nih.nci.cananolab.restful.publication.SearchPublicationBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimplePublicationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimplePublicationWithSamplesBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleSubmitPublicationBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.ui.form.PublicationForm;
import gov.nih.nci.cananolab.ui.form.SearchPublicationForm;


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

@Path("/publication")
public class PublicationServices {

private Logger logger = Logger.getLogger(PublicationServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	@GET
	@Path("/summaryView")
	@Produces ("application/json")
	 public Response view(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

		 PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");

		 PublicationSummaryViewBean pubBean = publicationBO.summaryView(sampleId, httpRequest);
			SimplePublicationSummaryViewBean view = new SimplePublicationSummaryViewBean();
			view.transferPublicationBeanForSummaryView(pubBean);
			
			// return Response.ok(view).build();
			return Response.ok(view).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing for publication " + e.getMessage())).build();
		}
	}

	@GET
	@Path("/download")
	@Produces ("application/pdf")
	 public Response download(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse, 
	    		@DefaultValue("") @QueryParam("fileId") String fileId){
		
		try { 

			 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			String result = publicationBO.download(fileId, httpRequest, httpResponse);
		
			return Response.ok(result).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while downloading the file" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/summaryPrint")
	@Produces ("application/json")
	 public Response summaryPrint(@Context HttpServletRequest httpRequest,  
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

			 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			 PublicationSummaryViewBean summaryBean = publicationBO.summaryView(sampleId, httpRequest);
			 SimplePublicationSummaryViewBean view = new SimplePublicationSummaryViewBean();
				view.transferPublicationBeanForSummaryView(summaryBean);
		
			
			return Response.ok(view).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while printing the file" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/summaryExport")
	@Produces ("application/vnd.ms-excel")
	 public Response summaryExport(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("type") String type){
		
		try { 
			
				 PublicationBO publicationBO = 
						(PublicationBO) applicationContext.getBean("publicationBO");

			 String result = publicationBO.summaryExport(sampleId, type, httpRequest, httpResponse);
				
			return Response.ok("").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while exporting the file" + e.getMessage())).build();

		}
	}
	@GET
	@Path("/summaryEdit")
	@Produces ("application/json")
	 public Response summaryEdit(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 

		 PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");

		 PublicationForm form = new PublicationForm();
		 form.setSampleId(sampleId);
		 SimplePublicationSummaryViewBean pubBean = publicationBO.summaryEdit(sampleId, httpRequest);
			
			// return Response.ok(view).build();
			return Response.ok(pubBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the publication results" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest) {
				
		try { 
			SearchPublicationBO searchPublicationBO = 
					(SearchPublicationBO) applicationContext.getBean("searchPublicationBO");
			Map<String, Object> dropdownMap = searchPublicationBO.setup(httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}

	@POST
	@Path("/searchPublication")
	@Produces ("application/json")
	public Response searchPublication(@Context HttpServletRequest httpRequest, SearchPublicationForm searchForm ) {
	
		try {
			SearchPublicationBO searchPublicationBO = 
					(SearchPublicationBO) applicationContext.getBean("searchPublicationBO");
			
						
			List results = searchPublicationBO.search(searchForm, httpRequest);
			
			Object result = results.get(0);
			if (result instanceof String) {
				return Response.status(Response.Status.NOT_FOUND).entity(result).build();
			} else
			return Response.ok(results).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while searching for publication " + e.getMessage())).build();
		}
	}
	
	
	@GET
	@Path("/edit")
	@Produces ("application/json")
	 public Response edit(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("publicationId") String publicationId,@DefaultValue("") @QueryParam("sampleId") String sampleId){
		
		try { 
			 
		 PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");

		 UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
		 SimpleSubmitPublicationBean view = publicationBO.setupUpdate(publicationId,sampleId, httpRequest);
			
			List<String> errors = view.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(view).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing for publication " + e.getMessage())).build();

		}
	}

	
	@GET
	@Path("/getSamples")
	@Produces ("application/json")
    public Response getSamples(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("searchStr") String searchStr) {
				
		try { 
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			String[] sampleList = pubBO.getMatchedSampleNames(searchStr, httpRequest);
			return Response.ok(sampleList).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(CommonUtil.wrapErrorMessageInList("Problem getting all sample names for publication submission"+ e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/submitPublication")
	@Produces ("application/json")
	public Response submitPublication(@Context HttpServletRequest httpRequest, SimpleSubmitPublicationBean form) {
	
		try {
			
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			List<String> msgs = pubBO.create(form, httpRequest);
			 
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

					
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the publication " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/searchById")
	@Produces("application/json")
	 public Response searchById(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("id") String id, @QueryParam("type") String type){
		
		PublicationManager pubManager = 
				(PublicationManager) applicationContext.getBean("publicationManager");

		try {
			SimplePublicationWithSamplesBean result = pubManager.searchPublicationById(httpRequest, id, type);
			
			return (result.getErrors().size() > 0) ?
					Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(result.getErrors()).build()
						:
						Response.ok(result).build();
		} 

		catch (Exception ioe) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ioe.getMessage()).build();
		}
	}	

	@GET
	@Path("/getPubmedPublication")
	@Produces ("application/json")
    public Response getPubmedPublication(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("pubmedId") String pubmedId) {
				
		try { 
			PublicationManager pubManager = 
					 (PublicationManager) applicationContext.getBean("publicationManager");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			String value = pubManager.getExistingPubMedPublication(pubmedId, httpRequest);
			return (value == null) ?
					Response.status(Response.Status.NOT_FOUND).entity("No pub found").build()
					:
					Response.ok(value).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(CommonUtil.wrapErrorMessageInList("Problem getting the publication Id"+ e.getMessage())).build();
		}
	}
	@GET
	@Path("/retrievePubMedInfo")
	@Produces ("application/json")
    public Response retrievePubMedInfo(@Context HttpServletRequest httpRequest,
    		@DefaultValue("") @QueryParam("pubmedId") String pubmedId) {
				
		try { 
			PublicationManager pubManager = 
					 (PublicationManager) applicationContext.getBean("publicationManager");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			PublicationForm form = new PublicationForm();
			PublicationBean pubBean = new PublicationBean();
			Publication domainFile = new Publication();
			pubBean.setDomainFile(domainFile);
			form.setPublicationBean(pubBean);
			
			PublicationBean pBean = pubManager.retrievePubMedInfo(pubmedId, form, httpRequest);
			return Response.ok(pBean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			// return Response.ok(dropdownMap).build();
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(CommonUtil.wrapErrorMessageInList("Problem retrieving the pubmed information"+ e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/saveAccess")
	@Produces ("application/json")
	public Response saveAccess(@Context HttpServletRequest httpRequest, SimpleSubmitPublicationBean bean) {
	
		try {
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
		
			SimpleSubmitPublicationBean view = pubBO.saveAccess(bean, httpRequest);
			
			List<String> errors = view.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(view).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();

			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the access to publication " + e.getMessage())).build();
		}
	}
	@POST
	@Path("/deletePublication")
	@Produces ("application/json")
	public Response deletePublication(@Context HttpServletRequest httpRequest, SimpleSubmitPublicationBean form) {
	
		try {
			
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			List<String> msgs = pubBO.delete(form, httpRequest);
			 
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the publication " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/deleteAccess")
	@Produces ("application/json")
	public Response deleteAccess(@Context HttpServletRequest httpRequest, SimpleSubmitPublicationBean form) {
	
		try {
			
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleSubmitPublicationBean bean = pubBO.deleteAccess(form, httpRequest);
			 
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();


		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the access " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/removeFromSample")
	@Produces ("application/json")
	public Response removeFromSample(@Context HttpServletRequest httpRequest, SimpleSubmitPublicationBean form) {
	
		try {
			
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimplePublicationSummaryViewBean bean = pubBO.removeFromSample(form, httpRequest);
			 
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();


		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the access " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/submitForReview")
	@Produces ("application/json")
	public Response submitForReview(@Context HttpServletRequest httpRequest, DataReviewStatusBean dataReviewStatusBean) {
	
		try {
			
			PublicationBO publicationBO = 
					(PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			String result = publicationBO.submitForReview(httpRequest, dataReviewStatusBean);
			 
			return Response.ok(result).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the access " + e.getMessage())).build();
		}
	}
	
	@GET
	@Path("/searchByIdImage")
	@Produces("text/plain")
	 public Response searchByIdImage(@Context HttpServletRequest httpRequest, 
	    		@DefaultValue("") @QueryParam("id") String id, @QueryParam("type") String type){
		
		PublicationManager pubManager = 
				(PublicationManager) applicationContext.getBean("publicationManager");

		//String fileRoot = PropertyUtils.getProperty(Constants.CANANOLAB_PROPERTY, "fileRepositoryDir");
		
		//java.io.File fileSuccess = new java.io.File(fileRoot + java.io.File.separator +"appLogo-nanolab.gif");
		//java.io.File fileError = new java.io.File(fileRoot + java.io.File.separator +"shim.gif");
		try {
			SimplePublicationWithSamplesBean result = pubManager.searchPublicationById(httpRequest, id, type);
			
			
			return (result.getErrors().size() > 0) ?
					Response.ok("/caNanoLab/images/doi-transparent.png").build()
						:
						Response.ok("/caNanoLab/images/canano_logo_mini.jpg").build();
		} 

		catch (Exception e) {
			return Response.ok("/caNanoLab/images/doi-transparent.png").build();
		}
	}	

	@GET
	@Path("/deletePublicationById")
	@Produces ("application/json")
	public Response deletePublicationById(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("publicationId") String publicationId) {
	
		try {
			
			PublicationBO pubBO = 
					 (PublicationBO) applicationContext.getBean("publicationBO");
			
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			List<String> msgs = pubBO.deletePublicationById(publicationId, httpRequest);
			 
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the publication " + e.getMessage())).build();
		}
	}
	
	
}
	
