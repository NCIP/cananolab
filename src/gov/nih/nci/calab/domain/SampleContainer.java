

package gov.nih.nci.calab.domain;
import gov.nih.nci.calab.domain.*;
import gov.nih.nci.system.applicationservice.*;
import java.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */
 
  /**
   * This is Sample Container. And it contains the information for the sample contained in the container. 
   * 
   */

public  class SampleContainer 
	implements java.io.Serializable 
{
	private static final long serialVersionUID = 1234567890L;

	
	   
	   public java.lang.Long id;
	   public  java.lang.Long getId(){
	      return id;
	   }
	   public void setId( java.lang.Long id){
	      this.id = id;
	   }
	
	   
	   public java.lang.Float quantity;
	   public  java.lang.Float getQuantity(){
	      return quantity;
	   }
	   public void setQuantity( java.lang.Float quantity){
	      this.quantity = quantity;
	   }
	
	   
	   public java.lang.Float concentration;
	   public  java.lang.Float getConcentration(){
	      return concentration;
	   }
	   public void setConcentration( java.lang.Float concentration){
	      this.concentration = concentration;
	   }
	
	   
	   public java.lang.Float volume;
	   public  java.lang.Float getVolume(){
	      return volume;
	   }
	   public void setVolume( java.lang.Float volume){
	      this.volume = volume;
	   }
	
	   
	   public java.lang.String diluentsSolvent;
	   public  java.lang.String getDiluentsSolvent(){
	      return diluentsSolvent;
	   }
	   public void setDiluentsSolvent( java.lang.String diluentsSolvent){
	      this.diluentsSolvent = diluentsSolvent;
	   }
	
	   
	   public java.lang.String safetyPrecaution;
	   public  java.lang.String getSafetyPrecaution(){
	      return safetyPrecaution;
	   }
	   public void setSafetyPrecaution( java.lang.String safetyPrecaution){
	      this.safetyPrecaution = safetyPrecaution;
	   }
	
	   
	   public java.lang.String storageCondition;
	   public  java.lang.String getStorageCondition(){
	      return storageCondition;
	   }
	   public void setStorageCondition( java.lang.String storageCondition){
	      this.storageCondition = storageCondition;
	   }
	
	   
	   public java.lang.String comments;
	   public  java.lang.String getComments(){
	      return comments;
	   }
	   public void setComments( java.lang.String comments){
	      this.comments = comments;
	   }
	
	   
	   public java.lang.String quantityUnit;
	   public  java.lang.String getQuantityUnit(){
	      return quantityUnit;
	   }
	   public void setQuantityUnit( java.lang.String quantityUnit){
	      this.quantityUnit = quantityUnit;
	   }
	
	   
	   public java.lang.String concentrationUnit;
	   public  java.lang.String getConcentrationUnit(){
	      return concentrationUnit;
	   }
	   public void setConcentrationUnit( java.lang.String concentrationUnit){
	      this.concentrationUnit = concentrationUnit;
	   }
	
	   
	   public java.lang.String volumeUnit;
	   public  java.lang.String getVolumeUnit(){
	      return volumeUnit;
	   }
	   public void setVolumeUnit( java.lang.String volumeUnit){
	      this.volumeUnit = volumeUnit;
	   }
	
	   
	   public java.lang.String createdBy;
	   public  java.lang.String getCreatedBy(){
	      return createdBy;
	   }
	   public void setCreatedBy( java.lang.String createdBy){
	      this.createdBy = createdBy;
	   }
	
	   
	   public java.util.Date createdDate;
	   public  java.util.Date getCreatedDate(){
	      return createdDate;
	   }
	   public void setCreatedDate( java.util.Date createdDate){
	      this.createdDate = createdDate;
	   }
	
	   
	   public java.lang.String barcode;
	   public  java.lang.String getBarcode(){
	      return barcode;
	   }
	   public void setBarcode( java.lang.String barcode){
	      this.barcode = barcode;
	   }
	
	   
	   public java.lang.String containerType;
	   public  java.lang.String getContainerType(){
	      return containerType;
	   }
	   public void setContainerType( java.lang.String containerType){
	      this.containerType = containerType;
	   }
	
	   
	   public java.lang.String createdMethod;
	   public  java.lang.String getCreatedMethod(){
	      return createdMethod;
	   }
	   public void setCreatedMethod( java.lang.String createdMethod){
	      this.createdMethod = createdMethod;
	   }
	

	
	   
	   
	   
	      
			
			
	   		public gov.nih.nci.calab.domain.Storage storage;
			public gov.nih.nci.calab.domain.Storage getStorage(){
			
              ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			  gov.nih.nci.calab.domain.SampleContainer thisIdSet = new gov.nih.nci.calab.domain.SampleContainer();
			  thisIdSet.setId(this.getId());
			  try {
			  java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.Storage", thisIdSet);				 
			 
			  if (resultList!=null && resultList.size()>0) {
			     storage = (gov.nih.nci.calab.domain.Storage)resultList.get(0);
			     }
			  } catch(Exception ex) 
			  { 
			      	System.out.println("SampleContainer:getStorage throws exception ... ...");
			   		ex.printStackTrace(); 
			  }
			  return storage;			
			 		
              }
                        
	      
	               
	   
	   
	   
	   public void setStorage(gov.nih.nci.calab.domain.Storage storage){
		this.storage = storage;
	   }	
	   
	   
	
	   
	   
	   
	      
	   		public java.util.Collection parentSampleContainerCollection = new java.util.HashSet();
			public java.util.Collection getParentSampleContainerCollection(){
			try{
			   if(parentSampleContainerCollection.size() == 0) {}
		           } catch(Exception e) {			     
			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			      try {
			      
			      
			         
				 	gov.nih.nci.calab.domain.SampleContainer thisIdSet = new gov.nih.nci.calab.domain.SampleContainer();
			         	thisIdSet.setId(this.getId());
			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.SampleContainer", thisIdSet);				 
				 	parentSampleContainerCollection = resultList;  
				 	return resultList;
				 
			      
			      }catch(Exception ex) 
			      {
			      	System.out.println("SampleContainer:getParentSampleContainerCollection throws exception ... ...");
			   		ex.printStackTrace(); 
			      }
			   }	
	              return parentSampleContainerCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setParentSampleContainerCollection(java.util.Collection parentSampleContainerCollection){
	   		this.parentSampleContainerCollection = parentSampleContainerCollection;
	        }	
	   
	   
	
	   
	   
	   
	      
	   		public java.util.Collection childSampleContainerCollection = new java.util.HashSet();
			public java.util.Collection getChildSampleContainerCollection(){
			try{
			   if(childSampleContainerCollection.size() == 0) {}
		           } catch(Exception e) {			     
			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			      try {
			      
			      
			         
				 	gov.nih.nci.calab.domain.SampleContainer thisIdSet = new gov.nih.nci.calab.domain.SampleContainer();
			         	thisIdSet.setId(this.getId());
			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.SampleContainer", thisIdSet);				 
				 	childSampleContainerCollection = resultList;  
				 	return resultList;
				 
			      
			      }catch(Exception ex) 
			      {
			      	System.out.println("SampleContainer:getChildSampleContainerCollection throws exception ... ...");
			   		ex.printStackTrace(); 
			      }
			   }	
	              return childSampleContainerCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setChildSampleContainerCollection(java.util.Collection childSampleContainerCollection){
	   		this.childSampleContainerCollection = childSampleContainerCollection;
	        }	
	   
	   
	
	   
	   
	   
	      
	   		public java.util.Collection runSampleContainerCollection = new java.util.HashSet();
			public java.util.Collection getRunSampleContainerCollection(){
			try{
			   if(runSampleContainerCollection.size() == 0) {}
		           } catch(Exception e) {			     
			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			      try {
			      
			      
			         
				 	gov.nih.nci.calab.domain.SampleContainer thisIdSet = new gov.nih.nci.calab.domain.SampleContainer();
			         	thisIdSet.setId(this.getId());
			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.RunSampleContainer", thisIdSet);				 
				 	runSampleContainerCollection = resultList;  
				 	return resultList;
				 
			      
			      }catch(Exception ex) 
			      {
			      	System.out.println("SampleContainer:getRunSampleContainerCollection throws exception ... ...");
			   		ex.printStackTrace(); 
			      }
			   }	
	              return runSampleContainerCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setRunSampleContainerCollection(java.util.Collection runSampleContainerCollection){
	   		this.runSampleContainerCollection = runSampleContainerCollection;
	        }	
	   
	   
	
	   
	   
	   
	      
			
			
			
			
	   		protected gov.nih.nci.calab.domain.Sample sample;
			public gov.nih.nci.calab.domain.Sample getSample(){
			
			
			
			  ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			  gov.nih.nci.calab.domain.SampleContainer thisIdSet = new gov.nih.nci.calab.domain.SampleContainer();
			  thisIdSet.setId(this.getId());
			  
			  try {
			     java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.Sample", thisIdSet);				 
		             if (resultList!=null && resultList.size()>0) {
		                sample = (gov.nih.nci.calab.domain.Sample)resultList.get(0);
		             }
		          
			  } catch(Exception ex) 
			  { 
			      	System.out.println("SampleContainer:getSample throws exception ... ...");
			   		ex.printStackTrace(); 
			  }
			  return sample;	
			 
			 		
           }
		   
	      
	               
	   
	   
	   
	   public void setSample(gov.nih.nci.calab.domain.Sample sample){
		this.sample = sample;
	   }	
	   
	   
	

		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof SampleContainer) {
				SampleContainer c =(SampleContainer)obj; 			 
				Long thisId = getId();		
				
					if(thisId != null && thisId.equals(c.getId())) {
					   eq = true;
				    }		
				
			}
			return eq;
		}
		
		public int hashCode(){
			int h = 0;
			
			if(getId() != null) {
				h += getId().hashCode();
			}
			
			return h;
	}
	
	
}