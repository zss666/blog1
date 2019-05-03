package cn.zss.blog.cotroller;


import cn.zss.blog.async.EventModel;
import cn.zss.blog.async.EventProducer;
import cn.zss.blog.async.EventType;
import cn.zss.blog.entity.*;
import cn.zss.blog.service.*;
import cn.zss.blog.util.JSONUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/index")
public class ForeController {
    private static final Logger logger = LoggerFactory.getLogger(ForeController.class);

    @Autowired
    ArticleService articleService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ArticleCategoryService articleCategoryService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    @Autowired
    EventProducer eventProducer;

    //罗列各分类文章
    private List<ViewObject> listArticleByCategoryId(int articleCategoryId){
        List<Article> articles = articleCategoryService.listArticleByCategoryId(articleCategoryId);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (Article article : articles) {
            ViewObject vo = new ViewObject();
            vo.set("article", article.getTitle());
            vo.set("articleId",article.getId());
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId,article.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }
    //罗列所有文章
    private List<ViewObject> listArticle(){
        List<Article> articles = articleService.listAll();
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (Article article : articles) {
            ViewObject vo = new ViewObject();
            vo.set("article", article.getTitle());
            if (localUserId != 0) {
                vo.set("like", likeService.getLikeStatus(localUserId,article.getId()));
            } else {
                vo.set("like", 0);
            }
            vos.add(vo);
        }
        return vos;
    }
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", listArticle());
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/articleCategory/{articleCategoryId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("articleCategoryId") int articleCategoryId) {
        model.addAttribute("vos", listArticleByCategoryId(articleCategoryId));
        return "home";
    }

    //查看文章
    @RequestMapping(path = {"/articles/{articleId}"}, method = {RequestMethod.GET})
    public String articleDetail(@PathVariable("articleId") int articleId, Model model) {
        Article article = articleService.getOneById(articleId);
        if (article!= null) {
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStatus(localUserId,article.getId()));
            } else {
                model.addAttribute("like", 0);
            }
            // 评论
            List<Comment> comments = commentService.listCommentByArticleId(article.getId());
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUserById(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("article", article);
        return "detail";
    }

    //列出分类
    @RequestMapping(path = "category",method = RequestMethod.GET)
    public List<Category> listCategory(){
        return categoryService.listAllCategory();
    }

    //最新的，总的文章罗列，统计


    //增加留言
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("articleId") int articleId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setArticleId(articleId);
            commentService.addComment(comment);
            eventProducer.fireEvent((new EventModel(EventType.COMMENT)
                    .setEntityOwnerId(0)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(articleId)));
            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount();
            articleService.updateCommentCount(articleId,count);

        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(articleId);
    }

    //点赞，点踩
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newId") int articleId) {
        long likeCount = likeService.like(hostHolder.getUser().getId(),articleId);
        // 更新喜欢数
        Article article = articleService.getOneById(articleId);
        articleService.updateLikeCount(articleId, (int) likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(0)//0号是博主账号
                .setActorId(hostHolder.getUser().getId()).setEntityId(articleId));
        return JSONUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), newsId);
        // 更新喜欢数
        articleService.updateLikeCount(newsId, (int) likeCount);
        return JSONUtil.getJSONString(0, String.valueOf(likeCount));
    }

    //增加私信,更新私信数，异步通知博主
    @RequestMapping(path = {"/addMessage"}, method = {RequestMethod.POST})
    public String addMessage(@RequestParam("content") String content) {
        try {
            Message message=new Message();
            message.setContent(content);
            messageService.addMessage(message);
            eventProducer.fireEvent(new EventModel(EventType.MASSAGE)
            .setEntityOwnerId(0)
            .setActorId(hostHolder.getUser().getId()));

        } catch (Exception e) {
            logger.error("提交私信错误" + e.getMessage());
        }
        return JSONUtil.getJSONString(0,String.valueOf(messageService.getAllMessageCount()));
    }

}
