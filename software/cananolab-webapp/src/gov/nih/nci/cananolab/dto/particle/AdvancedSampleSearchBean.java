/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * information needed for the advanced sample search form
 *
 * @author pansu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvancedSampleSearchBean {
	private List<SampleQueryBean> sampleQueries = new ArrayList<SampleQueryBean>();
	private List<CompositionQueryBean> compositionQueries = new ArrayList<CompositionQueryBean>();
	private List<CharacterizationQueryBean> characterizationQueries = new ArrayList<CharacterizationQueryBean>();
	
	private SampleQueryBean theSampleQuery = new SampleQueryBean();
	private CompositionQueryBean theCompositionQuery = new CompositionQueryBean();
	private CharacterizationQueryBean theCharacterizationQuery = new CharacterizationQueryBean();
	
	private String sampleLogicalOperator = "and";
	private String compositionLogicalOperator = "and";
	private String characterizationLogicalOperator = "and";
	private String logicalOperator = "or";
	private int nanoEntityCount = 0, funcEntityCount = 0, funcCount = 0,
			pocCount = 0, datumTypeCount = 0;
	
	Map<String, Object> setupMap = new HashMap<String, Object>();

	

	public Map<String, Object> getSetupMap() {
		return setupMap;
	}

	public void setSetupMap(Map<String, Object> setupMap) {
		this.setupMap = setupMap;
	}

	public List<CompositionQueryBean> getCompositionQueries() {
		return compositionQueries;
	}

	public void setCompositionQueries(
			List<CompositionQueryBean> compositionQueries) {
		this.compositionQueries = compositionQueries;
	}

	public List<CharacterizationQueryBean> getCharacterizationQueries() {
		return characterizationQueries;
	}

	public void setCharacterizationQueries(
			List<CharacterizationQueryBean> characterizationQueries) {
		this.characterizationQueries = characterizationQueries;
	}

	public CompositionQueryBean getTheCompositionQuery() {
		return theCompositionQuery;
	}

	public void setTheCompositionQuery(CompositionQueryBean theCompositionQuery) {
		this.theCompositionQuery = theCompositionQuery;
	}

	public CharacterizationQueryBean getTheCharacterizationQuery() {
		return theCharacterizationQuery;
	}

	public void setTheCharacterizationQuery(
			CharacterizationQueryBean theCharacterizationQuery) {
		this.theCharacterizationQuery = theCharacterizationQuery;
	}

	public String getCompositionLogicalOperator() {
		return compositionLogicalOperator;
	}

	public void setCompositionLogicalOperator(String compositionLogicalOperator) {
		this.compositionLogicalOperator = compositionLogicalOperator;
	}

	public String getCharacterizationLogicalOperator() {
		return characterizationLogicalOperator;
	}

	public void setCharacterizationLogicalOperator(
			String characterizationLogicalOperator) {
		this.characterizationLogicalOperator = characterizationLogicalOperator;
	}

//	public void addQuery(BaseQueryBean query) {
//		if (query instanceof SampleQueryBean) {
//			int index = sampleQueries.indexOf(query);
//			if (index != -1) {
//				sampleQueries.remove(query);
//				// retain the original order`
//				sampleQueries.add(index, (SampleQueryBean) query);
//			} else {
//				SampleQueryBean sampleQuery=(SampleQueryBean)query;
//				//set operand empty if name is empty
//				if (StringUtils.isEmpty(sampleQuery.getName())) {
//					sampleQuery.setOperand("");
//				}
//				sampleQueries.add(sampleQuery);
//			}
//		} else if (query instanceof CompositionQueryBean) {
//			int index = compositionQueries.indexOf(query);
//			if (index != -1) {
//				compositionQueries.remove(query);
//				// retain the original order
//				compositionQueries.add(index, (CompositionQueryBean) query);
//			} else {
//				CompositionQueryBean compQuery = (CompositionQueryBean) query;
//				//set operand empty if chemical name is empty
//				if (StringUtils.isEmpty(compQuery.getChemicalName())) {
//					compQuery.setOperand("");
//				}
//				compositionQueries.add(compQuery);
//			}
//		} else if (query instanceof CharacterizationQueryBean) {
//			int index = characterizationQueries.indexOf(query);
//			if (index != -1) {
//				characterizationQueries.remove(query);
//				// retain the original order
//				characterizationQueries.add(index,
//						(CharacterizationQueryBean) query);
//			} else {
//				CharacterizationQueryBean charQuery = (CharacterizationQueryBean) query;
//				//set operand and unit empty if value is empty
//				if (StringUtils.isEmpty(charQuery.getDatumValue())) {
//					charQuery.setOperand("");
//					charQuery.setDatumValueUnit("");
//				}
//				characterizationQueries.add(charQuery);
//			}
//		}
//	}

//	public void removeQuery(BaseQueryBean query) {
//		if (query instanceof SampleQueryBean) {
//			sampleQueries.remove((SampleQueryBean) query);
//		} else if (query instanceof CompositionQueryBean) {
//			compositionQueries.remove((CompositionQueryBean) query);
//		} else if (query instanceof CharacterizationQueryBean) {
//			characterizationQueries.remove((CharacterizationQueryBean) query);
//		}
//	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public Boolean getHasNanomaterial() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("nanomaterial entity")) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasAgentMaterial() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("functionalizing entity")) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasFunction() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("function")) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasPOC() {
		for (SampleQueryBean query : sampleQueries) {
			if (query.getNameType().equals("point of contact name")) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasDatum() {
		for (CharacterizationQueryBean charQuery : characterizationQueries) {
			if (!StringUtils.isEmpty(charQuery.getDatumName())
					|| !StringUtils.isEmpty(charQuery.getDatumValue())) {
				return true;
			}
		}
		return false;
	}

	public Boolean getHasChemicalName() {
		for (CompositionQueryBean query : getCompositionQueries()) {
			if (!StringUtils.isEmpty(query.getChemicalName())) {
				return true;
			}
		}
		return false;
	}

	public List<SampleQueryBean> getSampleQueries() {
		return sampleQueries;
	}

	public void setSampleQueries(List<SampleQueryBean> sampleQueries) {
		this.sampleQueries = sampleQueries;
	}

	public SampleQueryBean getTheSampleQuery() {
		return theSampleQuery;
	}

	public void setTheSampleQuery(SampleQueryBean theSampleQuery) {
		this.theSampleQuery = theSampleQuery;
	}

	public String getSampleLogicalOperator() {
		return sampleLogicalOperator;
	}

	public void setSampleLogicalOperator(String sampleLogicalOperator) {
		this.sampleLogicalOperator = sampleLogicalOperator;
	}

	private String getQueryDisplayName(
			List<? extends BaseQueryBean> queryBeans, String operator) {
		List<String> strs = new ArrayList<String>();
		for (BaseQueryBean query : queryBeans) {
			strs.add(query.getDisplayName());
		}
		String name = StringUtils.join(strs, "<br>" + operator + "<br>");
		if (StringUtils.isEmpty(name)) {
			return name;
		} else {
			return "(" + name + ")";
		}
	}

	public String getDisplayName() {
		List<String> strs = new ArrayList<String>();
		strs.add(getQueryDisplayName(sampleQueries, sampleLogicalOperator));
		strs.add(getQueryDisplayName(compositionQueries,
				compositionLogicalOperator));
		strs.add(getQueryDisplayName(characterizationQueries,
				characterizationLogicalOperator));
		return StringUtils.join(strs, "<br>" + logicalOperator + "<br>");
	}

	public List<String> getQueryAsColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		for (SampleQueryBean query : sampleQueries) {
			if (!StringUtils.isEmpty(query.getQueryAsColumnName()))
				columnNames.add(query.getQueryAsColumnName());
		}
		for (CompositionQueryBean query : compositionQueries) {
			columnNames.add(query.getQueryAsColumnName());
		}
		for (CharacterizationQueryBean query : characterizationQueries) {
			columnNames.add(query.getQueryAsColumnName());
		}
		return columnNames;
	}

	public void updateQueries() {
		pocCount = 0;
		nanoEntityCount = 0;
		funcEntityCount = 0;
		funcCount = 0;
		datumTypeCount = 0;
		// count how many each type of queries are
		for (SampleQueryBean q : sampleQueries) {
			if (q.getNameType().equals("point of contact name")) {
				pocCount++;
			}
		}
		for (CompositionQueryBean query : getCompositionQueries()) {
			if (query.getCompositionType().equals("nanomaterial entity")) {
				nanoEntityCount++;
			} else if (query.getCompositionType().equals(
					"functionalizing entity")) { 
				funcEntityCount++;
				
			} else if (query.getCompositionType().equals("function")) {
				funcCount++;
				if (query.getOperand() == null || query.getOperand().length() == 0)
					query.setOperand("contains");
			}
			
			String entType = query.getEntityType();
			if (entType != null && entType.startsWith("["))
				query.setEntityType(entType.substring(1, entType.length()-1));
			
			int i = 0;
		}
		// how many types of datum and set assay type if any
		Set<String> datumNames = new HashSet<String>();
		for (CharacterizationQueryBean query : getCharacterizationQueries()) {
			// set assay type and characterization name
			if (query.getCharacterizationName().contains(":")) {
				int ind = query.getCharacterizationName().indexOf(":");
				query.setAssayType(query.getCharacterizationName().substring(
						ind + 1));
				query.setCharacterizationName(query.getCharacterizationName()
						.substring(0, ind));
			}
			
			if( query.getCharacterizationName().startsWith("other")) {
				query.setCharacterizationName("OtherCharacterization");
			}
			datumNames.add(query.getDatumName());
		}
		datumTypeCount = datumNames.size();
	}

	public int getNanoEntityCount() {
		return nanoEntityCount;
	}

	public int getFuncEntityCount() {
		return funcEntityCount;
	}

	public int getFuncCount() {
		return funcCount;
	}

	public int getPocCount() {
		return pocCount;
	}

	public int getDatumTypeCount() {
		return datumTypeCount;
	}
	
	
}
