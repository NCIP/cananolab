package gov.nih.nci.cagrid.cananolab.service;

import gov.nih.nci.cananolab.domain.common.ExperimentConfig;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.PointOfContact;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.CompositionServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;

import java.rmi.RemoteException;
import java.util.List;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 *
 * @created by Introduce Toolkit version 1.3
 *
 */
public class CaNanoLabServiceImpl extends CaNanoLabServiceImplBase {
	public CaNanoLabServiceImpl() throws RemoteException {
		super();
	}

	public gov.nih.nci.cananolab.domain.common.Finding[] getFindingsByCharacterizationId(
			java.lang.String charId) throws RemoteException {
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		try {
			List<Finding> findings = helper
					.findFindingsByCharacterizationId(charId);
			return findings.toArray(new Finding[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Error finding remote findings for characterization id.");
		}
	}

	public gov.nih.nci.cananolab.domain.common.ExperimentConfig[] getExperimentConfigsByCharacterizationId(
			java.lang.String charId) throws RemoteException {
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		try {
			List<ExperimentConfig> configs = helper
					.findExperimentConfigsByCharacterizationId(charId);
			return configs.toArray(new ExperimentConfig[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Error finding remote experiment configs for characterization id");
		}
	}

	public gov.nih.nci.cananolab.domain.common.Keyword[] getKeywordsBySampleId(
			java.lang.String sampleId) throws RemoteException {
		SampleServiceHelper helper = new SampleServiceHelper();
		try {
			List<Keyword> keywords = helper.findKeywordsBySampleId(sampleId);
			return keywords.toArray(new Keyword[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Error finding keywords by nanoparticle sample id.");
		}
	}

	public gov.nih.nci.cananolab.domain.common.File[] getFilesByCompositionInfoId(
			java.lang.String id, java.lang.String className)
			throws RemoteException {
		CompositionServiceHelper helper = new CompositionServiceHelper();
		try {
			List<File> files = helper.findFilesByCompositionInfoId(id,
					className);
			return files.toArray(new File[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Error finding remote lab files by composition info");
		}
	}

	public gov.nih.nci.cananolab.domain.common.Protocol getProtocolByCharacterizationId(
			java.lang.String charId) throws RemoteException {
		CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
		try {
			Protocol protocol = helper.findProtocolByCharacterizationId(charId);
			return protocol;
		} catch (Exception e) {
			throw new RemoteException(
					"Error finding remote protocol file by characterization id.");
		}
	}

	public gov.nih.nci.cananolab.domain.common.PointOfContact getPrimaryPointOfContactBySampleId(
			java.lang.String sampleId) throws RemoteException {
		try {
			SampleServiceHelper helper = new SampleServiceHelper();
			PointOfContact primaryPOC = helper
					.findPrimaryPointOfContactBySampleId(sampleId);
			return primaryPOC;
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get primary point of contact by sample id");
		}
	}

	public gov.nih.nci.cananolab.domain.common.PointOfContact[] getOtherPointOfContactsBySampleId(
			java.lang.String sampleId) throws RemoteException {
		try {
			SampleServiceHelper helper = new SampleServiceHelper();
			List<PointOfContact> pocList = helper
					.findOtherPointOfContactsBySampleId(sampleId);
			return pocList.toArray(new PointOfContact[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get other point of contacts by sample id");
		}
	}

	public java.lang.String[] getSampleNamesByPublicationId(
			java.lang.String publicationId) throws RemoteException {
		try {
			PublicationServiceHelper helper = new PublicationServiceHelper();
			String[] sampleNames = helper
					.findSampleNamesByPublicationId(publicationId);
			return sampleNames;
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get publications by the given parameters");
		}
	}

	public gov.nih.nci.cananolab.domain.common.Publication[] getPublicationsBySampleId(
			java.lang.String sampleId) throws RemoteException {
		try {
			PublicationServiceHelper helper = new PublicationServiceHelper();
			List<Publication> publicationList = helper
					.findPublicationsBySampleId(sampleId);
			return publicationList.toArray(new Publication[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get publications by the given sampleId");
		}
	}

	public java.lang.String[] getSampleNames(java.lang.String sampleName,
			java.lang.String samplePointOfContact,
			java.lang.String[] nanomaterialEntityClassNames,
			java.lang.String[] functionalizingEntityClassNames,
			java.lang.String[] functionClassNames,
			java.lang.String[] characterizationClassNames,
			java.lang.String[] words) throws RemoteException {
		try {
			SampleServiceHelper helper = new SampleServiceHelper();
			List<String> sampleNames = helper.findSampleNamesBy(sampleName,
					samplePointOfContact, nanomaterialEntityClassNames, null,
					functionalizingEntityClassNames, null, functionClassNames,
					null, characterizationClassNames, null, words);
			return sampleNames.toArray(new String[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get sample names by the given criteria");
		}
	}

	public java.lang.String[] getSampleViewStrs(java.lang.String sampleName)
			throws RemoteException {
		try {
			SampleServiceHelper helper = new SampleServiceHelper();
			Sample sample = null;
			// if matches numbers assume to be an ID
			if (sampleName.matches("\\d+")) {
				sample = helper.findSampleById(sampleName);
			} else {
				sample = helper.findSampleByName(sampleName);
			}
			if (sample != null) {
				String[] columns = helper.getSampleViewStrs(sample);
				return columns;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get sample view columns for the given sample name or sample ID: "
							+ e);
		}
	}

	public java.lang.String[] getPublicationIdsBy(
			java.lang.String publicationTitle,
			java.lang.String publicationCategory, java.lang.String sampleName,
			java.lang.String[] researchAreas, java.lang.String[] keywords,
			java.lang.String pubMedId, java.lang.String digitalObjectId,
			java.lang.String[] authors,
			java.lang.String[] nanomaterialEntityClassNames,
			java.lang.String[] functionalizingEntityClassNames,
			java.lang.String[] functionClassNames) throws RemoteException {
		try {
			PublicationServiceHelper helper = new PublicationServiceHelper();
			List<String> publicationIds = helper.findPublicationIdsBy(
					publicationTitle, publicationCategory, sampleName,
					researchAreas, keywords, pubMedId, digitalObjectId,
					authors, nanomaterialEntityClassNames, null,
					functionalizingEntityClassNames, null, functionClassNames,
					null);
			return publicationIds.toArray(new String[0]);
		} catch (Exception e) {
			throw new RemoteException(
					"Can't get publications by the given sampleId");
		}
	}

	public gov.nih.nci.cananolab.domain.common.File getFileByProtocolId(
			java.lang.String protocolId) throws RemoteException {
		try {
			ProtocolServiceHelper helper = new ProtocolServiceHelper();
			File file = helper.findFileByProtocolId(protocolId);
			return file;
		} catch (Exception e) {
			throw new RemoteException("Can't get file by the given protocolId");
		}
	}

}
