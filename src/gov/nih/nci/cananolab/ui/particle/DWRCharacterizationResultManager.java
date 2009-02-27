package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.dto.common.DataColumnBean;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationResultServiceLocalImpl;

import java.util.List;
import java.util.SortedSet;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 * 
 * @author pansu, tanq
 * 
 */
public class DWRCharacterizationResultManager {
	long tempDataRowId = -1;
	private NanoparticleCharacterizationResultService service = new NanoparticleCharacterizationResultServiceLocalImpl();

	public DataSetBean resetDataSet() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean newDataSetBean = new DataSetBean();
		charBean.setTheDataSet(newDataSetBean);
		return newDataSetBean;
	}

	public DataSetBean addColumnHeader(DataColumnBean columnBean)
		throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (columnBean.getDatumOrCondition().equalsIgnoreCase("Condition")) {
			theDataSet.addConditionColumnBean(columnBean);
		}else {
			theDataSet.addDatumColumnBean(columnBean);
		}
		return theDataSet;
	}
	
	public DataSetBean removeColumnHeader(DataColumnBean columnBean)
		throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (columnBean.getDatumOrCondition().equalsIgnoreCase("Condition")) {
			theDataSet.removeConditionColumnBean(columnBean);
		}else {
			theDataSet.removeDatumColumnBean(columnBean);
		}
		return theDataSet;
	}


	// addRow includes the function add and edit
	public DataSetBean addRow(List<Datum> data, List<Condition> conditions)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		DataRowBean dataRowBean = null;
		if (data != null) {
			boolean hasData = false;
			int i = 0;
			for (Datum datum : data) {
				if (theDataSet.getDomain().getId() != null) {
					datum.setDataSet(theDataSet.getDomain());
				}
				if (i == 0) {
					dataRowBean = theDataSet.getDataRowBean(datum);
					if (dataRowBean == null) {
						dataRowBean = new DataRowBean();
					}
				}				
//				if (conditions != null) {					
//					datum.setConditionCollection(conditions);
//				}
				dataRowBean.setConditions(conditions);
				dataRowBean.addDatum(datum);
				hasData = true;
				i++;
			}
			if (hasData) {
				
				// if dataRowBean.id is null, set a negative id temporary
				// need to handle set nagative id to null when save to db
				if (dataRowBean.getDomain().getId() == null) {
					dataRowBean.getDomain().setId(tempDataRowId--);
				}
				theDataSet.addDataRow(dataRowBean);
			}
		}		
		return theDataSet;
	}

	// addRow includes the function add and edit
	public DataSetBean deleteRow(List<Datum> data, List<Condition> conditions)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (data != null) {
			DataRowBean dataRowBean = null;
			int i = 0;
			for (Datum datum : data) {
				if (theDataSet.getDomain().getId() != null) {
					datum.setDataSet(theDataSet.getDomain());
				}
				if (i == 0) {
					dataRowBean = theDataSet.getDataRowBean(datum);
					if (dataRowBean == null) {
						return theDataSet;
					}					
				}		
				if (conditions!=null && conditions.size()>0) {
					for (Condition condition : conditions) {
						dataRowBean.removeCondition(condition);
					}
				}
				dataRowBean.removeDatum(datum);
				i++;
			}
			
			if (dataRowBean != null
					&& (dataRowBean.getData() == null || dataRowBean.getData()
							.size() == 0)) {
				theDataSet.removeDataRow(dataRowBean);
			}
		}
		return theDataSet;
	}

	public DataSetBean getDataSetBeanBy(String dataSetId) throws Exception {
		List<Datum> data = service.getDataForDataSet(dataSetId);
		DataSetBean dataSetBean = new DataSetBean(data);
		return dataSetBean;
	}

	public DataSetBean findDataSetById(String dataSetId)
			throws CharacterizationResultException {
		List<Datum> data = service.getDataForDataSet(dataSetId);
		DataSetBean dataSetBean = new DataSetBean(data);
		dataSetBean.setTheDataRow(dataSetBean.getDataRows().get(0));
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		charBean.setTheDataSet(dataSetBean);
		return dataSetBean;
	}
	
	
	public String[] getConditionOptions() throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> conditions = InitCharacterizationSetup.getInstance()
				.getConditions(wctx.getHttpServletRequest());
		return conditions.toArray(new String[conditions.size()]);
	}

	public String[] getConditionPropertyOptions(String conditionName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();

		SortedSet<String> properties = LookupService
				.getDefaultAndOtherLookupTypes(conditionName, "property",
						"otherProperty");
		return properties.toArray(new String[properties.size()]);
	}

	public String[] getDatumNameOptions(String characterizationName)
			throws Exception {
		WebContext wctx = WebContextFactory.get();
		SortedSet<String> names = InitCharacterizationSetup.getInstance()
				.getDatumNamesByCharName(wctx.getHttpServletRequest(),
						characterizationName);
		return names.toArray(new String[names.size()]);
	}
}
