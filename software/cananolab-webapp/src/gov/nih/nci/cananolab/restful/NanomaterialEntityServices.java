package gov.nih.nci.cananolab.restful;

import gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean;
import gov.nih.nci.cananolab.restful.sample.NanomaterialEntityBO;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleComposingElementBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleNanomaterialEntityBean;
import gov.nih.nci.cananolab.service.security.UserBean;

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

@Path("/nanomaterialEntity")
public class NanomaterialEntityServices {
	
private Logger logger = Logger.getLogger(NanomaterialEntityServices.class);
	
	@Inject
	ApplicationContext applicationContext;
	
	@GET
	@Path("/setup")
	@Produces ("application/json")
    public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) applicationContext.getBean("nanomaterialEntityBO");
			Map<String, Object> dropdownMap = nanomaterialEntityBO.setupNew(sampleId, httpRequest);
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
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) applicationContext.getBean("nanomaterialEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			NanomaterialEntityBean nanoBean = nanomaterialEntityBO.setupUpdate(sampleId, dataId, httpRequest);
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			
			nano.transferNanoMaterialEntityBeanToSimple(nanoBean, httpRequest);
			return Response.ok(nano).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the NanoMaterial Entity" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveComposingElement")
	@Produces ("application/json")
    public Response saveComposingElement(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) applicationContext.getBean("nanomaterialEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			NanomaterialEntityBean bean = nanomaterialEntityBO.saveComposingElement(nanoBean, httpRequest);
			
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			
			nano.transferNanoMaterialEntityBeanToSimple(bean, httpRequest);
			return Response.ok(nano).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the composing element" + e.getMessage())).build();

		}
	}
	
	@GET
	@Path("/removeComposingElement")
	@Produces ("application/json")
    public Response removeComposingElement(@Context HttpServletRequest httpRequest) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) applicationContext.getBean("nanomaterialEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			SimpleNanomaterialEntityBean nanoBean = new SimpleNanomaterialEntityBean();
			SimpleComposingElementBean comp= new SimpleComposingElementBean();
			nanoBean.setType("fullerene");
			nanoBean.setSampleId("20917506");
			comp.setType("shell");
			comp.setName("shell");
			nanoBean.setSimpleCompBean(comp);
			NanomaterialEntityBean bean = nanomaterialEntityBO.removeComposingElement(nanoBean, httpRequest);
			
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			
			nano.transferNanoMaterialEntityBeanToSimple(bean, httpRequest);
			return Response.ok(nano).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while removing the composing element" + e.getMessage())).build();

		}
	}
	
	@POST
	@Path("/saveFile")
	@Produces ("application/json")
    public Response saveFile(@Context HttpServletRequest httpRequest, SimpleNanomaterialEntityBean nanoBean) {
				
		try { 
			NanomaterialEntityBO nanomaterialEntityBO = 
					(NanomaterialEntityBO) applicationContext.getBean("nanomaterialEntityBO");
			UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
			if (user == null) 
				return Response.status(Response.Status.UNAUTHORIZED)
						.entity("Session expired").build();
			
			
			NanomaterialEntityBean bean = nanomaterialEntityBO.saveFile(nanoBean, httpRequest);
			
			SimpleNanomaterialEntityBean nano = new SimpleNanomaterialEntityBean();
			
			nano.transferNanoMaterialEntityBeanToSimple(bean, httpRequest);
			return Response.ok(nano).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the file" + e.getMessage())).build();

		}
	}
}

