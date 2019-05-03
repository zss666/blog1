package cn.zss.blog.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Article {
    private Integer id;

    private String title;

    private String summary;

    private String content;

    private Boolean isTop;

    private Integer traffic;

    private Integer commentCount;

    private Integer likeCount;

    private Integer dislikeCount;

    private Date createBy;

    private Date modifiedBy;

}