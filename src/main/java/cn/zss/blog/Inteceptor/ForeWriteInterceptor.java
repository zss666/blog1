package cn.zss.blog.Inteceptor;

import cn.zss.blog.entity.SysLog;
import cn.zss.blog.entity.User;
import cn.zss.blog.service.SysLogService;
import cn.zss.blog.service.UserService;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ForeWriteInterceptor implements HandlerInterceptor {
    @Autowired
    SysLogService sysLogService;

    @Autowired
    UserService userService;

    private SysLog sysLog = new SysLog();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        //记录访问的ip和url
        sysLog.setIp(StringUtils.isEmpty(ip)?"0.0.0.0":ip);
        sysLog.setHeadUrl(StringUtils.isEmpty(url)?"获取url失败":url);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 记录访问的操作，如果没有则记录资源
            sysLog.setRemark(method.getName());
            sysLogService.addsysLog(sysLog);

        } else {
            String uri = request.getRequestURI();
            sysLog.setRemark(uri);
            sysLogService.addsysLog(sysLog);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
