/**
 * 
 */
package gov.nih.nci.calab.dto.common;

import java.util.List;
import java.util.ArrayList;
/**
 * @author zengje
 *
 */
public class ProtocolBean implements Comparable{
	private Long id;
	private String name;
	private String type;
	
	private List<ProtocolFileBean> fileBeanList = new ArrayList<ProtocolFileBean>();
	/**
	 * 
	 */
	public ProtocolBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ProtocolFileBean> getFileBeanList() {
		return fileBeanList;
	}
	public void setFileBeanList(List<ProtocolFileBean> fileBeanList) {
		this.fileBeanList = fileBeanList;
	}
	public int compareTo(Object obj){
		if(obj instanceof ProtocolBean){
			ProtocolBean inPb = (ProtocolBean)obj;
			int comparison = this.getName().compareTo(inPb.getName());
			return comparison;
		}
		return -1;
	}
	public boolean equals(Object obj){
		boolean eq = false;
		if(obj instanceof ProtocolBean) {
			ProtocolBean c =(ProtocolBean)obj; 			 
			Long thisId = this.getId();
			//String name = this.getName();
			if(thisId != null && thisId.equals(c.getId())){ // &&
				//name != null && name.equals(c.getName())) {
				eq = true;
			}		
		}
		return eq;
	}
}
