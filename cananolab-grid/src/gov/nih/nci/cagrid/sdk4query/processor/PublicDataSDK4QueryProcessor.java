package gov.nih.nci.cagrid.sdk4query.processor;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.DomainTypesInformationUtil;
import gov.nih.nci.cagrid.sdkquery4.processor.ParameterizedHqlQuery;
import gov.nih.nci.cagrid.sdkquery4.processor.RoleNameResolver;
import gov.nih.nci.cagrid.sdkquery4.processor.SDK4QueryProcessor;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.ClassUtils;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Resource;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.security.SecurityManager;

/**
 * SDK4QueryProcessor Processes CQL against a caCORE SDK 4.0 data source
 *
 * @author David Ervin
 *
 * @created Oct 3, 2007 10:34:55 AM
 * @version $Id: SDK4QueryProcessor.java,v 1.16 2008/05/28 16:32:35 dervin Exp $
 */

/**
 * modified by Sue Pan to add a filter for public data
 */
public class PublicDataSDK4QueryProcessor extends SDK4QueryProcessor {
	// logger
	private static final Log LOG = LogFactory
			.getLog(PublicDataSDK4QueryProcessor.class);

	private PublicDataCQL2ParameterizedHQL cqlTranslator;

	public PublicDataSDK4QueryProcessor() {
		super();
	}

	/**
	 * Overriden to add initialization of the inheritance manager
	 */
	public void initialize(Properties parameters, InputStream wsdd)
			throws InitializationException {
		super.initialize(parameters, wsdd);
		initializeCqlToHqlTranslator();
	}

	private ApplicationService getApplicationService()
			throws QueryProcessingException {
		ApplicationService service = null;

		boolean useLocal = useLocalApplicationService();
		boolean useLogin = useServiceLogin();
		boolean useStaticLogin = useStaticLogin();
		try {
			String username = null;
			String passwd = null;
			if (useLogin) {
				if (useStaticLogin) {
					username = getConfiguredParameters().getProperty(
							PROPERTY_STATIC_LOGIN_USERNAME);
					passwd = username = getConfiguredParameters().getProperty(
							PROPERTY_STATIC_LOGIN_PASSWORD);
				} else {
					SecurityManager securityManager = SecurityManager
							.getManager();
					username = securityManager.getCaller();
					// TODO: password?
				}
			}

			if (useLocal) {
				if (useLogin) {
					service = ApplicationServiceProvider.getApplicationService(
							username, passwd);
				} else {
					service = ApplicationServiceProvider
							.getApplicationService();
				}
			} else {
				String url = getRemoteApplicationUrl();
				if (useLogin) {
					service = ApplicationServiceProvider
							.getApplicationServiceFromUrl(url, username, passwd);
				} else {
					service = ApplicationServiceProvider
							.getApplicationServiceFromUrl(url);
				}
			}
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error obtaining application service instance: "
							+ ex.getMessage(), ex);
		}

