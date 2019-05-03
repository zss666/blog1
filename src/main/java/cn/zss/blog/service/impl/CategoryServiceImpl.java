package cn.zss.blog.service.impl;

import cn.zss.blog.dao.CategoryMapper;
import cn.zss.blog.entity.Category;
import cn.zss.blog.entity.CategoryExample;
import cn.zss.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired(required = false)
    CategoryMapper categoryMapper;

    @Override
    public void deleteCategoryById(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> listAllCategory() {
        CategoryExample example = new CategoryExample();
        return categoryMapper.selectByExample(example);
    }

    @Override
    public void addCategoryInfo(Category categoryInfo) {
        categoryMapper.insert(categoryInfo);
    }
}
