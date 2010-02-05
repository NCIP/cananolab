package gov.nih.nci.cananolab.util;

import gov.nih.nci.cananolab.domain.characterization.Characterization;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.common.Sample;
import gov.nih.nci.cananolab.dto.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.common.ChemicalAssociationBean;
import gov.nih.nci.cananolab.dto.common.ExperimentConfigBean;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.FunctionBean;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.TargetBean;

import java.util.Comparator;
import java.util.Date;

/**
 * Contains a list of static comparators for use in caNanoLab
 * 
 * @author pansu
 * 
 */

/* CVS $Id: Comparators.java,v 1.14 2008-09-23 21:53:45 tanq Exp $ */

public class Comparators {
	public static class InstrumentDateComparator implements
			Comparator<Instrument> {
		public int compare(Instrument instrument1, Instrument instrument2) {
			return instrument1.getCreatedDate().compareTo(
					instrument2.getCreatedDate());
		}
	}

	public static class PointOfContactBeanNameOrgComparator implements
			Comparator<PointOfContactBean> {
		public int compare(PointOfContactBean poc1, PointOfContactBean poc2) {
			if (poc1.getPersonDisplayName().equals(poc2.getPersonDisplayName())) {
				return poc1.getOrganizationDisplayName().compareTo(
						poc2.getOrganizationDisplayName());
			} else {
				return (poc1.getPersonDisplayName().compareTo(poc2
						.getPersonDisplayName()));
			}
		}
	}

