package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.characterization.OtherCharacterization;
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
import gov.nih.nci.cananolab.dto.particle.CharacterizationQueryBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean;
import gov.nih.nci.cananolab.restful.bean.characterization.SimpleAdvancedCellBean;
import gov.nih.nci.cananolab.restful.bean.characterization.SimpleCharacterizationAdvancedSearchResultBean;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class SimpleAdvancedSearchSampleBean extends SimpleSearchSampleBean {
	private Logger logger = Logger.getLogger(SimpleAdvancedSearchSampleBean.class);
	
//	List<String> orgNames = new ArrayList<String>();
//	List<String> nanomaterialEntityNames = new ArrayList<String>(); 
//	String functionalizingEntityName;
//	List<String> functionNames = new ArrayList<String>();

	
	List<List<SimpleCharacterizationAdvancedSearchResultBean>> orderedChars = 
			new ArrayList<List<SimpleCharacterizationAdvancedSearchResultBean>>();
	
	
	List<SimpleAdvancedCellBean> columns = new ArrayList<SimpleAdvancedCellBean>();
	
	boolean showPOC;
	boolean showNanomaterialEntity;
	boolean showFunctionalizingEntity;
	boolean showFunction;

//	public List<List<SimpleCharacterizationAdvancedSearchResultBean>> getOrderedChars() {
//		return orderedChars;
//	}
//
//
//	public void setOrderedChars(
//			List<List<SimpleCharacterizationAdvancedSearchResultBean>> orderedChars) {
//		this.orderedChars = orderedChars;
//	}

	

	public boolean isShowPOC() {
		return showPOC;
	}



	public List<SimpleAdvancedCellBean> getColumns() {
		return columns;
	}



	public void setColumns(List<SimpleAdvancedCellBean> columns) {
		this.columns = columns;
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


//	public List<String> getOrgNames() {
//		return orgNames;
//	}
//
//
//	public void setOrgNames(List<String> orgNames) {
//		this.orgNames = orgNames;
//	}
//
//
//	public List<String> getNanomaterialEntityNames() {
//		return nanomaterialEntityNames;
//	}
//
//
//	public void setNanomaterialEntityNames(List<String> nanomaterialEntityNames) {
//		this.nanomaterialEntityNames = nanomaterialEntityNames;
//	}

	
//
//	public String getFunctionalizingEntityName() {
//		return functionalizingEntityName;
//	}
//
//
//	public void setFunctionalizingEntityName(String functionalizingEntityName) {
//		this.functionalizingEntityName = functionalizingEntityName;
//	}
//
//
//	public List<String> getFunctionNames() {
//		return functionNames;
//	}
//
//
//	public void setFunctionNames(List<String> functionNames) {
//		this.functionNames = functionNames;
//	}


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
		
		List<String> sampleIdName = new ArrayList<String>();
		sampleIdName.add(this.sampleName);
		sampleIdName.add(String.valueOf(this.sampleId));
		
		
		this.columns.add(new SimpleAdvancedCellBean("Sample", sampleIdName));
		
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
		List<String> orgNames = new ArrayList<String>();

		if (pocs != null) {
			for (PointOfContact poc : pocs) {
				PointOfContactBean pocBean = new PointOfContactBean(poc);
				orgNames.add(pocBean.getOrganizationDisplayName());
			}

		}

		this.columns.add(new SimpleAdvancedCellBean("POC", orgNames));
	}
	
	protected void populateNanomaterialEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasNanomaterial())
			return;
		
		this.showNanomaterialEntity = true;
		
		List<NanomaterialEntity> nanos = sampleBean.getNanomaterialEntities();
		if (nanos == null)
			return;
		
		List<String> nanomaterialEntityNames = new ArrayList<String>();
		
		for (NanomaterialEntity nano : nanos) {
			String cn = ClassUtils.getShortClassName(nano.getClass().getName());
			if (nano instanceof OtherNanomaterialEntity)
				cn = ((OtherNanomaterialEntity)nano).getType();
			
			nanomaterialEntityNames.add(cn);
			
			Collection<ComposingElement> ces = nano.getComposingElementCollection();
			if (ces != null) {
				for (ComposingElement ce : ces) {
					ComposingElementBean ceBean = new ComposingElementBean(ce);
					String val = ceBean.getAdvancedSearchDisplayName();
					nanomaterialEntityNames.add(val);
				}
			}
		}
		
		this.columns.add(new SimpleAdvancedCellBean("Nanomaterial", nanomaterialEntityNames));
	}
	
	protected void populateFunctionalizingEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasAgentMaterial())
			return;
		
		this.showFunctionalizingEntity = true;
		
		List<FunctionalizingEntity> fes = sampleBean.getFunctionalizingEntities();
		if (fes == null)
			return;
		
		List<String> functionalizingEntityName = new ArrayList<String>();
		for (FunctionalizingEntity fe : fes) {
			FunctionalizingEntityBean fBean = new FunctionalizingEntityBean(fe);
			String name = fBean.getAdvancedSearchDisplayName();
			functionalizingEntityName.add(name);
		}
		
		//this.columns.add(functionalizingEntityName);
		this.columns.add(new SimpleAdvancedCellBean("FunctionalizaingEntity", functionalizingEntityName));
	}
	
	protected void populateFunctionNames(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasFunction())
			return;
		
		this.showFunction = true;
		
		List<Function> functions = sampleBean.getFunctions();
		if (functions == null)
			return;
		
		List<String> functionNames = new ArrayList<String>();
		for (Function fe : functions) {
			FunctionBean fbean = new FunctionBean(fe);
			
			//TODO: data doesn't seems to be formatted right.
			String[] dns = fbean.getTargetDisplayNames();
			if (dns != null)
				Collections.addAll(functionNames, dns);
		}
		
		//this.columns.add(functionNames);
		this.columns.add(new SimpleAdvancedCellBean("Function", functionNames));
	}
	
	protected void populateCharacterizations(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (searchBean.getCharacterizationQueries().size() == 0)
			return;
		
		List<Characterization> chars = sampleBean.getCharacterizations();

		if (chars == null) 
			return;
		
		Map<String, List<SimpleCharacterizationAdvancedSearchResultBean>> charsByType = 
				new HashMap<String, List<SimpleCharacterizationAdvancedSearchResultBean>>();
		
		for (Characterization achar : chars) {
			
			long id = achar.getId();
			String type = "";
			String name = "";
			
			if (achar instanceof OtherCharacterization) {
				type = ((OtherCharacterization) achar)
						.getAssayCategory();
				type = ((OtherCharacterization) achar)
						.getName();
				logger.debug("Type: " + type + " Name: " + name);
			} else {
				String superClassShortName = ClassUtils
						.getShortClassName(achar.getClass().getSuperclass()
								.getName());
				type = ClassUtils
						.getDisplayName(superClassShortName);
				
				name = ClassUtils.getDisplayName(ClassUtils.getShortClassName(achar.getClass().getName()));
				
				logger.debug("Type: " + type + " Name: " + name);
			}
			
			
			String assayType = achar.getAssayType();
			
			List<SimpleCharacterizationAdvancedSearchResultBean> currentTypeList = charsByType.get(type);
			
			if (currentTypeList == null) {
				currentTypeList = new ArrayList<SimpleCharacterizationAdvancedSearchResultBean>();
				charsByType.put(type, currentTypeList);
			}
			
			SimpleCharacterizationAdvancedSearchResultBean simpleChar = new SimpleCharacterizationAdvancedSearchResultBean();
			simpleChar.setCharId(id);
			
			if (assayType != null && assayType.length() > 0)
				name += ":" + assayType;
			simpleChar.setDisplayName(name);
			
			currentTypeList.add(simpleChar);
			
		}
		
		List<CharacterizationQueryBean> charQueries = searchBean.getCharacterizationQueries();
		
		for (CharacterizationQueryBean cqBean : charQueries) {
			List<SimpleCharacterizationAdvancedSearchResultBean> cc = charsByType.get(cqBean.getCharacterizationType());
			if (cc == null)
				cc = new ArrayList<SimpleCharacterizationAdvancedSearchResultBean>();
			
//			for (SimpleCharacterizationAdvancedSearchResultBean result : cc) {
//				List<String> cell = new ArrayList<String>();
//				cell.add("Characterization");
//				cell.add(result.getDisplayName());
//				cell.add(String.valueOf(result.getCharId()));
//			}
			
			
			//this.columns.add(cc);
			this.columns.add(new SimpleAdvancedCellBean("Characterization", cc));
		}

	}
	
	
}
