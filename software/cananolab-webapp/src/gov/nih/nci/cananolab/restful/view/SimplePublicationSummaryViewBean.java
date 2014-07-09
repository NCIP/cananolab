package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * SimplePublicationBean to hold a subset of the data in PublicationSummaryViewBean for display on web page.
 * 
 * @author jonnalah
 *
 */

public class SimplePublicationSummaryViewBean {
	
	private Set<String> publicationCategories;
	
	public SimplePublicationBean getPublicationBean() {
		return publicationBean;
	}
	public void setPublicationBean(SimplePublicationBean publicationBean) {
		this.publicationBean = publicationBean;
	}


	SimplePublicationBean publicationBean;
	
	public Set<String> getPublicationCategories() {
		return publicationCategories;
	}
	public void setPublicationCategories(Set<String> publicationCategories) {
		this.publicationCategories = publicationCategories;
	}
	
	
	public SortedMap<String, List<PublicationBean>> getCategory2Publications() {
		return category2Publications;
	}
	public void setCategory2Publications(
			SortedMap<String, List<PublicationBean>> category2Publications) {
		this.category2Publications = category2Publications;
	}


	private SortedMap<String, List<PublicationBean>> category2Publications = new TreeMap<String, List<PublicationBean>>();
	

	public void transferPublicationBeanForSummaryView(PublicationSummaryViewBean pubBean){
		if(pubBean == null) return;
	//	setCategory2Publications(pubBean.getCategory2Publications());
		
		setPublicationCategories(pubBean.getPublicationCategories());
	
//		publicationBean.setDescription(pubBean.getCategory2Publications().get("report").get(0).getDescription());
//		publicationBean.setDisplayName(pubBean.getCategory2Publications().get("report").get(0).getDisplayName());
//		setPublicationBean(publicationBean);
		
		
	}

}
