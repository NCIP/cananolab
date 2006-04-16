/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import java.util.HashMap;
import java.util.List;

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
	private HashMap<String, List<AssayBean>> assayBeanMap;
	
	// assaySeq = assayTypeCount;
	// runSeq   = assaySeq + assayCount;
	// aliquotSeq = runSeq + runCount*3 + 2;
	// inputFileSeq = aliquotSeq + aliquotCount;
	// outuputFileSeq = aliquotSeq + inputFileCount;

	public ExecuteWorkflowBean(int assayTypeCount, int assayCount, int runCount, int aliquotCount, int inputFileCount, HashMap<String, List<AssayBean>> assayBeanMap) {
		super();
		// TODO Auto-generated constructor stub
		this.assayTypeCount = assayTypeCount;
		this.assayCount = assayCount;
		this.runCount = runCount;
		this.aliquotCount = aliquotCount;
		this.inputFileCount = inputFileCount;
		this.assayBeanMap = assayBeanMap;
	}

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

	public HashMap<String, List<AssayBean>> getAssayBeanMap() {
		return assayBeanMap;
	}

	public void setAssayBeanMap(HashMap<String, List<AssayBean>> assayBeanMap) {
		this.assayBeanMap = assayBeanMap;
	}

}
