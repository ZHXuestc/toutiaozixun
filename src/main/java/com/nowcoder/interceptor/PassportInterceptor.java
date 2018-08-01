package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by nowcoder on 2018/3/3.
 * 该是用来验证用户是否已经登录
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {
    //因为要根据数据库表中的ticket查询用户的详细信息

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    //在执行controller之前要先进入拦截器
    //preHandle方法如果返回false，则请求结束，不在执行其他两个方法
    //preHandle方法如果返回true，后面才会进行处理
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //先从cookie中获取ticket
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                //如果cookie不为空，就可以遍历cookies（键和值），找到名字为“ticket”的值
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();//得到了cookie中的ticket
                    break;//找到即跳出循环往下执行
                }
            }
        }

        //这个ticket可能是认为的加入到cookie中，所以还要从数据中获取ticket进一判断
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            //Date.before(Date when) 测试此日期是否在指定日期之前。
            //如果ticket为空或者ticket的过期时间已经过期，或者状态是未登陆状态，name就什么都不做直接返回true
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            //通过上面的判断就可以知道ticket是存在的，那么就可以得到这次请求的用户的详细信息并保存到线程里面
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //在处理结束后需要进行判断,hostHolder.getUser()表示从线程得到用户，也就是说用户是登录状态
        //通过modelAndView可以把线程中的user设置到页面，这样就可以保证前端所有页面都可以访问user用户的信息
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        //每次请求结束后都要把这个用户清理掉，或者会对线程造成问题
        hostHolder.clear();
    }
}
