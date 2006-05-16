/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import java.util.Map;
import java.util.SortedSet;

/**
 * @author zengje
 * 
 */
public class ExecuteWorkflowBean {

	private int assayTypeCount;

	private int assayCount;

	private int runCount;

	private int aliquotCount;

	private int inputFileCount;

	private Map<String, SortedSet<AssayBean>> assayBeanMap;

	public int getAliquotCount() {
		return aliquotCount;
	}

	public void setAliquotCount(int aliquotCount) {
		this.aliquotCount = aliquotCount;
	}

	public int getAssayCount() {

		return assayCount;
	}

	public void setAssayCount(int assayCount) {
		this.assayCount = assayCount;
	}

	public int getAssayTypeCount() {
		return assayTypeCount;
	}

	public void setAssayTypeCount(int assayTypeCount) {
		this.assayTypeCount = assayTypeCount;
	}

	public int getInputFileCount() {
		return inputFileCount;
	}

	public void setInputFileCount(int inputFileCount) {
		this.inputFileCount = inputFileCount;
	}

	public int getRunCount() {
		return runCount;
	}

	public void setRunCount(int runCount) {
		this.runCount = runCount;
	}

	/**
	 * 
	 */
	public ExecuteWorkflowBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Map<String, SortedSet<AssayBean>> getAssayBeanMap() {
		return assayBeanMap;
	}

	public void setAssayBeanMap(Map<String, SortedSet<AssayBean>> assayBeanMap) {
		this.assayBeanMap = assayBeanMap;
	}
}
