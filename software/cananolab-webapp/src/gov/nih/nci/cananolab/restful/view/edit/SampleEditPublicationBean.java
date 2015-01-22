package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.dto.common.PublicationBean;
import gov.nih.nci.cananolab.dto.common.PublicationSummaryViewBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.restful.core.InitSetup;
import gov.nih.nci.cananolab.restful.view.SimplePublicationBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

public class SampleEditPublicationBean {
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

	public void transferPublicationBeanForSummaryEdit(HttpServletRequest request,
			PublicationSummaryViewBean pubBean) {
		SortedSet<String> types = null;
		if (pubBean == null)
			return;

		try {
			//
			types = InitSetup.getInstance().getDefaultAndOtherTypesByLookup(request,
					"publicationCategories", "publication", "category",
					"otherCategory", true);
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setPublicationCategories(types);

		try {
			
			for(final Iterator it = types.iterator(); it.hasNext(); ){
				String publicationCategory = (String) it.next();
				pubList = new ArrayList<SimplePublicationBean>();
				if(pubBean.getCategory2Publications().containsKey(publicationCategory)){
				for(PublicationBean pBean : pubBean.getCategory2Publications().get(publicationCategory)){
					publicationBean = new SimplePublicationBean();

					publicationBean.setDisplayName(pBean.getDisplayName());
					publicationBean.setDescription(pBean.getDescription());
					publicationBean.setResearchAreas(pBean.getResearchAreas());
					publicationBean.setKeywordsDisplayName(pBean.getKeywordsDisplayName());
					publicationBean.setKeywordsStr(pBean.getKeywordsStr());
					Publication pub = (Publication) pBean.getDomainFile();
					publicationBean.setStatus(pub.getStatus());
					System.out.println("publicationTypes from publication==="+pub.getCategory());
					
					pubList.add(publicationBean);
			}
				}
				category2Publications.put(publicationCategory, pubList);

        	}
		} catch (Exception e) {
			System.out.println("Error while setting the SimplePublicationBean"
					+ e);
		}

	}

}
