package com.xtsoft.category.service;

import java.util.List;

import com.xtsoft.category.model.Category;
import com.xtsoft.category.persistence.CategoryPersistence;
import com.xtsoft.exception.SystemException;

public class CategoryService {
	private CategoryPersistence persistence;

	public CategoryPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(CategoryPersistence persistence) {
		this.persistence = persistence;
	}

	public Category getCategoryByPrimaryKey(long categoryId) throws SystemException {
		return getPersistence().findByPrimaryKey(categoryId);

	}

	public Category update(Category model) throws SystemException {
		return getPersistence().update(model);

	}

	public List<Category> getCategoryByParentId(long parentId, String type) throws SystemException {
		return getPersistence().getCategoryByParentId(parentId, type);
	}

	public List<Category> findByFullPath(String fullpath) throws SystemException {
		return getPersistence().findByFullPath(fullpath);
	}

	public Category create(long categoryId) {
		Category model = new Category();
		model.setNew(true);
		model.setCategoryId(categoryId);
		return model;
	}
}
