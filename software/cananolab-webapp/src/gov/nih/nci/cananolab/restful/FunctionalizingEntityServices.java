package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.restful.context.SpringApplicationContext;
import gov.nih.nci.cananolab.restful.sample.FunctionalizingEntityBO;
import gov.nih.nci.cananolab.restful.sample.NanomaterialEntityBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.SimpleAdvacedSampleCompositionBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleFunctionalizingEntityBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleNanomaterialEntityBean;
import gov.nih.nci.cananolab.service.security.UserBean;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Path("/functionalizingEntity")
public class FunctionalizingEntityServices {

	private static final Logger logger = Logger.getLogger(FunctionalizingEntityServices.class);
		
//	@Inject
//	ApplicationContext applicationContext;
	private static final ApplicationContext applicationContext = ApplicationContextBO.getApplicationContextBO();

	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			Map<String, Object> dropdownMap = functionalizingEntity.setupNew(sampleId, httpRequest);
			return Response.ok(dropdownMap).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/edit")
	@Produces ("application/json")
    public Response edit(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleFunctionalizingEntityBean bean = functionalizingEntity.setupUpdate(sampleId, dataId, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the Functionalizing Entity" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveFunction")
	@Produces ("application/json")
    public Response saveFunction(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleFunctionalizingEntityBean bean = functionalizingEntity.saveFunction(funcBean, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the function" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveFile")
	@Produces ("application/json")
    public Response saveFile(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleFunctionalizingEntityBean bean = functionalizingEntity.saveFile(funcBean, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the File" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/submit")
	@Produces ("application/json")
    public Response submit(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			List<String> msgs = functionalizingEntity.create(funcBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the Functionalizing Entity" + e.getMessage())).build();

		}
	}
	
		
	@POST
	@Path("/removeFile")
	@Produces ("application/json")
    public Response removeFile(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleFunctionalizingEntityBean bean = functionalizingEntity.removeFile(funcBean, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the file" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/removeFunction")
	@Produces ("application/json")
    public Response removeFunction(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			SimpleFunctionalizingEntityBean bean = functionalizingEntity.removeFunction(funcBean, httpRequest);
			
			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() :
						Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the function" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/delete")
	@Produces ("application/json")
    public Response delete(@Context HttpServletRequest httpRequest, SimpleFunctionalizingEntityBean funcBean) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			List<String> msgs = functionalizingEntity.delete(funcBean, httpRequest);
			
			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

			
			
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the Functionalizing Entity" + e.getMessage())).build();

		}
	}
	@GET
	@Path("/viewDetails")
	@Produces ("application/json")
    public Response viewDetails(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId) {
				
		try { 
			FunctionalizingEntityBO functionalizingEntity = 
					(FunctionalizingEntityBO) applicationContext.getBean("functionalizingEntityBO");
			
			FunctionalizingEntityBean entityBean = functionalizingEntity.setupFunctionalizingEntityForAdvancedSearch(sampleId, dataId, httpRequest);
			
			SimpleAdvacedSampleCompositionBean bean = new SimpleAdvacedSampleCompositionBean();
			bean.transferFunctionalizingEntityForAdvancedSampleSearch(entityBean, httpRequest);
			
			return Response.ok(bean).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the Functionalizing Entity" + e.getMessage())).build();

		}
	}
}
