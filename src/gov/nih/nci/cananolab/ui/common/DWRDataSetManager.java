package gov.nih.nci.cananolab.ui.common;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.dto.common.DataSetBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.ExperimentConfigBean;
import gov.nih.nci.cananolab.exception.DataSetException;
import gov.nih.nci.cananolab.service.common.ExperimentConfigService;
import gov.nih.nci.cananolab.service.common.impl.ExperimentConfigServiceLocalImpl;

import java.util.List;

import org.apache.struts.validator.DynaValidatorForm;
import org.directwebremoting.WebContextFactory;

/**
 * Methods for DWR Ajax
 *
 * @author pansu, tanq
 *
 */
public class DWRDataSetManager {
	private ExperimentConfigService service = new ExperimentConfigServiceLocalImpl();

	public DataSetBean resetDataSet() {
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean newDataSetBean = new DataSetBean();
		charBean.setTheDataSet(newDataSetBean);
		return newDataSetBean;
	}


	//addColumnHeader includes the function add and edit
	public DataSetBean addColumnHeader(Datum datum)
		throws DataSetException {		
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		theDataSet.getTheDataRow().addDatum(datum);
		return theDataSet;
	}
	
	//addRow includes the function add and edit
	public DataSetBean addRow(List<Datum> data)
		throws DataSetException {		
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		DataSetBean theDataSet = charBean.getTheDataSet();
		if (data!=null) {
			for (Datum datum: data) {
				System.out.println("########### name="+datum.getName());
			}
		}
		return theDataSet;
	}

	public ExperimentConfigBean deleteInstrument(
			ExperimentConfigBean theExperimentConfig, Instrument instrument)
			throws DataSetException {
		theExperimentConfig.removeInstrument(instrument);
		DynaValidatorForm charForm = (DynaValidatorForm) (WebContextFactory
				.get().getSession().getAttribute("characterizationForm"));
		CharacterizationBean charBean = (CharacterizationBean) (charForm
				.get("achar"));
		charBean.setTheExperimentConfig(theExperimentConfig);
		return theExperimentConfig;
	}
}
