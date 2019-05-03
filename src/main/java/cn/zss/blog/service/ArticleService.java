package cn.zss.blog.service;

import cn.zss.blog.entity.Article;

import java.util.List;

public interface ArticleService {
    void addArticle(Article article);

    void deleteArticleById(int id);

    void updateArticle(Article article);

    void updateLikeCount(int articleId,int likeCount);

    void updateCommentCount(int articleId,int commentCount);

    Article getOneById(int id);


    List<Article> listAll();


    Article listLastest();


}
