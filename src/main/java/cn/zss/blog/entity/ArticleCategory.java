package cn.zss.blog.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ArticleCategory {
    private Integer id;

    private Integer categoryId;

    private Integer articleId;

    private Date createBy;

    private Date modifiedBy;


}