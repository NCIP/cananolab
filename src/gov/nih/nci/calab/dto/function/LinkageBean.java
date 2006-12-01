/**
 * 
 */
package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.Attachment;
import gov.nih.nci.calab.domain.nano.function.DNA;
import gov.nih.nci.calab.domain.nano.function.Encapsulation;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.Linkage;
import gov.nih.nci.calab.domain.nano.function.Peptide;
import gov.nih.nci.calab.domain.nano.function.Probe;
import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
import gov.nih.nci.calab.service.util.CananoConstants;

/**
 * @author zengje
 * 
 */
public class LinkageBean {

	private String id;

	private String description;

	private String type = CananoConstants.ATTACHMENT;

	private AgentBean agent = new AgentBean();
	
	private PeptideBean peptide =new PeptideBean();
	
	private DNABean dna=new DNABean();
	
	private SmallMoleculeBean smallMolecule=new SmallMoleculeBean();
	
	private ProbeBean probe=new ProbeBean();
	
	private AntibodyBean antibody=new AntibodyBean();
	
	private ImageContrastAgentBean imageContrastAgent=new ImageContrastAgentBean();
	
	private String bondType;//for atttachments
	
	private String localization; //for encapsulations	

	/**
	 * 
	 */
	public LinkageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LinkageBean(Linkage linkage) {
		this.description=linkage.getDescription();
		this.id=linkage.getId().toString();
		if (linkage instanceof Attachment) {
			bondType=((Attachment)linkage).getBondType();
		}
		if (linkage instanceof Encapsulation) {
			localization=((Encapsulation)linkage).getLocalization();
		}
		Agent agent=linkage.getAgent();		
		if (agent instanceof DNA) {
			dna=new DNABean((DNA)agent);
		}
		if (agent instanceof Peptide) {
			peptide=new PeptideBean((Peptide)agent);
		}
		if (agent instanceof SmallMolecule) {
			smallMolecule=new SmallMoleculeBean((SmallMolecule)agent);
		}
		if (agent instanceof Probe) {
			probe=new ProbeBean((Probe)agent);
		}
		if (agent instanceof ImageContrastAgent) {
			imageContrastAgent=new ImageContrastAgentBean((ImageContrastAgent)agent);
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
	}

	public Linkage getDomainObj() {
		AgentBean theAgent=null;
		if (agent.getType().equals(CananoConstants.DNA)){
			theAgent=dna;			
		}
		else if (agent.getType().equals(CananoConstants.PEPTIDE)){
			theAgent=peptide;
		}
		else if (agent.getType().equals(CananoConstants.ANTIBODY)){
			theAgent=antibody;
		}
		else if (agent.getType().equals(CananoConstants.PROBE)){
			theAgent=probe;
		}
		else if (agent.getType().equals(CananoConstants.IMAGE_CONTRAST_AGENT)){
			theAgent=imageContrastAgent;
		}
		else if (agent.getType().equals(CananoConstants.SMALL_MOLECULE)){
			theAgent=smallMolecule;
		}
		theAgent.setType(agent.getType());
		theAgent.setDescription(agent.getDescription());
		theAgent.setNumberOfAgentTargets(agent.getNumberOfAgentTargets());
		theAgent.setAgentTargets(agent.getAgentTargets());
		
		if (type.equals(CananoConstants.ATTACHMENT)) {
			Attachment doAttach = new Attachment();
			if (getId() != null && getId().length() > 0) {
				doAttach.setId(new Long(getId()));
			}
			doAttach.setBondType(bondType);
			doAttach.setDescription(description);
			doAttach.setAgent(theAgent.getDomainObj());
			return doAttach;
		} else {
			Encapsulation doEncap = new Encapsulation();
			if (getId() != null && getId().length() > 0) {
				doEncap.setId(new Long(getId()));
			}
			doEncap.setLocalization(localization);
			doEncap.setDescription(description);
			doEncap.setAgent(theAgent.getDomainObj());
			return doEncap;
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
