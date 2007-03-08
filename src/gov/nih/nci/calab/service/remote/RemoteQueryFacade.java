package gov.nih.nci.calab.service.remote;

import gov.nih.nci.calab.domain.AssociatedFile;
import gov.nih.nci.calab.domain.OutputFile;
import gov.nih.nci.calab.domain.Report;
import gov.nih.nci.calab.domain.nano.particle.Nanoparticle;

import java.util.List;

/**
 * This interface defines methods available to http clients.
 * 
 * @author pansu
 * 
 */
public interface RemoteQueryFacade {

	public boolean isPublicData(String data) throws Exception;

	public Nanoparticle[] getPublicNanoparticles(List<Nanoparticle> particles)
			throws Exception;

	public Report[] getPublicReports(List<Report> reports) throws Exception;

	public AssociatedFile[] getPublicAssociatedFiles(
			List<AssociatedFile> associatedFiles) throws Exception;

	public OutputFile[] getPublicOutputFiles(List<OutputFile> files)
			throws Exception;

	public List getPublicData(List dataObjects) throws Exception;

	public byte[] retrievePublicFileContent(Long fileId) throws Exception;
}