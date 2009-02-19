package gov.nih.nci.cananolab.ui.particle;

import gov.nih.nci.cananolab.domain.common.Datum;
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
		//System.out.println("xxxxxxxxx datum.id="+datum.getId());
		DataSetBean theDataSet = charBean.getTheDataSet();
		theDataSet.getTheDataRow().addDatumColumn(datum);
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
			//DataRowBean dataRowBean = theDataSet.getTheDataRow();
			DataRowBean dataRowBean = null;
			boolean hasData = false;
			int i=0;
			for (Datum datum : data) {
				if (theDataSet.getDomain().getId()!=null) {
					datum.setDataSet(theDataSet.getDomain());
				}
				if (i==0) {
					dataRowBean = theDataSet.getDataRowBean(datum);
					if (dataRowBean == null){
						dataRowBean = new DataRowBean();
					}
				}

				dataRowBean.addDatum(datum);
				hasData = true;
				i++;
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
