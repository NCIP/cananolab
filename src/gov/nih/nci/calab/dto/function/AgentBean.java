package gov.nih.nci.calab.dto.function;

import gov.nih.nci.calab.domain.nano.function.Agent;
import gov.nih.nci.calab.domain.nano.function.AgentTarget;
import gov.nih.nci.calab.domain.nano.function.Antibody;
import gov.nih.nci.calab.domain.nano.function.DNA;
import gov.nih.nci.calab.domain.nano.function.ImageContrastAgent;
import gov.nih.nci.calab.domain.nano.function.Peptide;
import gov.nih.nci.calab.domain.nano.function.SmallMolecule;
import gov.nih.nci.calab.domain.nano.function.UnclassifiedAgent;
import gov.nih.nci.calab.service.util.CaNanoLabConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author zengje
 * 
 */
public class AgentBean extends BaseAgentBean {

	private PeptideBean peptide = new PeptideBean();

	private DNABean dna = new DNABean();

	private SmallMoleculeBean smallMolecule = new SmallMoleculeBean();

	private AntibodyBean antibody = new AntibodyBean();

	private ImageContrastAgentBean imageContrastAgent = new ImageContrastAgentBean();

	private UnclassifiedAgentBean other = new UnclassifiedAgentBean();

	// otherValue can be "compoundName" for SamllMolecule; "type" for Probe and
	// ImageContrastAgent;
	// "speicies" for Antibody; or "sequence" for DNA and Peptide

	/**
	 * 
	 */
	public AgentBean() {
	}

	public AgentBean(Agent agent) {
		super(agent);

		if (agent instanceof DNA) {
			dna = new DNABean((DNA) agent);
			setType(CaNanoLabConstants.DNA);
		} else if (agent instanceof Peptide) {
			peptide = new PeptideBean((Peptide) agent);
			setType(CaNanoLabConstants.PEPTIDE);
		} else if (agent instanceof SmallMolecule) {
			smallMolecule = new SmallMoleculeBean((SmallMolecule) agent);
			setType(CaNanoLabConstants.SMALL_MOLECULE);
		} else if (agent instanceof Antibody) {
			antibody = new AntibodyBean((Antibody) agent);
			setType(CaNanoLabConstants.ANTIBODY);
		} else if (agent instanceof ImageContrastAgent) {
			imageContrastAgent = new ImageContrastAgentBean(
					(ImageContrastAgent) agent);
			setType(CaNanoLabConstants.IMAGE_CONTRAST_AGENT);
		} else if (agent instanceof UnclassifiedAgent) {
			setType(CaNanoLabConstants.OTHER);
		}
	}

	// update existing domain object
	public void updateDomainObj(Agent doAgent) {
		super.updateDomainObj(doAgent);
		if (getType().equals(CaNanoLabConstants.DNA)) {
			dna.updateDomainObj((DNA) doAgent);
		} else if (getType().equals(CaNanoLabConstants.PEPTIDE)) {
			peptide.updateDomainObj((Peptide) doAgent);
		} else if (getType().equals(CaNanoLabConstants.ANTIBODY)) {
			antibody.updateDomainObj((Antibody) doAgent);
		} else if (getType().equals(CaNanoLabConstants.IMAGE_CONTRAST_AGENT)) {
			imageContrastAgent.updateDomainObj((ImageContrastAgent) doAgent);
		} else if (getType().equals(CaNanoLabConstants.SMALL_MOLECULE)) {
			smallMolecule.updateDomainObj((SmallMolecule) doAgent);
		} else if (getType().equals(CaNanoLabConstants.OTHER)) {
			other.updateDomainObj((UnclassifiedAgent) doAgent);
		}
		updateAgentTargets(doAgent);
	}

	// create new domain object
	public Agent getDomainObj() {
		Agent doAgent = null;
		if (getType().equals(CaNanoLabConstants.DNA)) {
			doAgent = new DNA();
		} else if (getType().equals(CaNanoLabConstants.PEPTIDE)) {
			doAgent = new Peptide();
		} else if (getType().equals(CaNanoLabConstants.ANTIBODY)) {
			doAgent = new Antibody();
		} else if (getType().equals(CaNanoLabConstants.IMAGE_CONTRAST_AGENT)) {
			doAgent = new ImageContrastAgent();
		} else if (getType().equals(CaNanoLabConstants.SMALL_MOLECULE)) {
			doAgent = new SmallMolecule();
		} else if (getType().equals(CaNanoLabConstants.OTHER)) {
			doAgent = new UnclassifiedAgent();
		}
		super.updateDomainObj(doAgent);
		if (getType().equals(CaNanoLabConstants.DNA)) {
			dna.updateDomainObj((DNA) doAgent);
		} else if (getType().equals(CaNanoLabConstants.PEPTIDE)) {
			peptide.updateDomainObj((Peptide) doAgent);
		} else if (getType().equals(CaNanoLabConstants.ANTIBODY)) {
			antibody.updateDomainObj((Antibody) doAgent);
		} else if (getType().equals(CaNanoLabConstants.IMAGE_CONTRAST_AGENT)) {
			imageContrastAgent.updateDomainObj((ImageContrastAgent) doAgent);
		} else if (getType().equals(CaNanoLabConstants.SMALL_MOLECULE)) {
			smallMolecule.updateDomainObj((SmallMolecule) doAgent);
		} else if (getType().equals(CaNanoLabConstants.OTHER)) {
			other.updateDomainObj((UnclassifiedAgent) doAgent);
		}
		updateAgentTargets(doAgent);
		return doAgent;
	}

	private void updateAgentTargets(Agent doAgent) {
		// copy collection
		List<AgentTarget> doAgentTargets = new ArrayList<AgentTarget>(doAgent
				.getAgentTargetCollection());
		// clear the existing collection
		doAgent.getAgentTargetCollection().clear();
		for (AgentTargetBean agentTargetBean : getAgentTargets()) {
			AgentTarget doAgentTarget = null;
			// if no id, add new domain object
			if (agentTargetBean.getId() == null) {
				doAgentTarget = agentTargetBean.getDomainObj();
			} else {
				// find domain object with the same ID and add the updated
				// domain object
				if (doAgentTargets.size() > 0) {
					for (AgentTarget aDoAgentTarget : doAgentTargets) {
						if (aDoAgentTarget.getId().equals(
								new Long(agentTargetBean.getId()))) {
							if (aDoAgentTarget.getClass().getSimpleName()
									.equals(agentTargetBean.getType())) {
								doAgentTarget = aDoAgentTarget;
								agentTargetBean.updateDomainObj(doAgentTarget);
							} else {
								doAgentTarget = agentTargetBean.getDomainObj();
							}
							break;
						}
					}
				}
				else {
					doAgentTarget = agentTargetBean.getDomainObj();
				}
			}
			doAgent.getAgentTargetCollection().add(doAgentTarget);
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

	public void setSmallMolecule(SmallMoleculeBean smallMolecule) {
		this.smallMolecule = smallMolecule;
	}

}
