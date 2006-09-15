package gov.nih.nci.calab.dto.characterization;

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
		for (CharacterizationTableBean table: getCharacterizationTables()) {
			CharacterizationTableDataBean average=new CharacterizationTableDataBean();
			average.setType("Average");
			CharacterizationTableDataBean zaverage=new CharacterizationTableDataBean();
			average.setType("Z-Average");
			CharacterizationTableDataBean pdi=new CharacterizationTableDataBean();
			average.setType("PDI");
			table.getTableDataList().add(average);
			table.getTableDataList().add(zaverage);
			table.getTableDataList().add(pdi);
		}
	}
	
	public void setCharacterizationTables(
			List<CharacterizationTableBean> characterizationTables) {
		super.setCharacterizationTables(characterizationTables);
		
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
}
