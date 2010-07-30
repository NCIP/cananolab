package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DataAvailabilityException;
import gov.nih.nci.cananolab.service.sample.DataAvailabilityService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
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
 * @author lethai
 *
 */
public class DataAvailabilityServiceJDBCImpl extends JdbcDaoSupport implements DataAvailabilityService{
	
	//[data_availability] table columns.
	private static final String SAMPLE_ID = "sample_id";
	private static final String DATASOURCE_NAME = "datasource_name";
	private static final String AVAILABLE_ENTITY_NAME = "available_entity_name";
	private static final String CREATED_BY = "created_by";
	private static final String CREATED_DATE = "created_date";
	private static final String UPDATED_BY = "updated_by";
	private static final String UPDATED_DATE = "updated_date";
	
	private static String SELECT_DATA_AVAILABILITY = 
		"select * from data_availability where sample_id=" ;
	
	private static String INSERT_SQL = "INSERT INTO DATA_AVAILABILITY ";
	private static String UPDATE_SQL = "UPDATE DATA_AVAILABILITY ";
	
	//DATA_MAPPER
	private static DataAvailabilityMapper DATA_MAPPER = new DataAvailabilityMapper();
	
	public DataAvailabilityServiceJDBCImpl(){
		
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cananolab.service.sample.DataAvailabilityService#findDataAvailabilityBySampleId(java.lang.String)
	 */
	public List<DataAvailabilityBean> findDataAvailabilityBySampleId(String sampleId) throws DataAvailabilityException{
		List<DataAvailabilityBean> result = new ArrayList<DataAvailabilityBean>();
		
		JdbcTemplate data = this.getJdbcTemplate();
		
		result = (List<DataAvailabilityBean>) data.query(
				SELECT_DATA_AVAILABILITY + sampleId, DATA_MAPPER);
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cananolab.service.sample.DataAvailabilityService#generateDataAvailability(gov.nih.nci.cananolab.dto.particle.SampleBean, gov.nih.nci.cananolab.dto.common.UserBean)
	 */
	public List<DataAvailabilityBean> generateDataAvailability(SampleBean sampleBean, UserBean user) throws DataAvailabilityException{
		Long sampleId = sampleBean.getDomain().getId();
			
		Set<String> clazNames = null;
		try{
			clazNames = generate(sampleBean, user);
		}catch(Exception e){
			throw new DataAvailabilityException();
		}		
		
		System.out.println("total : " + clazNames.size());
		
		List<DataAvailabilityBean> dataAvailability = new ArrayList<DataAvailabilityBean>();
		
		for(String claz : clazNames){
			DataAvailabilityBean bean = new DataAvailabilityBean();
			bean.setSampleId(sampleId.longValue());
			bean.setAvailableEntityName(claz);
			bean.setDatasourceName("caNanoLab");
			bean.setCreatedDate(new Date());
			bean.setCreatedBy(user.getLoginName());
			dataAvailability.add(bean);
		}
		
		insertBatch(dataAvailability);
		
		return dataAvailability;
		
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cananolab.service.sample.DataAvailabilityService#saveDataAvailability(gov.nih.nci.cananolab.dto.particle.SampleBean, gov.nih.nci.cananolab.dto.common.UserBean)
	 */
	public List<DataAvailabilityBean> saveDataAvailability(SampleBean sampleBean, UserBean user) throws DataAvailabilityException{
		Long sampleId = sampleBean.getDomain().getId();
		List<DataAvailabilityBean> currentDataAvailability = sampleBean.getDataAvailability();
		
		Set<String> newGenernatedDataAvailability = null;
		try {
			newGenernatedDataAvailability = generate(sampleBean,user);
		} catch (Exception e) {
			throw new DataAvailabilityException();
		}
		//scenario where data is added
		List<DataAvailabilityBean> newDataAvailability = findAddedData(user.getLoginName(), sampleId, 
				currentDataAvailability,
				newGenernatedDataAvailability);
		List<String> removedEntityList = new ArrayList<String>();		
		//scenario where data is removed
		Set<DataAvailabilityBean> removedDataAvailability = findRemovedData(currentDataAvailability, newGenernatedDataAvailability);
		
		
		if(removedDataAvailability.size() > 0){
			for(DataAvailabilityBean removedBean:removedDataAvailability){
				removedEntityList.add(removedBean.getAvailableEntityName());
			}
			deleteBatch(removedEntityList, sampleId);
			currentDataAvailability.removeAll(removedDataAvailability);
		}
		
		System.out.println("Current size: " + currentDataAvailability.size());
		//update the currentDataAvailability
		updateBatch(currentDataAvailability);
		
		//insert the newDataAvailability
		if(newDataAvailability.size() > 0 ){
			insertBatchOnUpdate(newDataAvailability);
			currentDataAvailability.addAll(newDataAvailability);
		}
		
		System.out.println("new size: " + newDataAvailability.size());
		//return new list that contains both of them.
		System.out.println("currentDataAvailabitliy total size: " + currentDataAvailability.size());
		
		return currentDataAvailability;
	}

	
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cananolab.service.sample.DataAvailabilityService#deleteDataAvailability(java.lang.String)
	 */
	public void deleteDataAvailability(String sampleId) throws DataAvailabilityException{
		
		String sql = "delete from data_availability where sample_id = " + sampleId;
		this.getJdbcTemplate().execute(sql);		
	}

	private static final class DataAvailabilityMapper implements RowMapper {
		
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataAvailabilityBean dataBean = new DataAvailabilityBean();
			dataBean.setSampleId(rs.getLong(SAMPLE_ID));
			dataBean.setDatasourceName(rs.getString(DATASOURCE_NAME));
			dataBean.setAvailableEntityName(rs.getString(AVAILABLE_ENTITY_NAME));
			dataBean.setCreatedBy(rs.getString(CREATED_BY));
			dataBean.setCreatedDate(rs.getDate(CREATED_DATE));
			dataBean.setUpdatedBy(rs.getString(UPDATED_BY));
			dataBean.setUpdatedDate(rs.getDate(UPDATED_DATE));
			return dataBean;
		}		
	}

	/**
	 * generate the data availability information for the sample
	 * @param sampleBean
	 * @return
	 */
	private Set<String> generate(SampleBean sampleBean, UserBean user) throws Exception{
		
		boolean hasComposition = sampleBean.getHasComposition();
		boolean hasPublication = sampleBean.getHasPublications();
		
		SampleService sampleService = new SampleServiceLocalImpl(user);
		
		SampleServiceHelper helper = ((SampleServiceLocalImpl) sampleService).getHelper();
		
		//fullyLoadedSample = serviceImpl.findFullyLoadedSampleByName(sampleBean.getDomain().getName());
		
		//sampleBean.setDomain(fullyLoadedSample);
		Sample domain = sampleBean.getDomain();
		SampleComposition comp = domain.getSampleComposition();
		
		//System.out.println("Sample Composition: id " + comp.getId() + "; toString: " + comp.toString());
		
		
		SortedSet<String> storedChemicalAssociationClassNames = helper.getStoredChemicalAssociationClassNames(domain);
		
		SortedSet<String> storedFunctionalizingEntityClassNames = helper.getStoredFunctionalizingEntityClassNames(domain);
		SortedSet<String> storedFunctionClassNames = helper.getStoredFunctionClassNames(domain);
		SortedSet<String> storedNanomaterialEntityClassNames = helper.getStoredNanomaterialEntityClassNames(domain);
		
		//SortedSet<String> storedCharacterizationClassNames = helper.getStoredCharacterizationClassNames(domain);
		/*for(String s: storedCharacterizationClassNames){
			System.out.print(" characterization class name: " + s);
		}*/
		Set<String> clazzNames = new HashSet<String>();	
		
		//Collection<Characterization> characterizationCollection = domain.getCharacterizationCollection() ;
		if (domain.getCharacterizationCollection() != null) {
			for (Characterization achar : domain.getCharacterizationCollection()) {
				if (achar instanceof OtherCharacterization) {
				} else {
					String shortClassName = ClassUtils.getShortClassName(achar
							.getClass().getCanonicalName());
					String displayName = ClassUtils.getDisplayName(shortClassName);
					if(shortClassName.equalsIgnoreCase("surface")){
						CharacterizationServiceHelper charHelper = new CharacterizationServiceHelper(user);
						List<Finding> findingCollection = charHelper.findFindingsByCharacterizationId(achar.getId().toString());
						//Collection<Finding> findingCollection = achar.getFindingCollection();
						if(!findingCollection.isEmpty()){
							for(Finding finding: findingCollection){						
								Collection<Datum> datumCollection = finding.getDatumCollection();
								for(Datum datum:datumCollection){
									//System.out.println("datum: " + datum.getName() );
									clazzNames.add(datum.getName().toLowerCase());
								}
							}
						}else{
							clazzNames.add(displayName);
						}
					}else{
						clazzNames.add(displayName);
					}
				}
			}
		}
		
		for(String s: storedChemicalAssociationClassNames){
			//System.out.println("chemicalAssociation classname: " + s);
			clazzNames.add(s.toLowerCase());
		}
		if(storedFunctionalizingEntityClassNames.size() > 0){
			clazzNames.add("functionalizing entities");
		}
		if(storedNanomaterialEntityClassNames.size()> 0){
			//System.out.println("characterization classname: " + s);
			clazzNames.add("nanomaterial entities");
		}
		
		if(storedFunctionClassNames.size()> 0){
			clazzNames.add("sample function");
		}
		if(hasComposition){
			clazzNames.add("Sample Composition");
		}
		if(hasPublication){
			clazzNames.add("Publications");
		}
		return clazzNames;
	}
	
	
	/**
	 * delete data availability from database table in case these data availability
	 * are removed from currently persisted data.
	 * @param data
	 */
	private void deleteBatch(final List<String> entity, final Long sampleId){
		 
		  String sql = "DELETE FROM DATA_AVAILABILITY WHERE " +
			"SAMPLE_ID=? AND AVAILABLE_ENTITY_NAME=?";
		 
		  getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
		 
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				ps.setLong(1,sampleId);
				ps.setString(2, entity.get(i));
				
			}
		 
			public int getBatchSize() {
				return entity.size();
			}
		  });
	}
	/**
	 * insert data availability into database table
	 * @param data
	 */
	private void insertBatch(final List<DataAvailabilityBean> data){
		 
		  String sql = "INSERT INTO DATA_AVAILABILITY " +
			"(SAMPLE_ID, DATASOURCE_NAME, AVAILABLE_ENTITY_NAME, CREATED_DATE, CREATED_BY) VALUES (?, ?, ?, ?, ?)";
		 
		  getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
		 
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DataAvailabilityBean bean = data.get(i);
				ps.setLong(1, bean.getSampleId());
				ps.setString(2, bean.getDatasourceName());
				ps.setString(3, bean.getAvailableEntityName() );				
				ps.setDate(4, new java.sql.Date( bean.getCreatedDate().getTime()));
				ps.setString(5, bean.getCreatedBy());
			}
		 
			public int getBatchSize() {
				return data.size();
			}
		  });
	}
	
	/**
	 * insert data availability into the database in case these are 
	 * additional data availability on existing persisted data
	 * @param data
	 */
	private void insertBatchOnUpdate(final List<DataAvailabilityBean> data){
		 
		  String sql = "INSERT INTO DATA_AVAILABILITY " +
			"(SAMPLE_ID, DATASOURCE_NAME, AVAILABLE_ENTITY_NAME, CREATED_DATE, CREATED_BY, UPDATED_DATE, UPDATED_BY) VALUES (?, ?, ?, ?, ?, ?, ?)";
		 
		  getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
		 
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DataAvailabilityBean bean = data.get(i);
				ps.setLong(1, bean.getSampleId());
				ps.setString(2, bean.getDatasourceName());
				ps.setString(3, bean.getAvailableEntityName() );				
				ps.setDate(4, new java.sql.Date( bean.getCreatedDate().getTime()));
				ps.setString(5, bean.getCreatedBy());
				ps.setDate(6, new java.sql.Date( bean.getUpdatedDate().getTime()));
				ps.setString(7, bean.getUpdatedBy());				
			}
		 
			public int getBatchSize() {
				return data.size();
			}
		  });
	}
	
	/**
	 * update data availability to database table
	 * @param data
	 */
	private void updateBatch(final List<DataAvailabilityBean> data){
		String sql = "UPDATE DATA_AVAILABILITY SET AVAILABLE_ENTITY_NAME=?, UPDATED_DATE=?, UPDATED_BY=? WHERE SAMPLE_ID=? and AVAILABLE_ENTITY_NAME=?";
		
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			 
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DataAvailabilityBean bean = data.get(i);
				
				ps.setString(1, bean.getAvailableEntityName() );				
				ps.setDate(2, new java.sql.Date( bean.getUpdatedDate().getTime()));
				ps.setString(3, bean.getUpdatedBy());
				ps.setLong(4, bean.getSampleId());
				ps.setString(5, bean.getAvailableEntityName() );
			}

			public int getBatchSize() {
				return data.size();
			}
		  });
	}
	
	/**
	 * find any removed data availability from the newly generated list vs the current persisted list
	 * @param currentDataAvailability
	 * @param newGenernatedDataAvailability
	 * @return
	 */
	private Set<DataAvailabilityBean> findRemovedData(
			List<DataAvailabilityBean> currentDataAvailability,
			Set<String> newGenernatedDataAvailability) {
		
		Set<DataAvailabilityBean> removedList = new HashSet<DataAvailabilityBean>();
		Set<DataAvailabilityBean> currentListWithoutDuplicates = new HashSet<DataAvailabilityBean>();
		
		for(DataAvailabilityBean currentBean: currentDataAvailability){
			currentListWithoutDuplicates.add(currentBean);
		}
		if(newGenernatedDataAvailability.size() < currentDataAvailability.size()){
			//find the ones that are removed
			String[] availableEntityName = newGenernatedDataAvailability.toArray(new String[0]);
			for(DataAvailabilityBean currentBean : currentListWithoutDuplicates){
				String entityName = currentBean.getAvailableEntityName();
				boolean removed = false;
				for(int i=0; i<availableEntityName.length; i++){
					System.out.println("current entity: " + availableEntityName[i]);
					String newEntityName = availableEntityName[i];
					if(!entityName.equalsIgnoreCase(newEntityName) ){
						removed = true;			
					}else{
						removed = false;
						break;
					}
				}
				if(removed ){
					// this entity is removed from the current list
					removedList.add(currentBean);					
				}
			}
		}
		return removedList;
	}

	/**
	 * find any new added data availability from the newly generated list vs the current persisted list
	 * @param loginName
	 * @param sampleId
	 * @param currentDataAvailability
	 * @param newGenernatedDataAvailability
	 * @return
	 */
	private List<DataAvailabilityBean> findAddedData(String loginName, Long sampleId,
			List<DataAvailabilityBean> currentDataAvailability,
			Set<String> newGenernatedDataAvailability
			) {
		List<DataAvailabilityBean> newDataAvailability = new ArrayList<DataAvailabilityBean>();
		for(String entity: newGenernatedDataAvailability){
			System.out.println("entity in new generated list: " + entity);
			boolean updated = false;
			for(int i=0; i< currentDataAvailability.size(); i++){
				DataAvailabilityBean bean= currentDataAvailability.get(i);
			
				String availableEntityName = bean.getAvailableEntityName();
				System.out.println("current entity: " + availableEntityName);
				if(entity.equalsIgnoreCase(availableEntityName) ){
					//update
					bean.setUpdatedBy(loginName);
					bean.setUpdatedDate(new Date());
					updated = true;					
					break;
				}
			}
			
			if(!updated ){
			//insert
				DataAvailabilityBean newBean = new DataAvailabilityBean();
				newBean.setSampleId(sampleId);
				newBean.setAvailableEntityName(entity);
				newBean.setDatasourceName("caNanoLab");
				newBean.setCreatedDate(currentDataAvailability.get(0).getCreatedDate());
				newBean.setCreatedBy(currentDataAvailability.get(0).getCreatedBy());
				newBean.setUpdatedBy(loginName);
				newBean.setUpdatedDate(new Date());
				newDataAvailability.add(newBean);
			}
		}
		return newDataAvailability;
	}
}
