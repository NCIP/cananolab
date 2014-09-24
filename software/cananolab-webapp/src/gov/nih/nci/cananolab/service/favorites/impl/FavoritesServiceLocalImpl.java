package gov.nih.nci.cananolab.service.favorites.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.exception.FavoriteException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseServiceLocalImpl;
import gov.nih.nci.cananolab.service.favorites.FavoritesService;
import gov.nih.nci.cananolab.service.favorites.helper.FavoritesServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

public class FavoritesServiceLocalImpl extends BaseServiceLocalImpl implements FavoritesService{

	private static Logger logger = Logger
			.getLogger(FavoritesServiceLocalImpl.class);
	private FavoritesServiceHelper helper;

	public FavoritesServiceLocalImpl() {
		super();
		helper = new FavoritesServiceHelper(this.securityService);
	}

	public FavoritesServiceLocalImpl(UserBean user) {
		super(user);
		helper = new FavoritesServiceHelper(this.securityService);
	}

	public FavoritesServiceLocalImpl(SecurityService securityService) {
		super(securityService);
		helper = new FavoritesServiceHelper(this.securityService);
	}
	public void addFavorite(FavoriteBean bean, HttpServletRequest request) throws FavoriteException,
			NoAccessException {
		try{
		bean.setId(null);
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		bean.setLoginName(user.getLoginName());
		CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
				.getApplicationService();

		appService.saveOrUpdate(bean);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFromFavorite(FavoriteBean bean, HttpServletRequest request)
			throws FavoriteException, NoAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FavoriteBean findFavoriteById(String dataId, String loginName) throws FavoriteException,
			NoAccessException {
		FavoriteBean bean = null;
		try {
			bean = helper.findFavouritesById(dataId, loginName);
			
		} catch (Exception e) {
			String err = "Problem finding the favorite by id: " + dataId;
			logger.error(err, e);
			throw new FavoriteException(err, e);
		}
		return bean;
	}

	@Override
	public List<FavoriteBean> findFavorites(HttpServletRequest request)
			throws FavoriteException, NoAccessException {
		List<FavoriteBean> list = null;
		try {
			list = helper.findFavourites(request);
			
		} catch (Exception e) {
			String err = "Problem finding the favorite ";
			logger.error(err, e);
			throw new FavoriteException(err, e);
		}
		return list;
	}

}
