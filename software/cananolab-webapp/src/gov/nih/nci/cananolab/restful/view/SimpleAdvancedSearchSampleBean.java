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
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellBean;
import gov.nih.nci.cananolab.restful.bean.SimpleAdvancedResultCellUnitBean;
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

	
//	List<List<SimpleCharacterizationAdvancedSearchResultBean>> orderedChars = 
//			new ArrayList<List<SimpleCharacterizationAdvancedSearchResultBean>>();
	
	
	List<SimpleAdvancedResultCellBean> columns = new ArrayList<SimpleAdvancedResultCellBean>();
	
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



	public List<SimpleAdvancedResultCellBean> getColumns() {
		return columns;
	}



	public void setColumns(List<SimpleAdvancedResultCellBean> columns) {
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
		
		
		this.columns.add(new SimpleAdvancedResultCellBean("Sample", sampleIdName));
		
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

		this.columns.add(new SimpleAdvancedResultCellBean("POC", orgNames));
	}
	
	protected void populateNanomaterialEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasNanomaterial())
			return;
		
		this.showNanomaterialEntity = true;
		
		List<NanomaterialEntity> nanos = sampleBean.getNanomaterialEntities();
		if (nanos == null)
			return;
		
		List<SimpleAdvancedResultCellUnitBean> nanomaterialEntities = new ArrayList<SimpleAdvancedResultCellUnitBean>();
		
		for (NanomaterialEntity nano : nanos) {
			String cn = ClassUtils.getShortClassName(nano.getClass().getName());
			if (nano instanceof OtherNanomaterialEntity)
				cn = ((OtherNanomaterialEntity)nano).getType();
			
			String displayName = cn;
			Collection<ComposingElement> ces = nano.getComposingElementCollection();
			if (ces != null) {
				for (ComposingElement ce : ces) {
					ComposingElementBean ceBean = new ComposingElementBean(ce);
					String val = ceBean.getAdvancedSearchDisplayName();
					displayName += ":" + val;
				}
			}
			
			SimpleAdvancedResultCellUnitBean entity = new SimpleAdvancedResultCellUnitBean();
			entity.setDisplayName(displayName);
			entity.setDataId(nano.getId());
			
			nanomaterialEntities.add(entity);
		}
		
		this.columns.add(new SimpleAdvancedResultCellBean("Nanomaterial", nanomaterialEntities));
	}
	
	protected void populateFunctionalizingEntity(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasAgentMaterial())
			return;
		
		this.showFunctionalizingEntity = true;
		
		List<FunctionalizingEntity> fes = sampleBean.getFunctionalizingEntities();
		if (fes == null)
			return;
		
		List<SimpleAdvancedResultCellUnitBean> units = new ArrayList<SimpleAdvancedResultCellUnitBean>();
		
		for (FunctionalizingEntity fe : fes) {
			FunctionalizingEntityBean fBean = new FunctionalizingEntityBean(fe);
			String name = fBean.getAdvancedSearchDisplayName();
			
			SimpleAdvancedResultCellUnitBean unit = new SimpleAdvancedResultCellUnitBean();
			
			logger.debug(fBean.getClassName() + ";" + fBean.getDescription() + ";" + fBean.getDescriptionDisplayName() +
					";" + fBean.getDisplayName());
			unit.setDataId(fe.getId());
			unit.setDisplayName(fBean.getDisplayName());
			
			units.add(unit);
		}
		
		this.columns.add(new SimpleAdvancedResultCellBean("FunctionalizaingEntity", units));
	}
	
	protected void populateFunctionNames(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (!searchBean.getHasFunction())
			return;
		
		this.showFunction = true;
		
		List<Function> functions = sampleBean.getFunctions();
		if (functions == null)
			return;
		
		List<SimpleAdvancedResultCellUnitBean> units = new ArrayList<SimpleAdvancedResultCellUnitBean>();
		for (Function fe : functions) {
			FunctionBean fbean = new FunctionBean(fe);
			
			logger.debug(fbean.getClassName() + ":" + fbean.getDescription()+ ":" + fbean.getDescriptionDisplayName() + fbean.getDisplayName());
			
			SimpleAdvancedResultCellUnitBean unit = new SimpleAdvancedResultCellUnitBean();
			unit.setDataId(fe.getId());
			unit.setDisplayName(fbean.getDisplayName());
			
			if (fe.getComposingElement() == null)
				unit.setSubType("FunctionalizaingEntity");
			else
				unit.setSubType("Nanomaterial");
			
			units.add(unit);
		}
		
		this.columns.add(new SimpleAdvancedResultCellBean("Function", units));
	}
	
	protected void populateCharacterizations(AdvancedSampleBean sampleBean, AdvancedSampleSearchBean searchBean) {
		if (searchBean.getCharacterizationQueries().size() == 0)
			return;
		
		List<Characterization> chars = sampleBean.getCharacterizations();

		if (chars == null) 
			return;
		
		Map<String, List<SimpleAdvancedResultCellUnitBean>> charsByType = 
				new HashMap<String, List<SimpleAdvancedResultCellUnitBean>>();
		
		for (Characterization achar : chars) {
			
			long id = achar.getId();
			String type = "";
			String name = "";
			
			if (achar instanceof OtherCharacterization) {
//				type = ((OtherCharacterization) achar)
//						.getAssayCategory();
				type = ((OtherCharacterization) achar).getName();
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
			
			List<SimpleAdvancedResultCellUnitBean> currentTypeList = charsByType.get(type);
			
			if (currentTypeList == null) {
				currentTypeList = new ArrayList<SimpleAdvancedResultCellUnitBean>();
				charsByType.put(type, currentTypeList);
			}
			
			SimpleAdvancedResultCellUnitBean simpleChar = new SimpleAdvancedResultCellUnitBean();
			simpleChar.setDataId(id);
			
			if (assayType != null && assayType.length() > 0)
				name += ":" + assayType;
			simpleChar.setDisplayName(name);
			
			currentTypeList.add(simpleChar);
			
		}
		
		List<CharacterizationQueryBean> charQueries = searchBean.getCharacterizationQueries();
		
		for (CharacterizationQueryBean cqBean : charQueries) {
			List<SimpleAdvancedResultCellUnitBean> cc = charsByType.get(cqBean.getCharacterizationType());
			if (cc == null)
				cc = new ArrayList<SimpleAdvancedResultCellUnitBean>();
			
//			for (SimpleCharacterizationAdvancedSearchResultBean result : cc) {
//				List<String> cell = new ArrayList<String>();
//				cell.add("Characterization");
//				cell.add(result.getDisplayName());
//				cell.add(String.valueOf(result.getCharId()));
//			}
			
			
			//this.columns.add(cc);
			this.columns.add(new SimpleAdvancedResultCellBean("Characterization", cc));
		}

	}
	
	
}
