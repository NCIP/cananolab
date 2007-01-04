package gov.nih.nci.calab.service.util;

import gov.nih.nci.calab.dto.inventory.AliquotBean;
import gov.nih.nci.calab.dto.inventory.ContainerBean;
import gov.nih.nci.calab.dto.inventory.SampleBean;
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

/* CVS $Id: CaNanoLabComparators.java,v 1.1 2007-01-04 23:31:01 pansu Exp $ */

public class CaNanoLabComparators {

	public static class RunBeanComparator implements Comparator<RunBean> {
		public int compare(RunBean run1, RunBean run2) {
			// compare assay first
			AssayBeanComparator assayComp = new AssayBeanComparator();
			if (assayComp.compare(run1.getAssayBean(), run2.getAssayBean()) == 0) {
				// compare runName
				return new SortableNameComparator().compare(run1.getName(),
						run2.getName());
			}
			return assayComp.compare(run1.getAssayBean(), run2.getAssayBean());
		}
	}

	public static class AliquotBeanComparator implements
			Comparator<AliquotBean> {
		public int compare(AliquotBean aliquot1, AliquotBean aliquot2) {
			int diff = new SortableNameComparator().compare(aliquot1
					.getAliquotName(), aliquot2.getAliquotName());
			return diff;
		}
	}

	public static class SortableNameComparator implements Comparator<String> {
		public int compare(String name1, String name2) {
			// in case of sample name, container name and aliquot name
			if (name1.matches("\\D+(-(\\d+)(\\D+)*)+")
					&& name2.matches("\\D+(-(\\d+)(\\D+)*)+")) {
				String[] toks1 = name1.split("-");
				String[] toks2 = name2.split("-");
				int num = 0;
				if (toks1.length >= toks2.length) {
					num = toks1.length;
				} else {
					num = toks2.length;
				}

				for (int i = 0; i < num; i++) {
					String str1 = "0", str2 = "0";
					if (i < toks1.length) {
						str1 = toks1[i];
					}
					if (i < toks2.length) {
						str2 = toks2[i];
					}
					try {
						int num1 = 0, num2 = 0;
						num1 = Integer.parseInt(str1);
						num2 = Integer.parseInt(str2);
						if (num1 != num2) {
							return num1 - num2;
						}
					} catch (Exception e) {
						if (!str1.equals(str2)) {
							return str1.compareTo(str2);
						}
					}
				}
			}
			// in case of run name
			else if (name1.matches("(\\D+)(\\d+)")
					&& name2.matches("(\\D+)(\\d+)")) {
				try {
					String str1 = name1.replaceAll("(\\D+)(\\d+)", "$1");
					String str2 = name2.replaceAll("(\\D+)(\\d+)", "$1");

					int num1 = Integer.parseInt(name1.replaceAll(
							"(\\D+)(\\d+)", "$2"));
					int num2 = Integer.parseInt(name2.replaceAll(
							"(\\D+)(\\d+)", "$2"));
					if (str1.equals(str2)) {
						return num1 - num2;
					} else {
						return str1.compareTo(str2);
					}
				} catch (Exception e) {
					return name1.compareTo(name2);
				}
			}
			return name1.compareTo(name2);
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
			return new SortableNameComparator().compare(
					sample1.getSampleName(), sample2.getSampleName());
		}
	}

	public static class ContainerBeanComparator implements
			Comparator<ContainerBean> {
		public int compare(ContainerBean container1, ContainerBean container2) {
			return new SortableNameComparator().compare(container1
					.getContainerName(), container2.getContainerName());
		}
	}

	public static class FileBeanComparator implements Comparator<FileBean> {
		public int compare(FileBean file1, FileBean file2) {
			// compare short file name then path
			if (file1.getShortFilename().compareTo(file2.getShortFilename()) == 0) {
				return file1.getPath().compareTo(file2.getPath());
			} else {
				return file1.getShortFilename().compareTo(
						file2.getShortFilename());
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
					// compare file
					if (fileDiff == 0) {
						// compare aliquot
						return aliquotDiff;
					} else {
						return fileDiff;
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
