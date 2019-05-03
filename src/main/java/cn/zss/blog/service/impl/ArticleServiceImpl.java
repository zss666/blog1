package cn.zss.blog.service.impl;

import cn.zss.blog.dao.ArticleMapper;
import cn.zss.blog.entity.Article;
import cn.zss.blog.entity.ArticleExample;
import cn.zss.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public void addArticle(Article article) {
        articleMapper.insert(article);
    }

    @Override
    public void deleteArticleById(int id) {
        articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateArticle(Article article) {
        articleMapper.updateByPrimaryKey(article);
    }

    @Override
    public void updateLikeCount(int articleId, int likeCount) {
        articleMapper.selectByPrimaryKey(articleId).setLikeCount(likeCount);
    }

    @Override
    public void updateCommentCount(int articleId, int commentCount) {
        articleMapper.selectByPrimaryKey(articleId).setCommentCount(commentCount);
    }

    @Override
    public Article getOneById(int id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Article> listAll() {
        ArticleExample example =new ArticleExample();
        return articleMapper.selectByExample(example);
    }

    @Override
    public Article listLastest() {
        ArticleExample example = new ArticleExample();
        example.setOrderByClause("id desc");
        return articleMapper.selectByExample(example).get(0);
    }
}
