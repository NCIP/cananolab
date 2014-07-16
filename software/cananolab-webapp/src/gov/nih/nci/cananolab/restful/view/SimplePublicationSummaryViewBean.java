package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.domain.common.Publication;
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
 * SimplePublicationSummaryViewBean to hold a subset of the data in
 * PublicationSummaryViewBean for display on web page.
 * 
 * @author jonnalah
 * 
 */

public class SimplePublicationSummaryViewBean {

	Set<String> publicationCategories;

	SortedMap<String, List<SimplePublicationBean>> category2Publications = new TreeMap<String, List<SimplePublicationBean>>();

	List<SimplePublicationBean> pubList;

	SimplePublicationBean publicationBean;

	public Set<String> getPublicationCategories() {
		return publicationCategories;
	}

	public void setPublicationCategories(Set<String> publicationCategories) {
		this.publicationCategories = publicationCategories;
	}

	public SortedMap<String, List<SimplePublicationBean>> getCategory2Publications() {
		return category2Publications;
	}

	public void setCategory2Publications(
			SortedMap<String, List<SimplePublicationBean>> category2Publications) {
		this.category2Publications = category2Publications;
	}

	public void transferPublicationBeanForSummaryView(
			PublicationSummaryViewBean pubBean) {
		if (pubBean == null)
			return;

		setPublicationCategories(pubBean.getPublicationCategories());

		try {
			pubList = new ArrayList<SimplePublicationBean>();

			for(String publicationCategory : pubBean.getPublicationCategories()){
				for(PublicationBean pBean : pubBean.getCategory2Publications().get(publicationCategory)){
					publicationBean = new SimplePublicationBean();

					publicationBean.setDisplayName(pBean.getDisplayName());
					publicationBean.setDescription(pBean.getDescription());
					publicationBean.setResearchAreas(pBean.getResearchAreas());
					publicationBean.setKeywordsDisplayName(pBean.getKeywordsDisplayName());
					publicationBean.setKeywordsStr(pBean.getKeywordsStr());
					Publication pub = (Publication) pBean.getDomainFile();
					publicationBean.setStatus(pub.getStatus());
					
					pubList.add(publicationBean);
			}
				category2Publications.put(publicationCategory, pubList);

        	}
		} catch (Exception e) {
			System.out.println("Error while setting the SimplePublicationBean"
					+ e);
		}

	}

}
