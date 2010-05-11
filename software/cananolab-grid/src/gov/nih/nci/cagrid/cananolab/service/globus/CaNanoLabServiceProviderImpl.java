package gov.nih.nci.cagrid.cananolab.service.globus;

import gov.nih.nci.cagrid.cananolab.service.CaNanoLabServiceImpl;

import java.rmi.RemoteException;

/**
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the CaNanoLabServiceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 *
 * @created by Introduce Toolkit version 1.3
 *
 */
public class CaNanoLabServiceProviderImpl{

	CaNanoLabServiceImpl impl;

	public CaNanoLabServiceProviderImpl() throws RemoteException {
		impl = new CaNanoLabServiceImpl();
	}


    public gov.nih.nci.cagrid.cananolab.stubs.GetFindingsByCharacterizationIdResponse getFindingsByCharacterizationId(gov.nih.nci.cagrid.cananolab.stubs.GetFindingsByCharacterizationIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetFindingsByCharacterizationIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetFindingsByCharacterizationIdResponse();
    boxedResult.setFinding(impl.getFindingsByCharacterizationId(params.getCharId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetExperimentConfigsByCharacterizationIdResponse getExperimentConfigsByCharacterizationId(gov.nih.nci.cagrid.cananolab.stubs.GetExperimentConfigsByCharacterizationIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetExperimentConfigsByCharacterizationIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetExperimentConfigsByCharacterizationIdResponse();
    boxedResult.setExperimentConfig(impl.getExperimentConfigsByCharacterizationId(params.getCharId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetKeywordsBySampleIdResponse getKeywordsBySampleId(gov.nih.nci.cagrid.cananolab.stubs.GetKeywordsBySampleIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetKeywordsBySampleIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetKeywordsBySampleIdResponse();
    boxedResult.setKeyword(impl.getKeywordsBySampleId(params.getSampleId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetFilesByCompositionInfoIdResponse getFilesByCompositionInfoId(gov.nih.nci.cagrid.cananolab.stubs.GetFilesByCompositionInfoIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetFilesByCompositionInfoIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetFilesByCompositionInfoIdResponse();
    boxedResult.setFile(impl.getFilesByCompositionInfoId(params.getId(),params.getClassName()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetProtocolByCharacterizationIdResponse getProtocolByCharacterizationId(gov.nih.nci.cagrid.cananolab.stubs.GetProtocolByCharacterizationIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetProtocolByCharacterizationIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetProtocolByCharacterizationIdResponse();
    boxedResult.setProtocol(impl.getProtocolByCharacterizationId(params.getCharId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesResponse getSampleNames(gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesResponse();
    boxedResult.setResponse(impl.getSampleNames(params.getSampleName(), params.getSamplePointOfContact(),params.getNanomaterialEntityClassNames(),params.getFunctionalizingEntityClassNames(),params.getFunctionClassNames(),params.getCharacterizationClassNames(),params.getWords()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetPublicationIdsByResponse getPublicationIdsBy(gov.nih.nci.cagrid.cananolab.stubs.GetPublicationIdsByRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetPublicationIdsByResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetPublicationIdsByResponse();
    boxedResult.setResponse(impl.getPublicationIdsBy(params.getPublicationTitle(),params.getPublicationCategory(),params.getSampleName(),params.getResearchAreas(),params.getKeywords(),params.getPubMedId(),params.getDigitalObjectId(),params.getAuthors(),params.getNanomaterialEntityClassNames(),params.getFunctionalizingEntityClassNames(),params.getFunctionClassNames()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetOtherPointOfContactsBySampleIdResponse getOtherPointOfContactsBySampleId(gov.nih.nci.cagrid.cananolab.stubs.GetOtherPointOfContactsBySampleIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetOtherPointOfContactsBySampleIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetOtherPointOfContactsBySampleIdResponse();
    boxedResult.setPointOfContact(impl.getOtherPointOfContactsBySampleId(params.getSampleId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesByPublicationIdResponse getSampleNamesByPublicationId(gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesByPublicationIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesByPublicationIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetSampleNamesByPublicationIdResponse();
    boxedResult.setResponse(impl.getSampleNamesByPublicationId(params.getPublicationId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetPublicationsBySampleIdResponse getPublicationsBySampleId(gov.nih.nci.cagrid.cananolab.stubs.GetPublicationsBySampleIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetPublicationsBySampleIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetPublicationsBySampleIdResponse();
    boxedResult.setPublication(impl.getPublicationsBySampleId(params.getSampleId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetPrimaryPointOfContactBySampleIdResponse getPrimaryPointOfContactBySampleId(gov.nih.nci.cagrid.cananolab.stubs.GetPrimaryPointOfContactBySampleIdRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetPrimaryPointOfContactBySampleIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetPrimaryPointOfContactBySampleIdResponse();
    boxedResult.setPointOfContact(impl.getPrimaryPointOfContactBySampleId(params.getSampleId()));
    return boxedResult;
  }

    public gov.nih.nci.cagrid.cananolab.stubs.GetSampleViewStrsResponse getSampleViewStrs(gov.nih.nci.cagrid.cananolab.stubs.GetSampleViewStrsRequest params) throws RemoteException {
    gov.nih.nci.cagrid.cananolab.stubs.GetSampleViewStrsResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetSampleViewStrsResponse();
    boxedResult.setResponse(impl.getSampleViewStrs(params.getSampleName()));
    return boxedResult;
  }
    public gov.nih.nci.cagrid.cananolab.stubs.GetFileByProtocolIdResponse getFileByProtocolId(gov.nih.nci.cagrid.cananolab.stubs.GetFileByProtocolIdRequest params) throws RemoteException {
        gov.nih.nci.cagrid.cananolab.stubs.GetFileByProtocolIdResponse boxedResult = new gov.nih.nci.cagrid.cananolab.stubs.GetFileByProtocolIdResponse();
        boxedResult.setFile(impl.getFileByProtocolId(params.getProtocolId()));
        return boxedResult;
  }
}
