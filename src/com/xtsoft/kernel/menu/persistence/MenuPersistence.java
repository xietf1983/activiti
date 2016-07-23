package com.xtsoft.kernel.menu.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;
import com.xtsoft.kernel.menu.model.Menu;

/*******************************************************************************
 * 
 * @author x61
 * 
 */
public class MenuPersistence extends BasePersistence<Menu> {

	/**
	 * 根据用户与父节点，查询菜单
	 * 
	 * @param userId
	 * @param parent
	 * @return
	 */
	public List<Menu> getMenuByUserIdAndParent(long userId, long parent) throws SystemException {
		Map<String, Long> para = new HashMap();
		para.put("USERID", userId);
		para.put("PARENT", parent);
		return selectList(SqlMapUtil.MENU_LIST_FETCHBYUSERID, para);
	}

	public List<Menu> getMenuByParent(Menu menu) throws SystemException {
		if (menu != null) {
			return selectList(SqlMapUtil.MENU_LIST_FETCHBYPARENTID, menu.getId());
		} else {
			return selectList(SqlMapUtil.MENU_LIST_FETCHBYPARENTID, 0);
		}
	}

	public boolean isProtectedResource(String url) {
		long countValue = ((Long) selectOne(SqlMapUtil.MENU_COUNT_PROTECTEDRESOURC, url)).intValue();
		if (countValue > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasRight(String url,long userId) {
		Map para = new HashMap();
		para.put("URL", url);
		para.put("USERID", userId);
		long countValue = ((Long) selectOne(SqlMapUtil.MENU_COUNT_HASRIGHT, para)).intValue();
		if (countValue > 0) {
			return true;
		} else {
			return false;
		}
	}

	private static Log _log = LogFactory.getLog(MenuPersistence.class);

}
