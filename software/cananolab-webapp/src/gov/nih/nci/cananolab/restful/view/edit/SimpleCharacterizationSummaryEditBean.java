package gov.nih.nci.cananolab.restful.view.edit;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.sample.InitCharacterizationSetup;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.restful.view.SimpleCharacterizationsByTypeBean;

/**
 * Class to support Characterization edit main page. This page is almost the same as Characterization 
 * view page, except that all characterization types need be present, even there is no char of a type.
 * 
 * @author yangs8
 *
 */
public class SimpleCharacterizationSummaryEditBean extends SimpleCharacterizationSummaryViewBean {
	
	List<SimpleCharacterizationsByTypeBean> allCharTypeBeans = new ArrayList<SimpleCharacterizationsByTypeBean>();
	
	public List<SimpleCharacterizationsByTypeBean> transferData(HttpServletRequest request, 
			CharacterizationSummaryViewBean viewBean, String sampleId) 
			throws Exception {
		
		List<String> allCharacterizationTypes = InitCharacterizationSetup
				.getInstance().getCharacterizationTypes(request);
		
		List<SimpleCharacterizationsByTypeBean> charByTypeBeans = 
				super.transferData(request, viewBean, sampleId); 
		
		for (String aType : allCharacterizationTypes) {
			boolean exists = false;
			for (SimpleCharacterizationsByTypeBean charBean : charByTypeBeans) {
				if (charBean.getType().equals(aType)) {
					charBean.setParentSampleId(Long.parseLong(sampleId));
					allCharTypeBeans.add(charBean);
					exists = true;
					break;
				}
			}
			
			if (!exists) {
				SimpleCharacterizationsByTypeBean emptyBean = new SimpleCharacterizationsByTypeBean();
				emptyBean.setType(aType);
				emptyBean.setParentSampleId(Long.parseLong(sampleId));
				allCharTypeBeans.add(emptyBean);
			}
		}
		
		//place holder
		SimpleCharacterizationsByTypeBean other = new SimpleCharacterizationsByTypeBean();
		other.setType("other");
		other.setParentSampleId(Long.parseLong(sampleId));
		allCharTypeBeans.add(other);
		
		return allCharTypeBeans;
	}

	public List<SimpleCharacterizationsByTypeBean> getAllCharTypeBeans() {
		return allCharTypeBeans;
	}

	public void setAllCharTypeBeans(
			List<SimpleCharacterizationsByTypeBean> allCharTypeBeans) {
		this.allCharTypeBeans = allCharTypeBeans;
	}
}
