/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import java.util.List;

/**
 * @author zengje
 *
 */
public class RunBean {
	
	private String id;
	private String name;
	private List aliquotBeans;
	private List inputFileBeans;
	private List outputFileBeans;
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

	public List getAliquotBeans() {
		return aliquotBeans;
	}

	public void setAliquotBeans(List aliquotBeans) {
		this.aliquotBeans = aliquotBeans;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getInputFileBeans() {
		return inputFileBeans;
	}

	public void setInputFileBeans(List inputFileBeans) {
		this.inputFileBeans = inputFileBeans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getOutputFileBeans() {
		return outputFileBeans;
	}

	public void setOutputFileBeans(List outputFileBeans) {
		this.outputFileBeans = outputFileBeans;
	}

	public AssayBean getAssayBean() {
		return assayBean;
	}

	public void setAssayBean(AssayBean assayBean) {
		this.assayBean = assayBean;
	}

	
}
