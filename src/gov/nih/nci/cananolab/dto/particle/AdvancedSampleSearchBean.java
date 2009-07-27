package gov.nih.nci.cananolab.dto.particle;


import java.util.ArrayList;
import java.util.List;

/**
 * information needed for the advanced sample search form
 * 
 * @author pansu
 *
 */
public class AdvancedSampleSearchBean {
	private List<CompositionQueryBean> compositionQueries = new ArrayList<CompositionQueryBean>();
	private List<CharacterizationQueryBean> characterizationQueries = new ArrayList<CharacterizationQueryBean>();
	private CompositionQueryBean theCompositionQuery = new CompositionQueryBean();
	private CharacterizationQueryBean theCharacterizationQuery = new CharacterizationQueryBean();
	private String compositionLogicalOperator;
	private String characterizationLogicalOperator;
	public List<CompositionQueryBean> getCompositionQueries() {
		return compositionQueries;
	}
	public void setCompositionQueries(List<CompositionQueryBean> compositionQueries) {
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
}
