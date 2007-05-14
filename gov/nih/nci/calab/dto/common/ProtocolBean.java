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
public class ProtocolBean {
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

}
