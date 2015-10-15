package gov.nih.nci.cananolab.restful.favorites;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.restful.view.SimpleFavoriteBean;
import gov.nih.nci.cananolab.service.favorites.FavoritesService;
import gov.nih.nci.cananolab.service.favorites.impl.FavoritesServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

public class FavoritesBO extends BaseAnnotationBO {
	public List<String> create(FavoriteBean bean, HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FavoritesService service = this.setServiceInSession(request);

		if ((bean.getDataId() != null) && (bean.getLoginName() != null)) {
			FavoriteBean favBean = service.findFavoriteById(bean.getDataId(),
					bean.getLoginName());
			if (favBean != null) {
				msgs.add(bean.getDataName()
						+ " has already been added to your favorites");
				return msgs;
			} else {
				service.addFavorite(bean, request);
				msgs.add("Added to Favorites");
			}
		}
		return msgs;
	}

	public SimpleFavoriteBean findFavorites(HttpServletRequest request) {
		FavoritesService service;
		List<FavoriteBean> list = null;
		SimpleFavoriteBean simpleBean = null;
		try {
			service = this.setServiceInSession(request);
			list = service.findFavorites(request);
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

	private FavoritesService setServiceInSession(HttpServletRequest request)
			throws Exception {
		SecurityService securityService = super
				.getSecurityServiceFromSession(request);
		FavoritesService favoritesService = new FavoritesServiceLocalImpl(
				securityService);
		request.getSession().setAttribute("favoritesService", favoritesService);
		return favoritesService;
	}

	public SimpleFavoriteBean delete(FavoriteBean bean, HttpServletRequest request) {
		List<String> msgs = new ArrayList<String>();
		SimpleFavoriteBean simpleBean = null;
		try {
			FavoritesService service = this.setServiceInSession(request);
			if((bean.getDataId()!=null)&&(bean.getLoginName()!=null)){
					service.deleteFromFavorite(bean, request);
					simpleBean = this.findFavorites(request);
				}
				
			} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleBean;
	}
}
