package cn.zss.blog.service;

import cn.zss.blog.entity.Article;

import java.util.List;

public interface ArticleCategoryService {
    void addArticleById(int articleId,int categoryId);

    void deleteArticleById(int articleId);

    List<Article> listArticleByCategoryId(int categoryId);

    int getArticleCountByCategoryId(int categoryId);
}