		return service;
	}

	private String getRemoteApplicationUrl() {
		String hostname = getConfiguredParameters().getProperty(
				PROPERTY_HOST_NAME);
		/*
		 * if (!hostname.startsWith("http://") ||
		 * !hostname.startsWith("https://")) { hostname = "http://" + hostname; }
		 */
		String port = getConfiguredParameters().getProperty(PROPERTY_HOST_PORT);
		while (hostname.endsWith("/")) {
			hostname = hostname.substring(0, hostname.length() - 1);
		}
		String urlPart = hostname + ":" + port;
		urlPart += "/";
		urlPart += getConfiguredParameters().getProperty(
				PROPERTY_APPLICATION_NAME);
		return urlPart;
	}

	private boolean useCaseInsensitiveQueries() throws QueryProcessingException {
		String caseInsensitiveValue = getConfiguredParameters().getProperty(
				PROPERTY_CASE_INSENSITIVE_QUERYING);
		try {
			return Boolean.parseBoolean(caseInsensitiveValue);
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error determining case insensitivity: " + ex.getMessage(),
					ex);
		}
	}

	private boolean useLocalApplicationService()
			throws QueryProcessingException {
		String useLocalValue = getConfiguredParameters().getProperty(
				PROPERTY_USE_LOCAL_API);
		try {
			return Boolean.parseBoolean(useLocalValue);
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error determining local application service use: "
							+ ex.getMessage(), ex);
		}
	}

	private boolean useServiceLogin() throws QueryProcessingException {
		String useLoginValue = getConfiguredParameters().getProperty(
				PROPERTY_USE_LOGIN);
		try {
			return Boolean.parseBoolean(useLoginValue);
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error determining login use flag: " + ex.getMessage(), ex);
		}
	}

	private boolean useStaticLogin() throws QueryProcessingException {
		String useGridIdentLogin = getConfiguredParameters().getProperty(
				PROPERTY_USE_GRID_IDENTITY_LOGIN);
		try {
			return !Boolean.parseBoolean(useGridIdentLogin);
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error determining use of static login: " + ex.getMessage(),
					ex);
		}
	}

	private DomainModel getDomainModel() throws Exception {
		DomainModel domainModel = null;
		Resource serviceBaseResource = ResourceContext.getResourceContext()
				.getResource();
		Method[] resourceMethods = serviceBaseResource.getClass().getMethods();
		for (int i = 0; i < resourceMethods.length; i++) {
			if (resourceMethods[i].getReturnType() != null
					&& resourceMethods[i].getReturnType().equals(
							DomainModel.class)) {
				domainModel = (DomainModel) resourceMethods[i].invoke(
						serviceBaseResource, new Object[] {});
				break;
			}
		}
		return domainModel;
	}

	protected List queryCoreService(CQLQuery query)
			throws MalformedQueryException, QueryProcessingException {
		// get the caCORE application service
		ApplicationService service = getApplicationService();

		// generate the HQL to perform the query
		// new CQL2HQL process handles query modifiers at HQL level
		ParameterizedHqlQuery parameterizedHql = cqlTranslator
				.convertToHql(query);
		LOG.debug("Executing HQL:\n" + parameterizedHql);

		// process the query
		HQLCriteria hqlCriteria = new HQLCriteria(parameterizedHql.getHql(),
				parameterizedHql.getParameters());
		List targetObjects = null;
		try {
			targetObjects = service.query(hqlCriteria);
			// added by Sue Pan
			List filteredObjects = applyPublicFilter(query.getQueryModifier(),
					targetObjects);
			return filteredObjects;
		} catch (Exception ex) {
			throw new QueryProcessingException(
					"Error querying caCORE Application Service: "
							+ ex.getMessage(), ex);
		}
	}

	// added by Sue to filter out non public data
	private List applyPublicFilter(QueryModifier mods, List rawObjects)
			throws Exception {
		List filteredObjects = new ArrayList();
		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();
		List<String> publicDataIds = appService.getAllPublicData();
		if (publicDataIds.isEmpty()) {
			return filteredObjects;
		}

		// filter non public data if queryModifier doesn't exists
		if (mods == null) {
			for (Object obj : rawObjects) {
				String data = null;
				if (obj instanceof Sample) {
					data = ((Sample) obj).getName();
					if (data != null
							&& StringUtils.containsIgnoreCase(publicDataIds,
									data)) {
						filteredObjects.add(obj);
					}
				} else {
					data = ClassUtils.getIdString(obj);
					if (data != null
							&& StringUtils.containsIgnoreCase(publicDataIds,
									data)) {
						filteredObjects.add(obj);
					}
				}
			}
		}
		// filter based on the secured attribute
		else {
			// if count only, each row would have one column being the secure
			// attribute
			if (mods.isCountOnly()) {
				int count = 0;
				for (Object obj : rawObjects) {
					String secureAttribute = (String) obj.toString();
					if (StringUtils.containsIgnoreCase(publicDataIds,
							secureAttribute)) {
						// remove redundancy
						if (!filteredObjects.contains(obj)) {
							filteredObjects.add(obj);
							count++;
						}
					}
				}
				return Arrays.asList(new Integer[] { count });
			} else {
				for (Object obj : rawObjects) {
					Object[] row = (Object[]) obj;
					Object actualObj = null;
					// actual data is contained in the second column
					if (row.length == 2) {
						actualObj = row[1];
					} else {
						Object[] actualRow = new Object[row.length - 1];
						for (int i = 0; i < row.length - 1; i++) {
							actualRow[i] = row[i + 1];
						}
						actualObj = actualRow;
					}
					String secureAttribute = row[0].toString().trim()
							.toUpperCase();
					if (StringUtils.containsIgnoreCase(publicDataIds,
							secureAttribute)) {
						if (!filteredObjects.contains(actualObj)) {
							filteredObjects.add(actualObj);
						}
					}
				}
			}
		}
		return filteredObjects;
	}

	private void initializeCqlToHqlTranslator() throws InitializationException {
		// get the domain types information document
		String domainTypesFilename = getConfiguredParameters().getProperty(
				PROPERTY_DOMAIN_TYPES_INFO_FILENAME);
		DomainTypesInformation typesInfo = null;
		try {
			FileReader reader = new FileReader(domainTypesFilename);
			typesInfo = DomainTypesInformationUtil
					.deserializeDomainTypesInformation(reader);
			reader.close();
		} catch (Exception ex) {
			throw new InitializationException(
					"Error deserializing domain types information from "
							+ domainTypesFilename + ": " + ex.getMessage(), ex);
		}
		// get the domain model
		DomainModel domainModel = null;
		try {
			domainModel = getDomainModel();
		} catch (Exception ex) {
			throw new InitializationException("Error obtaining domain model: "
					+ ex.getMessage(), ex);
		}
		RoleNameResolver resolver = new RoleNameResolver(domainModel);
		// create the query translator instance
		try {
			cqlTranslator = new PublicDataCQL2ParameterizedHQL(typesInfo,
					resolver, useCaseInsensitiveQueries());
		} catch (Exception ex) {
			throw new InitializationException(
					"Error instantiating CQL to HQL translator: "
							+ ex.getMessage(), ex);
		}
		LOG.debug("CQL to HQL translator initialized");
	}
}
