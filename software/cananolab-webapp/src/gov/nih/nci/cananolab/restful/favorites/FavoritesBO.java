package gov.nih.nci.cananolab.restful.favorites;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.restful.core.BaseAnnotationBO;
import gov.nih.nci.cananolab.service.favorites.FavoritesService;
import gov.nih.nci.cananolab.service.favorites.impl.FavoritesServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;

public class FavoritesBO extends BaseAnnotationBO{
	public List<String> create(FavoriteBean bean,
			HttpServletRequest request)
			throws Exception {
		List<String> msgs = new ArrayList<String>();
		FavoritesService service = this.setServiceInSession(request);

		FavoriteBean favBean = service.findFavoriteById(bean.getId());
		if(favBean!=null){
			msgs.add(bean.getDataName() + "has already been added to your favorites");
			return msgs;
		}
		service.addToFavorite(bean, request);

		msgs.add("success");
		
		return msgs;
	}
	
	public List<FavoriteBean> findFavourite(HttpServletRequest request) {
		FavoritesService service;
		List<FavoriteBean> list = null;
		try {
			service = this.setServiceInSession(request);
			list = service.findFavorites(request);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
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
}
