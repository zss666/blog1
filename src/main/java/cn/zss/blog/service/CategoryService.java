package cn.zss.blog.service;

import cn.zss.blog.entity.Category;

import java.util.List;

public interface CategoryService {
    void deleteCategoryById(int id);

    Category getCategoryById(int id);

    List<Category> listAllCategory();

    void addCategoryInfo(Category categoryInfo);
}
