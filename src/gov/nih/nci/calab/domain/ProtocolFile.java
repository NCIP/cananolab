

package gov.nih.nci.calab.domain;
import gov.nih.nci.calab.domain.*;
import gov.nih.nci.system.applicationservice.*;
import java.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */
 
  /**

   */

public  class ProtocolFile 
	extends LabFile
	implements java.io.Serializable 
{
	private static final long serialVersionUID = 1234567890L;

//	   private java.lang.Long id;
//	   public  java.lang.Long getId(){
//	      return id;
//	   }
//	   public void setId( java.lang.Long id){
//	      this.id = id;
//	   }
//	
//	   
//	   private java.lang.String path;
//	   public  java.lang.String getPath(){
//	      return path;
//	   }
//	   public void setPath( java.lang.String path){
//	      this.path = path;
//	   }
//	
//	   
//	   private java.lang.String extension;
//	   public  java.lang.String getExtension(){
//	      return extension;
//	   }
//	   public void setExtension( java.lang.String extension){
//	      this.extension = extension;
//	   }
//	
//	   
//	   private java.lang.String createdBy;
//	   public  java.lang.String getCreatedBy(){
//	      return createdBy;
//	   }
//	   public void setCreatedBy( java.lang.String createdBy){
//	      this.createdBy = createdBy;
//	   }
//	
//	   
//	   private java.util.Date createdDate;
//	   public  java.util.Date getCreatedDate(){
//	      return createdDate;
//	   }
//	   public void setCreatedDate( java.util.Date createdDate){
//	      this.createdDate = createdDate;
//	   }
//	
//	   
//	   private java.lang.String version;
//	   public  java.lang.String getVersion(){
//	      return version;
//	   }
//	   public void setVersion( java.lang.String version){
//	      this.version = version;
//	   }

			private gov.nih.nci.calab.domain.Protocol protocol;
			public gov.nih.nci.calab.domain.Protocol getProtocol(){
			
			
			
			  ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			  gov.nih.nci.calab.domain.ProtocolFile thisIdSet = new gov.nih.nci.calab.domain.ProtocolFile();
			  thisIdSet.setId(this.getId());
			  
			  try {
			     java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.Protocol", thisIdSet);				 
		             if (resultList!=null && resultList.size()>0) {
		                protocol = (gov.nih.nci.calab.domain.Protocol)resultList.get(0);
		             }
		          
			  } catch(Exception ex) 
			  { 
			      	System.out.println("ProtocolFile:getProtocol throws exception ... ...");
			   		ex.printStackTrace(); 
			  }
			  return protocol;	
			 
			 		
           }
		   
	      
	               
	   
	   
	   
	   public void setProtocol(gov.nih.nci.calab.domain.Protocol protocol){
		this.protocol = protocol;
	   }	
	   
	   
	

//		public boolean equals(Object obj){
//			boolean eq = false;
//			if(obj instanceof ProtocolFile) {
//				ProtocolFile c =(ProtocolFile)obj; 			 
//				Long thisId = getId();		
//				
//					if(thisId != null && thisId.equals(c.getId())) {
//					   eq = true;
//				    }		
//				
//			}
//			return eq;
//		}
//		
//		public int hashCode(){
//			int h = 0;
//			
//			if(getId() != null) {
//				h += getId().hashCode();
//			}
//			
//			return h;
//	}
	
	
}