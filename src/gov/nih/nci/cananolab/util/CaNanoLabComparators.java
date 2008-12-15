package gov.nih.nci.cananolab.util;

import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Organization;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.ProtocolFile;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.SurfaceChemistry;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;
import gov.nih.nci.cananolab.dto.common.FileBean;
import gov.nih.nci.cananolab.dto.common.GridNodeBean;
import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedDatumBean;
import gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean;
import gov.nih.nci.cananolab.dto.particle.composition.FunctionBean;
import gov.nih.nci.cananolab.dto.particle.composition.TargetBean;

import java.util.Comparator;

/**
 * Contains a list of static comparators for use in caNanoLab
 * 
 * @author pansu
 * 
 */

/* CVS $Id: CaNanoLabComparators.java,v 1.14 2008-09-23 21:53:45 tanq Exp $ */

public class CaNanoLabComparators {

	public static class ParticlePointOfContactComparator implements Comparator<PointOfContact> {
		public int compare(PointOfContact poc1, PointOfContact poc2) {
			int diff = new SortableNameComparator().compare(poc1
					.getFirstName(), poc2.getFirstName());
			if (diff==0) {
				diff = new SortableNameComparator().compare(poc1
						.getLastName(), poc2.getLastName());
				if (diff==0) {
					if (poc1.getOrganization()!=null && poc2.getOrganization()!=null) {				
						diff = new SortableNameComparator().compare(poc1
							.getOrganization().getName(), poc2.getOrganization().getName());
					}else if (poc1.getOrganization()!=null) {
						diff = 1;
					}else {
						diff = -1;
					}
				}
			}
			return diff;
		}
	}
	
	public static class ParticlePointOfContactBeanComparator implements Comparator<PointOfContactBean> {
		public int compare(PointOfContactBean poc1, PointOfContactBean poc2) {
			int diff = new SortableNameComparator().compare(poc1.getDisplayName(), 
					poc2.getDisplayName());
			return diff;
		}
	}
	
