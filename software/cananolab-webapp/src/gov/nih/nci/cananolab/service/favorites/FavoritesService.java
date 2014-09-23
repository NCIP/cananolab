package gov.nih.nci.cananolab.service.favorites;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.exception.FavoriteException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.service.BaseService;

/**
 * This interface defines methods involved in adding and deleting the samples, publications and protocols to the myFavourites section
 *
 * @author jonnalah
 *
 */
public interface FavoritesService extends BaseService{

	public void addToFavorite(FavoriteBean bean, HttpServletRequest request)
			throws FavoriteException, NoAccessException;
	
	public void deleteFromFavorite(FavoriteBean bean, HttpServletRequest request)
			throws FavoriteException, NoAccessException;
	
	public FavoriteBean findFavoriteById(Long id)
			throws FavoriteException, NoAccessException;

	public List<FavoriteBean> findFavorites(HttpServletRequest request)
			throws FavoriteException, NoAccessException;
}
