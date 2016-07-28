package gov.nih.nci.cananolab.service.favorites.helper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.dto.common.FavoriteBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

@Component("favoritesServiceHelper")
public class FavoritesServiceHelper
{
	private static Logger logger = Logger.getLogger(ProtocolServiceHelper.class);

	public FavoriteBean findFavouritesById(String dataId, String loginName)
	{
		FavoriteBean bean = null;

		CaNanoLabApplicationService appService;
		try {
			appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(FavoriteBean.class);
			Criterion crit1 = Restrictions.eq("dataId", dataId);
			Criterion crit2 = Restrictions.like("loginName", loginName, MatchMode.EXACT);
			crit.add(Expression.and(crit1, crit2));
			List result = appService.query(crit);
			if (!result.isEmpty()) {
				bean = (FavoriteBean) result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bean;
	}

	public List<FavoriteBean> findFavourites(HttpServletRequest request)
	{
		List<FavoriteBean> list = new ArrayList<FavoriteBean>();

		CaNanoLabApplicationService appService;
		try {
			appService = (CaNanoLabApplicationService) ApplicationServiceProvider.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(FavoriteBean.class);
			crit.add(Restrictions.ilike("loginName", SpringSecurityUtil.getLoggedInUserName(), MatchMode.EXACT));

			List result = appService.query(crit);
//			for (Object obj : result) {
			for(int i = 0; i < result.size(); i++){
				FavoriteBean bean = (FavoriteBean) result.get(i);
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
