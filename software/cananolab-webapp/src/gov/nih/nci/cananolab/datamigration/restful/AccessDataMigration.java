package gov.nih.nci.cananolab.datamigration.restful;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import gov.nih.nci.cananolab.datamigration.service.MigrateDataService;
import gov.nih.nci.cananolab.datamigration.util.AESEncryption;
import gov.nih.nci.cananolab.restful.SpringApplicationContext;
import gov.nih.nci.cananolab.security.enums.SecureClassesEnum;

@Path("/datamigration")
public class AccessDataMigration 
{
	private static final Logger logger = Logger.getLogger(AccessDataMigration.class);

	@GET
	@Path("/migrateuseraccounts")
	@Produces ("application/json")
	public void migrateUserAccountsAndRoles(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all user accounts from CSM to Spring.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );
			migrateDataService.migrateUserAccountsFromCSMToSpring();
			migrateDataService.grantCuratorRoleToAccounts();
			
			String message = "All user accounts migrated successfully";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating user accounts and roles from CSM : ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
                .entity("Error in migrating user accounts and roles from CSM : " + e.getMessage())
                .build());
		}
	}
	
	@GET
	@Path("/migratesampleaccess")
	public void migrateSampleAccess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for samples - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );

            migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.SAMPLE);
			
			String message = "All Access data migrated successfully for samples.";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating all access data for samples : ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
					                    .entity("Error in migrating all access data for samples." + e.getMessage())
					                    .build());
		}
	}
	
	@GET
	@Path("/migrateprotocolaccess")
	public void migrateProtocolAccess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate default access for protocols - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );
			migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.PROTOCOL);
			
			String message = "All Access data migrated successfully for protocols.";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating all access data for protocols : ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
					                    .entity("Error in migrating all access data for protocols." + e.getMessage())
					                    .build());
		}
	}
	
	@GET
	@Path("/migratepublicationaccess")
	public void migratePublicationAccess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for publications - RWD access for owner and curator, R access for researcher, RWD and R access for specific users.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );
			migrateDataService.migrateDefaultAccessDataFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migratePublicAccessDataFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migrateRWDUserAccessFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			migrateDataService.migrateReadUserAccessFromCSMToSpring(SecureClassesEnum.PUBLICATION);
			
			String message = "All Access data migrated successfully for publications.";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating all access data for publications : ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
					                    .entity("Error in migrating all access data for publications." + e.getMessage())
					                    .build());
		}
	}
	
	@GET
	@Path("/migratecharaccess")
	public void migrateCharacterizationAccess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for characterizations.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );
			migrateDataService.migrateCharacterizationAccessData();
			
			String message = "All Access data migrated successfully for characterizations.";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating all access data for characterizations : ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
					                    .entity("Error in migrating all access data for characterizations." + e.getMessage())
					                    .build());
		}
	}
	
	@GET
	@Path("/migratesamplesourceaccess")
	public void migrateSampleSourceAccess(@Suspended final AsyncResponse asyncResponse, @Context HttpServletRequest httpRequest) 
	{
		logger.info("Migrate all access for Organizations and Point of Contacts.");
		
		try {
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			asyncResponse.setTimeout(600000, TimeUnit.MILLISECONDS);
            asyncResponse.setTimeoutHandler(new TimeoutHandler() {
								                @Override
								                public void handleTimeout(AsyncResponse asyncResponse) {
								                    asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
								                            .entity("Operation time out.").build());
								                }}
                );
			migrateDataService.migrateOrganizationAccessData();
			
			String message = "All Access data migrated successfully for Organizations and Point of Contacts.";
			logger.info(message);
			asyncResponse.resume(Response.status(Response.Status.OK).entity(message).build());
		} catch (Exception e) {
			logger.error("Error in migrating all access data for Organizations and Point of Contacts: ", e);
			asyncResponse.resume(Response.status(Response.Status.OK)
					                    .entity("Error in migrating all access data for Organizations and Point of Contacts." + e.getMessage())
					                    .build());
		}
	}
	
	@GET
	@Path("/encrypt")
	@Produces ("application/json")
	public Response encrypt(@Context HttpServletRequest httpRequest, @QueryParam("decryptedString") String decryptedString)
	{
		String encryptedString = "";
		try {
			AESEncryption aesencryption = new AESEncryption();
			encryptedString = aesencryption.encrypt(decryptedString);
		} catch (Exception e) {
			encryptedString = "Error in encryption: " + e.getMessage();
		}
		return Response.ok("Decrypted String = " + decryptedString + ", Encrypted String = " + encryptedString).build();
	}
	
	@GET
	@Path("/decrypt")
	@Produces ("application/json")
	public Response decrypt(@Context HttpServletRequest httpRequest, @QueryParam("encryptedString") String encryptedString)
	{
		String decryptedString = "";
		try {
			AESEncryption aesencryption = new AESEncryption();
			decryptedString = aesencryption.decrypt(encryptedString);
		} catch (Exception e) {
			decryptedString = "Error in decryption: " + e.getMessage();
		}
		return Response.ok("Encrypted String = " + encryptedString + ", Decrypted String = " + decryptedString).build();
	}
	
	@GET
	@Path("/bcrypt")
	@Produces ("application/json")
	public Response bcrypt(@Context HttpServletRequest httpRequest, @QueryParam("plainText") String plainText)
	{
		String encryptedString = "";
		try {
			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
			encryptedString = bcrypt.encode(plainText);
		} catch (Exception e) {
			encryptedString = "Error in encryption: " + e.getMessage();
		}
		return Response.ok("Plain text = " + plainText + ", Encrypted String = " + encryptedString).build();
	}
	
	@GET
	@Path("/bcryptpasswords")
	@Produces ("application/json")
	public Response bcryptPasswords(@Context HttpServletRequest httpRequest)
	{
		logger.info("Encrypt all the passowrds in users table.");
		String message = "";
		try
		{
			MigrateDataService migrateDataService = (MigrateDataService) SpringApplicationContext.getBean(httpRequest, "migrateDataService");
			migrateDataService.bcryptPasswords();
			message = "Passwords encrypted successfully.";
		} catch (Exception e) {
			message = "Error in encrypting passwords: " + e.getMessage();
		}
		return Response.ok(message).build();
	}

}
