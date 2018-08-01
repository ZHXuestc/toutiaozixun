package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder on 2018/3/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    //业务层注册方法，并对用户名和密码进行校验
    public Map<String, Object> register(String username, String password) {
        //map用来存放用户名和密码，还有用户名和密码的校验信息，当LoginController调用service层
        // 中的该方法时就可以把相关的校验信息的合法性返回
        Map<String, Object> map = new HashMap<String, Object>();
        //对用户名进行校验
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        //对密码名进行校验
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        //校验数据库中用户名是否已经存在
        User user = userDAO.selectByName(username);

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        // 对密码进行加密操作，密码强度
        user = new User();
        user.setName(username);
        //在生成的随机码中截取前六位
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        //注册成功会随机产生一个头像
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        // //注册完成就应该进行登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }


    //登录的方法
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    //向数据库中插入数据并返回ticket的方法
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();//得到当前时间
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);//把相关属性都设置到login_ticket表中
        return ticket.getTicket();//添加表之后要把表中的ticket返回给方法调用者
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //退出方法,ticket=1表示过期
    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
