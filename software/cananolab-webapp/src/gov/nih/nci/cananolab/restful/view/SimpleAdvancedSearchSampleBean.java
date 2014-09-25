package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.nanomaterial.OtherNanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.ComposingElement;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleBean;
import gov.nih.nci.cananolab.dto.particle.AdvancedSampleSearchBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.restful.sample.AdvancedSampleSearchBO;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	private Logger logger = Logger.getLogger(SimpleAdvancedSearchSampleBean.class);
	
	List<String> orgNames = new ArrayList<String>();
	List<String> nanomaterialEntityNames = new ArrayList<String>(); 
	String functionalizingEntityName;
	List<String> functionNames = new ArrayList<String>();
	
	List<SimpleCharacterizationsByTypeBean> charsByType = new ArrayList<SimpleCharacterizationsByTypeBean>();
	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctionalizingEntity;
	boolean showFunction;
	
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



	public boolean isShowFunctionalizingEntity() {
		return showFunctionalizingEntity;
	}


	public void setShowFunctionalizingEntity(boolean showFunctionalizingEntity) {
		this.showFunctionalizingEntity = showFunctionalizingEntity;
	}


	public List<String> getOrgNames() {
		return orgNames;
	}


	public void setOrgNames(List<String> orgNames) {
		this.orgNames = orgNames;
	}


	public List<String> getNanomaterialEntityNames() {
		return nanomaterialEntityNames;
	}


	public void setNanomaterialEntityNames(List<String> nanomaterialEntityNames) {
		this.nanomaterialEntityNames = nanomaterialEntityNames;
	}

	

	public String getFunctionalizingEntityName() {
		return functionalizingEntityName;
	}


	public void setFunctionalizingEntityName(String functionalizingEntityName) {
		this.functionalizingEntityName = functionalizingEntityName;
	}


	public List<String> getFunctionNames() {
		return functionNames;
	}


	public void setFunctionNames(List<String> functionNames) {
		this.functionNames = functionNames;
	}


	public boolean isShowFunction() {
		return showFunction;
	}


	public void setShowFunction(boolean showFunction) {
		this.showFunction = showFunction;
	}


	public void transferAdvancedSampleBeanForResultView(AdvancedSampleBean sampleBean,
			UserBean user, AdvancedSampleSearchBean searchBean) {

		if (sampleBean == null)
			return;
		
		setSampleId(sampleBean.getDomainSample().getId());
		setSampleName(sampleBean.getDomainSample().getName());
		
		populatePOCs(sampleBean, searchBean);
		populateNanomaterialEntity(sampleBean, searchBean);
		populateFunctionalizingEntity(sampleBean, searchBean);
		populateFunctionNames(sampleBean, searchBean);
		
		populateCharacterizations(sampleBean, searchBean);

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
		
		this.showNanomaterialEntity = true;
		
		List<NanomaterialEntity> nanos = sampleBean.getNanomaterialEntities();
		if (nanos == null)
			return;
		
		for (NanomaterialEntity nano : nanos) {
			String cn = ClassUtils.getShortClassName(nano.getClass().getName());
			if (nano instanceof OtherNanomaterialEntity)
				cn = ((OtherNanomaterialEntity)nano).getType();
			
			this.nanomaterialEntityNames.add(cn);
			
			Collection<ComposingElement> ces = nano.getComposingElementCollection();
			if (ces != null) {
				for (ComposingElement ce : ces) {
					ComposingElementBean ceBean = new ComposingElementBean(ce);
					String val = ceBean.getAdvancedSearchDisplayName();
					this.nanomaterialEntityNames.add(val);
				}
			}
		}
	}
	
	protected void populateFunctionalizingEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasAgentMaterial())
			return;
		
		this.showFunctionalizingEntity = true;
		
		List<FunctionalizingEntity> fes = sampleBean.getFunctionalizingEntities();
		if (fes == null)
			return;
		
		for (FunctionalizingEntity fe : fes) {
			FunctionalizingEntityBean fBean = new FunctionalizingEntityBean(fe);
			this.functionalizingEntityName = fBean.getAdvancedSearchDisplayName();
			
		}
	}
	
	protected void populateFunctionNames(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasFunction())
			return;
		
		this.showFunction = true;
		
		List<Function> functions = sampleBean.getFunctions();
		if (functions == null)
			return;
		
		for (Function fe : functions) {
			FunctionBean fbean = new FunctionBean(fe);
			
			//TODO: data doesn't seems to be formatted right.
			String[] dns = fbean.getTargetDisplayNames();
			if (dns != null)
				Collections.addAll(this.functionNames, dns);
		}
	}
	
	protected void populateCharacterizations(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasDatum())
			return;
		
		List<Characterization> chars = sampleBean.getCharacterizations();

		if (chars == null) 
			return;
		
		for (Characterization achar : chars) {
			CharacterizationBean charBean = new CharacterizationBean(achar);
			String type = charBean.getCharacterizationType();
			String assayType = charBean.getAssayType();
			String name = charBean.getCharacterizationName();
			
			logger.debug("Type: " + type + " Name: " + name + " assayType: " + assayType);
			
		}

	}
}
