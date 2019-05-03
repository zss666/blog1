package cn.zss.blog.Inteceptor;

import cn.zss.blog.entity.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    //博主账号密码有待添加进user表
    private static String username = "zss";
    private static String password = "654321";


    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //这个拦截器可以让没有登陆的用户无法访问某些页面、
        if (hostHolder.getUser() == null) {
            //实现重定向
            httpServletResponse.sendRedirect("/?pop=1");
            return false;
        }
        else if (hostHolder.getUser().getName().equals(username) && hostHolder.getUser().getPassword().equals(password)) {
            //验证时管理员，或者说博主，页面重定向到后台，这方面可以有扩展的可能。
            System.out.println("访问后台API");
            httpServletResponse.sendRedirect("/admin");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
