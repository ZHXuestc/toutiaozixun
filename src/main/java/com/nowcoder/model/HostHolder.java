package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */

//HostHolder这个类表示当前用户是谁,确定了本次访问的用户是谁

@Component
public class HostHolder {

    //每条用户线程通过set方法设置进来，在get的时候只能获取自己的线程
    //每条线程只能存放一个用户，所以多个用户同时访问时，通过TreadLocal(线程本地变量)就可以得到对应的用户和相应的信息
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    //获取用户
    public User getUser() {
        return users.get();
    }

    //设置用户
    public void setUser(User user) {
        users.set(user);
    }

    //清除用户
    public void clear() {
        users.remove();
    }
}
