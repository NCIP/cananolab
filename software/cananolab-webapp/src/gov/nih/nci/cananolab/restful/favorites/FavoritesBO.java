package gov.nih.nci.cananolab.restful.favorites;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.view.SimpleFavoriteBean;
import gov.nih.nci.cananolab.security.service.SpringSecurityAclService;
import gov.nih.nci.cananolab.service.curation.CurationService;
import gov.nih.nci.cananolab.service.favorites.FavoritesService;
import gov.nih.nci.cananolab.service.sample.SampleService;

@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
@Component("favoritesBO")
public class FavoritesBO extends BaseAnnotationBO
{
	@Autowired
	private SpringSecurityAclService springSecurityAclService;

	@Autowired
	private CurationService curationServiceDAO;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private FavoritesService favoritesService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public List<String> create(FavoriteBean bean, HttpServletRequest request) throws Exception 
	{
		List<String> msgs = new ArrayList<String>();
		if ((bean.getDataId() != null) && (bean.getLoginName() != null)) {
			FavoriteBean favBean = favoritesService.findFavoriteById(bean.getDataId(), bean.getLoginName());
			if (favBean != null) {
				msgs.add(bean.getDataName() + " has already been added to your favorites");
				return msgs;
			} else {
				favoritesService.addFavorite(bean, request);
				msgs.add("Added to Favorites");
			}
		}
		return msgs;
	}

	public SimpleFavoriteBean findFavorites(HttpServletRequest request)
	{
		List<FavoriteBean> list = null;
		SimpleFavoriteBean simpleBean = null;
		try {
			list = favoritesService.findFavorites(request);
			simpleBean = new SimpleFavoriteBean();
			List<FavoriteBean> samples = new ArrayList<FavoriteBean>();
			List<FavoriteBean> publications = new ArrayList<FavoriteBean>();
			List<FavoriteBean> protocols = new ArrayList<FavoriteBean>();

			for(FavoriteBean favBean : list){
				if(favBean.getDataType().equalsIgnoreCase("sample")){
					samples.add(favBean);
					simpleBean.setSamples(samples);
				}else if(favBean.getDataType().equalsIgnoreCase("publication")){
					publications.add(favBean);
					simpleBean.setPublications(publications);
				}else{
					protocols.add(favBean);
					simpleBean.setProtocols(protocols);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return simpleBean;
	}

	public SimpleFavoriteBean delete(FavoriteBean bean, HttpServletRequest request)
	{
		SimpleFavoriteBean simpleBean = null;
		try {
			if ((bean.getDataId()!=null) && (bean.getLoginName()!=null)){
				favoritesService.deleteFromFavorite(bean, request);
				simpleBean = this.findFavorites(request);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleBean;
	}

	@Override
	public CurationService getCurationServiceDAO() {
		return curationServiceDAO;
	}

	@Override
	public SampleService getSampleService() {
		return sampleService;
	}

	@Override
	public SpringSecurityAclService getSpringSecurityAclService() {
		return springSecurityAclService;
	}

	@Override
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

}
