/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

import gov.nih.nci.calab.domain.Run;
import gov.nih.nci.calab.dto.common.SortableName;
import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
import gov.nih.nci.calab.service.util.CaNanoLabComparators;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author zengje
 * 
 */
public class RunBean {

	private String id = "";

	private String name = "";

	private String runBy;

	private Date runDate;

	private List<AliquotBean> aliquotBeans;

	private List<FileBean> inputFileBeans;

	private List<FileBean> outputFileBeans;

	private SortedSet<SampleBean> sampleBeans;

	private String sampleSourceName;

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

	// used in WorkflowResultBean
	public RunBean(String id, String name, Date runDate) {
		this.id = id;
		this.name = name;
		this.runDate = runDate;
	}

	public RunBean(Run run) {
		this.id = StringUtils.convertToString(run.getId());
		this.name = StringUtils.convertToString(run.getName());
		this.runBy = run.getRunBy();
		this.runDate = run.getRunDate();
		this.assayBean = new AssayBean(run.getAssay());
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

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	// used for display tag
	public SortableName getSortableName() {
		return new SortableName(name);
	}

	public String getRunBy() {
		return runBy;
	}

	public SortedSet<SampleBean> getSampleBeans() {
		if (aliquotBeans == null) {
			return null;
		}
		sampleBeans = new TreeSet<SampleBean>(
				new CaNanoLabComparators.SampleBeanComparator());
		for (AliquotBean aliquot : aliquotBeans) {
			sampleBeans.add(aliquot.getSample());
		}
		return sampleBeans;
	}

	public String getSampleSourceName() {
		if (sampleSourceName != null) {
			return sampleSourceName;
		}
		Set<String> names = new HashSet<String>();
		if (getSampleBeans() != null) {
			for (SampleBean sample : sampleBeans) {
				if (sample.getSampleSource() != null
						&& sample.getSampleSource().length() > 0) {
					names.add(sample.getSampleSource());
				}
			}
			return StringUtils.join(new ArrayList<String>(names), ", ");
		}
		else return null;
	}

	public void setSampleSourceName(String sourceName) {
		this.sampleSourceName = sourceName;
	}
	
	public String getAliquotNames() {
		List<String> names=new ArrayList<String>();
		if (aliquotBeans!=null) {
			for(AliquotBean aliquot: aliquotBeans) {
				names.add(aliquot.getAliquotName());
			}
		}
		else {
			return null;
		}
		return StringUtils.join(names, ", ");
	}
}
