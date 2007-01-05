/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.Antibody;
import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.DNA;
import gov.nih.nci.calab.domain.nano.function.Encapsulation;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.function.Peptide;
import gov.nih.nci.calab.domain.nano.function.Probe;
import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedAgent;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedLinkage;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

/**
 * @author zengje
 * 
 */
public class LinkageBean {

	private String id;

	private String description;

	private String type = CaNanoLabConstants.ATTACHMENT;

	private AgentBean agent = new AgentBean();

	private PeptideBean peptide = new PeptideBean();

	private DNABean dna = new DNABean();

	private SmallMoleculeBean smallMolecule = new SmallMoleculeBean();

	private ProbeBean probe = new ProbeBean();

	private AntibodyBean antibody = new AntibodyBean();

	private ImageContrastAgentBean imageContrastAgent = new ImageContrastAgentBean();

	private UnclassifiedAgentBean other = new UnclassifiedAgentBean();

	private String bondType;// for atttachments

	private String localization; // for encapsulations

	/**
	 * 
	 */
	public LinkageBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LinkageBean(Linkage linkage) {
		this.description = linkage.getDescription();
		this.id = linkage.getId().toString();
		if (linkage instanceof Attachment) {
			bondType = ((Attachment) linkage).getBondType();
		} else if (linkage instanceof Encapsulation) {
			localization = ((Encapsulation) linkage).getLocalization();
			this.type = CaNanoLabConstants.ENCAPSULATION;
		} else if (linkage instanceof UnclassifiedLinkage) {
			this.type = CaNanoLabConstants.OTHER;
		}
		Agent theAgent = linkage.getAgent();
		if (theAgent != null) // shouldn't happen{
			agent = new AgentBean(theAgent);
			if (theAgent instanceof DNA) {
				dna = new DNABean((DNA) theAgent);
				agent.setType(CaNanoLabConstants.DNA);
			} else if (theAgent instanceof Peptide) {
				peptide = new PeptideBean((Peptide) theAgent);
				agent.setType(CaNanoLabConstants.PEPTIDE);
			} else if (theAgent instanceof SmallMolecule) {
				smallMolecule = new SmallMoleculeBean((SmallMolecule) theAgent);
				agent.setType(CaNanoLabConstants.SMALL_MOLECULE);
			} else if (theAgent instanceof Probe) {
				probe = new ProbeBean((Probe) theAgent);
				agent.setType(CaNanoLabConstants.PROBE);
			} else if (theAgent instanceof Antibody) {
				antibody = new AntibodyBean((Antibody) theAgent);
				agent.setType(CaNanoLabConstants.ANTIBODY);
			} else if (theAgent instanceof ImageContrastAgent) {
				imageContrastAgent = new ImageContrastAgentBean(
						(ImageContrastAgent) theAgent);
				agent.setType(CaNanoLabConstants.IMAGE_CONTRAST_AGENT);
			} else if (theAgent instanceof UnclassifiedAgent) {
				agent.setType(CaNanoLabConstants.OTHER);
			}
		}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AgentBean getAgent() {
		return agent;
	}

	public void setAgent(AgentBean agent) {
		this.agent = agent;
		this.dna.setAgentTargets(agent.getAgentTargets());
		this.dna.setDescription(agent.getDescription());
		this.peptide.setAgentTargets(agent.getAgentTargets());
		this.peptide.setDescription(agent.getDescription());
		this.antibody.setAgentTargets(agent.getAgentTargets());
		this.antibody.setDescription(agent.getDescription());
		this.imageContrastAgent.setAgentTargets(agent.getAgentTargets());
		this.imageContrastAgent.setDescription(agent.getDescription());
		this.smallMolecule.setAgentTargets(agent.getAgentTargets());
		this.smallMolecule.setDescription(agent.getDescription());
		this.other.setAgentTargets(agent.getAgentTargets());
		this.other.setDescription(agent.getDescription());
	}

	public Linkage getDomainObj() {
		Agent theAgent = null;
		if (agent.getType().equals(CaNanoLabConstants.DNA)) {
			dna.setAgentTargets(agent.getAgentTargets());
			dna.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = dna.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.PEPTIDE)) {
			peptide.setAgentTargets(agent.getAgentTargets());
			peptide.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = peptide.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.ANTIBODY)) {
			antibody.setAgentTargets(agent.getAgentTargets());
			antibody.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = antibody.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.PROBE)) {
			probe.setAgentTargets(agent.getAgentTargets());
			probe.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = probe.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.IMAGE_CONTRAST_AGENT)) {
			imageContrastAgent.setAgentTargets(agent.getAgentTargets());
			imageContrastAgent.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = imageContrastAgent.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.SMALL_MOLECULE)) {
			smallMolecule.setAgentTargets(agent.getAgentTargets());
			smallMolecule.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = smallMolecule.getDomainObj();
		} else if (agent.getType().equals(CaNanoLabConstants.OTHER)) {
			other.setAgentTargets(agent.getAgentTargets());
			other.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
			theAgent = other.getDomainObj();
		}

		if (type.equals(CaNanoLabConstants.ATTACHMENT)) {
			Attachment doAttach = new Attachment();
			if (getId() != null && getId().length() > 0) {
				doAttach.setId(new Long(getId()));
			}
			doAttach.setBondType(bondType);
			doAttach.setDescription(description);
			doAttach.setAgent(theAgent);
			return doAttach;
		} else if (type.equals(CaNanoLabConstants.ENCAPSULATION)) {
			Encapsulation doEncap = new Encapsulation();
			if (getId() != null && getId().length() > 0) {
				doEncap.setId(new Long(getId()));
			}
			doEncap.setLocalization(localization);
			doEncap.setDescription(description);
			doEncap.setAgent(theAgent);
			return doEncap;
		} else {
			UnclassifiedLinkage doLink = new UnclassifiedLinkage();
			if (getId() != null && getId().length() > 0) {
				doLink.setId(new Long(getId()));
			}
			doLink.setDescription(description);
			doLink.setAgent(theAgent);
			return doLink;
		}
	}

	public AntibodyBean getAntibody() {
		return antibody;
	}

	public DNABean getDna() {
		return dna;
	}

	public ImageContrastAgentBean getImageContrastAgent() {
		return imageContrastAgent;
	}

	public PeptideBean getPeptide() {
		return peptide;
	}

	public ProbeBean getProbe() {
		return probe;
	}

	public SmallMoleculeBean getSmallMolecule() {
		return smallMolecule;
	}

	public void setAntibody(AntibodyBean antibody) {
		this.antibody = antibody;
	}

	public void setDna(DNABean dna) {
		this.dna = dna;
	}

	public void setImageContrastAgent(ImageContrastAgentBean imageContrastAgent) {
		this.imageContrastAgent = imageContrastAgent;
	}

	public void setPeptide(PeptideBean peptide) {
		this.peptide = peptide;
	}

	public void setProbe(ProbeBean probe) {
		this.probe = probe;
	}

	public void setSmallMolecule(SmallMoleculeBean smallMolecule) {
		this.smallMolecule = smallMolecule;
	}

	public String getBondType() {
		return bondType;
	}

	public void setBondType(String bondType) {
		this.bondType = bondType;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

}
