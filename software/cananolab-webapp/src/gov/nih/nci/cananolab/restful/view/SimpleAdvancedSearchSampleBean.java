package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	
	List<String> orgNames = new ArrayList<String>();
	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctioningEntity;
	boolean showFuntion;
	
	
	
	public boolean isShowPOC() {
		return showPOC;
	}


	public void setShowPOC(boolean showPOC) {
		this.showPOC = showPOC;
	}


	public boolean isShowNanomaterialEntity() {
		return showNanomaterialEntity;
	}


	public void setShowNanomaterialEntity(boolean showNanomaterialEntity) {
		this.showNanomaterialEntity = showNanomaterialEntity;
	}


	public boolean isShowFunctioningEntity() {
		return showFunctioningEntity;
	}


	public void setShowFunctioningEntity(boolean showFunctioningEntity) {
		this.showFunctioningEntity = showFunctioningEntity;
	}


	public boolean isShowFuntion() {
		return showFuntion;
	}


	public void setShowFuntion(boolean showFuntion) {
		this.showFuntion = showFuntion;
	}


	public List<String> getOrgNames() {
		return orgNames;
	}


	public void setOrgNames(List<String> orgNames) {
		this.orgNames = orgNames;
	}


	public void transferAdvancedSampleBeanForResultView(AdvancedSampleBean sampleBean,
			UserBean user, AdvancedSampleSearchBean searchBean) {

		if (sampleBean == null)
			return;
		
		setSampleId(sampleBean.getDomainSample().getId());
		setSampleName(sampleBean.getDomainSample().getName());
		
		//Only if queries contain poc type
		populatePOCs(sampleBean, searchBean);
		

		

	
//
//		setFunctions(sampleBean.getFunctionClassNames());
//		setCharacterizations(sampleBean.getCharacterizationClassNames());
//		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
//		setCreatedDate(sampleBean.getPrimaryPOCBean().getDomain()
//				.getCreatedDate());
//
//		//editable = SecurityUtil.isEntityEditableForUser(sampleBean.getUserAccesses(), user);
//		editable = sampleBean.getUserUpdatable();

		//editable = SecurityUtil.isEntityEditableForUser(sampleBean.getUserAccesses(), user);
		editable = false; ///sampleBean.getUserUpdatable();
	}
	
	protected void populatePOCs(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasPOC())
			return;
		
		this.showPOC = true;
		
		List<PointOfContact> pocs = sampleBean.getPointOfContacts();

		if (pocs != null) {
			for (PointOfContact poc : pocs) {
				PointOfContactBean pocBean = new PointOfContactBean(poc);
				this.orgNames.add(pocBean.getOrganizationDisplayName());
			}

		}

	}
	
	protected void populateNanomaterialEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasNanomaterial())
			return;
		
		SampleComposition comp = sampleBean.getDomainSample().getSampleComposition();
		
		//TODO: need to filter for this only
		//query.getCompositionType().equals("nanomaterial entity")
		//query.getCompositionType().equals("function")
		//query.getCompositionType().equals("functionalizing entity")
		

		if (comp != null) {
			Collection<NanomaterialEntity> nanocoll = comp
					.getNanomaterialEntityCollection();
			String[] v = new String[nanocoll.size()];
			Iterator ite = nanocoll.iterator();

			int i = 0;
			while (ite.hasNext()) {
				NanomaterialEntity n = (NanomaterialEntity) ite.next();
				
				String cn = ClassUtils.getShortClassName(n.getClass().getName());
				if (n instanceof OtherNanomaterialEntity)
					cn = ((OtherNanomaterialEntity)n).getType();

				v[i++] = cn;
			}
			setComposition(v);
		}
	}
}
