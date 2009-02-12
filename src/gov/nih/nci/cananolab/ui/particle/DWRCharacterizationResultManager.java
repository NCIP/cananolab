package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.DataRow;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.dto.common.DataRowBean;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.ExperimentConfigBean;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;
import gov.nih.nci.cananolab.exception.ExperimentConfigException;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationResultService;
import gov.nih.nci.cananolab.service.particle.impl.NanoparticleCharacterizationResultServiceLocalImpl;

import java.util.List;
import java.util.SortedMap;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
public class DWRCharacterizationResultManager {
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
	public DataSetBean addColumnHeader(Datum datum) throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		theDataSet.getTheDataRow().addDatum(datum);
		return theDataSet;
	}

	// addRow includes the function add and edit
	public DataSetBean addRow(List<Datum> data) throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (data != null) {			
			//TODO:: if rowid!=null, get the row then update
			DataRowBean dataRowBean = new DataRowBean();
			boolean hasData = false;
			for (Datum datum : data) {
				if (theDataSet.getDomain().getId()!=null) {
					datum.setDataSet(theDataSet.getDomain());
				}
				dataRowBean.addDatum(datum);
				hasData = true;
			}
			if (hasData) {
				theDataSet.addDataRow(dataRowBean);
			}
		}
		return theDataSet;
	}

	public DataSetBean deleteDataRow(DataRowBean dataRowBean)
			throws CharacterizationResultException {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (dataRowBean != null) {
			// TODO:: remove base on rowID?? or row index??
			theDataSet.removeDataRow(dataRowBean);
		}
		return theDataSet;
	}

	public DataSetBean getDataSetBeanBy(String dataSetId) throws Exception{
		List<Datum> data=service.getDataForDataSet(dataSetId);
		DataSetBean dataSetBean=new DataSetBean(data);
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
