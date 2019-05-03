package cn.zss.blog.async.handler;


import cn.zss.blog.async.EventHandler;
import cn.zss.blog.async.EventModel;
import cn.zss.blog.async.EventType;
import cn.zss.blog.entity.Inform;
import cn.zss.blog.entity.User;
import cn.zss.blog.service.InformService;
import cn.zss.blog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class LoginExceptionHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginExceptionHandler.class);


    @Autowired
    private InformService informService;

    @Autowired
    private UserService userService;


    //实际业务上需要信息服务通知后台博主，待扩展发送邮件给用户说明登录异常
    @Override
    public void doHandle(EventModel eventModel) {
        Inform inform = new Inform();
        inform.setUserId(eventModel.getActorId());
        User user = userService.getUserById(eventModel.getActorId());
        inform.setContent("用户" + user.getName() +"登录异常" );
        informService.addInform(inform);
        System.out.println("login");
        logger.info(eventModel.toString());
    }
    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
