package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Publication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class PublicationSummaryViewBean {
	private Set<String> publicationCategories;
	private SortedMap<String, List<PublicationBean>> category2Publications = new TreeMap<String, List<PublicationBean>>();

	public PublicationSummaryViewBean(List<PublicationBean> publicationBeans) {
		for (PublicationBean pubBean : publicationBeans) {
			Publication pubObj = (Publication) (pubBean.getDomainFile());
			String category = pubObj.getCategory();
			List<PublicationBean> categoryPubs = null;
			if (category2Publications.get(category) != null) {
				categoryPubs = category2Publications.get(category);
			} else {
				categoryPubs = new ArrayList<PublicationBean>();
				category2Publications.put(category, categoryPubs);
			}
			categoryPubs.add(pubBean);
		}
		publicationCategories = category2Publications.keySet();
	}

	public Set<String> getPublicationCategories() {
		return publicationCategories;
	}

	public void setPublicationCategories(Set<String> publicationCategories) {
		this.publicationCategories = publicationCategories;
	}

	public SortedMap<String, List<PublicationBean>> getCategory2Publications() {
		return category2Publications;
	}
}
