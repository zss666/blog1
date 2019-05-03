package cn.zss.blog.entity;

import java.util.Date;

public class Message {
    private Integer id;

    private Integer userId;

    private String content;

    private Date createBy;

    private Integer hasRead;

    private Integer hasAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Date createBy) {
        this.createBy = createBy;
    }

    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    public Integer getHasAnswer() {
        return hasAnswer;
    }

    public void setHasAnswer(Integer hasAnswer) {
        this.hasAnswer = hasAnswer;
    }
}