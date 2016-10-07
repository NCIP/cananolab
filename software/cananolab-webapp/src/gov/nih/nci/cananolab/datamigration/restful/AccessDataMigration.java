package gov.nih.nci.cananolab.datamigration.restful;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.datamigration.service.MigrateDataService;
import gov.nih.nci.cananolab.restful.SpringApplicationContext;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

@Path("/datamigration")
public class AccessDataMigration 
{
	private static final Logger logger = Logger.getLogger(AccessDataMigration.class);

	@GET
	@Path("/migrateuseraccounts")
	@Produces ("application/json")
	public Response migrateUserAccountsAndRoles(@Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all user accounts from CSM to Spring.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			migrateDataService.migrateUserAccountsFromCSMToSpring();
			migrateDataService.grantCuratorRoleToAccounts();
			
			return Response.ok("Access data migrated successfully.").build();
		} catch (Exception e) {
			logger.error("Error in migrating user accounts and roles from CSM : ", e);
			return Response.ok("Error in migrating user accounts and roles from CSM.").build();
		}
	}
	
	@GET
	@Path("/migratesampleaccess")
	@Produces ("application/json")
	public Response migrateSampleAccess(@Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for samples - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			return Response.ok("All Access data migrated successfully for samples.").build();
		} catch (Exception e) {
			logger.error("Error in migrating all access data for samples : ", e);
			return Response.ok("Error in migrating all access data for samples.").build();
		}
	}
	
	@GET
	@Path("/migrateprotocolaccess")
	@Produces ("application/json")
	public Response migrateProtocolAccess(@Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate default access for protocols - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			return Response.ok("All Access data migrated successfully for protocols.").build();
		} catch (Exception e) {
			logger.error("Error in migrating all access data for protocols : ", e);
			return Response.ok("Error in migrating all access data for protocols.").build();
		}
	}
	
	@GET
	@Path("/migratepublicationaccess")
	@Produces ("application/json")
	public Response migratePublicationAccess(@Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for publications - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			return Response.ok("All Access data migrated successfully for publications.").build();
		} catch (Exception e) {
			logger.error("Error in migrating all access data for publications : ", e);
			return Response.ok("Error in migrating all access data for publications.").build();
		}
	}

}
