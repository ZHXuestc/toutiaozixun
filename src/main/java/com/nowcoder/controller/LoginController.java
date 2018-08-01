package com.nowcoder.controller;

import com.nowcoder.async.EventProducer;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by nowcoder on 2018/3/2.
 */
@Controller
public class LoginController {
    //Logger 对象用来记录特定系统或应用程序组件的日志消息,用来记录ToutiaoUtils类的日志信息
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    //注入userService
    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      //0代表是默认是登录状态
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.register(username, password);
            //因为注册完成后就立即执行了addLoginTicket方法向数据库添加了数据并得到相应ticket,可以作为判断条件
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                //设置这个cookie作用于前端所有的页面
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*7);//如果发现登录时rememberme大于0，就把ookie生效时间设置为7天
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");//0表示成功
            } else {
                //如果不成功就把相应的信息转成json串显示在页面上
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            //有异常先把异常保存下来
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }
    }

    //登录
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    //@RequestParam表示从页面上获取参数
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            //如果用户名和密码争取的话就会得到ticket，ticket存在map中
            Map<String, Object> map = userService.login(username, password);
            //如果map中包含ticket,则把ticket取出来放在cookie中
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                //设置这个cookie作用于前端所有的页面
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*7);
                }

                //如果没有添加cookie则，无法登陆成功，必须要加response.addCookie(cookie);这段代码
                response.addCookie(cookie);

                //添加事件，发送事件
//                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
//                        .setActorId((int) map.get("userId"))
//                        .setExt("username", username).setExt("email", "zjuyxy@qq.com"));

                return ToutiaoUtil.getJSONString(0, "登录成功");

            } else {
                return ToutiaoUtil.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }

    //退出
    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
