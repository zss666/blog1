package cn.zss.blog.Inteceptor;

import cn.zss.blog.dao.TicketMapper;
import cn.zss.blog.dao.UserMapper;
import cn.zss.blog.entity.HostHolder;
import cn.zss.blog.entity.Ticket;
import cn.zss.blog.entity.User;
import cn.zss.blog.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

public class ForeRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private HostHolder hostHolder;

    private User user = new User();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //处理用户信息，判断是否有ticket,一个用户一个ticket，但是有时限
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
            //判断ticket是否过期和无效
            if (ticket != null) {
                Ticket ticket1 = ticketService.getTicketByTicket(ticket);
                //当ticket不存在，无效，过期返回空，不能通过拦截
                if (ticket1 == null || ticket1.getExpired().before(new Date()) || ticket1.getStatus() != 0) {
                    return false;
                }
                else {
                    //将当前用户替换
                    user = userMapper.selectByPrimaryKey(ticket1.getUserId());
                    hostHolder.setUser(user);
                    return true;
                    //不能直接放在request里，因为是全局的一个ticket，其他服务想要读取时可能不会用到httprequest请求，
                    // 但是可以注入hostholder来获取用户信息。
                }
            }
        }
        return true;
    }

    //渲染之前提供的后处理方法，可以添加模型数据，自动传给前端。
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            Method method = handlerMethod.getMethod();
            // 记录访问的操作，如果没有则记录资源
            user.setRemark(method.getName());
            userMapper.updateByPrimaryKey(user);

        } else {
            String uri = httpServletRequest.getRequestURI();
            user.setRemark(uri);
            userMapper.updateByPrimaryKey(user);
        }
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject(hostHolder.getUser());
            hostHolder.clear();
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


}