	public static class OrganizationComparator implements Comparator<Organization> {
		public int compare(Organization org1, Organization org2) {
			int diff = new SortableNameComparator().compare(org1.getName(), 
					org2.getName());
			return diff;
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

	public static class ParticleBeanComparator implements
			Comparator<ParticleBean> {
		public int compare(ParticleBean particle1, ParticleBean particle2) {
			return new SortableNameComparator().compare(particle1
					.getDomainParticleSample().getName(), particle2
					.getDomainParticleSample().getName());
		}
	}

	public static class NanoparticleSampleComparator implements
			Comparator<NanoparticleSample> {
		public int compare(NanoparticleSample particle1,
				NanoparticleSample particle2) {
			return new SortableNameComparator().compare(particle1.getName(),
					particle2.getName());
		}
	}

	public static class DataLinkTypeDateComparator implements
			Comparator<DataLinkBean> {
		public int compare(DataLinkBean link1, DataLinkBean link2) {
			if (link1.getDataDisplayType().equals(link2.getDataDisplayType())) {
				return link1.getCreatedDate().compareTo(link2.getCreatedDate());
			} else {
				return link1.getDataDisplayType().compareTo(
						link2.getDataDisplayType());
			}
		}
	}

	public static class NanoparticleEntityTypeDateComparator implements
			Comparator<NanoparticleEntity> {
		public int compare(NanoparticleEntity entity1,
				NanoparticleEntity entity2) {
			if (entity1.getClass().getCanonicalName().equals(
					entity2.getClass().getCanonicalName())) {
				return entity1.getCreatedDate().compareTo(
						entity2.getCreatedDate());
			} else {
				return entity1.getClass().getCanonicalName().compareTo(
						entity2.getClass().getCanonicalName());
			}
		}
	}

	public static class FunctionalizingEntityTypeDateComparator implements
			Comparator<FunctionalizingEntity> {
		public int compare(FunctionalizingEntity entity1,
				FunctionalizingEntity entity2) {
			if (entity1.getClass().getCanonicalName().equals(
					entity2.getClass().getCanonicalName())) {
				return entity1.getCreatedDate().compareTo(
						entity2.getCreatedDate());
			} else {
				return entity1.getClass().getCanonicalName().compareTo(
						entity2.getClass().getCanonicalName());
			}
		}
	}

	public static class ChemicalAssociationTypeDateComparator implements
			Comparator<ChemicalAssociation> {
		public int compare(ChemicalAssociation assoc1,
				ChemicalAssociation assoc2) {
			if (assoc1.getClass().getCanonicalName().equals(
					assoc2.getClass().getCanonicalName())) {
				return assoc1.getCreatedDate().compareTo(
						assoc2.getCreatedDate());
			} else {
				return assoc1.getClass().getCanonicalName().compareTo(
						assoc2.getClass().getCanonicalName());
			}
		}
	}

	public static class FileTypeDateComparator implements
			Comparator<File> {
		public int compare(File file1, File file2) {
			if (file1.getType().equals(file2.getType())) {
				return file1.getCreatedDate().compareTo(file2.getCreatedDate());
			} else {
				return file1.getClass().getCanonicalName().compareTo(
						file2.getClass().getCanonicalName());
			}
		}
	}

	public static class FileDateComparator implements Comparator<File> {
		public int compare(File file1, File file2) {
			return file1.getCreatedDate().compareTo(file2.getCreatedDate());
		}
	}

	public static class DerivedBioAssayDataBeanDateComparator implements
			Comparator<DerivedBioAssayDataBean> {
		public int compare(DerivedBioAssayDataBean bioassay1,
				DerivedBioAssayDataBean bioassay2) {
			return bioassay1.getDomainBioAssayData().getCreatedDate()
					.compareTo(
							bioassay2.getDomainBioAssayData().getCreatedDate());
		}
	}

	public static class FileBeanDateComparator implements
			Comparator<FileBean> {
		public int compare(FileBean file1, FileBean file2) {
			return file1.getDomainFile().getCreatedDate().compareTo(
					file2.getDomainFile().getCreatedDate());
		}
	}

	public static class DerivedDatumBeanDateComparator implements
			Comparator<DerivedDatumBean> {
		public int compare(DerivedDatumBean data1, DerivedDatumBean data2) {
			return data1.getDomainDerivedDatum().getCreatedDate().compareTo(
					data2.getDomainDerivedDatum().getCreatedDate());
		}
	}

	public static class DerivedDatumDateComparator implements
			Comparator<DerivedDatum> {
		public int compare(DerivedDatum data1, DerivedDatum data2) {
			return data1.getCreatedDate().compareTo(data2.getCreatedDate());
		}
	}

	public static class FunctionBeanDateComparator implements
			Comparator<FunctionBean> {
		public int compare(FunctionBean function1, FunctionBean function2) {
			return function1.getDomainFunction().getCreatedDate().compareTo(
					function2.getDomainFunction().getCreatedDate());
		}
	}

	public static class ComposingElementBeanDateComparator implements
			Comparator<ComposingElementBean> {
		public int compare(ComposingElementBean element1,
				ComposingElementBean element2) {
			return element1.getDomainComposingElement().getCreatedDate()
					.compareTo(
							element2.getDomainComposingElement()
									.getCreatedDate());
		}
	}

	public static class TargetBeanDateComparator implements
			Comparator<TargetBean> {
		public int compare(TargetBean target1, TargetBean target2) {
			return target1.getDomainTarget().getCreatedDate().compareTo(
					target2.getDomainTarget().getCreatedDate());
		}
	}

	public static class CharacterizationDateComparator implements
			Comparator<Characterization> {
		public int compare(Characterization chara1, Characterization chara2) {
			return chara1.getCreatedDate().compareTo(chara2.getCreatedDate());
		}
	}

	public static class SurfaceChemistryDateComparator implements
			Comparator<SurfaceChemistry> {
		public int compare(SurfaceChemistry chem1, SurfaceChemistry chem2) {
			return chem1.getCreatedDate().compareTo(chem2.getCreatedDate());
		}
	}

	public static class ProtocolFileBeanNameVersionComparator implements
			Comparator<ProtocolFileBean> {
		public int compare(ProtocolFileBean protocolFile1,
				ProtocolFileBean protocolFile2) {
			String name1 = ((ProtocolFile) protocolFile1.getDomainFile())
					.getProtocol().getName();
			String name2 = ((ProtocolFile) protocolFile2.getDomainFile())
					.getProtocol().getName();
			int nameComp = new SortableNameComparator().compare(name1, name2);
			if (nameComp == 0) {
				String version1 = protocolFile1.getDomainFile().getVersion();
				String version2 = protocolFile2.getDomainFile().getVersion();
				if (version1 == null || version2 == null) {
					return 0;
				}
				return version1.compareTo(version2);
			} else {
				return nameComp;
			}
		}
	}

	public static class PublicationBeanTitleComparator implements
			Comparator<PublicationBean> {
		public int compare(PublicationBean file1, PublicationBean file2) {
			return file1.getDomainFile().getTitle().compareTo(
					file2.getDomainFile().getTitle());
		}
	}

	public static class GridNodeHostNameComparator implements
			Comparator<GridNodeBean> {
		public int compare(GridNodeBean node1, GridNodeBean node2) {
			return node1.getHostName().compareTo(node2.getHostName());
		}
	}

}
