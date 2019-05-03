package cn.zss.blog.service.impl;

import cn.zss.blog.dao.ArticleCategoryMapper;
import cn.zss.blog.dao.ArticleMapper;
import cn.zss.blog.entity.Article;
import cn.zss.blog.entity.ArticleCategory;
import cn.zss.blog.entity.ArticleCategoryExample;
import cn.zss.blog.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService{
    @Autowired(required = false)
    ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public void addArticleById(int articleId,int categoryId) {
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleId(articleId);
        articleCategory.setCategoryId(categoryId);
        articleCategoryMapper.insert(articleCategory);
    }

    @Override
    public void deleteArticleById(int articleId) {
        ArticleCategoryExample example = new ArticleCategoryExample();
        example.or().andArticleIdEqualTo(articleId);
        ArticleCategory articleCategory = articleCategoryMapper.selectByExample(example).get(0);
        articleCategoryMapper.deleteByPrimaryKey(articleCategory.getId());
    }

    @Override
    public List<Article> listArticleByCategoryId(int categoryId) {
        ArticleCategoryExample example = new ArticleCategoryExample();
        example.or().andCategoryIdEqualTo(categoryId);
        List<ArticleCategory> articleCategorys = articleCategoryMapper.selectByExample(example);
        List<Article> articles=new ArrayList<>();
        for (ArticleCategory articleCategory:articleCategorys){
            articles.add(articleMapper.selectByPrimaryKey(articleCategory.getArticleId()));
        }
        return articles;
    }

    @Override
    public int getArticleCountByCategoryId(int categoryId) {
        return listArticleByCategoryId(categoryId).size();
    }
}
