package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rainday on 16/6/30.
 */

//这个类是用来管理要在页面上显示的类，相当于是你要在页面上进行显示的数据可以把包含该数据的对象放入到这个类当中，
// 然后在页面上直接通过类似于ognl表达式的方式获取这个数据的值，这样更便于管理在页面上显示的属性
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
