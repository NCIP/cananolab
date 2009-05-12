package gov.nih.nci.cananolab.dto.particle.composition;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cananolab.dto.common.FileBean;

/**
 * Base class for NanomaterialEntityBean, FunctionalizingEntityBean and
 * ChemicalAssociationBean
 *
 * @author pansu
 *
 */
public class BaseCompositionEntityBean {
	protected String type;

	protected String description;

	protected FileBean theFile = new FileBean();

	protected  String className;

	protected List<FileBean> files = new ArrayList<FileBean>();

	protected int theFileIndex=-1;

	public BaseCompositionEntityBean() {
	}

	public void addFile(FileBean file, int index) {
		if (index == -1) {
			files.add(file);
			return;
		}
		if (!files.isEmpty()) {
			files.remove(index);
		}
		files.add(index, file);
	}

	public void removeFile(int index) {
		files.remove(index);
	}

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

	public FileBean getTheFile() {
		return theFile;
	}

	public void setTheFile(FileBean theFile) {
		this.theFile = theFile;
	}

	public List<FileBean> getFiles() {
		return files;
	}

	public void setFiles(List<FileBean> files) {
		this.files = files;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getTheFileIndex() {
		return theFileIndex;
	}

	public void setTheFileIndex(int theFileIndex) {
		this.theFileIndex = theFileIndex;
	}
}
