package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;
import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * information needed for the advanced sample search form
 * 
 * @author pansu
 * 
 */
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
	private Boolean hasNanomaterial = false;
	private Boolean hasAgentMaterial = false;
	private Boolean hasPOC = false;
	private Boolean hasFunction = false;

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

	public void addQuery(BaseQueryBean query) {
		if (query instanceof SampleQueryBean) {
			int index = compositionQueries.indexOf(query);
			if (index != -1) {
				sampleQueries.remove(query);
				// retain the original order`
				sampleQueries.add(index, (SampleQueryBean) query);
			} else {
				sampleQueries.add((SampleQueryBean) query);
			}
		} else if (query instanceof CompositionQueryBean) {
			int index = compositionQueries.indexOf(query);
			if (index != -1) {
				compositionQueries.remove(query);
				// retain the original order
				compositionQueries.add(index, (CompositionQueryBean) query);
			} else {
				compositionQueries.add((CompositionQueryBean) query);
			}
		} else if (query instanceof CharacterizationQueryBean) {
			int index = characterizationQueries.indexOf(query);
			if (index != -1) {
				characterizationQueries.remove(query);
				// retain the original order
				characterizationQueries.add(index,
						(CharacterizationQueryBean) query);
			} else {
				characterizationQueries.add((CharacterizationQueryBean) query);
			}
		}
	}

	public void removeQuery(BaseQueryBean query) {
		if (query instanceof SampleQueryBean) {
			sampleQueries.remove((SampleQueryBean) query);
		} else if (query instanceof CompositionQueryBean) {
			compositionQueries.remove((CompositionQueryBean) query);
		} else if (query instanceof CharacterizationQueryBean) {
			characterizationQueries.remove((CharacterizationQueryBean) query);
		}
	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public Boolean getHasNanomaterial() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("nanomaterial entity")) {
				hasNanomaterial = true;
				break;
			}
		}
		return hasNanomaterial;
	}

	public Boolean getHasAgentMaterial() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("functionalizing entity")) {
				hasAgentMaterial = true;
				break;
			}
		}
		return hasAgentMaterial;
	}

	public Boolean getHasFunction() {
		for (CompositionQueryBean query : compositionQueries) {
			if (query.getCompositionType().equals("function")) {
				hasFunction = true;
				break;
			}
		}
		return hasFunction;
	}

	public Boolean getHasPOC() {
		for (SampleQueryBean query : sampleQueries) {
			if (query.getNameType().equals("point of contact name")) {
				hasPOC = true;
				break;
			}
		}
		return hasPOC;
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
		String name = StringUtils.join(strs, "<br>"+operator + "<br>");
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
		return StringUtils.join(strs, "<br>"+logicalOperator+"<br>");
	}
}
