package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.restful.view.SimpleSampleBean;

import java.util.Collection;

public class SimpleSampleComposition {

		Long id;
		
		Collection<SimpleNanomaterialEntityBean> nanomaterialEntityCollection;
		
		//SimpleSampleBean sample;
		
		Collection<SimpleChemicalAssociationBean> chemicalAssociationCollection;
		
		Collection<SimpleFileBean> fileCollection;
						
		Collection<SimpleFunctionalizingEntityBean> functionalizingEntityCollection;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Collection<SimpleNanomaterialEntityBean> getNanomaterialEntityCollection() {
			return nanomaterialEntityCollection;
		}

		public void setNanomaterialEntityCollection(
				Collection<SimpleNanomaterialEntityBean> nanomaterialEntityCollection) {
			this.nanomaterialEntityCollection = nanomaterialEntityCollection;
		}

//		public SimpleSampleBean getSample() {
//			return sample;
//		}
//
//		public void setSample(SimpleSampleBean sample) {
//			this.sample = sample;
//		}

		public Collection<SimpleChemicalAssociationBean> getChemicalAssociationCollection() {
			return chemicalAssociationCollection;
		}

		public void setChemicalAssociationCollection(
				Collection<SimpleChemicalAssociationBean> chemicalAssociationCollection) {
			this.chemicalAssociationCollection = chemicalAssociationCollection;
		}

		public Collection<SimpleFileBean> getFileCollection() {
			return fileCollection;
		}

		public void setFileCollection(Collection<SimpleFileBean> fileCollection) {
			this.fileCollection = fileCollection;
		}

		public Collection<SimpleFunctionalizingEntityBean> getFunctionalizingEntityCollection() {
			return functionalizingEntityCollection;
		}

		public void setFunctionalizingEntityCollection(
				Collection<SimpleFunctionalizingEntityBean> functionalizingEntityCollection) {
			this.functionalizingEntityCollection = functionalizingEntityCollection;
		}
		
		
}
