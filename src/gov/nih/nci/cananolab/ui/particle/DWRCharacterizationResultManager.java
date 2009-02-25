package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.dto.common.DataColumnBean;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationResultServiceLocalImpl;

import java.util.List;

import org.apache.struts.validator.DynaValidatorForm;
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

	// addColumnHeader includes the function add and edit
	//RETIRED
	public DataSetBean addColumnHeader(Datum datum)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		theDataSet.getTheDataRow().addDatumColumn(datum);
		return theDataSet;
	}
	
//	public DataSetBean addColumnHeader(DataColumnBean columnBean)
//		throws CharacterizationResultException {
//		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
//				.get().getSession().getAttribute("characterizationForm"));
//		CharacterizationBean charBean = (CharacterizationBean) (charForm
//				.get("achar"));
//		DataSetBean theDataSet = charBean.getTheDataSet();
//		theDataSet.addColumnBean(columnBean);
//		return theDataSet;
//	}
	
	public DataSetBean addConditionColumnHeader(Condition condition)
			throws CharacterizationResultException {
		System.out.println("########### addConditionColumnHeader");
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		theDataSet.getTheDataRow().addConditionColumn(condition);
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
		if (data != null) {
			// TODO:: if rowid!=null, get the row then update
			// DataRowBean dataRowBean = theDataSet.getTheDataRow();
			DataRowBean dataRowBean = null;
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
	public DataSetBean deleteRow(List<Datum> data)
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
				dataRowBean.removeDatum(datum);
				i++;
			}
			if (dataRowBean != null
					&& (dataRowBean.getData() == null || dataRowBean.getData()
							.size() == 0)
					&& (dataRowBean.getConditions() == null || dataRowBean
							.getConditions().size() == 0)) {
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
}
