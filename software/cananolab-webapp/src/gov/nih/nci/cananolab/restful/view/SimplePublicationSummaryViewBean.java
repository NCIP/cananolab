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

			if (pubBean.getCategory2Publications().lastKey().equals("review")) {

				for (int i = 0; i < pubBean.getCategory2Publications()
						.get("review").size(); i++) {
					publicationBean = new SimplePublicationBean();

					publicationBean.setDisplayName(pubBean
							.getCategory2Publications().get("review").get(i)
							.getDisplayName());
					publicationBean.setDescription(pubBean
							.getCategory2Publications().get("review").get(i)
							.getDescription());

					publicationBean.setResearchAreas(pubBean
							.getCategory2Publications().get("review").get(i)
							.getResearchAreas());
					publicationBean.setKeywordsDisplayName(pubBean
							.getCategory2Publications().get("review").get(i)
							.getKeywordsDisplayName());
					publicationBean.setKeywordsStr(pubBean
							.getCategory2Publications().get("review").get(i)
							.getKeywordsStr());
					Publication pub = (Publication) pubBean
							.getCategory2Publications().get("review").get(i)
							.getDomainFile();
					publicationBean.setStatus(pub.getStatus());

					pubList.add(publicationBean);

				}
				category2Publications.put("review", pubList);
			}

			if (pubBean.getCategory2Publications().firstKey().equals("report")) {

				publicationBean = new SimplePublicationBean();
				pubList = new ArrayList<SimplePublicationBean>();

				for (int i = 0; i < pubBean.getCategory2Publications()
						.get("report").size(); i++) {

					publicationBean.setDescription(pubBean
							.getCategory2Publications().get("report").get(i)
							.getDescription());
					publicationBean.setDisplayName(pubBean
							.getCategory2Publications().get("report").get(i)
							.getDisplayName());
					publicationBean.setResearchAreas(pubBean
							.getCategory2Publications().get("report").get(i)
							.getResearchAreas());
					publicationBean.setKeywordsDisplayName(pubBean
							.getCategory2Publications().get("report").get(i)
							.getKeywordsDisplayName());
					publicationBean.setKeywordsStr(pubBean
							.getCategory2Publications().get("report").get(i)
							.getKeywordsStr());
					Publication pub = (Publication) pubBean
							.getCategory2Publications().get("report").get(i)
							.getDomainFile();
					publicationBean.setStatus(pub.getStatus());

					pubList.add(publicationBean);

				}
				category2Publications.put("report", pubList);
			}
		} catch (Exception e) {
			System.out.println("Error while setting the SimplePublicationBean"
					+ e);
		}

	}

}
