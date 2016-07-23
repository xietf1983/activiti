package com.xtsoft.category.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xtsoft.SqlMapUtil;
import com.xtsoft.category.model.Category;
import com.xtsoft.dao.base.persistence.BasePersistence;
import com.xtsoft.exception.SystemException;

public class CategoryPersistence extends BasePersistence<Category> {
	public Category create(long categoryId) {
		Category model = new Category();
		model.setNew(true);
		model.setCategoryId(categoryId);
		return model;
	}

	public Category update(Category model) throws SystemException {
		if (model.getParentId() == 0) {
			model.setFullPath("0|" + model.getCategoryId() + "|");
		} else {
			Category c = findByPrimaryKey(model.getParentId());
			model.setFullPath(c.getFullPath() + model.getCategoryId() + "|");
		}
		if (findByPrimaryKey(model.getCategoryId()) != null) {
			update(SqlMapUtil.CATEGORY_UPDATE, model);
		} else {
			insert(SqlMapUtil.CATEGORY_INSERT, model);
		}
		return model;

	}

	public void removeCategory(long typeId) throws SystemException {
		getSqlSession().delete(SqlMapUtil.CATEGORY_DELETE, typeId);
	}

	public Category findByPrimaryKey(long typeId) throws SystemException {
		Category model = (Category) selectOne(SqlMapUtil.CATEGORY_FETCHBYPRIMARYKEY, typeId);
		return model;
	}

	public List<Category> findByFullPath(String fullpath) throws SystemException {
		return selectList(SqlMapUtil.CATEGORY_LIST_FETCHBYFULLPATH, fullpath);
	}

	public List<Category> getCategoryByParentId(long parentId, String type) {
		Map map = new HashMap();
		map.put("PARENTID", parentId);
		if (type != null && !type.equals("")) {
			map.put("TYPEKEY", type);
		}
		return selectList(SqlMapUtil.CATEGORY_LIST_FETCHBYPARENTID, map);

	}
}
