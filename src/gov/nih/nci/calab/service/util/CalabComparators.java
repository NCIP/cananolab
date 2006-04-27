package gov.nih.nci.calab.service.util;

import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.dto.search.WorkflowResultBean;
import gov.nih.nci.calab.dto.workflow.AssayBean;
import gov.nih.nci.calab.dto.workflow.FileBean;
import gov.nih.nci.calab.dto.workflow.RunBean;

import java.util.Comparator;

/**
 * Contains a list of static comparators for use in caLAB
 * 
 * @author pansu
 * 
 */

/* CVS $Id: CalabComparators.java,v 1.4 2006-04-27 14:51:20 pansu Exp $ */

public class CalabComparators {

	public static class RunBeanComparator implements Comparator<RunBean> {
		public int compare(RunBean run1, RunBean run2) {
			// if no run number compare by name
			if (run1.getRunNumber() == -1 || run2.getRunNumber() == -1) {
				return run1.getName().compareTo(run2.getName());
			}
			return run1.getRunNumber().compareTo(run2.getRunNumber());
		}
	}

	public static class AliquotBeanComparator implements
			Comparator<AliquotBean> {
		public int compare(AliquotBean aliquot1, AliquotBean aliquot2) {

			return aliquot1.getAliquotName().compareTo(
					aliquot2.getAliquotName());
		}
	}

	public static class AssayBeanComparator implements Comparator<AssayBean> {
		public int compare(AssayBean assay1, AssayBean assay2) {

			// compare assayType first then if assayNumber is valid, compare
			// prefix then number, else
			// compare name
			if (assay1.getAssayType().compareTo(assay2.getAssayType()) == 0) {
				if (assay1.getAssayNumber() == -1
						|| assay2.getAssayNumber() == -1) {
					return assay1.getAssayName().compareTo(
							assay2.getAssayName());
				} else if (assay1.getAssayPrefix().compareTo(
						assay2.getAssayPrefix()) == 0) {
					return assay1.getAssayNumber().compareTo(
							assay2.getAssayNumber());
				} else {
					return assay1.getAssayPrefix().compareTo(
							assay2.getAssayPrefix());
				}
			} else {
				return assay1.getAssayType().compareTo(assay2.getAssayType());
			}
		}
	}

	public static class SampleBeanComparator implements Comparator<SampleBean> {
		public int compare(SampleBean sample1, SampleBean sample2) {
			return sample1.getSampleName().compareTo(sample2.getSampleName());
		}
	}

	public static class ContainerBeanComparator implements
			Comparator<ContainerBean> {
		public int compare(ContainerBean container1, ContainerBean container2) {

			// if no container number compare by name
			if (container1.getContainerNumber() == -1
					&& container2.getContainerNumber() == -1) {
				return container1.getContainerName().compareTo(
						container2.getContainerName());
			}
			return container1.getContainerNumber().compareTo(
					container2.getContainerNumber());
		}
	}

	public static class FileBeanComparator implements Comparator<FileBean> {
		public int compare(FileBean file1, FileBean file2) {
			// compare file name then file inouttype
			if (file1.getFilename().compareTo(file2.getFilename()) == 0) {
				return file1.getInoutType().compareTo(file2.getInoutType());
			} else {
				return file1.getFilename().compareTo(file2.getFilename());
			}
		}
	}

	public static class WorkflowResultBeanComparator implements
			Comparator<WorkflowResultBean> {
		public int compare(WorkflowResultBean workflow1,
				WorkflowResultBean workflow2) {
			int assayDiff = (new AssayBeanComparator()).compare(workflow1
					.getAssay(), workflow2.getAssay());
			int runDiff = (new RunBeanComparator()).compare(workflow1.getRun(),
					workflow2.getRun());
			int fileDiff = (new FileBeanComparator()).compare(workflow1
					.getFile(), workflow2.getFile());
			int aliquotDiff = (new AliquotBeanComparator()).compare(workflow1
					.getAliquot(), workflow2.getAliquot());

			// compare assay first
			if (assayDiff == 0) {
				// compare run
				if (runDiff == 0) {
					// compare aliquot
					if (aliquotDiff == 0) {
						return fileDiff;
					} else {
						return aliquotDiff;
					}
				} else {
					return runDiff;
				}
			} else {
				return assayDiff;
			}
		}
	}
}
