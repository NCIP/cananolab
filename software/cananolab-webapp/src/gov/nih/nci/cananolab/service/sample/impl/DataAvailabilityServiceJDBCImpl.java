package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DataAvailabilityException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SampleException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Service methods for data availability
 *
 * @author lethai
 *
 */
public class DataAvailabilityServiceJDBCImpl extends JdbcDaoSupport implements
		DataAvailabilityService {

	// [data_availability] table columns.
	private static final String SAMPLE_ID = "sample_id";
	private static final String DATASOURCE_NAME = "datasource_name";
	private static final String AVAILABLE_ENTITY_NAME = "available_entity_name";
	private static final String CREATED_BY = "created_by";
	private static final String CREATED_DATE = "created_date";
	private static final String UPDATED_BY = "updated_by";
	private static final String UPDATED_DATE = "updated_date";
	private static final String TABLE_COLS = SAMPLE_ID + ", " + DATASOURCE_NAME
			+ ", " + AVAILABLE_ENTITY_NAME + ", " + CREATED_BY + ", "
			+ CREATED_DATE + ", " + UPDATED_BY + ", " + UPDATED_DATE;

	private static String SELECT_DATA_AVAILABILITY = "select " + TABLE_COLS
			+ " from data_availability where sample_id=";

	private static String INSERT_SQL = "INSERT INTO DATA_AVAILABILITY ";
	private static String INSERT_COLS = "(SAMPLE_ID, DATASOURCE_NAME, AVAILABLE_ENTITY_NAME, CREATED_DATE, CREATED_BY) VALUES (?, ?, ?, ?, ?)";
	private static String UPDATE_SQL = "UPDATE DATA_AVAILABILITY SET AVAILABLE_ENTITY_NAME=?, UPDATED_DATE=?, UPDATED_BY=? WHERE SAMPLE_ID=? and AVAILABLE_ENTITY_NAME=?";
	private static String INSERT_ON_UPDATE = INSERT_SQL
			+ "(SAMPLE_ID, DATASOURCE_NAME, AVAILABLE_ENTITY_NAME, CREATED_DATE, CREATED_BY, UPDATED_DATE, UPDATED_BY ) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static String DELETE_SQL = "DELETE FROM DATA_AVAILABILITY ";
	private static String DELETE_ON_UPDATE = DELETE_SQL
			+ " WHERE SAMPLE_ID=? AND AVAILABLE_ENTITY_NAME=?";

	private static String noAccessException = "You do not have permission to access the specified sample data: ";
	
	// DATA_MAPPER
	private static DataAvailabilityMapper DATA_MAPPER = new DataAvailabilityMapper();

	public DataAvailabilityServiceJDBCImpl() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seegov.nih.nci.cananolab.service.sample.DataAvailabilityService#
	 * findDataAvailabilityBySampleId(java.lang.String)
	 */
	public Set<DataAvailabilityBean> findDataAvailabilityBySampleId(
			String sampleId, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException {
		
		List<DataAvailabilityBean> result = new ArrayList<DataAvailabilityBean>();

		JdbcTemplate data = this.getJdbcTemplate();

		result = (List<DataAvailabilityBean>) data.query(
				SELECT_DATA_AVAILABILITY + sampleId, DATA_MAPPER);

		Set<DataAvailabilityBean> resultSet = new HashSet<DataAvailabilityBean>();
		Set<String> availableEntityNames = new HashSet<String>();
		for (DataAvailabilityBean bean : result) {
			String availableEntityName = bean.getAvailableEntityName();
			if (availableEntityNames.isEmpty()) {
				resultSet.add(bean);
				availableEntityNames.add(availableEntityName);
			} else {
				if (!availableEntityNames.contains(availableEntityName)) {
					resultSet.add(bean);
				}
				availableEntityNames.add(availableEntityName);
			}
		}
		return resultSet;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @seegov.nih.nci.cananolab.service.sample.DataAvailabilityService#
	 * deleteDataAvailability(java.lang.String)
	 */
	public void deleteDataAvailability1(String sampleId,
			SecurityService securityService) throws DataAvailabilityException,
			NoAccessException, SecurityException {

		if (!securityService.checkDeletePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}

		String sql = "delete from data_availability where sample_id = "
				+ sampleId;
		this.getJdbcTemplate().execute(sql);
	}

	private SampleBean loadSample(String sampleId,
			SecurityService securityService) throws SampleException,
			NoAccessException {
		SampleService service = new SampleServiceLocalImpl(securityService);
		SampleBean sampleBean = service.findSampleById(sampleId, false);

		return sampleBean;
	}

	public void deleteBatchDataAvailability(SecurityService securityService) throws Exception {
		UserBean ub = securityService.getUserBean();
		if (!ub.isCurator()) {
			throw new Exception("No permission to process the request");
		} else {
			this.getJdbcTemplate().execute(DELETE_SQL);
		}

	}

	public List<String> findSampleIdsWithDataAvailability(SecurityService securityService)
			throws Exception {
		List sampleIdsList = this.getJdbcTemplate().queryForList(
				"select distinct(sample_id) from data_availability",
				String.class);
		List<String> sampleIds = new ArrayList<String>();
		for (Object obj : sampleIdsList) {
			sampleIds.add(obj.toString());
		}
		return sampleIds;
	}

	public void deleteDataAvailability(String sampleId, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException {
		if (!securityService.checkDeletePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}

		String sql = "delete from data_availability where sample_id = "
				+ sampleId;
		this.getJdbcTemplate().execute(sql);
		
	}

	public void deleteBatchDataAvailability(List<String> sampleIds,
			SecurityService securityService) throws Exception {
		// delete all records from data_availability table
		
		UserBean ub = securityService.getUserBean();
		if (!ub.isCurator()) {
			throw new Exception("No permission to process the request");
		} else {
			for(String sampleId : sampleIds){
				deleteDataAvailability(sampleId, securityService);
			}
		}
		
	}

	public Set<DataAvailabilityBean> saveDataAvailability(SampleBean sampleBean,
			SecurityService securityService) throws DataAvailabilityException,
			NoAccessException, SecurityException {
		String sampleId = sampleBean.getDomain().getId().toString();
		if (!securityService.checkCreatePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}
		Set<DataAvailabilityBean> results = new HashSet<DataAvailabilityBean>();
		Set<DataAvailabilityBean> dataAvailability = findDataAvailabilityBySampleId(sampleId, securityService);
		//Set<DataAvailabilityBean> saveDataAvailability
		if(dataAvailability.size() > 0){
			//update
			results = save(sampleBean, securityService);
		}else{
			//insert
			results = insert(sampleBean, securityService);
		}
		
		return results;
	}

	public Set<DataAvailabilityBean> saveDataAvailability(String sampleId,
			SecurityService securityService) throws DataAvailabilityException,
			NoAccessException, SecurityException, SampleException {
		Set<DataAvailabilityBean> results = new HashSet<DataAvailabilityBean>();
		//check for existing data availability for this sample
		//if exist, update else insert
		if (!securityService.checkCreatePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}
		Set<DataAvailabilityBean> dataAvailability = findDataAvailabilityBySampleId(sampleId, securityService);
		SampleBean sampleBean = loadSample(sampleId, securityService);
		if(sampleBean != null){
			sampleBean.setDataAvailability(dataAvailability);
			if(dataAvailability.size() > 0){
				//update
				results = save(sampleBean, securityService);
			}else{
				//insert
				results = insert(sampleBean, securityService);
			}
		}
		return results;
	}

	
	public void saveBatchDataAvailability(List<String> sampleIds, SecurityService securityService) throws Exception {
		// find data availability for the sampleId, 
		//update if exist, otherwise insert
		
		SampleService service = new SampleServiceLocalImpl(securityService);
		
		for(String sampleId : sampleIds){
			SampleBean sampleBean = service.findSampleById(sampleId, false);
			saveDataAvailability(sampleBean, securityService);
		}		
	}
	
	private Set<DataAvailabilityBean> insert(
			SampleBean sampleBean, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException {
		Long sampleId = sampleBean.getDomain().getId();
		UserBean user = securityService.getUserBean();

		if (!securityService.checkCreatePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}
		Set<String> clazNames = null;
		try {
			clazNames = generate(sampleBean, securityService);
			
		} catch (Exception e) {
			throw new DataAvailabilityException();
		}

		Set<DataAvailabilityBean> dataAvailability = new HashSet<DataAvailabilityBean>();

		for (String claz : clazNames) {
			DataAvailabilityBean bean = new DataAvailabilityBean();
			bean.setSampleId(sampleId.longValue());
			bean.setAvailableEntityName(claz);
			bean.setDatasourceName("caNanoLab");
			bean.setCreatedDate(new Date());
			bean.setCreatedBy(user.getLoginName());
			dataAvailability.add(bean);
		}
		List<DataAvailabilityBean> list = new ArrayList<DataAvailabilityBean>();
		list.addAll(dataAvailability);
		insertBatch(list);

		return dataAvailability;
	}
	private Set<DataAvailabilityBean> save(
			SampleBean sampleBean, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException {
		Long sampleId = sampleBean.getDomain().getId();

		UserBean user = securityService.getUserBean();

		if (!securityService.checkCreatePermission(sampleId.toString())) {
			throw new NoAccessException(noAccessException + sampleId);
		}

		Set<DataAvailabilityBean> currentDataAvailability = sampleBean
				.getDataAvailability();

		Set<String> newGenernatedDataAvailability = null;
		try {
			newGenernatedDataAvailability = generate(sampleBean,
					securityService);
		} catch (Exception e) {
			throw new DataAvailabilityException();
		}
		// scenario where data is added
		Set<DataAvailabilityBean> newDataAvailability = findAddedData(user
				.getLoginName(), sampleId, currentDataAvailability,
				newGenernatedDataAvailability);
		List<String> removedEntityList = new ArrayList<String>();
		// scenario where data is removed
		Set<DataAvailabilityBean> removedDataAvailability = findRemovedData(
				currentDataAvailability, newGenernatedDataAvailability);

		if (removedDataAvailability.size() > 0) {
			for (DataAvailabilityBean removedBean : removedDataAvailability) {
				removedEntityList.add(removedBean.getAvailableEntityName());
			}
			deleteBatch(removedEntityList, sampleId);
			currentDataAvailability.removeAll(removedDataAvailability);
		}

		//System.out.println("Current size: " + currentDataAvailability.size());
		for (DataAvailabilityBean bean : currentDataAvailability) {
			bean.setUpdatedBy(user.getLoginName());
			bean.setUpdatedDate(new Date());
		}
		// update the currentDataAvailability
		List<DataAvailabilityBean> currentList = new ArrayList<DataAvailabilityBean>();
		currentList.addAll(currentDataAvailability);

		updateBatch(currentList);

		// insert the newDataAvailability
		List<DataAvailabilityBean> newList = new ArrayList<DataAvailabilityBean>();
		newList.addAll(newDataAvailability);

		if (newList.size() > 0) {
			insertBatchOnUpdate(newList);
			currentDataAvailability.addAll(newDataAvailability);
		}

		/*System.out.println("new size: " + newDataAvailability.size());
		// return new list that contains both of them.
		System.out.println("currentDataAvailabitliy total size: "
				+ currentDataAvailability.size());*/

		return currentDataAvailability;
	}
	
	/**
	 * generate the data availability information for the sample
	 *
	 * @param sampleBean
	 * @return
	 */
	private Set<String> generate(SampleBean sampleBean,
			SecurityService securityService) throws Exception {

		boolean hasComposition = sampleBean.getHasComposition();
		boolean hasPublication = sampleBean.getHasPublications();

		SampleServiceHelper helper = new SampleServiceHelper(securityService);
		Sample domain = sampleBean.getDomain();

		SortedSet<String> storedChemicalAssociationClassNames = helper
				.getStoredChemicalAssociationClassNames(domain);

		SortedSet<String> storedFunctionalizingEntityClassNames = helper
				.getStoredFunctionalizingEntityClassNames(domain);
		SortedSet<String> storedFunctionClassNames = helper
				.getStoredFunctionClassNames(domain);
		SortedSet<String> storedNanomaterialEntityClassNames = helper
				.getStoredNanomaterialEntityClassNames(domain);

		Set<String> clazzNames = new HashSet<String>();
		clazzNames.add("General Sample Information");
		if (domain.getCharacterizationCollection() != null) {
			for (Characterization achar : domain
					.getCharacterizationCollection()) {
				if (achar instanceof OtherCharacterization) {
				} else {
					String shortClassName = ClassUtils.getShortClassName(achar
							.getClass().getCanonicalName());
					String displayName = ClassUtils
							.getDisplayName(shortClassName);
					if (shortClassName.equalsIgnoreCase("surface")) {
						CharacterizationServiceHelper charHelper = new CharacterizationServiceHelper(
								securityService);
						List<Finding> findingCollection = charHelper
								.findFindingsByCharacterizationId(achar.getId()
										.toString());
						// Collection<Finding> findingCollection =
						// achar.getFindingCollection();
						if (!findingCollection.isEmpty()) {
							for (Finding finding : findingCollection) {
								Collection<Datum> datumCollection = finding
										.getDatumCollection();
								for (Datum datum : datumCollection) {
									// System.out.println("datum: " +
									// datum.getName() );
									clazzNames.add(datum.getName()
											.toLowerCase());
								}
							}
						} else {
							clazzNames.add(displayName);
						}
					} else {
						clazzNames.add(displayName);
					}
				}
			}
		}

		for (String s : storedChemicalAssociationClassNames) {
			// System.out.println("chemicalAssociation classname: " + s);
			clazzNames.add(s.toLowerCase());
		}
		if (storedFunctionalizingEntityClassNames.size() > 0) {
			clazzNames.add("functionalizing entities");
		}
		if (storedNanomaterialEntityClassNames.size() > 0) {
			// System.out.println("characterization classname: " + s);
			clazzNames.add("nanomaterial entities");
		}

		if (storedFunctionClassNames.size() > 0) {
			clazzNames.add("sample function");
		}
		if (hasComposition) {
			clazzNames.add("Sample Composition");
		}
		if (hasPublication) {
			clazzNames.add("Publications");
		}
		return clazzNames;
	}
	
	/**
	 * delete data availability from database table in case these data
	 * availability are removed from currently persisted data.
	 *
	 * @param data
	 */
	private void deleteBatch(final List<String> entity, final Long sampleId) {

		getJdbcTemplate().batchUpdate(DELETE_ON_UPDATE,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, sampleId);
						ps.setString(2, entity.get(i));
					}

					public int getBatchSize() {
						return entity.size();
					}
				});
	}

	/**
	 * insert data availability into database table
	 *
	 * @param data
	 */
	private void insertBatch(final List<DataAvailabilityBean> data) {

		getJdbcTemplate().batchUpdate(INSERT_SQL + INSERT_COLS,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						DataAvailabilityBean bean = data.get(i);
						ps.setLong(1, bean.getSampleId());
						ps.setString(2, bean.getDatasourceName());
						ps.setString(3, bean.getAvailableEntityName());
						ps.setDate(4, new java.sql.Date(bean.getCreatedDate()
								.getTime()));
						ps.setString(5, bean.getCreatedBy());
					}

					public int getBatchSize() {
						return data.size();
					}
				});
	}

	/**
	 * insert data availability into the database in case these are additional
	 * data availability on existing persisted data
	 *
	 * @param data
	 */
	private void insertBatchOnUpdate(final List<DataAvailabilityBean> data) {

		getJdbcTemplate().batchUpdate(INSERT_ON_UPDATE,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						DataAvailabilityBean bean = data.get(i);
						ps.setLong(1, bean.getSampleId());
						ps.setString(2, bean.getDatasourceName());
						ps.setString(3, bean.getAvailableEntityName());
						ps.setDate(4, new java.sql.Date(bean.getCreatedDate()
								.getTime()));
						ps.setString(5, bean.getCreatedBy());
						ps.setDate(6, new java.sql.Date(bean.getUpdatedDate()
								.getTime()));
						ps.setString(7, bean.getUpdatedBy());
					}

					public int getBatchSize() {
						return data.size();
					}
				});
	}

	/**
	 * update data availability to database table
	 *
	 * @param data
	 */
	private void updateBatch(final List<DataAvailabilityBean> data) {
		// String sql =
		// "UPDATE DATA_AVAILABILITY SET AVAILABLE_ENTITY_NAME=?, UPDATED_DATE=?, UPDATED_BY=? WHERE SAMPLE_ID=? and AVAILABLE_ENTITY_NAME=?";

		getJdbcTemplate().batchUpdate(UPDATE_SQL,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						DataAvailabilityBean bean = data.get(i);

						ps.setString(1, bean.getAvailableEntityName());
						ps.setDate(2, new java.sql.Date(bean.getUpdatedDate()
								.getTime()));
						ps.setString(3, bean.getUpdatedBy());
						ps.setLong(4, bean.getSampleId());
						ps.setString(5, bean.getAvailableEntityName());
					}

					public int getBatchSize() {
						return data.size();
					}
				});
	}

	/**
	 * find any removed data availability from the newly generated list vs the
	 * current persisted list
	 *
	 * @param currentDataAvailability
	 * @param newGenernatedDataAvailability
	 * @return
	 */
	private Set<DataAvailabilityBean> findRemovedData(
			Set<DataAvailabilityBean> currentDataAvailability,
			Set<String> newGenernatedDataAvailability) {

		Set<DataAvailabilityBean> removedList = new HashSet<DataAvailabilityBean>();

		if (newGenernatedDataAvailability.size() < currentDataAvailability
				.size()) {
			// find the ones that are removed
			String[] availableEntityName = newGenernatedDataAvailability
					.toArray(new String[0]);
			for (DataAvailabilityBean currentBean : currentDataAvailability) {
				String entityName = currentBean.getAvailableEntityName();
				boolean removed = false;
				for (int i = 0; i < availableEntityName.length; i++) {
					// System.out.println("current entity: " +
					// availableEntityName[i]);
					String newEntityName = availableEntityName[i];
					if (!entityName.equalsIgnoreCase(newEntityName)) {
						removed = true;
					} else {
						removed = false;
						break;
					}
				}
				if (removed) {
					// this entity is removed from the current list
					removedList.add(currentBean);
				}
			}
		}
		return removedList;
	}

	/**
	 * find any new added data availability from the newly generated list vs the
	 * current persisted list
	 *
	 * @param loginName
	 * @param sampleId
	 * @param currentDataAvailability
	 * @param newGenernatedDataAvailability
	 * @return
	 */
	private Set<DataAvailabilityBean> findAddedData(String loginName,
			Long sampleId, Set<DataAvailabilityBean> currentDataAvailability,
			Set<String> newGenernatedDataAvailability) {
		Set<DataAvailabilityBean> newDataAvailability = new HashSet<DataAvailabilityBean>();

		for (String entity : newGenernatedDataAvailability) {
			// System.out.println("entity in new generated list: " + entity);
			boolean updated = false;
			for (DataAvailabilityBean bean : currentDataAvailability) {// int
				String availableEntityName = bean.getAvailableEntityName();
				// System.out.println("current entity: " + availableEntityName);
				if (entity.equalsIgnoreCase(availableEntityName)) {
					// update
					bean.setUpdatedBy(loginName);
					bean.setUpdatedDate(new Date());
					updated = true;
					break;
				}
			}

			if (!updated) {
				// insert
				DataAvailabilityBean newBean = new DataAvailabilityBean();
				newBean.setSampleId(sampleId);
				newBean.setAvailableEntityName(entity);
				newBean.setDatasourceName("caNanoLab");
				if (currentDataAvailability.iterator().hasNext()) {
					newBean.setCreatedDate(currentDataAvailability.iterator()
							.next().getCreatedDate());
					newBean.setCreatedBy(currentDataAvailability.iterator()
							.next().getCreatedBy());
				}
				newBean.setUpdatedBy(loginName);
				newBean.setUpdatedDate(new Date());
				newDataAvailability.add(newBean);
			}
		}
		return newDataAvailability;
	}

	private static final class DataAvailabilityMapper implements RowMapper {

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataAvailabilityBean dataBean = new DataAvailabilityBean();
			dataBean.setSampleId(rs.getLong(SAMPLE_ID));
			dataBean.setDatasourceName(rs.getString(DATASOURCE_NAME));
			dataBean
					.setAvailableEntityName(rs.getString(AVAILABLE_ENTITY_NAME));
			dataBean.setCreatedBy(rs.getString(CREATED_BY));
			dataBean.setCreatedDate(rs.getDate(CREATED_DATE));
			dataBean.setUpdatedBy(rs.getString(UPDATED_BY));
			dataBean.setUpdatedDate(rs.getDate(UPDATED_DATE));
			return dataBean;
		}
	}
}
