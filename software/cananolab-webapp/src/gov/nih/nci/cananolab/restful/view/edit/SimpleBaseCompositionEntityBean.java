package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.particle.composition.BaseCompositionEntityBean;

import java.util.ArrayList;
import java.util.List;

public class SimpleBaseCompositionEntityBean {

	String type;

	String description;

	SimpleFileBean theFile;

	String className;

	List<SimpleFileBean> files;

	String domainId, displayName; // used for DWR ajax in

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SimpleFileBean getTheFile() {
		return theFile;
	}

	public void setTheFile(SimpleFileBean theFile) {
		this.theFile = theFile;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<SimpleFileBean> getFiles() {
		return files;
	}

	public void setFiles(List<SimpleFileBean> files) {
		this.files = files;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void trasferSimpleBaseCompositionBean(BaseCompositionEntityBean bean){
		setType(bean.getType());
		setClassName(bean.getClassName());
		setDescription(bean.getDescription());
		setDomainId(bean.getDomainId());
		
		theFile = new SimpleFileBean();
		theFile.setCreatedBy(bean.getTheFile().getDomainFile().getCreatedBy());
		theFile.setCreatedDate(bean.getTheFile().getDomainFile().getCreatedDate());
		theFile.setDescription(bean.getTheFile().getDomainFile().getDescription());
		theFile.setId(bean.getTheFile().getDomainFile().getId());
		theFile.setKeywordsStr(bean.getTheFile().getKeywordsStr());
		theFile.setTitle(bean.getTheFile().getDomainFile().getTitle());
		theFile.setType(bean.getTheFile().getDomainFile().getType());
		theFile.setUri(bean.getTheFile().getDomainFile().getUri());
		theFile.setUriExternal(bean.getTheFile().getDomainFile().getUriExternal());
		
		setTheFile(theFile);
	}
}
