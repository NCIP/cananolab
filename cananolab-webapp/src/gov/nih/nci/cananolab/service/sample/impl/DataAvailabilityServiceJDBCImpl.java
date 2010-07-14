package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.service.sample.SampleService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class DataAvailabilityServiceJDBCImpl extends JdbcDaoSupport{
	
	//[sample-data_availability] table columns.
	private static final String SAMPLE_ID = "sample_id";
	private static final String DATASOURCE_NAME = "datasource_name";
	private static final String AVAILABLE_ENTITY_NAME = "available_entity_name";
	private static final String CREATED_BY = "created_by";
	private static final String CREATED_DATE = "created_date";
	private static final String UPDATED_BY = "updated_by";
	private static final String UPDATED_DATE = "updated_date";
	private SampleService helper;
	private UserBean user;
	private static String SELECT_DATA_AVAILABILITY = 
		"select from data_availability where sample_id=" ;
	
	//DATA_MAPPER
	private static DataAvailabilityMapper DATA_MAPPER = new DataAvailabilityMapper();
	
	public DataAvailabilityServiceJDBCImpl(){
		
	}
	public DataAvailabilityServiceJDBCImpl(UserBean user) {
		this.user = user;		
	}
	public DataAvailabilityServiceJDBCImpl(UserBean user, SampleService sampleService){
		this.user = user;
	}
	
	//data availability
	public List<DataAvailabilityBean> findDataAvailabilityBySampleId(String sampleId) throws Exception{
		List<DataAvailabilityBean> result = new ArrayList<DataAvailabilityBean>();
		JdbcTemplate data = this.getJdbcTemplate();
		
		result = (List<DataAvailabilityBean>) data.query(
				SELECT_DATA_AVAILABILITY + sampleId, DATA_MAPPER);
		//return result;
		System.out.println("result size: " + result.size());
		return result;
	}
	
	public void generateDataAvailability(SampleBean sampleBean, UserBean user){
		Long sampleId = sampleBean.getDomain().getId();
		//sampleBean.getDomain()
		boolean hasComposition = sampleBean.getHasComposition();
		boolean hasCharacterization = sampleBean.getHasCharacterizations();
		boolean hasPublication = sampleBean.getHasPublications();
		boolean hasProtocol = false;
		
		System.out.println("hasComposition: " + hasComposition);
		System.out.println("hasChars: " + hasCharacterization);
		Sample domain = sampleBean.getDomain();
		SampleComposition comp = domain.getSampleComposition();
		Collection<Characterization> chars = domain.getCharacterizationCollection();
		Iterator<Characterization> iterator = chars.iterator();
		while(iterator.hasNext()){
			Characterization characterization = iterator.next();
			Protocol protocol = characterization.getProtocol();
			
			if(protocol != null){
				System.out.println("protocol for characterization: " + characterization.getAssayType() + 
						", protocol name:  " + protocol.getName() + ", protocol type: " + protocol.getType());
				hasProtocol = true;
			}
		}
		
		Collection<Keyword> keywordCollection = domain.getKeywordCollection();
		Collection<Publication> publicationCollection = domain.getPublicationCollection();
		
		String[] characterizationClassNames = sampleBean.getCharacterizationClassNames();
		String[] chemicalAssociationClassNames = sampleBean.getChemicalAssociationClassNames();
		String[] functionalizingEntityClassNames = sampleBean.getFunctionalizingEntityClassNames();
		String[] functionClassNames = sampleBean.getFunctionClassNames();
		String[] nanomaterialEntityClassNames = sampleBean.getNanomaterialEntityClassNames();
		
		
		
		
		
	}

	public void saveDataAvailability(SampleBean sampleBean){
		
	}
	
	public void deleteDataAvailability(String sampleId){
		
		String sql = "delete from data_availability where sample_id = " + sampleId;
		
	}

	private static final class DataAvailabilityMapper implements RowMapper {

		@Override
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
}
