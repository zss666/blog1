package cn.zss.blog.cotroller;

import cn.zss.blog.async.EventModel;
import cn.zss.blog.async.EventProducer;
import cn.zss.blog.async.EventType;
import cn.zss.blog.service.UserService;
import cn.zss.blog.util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "/index")
public class LoginRegistryController {
    //登录。注册
    private static final Logger logger = LoggerFactory.getLogger(LoginRegistryController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/register/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("email") String email,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            //输入用户名，密码，邮箱注册用户
            Map<String, Object> map = userService.register(username, password,email);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    //保存５天
                    cookie.setMaxAge(3600*24*5);
                }
                //加入用户信息
                response.addCookie(cookie);
                return JSONUtil.getJSONString(0, "注册成功");
            } else {
                //注册失败，返回注册信息
                return JSONUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return JSONUtil.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                //通知后台用户登录,并且把用户邮箱也发过去
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .set("username", username).set("email",userService.getUserByUserName(username).getEmail()));
                return JSONUtil.getJSONString(0, "登录成功");
            } else {
                //登录失败，返回登录信息
                return JSONUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return JSONUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