	public static class SortableNameComparator implements Comparator<String> {
		public int compare(String name1, String name2) {

			// in case of sample name, container name and aliquot name
			if (name1 == null)
				return -1;
			if (name2 == null)
				return 1;

			name1 = name1.trim();
			name2 = name2.trim();

			// if (name1.matches("\\D+(-(\\d+)(\\D+)*)+")
			// && name2.matches("\\D+(-(\\d+)(\\D+)*)+")) {

			if (name1.matches("\\D+(-.+)+") && name2.matches("\\D+(-.+)+")) {
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

	public static class SampleNameComparator implements Comparator<Sample> {
		public int compare(Sample particle1, Sample particle2) {
			return new SortableNameComparator().compare(particle1.getName(),
					particle2.getName());
		}
	}

//	public static class NanomaterialEntityBeanTypeDateComparator implements
//			Comparator<NanomaterialEntityBean> {
//		public int compare(NanomaterialEntityBean entity1,
//				NanomaterialEntityBean entity2) {
//			if (entity1.getDomainEntity().getClass().getCanonicalName().equals(
//					entity2.getDomainEntity().getClass().getCanonicalName())) {
//				if (entity1.getDomainEntity().getCreatedDate().compareTo(
//						entity2.getDomainEntity().getCreatedDate()) == 0) {
//					return entity1.getDomainEntity().getId().compareTo(
//							entity2.getDomainEntity().getId());
//				} else {
//					return entity1.getDomainEntity().getCreatedDate()
//							.compareTo(
//									entity2.getDomainEntity().getCreatedDate());
//				}
//			} else {
//				return entity1.getDomainEntity().getClass().getCanonicalName()
//						.compareTo(
//								entity2.getDomainEntity().getClass()
//										.getCanonicalName());
//			}
//		}
//	}
//
//	public static class FunctionalizingEntityBeanTypeDateComparator implements
//			Comparator<FunctionalizingEntityBean> {
//		public int compare(FunctionalizingEntityBean entity1,
//				FunctionalizingEntityBean entity2) {
//			if (entity1.getDomainEntity().getClass().getCanonicalName().equals(
//					entity2.getDomainEntity().getClass().getCanonicalName())) {
//				if (entity1.getDomainEntity().getCreatedDate().compareTo(
//						entity2.getDomainEntity().getCreatedDate()) == 0) {
//					return entity1.getDomainEntity().getId().compareTo(
//							entity2.getDomainEntity().getId());
//				}
//				return entity1.getDomainEntity().getCreatedDate().compareTo(
//						entity2.getDomainEntity().getCreatedDate());
//			} else {
//				return entity1.getDomainEntity().getClass().getCanonicalName()
//						.compareTo(
//								entity2.getDomainEntity().getClass()
//										.getCanonicalName());
//			}
//		}
//	}

//	public static class ChemicalAssociationBeanTypeDateComparator implements
//			Comparator<ChemicalAssociationBean> {
//		public int compare(ChemicalAssociationBean assoc1,
//				ChemicalAssociationBean assoc2) {
//			if (assoc1.getDomainAssociation().getClass().getCanonicalName()
//					.equals(
//							assoc2.getDomainAssociation().getClass()
//									.getCanonicalName())) {
//				return assoc1.getDomainAssociation().getCreatedDate()
//						.compareTo(
//								assoc2.getDomainAssociation().getCreatedDate());
//			} else {
//				return assoc1.getDomainAssociation().getClass()
//						.getCanonicalName().compareTo(
//								assoc2.getDomainAssociation().getClass()
//										.getCanonicalName());
//			}
//		}
//	}

	public static class FileDateComparator implements Comparator<File> {
		public int compare(File file1, File file2) {
			return file1.getCreatedDate().compareTo(file2.getCreatedDate());
		}
	}

	public static class DatumDateComparator implements Comparator<Datum> {
		public int compare(Datum datum1, Datum datum2) {
			return datum1.getCreatedDate().compareTo(datum2.getCreatedDate());
		}
	}

	public static class ConditionDateComparator implements
			Comparator<Condition> {
		public int compare(Condition condition1, Condition condition2) {
			return condition1.getCreatedDate().compareTo(
					condition2.getCreatedDate());
		}
	}

	public static class FileBeanDateComparator implements Comparator<FileBean> {
		public int compare(FileBean file1, FileBean file2) {
			return file1.getDomainFile().getCreatedDate().compareTo(
					file2.getDomainFile().getCreatedDate());
		}
	}

	// public static class DerivedDatumBeanDateComparator implements
	// Comparator<DerivedDatumBean> {
	// public int compare(DerivedDatumBean data1, DerivedDatumBean data2) {
	// return data1.getDomainDerivedDatum().getCreatedDate().compareTo(
	// data2.getDomainDerivedDatum().getCreatedDate());
	// }
	// }
	//
	// public static class DerivedDatumDateComparator implements
	// Comparator<DerivedDatum> {
	// public int compare(DerivedDatum data1, DerivedDatum data2) {
	// return data1.getCreatedDate().compareTo(data2.getCreatedDate());
	// }
	// }

	public static class FunctionBeanDateComparator implements
			Comparator<FunctionBean> {
		public int compare(FunctionBean function1, FunctionBean function2) {
			return function1.getDomainFunction().getCreatedDate().compareTo(
					function2.getDomainFunction().getCreatedDate());
		}
	}

//	public static class ComposingElementBeanDateComparator implements
//			Comparator<ComposingElementBean> {
//		public int compare(ComposingElementBean element1,
//				ComposingElementBean element2) {
//			return element1.getDomain().getCreatedDate().compareTo(
//					element2.getDomain().getCreatedDate());
//		}
//	}

	public static class TargetBeanDateComparator implements
			Comparator<TargetBean> {
		public int compare(TargetBean target1, TargetBean target2) {
			return target1.getDomainTarget().getCreatedDate().compareTo(
					target2.getDomainTarget().getCreatedDate());
		}
	}

	public static class CharacterizationBeanNameDateComparator implements
			Comparator<CharacterizationBean> {
		public int compare(CharacterizationBean chara1,
				CharacterizationBean chara2) {
			String name1 = chara1.getCharacterizationName();
			String name2 = chara2.getCharacterizationName();
			if (name1.compareTo(name2) == 0) {
				String assay1 = chara1.getAssayType();
				if (assay1 == null) {
					assay1 = "";
				}
				String assay2 = chara2.getAssayType();
				if (assay2 == null) {
					assay2 = "";
				}
				if (assay1.compareTo(assay2) == 0) {
					Date date1 = chara1.getDomainChar().getCreatedDate();
					Date date2 = chara2.getDomainChar().getCreatedDate();
					if (date1.compareTo(date2) == 0) {
						return chara1.getDomainChar().getId().compareTo(
								chara2.getDomainChar().getId());
					} else {
						return date1.compareTo(date2);
					}
				} else {
					return assay1.compareTo(assay2);
				}
			} else {
				return name1.compareTo(name2);
			}
		}
	}

//	public static class CharacterizationNameAssayTypeDateComparator implements
//			Comparator<Characterization> {
//		public int compare(Characterization chara1,
//				Characterization chara2) {
//			String name1 = chara1.getClass().getName();
//			String name2 = chara2.getClass().getName();
//			if (name1.compareTo(name2) == 0) {
//				String assay1 = chara1.getAssayType();
//				if (assay1 == null) {
//					assay1 = "";
//				}
//				String assay2 = chara2.getAssayType();
//				if (assay2 == null) {
//					assay2 = "";
//				}
//				if (assay1.compareTo(assay2) == 0) {
//					Date date1 = chara1.getCreatedDate();
//					Date date2 = chara2.getCreatedDate();
//					if (date1.compareTo(date2) == 0) {
//						return chara1.getId().compareTo(
//								chara2.getId());
//					} else {
//						return date1.compareTo(date2);
//					}
//				} else {
//					return assay1.compareTo(assay2);
//				}
//			} else {
//				return name1.compareTo(name2);
//			}
//		}
//	}

	public static class ExperimentConfigBeanDateComparator implements
			Comparator<ExperimentConfigBean> {
		public int compare(ExperimentConfigBean config1,
				ExperimentConfigBean config2) {
			return config1.getDomain().getCreatedDate().compareTo(
					config2.getDomain().getCreatedDate());
		}
	}

//	public static class FindingBeanDateComparator implements
//			Comparator<CharacterizationResultBean> {
//		public int compare(CharacterizationResultBean finding1, CharacterizationResultBean finding2) {
//			return finding1.getDomain().getCreatedDate().compareTo(
//					finding2.getDomain().getCreatedDate());
//		}
//	}

	public static class ProtocolNameVersionComparator implements
			Comparator<Protocol> {
		public int compare(Protocol protocol1, Protocol protocol2) {
			String name1 = protocol1.getName();
			String name2 = protocol2.getName();
			int nameComp = new SortableNameComparator().compare(name1, name2);
			if (nameComp == 0) {
				String version1 = protocol1.getVersion();
				String version2 = protocol2.getVersion();
				if (version1 == null || version2 == null) {
					return 0;
				}
				return version1.compareTo(version2);
			} else {
				return nameComp;
			}
		}
	}

	public static class PublicationCategoryTitleComparator implements
			Comparator<Publication> {
		public int compare(Publication pub1, Publication pub2) {
			if (pub1.getCategory().equals(pub2.getCategory())) {
				return pub1.getTitle().compareTo(pub2.getTitle());
			} else {
				return pub1.getCategory().compareTo(pub2.getCategory());
			}
		}
	}

	public static class GridNodeHostNameComparator implements
			Comparator<GridNodeBean> {
		public int compare(GridNodeBean node1, GridNodeBean node2) {
			return node1.getHostName().compareTo(node2.getHostName());
		}
	}
}
