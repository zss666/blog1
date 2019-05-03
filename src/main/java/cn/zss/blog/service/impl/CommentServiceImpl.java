package cn.zss.blog.service.impl;

import cn.zss.blog.dao.CommentMapper;
import cn.zss.blog.entity.Comment;
import cn.zss.blog.entity.CommentExample;
import cn.zss.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public void addComment(Comment comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void deleteComment(int id) {
        commentMapper.deleteByPrimaryKey(id);
    }


    @Override
    public int getCommentCount() {
        return listComment().size();
    }

    @Override
    public List<Comment> listComment() {
        CommentExample example = new CommentExample();
        commentMapper.selectByExample(example);
        return commentMapper.selectByExample(example);
    }

    @Override
    public Comment getComment(int id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Comment> listCommentByArticleId(int articleId) {
        CommentExample example = new CommentExample();
        example.or().andArticleIdEqualTo(articleId);
        commentMapper.selectByExample(example);
        return commentMapper.selectByExample(example);
    }

    @Override
    public List<Comment> listCommentByUserId(int userId) {
        CommentExample example = new CommentExample();
        example.or().andUserIdEqualTo(userId);
        commentMapper.selectByExample(example);
        return commentMapper.selectByExample(example);
    }

    @Override
    public int getCommentCountByArticleId(int articleId) {
        return listCommentByArticleId(articleId).size();
    }

    @Override
    public int getCommentCountByUserId(int userId) {
        return listCommentByUserId(userId).size();
    }

}
