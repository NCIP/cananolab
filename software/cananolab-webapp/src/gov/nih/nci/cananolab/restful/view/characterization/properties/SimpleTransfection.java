package gov.nih.nci.cananolab.restful.view.characterization.properties;

import gov.nih.nci.cananolab.domain.characterization.invitro.Transfection;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonTypeName;

@JsonTypeName("SimpleTransfection")
public class SimpleTransfection extends SimpleCharacterizationProperty {
	String cellLine;
	
	

	@Override
	public void setLookups(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transferFromPropertyBean(HttpServletRequest request, CharacterizationBean charBean, boolean needOptions)
			throws Exception {
		super.transferFromPropertyBean(request, charBean, needOptions);
		
		Transfection transfection = charBean.getTransfection();
		if (transfection.getCellLine() != null)
			this.cellLine = transfection.getCellLine();
		
	}

	@Override
	public List<String> getPropertyViewTitles() {
		List<String> vals = new ArrayList<String>();
		vals.add("Cell Line");
		return vals;
	}

	@Override
	public List<String> getPropertyViewValues() {
		List<String> vals = new ArrayList<String>();
		vals.add(this.cellLine);
		return vals;
	}

	@Override
	public void transferToPropertyBean(CharacterizationBean charBean)
			throws Exception {
		Transfection transfection = charBean.getTransfection();
		
		if (this.cellLine != null)
			transfection.setCellLine(this.cellLine);
	}



	public String getCellLine() {
		return cellLine;
	}

	public void setCellLine(String cellLine) {
		this.cellLine = cellLine;
	}
	
	
}
