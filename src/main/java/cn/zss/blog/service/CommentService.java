package cn.zss.blog.service;

import cn.zss.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);

    void deleteComment(int id);

    int getCommentCount();

    List<Comment> listComment();

    Comment getComment(int id);

    List<Comment> listCommentByArticleId(int articleId);

    List<Comment> listCommentByUserId(int userId);

    int getCommentCountByArticleId(int articleId);

    int getCommentCountByUserId(int userId);

}
