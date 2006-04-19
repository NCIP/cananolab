/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import gov.nih.nci.calab.dto.administration.AliquotBean;

import java.util.List;

/**
 * @author zengje
 *
 */
public class RunBean {
	
	private String id;
	private String name;
	private List<AliquotBean> aliquotBeans;
	private List<FileBean> inputFileBeans;
	private List<FileBean> outputFileBeans;
	private AssayBean assayBean;
	
	/**
	 * 
	 */
	public RunBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public RunBean(String id, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
	}

	public List<AliquotBean> getAliquotBeans() {
		return aliquotBeans;
	}

	public void setAliquotBeans(List<AliquotBean> aliquotBeans) {
		this.aliquotBeans = aliquotBeans;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FileBean> getInputFileBeans() {
		return inputFileBeans;
	}

	public void setInputFileBeans(List<FileBean> inputFileBeans) {
		this.inputFileBeans = inputFileBeans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FileBean> getOutputFileBeans() {
		return outputFileBeans;
	}

	public void setOutputFileBeans(List<FileBean> outputFileBeans) {
		this.outputFileBeans = outputFileBeans;
	}

	public AssayBean getAssayBean() {
		return assayBean;
	}

	public void setAssayBean(AssayBean assayBean) {
		this.assayBean = assayBean;
	}

	
}
