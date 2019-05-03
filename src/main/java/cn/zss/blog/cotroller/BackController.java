package cn.zss.blog.cotroller;

import cn.zss.blog.entity.Article;
import cn.zss.blog.entity.Category;
import cn.zss.blog.entity.Comment;
import cn.zss.blog.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//待完善、、、、、、、
@Controller
@RequestMapping("/admin")
public class BackController {
    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleCategoryService articleCategoryService;

    @Autowired
    SysLogService sysLogService;

    @Autowired
    MessageService messageService;

    @Autowired
    InformService informService;



    @Autowired
    CategoryService categoryService;



    @Autowired
    CommentService commentService;


    //文章，罗列统计，增删改
    //分类，罗列统计，增删改
    //文章分类，罗列统计，增删
    //评论，罗列统计，删，通知
    //私信，罗列统计，通知回复
    //点赞，罗列统计，通知
    //用户，罗列统计，登录通知


        /**
         * 增加一篇文章
         *
         * @return
         */
        @ApiOperation("增加一篇文章")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String"),
                @ApiImplicitParam(name = "summary", value = "文章简介", required = true, dataType = "String"),
                @ApiImplicitParam(name = "isTop", value = "文章是否置顶", required = true, dataType = "Boolean"),
                @ApiImplicitParam(name = "categoryId", value = "文章分类对应ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "content", value = "文章md源码", required = true, dataType = "String"),
                @ApiImplicitParam(name = "pictureUrl", value = "文章题图url", required = true, dataType = "String")
        })
        @PostMapping("article/")
        public String addArticle(@RequestBody Article article) {
            articleService.addArticle(article);
            return null;
        }


        /**
         * 删除一篇文章
         *
         * @param id
         * @return
         */
        @ApiOperation("删除一篇文章")
        @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long")
        @DeleteMapping("article/{id}")
        public String deleteArticle(@PathVariable int id) {
            articleService.deleteArticleById(id);
            return null;
        }

        /**
         * 编辑/更新一篇文章
         *
         * @return
         */
        @ApiOperation("编辑/更新一篇文章")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String"),
                @ApiImplicitParam(name = "summary", value = "文章简介", required = true, dataType = "String"),
                @ApiImplicitParam(name = "isTop", value = "文章是否置顶", required = true, dataType = "Boolean"),
                @ApiImplicitParam(name = "categoryId", value = "文章分类对应ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "content", value = "文章md源码", required = true, dataType = "String"),
                @ApiImplicitParam(name = "pictureUrl", value = "文章题图url", required = true, dataType = "String")
        })
        @PutMapping("article/{id}")
        public String updateArticle(@PathVariable int id, @RequestBody Article article) {
            article.setId(id);
            articleService.updateArticle(article);
//        System.out.println(articleDto.getTop());
            return null;
        }

        /**
         * 改变某一篇文章的分类
         *
         * @param id
         * @return
         */
        @ApiOperation("改变文章分类")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, dataType = "Long"),
        })
        @PutMapping("article/sort/{id}")
        public String changeArticleCategory(@PathVariable int id, int categoryId) {
            articleCategoryService.deleteArticleById(id);
            articleCategoryService.addArticleById(id,categoryId);
            return null;
        }

        /**
         * 通过题图的id更改题图信息
         *
         * @param id
         * @return
         */
        @ApiOperation("更改文章题图信息")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "pictureUrl", value = "题图URL", required = true, dataType = "String")
        })
        @PutMapping("article/picture/{id}")
        public String updateArticlePicture(@PathVariable int id) {
            Article article = articleService.getOneById(id);
            articleService.updateArticle(article);
            return null;
        }


        /**
         * 增加一条分类信息数据
         *
         * @return
         */
        @ApiOperation("增加分类信息")
        @ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = "String")
        @PostMapping("category")
        public String addCategoryInfo(@RequestBody Category categoryInfo) {
            categoryService.addCategoryInfo(categoryInfo);
            return null;
        }

        /**
         * 更新/编辑一条分类信息
         *
         * @param id
         * @return
         */
        @ApiOperation("更新/编辑分类信息")
        @ApiImplicitParams({
                @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "Long"),
                @ApiImplicitParam(name = "name", value = "分类名称", required = true, dataType = "String")
        })
        @PutMapping("category/{id}")
        public String updateCategoryInfo(@PathVariable int id, @RequestBody Category categoryInfo) {
            categoryService.deleteCategoryById(id);
            categoryService.addCategoryInfo(categoryInfo);
            return null;
        }

        /**
         * 根据ID删除分类信息
         *
         * @param id
         * @return
         */
        @ApiOperation("删除分类信息")
        @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long")
        @DeleteMapping("category/{id}")
        public String deleteCategoryInfo(@PathVariable int id) {
            categoryService.deleteCategoryById(id);
            return null;
        }

        /**
         * 通过id获取一条分类信息
         *
         * @param id
         * @return
         */
        @ApiOperation("获取某一条分类信息")
        @ApiImplicitParam(name = "id", value = "分类ID", required = true, dataType = "Long")
        @GetMapping("category/{id}")
        public Category getCategoryInfo(@PathVariable int id) {
            return categoryService.getCategoryById(id);
        }

        /**
         * 通过评论ID删除文章评论信息
         *
         * @param id
         * @return
         */
        @ApiOperation("删除文章评论信息")
        @ApiImplicitParam(name = "id", value = "评论ID", required = true, dataType = "Long")
        @DeleteMapping("comment/article/{id}")
        public String deleteArticleComment(@PathVariable int id) {
            commentService.deleteComment(id);
            return null;
        }

        /**
         * 通过id删除某一条留言
         *
         * @param id
         * @return
         */
        @ApiOperation("删除一条留言")
        @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
        @DeleteMapping("comment/{id}")
        public String deleteComment(@PathVariable int id) {
            commentService.deleteComment(id);
            return null;
        }

        /**
         * 回复留言/评论，通过id去找到对应的Email
         *
         * @param id
         * @return
         */
        @ApiOperation("回复留言/评论")
        @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
        @GetMapping("comment/reply/{id}")
        public String replyComment(@PathVariable int id) {
            return null;
        }

        /**
         * 通过id获取某一条评论/留言
         *
         * @param id
         * @return
         */
        @ApiOperation("获取某一条评论")
        @ApiImplicitParam(name = "id", value = "评论/留言ID", required = true, dataType = "Long")
        @GetMapping("comment/{id}")
        public Comment getCommentById(@PathVariable int id) {
            return commentService.getComment(id);
        }


        /**
         * 通过ID获取一篇文章，内容为MD源码格式
         *
         * @param id
         * @return
         */
        @ApiOperation("获取一篇文章，内容为md源码格式")
        @ApiImplicitParam(name = "id", value = "文章ID", required = true, dataType = "int")
        @GetMapping("article/{id}")
        public Article getArticleById(@PathVariable int id) {
            return articleService.getOneById(id);
        }


}
