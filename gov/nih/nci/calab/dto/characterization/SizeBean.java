package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.physical.Size;
import gov.nih.nci.calab.domain.nano.characterization.Characterization;
import gov.nih.nci.calab.domain.nano.characterization.CharacterizationTable;

import java.util.List;


/**
 * This class represents the size characterization information to be shown in
 * the view page.
 * 
 * @author pansu
 * 
 */
public class SizeBean extends CharacterizationBean {
	public SizeBean() {
		super();
		initSetup();
	}
	
	public SizeBean(Characterization aChar) {
		super();
		this.setCharacterizationSource(aChar.getSource());
		this.setViewTitle(aChar.getIdentificationName());
		this.setDescription(aChar.getDescription());
		if (aChar.getInstrument() != null) {
			this.getInstrument().setType(aChar.getInstrument().getType());
			this.getInstrument().setDescription(aChar.getInstrument().getDescription());
			this.getInstrument().setManufacturer(aChar.getInstrument().getManufacturer());
		}
		this.setNumberOfCharacterizationTables(Integer.valueOf(aChar.getCharacterizationTableCollection().size()).toString());
		for (CharacterizationTable table : aChar.getCharacterizationTableCollection()) {
			CharacterizationTableBean ctBean = new CharacterizationTableBean(table);
			this.getCharacterizationTables().add(ctBean);
		}
	}
	
	public void setCharacterizationTables(
			List<CharacterizationTableBean> characterizationTables) {
		super.setCharacterizationTables(characterizationTables);
		initSetup();
	}
	
	public void initSetup() {
		for (CharacterizationTableBean table:getCharacterizationTables()) {
			CharacterizationTableDataBean average=new CharacterizationTableDataBean();
			average.setType("Average");
			average.setValueUnit("nm");
			CharacterizationTableDataBean zaverage=new CharacterizationTableDataBean();
			zaverage.setType("Z-Average");
			zaverage.setValueUnit("nm");
			CharacterizationTableDataBean pdi=new CharacterizationTableDataBean();
			pdi.setType("PDI");
			table.getTableDataList().add(average);
			table.getTableDataList().add(zaverage);
			table.getTableDataList().add(pdi);
		}
	}
	
	public Size getDomainObj() {
		Size size = new Size();
		super.updateDomainObj(size);
		return size;
	}
}
