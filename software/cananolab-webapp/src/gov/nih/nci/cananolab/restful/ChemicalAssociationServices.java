package gov.nih.nci.cananolab.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.restful.sample.ChemicalAssociationBO;
import gov.nih.nci.cananolab.restful.sample.CompositionManager;
import gov.nih.nci.cananolab.restful.util.CommonUtil;
import gov.nih.nci.cananolab.restful.view.edit.SimpleBaseCompositionEntityBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleChemicalAssociationBean;
import gov.nih.nci.cananolab.restful.view.edit.SimpleComposingElementBean;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.util.Constants;

@Path("/chemicalAssociation")
public class ChemicalAssociationServices
{
	private static final Logger logger = Logger.getLogger(ChemicalAssociationServices.class);

	@GET
	@Path("/setup")
	@Produces ("application/json")
	public Response setup(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			Map<String, Object> dropdownMap = chem.setupNew(sampleId, httpRequest);
			List<String> errors = (List<String>) dropdownMap.get("errors");
			return (errors == null || errors.size() == 0) ?
					Response.ok(dropdownMap).build() : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while setting up drop down lists" + e.getMessage())).build();
		}
	}

	@GET
	@Path("/edit")
	@Produces ("application/json")
	public Response edit(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("sampleId") String sampleId, @DefaultValue("") @QueryParam("dataId") String dataId)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleChemicalAssociationBean bean = chem.setupUpdate(sampleId, dataId, httpRequest);

			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while viewing the chemical association " + e.getMessage())).build();

		}
	}

	@POST
	@Path("/getAssociatedElementOptions")   
	@Produces ("application/json")
	public Response getAssociatedElementOptions(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("compositionType") String compositionType)
	{
		try { 
			CompositionManager comp = (CompositionManager) SpringApplicationContext.getBean(httpRequest, "compositionManager");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			List<BaseCompositionEntityBean> beans = comp.getAssociatedElementOptions(compositionType, httpRequest);
			List<SimpleBaseCompositionEntityBean> simpleBeans = new ArrayList<SimpleBaseCompositionEntityBean>();
			for(BaseCompositionEntityBean compBean : beans){
				SimpleBaseCompositionEntityBean simpleBean = new SimpleBaseCompositionEntityBean();
				simpleBean.trasferSimpleBaseCompositionBean(compBean);
				simpleBeans.add(simpleBean);
			}
			return Response.ok(simpleBeans).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while getting AssociatedElementOptions " + e.getMessage())).build();

		}
	}

	@POST
	@Path("/getComposingElementsByNanomaterialEntityId")   
	@Produces ("application/json")
	public Response getComposingElementsByNanomaterialEntityId(@Context HttpServletRequest httpRequest, @DefaultValue("") @QueryParam("id") String id)
	{
		try { 
			CompositionManager comp = (CompositionManager) SpringApplicationContext.getBean(httpRequest, "compositionManager");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			List<ComposingElementBean> beans = comp.getComposingElementsByNanomaterialEntityId(id, httpRequest);
			List<SimpleComposingElementBean> simpleBeans = new ArrayList<SimpleComposingElementBean>();
			for(ComposingElementBean compBean : beans){
				SimpleComposingElementBean simpleBean = new SimpleComposingElementBean();
				simpleBean.trasferSimpleComposingElementBean(compBean);
				simpleBeans.add(simpleBean);
			}
			return Response.ok(simpleBeans).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();

		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while getting ComposingElementsByNanomaterialEntityId" + e.getMessage())).build();

		}
	}

	@POST
	@Path("/saveFile")
	@Produces ("application/json")
	public Response saveFile(@Context HttpServletRequest httpRequest, SimpleChemicalAssociationBean chemBean)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleChemicalAssociationBean bean = chem.saveFile(chemBean, httpRequest);

			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while saving the File" + e.getMessage())).build();
		}
	}

	@POST
	@Path("/submit")
	@Produces ("application/json")
	public Response submit(@Context HttpServletRequest httpRequest, SimpleChemicalAssociationBean chemBean)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			List<String> msgs = chem.create(chemBean, httpRequest);

			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while submitting the chemical association" + e.getMessage())).build();
		}
	}

	@POST
	@Path("/removeFile")
	@Produces ("application/json")
	public Response removeFile(@Context HttpServletRequest httpRequest, SimpleChemicalAssociationBean chemBean)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			SimpleChemicalAssociationBean bean = chem.removeFile(chemBean, httpRequest);

			List<String> errors = bean.getErrors();
			return (errors == null || errors.size() == 0) ?
					Response.ok(bean).build() : Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the file" + e.getMessage())).build();

		}
	}

	@POST
	@Path("/delete")
	@Produces ("application/json")
	public Response delete(@Context HttpServletRequest httpRequest, SimpleChemicalAssociationBean chemBean)
	{
		try { 
			ChemicalAssociationBO chem = (ChemicalAssociationBO) SpringApplicationContext.getBean(httpRequest, "chemicalAssociationBO");
			if (!SpringSecurityUtil.isUserLoggedIn())
				return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.MSG_SESSION_INVALID).build();

			List<String> msgs = chem.delete(chemBean, httpRequest);

			return Response.ok(msgs).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization").build();
		}
		catch (Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(CommonUtil.wrapErrorMessageInList("Error while deleting the Chemical Association" + e.getMessage())).build();
		}
	}
	
}
