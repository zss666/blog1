package cn.zss.blog.async.handler;

import cn.zss.blog.async.EventHandler;
import cn.zss.blog.async.EventModel;
import cn.zss.blog.async.EventType;
import cn.zss.blog.entity.Inform;
import cn.zss.blog.entity.Message;
import cn.zss.blog.entity.User;
import cn.zss.blog.service.InformService;
import cn.zss.blog.service.MessageService;
import cn.zss.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(LikeHandler.class);

    @Autowired
    private InformService informService;

    @Autowired
    private UserService userService;


    //实际业务上需要信息服务通知后台博主
    @Override
    public void doHandle(EventModel eventModel) {
        Inform inform = new Inform();
        inform.setUserId(eventModel.getActorId());
        User user = userService.getUserById(eventModel.getActorId());
        inform.setContent("用户" + user.getName() + "赞了你的资讯"
                + ",http://127.0.0.1:8088/news/" + eventModel.getEntityId());
        informService.addInform(inform);
        System.out.println("like");
        logger.info(eventModel.toString());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
